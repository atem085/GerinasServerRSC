
package table;

import javax.net.ssl.SSLSocket;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.io.FileInputStream;
import utils.ObjectZipOutputStream;
import java.io.IOException;
import data.*;
import utils.Consts;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class IncomingClientThread extends Thread
{
  protected SSLSocket incomingSocket;
  protected MyObjectOutputStream incomingOut;
  protected MyObjectInputStream incomingIn;
  protected Table selectedTable;
  protected User user;
  public String language;
  protected int counter;
  protected boolean canBeRunning;
  public long[] elementCode;
  public int[] subtableIndex;
  public String tableName;
  public long timeOfCreation;
  private boolean set = false;
  protected ConnectionThread connectionThread;
  private ObjectZipOutputStream dbStream;
  private ObjectZipOutputStream updateStream;
  private Object lock;
  private boolean executing;

  protected IncomingClientThread()
  {
  }

  public IncomingClientThread(SSLSocket incomingSocket, ConnectionThread connectionTd, ObjectZipOutputStream dbStream, ObjectZipOutputStream updateStream, int counter) throws IOException
  {
    this.timeOfCreation = new java.util.Date().getTime();
    this.lock = new Object();
    this.executing = false;
    this.incomingSocket = incomingSocket;
    this.incomingSocket.setTcpNoDelay(true);
//    this.incomingSocket.setKeepAlive(true);
    this.connectionThread = connectionTd;
    this.dbStream = dbStream;
    this.incomingSocket.setSoTimeout(20000);
    this.updateStream = updateStream;
    this.counter = counter;
    this.incomingIn = new MyObjectInputStream(incomingSocket.getInputStream());
//    incomingIn.setEcho(true);
    this.incomingOut = new MyObjectOutputStream(this.incomingSocket.getOutputStream(), false);
//    incomingOut.setEcho(true);
    this.incomingSocket.setSoTimeout(0);
//    this.incomingSocket.setUseClientMode(false);
    this.selectedTable = null;
    this.user = null;
    this.language = null;
    this.canBeRunning = true;
    this.subtableIndex = null;
    this.elementCode = null;
    this.tableName = null;
    this.incomingIn.setEncoding(false);
    this.incomingOut.setEncoding(false);
    super.setPriority(Thread.MAX_PRIORITY);
  }

  public ObjectZipOutputStream getDBStream()
  {
    return this.dbStream;
  }

  public ObjectZipOutputStream getUpdateStream()
  {
    return this.updateStream;
  }

  public void halt() throws IOException
  {
    this.canBeRunning = false;
    this.incomingSocket.close();
  }

  public int getCounter()
  {
    return this.counter;
  }

  public MyObjectOutputStream getOut()
  {
    return this.incomingOut;
  }

  public MyObjectInputStream getIn()
  {
    return this.incomingIn;
  }

  public String getLanguage()
  {
    return this.language;
  }

  public User getUser()
  {
    return this.user;
  }

  public void setUser(User user)
  {
    this.user = user;
    int pos = -1;
    for(int i = 0; i < Data.outgoingChannels.size(); i++)
      if(((OutgoingChannel)Data.outgoingChannels.get(i)).counter == this.counter)
        pos = i;
//    ((OutgoingChannel)Data.outgoingChannels.get(pos)).setEncoding((StaticUser)user);

/*
    this.incomingIn.a = ((StaticUser)user).a;
    this.incomingIn.b = ((StaticUser)user).b;
    this.incomingIn.c = ((StaticUser)user).c;
    this.incomingOut.a = ((StaticUser)user).a;
    this.incomingOut.b = ((StaticUser)user).b;
    this.incomingOut.c = ((StaticUser)user).c;
    this.incomingIn.setEncoding(true);
    this.incomingOut.setEncoding(true);
*/
  }

  public String selectTable(String tableName, String language)
  {
    this.selectedTable = Data.db.getTable(tableName);
    if(this.selectedTable == null)
    {
      return Consts.ermsNoSuchTable(language);
    }
    this.elementCode = null;
    this.subtableIndex = null;
    this.tableName = tableName;
    return "";
  }

  public String selectTable(String tableName, long[] elementCode, int[] subtableIndex)
  {
    try
    {
      this.selectedTable = Data.db.getTable(tableName);
      for (int i = 0; i < elementCode.length; i++)
        this.selectedTable = this.selectedTable.findElement(
            elementCode[i]).getSubtable(subtableIndex[i]);
    }
    catch(Exception e)
    {
      return e.toString();
    }
    this.elementCode = elementCode;
    this.subtableIndex = subtableIndex;
    this.tableName = tableName;
    return "";
  }

  public String subselectTable(long elementCode, int subtableIndex)
  {
    try
    {
      this.selectedTable = this.selectedTable.findElement(elementCode).getSubtable(subtableIndex);
    }
    catch(Exception e)
    {
      return e.toString();
    }
    long[] ec = new long[this.elementCode.length];
    for(int i = 0; i < ec.length; i++)
      ec[i] = this.elementCode[i];
    this.elementCode = new long[ec.length + 1];
    for(int i = 0; i < ec.length; i++)
      this.elementCode[i] = ec[i];
    this.elementCode[this.elementCode.length - 1] = elementCode;
    int[] si = new int[this.subtableIndex.length];
    for(int i = 0; i < si.length; i++)
      si[i] = this.subtableIndex[i];
    this.subtableIndex = new int[si.length + 1];
    for(int i = 0; i < si.length; i++)
      this.subtableIndex[i] = si[i];
    this.subtableIndex[this.subtableIndex.length - 1] = subtableIndex;
    return "";
  }

  public Table getSelectedTable()
  {
    return this.selectedTable;
  }

  public void run()
  {
    int requestType = -1;

//    OutgoingChannel channel = null;

    for(int i = 0; this.canBeRunning; i++)
    {
      if(!set && this.user != null && ((StaticUser)this.user).hasDB)
        this.connectionThread.setUser((StaticUser)this.user);
      try
      {
        requestType = this.incomingIn.readInt();
/*        if(requestType == -1)
        {
          this.incomingOut.writeInt(-1);
          this.incomingOut.flush();
          continue;
        }*/
      }
      catch(IOException e)
      {
        System.out.println("ERROR reading requestType");
        requestType = -1;
        if(this.canBeRunning)
          try
          {
            Data.commander.forceOut(this);
          }
          catch(Exception e1)
          {
            try
            {
              connectionThread.halt();
            }
            catch(Exception ee) {};
          return;
        }
        return;
      }
      if(requestType == -1)
      {
        try
        {
          Data.commander.forceOut(this);
        }
        catch(Exception ee) {}
        return;
      }
      synchronized(this.lock)
      {
        try
        {
          switch(requestType)
          {
            case 0:
              this.getOut().writeInt(0);
              this.getOut().flush();
              continue;
            case 7:
              Command reader = new Command7();
              Command command = reader.read(this.incomingIn, false);
              if((this.getUser() == null || this.getLanguage() == null ||
                  !this.getUser().isLoggedIn))
              {
                try
                {
                  Data.commander.forceOut(this);
                }
                catch(Exception e) {}
                return;
              }
//              System.out.println("Selecting table " + this.tableName + " user " + new String(this.user.name));
              boolean res = command.execute(this, Data.db, this.getLanguage());
//              System.out.println("" + res + " selecting table " + this.tableName + " user " + new String(this.user.name));
              try
              {
                this.incomingOut.writeBoolean(res);
                this.incomingOut.flush();
//                System.out.println("Res written");
              }
              catch(Exception e)
              {
                e.printStackTrace();
//                System.out.println("IncomingOut of user " + new String(this.user.name) + " reset");
              }
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
              }
              continue;
            case 8:
              reader = new Command8();
              command = reader.read(this.incomingIn, false);
              if((this.getUser() == null || this.getLanguage() == null ||
                  !this.getUser().isLoggedIn))
              {
                try
                {
                  Data.commander.forceOut(this);
                }
                catch(Exception e) {}
                return;
              }
              res = command.execute(this, Data.db, this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
              }
              continue;
            case 19:
              reader = new Command19();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {
                    if(((Command19)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientFile);
                    else
                      fin = new FileInputStream(Data.clientFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 20:
              reader = new Command20();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {
                    if(((Command20)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientCustomerFile);
                    else
                      fin = new FileInputStream(Data.clientCustomerFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 21:
              reader = new Command21();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {

                    if(((Command21)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientFile);
                    else
                      fin = new FileInputStream(Data.clientFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 22:
              reader = new Command22();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {
                    if(((Command22)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientCustomerFile);
                    else
                      fin = new FileInputStream(Data.clientCustomerFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 28:
              reader = new Command28();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {
                    if(((Command28)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientBuyerFile);
                    else
                      fin = new FileInputStream(Data.clientBuyerFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 29:
              reader = new Command29();
              command = reader.read(this.incomingIn, false);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
                if(command.getErrorMessage().equals(Consts.ermsWrongVersion(this.getLanguage())))
                {
                  FileInputStream fin = null;
                  try
                  {
                    if(((Command29)command).getBits().equals("32"))
                      fin = new FileInputStream(Data.clientBuyerFile);
                    else
                      fin = new FileInputStream(Data.clientBuyerFile64);
                  }
                  catch(Exception e)
                  {
                    e.printStackTrace();
                  }
                  if(fin != null)
                  {
                    try
                    {
                      this.updateStream.prepare();
                      int length = fin.available();
                      this.updateStream.writeInt(length);
                      this.updateStream.flush();
                      for(int j = 0; j < length; j++)
                        this.updateStream.write(fin.read());
                      this.updateStream.flush();
                      this.updateStream.close(false);
                    }
                    catch(Exception e)
                    {
                      e.printStackTrace();
                    }
                  }
                }
                Data.commander.forceOut(this);
                return;
              }
              else
              {
                  try
                  {
                    command.log(this, Data.db.path, "en");
                  }
                  catch(Exception e) {}
                for(int j = 0; j < Data.outgoingChannels.size(); j++)
                {
                  OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                  if(ch.counter == this.getCounter())
                    continue;
                  boolean ok = true;
//                  synchronized(Data.lock)
//                  {
                    try
                    {
                      command.write(this, ch, true);
                    }
                    catch(Exception e)
                    {
                      ok = false;
                    }
//                  }
                  if(!ok)
                    try
                    {
                      Data.commander.forceOut(ch);
                      if(j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                        j--;
                    }
                    catch(Exception ee) {}
                }
                Table t = Data.db.getTable(Consts.tbUsers);
                StaticUser us;
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              continue;
            case 33:
              reader = new Command33();
              command = reader.read(this.incomingIn, false);
              if((this.getUser() == null || this.getLanguage() == null ||
                  !this.getUser().isLoggedIn))
              {
                try
                {
                  Data.commander.forceOut(this);
                }
                catch(Exception e) {}
                return;
              }
              res = command.execute(this, Data.db, this.getLanguage());
              this.incomingOut.writeBoolean(res);
              this.incomingOut.flush();
              if(!res)
              {
                this.incomingOut.writeUTF(command.getErrorMessage());
                this.incomingOut.flush();
              }
              else
              {
                try
                {
                  command.write(this, this.getOut(), true);
                }
                catch(Exception e)
                {
                  e.printStackTrace();
                }
              }
              continue;
            case 35:
              reader = new Command35();
              command = reader.read(this.incomingIn, false);
              if((this.getUser() == null || this.getLanguage() == null ||
                  !this.getUser().isLoggedIn))
              {
                try
                {
                  Data.commander.forceOut(this);
                }
                catch(Exception e) {}
                return;
              }
              res = command.execute(this, Data.db, this.getLanguage());
              command.log(this, Data.db.path, "en");
              continue;
            case 36:
              reader = new Command36();
              command = reader.read(this.incomingIn, false);
              if((this.getUser() == null || this.getLanguage() == null ||
                  !this.getUser().isLoggedIn))
              {
                try
                {
                  Data.commander.forceOut(this);
                }
                catch(Exception e) {}
                return;
              }
              res = command.execute(this, Data.db, this.getLanguage());
              command.log(this, Data.db.path, "en");
              continue;
            case 50:
              try
              {
                String path = this.incomingIn.readUTF();
                File pf = new File(path.substring(0, path.lastIndexOf("\\")));
                if(!pf.exists())
                  pf.mkdir();
                pf = new File(path);
                if(!pf.exists())
                  pf.mkdir();
                int n = this.incomingIn.readInt();
                File out;
                File[] files = pf.listFiles();
                for(int fk = 0; fk < files.length; fk++)
                  files[fk].delete();
                System.out.println(path);
                for (int m = 0; m < n; m++)
                {
                  long length = this.incomingIn.readLong();
                  out = new File(path + "\\img_" + m + ".gif");
                  FileOutputStream fout = new FileOutputStream(out);
                  for(int ll = 0; ll < length; ll++)
                    fout.write(this.incomingIn.read());
                  fout.close();
                }
                System.out.println("finished");
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
              continue;
            case 51:
              try
              {
                String path = this.incomingIn.readUTF();
                File f = new File(path);
                if (!f.exists())
                {
                  this.incomingOut.writeInt(0);
                  this.incomingOut.flush();
                  continue;
                }
                File[] files = f.listFiles();
                this.incomingOut.writeInt(files.length);
                File cf;
                for (int m = 0; m < files.length; m++)
                {
                  cf = new File(path + "\\img_" + m + ".gif");
                  FileInputStream fin = new FileInputStream(cf);
                  this.incomingOut.writeLong(cf.length());
                  while (fin.available() > 0)
                    this.incomingOut.write(fin.read());
                  fin.close();
                }
                this.incomingOut.flush();
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
              continue;
            case 52:
              try
              {
                long elc = this.incomingIn.readLong();
                String path = this.incomingIn.readUTF();
                File pf = new File(path.substring(0, path.lastIndexOf("\\")));
                if(!pf.exists())
                  pf.mkdir();
                pf = new File(path);
                if(!pf.exists())
                  pf.mkdir();
                int n = this.incomingIn.readInt();
                File out;
                System.out.println(path);
                for (int m = 0; m < n; m++)
                {
                  String filename = this.incomingIn.readUTF();
                  long length = this.incomingIn.readLong();
                  out = new File(path + "\\" + filename);
                  if(out.exists()) out.delete();
                  FileOutputStream fout = new FileOutputStream(out);
                  int al = 1024;
                  if(length < al)
                    al = (int)length;
                  int parts = (int)(length / (long)al);
                  int rest = (int)(length - (long)parts * al);
                  byte[] b = new byte[al];
                  for(int ll = 0; ll < parts; ll++)
                  {
                    for(int kl = 0; kl < al; kl++)
                      b[kl] = (byte)this.incomingIn.read();
                    fout.write(b);
                  }
                  if(rest > 0)
                    for(int ll = 0; ll < rest; ll++)
                      fout.write(this.incomingIn.read());
//                  for(int ll = 0, bl = 0; ll < length; ll++, bl++)
//                    fout.write(this.incomingIn.read());
                  fout.close();
                }
                if(n == 0)
                {
                  File[] lpf = pf.listFiles();
                  for(int m = 0; m < lpf.length; m++)
                    lpf[m].delete();
                  System.out.println("Deleted files");
                  pf.delete();
                  System.out.println("Deleted directory");
//                  this.incomingIn.setEcho(true);
//                  this.incomingOut.setEcho(true);
                }
                System.out.println("finished");
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
              continue;
            case 53:
              try
              {
                String path = this.incomingIn.readUTF();
                File f = new File(path);
                if (!f.exists())
                {
                  this.incomingOut.writeInt(0);
                  this.incomingOut.flush();
                  continue;
                }
                File[] files = f.listFiles();
                this.incomingOut.writeInt(files.length);
                File cf;
                for (int m = 0; m < files.length; m++)
                {
                  cf = files[m];
                  FileInputStream fin = new FileInputStream(cf);
                  this.incomingOut.writeUTF(cf.getName());
                  this.incomingOut.writeLong(cf.length());
                  while (fin.available() > 0)
                    this.incomingOut.write(fin.read());
                  fin.close();
                }
                this.incomingOut.flush();
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
              continue;
            case 54:
              try
              {
                String path = this.incomingIn.readUTF();
                File f = new File(path);
                if (!f.exists())
                {
                  this.incomingOut.writeInt(0);
                  this.incomingOut.flush();
                  continue;
                }
                File[] files = f.listFiles();
                this.incomingOut.writeInt(files.length);
                File cf = new File(path + "\\img_" + 0 + ".gif");
                this.incomingOut.writeBoolean(cf.exists());
                this.incomingOut.flush();
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
              continue;
          }
          Data.commander.readAndExecute(this, this.incomingIn, this.incomingOut,
                                        requestType, true);
        }
        catch(Exception e)
        {
          try
          {
            Data.commander.forceOut(this);
          }
          catch(Exception eee)
          {
            try
            {
              this.connectionThread.halt();
            }
            catch(Exception eeee) {}
          }
        }
      }
    }
  }

}

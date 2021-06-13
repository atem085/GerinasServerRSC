package table;

import javax.net.ssl.SSLSocket;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.io.IOException;
import data.*;
import utils.Consts;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HttpClientThread extends Thread
{
  protected SSLSocket httpSocket;
  protected MyObjectOutputStream out;
  protected MyObjectInputStream in;
  protected Table selectedTable;
  protected User user;
  protected int userType;
  public String language;
  protected int counter;
  protected boolean canBeRunning;
  public long[] elementCode;
  public int[] subtableIndex;
  public String tableName;
  public long timeOfCreation;

  protected HttpClientThread()
  {

  }

  public HttpClientThread(SSLSocket httpSocket, int counter) throws IOException
  {
    this.timeOfCreation = new java.util.Date().getTime();
    this.httpSocket = httpSocket;
    this.counter = counter;
    this.in = new MyObjectInputStream(httpSocket.getInputStream());
//    incomingIn.setEcho(true);
    this.out = new MyObjectOutputStream(this.httpSocket.getOutputStream(), false);
//    incomingOut.setEcho(true);
    this.selectedTable = null;
    this.user = null;
    this.language = null;
    this.canBeRunning = true;
    this.subtableIndex = null;
    this.elementCode = null;
    this.tableName = null;
    this.in.setEncoding(false);
    this.out.setEncoding(false);
    super.setPriority(Thread.MAX_PRIORITY);
  }

  public void halt() throws IOException
  {
    this.canBeRunning = false;
    this.in.close();
    this.out.close();
    this.httpSocket.close();
  }

  public int getCounter()
  {
    return this.counter;
  }

  public MyObjectOutputStream getOut()
  {
    return this.out;
  }

  public MyObjectInputStream getIn()
  {
    return this.in;
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
    HttpCommand command;
    HttpCommand reader;
    for(int i = 0; this.canBeRunning; i++)
    {
      try
      {
        requestType = this.in.readInt();
      }
      catch(IOException e)
      {
        requestType = -1;
      }
      try
      {
        switch(requestType)
        {
          case 3 :
            reader = new HttpCommand219();
            command = reader.read(this.in);
            boolean res = command.execute(this, Data.db, this.language);
            this.out.writeBoolean(res);
            this.out.flush();
            if(!res)
            {
              this.out.writeUTF(command.getErrorMessage());
              this.out.flush();
            }
            else
            {
              command.log(this, Data.db.path, "en");
              for(int j = 0; j < Data.outgoingChannels.size(); j++)
              {
                OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                synchronized(Data.lock)
                {
                try
                {
                    command.write(null, ch);
                }
                catch(Exception e)
                {
                  try
                  {
                    Data.commander.forceOut(ch);
                  j--;
                }
                catch(Exception ee) {}
/*                  if(ch.user != null)
                    ch.user.addCommand(command);
                  else
                  {
                    IncomingClientThread ct = null;
                    for(int k = 0; k < Data.incomingClientThreads.size(); k++)
                    {
                      ct = (IncomingClientThread)Data.incomingClientThreads.get(k);
                      if(ch.counter == ct.getCounter() && ct.getUser() != null)
                        ((StaticUser)ct.getUser()).addCommand(command);
                    }
                  }*/
                }
              }
              }
              Table t = Data.db.getTable(Consts.tbUsers);
              StaticUser us;
              for(int j = 0; j < t.size(); j++)
              {
                us = (StaticUser)t.at(j);
                if(!us.isLoggedIn)
                  us.addCommand(command);
              }
              if(Data.guestInvolved)
              {
                t = Data.db.getTable(Consts.tbGuestUsers);
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              if(Data.guestBuyerInvolved)
              {
                t = Data.db.getTable(Consts.tbGuestBuyerUsers);
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
            }
            continue;
          case 4:
            reader = new HttpCommand223();
            command = reader.read(in);
            if((this.getUser() == null || this.getLanguage() == null || !this.getUser().isLoggedIn))
            {
              return;
            }
//            this.readAndSaveSettings(in, incomingClientThread);
              res = command.execute(this, Data.db,
                                    this.getLanguage());
              command.log(this, Data.db.path, "en");
              for(int j = 0; j < Data.outgoingChannels.size(); j++)
              {
                OutgoingChannel ch = (OutgoingChannel)Data.outgoingChannels.get(j);
                try
                {
                  command.write(this, ch);
                }
                catch(Exception e)
                {
                  Data.commander.forceOut(ch);
                  j--;
/*                  if(ch.user != null)
                    ch.user.addCommand(command);
                  else
                  {
                    IncomingClientThread ct = null;
                    for(int k = 0; k < Data.incomingClientThreads.size(); k++)
                    {
                      ct = (IncomingClientThread)Data.incomingClientThreads.get(k);
                      if(ch.counter == ct.getCounter() && ct.getUser() != null)
                        ((StaticUser)ct.getUser()).addCommand(command);
                    }
                  }*/
                }
              }
              Table t = Data.db.getTable(Consts.tbUsers);
              StaticUser us;
              for(int j = 0; j < t.size(); j++)
              {
                us = (StaticUser)t.at(j);
                if(!us.isLoggedIn)
                  us.addCommand(command);
              }
              if(Data.guestInvolved)
              {
                t = Data.db.getTable(Consts.tbGuestUsers);
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
              if(Data.guestBuyerInvolved)
              {
                t = Data.db.getTable(Consts.tbGuestBuyerUsers);
                for(int j = 0; j < t.size(); j++)
                {
                  us = (StaticUser)t.at(j);
                  if(!us.isLoggedIn)
                    us.addCommand(command);
                }
              }
            continue;

        }
      }
      catch(Exception e)
      {
        break;
      }
    }
  }

}

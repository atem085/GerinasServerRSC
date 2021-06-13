package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import data.*;
import utils.Consts;

public class Commander
{
  public Commander()
  {
  }

  public /*synchronized*/ void readAndExecute(IncomingClientThread incomingClientThread, MyObjectInputStream in, MyObjectOutputStream out, int commandType, boolean user)
  {
    Command command = null;
    Command reader = null;

    try
    {
      if(incomingClientThread != null)
        incomingClientThread.connectionThread.incomingSocket.setSoTimeout(60000);
    }
    catch(Exception exx)
    {
      exx.printStackTrace();
    }
    try
    {
      switch (commandType)
      {
        case 1:
          reader = new Command1();
          this.doit(incomingClientThread, command, reader, out, in, user, 1);
          break;
        case 2:
          reader = new Command2();
          this.doit(incomingClientThread, command, reader, out, in, user, 2);
          break;
        case 3:
          reader = new Command3();
          this.doit(incomingClientThread, command, reader, out, in, user, 3);
          break;
        case 4:
          reader = new Command4();
          this.doit(incomingClientThread, command, reader, out, in, user, 4);
          break;
        case 5:
          reader = new Command5();
          this.doit(incomingClientThread, command, reader, out, in, user, 5);
          break;
        case 6:
          reader = new Command6();
          this.doit(incomingClientThread, command, reader, out, in, user, 6);
          break;
        case 9:
          reader = new Command9();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 10:
          reader = new Command10();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 11:
          reader = new Command11();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 12:
          reader = new Command12();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 13:
          reader = new Command13();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 14:
          reader = new Command14();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 15:
          reader = new Command15();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 16:
          reader = new Command16();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 17:
          reader = new Command17();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 18:
          reader = new Command18();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 23:
          boolean res;
          if ( (incomingClientThread.getUser() == null || incomingClientThread.getLanguage() == null ||
                !incomingClientThread.getUser().isLoggedIn))
          {
            try
            {
              Data.commander.forceOut(incomingClientThread);
            }
            catch(Exception e) {}
            break;
          }
          synchronized(Data.lock)
          {
            try
            {
              reader = new Command23();
              command = reader.read(in, false);
              incomingClientThread.getUser().isLoggedIn = false;
              this.readAndSaveSettings(in, incomingClientThread);
              out.writeLong(Data.version);
              out.flush();
              res = command.execute(incomingClientThread, Data.db,
                                    incomingClientThread.getLanguage());
            }
            catch(Exception e)
            {
              e.printStackTrace();
            }
          }
          command.log(incomingClientThread, Data.db.path, "en");
          for (int j = 0; j < Data.outgoingChannels.size(); j++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(j);
            if (ch.counter == incomingClientThread.getCounter())
              continue;
            boolean ok = true;
            synchronized(Data.lock)
            {
              try
              {
                command.write(incomingClientThread, ch, true);
              }
              catch (Exception e)
              {
                ok = false;
              }
            }
            if(!ok)
            {
              this.forceOut(ch);
              if (j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                break;
//                j--;
            }
          }
          Table t = Data.db.getTable(Consts.tbUsers);
          StaticUser us;
          for (int j = 0; j < t.size(); j++)
          {
            us = (StaticUser) t.at(j);
            if (!us.isLoggedIn || incomingClientThread.user == us)
              us.addCommand(command);
          }
          break;
        case 24:
          if ( (incomingClientThread.getUser() == null || incomingClientThread.getLanguage() == null ||
                !incomingClientThread.getUser().isLoggedIn))
          {
            try
            {
              Data.commander.forceOut(incomingClientThread);
            }
            catch(Exception e) {}
            break;
          }
          synchronized(Data.lock)
          {
            try
            {
              reader = new Command24();
              command = reader.read(in, false);
              incomingClientThread.getUser().isLoggedIn = false;
              this.readAndSaveSettings(in, incomingClientThread);
              out.writeLong(Data.version);
              out.flush();
              res = command.execute(incomingClientThread, Data.db, incomingClientThread.getLanguage());
            }
            catch(Exception e)
            {
              e.printStackTrace();
            }
          }
          command.log(incomingClientThread, Data.db.path, "en");
          for (int j = 0; j < Data.outgoingChannels.size(); j++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(j);
            if (ch.counter == incomingClientThread.getCounter())
              continue;
            boolean ok = true;
            synchronized(Data.lock)
            {
              try
              {
                command.write(incomingClientThread, ch, true);
              }
              catch (Exception e)
              {
                ok = false;
              }
            }
            if(!ok)
              this.forceOut(ch);
              if (j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                break;
//                j--;
          }
          t = Data.db.getTable(Consts.tbUsers);
          for (int j = 0; j < t.size(); j++)
          {
            us = (StaticUser) t.at(j);
            if (!us.isLoggedIn || incomingClientThread.user == us)
              us.addCommand(command);
          }
          break;
        case 25:
          reader = new Command25();
          command = reader.read(in, false);
          if(user && (incomingClientThread.getUser() == null || incomingClientThread.getLanguage() == null || !incomingClientThread.getUser().isLoggedIn))
          {
            try
            {
              this.forceOut(incomingClientThread);
            }
            catch(Exception e) {}
            break;
          }
          if(user)
          {
            res = command.execute(incomingClientThread, Data.db,
                                  incomingClientThread.getLanguage());
            out.writeBoolean(res);
            out.flush();
          }
          else
            res = command.execute(null, Data.db, "en");
          if(user && !res)
          {
            out.writeUTF(command.getErrorMessage());
            out.flush();
          }
          if(user && res)
            command.log(incomingClientThread, Data.db.path, "en");
          if(res)
          {
            File f = new File(Data.db.path + "\\log.dat");
            if(f.exists()) f.delete();
          }
          break;
        case 26:
          reader = new Command26(0);
          command = reader.read(in, false);
          if((incomingClientThread.getUser() == null || incomingClientThread.getLanguage() == null || !incomingClientThread.getUser().isLoggedIn))
          {
            try
            {
              Data.commander.forceOut(incomingClientThread);
            }
            catch(Exception e) {}
            break;
          }
            res = command.execute(incomingClientThread, Data.db,
                                  incomingClientThread.getLanguage());
            out.writeBoolean(res);
            out.flush();
            if(res)
              command.log(incomingClientThread, Data.db.path, "en");
          break;
        case 27:
          if ( (incomingClientThread.getUser() == null || incomingClientThread.getLanguage() == null ||
                !incomingClientThread.getUser().isLoggedIn))
          {
            try
            {
              Data.commander.forceOut(incomingClientThread);
            }
            catch(Exception e) {}
            break;
          }
          synchronized(Data.lock)
          {
            try
            {
              reader = new Command27();
              command = reader.read(in, false);
              incomingClientThread.getUser().isLoggedIn = false;
              this.readAndSaveSettings(in, incomingClientThread);
              out.writeLong(Data.version);
              out.flush();
              res = command.execute(incomingClientThread, Data.db, incomingClientThread.getLanguage());
            }
            catch(Exception e)
            {
              e.printStackTrace();
            }
          }
          command.log(incomingClientThread, Data.db.path, "en");
          for (int j = 0; j < Data.outgoingChannels.size(); j++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(j);
            if (ch.counter == incomingClientThread.getCounter())
              continue;
            boolean ok = true;
            synchronized(Data.lock)
            {
              try
              {
                command.write(incomingClientThread, ch, true);
              }
              catch (Exception e)
              {
                ok = false;
              }
            }
            if(!ok)
              this.forceOut(ch);
              if (j < Data.outgoingChannels.size() && Data.outgoingChannels.get(j) != ch)
                j--;
          }
          t = Data.db.getTable(Consts.tbUsers);
          for (int j = 0; j < t.size(); j++)
          {
            us = (StaticUser) t.at(j);
            if (!us.isLoggedIn || incomingClientThread.user == us)
              us.addCommand(command);
          }
          break;
        case 30:
          reader = new Command30();
          this.doit(incomingClientThread, command, reader, out, in, user, 30);
          break;
        case 31:
          reader = new Command31();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
        case 32:
          reader = new Command32();
          this.doit(incomingClientThread, command, reader, out, in, user);
          break;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    try
    {
      if(incomingClientThread != null)
        incomingClientThread.connectionThread.incomingSocket.setSoTimeout(20000);
    }
    catch(Exception exx)
    {
      exx.printStackTrace();
    }
  }

  public boolean forceOut(IncomingClientThread incomingClientThread)
  {
    System.out.println("Trying to force user from incoming thread " + incomingClientThread.counter + " out ");
    int pos = -1;
    OutgoingChannel ch = null;
    for(int i = 0; i < Data.incomingClientThreads.size(); i++)
      if(((IncomingClientThread)Data.incomingClientThreads.get(i)).getCounter() == incomingClientThread.getCounter())
        pos = i;
    if(pos > -1 && pos < Data.incomingClientThreads.size())
    {
      long currentTime = new java.util.Date().getTime();
//      if(currentTime - ((IncomingClientThread)Data.incomingClientThreads.get(pos)).timeOfCreation > 3000)
//      {
        Data.incomingClientThreads.remove(pos);
        ch = (OutgoingChannel)Data.outgoingChannels.remove(pos);
/*      }
      else
      {
        System.out.println("Thread is younger than 3 seconds.  User will stay online");
        return false;
      }*/
    }
    try
    {
      incomingClientThread.halt();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Incoming thread stopped");
    if(ch != null)
      try
      {
        ch.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    System.out.println("Outgoing channel closed");
    if(!this.isUserActive(incomingClientThread.user, incomingClientThread.counter))
    {
      if(incomingClientThread.getUser() != null)
        incomingClientThread.getUser().isLoggedIn = false;
      else if(ch != null && ch.getUser() != null)
        ch.getUser().isLoggedIn = false;
    }
    else
      System.out.println("User is already active somewhere else");
    if(ch != null)
      System.out.println("User " + ch.counter + " has been succsessfully forced out");
    else
      System.out.println("Outgoing channel was not found");
    return true;
  }

  public boolean forceOut(OutgoingChannel outgoingChannel)
  {
      System.out.println("Trying to force user from outgoing channel " + outgoingChannel.counter + " out");
      int pos = -1;
      IncomingClientThread it = null;
      for (int i = 0; i < Data.outgoingChannels.size(); i++)
        if ( ( (OutgoingChannel) Data.outgoingChannels.get(i)).counter == outgoingChannel.counter)
          pos = i;
      if(Data.incomingClientThreads.size() != Data.outgoingChannels.size())
        System.out.println("SIZE outgoingChannel != incomingClientThread " + Data.outgoingChannels.size() + " " + Data.incomingClientThreads.size());
      if (pos > -1 && pos < Data.incomingClientThreads.size())
      {
        long currentTime = new java.util.Date().getTime();
        if (currentTime - ( (IncomingClientThread) Data.incomingClientThreads.get(pos)).timeOfCreation > 3000)
        {
          it = (IncomingClientThread) Data.incomingClientThreads.remove(pos);
          System.out.println("IncomingClientThread removed");
          Data.outgoingChannels.remove(pos);
          System.out.println("OutgoingChannel removed");
        }
        else
        {
          System.out.println("Incoming thread is younger than 3 seconds.  User will stay online");
          return false;
        }
      }
      try
      {
        it.halt();
      }
      catch (Exception e)
      {
//      e.printStackTrace();
      }
      System.out.println("Incoming thread stoped");
      try
      {
        outgoingChannel.close();
//        outgoingChannel.outgoingOut.close();
      }
      catch (Exception e)
      {
//      e.printStackTrace();
      }
      System.out.println("Outgoing channel closed");
      try
      {
        Data.outgoingChannels.remove(outgoingChannel);
        System.out.println("Outgoing channel removed");
      }
      catch (Exception ee)
      {
        System.out.println("Failed to remove outgoingchannel");
        ee.printStackTrace();
      }
      try
      {
        Data.incomingClientThreads.remove(it);
        System.out.println("IncomingClientThread removed");
      }
      catch (Exception ee)
      {
        System.out.println("Failed to remove incomingClientThread");
        ee.printStackTrace();
      }
      if (!this.isUserActive(it.user, it.counter))
      {
        if (it.getUser() != null)
          it.getUser().isLoggedIn = false;
        else if (outgoingChannel.getUser() != null)
          outgoingChannel.getUser().isLoggedIn = false;
      }
      else
        System.out.println("User is already active somewhere else");
      if (it == null)
        System.out.println("Incoming thread was not found");
      else
        System.out.println("User " + it.counter + " has been succsessfully forced out");
    return true;
  }

  private boolean isUserActive(User user, int counter)
  {
    IncomingClientThread it = null;
    for(int i = 0; i < Data.incomingClientThreads.size(); i++)
    {
      it = (IncomingClientThread)Data.incomingClientThreads.get(i);
      if(it.counter != counter && it.user == user)
        return true;
    }
    return false;
  }

  public boolean forceOut(ConnectionThread connectionThread)
  {
    System.out.println("Trying to force user  from connection thread " + connectionThread.getCounter() + " out");
    IncomingClientThread incomingClientThread = null;
    OutgoingChannel ch = null;
    int pos = -1;
    for(int i = 0; i < Data.incomingClientThreads.size(); i++)
      if(((IncomingClientThread)Data.incomingClientThreads.get(i)).getCounter() == connectionThread.getCounter())
        pos = i;
    if(pos > -1 && pos < Data.incomingClientThreads.size())
    {
      long currentTime = new java.util.Date().getTime();
      if(currentTime - ((IncomingClientThread)Data.incomingClientThreads.get(pos)).timeOfCreation > 3000)
      {
        incomingClientThread = (IncomingClientThread)Data.incomingClientThreads.remove(pos);
        ch = (OutgoingChannel)Data.outgoingChannels.remove(pos);
        if(ch == null)
          System.out.println("Outgoing channel null");
      }
      else
       {
         System.out.println("Connection thread is younger than 3 seconds.  User will stay online");
         return false;
       }
      try
      {
        incomingClientThread.halt();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      System.out.println("Incoming thread stopped");
      try
      {
        ch.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      System.out.println("Outgoing channel closed");
      if(!this.isUserActive(incomingClientThread.user, incomingClientThread.counter))
      {
        System.out.println("Logging out");
        if(incomingClientThread.getUser() != null)
          incomingClientThread.getUser().isLoggedIn = false;
        else if(ch != null || ch.getUser() != null)
        {
          System.out.println("Incoming user not null " + ch);
          ch.getUser().isLoggedIn = false;
        }
        else
          System.out.println("Outgoing user not null");
      }
      else
        System.out.println("User is already active somewhere else");
    }
    else
      System.out.println("Outgoing channel not found");
    if(incomingClientThread != null)
      System.out.println("User " + incomingClientThread.counter + " has been succsessfully forced out " + new String(incomingClientThread.getUser().name));
    else
      System.out.println("IncomingClientThread null");
    return true;
  }

//  int ccc = 0;

  private void doit(IncomingClientThread incomingClientThread, Command command, Command reader, MyObjectOutputStream out, MyObjectInputStream in, boolean user) throws IOException
  {
    synchronized(Data.lock)
    {
      try
      {
        command = reader.read(in, false);
        if (user &&
            (incomingClientThread.getUser() == null ||
             incomingClientThread.getLanguage() == null ||
             !incomingClientThread.getUser().isLoggedIn))
        {
          try
          {
            this.forceOut(incomingClientThread);
          }
          catch(Exception e) {}
          return;
        }
/*        if(!(command instanceof Command31 || command instanceof Command9)) return;
        if(command instanceof Command31 && !((Command31)command).tableName.equals(Consts.tbfinalInvoicesA)) return;
        if(command instanceof Command9 && !((Command9)command).tableName.equals(Consts.tbfinalInvoicesA)) return;
        ccc++;
        if(ccc < 21) return;*/
        boolean res = false;
        if (user)
          res = command.execute(incomingClientThread, Data.db,
                                incomingClientThread.getLanguage());
        else
        {
          if(Data.version >= command.getVersionAfterExecuting()) return;
          res = command.execute(null, Data.db, "en");
        }
        if (user)
        {
          out.writeBoolean(res);
          out.writeObject(command.getTimeStamp());
          out.writeUTF(command.getErrorMessage());
          out.flush();
        }
        if (user && res)
        {
          command.log(incomingClientThread, Data.db.path, "en");
          for (int i = 0; i < Data.outgoingChannels.size(); i++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(i);
            if (ch.counter == incomingClientThread.getCounter())
              continue;
            try
            {
              command.write(incomingClientThread, ch, true);
              Data.cat.outgoingChannels.add(ch);
              Data.cat.commands.add(command);
//              Data.cat.users.add(incomingClientThread.getUser());
            }
            catch (Exception e)
            {
              e.printStackTrace();
              if (ch.getUser() != null)
                ch.getUser().addCommand(command);
              else
              {
                IncomingClientThread ct = null;
                for (int k = 0; k < Data.incomingClientThreads.size(); k++)
                {
                  ct = (IncomingClientThread) Data.incomingClientThreads.get(k);
                  if (ch.counter == ct.getCounter() && ct.getUser() != null)
                    ( (StaticUser) ct.getUser()).addCommand(command);
                  if (ch.counter == ct.getCounter())
                  {
                    Data.incomingClientThreads.remove(k);
                    break;
                  }
                }
                if (ct != null)
                  for (int k = 0; k < Data.outgoingChannels.size(); k++)
                    if ( ( (OutgoingChannel) Data.outgoingChannels.get(k)).counter == ct.getCounter())
                    {
                      Data.outgoingChannels.remove(k);
                      break;
                    }
              }
              continue;
            }
          }
          Table t = Data.db.getTable(Consts.tbUsers);
          StaticUser us;
          for (int i = 0; i < t.size(); i++)
          {
            us = (StaticUser) t.at(i);
            if (!us.isLoggedIn || incomingClientThread.user == us)
              us.addCommand(command);
          }
        }
      }
      catch(Exception e) {}
    }
  }

  private void log(Command command, String userName, String password, String language, Table selectedTable, long[] elementCode, int[] subtableIndex, String tableName)
  {
    try
    {
      IncomingClientThread ic = new IncomingClientThread();
      ic.user = new DynamicUser(userName, password);
      ic.tableName = tableName;
      ic.selectedTable = selectedTable;
      ic.elementCode = elementCode;
      ic.subtableIndex = subtableIndex;
      command.log(ic, Data.path, language);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void announce(Command command, String userName, String password, String language, boolean log, Table selectedTable, long[] elementCode, int[] subtableIndex, String tableName)
  {
    if(log)
      this.log(command, userName, password, language, selectedTable, elementCode, subtableIndex, tableName);
    synchronized(Data.lock)
    {
//      Data.version++;
      try
      {
        Table t = Data.db.getTable(Consts.tbUsers);
        Table t1 = null;
        Table t2 = null;
        if (t.size() > 0 || (t1 != null && t1.size() > 0) || (t2 != null && t2.size() > 0) ||
            Data.outgoingChannels.size() > 0)
        {
          for (int i = 0; i < Data.outgoingChannels.size(); i++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(i);
            try
            {
              command.write(null, ch, true);
              Data.cat.outgoingChannels.add(ch);
              Data.cat.commands.add(command);
//              Data.cat.users.add(ch.getUser());
            }
            catch (Exception e)
            {
              e.printStackTrace();
              this.forceOut(ch);
              if (i < Data.outgoingChannels.size() && Data.outgoingChannels.get(i) != ch)
                i--;
              continue;
            }
          }
          StaticUser us;
          for (int i = 0; i < t.size(); i++)
          {
            us = (StaticUser) t.at(i);
            if (!us.isLoggedIn)
            {
              us.addCommand(command);
            }
          }
          if (Data.guestInvolved)
            for (int i = 0; i < t1.size(); i++)
            {
              us = (StaticUser) t1.at(i);
              if (!us.isLoggedIn)
                us.addCommand(command);
            }
          if (Data.guestBuyerInvolved)
            for (int i = 0; i < t2.size(); i++)
            {
              us = (StaticUser) t2.at(i);
              if (!us.isLoggedIn)
                us.addCommand(command);
            }
        }
      }
      catch(Exception ee) {ee.printStackTrace();}
    }

  }

  private void doit(IncomingClientThread incomingClientThread, Command command, Command reader, MyObjectOutputStream out, MyObjectInputStream in, boolean user, int requestType) throws IOException
  {
    if (user &&
        (incomingClientThread.getUser() == null ||
         incomingClientThread.getLanguage() == null ||
         !incomingClientThread.getUser().isLoggedIn))
    {
      try
      {
        this.forceOut(incomingClientThread);
      }
      catch(Exception e) {}
      return;
    }
    synchronized(Data.lock)
    {
      try
      {
        command = reader.read(in, false);
        boolean res = false;
        if (user)
          res = command.execute(incomingClientThread, Data.db,
                                incomingClientThread.getLanguage());
        else
          res = command.execute(null, Data.db, "en");
        if (user)
        {
          out.writeBoolean(res);
          out.writeObject(command.getTimeStamp());
          out.writeLong(Data.version);
          if (command.getErrorMessage() == null)
            out.writeUTF("Unknown error message");
          else
            out.writeUTF(command.getErrorMessage());
          out.flush();
        }
        if (user && res)
        {
          command.log(incomingClientThread, Data.db.path, "en");
          Command realCommand = null;
          Table t = Data.db.getTable(Consts.tbUsers);
          Table t1 = null;
          Table t2 = null;
          if (t.size() > 0 || (t1 != null && t1.size() > 0) || (t2 != null && t2.size() > 0) ||
              Data.outgoingChannels.size() > 0)
          {
            switch (requestType)
            {
              case 1:
                Command1 co1 = (Command1) command;
                if (incomingClientThread.subtableIndex == null)
                  realCommand = new Command9(incomingClientThread.tableName,
                                             co1.elementCode, co1.column,
                                             co1.aValue, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co1.date, Data.version);
                else
                  realCommand = new Command10(incomingClientThread.tableName,
                                              incomingClientThread.elementCode,
                                              incomingClientThread.subtableIndex,
                                              co1.elementCode, co1.column,
                                              co1.aValue, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co1.date, Data.version);
                break;
              case 2:
                Command2 co2 = (Command2) command;
                if (incomingClientThread.subtableIndex == null)
                  realCommand = new Command11(incomingClientThread.tableName,
                                              co2.parameters, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co2.getTimeStamp(), Data.version);
                else
                  realCommand = new Command12(incomingClientThread.tableName,
                                              incomingClientThread.elementCode,
                                              incomingClientThread.subtableIndex,
                                              co2.parameters, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co2.getTimeStamp(), Data.version);
                break;
              case 3:
                Command3 co3 = (Command3) command;
                if (incomingClientThread.subtableIndex == null)
                  realCommand = new Command13(incomingClientThread.tableName,
                                              co3.elementCode, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co3.getTimeStamp(), Data.version);
                else
                  realCommand = new Command14(incomingClientThread.tableName,
                                              incomingClientThread.elementCode,
                                              incomingClientThread.subtableIndex,
                                              co3.elementCode, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co3.getTimeStamp(), Data.version);
                break;
              case 4:
                Command4 co4 = (Command4) command;
                realCommand = new Command15(incomingClientThread.tableName,
                                            co4.elementCode, co4.element, co4.amount,
                                            co4.tableNameTo, incomingClientThread.getUser().getValue(4, 1).toString(),
                                            co4.getTimeStamp(), Data.version);
                break;
              case 5:
                Command5 co5 = (Command5) command;
                realCommand = new Command16(incomingClientThread.tableName,
                                            incomingClientThread.elementCode,
                                            incomingClientThread.subtableIndex,
                                            co5.elementCode, co5.amount,
                                            co5.tableNameTo, co5.elementCodeTo,
                                            co5.subtableIndexTo, incomingClientThread.getUser().getValue(4, 1).toString(),
                                            co5.getTimeStamp(), Data.version);
                break;
              case 6:
                Command6 co6 = (Command6) command;
                if (incomingClientThread.subtableIndex == null)
                  realCommand = new Command17(incomingClientThread.tableName,
                                              co6.elementCode, co6.column,
                                              co6.amount, co6.aValue, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co6.getTimeStamp(), Data.version);
                else
                  realCommand = new Command18(incomingClientThread.tableName,
                                              incomingClientThread.elementCode,
                                              incomingClientThread.subtableIndex,
                                              co6.elementCode, co6.column,
                                              co6.amount, co6.aValue, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co6.getTimeStamp(), Data.version);
                break;
              case 30:
                Command30 co30 = (Command30) command;
                if (incomingClientThread.subtableIndex == null)
                  realCommand = new Command31(incomingClientThread.tableName,
                                              co30.parameters, co30.selectedCode,
                                              co30.selectedColumn,
                                              co30.operationIndex, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co30.getTimeStamp(), Data.version);
                else
                  realCommand = new Command32(incomingClientThread.tableName,
                                              incomingClientThread.elementCode,
                                              incomingClientThread.subtableIndex,
                                              co30.parameters, co30.selectedCode,
                                              co30.selectedColumn,
                                              co30.operationIndex, incomingClientThread.getUser().getValue(4, 1).toString(),
                                              co30.getTimeStamp(), Data.version);
                break;
            }
          }
          for (int i = 0; i < Data.outgoingChannels.size(); i++)
          {
            OutgoingChannel ch = (OutgoingChannel) Data.outgoingChannels.get(i);
            if (ch.counter == incomingClientThread.getCounter())
              continue;
            try
            {
              realCommand.write(incomingClientThread, ch.outgoingOut, true);
              Data.cat.outgoingChannels.add(ch);
              Data.cat.commands.add(realCommand);
//              Data.cat.users.add(incomingClientThread.getUser());
            }
            catch (Exception e)
            {
              e.printStackTrace();
              this.forceOut(ch);
              if(i < Data.outgoingChannels.size() && Data.outgoingChannels.get(i) != ch)
                i--;
              continue;
            }
          }
          StaticUser us;
          for (int i = 0; i < t.size(); i++)
          {
            us = (StaticUser) t.at(i);
            if (!us.isLoggedIn || incomingClientThread.user == us)
            {
              us.addCommand(realCommand);
            }
          }
          if (Data.guestInvolved)
            for (int i = 0; i < t1.size(); i++)
            {
              us = (StaticUser) t1.at(i);
              if (!us.isLoggedIn || incomingClientThread.user == us)
                us.addCommand(realCommand);
            }
          if (Data.guestBuyerInvolved)
            for (int i = 0; i < t2.size(); i++)
            {
              us = (StaticUser) t2.at(i);
              if (!us.isLoggedIn || incomingClientThread.user == us)
                us.addCommand(realCommand);
            }
        }
      }
      catch(Exception ee) {}
    }
  }

  public void repareSettings() throws Exception
  {
    File dir = new File(Data.path);
    File[] dirs = dir.listFiles();
    for(int i = 0; i < dirs.length; i++)
    {
      if(!dirs[i].isDirectory()) continue;
      System.out.println(dirs[i].getAbsolutePath());
      File fs = new File(dirs[i].getAbsolutePath() + "\\settings.dat");
      if(!fs.exists()) continue;
      System.out.println("SETTINGS");
      MyObjectInputStream in = new MyObjectInputStream(new FileInputStream(fs));
      in.setEncoding(false);
//      in.setEcho(true);
      File fSettings = new File(dirs[i].getAbsolutePath() + "\\settings.bck");
      MyObjectOutputStream mout = new MyObjectOutputStream(new FileOutputStream(fSettings), false);
      mout.setEncoding(false);
      int tableCount = in.readInt();
      mout.writeInt(tableCount);
      for(int j = 0; j < tableCount; j++)
      {
        try
        {
          this.repareTableSettings(in, mout);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          System.out.println("Could not repare " + dirs[i].getAbsolutePath());
        }
      }
      mout.close();
      in.close();
      fs.delete();
      File fd = new File(dirs[i].getAbsolutePath() + "\\defaults.dat");
      if(!fd.exists()) continue;
      System.out.println("DEFAULTS");
      in = new MyObjectInputStream(new FileInputStream(fd));
      in.setEncoding(false);
      File fDefaults = new File(dirs[i].getAbsolutePath() + "\\defaults.bck");
      mout = new MyObjectOutputStream(new FileOutputStream(fDefaults), false);
      mout.setEncoding(false);
      for(int j = 0; j < tableCount; j++)
        try
        {
          this.repareDefaults(in, mout);
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      mout.close();
      in.close();
      fd.delete();
      fSettings.renameTo(fs);
      fDefaults.renameTo(fd);
      System.out.println("Finished");
      System.out.println();
    }
  }

  private void readAndSaveSettings(MyObjectInputStream in, IncomingClientThread incomingClientThread)
  {
    try
    {
      int userType = 0;
      if(incomingClientThread.user instanceof element.EmployeeUser)
        userType = 0;
      File f = new File(Data.path + "\\" + new String(incomingClientThread.getUser().name) + userType);
      if(!f.exists())
        f.mkdir();
      File fSettings = new File(Data.path + "\\" + new String(incomingClientThread.getUser().name) + userType + "\\settings.bck");
      MyObjectOutputStream mout = new MyObjectOutputStream(new FileOutputStream(fSettings, false), true);
      mout.setEncoding(false);
      int tableCount = in.readInt();
      mout.writeInt(tableCount);
      for(int i = 0; i < tableCount; i++)
        this.readAndSaveTableSettings(in, mout);
      mout.close();
      File fDefaults = new File(Data.path + "\\" + new String(incomingClientThread.getUser().name) + userType + "\\defaults.bck");
      mout = new MyObjectOutputStream(new FileOutputStream(fDefaults, false), true);
      mout.setEncoding(false);
      for(int i = 0; i < tableCount; i++)
        this.readAndSaveDefaults(in, mout);
      mout.close();
      File fS = new File(Data.path + "\\" + new String(incomingClientThread.getUser().name) + userType + "\\settings.dat");
      fS.delete();
      File fD = new File(Data.path + "\\" + new String(incomingClientThread.getUser().name) + userType + "\\defaults.dat");
      fD.delete();
      fSettings.renameTo(fS);
      fDefaults.renameTo(fD);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void repareTableSettings(MyObjectInputStream in, MyObjectOutputStream out) throws Exception
  {
    String tableName = in.readUTF();
    int subtableIndexLength = in.readInt();
    out.writeUTF(tableName);
    out.writeInt(subtableIndexLength);
    for(int i = 0; i < subtableIndexLength; i++)
      out.writeInt(in.readInt());
    int hc = in.readInt();
    out.writeInt(hc);
    for(int i = 0; i < hc; i++)
      out.writeInt(in.readInt());
    int mc = in.readInt();
    out.writeInt(mc);
    for(int i = 0; i < mc; i++)
      out.writeInt(in.readInt());
    for(int i = 0; i < mc; i++)
      out.writeInt(0);
  }

  private void readAndSaveTableSettings(MyObjectInputStream in, MyObjectOutputStream out) throws Exception
  {
    String tableName = in.readUTF();
    int subtableIndexLength = in.readInt();
    out.writeUTF(tableName);
    out.writeInt(subtableIndexLength);
    for(int i = 0; i < subtableIndexLength; i++)
      out.writeInt(in.readInt());
    int hc = in.readInt();
    out.writeInt(hc);
    for(int i = 0; i < hc; i++)
      out.writeInt(in.readInt());
    int mc = in.readInt();
    out.writeInt(mc);
    for(int i = 0; i < mc; i++)
      out.writeInt(in.readInt());
    for(int i = 0; i < mc; i++)
      out.writeInt(in.readInt());
  }

  private void readAndSaveDefaults(MyObjectInputStream in, MyObjectOutputStream out) throws Exception
  {
    int size = in.readInt();
    out.writeInt(size);
    int length;
    for(int i = 0; i < size; i++)
    {
      out.writeUTF(in.readUTF());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      length = in.readInt();
      out.writeInt(length);
      boolean n;
      int ls;
      for(int j = 0; j < length; j++)
      {
        ls = in.readInt();
        out.writeInt(ls);
        for(int k = 0; k < ls; k++)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
      }
    }
  }

  private void repareDefaults(MyObjectInputStream in, MyObjectOutputStream out) throws Exception
  {
    int size = in.readInt();
    out.writeInt(size);
    int length;
    for(int i = 0; i < size; i++)
    {
      out.writeUTF(in.readUTF());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      for(int j = 0; j < length; j++)
        out.writeInt(0);
      length = in.readInt();
      out.writeInt(length);
      boolean n;
      int ls;
      for(int j = 0; j < length; j++)
      {
        ls = in.readInt();
        out.writeInt(ls);
        for(int k = 0; k < ls; k++)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
      }
    }
  }

}
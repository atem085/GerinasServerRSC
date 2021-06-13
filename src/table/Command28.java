package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.util.ArrayList;
import utils.Consts;
import java.io.File;

public class Command28 implements Command
{
  private String errorMessage, password, language, version, javaVersion, bits;
  private long code;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;

  public Command28()
  {
    this.errorMessage = "";
    this.code = 0;
    this.language = null;
    this.password = null;
    this.version = null;
    this.javaVersion = null;
    this.bits = null;
    this.date = null;
  }

  public Command28(long code, String password, String language, String version, String javaVersion, DateRepresentation date, long versionAfterExecuting, String bits)
  {
    this.errorMessage = "";
    this.code = code;
    this.password = password;
    this.language = language;
    this.version = version;
    this.javaVersion = javaVersion;
    this.bits = bits;
    this.bits = bits;
    this.date = date;
    this.versionAfterExecuting = versionAfterExecuting;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(28);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    out.writeUTF(this.bits);
    if(flush)
     out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(28);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    out.writeUTF(this.bits);
    if(flush)
     out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    try
    {
      if(local)
        return new Command28(in.readLong(), in.readUTF(), in.readUTF(), null, null, (DateRepresentation) in.readObject(), in.readLong(), in.readUTF());
      return new Command28(in.readLong(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), (DateRepresentation) in.readObject(), Data.version, in.readUTF());
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      DateRepresentation date = new DateRepresentation(true);
      File fout = new File(Data.path + "\\log\\");
      if(!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\");
      if(!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\");
      if(!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\");
      if(!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\login.txt");
      boolean exists = true;
      if(!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if(!exists)
        Data.logWriter.println("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tBITS\tOPERATION\tERROR_MESSAGE\t");
      Data.logWriter.println(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + this.password + "\t" + this.bits + "\tloginBuyerWithUpdate\t" + this.errorMessage);
      Data.logWriter.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    StaticUser te = null;
    if (this.date == null)
      this.date = new DateRepresentation(true);
    if(clientThread != null) clientThread.language = this.language;
    try
    {
      Table t = db.getTable(Consts.tbGuestBuyerUsers);
      TableElement te1 = t.findElement(this.code);
      if(te1 == null)
      {
        errorMessage = Consts.ermsInvName(this.language);
        System.out.println(this.errorMessage);
        return false;
      }
      te = (StaticUser)te1;
      if(!new String(te.password).equals(this.password))
      {
        errorMessage = Consts.ermsInvLogin(this.language);
        System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage + "\t" + this.password);
        return false;
      }
      if(te.isLoggedIn)
      {
        errorMessage = Consts.ermsUserNotLoged(this.language);
        System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage);
        return false;
      }
      StringTokenizer strv = new StringTokenizer(this.javaVersion, "_");
      String lv = System.getProperty("java.version");
      StringTokenizer stlv = new StringTokenizer(lv, "_");
      String rv = this.javaVersion;
      if(stlv.countTokens() > 1)
        lv = stlv.nextToken();
      if(strv.countTokens() > 1)
        rv = strv.nextToken();
      if(!lv.equals(rv))
      {
        this.errorMessage = Consts.ermsWrongJavaVersion(this.language);
        System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage + "\t" + this.javaVersion);
        return false;
      }
      if(!Consts.version.equals(this.version))
      {
        this.errorMessage = Consts.ermsWrongVersion(this.language);
        System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage + "\t" + this.version);
        return false;
      }
      if(clientThread == null)
      {
        this.errorMessage = "No";
        System.out.println(te.getValue(0, 1).toString() + "\tclient thread is null");
        return false;
      }
      clientThread.getOut().writeBoolean(true);
      clientThread.getOut().flush();
        clientThread.setUser(te);
        if(te.needsUpdate)
        {
          clientThread.getOut().writeBoolean(true);
          clientThread.getOut().flush();
          te.clearCommands();
          te.needsUpdate = false;
/*
          clientThread.getDBStream().a = te.a;
          clientThread.getDBStream().b = te.b;
          clientThread.getDBStream().c = te.c;
          clientThread.getDBStream().setEncoding(true);
*/
          db.send(clientThread.getOut(), clientThread.getIn(), clientThread.getDBStream(), clientThread.getUser());
          clientThread.getIn().readBoolean();
        }
        else
        {
          clientThread.getOut().writeBoolean(false);
          clientThread.getOut().flush();
          long v = 0;
          synchronized(Data.lock)
          {
            try
            {
              v = Data.version;
              clientThread.getUpdateStream().setRemember(true);
/*
              clientThread.getUpdateStream().a = te.a;
              clientThread.getUpdateStream().b = te.b;
              clientThread.getUpdateStream().c = te.c;
              clientThread.getUpdateStream().setEncoding(true);
*/
              te.sendCommands(clientThread.getUpdateStream());
              te.isLoggedIn = true;
            }
            catch(Exception e) {}
          }
//          System.out.println("Starting heart beat...");
//          heartBeat.start();
          clientThread.getUpdateStream().release();
          clientThread.getUpdateStream().close(false);
//          System.out.println("Stopping heart beat...");
//          heartBeat.halt();
//          while(heartBeat.isActive())
//          {
//            try
//            {
//              Thread.sleep(1000);
//            }
//            catch(Exception e) {}
//          }
          long version = clientThread.getIn().readLong();
          boolean b = v == version;
          clientThread.getOut().writeBoolean(b);
          clientThread.getOut().flush();
          if(!b)
          {
            b = clientThread.getIn().readBoolean();
            if(b)
            {
//              db.send(clientThread.getOut(), clientThread.getIn(), clientThread.getDBStream(), clientThread.getUser());
//              clientThread.getIn().readBoolean();
              te.isLoggedIn = false;
              return false;
            }
            else
             {
               te.isLoggedIn = false;
               return false;
             }
          }
        }
        int pos = -1;
        for(int i = 0; i < Data.outgoingChannels.size(); i++)
          if(((OutgoingChannel)Data.outgoingChannels.get(i)).counter ==
             clientThread.getCounter())
            pos = i;
        ((OutgoingChannel)Data.outgoingChannels.get(pos)).setUser(te);
        ((OutgoingChannel)Data.outgoingChannels.get(pos)).language = this.language;
        System.out.println("User" + clientThread.getCounter() + " " + new String(te.name) + " has been successfully logged in");
        ((StaticUser)clientThread.getUser()).hasDB = true;
        te.isLoggedIn = true;
      return true;
    }
    catch(OutOfMemoryError e1)
    {
      if(te != null) te.isLoggedIn = false;
      this.errorMessage = "Server busy!  Please try later.";
      e1.printStackTrace();
      System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage);
      return false;
    }
    catch(Exception e)
    {
      if(te != null) te.isLoggedIn = false;
      this.errorMessage = e.toString();
      System.out.println(te.getValue(0, 1).toString() + "\t" + this.errorMessage);
      return false;
    }
  }

  public String getBits()
  {
    return this.bits;
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

  public long getVersionAfterExecuting()
  {
    return this.versionAfterExecuting;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.errorMessage + ";"
          + this.password + ";"
          + this.language + ";"
          + this.version + ";"
          + this.javaVersion + ";"
          + this.bits + ";"
          + this.code + ";"
          + this.date.toString() + " " + this.date.getFullTime() + ";"
          + this.versionAfterExecuting;
    }
    catch (Exception e)
    {
      return "ex :" + e + " " + getClass().getName();
    }
  }

  @Override
  public DateRepresentation getTimeStamp()
  {
    return this.date;
  }
}
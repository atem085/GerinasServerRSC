package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import java.util.ArrayList;
import utils.Consts;

public class Command34 implements Command
{
  private String errorMessage, password, language;
  private long code;
  private DateRepresentation date;
  private String currentUser;

  public Command34()
  {
    this.errorMessage = "";
    this.code = 0;
    this.password = null;
    this.language = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command34(long code, String password, String language, String currentUser, DateRepresentation date)
  {
    this.errorMessage = "";
    this.code = code;
    this.password = password;
    this.language = language;
    this.date = date;
    this.currentUser = currentUser;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(34);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(34);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    try
    {
      return new Command34(in.readLong(), in.readUTF(), in.readUTF(), in.readUTF(), (DateRepresentation) in.readObject());
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
      Data.logWriter = new PrintWriter(new FileOutputStream(Data.path + "\\" + date.toString() + "log.txt", true), true);
      Data.logWriter.println("TIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION");
      Data.logWriter.println(date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) +
                             "\t" + this.password + "\tloginEmployee\t" + this.errorMessage);
      Data.logWriter.println();
      Data.logWriter.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    if (clientThread != null)
      clientThread.language = this.language;
    try
    {
      Table t = db.getTable(Consts.tbUsers);
      TableElement te1 = t.findElement(this.code);
      if (te1 == null)
      {
        errorMessage = Consts.ermsInvName(this.language);
        return false;
      }
      StaticUser te = (StaticUser) te1;
      if (!new String(te.password).equals(this.password))
      {
        errorMessage = Consts.ermsInvLogin(this.language);
        return false;
      }
      if (te.isLoggedIn)
      {
        errorMessage = Consts.ermsUserNotLoged(this.language);
        return false;
      }
      if (clientThread == null)
      {
        this.errorMessage = "No";
        return false;
      }
      clientThread.getOut().writeBoolean(true);
      clientThread.getOut().flush();
      clientThread.setUser(te);
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
      int pos = -1;
      for (int i = 0; i < Data.outgoingChannels.size(); i++)
        if ( ( (OutgoingChannel) Data.outgoingChannels.get(i)).counter ==
            clientThread.getCounter())
          pos = i;
      ( (OutgoingChannel) Data.outgoingChannels.get(pos)).setUser(te);
      ( (OutgoingChannel) Data.outgoingChannels.get(pos)).language = this.language;
      System.out.println("User" + clientThread.getCounter() + " " + new String(te.name) + " has been successfully logged in");
      ( (StaticUser) clientThread.getUser()).hasDB = true;
      te.isLoggedIn = true;
      return true;
    }
    catch (OutOfMemoryError e1)
    {
      this.errorMessage = "Server busy!  Please try later.";
      e1.printStackTrace();
      return false;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      this.errorMessage = e.toString();
      return false;
    }
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

  public long getVersionAfterExecuting()
  {
    return Data.version;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.errorMessage + ";"
          + this.password + ";"
          + this.language + ";"
          + this.code + ";"
          + this.date.toString() + " " + this.date.getFullTime();
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
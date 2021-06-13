package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import utils.Consts;
import java.io.File;

public class Command24 implements Command
{
  private String errorMessage;
  private long code;
  private String userName, password;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;

  public Command24()
  {
    this.code = 0;
    this.errorMessage = "";
    this.date = null;
  }

  public Command24(long code, DateRepresentation date, long versionAfterExecuting)
  {
    this.code = code;
    this.errorMessage = "";
    this.date = date;
    this.versionAfterExecuting = versionAfterExecuting;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(24);
    out.writeLong(this.code);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(24);
    out.writeLong(this.code);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    try
    {
      if(local)
        return new Command24(in.readLong(), (DateRepresentation) in.readObject(),in.readLong());
      return new Command24(in.readLong(),(DateRepresentation) in.readObject(),Data.version);
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
      Data.logWriter.println(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + this.userName + "\t" + this.password + "\tlogoutCustomer\t" + this.errorMessage);
      Data.logWriter.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    Table t = db.getTable(Consts.tbGuestUsers);
    if (this.date == null)
      this.date = new DateRepresentation(true);
    if(t == null)
    {
      errorMessage = Consts.ermsNotTableDB(language);
      return false;
    }
    TableElement te1 = t.findElement(this.code);
    if(te1 == null)
    {
      errorMessage = Consts.ermsInvName(language);
      return false;
    }
    User user = (User)te1;
    user.isLoggedIn = false;
    this.userName = new String(user.name);
    this.password = new String(user.password);
    if(clientThread != null)
    {
      try
      {
        Data.commander.forceOut(clientThread);
      }
      catch(Exception e)
      {
        this.errorMessage = e.toString();
        return false;
      }
      System.out.println("User" + clientThread.getCounter() +
                         " has been successfully logged out");
    }
    return true;
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
          + this.code + ";"
          + this.userName + ";"
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
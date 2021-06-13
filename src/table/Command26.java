package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import data.Data;
import java.io.PrintWriter;
import java.io.File;

public class Command26 implements Command
{
  private String errorMessage;
  private long version;

  public Command26(long version)
  {
    this.version = version;
    this.errorMessage = "";
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(26);
    out.writeLong(this.version);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(26);
    out.writeLong(this.version);
    if(flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    return new Command26(in.readLong());
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
      Data.logWriter.println(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\t \tsendDB\t" + this.errorMessage);
      Data.logWriter.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    boolean needsDB = (this.version != Data.version);
    try
    {
      clientThread.getOut().writeBoolean(needsDB);
      clientThread.getOut().flush();
      if(needsDB)
      {
        needsDB = clientThread.getIn().readBoolean();
        if(needsDB)
        {
/*
          clientThread.getDBStream().a = ((StaticUser)clientThread.user).a;
          clientThread.getDBStream().b = ((StaticUser)clientThread.user).b;
          clientThread.getDBStream().c = ((StaticUser)clientThread.user).c;
          clientThread.getDBStream().setEncoding(true);
*/
          db.send(clientThread.getOut(), clientThread.getIn(), clientThread.getDBStream(), clientThread.getUser());
          clientThread.getIn().readBoolean();
        }
      }
    }
    catch(OutOfMemoryError e1)
    {
      this.errorMessage = "Server busy!  Please try later.";
      e1.printStackTrace();
      return false;
    }
    catch(Exception e)
    {
      this.errorMessage = e.toString();
      return false;
    }
    return needsDB;
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
          + this.version;
    }
    catch (Exception e)
    {
      return "ex :" + e + " " + getClass().getName();
    }
  }

  @Override
  public DateRepresentation getTimeStamp()
  {
    return new DateRepresentation(true);
  }
}
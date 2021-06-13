package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import java.io.File;
import data.Data;

public class Command35 implements Command
{
  private String errorMessage;

  public Command35()
  {
    this.errorMessage = "";
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(35);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(35);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    return new Command35();
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      DateRepresentation date = new DateRepresentation(true);
      Data.logWriter = new PrintWriter(new FileOutputStream(Data.path + "\\" + date.toString() + "log.txt", true), true);
      Data.logWriter.println("TIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION");
      Data.logWriter.println(date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + clientThread.getUser().name + "\t" +
                             clientThread.getUser().password + "\tscreen_of_" + clientThread.getSelectedTable().getName() + "\t" + this.errorMessage);
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
    return true;
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
          + this.errorMessage;
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
package table;

import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;

public class Command7 implements Command
{
  private String errorMessage, tableName;

  public Command7()
  {
    this.errorMessage = "";
    this.tableName = null;
  }

  public Command7(String tableName)
  {
    this.errorMessage = "";
    this.tableName = tableName;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(7);
    out.writeUTF(this.tableName);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(7);
    out.writeUTF(this.tableName);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    return new Command7(in.readUTF());
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    try
    {
      this.errorMessage = clientThread.selectTable(this.tableName, language);
    }
    catch (Exception e)
    {
      this.errorMessage = e.toString();
      return false;
    }
    return this.errorMessage.length() == 0;
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

  public long getVersionAfterExecuting()
  {
    return data.Data.version;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.errorMessage + ";"
          + this.tableName;
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
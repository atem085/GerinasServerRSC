package table;

import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;

public class Command8 implements Command
{
  private String errorMessage, tableName;
  private long elementCode[];
  private int subtableIndex[];

  public Command8()
  {
    this.errorMessage = "";
    this.elementCode = null;
    this.subtableIndex = null;
    this.tableName = null;
  }

  public Command8(String tableName, long[] elementCode, int[] subtableIndex)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.subtableIndex = subtableIndex;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(8);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeInt(this.subtableIndex[i]);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(8);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeInt(this.subtableIndex[i]);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    String tn = in.readUTF();
    int len = in.readInt();
    long[] el = new long[len];
    int[] sub = new int[len];
    for (int i = 0; i < len; i++)
      el[i] = in.readLong();
    for (int i = 0; i < len; i++)
      sub[i] = in.readInt();
    return new Command8(tn, el, sub);
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    try
    {
      clientThread.selectTable(this.tableName, this.elementCode, this.subtableIndex);
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
          + this.tableName + ";"
          + this.elementCode + ";"
          + this.subtableIndex;
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
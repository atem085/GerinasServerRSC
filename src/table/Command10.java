package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.Data;
import java.io.File;

public class Command10 implements Command
{
  private String errorMessage, tableName;
  public long[] elementCode;
  public int[] subtableIndex;
  public long subelementCode;
  public int column;
  private Object aValue;
  private Table table;
  private TableElement element;
  private long versionAfterExecuting = 0;
  private String currentUser;
  private DateRepresentation date;

  public Command10()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.elementCode = null;
    this.aValue = null;
    this.subtableIndex = null;
    this.column = 0;
    this.subelementCode = 0;
    this.currentUser = "";
    this.date = null;
  }

  public Command10(String tableName, long[] elementCode, int[] subtableIndex, long subelementCode, int column, Object aValue,  String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.subtableIndex = subtableIndex;
    this.subelementCode = subelementCode;
    this.column = column;
    this.aValue = aValue;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(10);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for(int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for(int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
    out.writeLong(this.subelementCode);
    out.writeInt(this.column);
    out.writeObject(this.aValue);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }
  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(10);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for(int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for(int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
    out.writeLong(this.subelementCode);
    out.writeInt(this.column);
    out.writeObject(this.aValue);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    String tn = in.readUTF();
    int length = in.readInt();
    long[] ec = new long[length];
    int[] si = new int[length];
    for(int i = 0; i < length; i++)
      ec[i] = in.readLong();
    for(int i = 0; i < length; i++)
      si[i] = in.readInt();
    long sec = in.readLong();
    int col = in.readInt();
    Object p = null;
    try
    {
      p = in.readObject();
    }
    catch(ClassNotFoundException e)
    {
      this.errorMessage = e.toString();
    }
    try
    {
      if(local)
        return new Command10(tn, ec, si, sec, col, p, in.readUTF(), (DateRepresentation) in.readObject(), in.readLong());
      return new Command10(tn, ec, si, sec, col, p, in.readUTF(), (DateRepresentation) in.readObject(), Data.version + 1);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      Data.logStream.writeInt(10);
      Data.logStream.writeUTF(this.tableName);
      Data.logStream.writeInt(this.elementCode.length);
      for(int i = 0; i < this.elementCode.length; i++)
      {
        Data.logStream.writeLong(this.elementCode[i]);
      }
      for(int i = 0; i < this.subtableIndex.length; i++)
      {
        Data.logStream.writeInt(this.subtableIndex[i]);
      }
      Data.logStream.writeLong(this.subelementCode);
      Data.logStream.writeInt(this.column);
      Data.logStream.writeObject(this.aValue);
      Data.logStream.writeUTF(this.currentUser);
      Data.logStream.writeObject(this.date);
      Data.logStream.writeLong(this.versionAfterExecuting);
      Data.logStream.flush();
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
      String tbn = new String(this.tableName);
      for(int i = 0; i < this.subtableIndex.length; i++)
        tbn = "" + this.subtableIndex[i] + "." + tbn;
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + tbn + ".txt");
      boolean exists = true;
      if(!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if(this.table == null)
      {
        Table t = Data.db.getTable(this.tableName, this.elementCode, this.subtableIndex);
        this.table = t;
      }
      if(this.element == null)
        this.element = this.table.findElement(this.subelementCode).duplicate();
      if(!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tsetValue\t" + this.table.getName() + "\t" + this.table.getExample().getColumnNames(this.table.getHowToRead(), language, null)[this.column] + "\t" + this.aValue + "\t \t" + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(this.table.getHowToRead()));
      Data.logWriter.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    try
    {
      Table t = db.getTable(this.tableName, this.elementCode, this.subtableIndex);
      this.table = t;
      this.element = t.findElement(this.subelementCode).duplicate();
      if (clientThread != null)
        this.currentUser = clientThread.user.getValue(4, 1).toString();
      if (this.date == null)
        this.date = new DateRepresentation(true);
      this.errorMessage = t.setValueAt(this.aValue, this.subelementCode, this.column, language, this.currentUser, this.date);
      if(t.lastOperationResult)
        Data.version++;
      return t.lastOperationResult;
    }
    catch(Exception e)
    {
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
    return this.versionAfterExecuting;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.errorMessage + ";"
          + this.tableName + ";"
          + this.elementCode + ";"
          + this.subtableIndex + ";"
          + this.subelementCode + ";"
          + this.column + ";"
          + this.aValue + ";"
          + this.table + ";"
          + this.element + ";"
          + this.currentUser + ";"
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

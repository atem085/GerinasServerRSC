package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command17 implements Command
{
  private String errorMessage, tableName;
  public long elementCode;
  public int column, amount;
  private Object aValue;
  private Table table;
  private TableElement element;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;
  private String currentUser;

  public Command17()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.elementCode = 0;
    this.column = -2;
    this.amount = 0;
    this.aValue = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command17(String tableName, long elementCode, int column, int amount, Object aValue, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.column = column;
    this.amount = amount;
    this.aValue = aValue;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(17);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeInt(this.amount);
    out.writeObject(this.aValue);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(17);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeInt(this.amount);
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
    long ec = 0;
    ec = in.readLong();
    int c = in.readInt();
    int a = in.readInt();
    Object v = null;
    try
    {
      v = in.readObject();
    }
    catch(ClassNotFoundException e)
    {
      this.errorMessage = e.toString();
    }
    try
    {
      if(local)
        return new Command17(tn, ec, c, a, v, in.readUTF(), (DateRepresentation) in.readObject(), in.readLong());
      return new Command17(tn, ec, c, a, v, in.readUTF(), (DateRepresentation) in.readObject(),Data.version + 1);
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
      Data.logStream.writeInt(17);
      Data.logStream.writeUTF(this.tableName);
      Data.logStream.writeLong(this.elementCode);
      Data.logStream.writeInt(this.column);
      Data.logStream.writeObject(this.aValue);
      Data.logStream.writeUTF(this.currentUser);
      Data.logStream.writeObject(this.date);
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
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + this.tableName + ".txt");
      boolean exists = true;
      if(!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if(this.table == null)
        this.table = Data.db.getTable(this.tableName);
      if(!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tsplitElement\t" + this.table.getName() + "\t" + this.table.getExample().getColumnNames(this.table.getHowToRead(), language, null)[this.column] + "\t" + this.aValue + "\t" + this.amount + "\t" + this.errorMessage + "\t");
      if(this.element.getCode().longValue() == this.elementCode)
        Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()));
      else
        Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()).replaceFirst(this.element.getCode().toString(), "" + this.elementCode));
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
      Table t = db.getTable(this.tableName);
      this.table = t;
      this.element = t.findElement(this.elementCode).duplicate();
      this.errorMessage = t.splitTableElement(this.elementCode, this.column, this.amount, this.aValue, language, this.currentUser, this.date);
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
          + this.column + ";"
          + this.amount + ";"
          + this.aValue + ";"
          + this.table + ";"
          + this.element + "; "
          + this.currentUser + "; "
          + this.date.toString() + " " + this.date.getFullTime() + "; "
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
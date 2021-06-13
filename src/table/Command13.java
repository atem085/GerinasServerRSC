package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import data.*;
import java.io.PrintWriter;
import java.io.File;

public class Command13 implements Command
{
  private String errorMessage = "", tableName;
  private long elementCode;
  private Table table;
  private TableElement element;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;
  private String currentUser;

  public Command13()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.elementCode = 0;
    this.date = null;
    this.currentUser = "";
  }

  public Command13(String tableName, long elementCode, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.versionAfterExecuting = versionAfterExecuting;
    this.date = date;
    this.currentUser = currentUser;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(13);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(13);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeUTF(this.currentUser);
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
      return new Command13(in.readUTF(), in.readLong(), in.readUTF(), (DateRepresentation) in.readObject(), in.readLong());
    return new Command13(in.readUTF(), in.readLong(), in.readUTF(), (DateRepresentation) in.readObject(), Data.version + 1);
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
      Data.logStream.writeInt(13);
      Data.logStream.writeUTF(this.tableName);
      Data.logStream.writeLong(this.elementCode);
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
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + this.table.getName() + ".txt");
      boolean exists = true;
      if(!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if(this.table == null)
      {
        Table t = Data.db.getTable(this.tableName);
        this.table = t;
      }
      if(this.element == null)
        this.element = this.table.findElement(this.elementCode).duplicate();
      if(!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tremoveElement\t" + this.table.getName() + "\t \t" + this.elementCode + "\t \t" + this.errorMessage + "\t");
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
      this.errorMessage = t.removeTableElement(this.elementCode, language, this.currentUser, this.date);
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
          + this.table + ";"
          + this.element + "; "
          + this.currentUser + "; "
          + this.date.toString() + " " + this.date.getFullTime()  + "; "
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
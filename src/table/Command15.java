package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command15 implements Command
{
  private String errorMessage, tableNameFrom, tableNameTo;
  public long elementCode;
  public int amount;
  private Table table;
  private TableElement element;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;
  private String currentUser;

  public Command15()
  {
    this.errorMessage = "";
    this.tableNameFrom = null;
    this.tableNameTo = null;
    this.elementCode = 0;
    this.amount = 0;
    this.date = null;
    this.currentUser = "";
  }

  public Command15(String tableNameFrom, long elementCode, TableElement element, int amount, String tableNameTo, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableNameFrom = tableNameFrom;
    this.table = Data.db.getTable(this.tableNameFrom);
    this.tableNameTo = tableNameTo;
    this.elementCode = elementCode;
    this.amount = amount;
    this.element = element;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(15);
    out.writeUTF(this.tableNameFrom);
    out.writeLong(this.elementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(15);
    out.writeUTF(this.tableNameFrom);
    out.writeLong(this.elementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
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
        return new Command15(in.readUTF(), in.readLong(), null, in.readInt(), in.readUTF(),in.readUTF(), (DateRepresentation) in.readObject(),in.readLong());
      return new Command15(in.readUTF(), in.readLong(), null, in.readInt(), in.readUTF(), in.readUTF(), (DateRepresentation) in.readObject(),Data.version + 1);
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
      Data.logStream.writeInt(15);
      Data.logStream.writeUTF(this.tableNameFrom);
      Data.logStream.writeLong(this.elementCode);
      Data.logStream.writeInt(this.amount);
      Data.logStream.writeUTF(this.tableNameTo);
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
      if(!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tmoveElement\t" + this.table.getName() + "\t" + this.elementCode + "\t" + this.tableNameTo + "\t" + this.amount + "\t" + this.errorMessage + "\t");
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
      this.element = this.table.findElement(this.elementCode).duplicate();
      this.errorMessage = this.table.moveTableElement(this.elementCode, this.amount, language, this.tableNameTo, this.currentUser, this.date);
      if(this.table.lastOperationResult)
        Data.version++;
      return this.table.lastOperationResult;
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
          + this.tableNameFrom + ";"
          + this.tableNameTo + ";"
          + this.elementCode + ";"
          + this.amount + ";"
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
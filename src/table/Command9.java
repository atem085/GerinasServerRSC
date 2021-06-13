package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.Data;
import java.io.File;

public class Command9 implements Command
{
  protected String errorMessage = "", tableName;
  public long elementCode;
  public int column;
  private Object aValue;
  private TableElement element;
  private Table table;
  private long versionAfterExecuting = 0;
  private String currentUser;
  private DateRepresentation date;

  public Command9()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.elementCode = 0;
    this.aValue = null;
    this.column = 0;
    this.currentUser = "";
    this.date = null;
  }

  public Command9(String tableName, long elementCode, int column, Object aValue, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.column = column;
    this.aValue = aValue;
    this.currentUser = currentUser;
    this.date = date;
    this.versionAfterExecuting = versionAfterExecuting;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(9);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeObject(this.aValue);
    out.writeUTF(this.currentUser);
    out.writeObject(date);
    out.writeLong(this.versionAfterExecuting);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(9);
    out.writeUTF(this.tableName);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeObject(this.aValue);
    out.writeUTF(this.currentUser);
    out.writeObject(date);
    out.writeLong(this.versionAfterExecuting);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    String tn = in.readUTF();
    long ec = in.readLong();
    int col = in.readInt();
    Object p = null;
    try
    {
      p = in.readObject();
    }
    catch (ClassNotFoundException e)
    {
      this.errorMessage = e.toString();
    }
    try
    {
    if (local)
      return new Command9(tn, ec, col, p, in.readUTF(),(DateRepresentation) in.readObject(), in.readLong());
    return new Command9(tn, ec, col, p, in.readUTF(), (DateRepresentation) in.readObject(), Data.version + 1);
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
      Data.logStream.writeInt(9);
      Data.logStream.writeUTF(this.tableName);
      Data.logStream.writeLong(this.elementCode);
      Data.logStream.writeInt(this.column);
      Data.logStream.writeObject(this.aValue);
      Data.logStream.writeUTF(this.currentUser);
      Data.logStream.writeObject(this.date);
      Data.logStream.flush();
      DateRepresentation date = new DateRepresentation(true);

      File fout = new File(Data.path + "\\log\\");
      if (!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\");
      if (!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\");
      if (!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\");
      if (!fout.exists())
        fout.mkdir();
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + this.tableName + ".txt");
      boolean exists = true;
      if (!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if (this.table == null)
        this.table = Data.db.getTable(this.tableName);
      if (this.element == null)
        this.element = this.table.findElement(this.elementCode).duplicate();
      if (!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      if (clientThread == null)
      {
        Data.logWriter.print("exception clientThread=null");
        System.out.println("Exception Command9 clientThread=null");
      }
      else if (clientThread.getUser() == null)
      {
        Data.logWriter.print("exception clientThread.getUser()=null");
        System.out.println("Exception Command9 clientThread.getUser()=null");
      }
      else if (table == null)
      {
        Data.logWriter.print("exception this.table=null");
        System.out.println("Exception Command9 this.table=null");
      }
      else if (this.aValue == null)
      {
        Data.logWriter.print("exception this.aValue=null");
        System.out.println("Exception Command9 this.aValue=null");
      }
      else if (this.errorMessage == null)
      {
        Data.logWriter.print("exception errorMessage=null");
        System.out.println("Exception Command9 errorMessage=null");
      }
      else
        Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" +
                             new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tsetValue\t" + this.table.getName() + "\t" +
                             this.table.getExample().getColumnNames(this.table.getHowToRead(), language, null)[this.column] + "\t" + this.aValue + "\t \t" +
                             this.errorMessage + "\t");
      if (this.element.getCode().longValue() == this.elementCode)
        Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()));
      else
        Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()).replaceFirst(this.element.getCode().toString(),
            "" + this.elementCode));
      Data.logWriter.close();
    }
    catch (Exception e)
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
      /*      if(!this.tableName.equals(utils.Consts.tbfinalInvoicesA))
              return true;
            if(this.tableName.equals(utils.Consts.tbfinalInvoicesA))
            {
              System.out.println("SET " + this.tableName + " " + this.aValue + " " + this.elementCode);
            }*/
      if (clientThread != null)
        this.currentUser = clientThread.user.getValue(4, 1).toString();
      if (this.date == null)
        this.date = new DateRepresentation(true);
      this.errorMessage = t.setValueAt(this.aValue, this.elementCode, this.column, language, this.currentUser, this.date);
      if (t.lastOperationResult)
        Data.version++;
      return t.lastOperationResult;
    }
    catch (Exception e)
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
          + this.aValue + ";"
          + this.element + ";"
          + this.table + ";"
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
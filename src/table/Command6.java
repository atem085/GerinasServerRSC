package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command6 implements Command
{
  private String errorMessage;
  public long elementCode;
  public int column, amount;
  public Object aValue;
  private TableElement element;
  protected DateRepresentation date;

  public Command6()
  {
    this.errorMessage = "";
    this.elementCode = 0;
    this.column = -2;
    this.amount = 0;
    this.aValue = null;
  }

  public Command6(long elementCode, int column, int amount, Object aValue)
  {
    this.errorMessage = "";
    this.elementCode = elementCode;
    this.column = column;
    this.amount = amount;
    this.aValue = aValue;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    if (clientThread != null)
    {
      if (clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(17);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(18);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for (int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for (int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(6);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeInt(this.amount);
    out.writeObject(this.aValue);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    if (clientThread != null)
    {
      if (clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(17);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(18);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for (int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for (int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(6);
    out.writeLong(this.elementCode);
    out.writeInt(this.column);
    out.writeInt(this.amount);
    out.writeObject(this.aValue);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    long ec = in.readLong();
    int c = in.readInt();
    int a = in.readInt();
    Object v = null;
    try
    {
      v = in.readObject();
    }
    catch (ClassNotFoundException e)
    {
      this.errorMessage = e.toString();
    }
    return new Command6(ec, c, a, v);
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      if (clientThread.subtableIndex == null)
      {
        Data.logStream.writeInt(17);
        Data.logStream.writeUTF(clientThread.getSelectedTable().getName());
      }
      else
      {
        Data.logStream.writeInt(18);
        Data.logStream.writeUTF(clientThread.tableName);
        Data.logStream.writeInt(clientThread.elementCode.length);
        for (int i = 0; i < clientThread.elementCode.length; i++)
        {
          Data.logStream.writeLong(clientThread.elementCode[i]);
        }
        for (int i = 0; i < clientThread.subtableIndex.length; i++)
        {
          Data.logStream.writeInt(clientThread.subtableIndex[i]);
        }
      }
      Data.logStream.writeLong(this.elementCode);
      Data.logStream.writeInt(this.column);
      Data.logStream.writeObject(this.aValue);
      Data.logStream.writeUTF(clientThread.getUser().getValue(4, 1).toString());
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
      if (clientThread.elementCode == null || clientThread.elementCode.length == 0)
        fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + clientThread.tableName + ".txt");
      else
      {
        String tbn = new String(clientThread.tableName);
        for (int i = 0; i < clientThread.subtableIndex.length; i++)
          tbn = "" + clientThread.subtableIndex[i] + "." + tbn;
        fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + tbn + ".txt");
      }
      boolean exists = true;
      if (!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if (!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < clientThread.getSelectedTable().getColumnCount(); i++)
          cn += clientThread.getSelectedTable().getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" +
                           new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tsplitElement\t" +
                           clientThread.getSelectedTable().getName() + "\t" +
                           clientThread.getSelectedTable().getExample().getColumnNames(clientThread.getSelectedTable().getHowToRead(), language, null)[this.column] +
                           "\t" + this.aValue + "\t" + this.amount + "\t" + this.errorMessage + "\t");
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
      Table t = clientThread.getSelectedTable();
      this.element = t.findElement(this.elementCode).duplicate();
      if (this.date == null)
        this.date = new DateRepresentation(true);
      String currentUser = clientThread != null ? clientThread.user.getValue(4, 1).toString() : "";
      this.errorMessage = t.splitTableElement(this.elementCode, this.column, this.amount, this.aValue, language, currentUser, date);
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
    return Data.version;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.errorMessage + ";"
          + this.elementCode + ";"
          + this.column + ";"
          + this.amount + ";"
          + this.aValue + ";"
          + this.date.toString() + " " + this.date.getFullTime();
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
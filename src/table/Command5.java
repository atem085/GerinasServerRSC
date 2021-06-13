package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import java.io.File;

public class Command5 implements Command
{
  private String errorMessage;
  public String tableNameTo;
  public long[] elementCodeTo;
  public int[] subtableIndexTo;
  public long elementCode;
  public int amount;
  private TableElement element;
  private Table to;
  protected DateRepresentation date;

  public Command5()
  {
    this.errorMessage = "";
    this.tableNameTo = null;
    this.elementCodeTo = null;
    this.elementCode = 0;
    this.amount = 0;
    this.subtableIndexTo = null;
  }

  public Command5(long elementCode, int amount, String tableNameTo, long[] elementCodeTo, int[] subtableIndexTo)
  {
    this.errorMessage = "";
    this.tableNameTo = tableNameTo;
    this.elementCodeTo = elementCodeTo;
    this.amount = amount;
    this.subtableIndexTo = subtableIndexTo;
    this.elementCode = elementCode;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    if (clientThread != null)
    {
      out.writeInt(16);
      out.writeUTF(clientThread.tableName);
      out.writeInt(clientThread.elementCode.length);
      for (int i = 0; i < clientThread.elementCode.length; i++)
        out.writeLong(clientThread.elementCode[i]);
      for (int i = 0; i < clientThread.subtableIndex.length; i++)
        out.writeInt(clientThread.subtableIndex[i]);
    }
    else
      out.writeInt(5);
    out.writeLong(this.elementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
    out.writeInt(this.elementCodeTo.length);
    for (int i = 0; i < this.elementCodeTo.length; i++)
      out.writeLong(this.elementCodeTo[i]);
    for (int i = 0; i < this.subtableIndexTo.length; i++)
      out.writeInt(this.subtableIndexTo[i]);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    if (clientThread != null)
    {
      out.writeInt(16);
      out.writeUTF(clientThread.tableName);
      out.writeInt(clientThread.elementCode.length);
      for (int i = 0; i < clientThread.elementCode.length; i++)
        out.writeLong(clientThread.elementCode[i]);
      for (int i = 0; i < clientThread.subtableIndex.length; i++)
        out.writeInt(clientThread.subtableIndex[i]);
    }
    else
      out.writeInt(5);
    out.writeLong(this.elementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
    out.writeInt(this.elementCodeTo.length);
    for (int i = 0; i < this.elementCodeTo.length; i++)
      out.writeLong(this.elementCodeTo[i]);
    for (int i = 0; i < this.subtableIndexTo.length; i++)
      out.writeInt(this.subtableIndexTo[i]);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    long ec = in.readLong();
    int am = in.readInt();
    String tnt = in.readUTF();
    int lengthTo = in.readInt();
    long[] ect = new long[lengthTo];
    int[] sit = new int[lengthTo];
    for (int i = 0; i < lengthTo; i++)
      ect[i] = in.readLong();
    for (int i = 0; i < lengthTo; i++)
      sit[i] = in.readInt();
    return new Command5(ec, am, tnt, ect, sit);
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      Data.logStream.writeInt(16);
      Data.logStream.writeUTF(clientThread.tableName);
      if (clientThread.elementCode != null)
        Data.logStream.writeInt(clientThread.elementCode.length);
      else
        Data.logStream.writeInt(0);
      if (clientThread.elementCode != null)
        for (int i = 0; i < clientThread.elementCode.length; i++)
          Data.logStream.writeLong(clientThread.elementCode[i]);
      if (clientThread.elementCode != null)
        for (int i = 0; i < clientThread.subtableIndex.length; i++)
        {
          Data.logStream.writeInt(clientThread.subtableIndex[i]);
        }
      Data.logStream.writeLong(this.elementCode);
      Data.logStream.writeInt(this.amount);
      Data.logStream.writeUTF(this.tableNameTo);
      Data.logStream.writeInt(this.elementCodeTo.length);
      for (int i = 0; i < this.elementCodeTo.length; i++)
      {
        Data.logStream.writeLong(this.elementCodeTo[i]);
      }
      for (int i = 0; i < this.subtableIndexTo.length; i++)
      {
        Data.logStream.writeInt(this.subtableIndexTo[i]);
      }
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
                           new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tmoveElement\t" +
                           clientThread.getSelectedTable().getName() + "\t" + this.elementCode + "\t" + this.to.getName() + "\t" + this.amount + "\t" + this.errorMessage +
                           "\t");
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
      this.to = t;
      this.element = t.findElement(this.elementCode).duplicate();
      if (this.date == null)
        this.date = new DateRepresentation(true);
      String currentUser = clientThread != null ? clientThread.user.getValue(4, 1).toString() : "";
      this.errorMessage = t.moveTableElement(this.elementCode, this.amount, language, this.tableNameTo, this.elementCodeTo, this.subtableIndexTo, currentUser, date);
      if (t.lastOperationResult)
        Data.version++;
      return t.lastOperationResult;
    }
    catch (Exception e)
    {
      e.printStackTrace();
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
          + this.tableNameTo + ";"
          + this.elementCodeTo + ";"
          + this.elementCode + ";"
          + this.amount + ";"
          + this.element + ";"
          + this.to + "; "
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
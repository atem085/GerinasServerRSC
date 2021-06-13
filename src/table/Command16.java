package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command16 implements Command
{
  private String errorMessage, tableNameFrom, tableNameTo;
  public int[] subtableIndexFrom, subtableIndexTo;
  public long[] elementCodeFrom, elementCodeTo;
  public long subelementCode;
  public int amount;
  private Table from;
  private Table to;
  private TableElement element;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;
  private String currentUser;

  public Command16()
  {
    this.errorMessage = "";
    this.tableNameFrom = null;
    this.tableNameTo = null;
    this.elementCodeFrom = null;
    this.elementCodeTo = null;
    this.subelementCode = 0;
    this.amount = 0;
    this.subtableIndexFrom = null;
    this.subtableIndexTo = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command16(String tableNameFrom, long[] elementCodeFrom, int[] subtableIndexFrom, long subelementCode, int amount, String tableNameTo, long[] elementCodeTo, int[] subtableIndexTo,
                   String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableNameFrom = tableNameFrom;
    this.tableNameTo = tableNameTo;
    this.elementCodeFrom = elementCodeFrom;
    this.elementCodeTo = elementCodeTo;
    this.subtableIndexFrom = subtableIndexFrom;
    this.amount = amount;
    this.subtableIndexTo = subtableIndexTo;
    this.subelementCode = subelementCode;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(16);
    out.writeUTF(this.tableNameFrom);
    if(this.elementCodeFrom != null)
      out.writeInt(this.elementCodeFrom.length);
    else
      out.writeInt(0);
    if(this.elementCodeFrom != null)
      for(int i = 0; i < this.elementCodeFrom.length; i++)
        out.writeLong(this.elementCodeFrom[i]);
    if(this.subtableIndexFrom != null)
      for (int i = 0; i < this.subtableIndexFrom.length; i++)
        out.writeInt(this.subtableIndexFrom[i]);
    out.writeLong(this.subelementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
    out.writeInt(this.elementCodeTo.length);
    for(int i = 0; i < this.elementCodeTo.length; i++)
      out.writeLong(this.elementCodeTo[i]);
    for(int i = 0; i < this.subtableIndexTo.length; i++)
      out.writeInt(this.subtableIndexTo[i]);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }
  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(16);
    out.writeUTF(this.tableNameFrom);
    if(this.elementCodeFrom != null)
      out.writeInt(this.elementCodeFrom.length);
    else
      out.writeInt(0);
    if(this.elementCodeFrom != null)
      for(int i = 0; i < this.elementCodeFrom.length; i++)
        out.writeLong(this.elementCodeFrom[i]);
    if(this.subtableIndexFrom != null)
      for (int i = 0; i < this.subtableIndexFrom.length; i++)
        out.writeInt(this.subtableIndexFrom[i]);
    out.writeLong(this.subelementCode);
    out.writeInt(this.amount);
    out.writeUTF(this.tableNameTo);
    out.writeInt(this.elementCodeTo.length);
    for(int i = 0; i < this.elementCodeTo.length; i++)
      out.writeLong(this.elementCodeTo[i]);
    for(int i = 0; i < this.subtableIndexTo.length; i++)
      out.writeInt(this.subtableIndexTo[i]);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }


  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    String tnf = in.readUTF();
    int lengthFrom = in.readInt();
    long[] ecf = new long[lengthFrom];
    int[] sif = new int[lengthFrom];
    for(int i = 0; i < lengthFrom; i++)
      ecf[i] = in.readLong();
    for(int i = 0; i < lengthFrom; i++)
      sif[i] = in.readInt();
    long sc = in.readLong();
    int am = in.readInt();
    String tnt = in.readUTF();
    int lengthTo = in.readInt();
    long[] ect = new long[lengthTo];
    int[] sit = new int[lengthTo];
    for(int i = 0; i < lengthTo; i++)
      ect[i] = in.readLong();
    for(int i = 0; i < lengthTo; i++)
      sit[i] = in.readInt();
    try
    {
      if(local)
        return new Command16(tnf, ecf, sif, sc, am, tnt, ect, sit, in.readUTF(), (DateRepresentation) in.readObject(),in.readLong());
      return new Command16(tnf, ecf, sif, sc, am, tnt, ect, sit, in.readUTF(), (DateRepresentation) in.readObject(),Data.version + 1);
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
      Data.logStream.writeInt(16);
      Data.logStream.writeUTF(this.tableNameFrom);
      Data.logStream.writeInt(this.elementCodeFrom.length);
      for(int i = 0; i < this.elementCodeFrom.length; i++)
      {
        Data.logStream.writeLong(this.elementCodeFrom[i]);
      }
      for(int i = 0; i < this.subtableIndexFrom.length; i++)
      {
        Data.logStream.writeInt(clientThread.subtableIndex[i]);
      }
      Data.logStream.writeLong(this.subelementCode);
      Data.logStream.writeInt(this.amount);
      Data.logStream.writeUTF(this.tableNameTo);
      Data.logStream.writeInt(this.elementCodeTo.length);
      for(int i = 0; i < this.elementCodeTo.length; i++)
      {
        Data.logStream.writeLong(this.elementCodeTo[i]);
      }
      for(int i = 0; i < this.subtableIndexTo.length; i++)
      {
        Data.logStream.writeInt(this.subtableIndexTo[i]);
      }
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
      String tbn = new String(this.tableNameFrom);
      for(int i = 0; i < this.subtableIndexFrom.length; i++)
        tbn = "" + this.subtableIndexFrom[i] + "." + tbn;
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + tbn + ".txt");
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
        for (int i = 0; i < this.from.getColumnCount(); i++)
          cn += this.from.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\tmoveElement\t" + this.from.getName() + "\t" + this.elementCodeFrom + "\t" + this.tableNameTo + "\t" + this.amount + "\t" + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(this.from.getHowToRead()));
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
      Table t = db.getTable(this.tableNameFrom, this.elementCodeFrom, this.subtableIndexFrom);
      this.from = t;
      this.to = db.getTable(this.tableNameTo, this.elementCodeTo, this.subtableIndexTo);
      this.element = from.findElement(this.subelementCode).duplicate();
      this.errorMessage = t.moveTableElement(this.subelementCode, this.amount, language, this.tableNameTo, this.elementCodeTo, this.subtableIndexTo, this.currentUser, this.date);
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
          + this.tableNameFrom + ";"
          + this.tableNameTo + ";"
          + this.subtableIndexFrom + ";"
          + this.subtableIndexTo + ";"
          + this.elementCodeFrom + ";"
          + this.elementCodeTo + ";"
          + this.subelementCode + ";"
          + this.amount + ";"
          + this.from + ";"
          + this.to + ";"
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
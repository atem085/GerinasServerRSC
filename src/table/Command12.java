package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command12 implements Command
{
  private String errorMessage = "", tableName;
  private long[] elementCode;
  private int[] subtableIndex;
  private Object[] parameters;
  private Table table;
  private long versionAfterExecuting = 0;
  private TableElement element;
  private DateRepresentation date;
  private String currentUser;

  public Command12()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.elementCode = null;
    this.parameters = null;
    this.subtableIndex = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command12(String tableName, long[] elementCode, int[] subtableIndex, Object[] parameters, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.subtableIndex = subtableIndex;
    this.parameters = parameters;
    this.versionAfterExecuting = versionAfterExecuting;
    this.date = date;
    this.currentUser = currentUser;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(12);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for(int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for(int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(parameters[i]);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(12);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for(int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for(int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(parameters[i]);
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
    length = in.readInt();
    Object[] p = new Object[length];
    for(int i = 0; i < length; i++)
    {
      try
      {
        p[i] = in.readObject();
      }
      catch(ClassNotFoundException e)
      {
        this.errorMessage = e.toString();
      }
    }
    try
    {
      if(local)
        return new Command12(tn, ec, si, p, in.readUTF(), (DateRepresentation) in.readObject(), in.readLong());
      return new Command12(tn, ec, si, p, in.readUTF(),(DateRepresentation) in.readObject(), Data.version + 1);
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
      Data.logStream.writeInt(12);
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
      Data.logStream.writeInt(this.parameters.length);
      for(int i = 0; i < this.parameters.length; i++)
      {
        Data.logStream.writeObject(parameters[i]);
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
      {
        TableElement te = this.table.getExample().newInstance(this.parameters, this.table.getHowToRead(), this.table.getParentElement());
        this.element = te;
      }
      if(!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      String pn = "";
      if(this.parameters.length > 3)
      {
        pn = this.parameters[0] + "\t" + this.parameters[1] + "\t" + this.parameters[2] + ";";
        for(int k = 3; k < parameters.length; k++)
          pn += parameters[k] + ";";
        pn += "\t";
      }
      else if(this.parameters.length == 3)
        pn = this.parameters[0] + "\t" + this.parameters[1] + "\t" + this.parameters[2] + "\t";
      else if(this.parameters.length == 2)
        pn = this.parameters[0] + "\t" + this.parameters[1] + "\t \t";
      else if(this.parameters.length == 1)
        pn = this.parameters[0] + "\t \t \t";
      else if(this.parameters.length == 0)
        pn = " \t \t \t";
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\taddElement\t" + this.table.getName() + "\t" + pn + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(this.table.getHowToRead()));
      Data.logWriter.close();

      Data.logWriter = new PrintWriter(new FileOutputStream(Data.path + "\\" + date.toString() + "log.txt", true), true);
      Data.logWriter.println("TIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tERROR_MESSAGE");
      String element = "";
      for(int i = 0; i < this.parameters.length; i++)
        element += parameters[i].toString() + "\t";
      Data.logWriter.println(date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\taddElement\t" + this.table.getName() + "\t" + this.errorMessage);
      String cn = "";
      for(int i = 0; i < this.table.getColumnCount(); i++)
        cn += this.table.getColumnName(i) + "\t";
      Data.logWriter.println(cn);
      Data.logWriter.println(element);
      Data.logWriter.println();
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
      TableElement te = t.getExample().newInstance(this.parameters, t.getHowToRead(), t.getParentElement());
      this.element = te;
      this.errorMessage = t.addTableElementComplete(te, language, this.currentUser, this.date);
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
          + this.elementCode + ";"
          + this.subtableIndex + ";"
          + this.parameters + ";"
          + this.table + "; "
          + this.currentUser + ";"
          + this.date.toString() + " " + this.date.getFullTime() + "; "
          + this.versionAfterExecuting + ";"
          + this.element;
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
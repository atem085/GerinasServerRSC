package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import utils.DateRepresentation;
import java.io.FileOutputStream;
import data.*;
import java.io.PrintWriter;
import java.io.File;

public class Command11 implements Command
{
  private String errorMessage = "", tableName;
  private Object[] parameters;
  private Table table;
  private long versionAfterExecuting = 0;
  private TableElement element;
  private String currentUser;
  private  DateRepresentation date;

  public Command11()
  {
    this.errorMessage = "";
    this.tableName = null;
    this.parameters = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command11(String tableName, Object[] parameters, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.parameters = parameters;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(11);
    out.writeUTF(this.tableName);
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
    out.writeInt(11);
    out.writeUTF(this.tableName);
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
        e.printStackTrace();
      }
    }
    try
    {
      if(local)
        return new Command11(tn, p, in.readUTF(), (DateRepresentation)in.readObject(), in.readLong());
      return new Command11(tn, p, in.readUTF(), (DateRepresentation)in.readObject(), Data.version + 1);
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
      Data.logStream.writeInt(11);
      Data.logStream.writeUTF(this.tableName);
      Data.logStream.writeInt(this.parameters.length);
      for(int i = 0; i < this.parameters.length; i++)
      {
        Data.logStream.writeObject(parameters[i]);
      }
      if(this.table == null)
      {
        Table t = Data.db.getTable(this.tableName);
        this.table = t;
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
      if(this.element == null)
      {
        TableElement te = this.table.getExample().newInstance(this.parameters, this.table.getHowToRead(), this.table.getParentElement());
        this.element = te;
      }
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\taddElement\t" + this.table.getName() + "\t" + pn + this.errorMessage + "\t");
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
      Table t = db.getTable(this.tableName);
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
          + this.parameters + ";"
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

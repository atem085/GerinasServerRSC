package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command2 implements Command
{
  private String errorMessage;
  public Object[] parameters;
  private TableElement element;
  private DateRepresentation date;

  public Command2()
  {
    this.errorMessage = "";
    this.parameters = null;
  }

  public Command2(Object[] parameters)
  {
    this.errorMessage = "";
    this.parameters = parameters;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    if(clientThread != null)
    {
      if(clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(11);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(12);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for(int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(2);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(parameters[i]);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    if(clientThread != null)
    {
      if(clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(11);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(12);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for(int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(2);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(parameters[i]);
    if(flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
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
      }
    }
    return new Command2(p);
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
      if(clientThread.subtableIndex == null)
      {
        Data.logStream.writeInt(11);
        Data.logStream.writeUTF(clientThread.getSelectedTable().getName());
      }
      else
      {
        Data.logStream.writeInt(12);
        Data.logStream.writeUTF(clientThread.tableName);
        Data.logStream.writeInt(clientThread.elementCode.length);
        for(int i = 0; i < clientThread.elementCode.length; i++)
        {
          Data.logStream.writeLong(clientThread.elementCode[i]);
        }
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
        {
          Data.logStream.writeInt(clientThread.subtableIndex[i]);
        }
      }
      Data.logStream.writeInt(this.parameters.length);
      for(int i = 0; i < this.parameters.length; i++)
      {
        Data.logStream.writeObject(parameters[i]);
      }
      Data.logStream.writeUTF(clientThread.getUser().getValue(4, 1).toString());
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
      if(clientThread.elementCode == null || clientThread.elementCode.length == 0)
        fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + clientThread.tableName + ".txt");
      else
      {
        String tbn = new String(clientThread.tableName);
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
          tbn = "" + clientThread.subtableIndex[i] + "." + tbn;
        fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + tbn + ".txt");
      }
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
        for (int i = 0; i < clientThread.getSelectedTable().getColumnCount(); i++)
          cn += clientThread.getSelectedTable().getColumnName(i) + "\t";
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
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\taddElement\t" + clientThread.getSelectedTable().getName() + "\t" + pn + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()));
      Data.logWriter.close();
    }
    catch(Exception e)
    {
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    Table t = null;
    try
    {
      t = clientThread.getSelectedTable();
      if (this.date == null)
        this.date = new DateRepresentation(true);
      String currentUser = clientThread != null ? clientThread.user.getValue(4, 1).toString() : "";
      TableElement te = t.getExample().newInstance(this.parameters, t.getHowToRead(), t.getParentElement());
      this.element = te;
      this.errorMessage = t.addTableElementComplete(te, language, currentUser, this.date);
      if(t.lastOperationResult)
        Data.version++;
      return t.lastOperationResult;
    }
    catch(Exception e)
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
          + this.parameters + ";"
          + this.element  + ";"
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
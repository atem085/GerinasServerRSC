package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import utils.Consts;
import utils.DateRepresentation;
import java.io.PrintWriter;
import data.*;
import java.io.File;

public class Command30 implements Command
{
  private String errorMessage;
  public long selectedCode;
  public int selectedColumn;
  public int operationIndex;
  public TableElement element;
  public Object[] parameters;
  private DateRepresentation date;

  public Command30()
  {
    this.errorMessage = "";
    this.selectedCode = 0;
    this.parameters = null;
    this.element = null;
    this.selectedColumn = -2;
    this.operationIndex = -1;
  }

  public Command30(Object[] parameters, long selectedCode, int selectedColumn, int operationIndex)
  {
    this.errorMessage = "";
    this.parameters = parameters;
    this.selectedCode = selectedCode;
    this.selectedColumn = selectedColumn;
    this.operationIndex = operationIndex;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    if(clientThread != null)
    {
      if(clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(31);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(32);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for(int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(30);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(this.parameters[i]);
    out.writeLong(this.selectedCode);
    out.writeInt(this.selectedColumn);
    out.writeInt(this.operationIndex);
    if(flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    if(clientThread != null)
    {
      if(clientThread.elementCode == null || clientThread.subtableIndex == null)
      {
        out.writeInt(31);
        out.writeUTF(clientThread.tableName);
      }
      else
      {
        out.writeInt(32);
        out.writeUTF(clientThread.tableName);
        out.writeInt(clientThread.elementCode.length);
        for(int i = 0; i < clientThread.elementCode.length; i++)
          out.writeLong(clientThread.elementCode[i]);
        for(int i = 0; i < clientThread.subtableIndex.length; i++)
          out.writeInt(clientThread.subtableIndex[i]);
      }
    }
    else
      out.writeInt(30);
    out.writeInt(this.parameters.length);
    for(int i = 0; i < this.parameters.length; i++)
      out.writeObject(this.parameters[i]);
    out.writeLong(this.selectedCode);
    out.writeInt(this.selectedColumn);
    out.writeInt(this.operationIndex);
    if(flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    int length = in.readInt();
    Object[] p = new Object[length];
    for(int i = 0; i < length; i++)
      try
      {
        p[i] = in.readObject();
      }
      catch(ClassNotFoundException e) {}
    return new Command30(p, in.readLong(), in.readInt(), in.readInt());
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    if(this.selectedColumn < 0 || this.selectedColumn >= clientThread.getSelectedTable().getColumnCount())
      this.selectedColumn = 0;
    try
    {
      if(clientThread.subtableIndex == null)
      {
        Data.logStream.writeInt(31);
        Data.logStream.writeUTF(clientThread.getSelectedTable().getName());
      }
      else
      {
        Data.logStream.writeInt(32);
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
        Data.logStream.writeObject(this.parameters[i]);
      }
      Data.logStream.writeLong(this.selectedCode);
      Data.logStream.writeInt(this.selectedColumn);
      Data.logStream.writeInt(this.operationIndex);
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
      if(this.parameters.length > 2)
      {
        pn = this.parameters[0] + "\t" + this.parameters[1] + ";";
        for(int k = 2; k < parameters.length; k++)
          pn += parameters[k] + ";";
        pn += "\t";
      }
      else if(this.parameters.length == 2)
        pn = this.parameters[0] + "\t" + this.parameters[1] + "\t";
      else if(this.parameters.length == 1)
        pn = this.parameters[0] + "\t \t";
      else if(this.parameters.length == 0)
        pn = " \t \t";
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\t" + clientThread.getSelectedTable().getExample().getOperationName(clientThread.getSelectedTable().getHowToRead(), this.operationIndex, "en") + "\t" + clientThread.getSelectedTable().getName() + "\t" + clientThread.getSelectedTable().getColumnName(this.selectedColumn) + "\t" + pn + this.errorMessage + "\t");
      if(this.element == null)
      {
        Data.logWriter.println("exception element=null");
        System.out.println("Exception Command30 element=null");
      }
      else if(clientThread == null)
      {
        Data.logWriter.println("exception clientThread=null");
        System.out.println("Exception Command30 clientThread=null");
      }
      else if(clientThread.getSelectedTable() == null)
      {
        Data.logWriter.println("exception clientThread.getSelectedTable()=null");
        System.out.println("Exception Command30 clientThread.getSelectedTable()=null");
      }
      else
        Data.logWriter.println(this.element.toString(clientThread.getSelectedTable().getHowToRead()));
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
      this.element = clientThread.getSelectedTable().findElement(this.selectedCode);
      if (this.date == null)
        this.date = new DateRepresentation(true);
      String currentUser = clientThread != null ? clientThread.user.getValue(4, 1).toString() : "";
      this.errorMessage = clientThread.getSelectedTable().executeOperation(this.parameters, this.selectedCode, this.selectedColumn, this.operationIndex, language, currentUser, this.date);
      if(clientThread.getSelectedTable().lastOperationResult)
        Data.version++;
      return clientThread.getSelectedTable().lastOperationResult;
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
          + this.selectedCode + ";"
          + this.selectedColumn + ";"
          + this.operationIndex + ";"
          + this.element + ";"
          + this.parameters + ";date "
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
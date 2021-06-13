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

public class Command31 implements Command
{
  private String errorMessage = "";
  public long selectedCode;
  public int selectedColumn;
  public int operationIndex;
  public TableElement element;
  public Object[] parameters;
  protected String tableName;
  private long versionAfterExecuting = 0;
  private DateRepresentation date;
  private String currentUser;

  public Command31()
  {
    this.errorMessage = "";
    this.selectedCode = 0;
    this.parameters = null;
    this.element = null;
    this.selectedColumn = -2;
    this.operationIndex = -1;
    this.tableName = null;
    this.date = null;
    this.currentUser = "";
  }

  public Command31(String tableName, Object[] parameters, long selectedCode, int selectedColumn, int operationIndex, String currentUser, DateRepresentation date, long versionAfterExecuting)
  {
    this.errorMessage = "";
    this.tableName = tableName;
    this.parameters = parameters;
    this.selectedCode = selectedCode;
    this.selectedColumn = selectedColumn;
    this.operationIndex = operationIndex;
    this.versionAfterExecuting = versionAfterExecuting;
    this.currentUser = currentUser;
    this.date = date;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(31);
    out.writeUTF(this.tableName);
    out.writeInt(this.parameters.length);
    for (int i = 0; i < this.parameters.length; i++)
      out.writeObject(this.parameters[i]);
    out.writeLong(this.selectedCode);
    out.writeInt(this.selectedColumn);
    out.writeInt(this.operationIndex);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
    out.writeInt(31);
    out.writeUTF(this.tableName);
    out.writeInt(this.parameters.length);
    for (int i = 0; i < this.parameters.length; i++)
      out.writeObject(this.parameters[i]);
    out.writeLong(this.selectedCode);
    out.writeInt(this.selectedColumn);
    out.writeInt(this.operationIndex);
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    out.writeLong(this.versionAfterExecuting);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    String tableName = in.readUTF();
    int length = in.readInt();
    Object[] p = new Object[length];
    for (int i = 0; i < length; i++)
      try
      {
        p[i] = in.readObject();
      }
      catch (ClassNotFoundException e)
      {}
    try
    {
      if(local)
        return new Command31(tableName, p, in.readLong(), in.readInt(), in.readInt(), in.readUTF(), (DateRepresentation) in.readObject(), in.readLong());
      return new Command31(tableName, p, in.readLong(), in.readInt(), in.readInt(), in.readUTF(), (DateRepresentation) in.readObject(), Data.version + 1);
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
      Data.logStream.writeInt(31);
      Data.logStream.writeUTF(clientThread.getSelectedTable().getName());
      Data.logStream.writeInt(this.parameters.length);
      for (int i = 0; i < this.parameters.length; i++)
      {
        Data.logStream.writeObject(this.parameters[i]);
      }
      Data.logStream.writeLong(this.selectedCode);
      Data.logStream.writeInt(this.selectedColumn);
      Data.logStream.writeInt(this.operationIndex);
      Data.logStream.writeUTF(this.currentUser);
      Data.logStream.writeObject(this.date);
      Data.logStream.flush();
      DateRepresentation date = new DateRepresentation(true);
      Table table = Data.db.getTable(this.tableName);

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
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + clientThread.tableName + ".txt");
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
        for (int i = 0; i < table.getColumnCount(); i++)
          cn += table.getColumnName(i) + "\t";
        Data.logWriter.println(cn);
      }
      String pn = "";
      if (this.parameters.length > 2)
      {
        pn = this.parameters[0] + "\t" + this.parameters[1] + ";";
        for (int k = 2; k < parameters.length; k++)
          pn += parameters[k] + ";";
        pn += "\t";
      }
      else if (this.parameters.length == 2)
        pn = this.parameters[0] + "\t" + this.parameters[1] + "\t";
      else if (this.parameters.length == 1)
        pn = this.parameters[0] + "\t \t";
      else if (this.parameters.length == 0)
        pn = " \t \t";
      if (this.element == null)
        this.element = table.findElement(this.selectedCode);
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" +
                           new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\t" +
                           table.getExample().getOperationName(table.getHowToRead(), this.operationIndex, "en") + "\t" + table.getName() + "\t" +
                           table.getColumnName(this.selectedColumn) + "\t" + pn + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(table.getHowToRead()));
      Data.logWriter.close();
    }
    catch (Exception e)
    {
    }
  }

  public boolean execute(IncomingClientThread clientThread, DB db, String language)
  {
    try
    {
      Table table = Data.db.getTable(this.tableName);
      /*      if(!this.tableName.equals(Consts.tbfinalInvoicesA))
              return true;
            if(this.tableName.equals(Consts.tbfinalInvoicesA))
            {
              System.out.println(this.operationIndex + " " + this.tableName);
              for(int i = 0; i < this.parameters.length; i++)
                System.out.println(parameters[i].toString());
            }
            else
              System.out.println("WRONG COMMAND");*/
      this.element = table.findElement(this.selectedCode);
      if (this.element != null)
      {
        this.element = this.element.duplicate();
        if (this.element == null)
        {
          this.errorMessage = Consts.ermsNoPosition(language);
          return false;
        }
      }
      this.errorMessage = table.executeOperation(this.parameters, this.selectedCode, this.selectedColumn, this.operationIndex, language, this.currentUser, this.date);
      /*      if(this.tableName.equals(Consts.tbfinalInvoicesA))
            {
              System.out.println("Result " + table.lastOperationResult);
              System.out.println("Error " + this.errorMessage);
            }*/
      if (table.lastOperationResult)
        Data.version++;
      return table.lastOperationResult;
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
    return this.versionAfterExecuting;
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
          + this.parameters + ";"
          + this.tableName + "; "
          + this.currentUser + "; "
          + this.date.toString()  + " " + this.date.getFullTime() + "; "
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
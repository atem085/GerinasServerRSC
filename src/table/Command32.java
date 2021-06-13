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

public class Command32 implements Command
{
  private String errorMessage = "";
  public long selectedCode;
  public int selectedColumn;
  public int operationIndex;
  public TableElement element;
  public Object[] parameters;
  private String tableName;
  private long[] elementCode;
  private int[] subtableIndex;
  private long versionAfterExecuting = 0;
  private Table table;
  private DateRepresentation date;
  private String currentUser;

  public Command32()
  {
    this.errorMessage = "";
    this.selectedCode = 0;
    this.parameters = null;
    this.element = null;
    this.selectedColumn = -2;
    this.operationIndex = -1;
    this.tableName = null;
    this.elementCode = null;
    this.subtableIndex = null;
    this.date = date;
    this.currentUser = "";
  }

  public Command32(String tableName, long[] elementCode, int[] subtableIndex, Object[] parameters, long selectedCode, int selectedColumn, int operationIndex, String currentUser,
                   DateRepresentation date, long versionAfterExecuting)
  {
    this.tableName = tableName;
    this.elementCode = elementCode;
    this.subtableIndex = subtableIndex;
    this.errorMessage = "";
    this.parameters = parameters;
    this.selectedCode = selectedCode;
    this.selectedColumn = selectedColumn;
    this.operationIndex = operationIndex;
    this.currentUser = currentUser;
    this.date = date;
    this.versionAfterExecuting = versionAfterExecuting;
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
    out.writeInt(32);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for (int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
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
    out.writeInt(32);
    out.writeUTF(this.tableName);
    out.writeInt(this.elementCode.length);
    for (int i = 0; i < this.elementCode.length; i++)
      out.writeLong(this.elementCode[i]);
    for (int i = 0; i < this.subtableIndex.length; i++)
      out.writeInt(this.subtableIndex[i]);
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
    String tn = in.readUTF();
    int length = in.readInt();
    long[] ec = new long[length];
    int[] si = new int[length];
    for (int i = 0; i < length; i++)
      ec[i] = in.readLong();
    for (int i = 0; i < length; i++)
      si[i] = in.readInt();
    length = in.readInt();
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
      if (local)
        return new Command32(tn, ec, si, p, in.readLong(), in.readInt(), in.readInt(), in.readUTF(), (DateRepresentation) in.readObject(),in.readLong());
      return new Command32(tn, ec, si, p, in.readLong(), in.readInt(), in.readInt(), in.readUTF(), (DateRepresentation) in.readObject(),Data.version + 1);
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
      Data.logStream.writeInt(32);
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
      String tbn = new String(this.tableName);
      for (int i = 0; i < this.subtableIndex.length; i++)
        tbn = "" + this.subtableIndex[i] + "." + tbn;
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\" + tbn + ".txt");
      boolean exists = true;
      if (!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if (this.table == null)
        this.table = Data.db.getTable(this.tableName, this.elementCode, this.subtableIndex);
      if (this.element == null)
        this.element = table.findElement(this.selectedCode);
      if (!exists)
      {
        Data.logWriter.print("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION\tTABLE\tCOLUMN\tNEW_VALUE\tAMOUNT\tERROR_MESSAGE\t");
        String cn = "";
        for (int i = 0; i < this.table.getColumnCount(); i++)
          cn += this.table.getColumnName(i) + "\t";
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
      if (this.table == null)
        this.table = Data.db.getTable(this.tableName, this.elementCode, this.subtableIndex);
      if (this.element == null)
        this.element = table.findElement(this.selectedCode);
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" +
                           new String(clientThread.getUser().name) + "\t" + new String(clientThread.getUser().password) + "\t" +
                           this.table.getExample().getOperationName(this.table.getHowToRead(), this.operationIndex, "en") + "\t" + this.table.getName() + "\t" +
                           this.table.getColumnName(this.selectedColumn) + "\t" + pn + this.errorMessage + "\t");
      Data.logWriter.println(this.element.toString(this.table.getHowToRead()));
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
      this.table = Data.db.getTable(this.tableName, this.elementCode, this.subtableIndex);
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
      if (table.lastOperationResult)
        Data.version++;
      return table.lastOperationResult;
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

  @Override
  public DateRepresentation getTimeStamp()
  {
    return this.date;
  }
}
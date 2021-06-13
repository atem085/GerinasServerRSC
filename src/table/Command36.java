package table;

import utils.MyObjectOutputStream;
import java.io.IOException;
import utils.MyObjectInputStream;
import java.io.File;
import java.io.FileOutputStream;
import data.Data;
import utils.DateRepresentation;
import java.io.PrintWriter;
import java.io.FileInputStream;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Command36 implements Command
{
  private long[] lastModifiedClient;
  private File[] serverFiles;
  private String errorMessage = "";
  private DateRepresentation date;

  public Command36()
  {
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException
  {
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException
  {
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    try
    {
      int length = in.readInt();
      this.lastModifiedClient = new long[length];
      this.serverFiles = new File[length];
      for (int i = 0; i < length; i++)
      {
        this.lastModifiedClient[i] = in.readLong();
        this.serverFiles[i] = new File(in.readUTF());
      }
    }
    catch (Exception e)
    {
      this.errorMessage = e.toString();
    }
    return this;
  }

  public void log(IncomingClientThread clientThread, String path, String language)
  {
    try
    {
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
      fout = new File(Data.path + "\\log\\" + date.getYear() + "\\" + date.getMonth() + "\\download.txt");
      boolean exists = true;
      if (!fout.exists())
      {
        fout.createNewFile();
        exists = false;
      }
      Data.logWriter = new PrintWriter(new FileOutputStream(fout, true), true);
      if (!exists)
        Data.logWriter.println("DAY\tTIME\tUSER_NAME\tUSER_PASSWORD\tTABLE\tERROR_MESSAGE\tFILES");

      String element = "";
      for (int i = 0; i < this.serverFiles.length; i++)
        element += this.serverFiles[i].getAbsolutePath() + "\t";
      Data.logWriter.print(date.getDay() + "\t" + date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" +
                           date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" +
                           new String(clientThread.getUser().password) + "\t" + clientThread.getSelectedTable().getName() + "\t" + this.errorMessage + "\t");
      Data.logWriter.println(element);
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
      boolean res = false;
      for (int i = 0; i < this.serverFiles.length; i++)
      {
        long lss = this.serverFiles[i].lastModified();
        clientThread.getOut().writeLong(lss);
        if (lss != this.lastModifiedClient[i])
        {
          System.out.println("Sending " + this.serverFiles[i].getAbsolutePath());
          FileInputStream fin = new FileInputStream(this.serverFiles[i]);
          int length = fin.available();
          clientThread.getOut().writeInt(length);
          for (int j = 0; j < length; j++)
            clientThread.getOut().write(fin.read());
          fin.close();
          System.out.println("Sending finished");
          res = true;
        }
      }
      System.out.println("Files to download " + res);
      clientThread.getOut().flush();
      return res;
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
          + this.lastModifiedClient + ";"
          + this.serverFiles + ";"
          + this.errorMessage + ";"
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
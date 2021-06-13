package table;

import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.*;
import java.util.Vector;

public class Command33 implements Command
{
  private int commandNo;
  private String param;
  private int size;
  private Vector data;
  private String errorMessage;
  private DateRepresentation date;
  private String currentUser;

  public Command33()
  {
    this.commandNo = 0;
    this.param = "";
    this.errorMessage = "";
    this.data = null;
    this.currentUser = "";
  }

  public Command33(int commandNo, String param, String currentUser, DateRepresentation date)
  {
    this.commandNo = commandNo;
    this.param = param;
    this.errorMessage = "";
    this.date = date;
    this.currentUser = "";
  }

  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws
      IOException
  {
    switch (commandNo)
    {
      case 0:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeUTF( (String) data.get(i));
        break;
      case 1:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeUTF( (String) data.get(i));
        break;
      case 2:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeObject(data.get(i));
        break;
    }
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    if (flush)
      out.flush();
  }

  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws
      IOException
  {
    switch (commandNo)
    {
      case 0:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeUTF( (String) data.get(i));
        break;
      case 1:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeUTF( (String) data.get(i));
        break;
      case 2:
        out.writeInt(size);
        for (int i = 0; i < size; i++)
          out.writeObject(data.get(i));
        break;
    }
    out.writeUTF(this.currentUser);
    out.writeObject(this.date);
    if (flush)
      out.flush();
  }

  public Command read(MyObjectInputStream in, boolean local) throws IOException
  {
    commandNo = in.readInt();
    param = in.readUTF();
    currentUser = in.readUTF();
    DateRepresentation date = null;
    try
    {
      date = (DateRepresentation) in.readObject();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    return new Command33(commandNo, param, currentUser, date);
  }

  public void log(IncomingClientThread clientThread, String path,
                  String language)
  {
  }

  public boolean execute(IncomingClientThread clientThread, DB db,
                         String language)
  {
    switch (commandNo)
    {
      case 0:
        Vector v = new Vector();
        int size = 0;
        File f = new File(param);
        try
        {
          File[] files = f.listFiles();
          for (int i = 0; i < files.length; i++)
          {
            if (files[i].isDirectory())
            {
              size++;
              v.add(files[i].getName());
            }
          }
          this.size = size;
          this.data = v;
        }
        catch (Exception e)
        {
          this.errorMessage = e.toString();
          return false;
        }
        break;
      case 1:
        v = new Vector();
        f = new File(param);
        size = 0;
        try
        {
          File[] files = f.listFiles();
          for (int i = 0; i < files.length; i++)
          {
            if (files[i].isFile())
            {
              size++;
              v.add(files[i].getName());
            }
          }
          this.size = size;
          this.data = v;
        }
        catch (Exception e)
        {
          this.errorMessage = e.toString();
          return false;
        }
        break;
      case 2:
        v = new Vector();
        f = new File("");
        File[] files = f.listRoots();
        this.size = files.length - 1;
        for (int i = 0; i < files.length; i++)
        {
          if (files[i].toString().indexOf("A") == -1)
            v.add(files[i]);
        }
        this.data = v;
        break;
    }
    return true;
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

  public long getVersionAfterExecuting()
  {
    return 0;
  }

  public String toString()
  {
    try
    {
      return "command_name_" + getClass().getName() + ";"
          + this.commandNo + ";"
          + this.param + ";"
          + this.size + ";"
          + this.data + ";"
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
package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import java.util.ArrayList;
import utils.Consts;

public class HttpCommand219 implements HttpCommand
{
  private String errorMessage, password, language;
  private long code;
  private int userType;

  public HttpCommand219()
  {
    this.errorMessage = "";
    this.code = 0;
    this.password = null;
    this.language = null;
    this.userType = 0;
  }

  public HttpCommand219(int userType, long code, String password, String language)
  {
    this.errorMessage = "";
    this.code = code;
    this.password = password;
    this.language = language;
  }

  public void write(HttpClientThread clientThread, MyObjectOutputStream out) throws IOException
  {
    out.writeInt(219);
    out.writeInt(this.userType);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.flush();
  }

  public void write(HttpClientThread clientThread, OutgoingChannel out) throws IOException
  {
    out.writeInt(219);
    out.writeInt(this.userType);
    out.writeLong(this.code);
    out.writeUTF(this.password);
    out.writeUTF(this.language);
    out.flush();
  }

  public HttpCommand read(MyObjectInputStream in) throws IOException
  {
    return new HttpCommand219(in.readInt(), in.readLong(), in.readUTF(), in.readUTF());
  }

  public void log(HttpClientThread clientThread, String path, String language)
  {
    try
    {
      DateRepresentation date = new DateRepresentation(true);
      Data.logWriter = new PrintWriter(new FileOutputStream(Data.path + "\\" + date.toString() + "log.txt", true), true);
      Data.logWriter.println("TIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION");
      String op = "loginWeb";
      if(this.userType == 0)
        op += "Employee";
      else if(this.userType == 1)
        op += "Customer";
      else if(this.userType == 2)
        op += "Buyer";
      Data.logWriter.println(date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + new String(clientThread.getUser().name) + "\t" + this.password + "\t" + op + "\t" + this.errorMessage);
      Data.logWriter.println();
      Data.logWriter.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public boolean execute(HttpClientThread clientThread, DB db, String language)
  {
    if(clientThread != null) clientThread.language = this.language;
    try
    {
      Table t = null;
      if(this.userType == 0)
        t = db.getTable(Consts.tbWebUsers);
      else if(this.userType == 1)
        t = db.getTable(Consts.tbWebGuestUsers);
      else if(this.userType == 2)
        t = db.getTable(Consts.tbWebGuestBuyerUsers);
      TableElement te1 = t.findElement(this.code);
      if(te1 == null)
      {
        this.errorMessage = Consts.ermsInvName(this.language);
        return false;
      }
      User te = (User)te1;
      if(!new String(te.password).equals(this.password))
      {
        errorMessage = Consts.ermsInvLogin(this.language);
        return false;
      }
/*      if(te.isLoggedIn)
      {
        this.errorMessage = Consts.ermsUserNotLoged(this.language);
        return false;
      }*/
      if(clientThread == null)
      {
        this.errorMessage = "No";
        return false;
      }
      clientThread.setUser(te);
      System.out.println("User" + clientThread.getCounter() + " " + new String(te.name) + " has been successfully logged in");
      te.isLoggedIn = true;
      return true;
    }
    catch(Exception e)
    {
      this.errorMessage = e.toString();
      e.printStackTrace();
      return false;
    }
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

}

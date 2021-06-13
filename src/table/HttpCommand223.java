package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import java.io.PrintWriter;
import utils.Consts;

public class HttpCommand223 implements HttpCommand
{
  private String password, errorMessage;
  private long code;
  private int userType;
  private String userName;

  public HttpCommand223()
  {
    this.code = 0;
    this.password = "";
    this.userName = "";
    this.errorMessage = "";
    this.userType = 0;
  }

  public HttpCommand223(int userType, long code)
  {
    this.code = code;
    this.errorMessage = "";
    this.userName = "";
    this.password = "";
    this.userType = userType;
  }

  public void write(HttpClientThread clientThread, MyObjectOutputStream out) throws IOException
  {
    out.writeInt(23);
    out.writeInt(this.userType);
    out.writeLong(this.code);
    out.flush();
  }

  public void write(HttpClientThread clientThread, OutgoingChannel out) throws IOException
  {
    out.writeInt(23);
    out.writeInt(this.userType);
    out.writeLong(this.code);
    out.flush();
  }

  public HttpCommand read(MyObjectInputStream in) throws IOException
  {
    return new HttpCommand223(in.readInt(), in.readLong());
  }

  public void log(HttpClientThread clientThread, String path, String language)
  {
    try
    {
      DateRepresentation date = new DateRepresentation(true);
      Data.logWriter = new PrintWriter(new FileOutputStream(Data.path + "\\" + date.toString() + "log.txt", true), true);
      Data.logWriter.println("TIME\tUSER_NAME\tUSER_PASSWORD\tOPERATION");
      String op = "logoutWeb";
      if(this.userType == 0)
        op += "Employee";
      else if(this.userType == 1)
        op += "Customer";
      else if(this.userType == 2)
        op += "Buyer";
      Data.logWriter.println(date.get(date.HOUR_OF_DAY) + ":" + date.get(date.MINUTE) + ":" + date.get(date.SECOND) + "\t" + this.userName + "\t" + this.password + "\t" + op + "\t" + this.errorMessage);
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
    Table t = null;
    if(this.userType == 0)
      t = db.getTable(Consts.tbWebUsers);
    else if(this.userType == 1)
      t = db.getTable(Consts.tbWebGuestUsers);
    else if(this.userType == 2)
      t = db.getTable(Consts.tbWebGuestBuyerUsers);
    if(t == null)
    {
      errorMessage = Consts.ermsNotTableDB(language);
      return false;
    }
    TableElement te1 = t.findElement(this.code);
    if(te1 == null)
    {
      errorMessage = Consts.ermsInvName(language);
      return false;
    }
    User user = (User)te1;
    this.userName = new String(user.name);
    this.password = new String(user.password);
    user.isLoggedIn = false;
    if(clientThread != null)
    {
      try
      {
        clientThread.canBeRunning = false;
        clientThread.out.close();
        clientThread.in.close();
        clientThread.httpSocket.close();
      }
      catch(Exception e)
      {
        this.errorMessage = e.toString();
        return false;
      }
      System.out.println("User" + clientThread.getCounter() +
                         " has been successfully logged out");
    }
    return true;
  }

  public String getErrorMessage()
  {
    return this.errorMessage;
  }

}

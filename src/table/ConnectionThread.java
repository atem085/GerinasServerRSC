package table;

import javax.net.ssl.SSLSocket;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.io.IOException;
import data.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ConnectionThread extends Thread
{
  protected SSLSocket incomingSocket;
  private MyObjectOutputStream incomingOut;
  private MyObjectInputStream incomingIn;
  private int counter;
  private boolean canBeRunning;
  public long timeOfCreation;
  private StaticUser user;


  public ConnectionThread(SSLSocket incomingSocket, int counter) throws IOException
  {
    this.timeOfCreation = new java.util.Date().getTime();
    this.incomingSocket = incomingSocket;
    this.user = null;
    this.incomingSocket.setSoTimeout(20000);
    this.incomingIn = new MyObjectInputStream(incomingSocket.getInputStream());
//    incomingIn.setEcho(true);
    this.counter = counter;
    this.incomingOut = new MyObjectOutputStream(this.incomingSocket.getOutputStream(), false);
//    incomingOut.setEcho(true);
    this.canBeRunning = true;
    this.incomingIn.setEncoding(false);
    this.incomingOut.setEncoding(false);
    this.setPriority(Thread.MIN_PRIORITY);
  }

  public void setUser(StaticUser user)
  {
    this.user = user;
  }

  public int getCounter()
  {
    return this.counter;
  }

  public void halt()
  {
    this.canBeRunning = false;
    try
    {
      this.incomingSocket.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
    System.out.println("Connection thread stopped");
  }

  public MyObjectOutputStream getOut()
  {
    return this.incomingOut;
  }

  public MyObjectInputStream getIn()
  {
    return this.incomingIn;
  }

  public void run()
  {
    while(this.user == null || !this.user.hasDB)
      try
      {
        Thread.sleep(2000);
      }
    catch(Exception e) {}
  System.out.println("Entering connection loop " + new String(this.user.name));
    while(this.canBeRunning)
    {
//      System.out.println("TRYING TO READ SIGNAL");
        try
        {
          this.incomingIn.readBoolean();
        }
        catch(Exception e)
        {
          System.out.println("Connection lost");
          this.canBeRunning = false;
        }
    }
    try
    {
      Data.commander.forceOut(this);
    }
    catch(Exception e1)
    {
      this.canBeRunning = false;
      e1.printStackTrace();
    }
  }

}

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

public class DynamicConnectionThread extends Thread
{
  private SSLSocket incomingSocket;
  private MyObjectOutputStream out;
  private MyObjectInputStream in;
  private int counter;
  private boolean canBeRunning;
  public long timeOfCreation;
  private User user;


  public DynamicConnectionThread(SSLSocket incomingSocket, int counter) throws IOException
  {
    this.timeOfCreation = new java.util.Date().getTime();
    this.incomingSocket = incomingSocket;
    this.user = null;
    this.incomingSocket.setSoTimeout(10000);
    this.in = new MyObjectInputStream(incomingSocket.getInputStream());
//    incomingIn.setEcho(true);
    this.counter = counter;
    this.out = new MyObjectOutputStream(this.incomingSocket.getOutputStream(), false);
//    incomingOut.setEcho(true);
    this.canBeRunning = true;
    this.in.setEncoding(false);
    this.out.setEncoding(false);
    this.setPriority(Thread.MIN_PRIORITY);
  }

  public void setUser(User user)
  {
    this.user = user;
  }

  public int getCounter()
  {
    return this.counter;
  }

  public void halt() throws IOException
  {
    this.canBeRunning = false;
    this.in.close();
    this.out.close();
    this.incomingSocket.close();
  }

  public MyObjectOutputStream getOut()
  {
    return this.out;
  }

  public MyObjectInputStream getIn()
  {
    return this.in;
  }

  public void run()
  {
    while(this.user == null)
      try
      {
        Thread.sleep(2000);
      }
    catch(Exception e) {}
    while(this.canBeRunning)
    {
        try
        {
          this.in.readBoolean();
        }
        catch(Exception e)
        {
          try
          {
//            Data.commander.forceOut(this);
          }
          catch(Exception e1)
          {
            this.canBeRunning = false;
            break;
          }
          this.canBeRunning = false;
          break;
        }
    }
  }

}

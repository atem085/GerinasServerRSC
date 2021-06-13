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

public class OutgoingChannel// extends Thread
{
  public SSLSocket outgoingSocket;
  protected MyObjectOutputStream outgoingOut;
  protected MyObjectInputStream outgoingIn;
  public StaticUser user;
  public String language;
  public int counter;
  private boolean canBeRunning = true;
  private boolean isActive = false;
  private int signalCount = 0;
  private boolean readNow = false;
  private boolean read;

  public OutgoingChannel(SSLSocket outgoingSocket, int counter) throws IOException
  {
    this.outgoingSocket = outgoingSocket;
    this.outgoingSocket.setSoTimeout(20000);
//    this.outgoingSocket.setKeepAlive(true);
    this.counter = counter;
    this.outgoingIn = new MyObjectInputStream(outgoingSocket.getInputStream());
    this.outgoingOut = new MyObjectOutputStream(this.outgoingSocket.getOutputStream(), false);
//    this.outgoingSocket.setTcpNoDelay(true);
//    this.outgoingSocket.setUseClientMode(true);
//    this.outgoingIn.setEcho(true);
//    this.outgoingOut.setEcho(true);
    this.language = null;
    this.user = null;
    this.outgoingIn.setEncoding(false);
    this.outgoingOut.setEncoding(false);
  }
/*
  public void setEncoding(StaticUser user)
  {
    this.outgoingOut.a = user.a;
    this.outgoingOut.b = user.b;
    this.outgoingOut.c = user.c;
    this.outgoingIn.a = user.a;
    this.outgoingIn.b = user.b;
    this.outgoingIn.c = user.c;
  }
*/
  public void setUser(StaticUser user)
  {
    this.user = user;
/*    this.outgoingOut.a = user.a;
    this.outgoingOut.b = user.b;
    this.outgoingOut.c = user.c;
    this.outgoingIn.a = user.a;
    this.outgoingIn.b = user.b;
    this.outgoingIn.c = user.c;*/
  }

  public StaticUser getUser()
  {
    return this.user;
  }

  public boolean isActive()
  {
    return this.isActive;
  }

  public void halt()
  {
    this.canBeRunning = false;
  }

  public void close() throws IOException
  {
    try
    {
      this.outgoingIn.close();
    }
    catch(Exception e)
    {
//      e.printStackTrace();
    }
    try
    {
      this.outgoingOut.close();
    }
    catch(Exception e)
    {
//      e.printStackTrace();
    }
    this.outgoingSocket.close();
  }

  public synchronized void writeInt(int n) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeInt(n);
  }

  public synchronized void writeUTF(String s) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeUTF(s);
  }

  public synchronized void writeBoolean(boolean b) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeBoolean(b);
  }

  public synchronized void writeObject(Object o) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeObject(o);
  }

  public synchronized void writeDouble(double d) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeDouble(d);
  }

  public synchronized void writeLong(long n) throws IOException
  {
//    this.outgoingOut.writeBoolean(true);
    this.outgoingOut.writeLong(n);
  }
/*
  public synchronized void writeSignal() throws IOException
  {
    if(this.outgoingOut.echo)
      System.out.println("Writing signal");
    this.outgoingOut.writeBoolean(false);
    this.outgoingOut.writeInt(-1);
    this.signalCount++;
    this.outgoingOut.flush();
  }
*/
  public synchronized boolean readBoolean() throws IOException
  {
/*    if(this.readNow)
    {
      this.readNow = false;
      return this.read;
    }
    int n = this.outgoingIn.readInt();
    while(n < 0)
    {
      if(this.outgoingOut.echo)
        System.out.println("Unplanned signal read");
      this.signalCount--;
      n = this.outgoingIn.readInt();
    }*/
    return this.outgoingIn.readBoolean();
  }
/*
  public synchronized void readSignal() throws IOException
  {
    if(this.outgoingOut.echo)
      System.out.println("Reading signal " + this.signalCount);
    if(this.signalCount > 0)
    {
      if (this.outgoingIn.readInt() == 0)
      {
        if(this.outgoingOut.echo)
          System.out.println("Unplanned data read");
        this.read = this.outgoingIn.readBoolean();
        this.readNow = true;
        if(this.outgoingOut.echo)
          System.out.println("Reading late signal");
        this.outgoingIn.readInt();
      }
      this.signalCount--;
    }
  }
*/
  public synchronized void flush() throws IOException
  {
    this.outgoingOut.flush();
  }
/*
  public void run()
  {
    this.isActive = true;
    while(this.user == null)
      try
      {
        Thread.sleep(2000);
      }
      catch(Exception e) {}
    while(!this.user.hasDB)
      try
      {
        Thread.sleep(2000);
      }
      catch(Exception e) {}
    System.out.println("Starting heart beat");
    while(this.canBeRunning)
    {
        try
        {
          this.writeSignal();
          this.readSignal();
        }
        catch (Exception e)
        {
          System.out.println("Heart beat failed");
          this.isActive = false;
          return;
        }
      if(!this.canBeRunning) break;
      try
      {
        Thread.sleep(60000);
      }
      catch(Exception e) {}
    }
    this.isActive = false;
    System.out.println("Heart beat stopped");
  }*/
}

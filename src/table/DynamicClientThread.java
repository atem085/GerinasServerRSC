package table;

import javax.net.ssl.SSLSocket;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.io.IOException;
import data.*;
import utils.Consts;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DynamicClientThread extends IncomingClientThread
{
  private DynamicConnectionThread connectionThread;


  public DynamicClientThread(SSLSocket incomingSocket, DynamicConnectionThread connectionThread, int counter) throws IOException
  {
    this.timeOfCreation = new java.util.Date().getTime();
    this.incomingSocket = incomingSocket;
    this.connectionThread = connectionThread;
    this.counter = counter;
    this.incomingIn = new MyObjectInputStream(incomingSocket.getInputStream());
//    incomingIn.setEcho(true);
    this.incomingOut = new MyObjectOutputStream(this.incomingSocket.getOutputStream(), false);
//    incomingOut.setEcho(true);
    this.selectedTable = null;
    this.user = null;
    this.language = null;
    this.canBeRunning = true;
    this.subtableIndex = null;
    this.elementCode = null;
    this.tableName = null;
    this.incomingIn.setEncoding(false);
    this.incomingOut.setEncoding(false);
    super.setPriority(Thread.MAX_PRIORITY);
  }

  public void run()
  {
    int requestType = -1;

    for(int i = 0; this.canBeRunning; i++)
    {
      if(this.user != null)
        this.connectionThread.setUser(this.user);
      try
      {
        requestType = this.incomingIn.readInt();
      }
      catch(IOException e)
      {
        requestType = -1;
/*        try
        {
          Data.commander.forceOut(this);
        }
        catch(IOException e1)
        {
          break;
        }*/
        break;
      }
      try
      {
        switch(requestType)
        {
        }
//        Data.commander.readAndExecute(this, this.incomingIn, this.incomingOut, requestType, true);
      }
      catch(Exception e)
      {
//        try
//        {
//          Data.commander.forceOut(this);
          break;
//        }
//        catch(Exception eee)
//        {
//          break;
//        }
      }
    }
  }

}

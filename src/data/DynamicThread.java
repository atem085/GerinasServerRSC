package data;

import javax.net.ssl.SSLSocket;
import table.DynamicClientThread;
import table.OutgoingChannel;
import table.DynamicConnectionThread;
import utils.ObjectZipOutputStream;
import java.io.IOException;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class DynamicThread extends Thread
{
  public DynamicThread()
  {
  }

  public void run()
  {
    SSLSocket incomingSocket = null;
    SSLSocket connectionSocket = null;
//    IncomingClientThread incomingClientThread = null;
//    ConnectionThread connectionThread = null;
    int counter = 0;
    while(Data.canBeRunning)
    {
      try
      {
        connectionSocket = (SSLSocket)Data.dynamicConnectionServerSocket.accept();
//        incomingSocket = (SSLSocket)Data.dynamicServerSocket.accept();
//        connectionThread = new ConnectionThread(connectionSocket, counter);
//        incomingClientThread  = new IncomingClientThread(incomingSocket, connectionThread, dbStream, updateStream, counter);
      }
      catch(IOException e)
      {
        System.out.println("Users cannot be accepted");
//        e.printStackTrace();
        continue;
      }
      System.out.println("User " + counter + " has been successfully connected");
//      Data.incomingClientThreads.add(incomingClientThread);
//      incomingClientThread.start();
//      connectionThread.start();
      counter++;
    }
  }

}

package data;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import table.HttpClientThread;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class HttpThread extends Thread
{
  public HttpThread()
  {
  }

  public void run()
  {
    SSLSocket httpSocket = null;
    HttpClientThread httpClientThread = null;
    int counter = 0;
    while(Data.canBeRunning)
    {
      try
      {
//        httpSocket = (SSLSocket)Data.httpServerSocket.accept();
        httpClientThread  = new HttpClientThread(httpSocket, counter);
      }
      catch(IOException e)
      {
        System.out.println("Users cannot be accepted");
        e.printStackTrace();
        continue;
      }
      System.out.println("User " + counter + " has been successfully connected");
      httpClientThread.start();
      counter++;
    }
  }

}

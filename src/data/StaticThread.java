package data;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import table.IncomingClientThread;
import table.OutgoingChannel;
import table.ConnectionThread;
import utils.ObjectZipOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class StaticThread extends Thread
{

  private Object lock = new Object();

  public StaticThread()
  {

  }

  /*
    public static void createRule()
    {
      try
      {
        Process p = Runtime.getRuntime().exec("runas /savecred /user:Administrator \"netsh advfirewall firewall add rule name=RemoteIPsblocken dir=in action=block remoteip=85.93.20.170\"");
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        while ((line = bri.readLine()) != null)
        {
          System.out.println(line);
        }
        bri.close();
        while ((line = bre.readLine()) != null)
        {
          System.out.println(line);
        }
        bre.close();
        p.waitFor();
        System.out.println("Done creating the rule.");
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  */
  public static String getBlockedIPs()
  {
    try
    {
      Process p = Runtime.getRuntime().exec("netsh advfirewall firewall show rule name=RemoteIPsblocken");
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = bri.readLine()) != null)
      {
        if(line.startsWith("Remote"))
        {
          line = line.substring(41, line.length());
          line = line.replaceAll("/32", "");
          System.out.println(line);
          bri.close();
          p.waitFor();
          System.out.println("Done showing the rule.");
          return line;
        }
      }
      bri.close();
      p.waitFor();
      System.out.println("Done showing the rule.");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public static void setRule(String ips, String additional)
  {
    try
    {
      Process p = Runtime.getRuntime().exec("netsh advfirewall firewall set rule name=RemoteIPsblocken new remoteip=" + ips + "," + additional + "\"");
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = bri.readLine()) != null)
        System.out.println(line);
      bri.close();
      p.waitFor();
      System.out.println("Done changing the rule.");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void run()
  {
    try
    {
      Data.incomingServerSocket.setReuseAddress(true);
      Data.dbServerSocket.setReuseAddress(true);
      Data.updateServerSocket.setReuseAddress(true);
      Data.outgoingServerSocket.setReuseAddress(true);
      Data.connectionServerSocket.setReuseAddress(true);
    }
    catch(Exception e) {}

    SSLSocket incomingSocket = null;
    SSLSocket dbSocket = null;
    SSLSocket updateSocket = null;
    SSLSocket outgoingSocket = null;
    SSLSocket connectionSocket = null;
    IncomingClientThread incomingClientThread = null;
    OutgoingChannel outgoingChannel = null;
    ConnectionThread connectionThread = null;
    InetAddress connectionAddress, dbAddress, updateAddress, incomingAddress, outgoingAddress;
    int connectionPort, dbPort, updatePort, incomingPort, outgoingPort;
    int counter = 0;
    int so = 20000;
    boolean failed = false;
    int i;
    while(Data.canBeRunning)
    {
      synchronized (this.lock)
      {
        if (failed)
        {
          try
          {
            incomingSocket.close();
            incomingSocket = null;
          }
          catch (Exception ex)
          {}
          try
          {
            dbSocket.close();
            dbSocket = null;
          }
          catch (Exception ex)
          {}
          try
          {
            updateSocket.close();
            updateSocket = null;
          }
          catch (Exception ex)
          {}
          try
          {
            outgoingSocket.close();
            outgoingSocket = null;
          }
          catch (Exception ex)
          {}
          try
          {
            connectionSocket.close();
            connectionSocket = null;
          }
          catch (Exception ex)
          {}
          incomingClientThread = null;
          outgoingChannel = null;
          connectionThread = null;
          connectionAddress = null;
          dbAddress = null;
          updateAddress = null;
          incomingAddress = null;
          outgoingAddress = null;
          connectionPort = 0;
          dbPort = 0;
          updatePort = 0;
          incomingPort = 0;
          outgoingPort = 0;
          try
          {
            Data.connectionServerSocket.setSoTimeout(1);
            Data.dbServerSocket.setSoTimeout(1);
            Data.updateServerSocket.setSoTimeout(1);
            Data.incomingServerSocket.setSoTimeout(1);
            Data.outgoingServerSocket.setSoTimeout(1);
            boolean accepted = true;
            while (accepted)
              try
              {
                Data.connectionServerSocket.accept();
              }
              catch (Exception e)
              {
                accepted = false;
              }
            accepted = true;
            while (accepted)
              try
              {
                Data.dbServerSocket.accept();
              }
              catch (Exception e)
              {
                accepted = false;
              }
            accepted = true;
            while (accepted)
              try
              {
                Data.updateServerSocket.accept();
              }
              catch (Exception e)
              {
                accepted = false;
              }
            accepted = true;
            while (accepted)
              try
              {
                Data.incomingServerSocket.accept();
              }
              catch (Exception e)
              {
                accepted = false;
              }
            accepted = true;
            while (accepted)
              try
              {
                Data.outgoingServerSocket.accept();
              }
              catch (Exception e)
              {
                accepted = false;
              }
            Thread.sleep(100);
            Data.connectionServerSocket.setSoTimeout(0);
            Data.dbServerSocket.setSoTimeout(0);
            Data.updateServerSocket.setSoTimeout(0);
            Data.incomingServerSocket.setSoTimeout(0);
            Data.outgoingServerSocket.setSoTimeout(0);
          }
          catch (Exception e)
          {}
        }
        failed = false;

        try
        {
          connectionSocket = (SSLSocket) Data.connectionServerSocket.accept();
          connectionAddress = connectionSocket.getInetAddress();
          connectionPort = connectionSocket.getPort();
          System.out.println("connection\t" + connectionAddress + "\t" + connectionPort);
          Data.dbServerSocket.setSoTimeout(so);
          dbSocket = (SSLSocket) Data.dbServerSocket.accept();
          dbAddress = dbSocket.getInetAddress();
          dbPort = dbSocket.getPort();
          if (!connectionAddress.equals(dbAddress))
          {
            System.out.println("port scanning attack!!! db\t" + dbAddress + "\t" + dbPort);
            setRule(getBlockedIPs(), dbAddress.toString().substring(1, dbAddress.toString().length()));
            failed = true;
            throw new SocketTimeoutException("Probable port scanning!!! Attack has been rejected");
          }
          System.out.println("db\t" + dbAddress + "\t" + dbPort);
          Data.dbServerSocket.setSoTimeout(0);
          Data.updateServerSocket.setSoTimeout(so);
          updateSocket = (SSLSocket) Data.updateServerSocket.accept();
          updateAddress = updateSocket.getInetAddress();
          updatePort = updateSocket.getPort();
          if (!dbAddress.equals(updateAddress))
          {
            System.out.println("port scanning attack!!! update\t" + updateAddress + "\t" + updatePort);
            setRule(getBlockedIPs(), updateAddress.toString().substring(1, updateAddress.toString().length()));
            failed = true;
            throw new SocketTimeoutException("Probable port scanning!!! Attack has been rejected");
          }
          System.out.println("update\t" + updateAddress + "\t" + updatePort);
          Data.updateServerSocket.setSoTimeout(0);
          Data.incomingServerSocket.setSoTimeout(so);
          incomingSocket = (SSLSocket) Data.incomingServerSocket.accept();
          incomingAddress = incomingSocket.getInetAddress();
          incomingPort = incomingSocket.getPort();
          if (!updateAddress.equals(incomingAddress))
          {
            System.out.println("port scanning attack incoming\t" + incomingAddress + "\t" + incomingPort);
            setRule(getBlockedIPs(), incomingAddress.toString().substring(1, incomingAddress.toString().length()));
            failed = true;
            throw new SocketTimeoutException("Probable port scanning!!! Attack has been rejected");
          }
          System.out.println("incoming\t" + incomingAddress + "\t" + incomingPort);
          Data.incomingServerSocket.setSoTimeout(0);
          Data.outgoingServerSocket.setSoTimeout(so);
          outgoingSocket = (SSLSocket) Data.outgoingServerSocket.accept();
          outgoingAddress = outgoingSocket.getInetAddress();
          outgoingPort = outgoingSocket.getPort();
          if (!incomingAddress.equals(outgoingAddress))
          {
            System.out.println("port scanning attack!!! outgoing\t" + outgoingAddress + "\t" + outgoingPort);
            setRule(getBlockedIPs(), outgoingAddress.toString().substring(1, outgoingAddress.toString().length()));
            failed = true;
            throw new SocketTimeoutException("Probable port scanning!!! Attack has been rejected");
          }
          System.out.println("outgoing\t" + outgoingAddress + "\t" + outgoingPort);
          Data.outgoingServerSocket.setSoTimeout(0);
          connectionThread = new ConnectionThread(connectionSocket, counter);
          System.out.println("clientThread ok");
          ObjectZipOutputStream dbStream = new ObjectZipOutputStream(dbSocket.getOutputStream());
          System.out.println("dbStream ok");
          ObjectZipOutputStream updateStream = new ObjectZipOutputStream(updateSocket.getOutputStream());
          System.out.println("updateStream ok");
          incomingClientThread = new IncomingClientThread(incomingSocket, connectionThread, dbStream,
              updateStream, counter);
          System.out.println("incomingClientThread ok");
          outgoingChannel = new OutgoingChannel(outgoingSocket, counter);
          System.out.println("outgoingChannel ok");
        }
        catch (SocketTimeoutException e1)
        {
          failed = true;
          System.out.println("Probable port scanning!!!  Attack has been rejected");
          continue;
        }
        catch (IOException e)
        {
          failed = true;
          System.out.println("Aborting accepting user");
          continue;
        }
        catch (Exception e2)
        {
          failed = true;
          System.out.println("Something is wrong!!!");
          e2.printStackTrace();
          continue;
        }
        try
        {
          System.out.println("User " + counter + " has been successfully connected");
          Data.incomingClientThreads.add(incomingClientThread);
          System.out.println("Incoming client thread was add");
          Data.outgoingChannels.add(outgoingChannel);
          System.out.println("Outgoing channel was add");
          incomingClientThread.start();
          System.out.println("Incoming client thread started");
          connectionThread.start();
          System.out.println("Connection thread started");
//          outgoingChannel.start();
          System.out.println("Outgoing channel started");
          counter++;
        }
        catch (Exception e)
        {
          System.out.println("Failed");
          e.printStackTrace();
        }
      }
    }
  }

}
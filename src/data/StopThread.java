package data;

import java.io.File;
import table.Command25;
import table.OutgoingChannel;
import table.IncomingClientThread;
import javax.swing.JOptionPane;
import utils.MyObjectInputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;
import utils.Consts;
import table.StaticUser;
import table.Table;
import table.User;
import element.EmployeeUser;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import table.CheckAnswersThread;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class StopThread extends Thread
{

  private boolean askToBackup, backup;
  private String backupPath;

  public StopThread()
  {
    this.askToBackup = true;
    this.backup = false;
    this.backupPath = "";
  }

  public StopThread(boolean askToBackup, boolean backup, String backupPath)
  {
    this.askToBackup = askToBackup;
    this.backup = backup;
    this.backupPath = backupPath;
  }

  public void run()
  {
    if(Data.canBeRunning)
    {
    Data.ssButton.setText("Start");
    System.out.println("Stopping services...");
    Data.canBeRunning = false;
    System.out.println("Last connection allowed!");
    Command25 cc = new Command25();
    if(Data.outgoingChannels.size() > 0)
     System.out.println("Asking clients to politely log out...");
    for(int i = 0; i < Data.outgoingChannels.size(); i++)
      try
      {
        cc.write(null,
                 (OutgoingChannel)Data.outgoingChannels.get(i), true);
      }
      catch(Exception e1)
      {
//        e1.printStackTrace();
        continue;
      }
    if(Data.outgoingChannels.size() > 0 && Data.incomingClientThreads.size() > 0)
      System.out.println(
          "Waiting for one minute for successfull polite logouts...");
      try
      {
        Data.incomingServerSocket.close();
        Data.outgoingServerSocket.close();
        Data.connectionServerSocket.close();
        Data.dbServerSocket.close();
        Data.updateServerSocket.close();
//        Data.dynamicServerSocket.close();
//        Data.dynamicConnectionServerSocket.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      if (Data.incomingClientThreads.size() > 0)
        System.out.println("Forcing " + Data.incomingClientThreads.size() +
                           " users out...");
      while (Data.incomingClientThreads.size() > 0)
        try
        {
          Data.commander.forceOut( (IncomingClientThread) Data.
                                  incomingClientThreads.
                                  remove(0));
        }
        catch (Exception e3)
        {
          e3.printStackTrace();
          continue;
        }
      try
      {
        Data.cat.halt();
      }
      catch (Exception e)
      {}
//      Data.webThread.halt();
      while (Data.outgoingChannels.size() > 0)
        Data.outgoingChannels.remove(0);
      System.out.println("Saving DB...");
      boolean error = false;
      try
      {
        Data.db.save();
        Data.db = null;
      }
      catch (Exception e4)
      {
        e4.printStackTrace();
        error = true;
      }
      System.out.println("Closing log stream...");
      try
      {
        Data.logStream.close();
      }
      catch (Exception e5)
      {
        e5.printStackTrace();
        error = true;
      }
      if (!error)
      {
        File f = new File(Data.path + "\\log.dat");
        if (!f.delete())
          System.out.println("Error deleting log");
      }
      System.out.println("Server stopped");
    }
    if(this.askToBackup)
    {
      int opt = JOptionPane.showConfirmDialog(Data.mainFrame, "Would you like to back the current db up?",
                                              "Backup", JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE);
      try
      {
        if (opt == 0)
          Data.backup();
      }
      catch (Exception e)
      {}
    }
    else
    {
      if(this.backup)
        Data.backup(Data.backupPath);
      Data.mainFrame.setVisible(false);
      Runtime.getRuntime().addShutdownHook(Data.hook);
      try
      {
        Thread.sleep(60000);
      }
      catch(Exception e) {}
      System.exit(0);
    }

  }

}
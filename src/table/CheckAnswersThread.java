package table;

import java.util.Vector;
import data.*;
/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class CheckAnswersThread extends Thread
{
  public Vector outgoingChannels;
  public Vector commands;
//  public Vector users;
  private boolean canBeRunning;
  private Object lock;

  public CheckAnswersThread()
  {
    this.outgoingChannels = new Vector();
    this.commands = new Vector();
//    this.users = new Vector();
    this.canBeRunning = true;
    this.lock = new Object();
    super.setPriority(Thread.MIN_PRIORITY);
  }

  public void halt()
  {
    this.canBeRunning = false;
  }
/*
  public void checkForExistingCommands(StaticUser user)
  {
    int c = 0;
    synchronized(this.lock)
    {
      for(int i = 0; i < this.outgoingChannels.size(); i++)
      {
        OutgoingChannel ch = (OutgoingChannel)this.outgoingChannels.get(i);
        if(ch.getUser() == user)
        {
          this.outgoingChannels.remove(i);
          user.addCommand((Command)commands.remove(i));
          c++;
          i--;
        }
      }
      System.out.println("Moved " + c + " commands of " + new String(user.name));
    }
  }
*/
  public void run()
  {
    while(this.canBeRunning)
    {
      try
      {
        Thread.sleep(5000);
      }
      catch(Exception e) {}
//      synchronized(this.lock)
//      {
        while (this.outgoingChannels.size() > 0 && this.commands.size() > 0)
        {
          OutgoingChannel ch = (OutgoingChannel)this.outgoingChannels.remove(0);
          Command cm = (Command) commands.remove(0);
//          StaticUser user = (StaticUser)this.users.remove(0);
          try
          {
            ch.readBoolean();
          }
          catch (Exception e)
          {
            if(ch.user != null)
              ch.user.addCommand(cm);
            if(ch.user != null && ((StaticUser)ch.user).hasDB)
              try
              {
                Data.commander.forceOut(ch);
              }
              catch(Exception ee) {}
/*
          System.out.println("Command send to user did not deliver any answer.  Adding command to user update sequence " + new String(user.name));
          user.addCommand(cm);
            if (user.hasDB)
              try
              {
                Data.commander.forceOut(ch);
              }
              catch (Exception ee)
              {}
*/
//          }
        }
      }
    }
  }

}
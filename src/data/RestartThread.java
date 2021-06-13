package data;

import java.util.GregorianCalendar;

public class RestartThread extends Thread
{

  public void run()
  {
    GregorianCalendar date = new GregorianCalendar(Data.year, Data.month, Data.day, Data.hour, Data.minute, 0);
    GregorianCalendar current = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH), date.get(GregorianCalendar.HOUR_OF_DAY), date.get(GregorianCalendar.MINUTE), 0);
    while(true)
    {
      GregorianCalendar today = new GregorianCalendar();
      today = new GregorianCalendar(today.get(GregorianCalendar.YEAR), today.get(GregorianCalendar.MONTH),
                                    today.get(GregorianCalendar.DAY_OF_MONTH),
                                    today.get(GregorianCalendar.HOUR_OF_DAY),
                                    today.get(GregorianCalendar.MINUTE), 0);
      if(Data.every == 0 && today.equals(date))
      {
        StopThread stopThread = new StopThread(false, Data.backup, Data.backupPath);
        stopThread.start();
        try
        {
          Thread.sleep(70000);
        }
        catch(Exception e) {}
      }
      else if(Data.every == 1 && today.after(date))
      {
        if(today.get(GregorianCalendar.HOUR_OF_DAY) == Data.hour && today.get(GregorianCalendar.MINUTE) == Data.minute)
        {
          StopThread stopThread = new StopThread(false, Data.backup, Data.backupPath);
          stopThread.start();
          try
          {
            Thread.sleep(70000);
          }
          catch(Exception e) {}
        }
      }
      else if(Data.every > 1 && today.after(date))
      {
        while(current.before(today))
          current.add(GregorianCalendar.DAY_OF_MONTH, Data.every);
        if(current.equals(today))
        {
          StopThread stopThread = new StopThread(false, Data.backup, Data.backupPath);
          stopThread.start();
          try
          {
            Thread.sleep(70000);
          }
          catch(Exception e) {}
        }
      }
      try
      {
        Thread.sleep(20000);
      }
      catch(Exception e) {}
    }
  }

}
package data;

import javax.net.ssl.SSLSocket;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import utils.Consts;
import table.Table;
import element.EmployeeUser;
import element.GuestBuyerUser;
import element.GuestUser;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ABCThread extends Thread
{

  public ABCThread()
  {
  }

  public void run()
  {
    SSLSocket abcSocket = null;
    while(Data.canBeRunning)
    {
      try
      {
        abcSocket = (SSLSocket)Data.abcServerSocket.accept();
        System.out.println("abc accepted");
        final SSLSocket as = abcSocket;
        Thread t = new Thread()
        {
          public void run()
          {
            try
            {
              System.out.println("abc thread started");
              MyObjectInputStream moi = null;
              MyObjectOutputStream moo = null;
              moi = new MyObjectInputStream(as.getInputStream());
              System.out.println("abc in created");

              moo = new MyObjectOutputStream(as.getOutputStream(), false);
              moo.setEncoding(false);

              moi.setEncoding(false);

              System.out.println("abc out created");

              as.setSoTimeout(20000);

              String userName = moi.readUTF();
              String password = moi.readUTF();
              int userType = moi.readInt();
              System.out.println("abc " + userName);

              if (userType == 0)
              {
                EmployeeUser eu = (EmployeeUser) Data.db.getTable(Consts.tbUsers).findElement(Table.
                    dtJSHashCode(
                    userName));
                if(eu == null || !password.equals(new String(eu.password)))
                {
                  moo.writeInt(0);
                  moo.writeInt(0);
                  moo.writeInt(0);
                }
                else
                {
//                  moo.writeInt(eu.a);
//                  moo.writeInt(eu.b);
//                  moo.writeInt(eu.c);
                }
              }
              else if (userType == 1)
              {
                GuestUser eu = (GuestUser) Data.db.getTable(Consts.tbGuestUsers).findElement(Table.
                    dtJSHashCode(
                    userName));
                if(eu == null || !password.equals(new String(eu.password)))
                {
                  moo.writeInt(0);
                  moo.writeInt(0);
                  moo.writeInt(0);
                }
                else
                {
//                  moo.writeInt(eu.a);
//                  moo.writeInt(eu.b);
//                  moo.writeInt(eu.c);
                }
              }
              else if (userType == 2)
              {
                GuestBuyerUser eu = (GuestBuyerUser) Data.db.getTable(Consts.tbGuestBuyerUsers).findElement(
                    Table.
                    dtJSHashCode(userName));
                if(eu == null || !password.equals(new String(eu.password)))
                {
                  moo.writeInt(0);
                  moo.writeInt(0);
                  moo.writeInt(0);
                }
                else
                {
//                  moo.writeInt(eu.a);
//                  moo.writeInt(eu.b);
//                  moo.writeInt(eu.c);
                }
              }
              moo.flush();

              moi.close();

              moo.close();

              as.close();
            }
            catch(Exception e)
            {
              e.printStackTrace();
              System.out.println("ERROR sending ABC");
            }
          }
        };
        t.start();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

}
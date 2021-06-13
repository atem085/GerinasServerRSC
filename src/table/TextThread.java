package table;

import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import data.Data;
import utils.Consts;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class TextThread extends Thread
{
  private String path = "C:\\RSCTomcatGate";
  private String requestPath = path + "\\requests";
  private String answerPath = path + "\\answers";
  private String htmlPath = path + "\\html";

  public TextThread()
  {
    super();
    File f = new File(this.path);
    if(!f.exists())
      f.mkdir();
    f = new File(this.requestPath);
    if(!f.exists())
      f.mkdir();
    f = new File(this.answerPath);
    if(!f.exists())
      f.mkdir();
    f = new File(this.htmlPath);
    if(!f.exists())
      f.mkdir();
  }

  public void run()
  {
    String userName, password, tableName;
    int length, criteria;
    long[] elementCode;
    int[] subtableIndex;
    boolean view, ascending;
    File f = new File(this.requestPath);
    File request = null;
    while(Data.canBeRunning)
    {
      userName = null;
      password = null;
      tableName = null;
      length = 0;
      criteria = 0;
      view = false;
      if(request != null)
        request.delete();
      while(f.listFiles().length == 0)
        try
        {
          Thread.sleep(500);
        }
        catch(Exception e) {}
      request = f.listFiles()[0];
      System.out.println("Request " + request.getAbsolutePath());
      ObjectInputStream in = null;
      try
      {
        in = new ObjectInputStream(new FileInputStream(request));
        view = in.readBoolean();
        userName = in.readUTF();
        password = in.readUTF();
        tableName = in.readUTF();
        criteria = in.readInt();
        ascending = in.readBoolean();
        length = in.readInt();
      }
      catch(Exception e)
      {
        e.printStackTrace();
        try
        {
          in.close();
        }
        catch(Exception ee) {}
        continue;
      }
      System.out.println("" + view + "\t" + userName + "\t" + password + "\t" + tableName + "\t" + criteria + "\t" + ascending + "\t" + length);
      ObjectOutputStream out = null;
      try
      {
        out = new ObjectOutputStream(new FileOutputStream(this.answerPath +
            "\\" + request.getName()));
      }
      catch(Exception e)
      {
        e.printStackTrace();
        try
        {
          in.close();
        }
        catch(Exception ee) {}
        continue;
      }
      Table t = Data.db.getTable(Consts.tbWebUsers);
      if(t == null)
      {
        try
        {
          out.writeBoolean(false);
          in.close();
          out.close();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        try
        {
          in.close();
        }
        catch(Exception e) {}
        continue;
      }
      try
      {
        out.writeBoolean(true);
      }
      catch(Exception e) {}
      System.out.println("Table users found");
      TableElement us = t.findElement(Table.dtJSHashCode(userName));
      if(us == null)
      {
        try
        {
          out.writeBoolean(false);
          out.close();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        try
        {
          in.close();
        }
        catch(Exception e) {}
        continue;
      }
      try
      {
        out.writeBoolean(true);
      }
      catch(Exception e) {}
      System.out.println("User found");
      if(!new String(((User)us).password).equals(password))
      {
        try
        {
          out.writeBoolean(false);
          out.close();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        try
        {
          in.close();
        }
        catch(Exception e) {}
        continue;
      }
      try
      {
        out.writeBoolean(true);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      System.out.println("Password correct");
      subtableIndex = new int[length];
      elementCode = new long[length];
      try
      {
        for(int i = 0; i < length; i++)
          elementCode[i] = in.readLong();
        for(int i = 0; i < length; i++)
          subtableIndex[i] = in.readInt();
      }
      catch(Exception e)
      {
        try
        {
          out.writeBoolean(false);
          out.close();
          in.close();
        }
        catch(Exception ee) {}
        continue;
      }
      try
      {
        out.writeBoolean(true);
      }
      catch(Exception e) {}
      System.out.println("Subtable read");
      if(length == 0)
        t = Data.db.getTable(tableName);
      else
        t = Data.db.getTable(tableName, elementCode, subtableIndex);
      if(t == null)
       {
         try
         {
           out.writeBoolean(false);
           out.close();
           in.close();
         }
         catch(Exception e) {}
         continue;
       }
      try
      {
        out.writeBoolean(true);
        out.close();
        in.close();
      }
      catch(Exception e) {}
      System.out.println("Table found");
      PrintWriter pout = null;
      try
      {
        pout = new PrintWriter(new FileOutputStream(this.htmlPath + "\\" +
            request.getName()));
      }
      catch(Exception e) {continue;}
      t.getExample().printTable(pout, view, criteria, true, (User)us);
      try
      {
        pout.close();
      }
      catch(Exception e) {}
      System.out.println("HTML ready");
    }
  }

}
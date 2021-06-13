package table;

import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import utils.ObjectZipOutputStream;
import utils.Progress;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.ProgressMonitor;
import data.Data;
import utils.DateRepresentation;
import java.util.StringTokenizer;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import java.util.Random;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DB
{

  private Table[] tables;
  public String path;
  public boolean success;
  private boolean internetConnection = false;
  private String trialStartDate = new DateRepresentation(true).toString();
  private String lastLicenceMessage = "ok";
  private int counter = 0;
  private CheckLicence cl;

  private DB()
  {
    this.path = null;
    this.success = true;
    this.tables = new Table[Data.names.length];
    for(int i = 0; i < Data.names.length; i++)
      this.tables[i] = new Table(Data.names[i], Data.elementExamples[i], Data.howToRead[i], Data.firstCodes[i], null, null);
  }

  public DB(String path, String[] names, TableElementExample[] elementExamples, long[] firstCodes, int[] howToRead)
  {
    this.success = true;
    this.path = path;
    this.tables = new Table[names.length];
    for(int i = 0; i < names.length; i++)
      this.tables[i] = new Table(names[i], elementExamples[i], howToRead[i], firstCodes[i], null, null);
  }

  private boolean evaluateLicenceMessage(String message)
  {
    if(message.equalsIgnoreCase("ok")) return true;
    StringTokenizer st = new StringTokenizer(message, "-");
    if(!st.hasMoreTokens()) return false;
    if(st.nextToken().equalsIgnoreCase("ok")) return true;
    return false;
  }

  private class CheckLicence extends Thread
  {
    private DB db;
    private String previousHost = null;

    public CheckLicence(DB db)
    {
      this.db = db;
      this.load();
    }

    public void run()
    {
      boolean ic = false;
      URL url = null;
      boolean first = true;
      long waitTime = 3600000;
//      int waitTime = 60000;
      int maxCounter = 2159;
//      int maxCounter = 5;
      while(!ic)
      {
        db.counter++;
        if(!first && !db.internetConnection && (new DateRepresentation(true).calculateDifference(new DateRepresentation(db.trialStartDate)) > 89 || counter > maxCounter))
        {
          db.lastLicenceMessage = "You have reached the deadline of your licence.  Please prolong it calling +49 176 34928125";
          Data.ssButton.doClick();
          System.out.println(db.lastLicenceMessage);
          JOptionPane.showConfirmDialog(Data.mainFrame, db.lastLicenceMessage, "Licence",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
          System.exit(0);
        }
        first = false;
        String llm = db.lastLicenceMessage;
        try
        {
          if(this.previousHost == null)
            url = new URL("http://www.itconsulting24.de/jserv/checklicence?versionID=" +
                            Data.versionID + "&trial=" + Data.trial + "&licencedTo=" + Data.licencedTo +
                            "&deadline=" + Data.licenceDeadline);
          else
            url = new URL("http://www.itconsulting24.de/jserv/checklicence?versionID=" +
                            Data.versionID + "&trial=" + Data.trial + "&licencedTo=" + Data.licencedTo +
                            "&deadline=" + Data.licenceDeadline + "&previousHost=" + this.previousHost);
        }
        catch(Exception e)
        {
          if(!db.evaluateLicenceMessage(llm))
          {
            Data.ssButton.doClick();
            JOptionPane.showConfirmDialog(Data.mainFrame, llm, "Licence",
                                          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            System.out.println(llm);
          }
          if(db.internetConnection)
          {
            db.internetConnection = false;
            db.trialStartDate = new DateRepresentation(true).toString();
          }
          try
          {
            Thread.sleep(3600000);
          }
          catch(Exception ee) {}
          continue;
        }
        BufferedReader br = null;
        try
        {
          br = new BufferedReader(new InputStreamReader(url.openStream()));
          db.lastLicenceMessage = br.readLine();
          try
          {
            this.previousHost = br.readLine();
          }
          catch(Exception e) {}
          db.internetConnection = true;
          db.counter = 0;
          System.out.println(db.lastLicenceMessage);
          if(db.evaluateLicenceMessage(db.lastLicenceMessage))
          {
            db.trialStartDate = new DateRepresentation(true).toString();
            try
            {
              Thread.sleep(waitTime);
            }
            catch(Exception e) {}
            continue;
          }
          Data.ssButton.doClick();
          JOptionPane.showConfirmDialog(Data.mainFrame, db.lastLicenceMessage, "Licence", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
          System.exit(0);
          System.out.println(db.lastLicenceMessage);
        }
        catch(Exception e)
        {
          if(!db.evaluateLicenceMessage(llm))
          {
            Data.ssButton.doClick();
            JOptionPane.showConfirmDialog(Data.mainFrame, db.lastLicenceMessage, "Licence",
                                          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            System.out.println(db.lastLicenceMessage);
          }
          if(db.internetConnection)
          {
            db.internetConnection = false;
            db.trialStartDate = new DateRepresentation(true).toString();
          }
        }
        try
        {
          Thread.sleep(waitTime);
        }
        catch(Exception e) {}
      }
    }

    public void save()
    {
      if(this.previousHost != null && !this.previousHost.equals(""))
      {
        try
        {
          MyObjectOutputStream h = new MyObjectOutputStream(new FileOutputStream(Data.path + "\\host.dat"), true);
          h.writeUTF(this.previousHost);
          h.close();
        }
        catch(Exception e) {e.printStackTrace();}
      }
    }

    public void load()
    {
      try
      {
        MyObjectInputStream h = new MyObjectInputStream(new FileInputStream(Data.path + "\\host.dat"));
        this.previousHost = h.readUTF();
        h.close();
      }
      catch(Exception e) {e.printStackTrace();}
    }
  }

  public void load()
  {
    MyObjectInputStream in = null;
    boolean error;
    Data.progress = new Progress("Loading db...", 0, this.tables.length - 1);
    Data.progress.setVisible(true);
    for(int i = 0; i < this.tables.length; i++)
    {
      Data.progress.setProgressOverall(i);
      Data.progress.setNote("Loading " + this.tables[i].getName() + "...");
      error = false;
      try
      {
        in = new MyObjectInputStream(new FileInputStream(path + "\\" + this.tables[i].getName() + ".dat"));
        in.setEncoding(false);
      }
      catch(IOException e)
      {
        error = true;
        in = null;
      }
      if(!error)
      {
        this.tables[i].load(in, true);
//        if(this.tables[i].getName().equals("transportation"))
//          JOptionPane.showConfirmDialog(null, this.tables[i].at(this.tables[i].size() - 1).getCode()));
        if(this.tables[i].getName().equals(Data.licenceDestination))
        {
          try
          {
            this.internetConnection = in.readBoolean();
            this.trialStartDate = in.readUTF();
            this.lastLicenceMessage = in.readUTF();
            this.counter = in.readInt();
          }
          catch(Exception eee) {}
        }
      }
      try
      {
        if(in != null)
         in.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      if(!this.tables[i].lastOperationResult)
        this.success = false;
    }
    this.cl = new CheckLicence(this);
    this.cl.start();
    Data.progress.dispose();
  }

  public static int getPosOfDependingIndex(int[] initialIndices, int dependingIndex)
  {
    for(int i = 0; i < initialIndices.length; i++)
      if(dependingIndex == initialIndices[i])
        return i;
    return -1;
  }

  public Table getTable(String name)
  {
    int pos = this.findTable(name);
    if(pos < 0 || pos >= this.tables.length)
      return null;
    return this.tables[pos];
  }

  public Table getSubtableExample(String name)
  {
    for(int i = 0; i < Data.reportName.length; i++)
      if(Data.reportName[i].equals(name))
        return new Table(Data.reportName[i], Data.reportExample[i], Data.reportHowToRead[i], 0, null, null);
    StringTokenizer st = new StringTokenizer(name, ".");
    String n = null;
    while(st.hasMoreTokens())
      n = st.nextToken();
    return this.getTable(n).getSubtable(name);
  }

  public static boolean canBeInstatiated(int[] selectedColumns, int[] initialParametersColumns)
  {
    boolean allowed;
    for(int i = 0; i < selectedColumns.length; i++)
    {
      if(selectedColumns[i] == -1) continue;
      allowed = false;
      for(int j = 0; j < initialParametersColumns.length; j++)
        allowed = allowed || selectedColumns[i] == initialParametersColumns[j];
      if(!allowed) return false;
    }
    return true;
  }

  public Table getTable(String name, long[] elementCode, int[] subtableIndex, User webUser, int userType)
  {
    int pos = this.findTable(name);
    if(pos < 0 || pos >= this.tables.length)
      return null;
    Table t = this.tables[pos];
    element.TableAuthorization ta = null;
    String tn = name;
    if(elementCode.length > 0)
    {
      if(userType == 0)
        ta = (element.TableAuthorization)((DynamicUser)webUser).getSubtable(0).findElement(Table.dtJSHashCode(
            name));
      else if(userType == 1)
        ta = (element.TableAuthorization)((element.DynamicGuestUser)webUser).getSubtable(0).findElement(Table.
            dtJSHashCode(name));
      t = t.filteredByDefault(ta, tn, userType);
    }
    for(int i = 0; i < elementCode.length; i++)
    {
      t = t.findElement(elementCode[i]).getSubtable(subtableIndex[i]);
      tn = subtableIndex[i] + "." + tn;
      if(userType == 0)
        ta = (element.TableAuthorization)((DynamicUser)webUser).getSubtable(0).findElement(Table.dtJSHashCode(tn));
      else if(userType == 1)
        ta = (element.TableAuthorization)((element.DynamicGuestUser)webUser).getSubtable(0).findElement(Table.dtJSHashCode(tn));
      if(i < elementCode.length - 1)
        t = t.filteredByDefault(ta, tn, userType);
    }
    return t;
  }

  public Table getTable(String name, long[] elementCode, int[] subtableIndex)
  {
    int pos = this.findTable(name);
    if(pos < 0 || pos >= this.tables.length)
      return null;
    Table t = this.tables[pos];
    for(int i = 0; i < elementCode.length; i++)
      t = t.findElement(elementCode[i]).getSubtable(subtableIndex[i]);
    return t;
  }

  public void save() throws Exception
  {
    Data.progress = new Progress("Saving db...", 0, this.tables.length - 1);
    Data.progress.setVisible(true);
    MyObjectOutputStream out = null;
    for(int i = 0; i < this.tables.length; i++)
    {
      Data.progress.setProgressOverall(i);
      Data.progress.setNote(this.tables[i].getName());
      out = new MyObjectOutputStream(new FileOutputStream(this.path + "\\" + this.tables[i].getName() + ".dat"), true);
      out.setEncoding(false);
      this.tables[i].save(out, true);
      if(this.tables[i].getName().equals(Data.licenceDestination))
      {
        out.writeBoolean(this.internetConnection);
        out.writeUTF(this.trialStartDate);
        if(this.lastLicenceMessage == null)
          out.writeUTF("ok");
        else
          out.writeUTF(this.lastLicenceMessage);
        out.writeInt(this.counter);
      }
      out.close();
    }
    File f = new File(Data.path + "\\version.dat");
      MyObjectOutputStream out2 = null;
      try
      {
        out2 = new MyObjectOutputStream(new FileOutputStream(f), true);
        out2.setEncoding(false);
        out2.writeLong(Data.version);
        out2.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      f = new File(Data.path + "\\backup.dat");
      out2 = null;
        try
        {
          out2 = new MyObjectOutputStream(new FileOutputStream(f), true);
          out2.setEncoding(false);
          out2.writeInt(Data.day);
          out2.writeInt(Data.month);
          out2.writeInt(Data.year);
          out2.writeInt(Data.hour);
          out2.writeInt(Data.minute);
          out2.writeInt(Data.every);
          out2.writeBoolean(Data.backup);
          out2.writeUTF(Data.backupPath);
          out2.close();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        Data.progress.dispose();
      this.cl.save();
  }

  public String toString()
  {
    String s = "";
    for(int i = 0; i < this.tables.length; i++)
      s += tables[i].toString();
    return s;
  }

  public void readAndSendSettings(MyObjectOutputStream out, MyObjectInputStream in) throws Exception
  {
    String tableName = in.readUTF();
    int subtableIndexLength = in.readInt();
    out.writeUTF(tableName);
    out.writeInt(subtableIndexLength);
    for(int i = 0; i < subtableIndexLength; i++)
      out.writeInt(in.readInt());
    int hc = in.readInt();
    out.writeInt(hc);
    for(int i = 0; i < hc; i++)
      out.writeInt(in.readInt());
    int mc = in.readInt();
    out.writeInt(mc);
    for(int i = 0; i < mc; i++)
      out.writeInt(in.readInt());
    for(int i = 0; i < mc; i++)
      out.writeInt(in.readInt());
  }

  public void readAndSendDefaults(MyObjectOutputStream out, MyObjectInputStream in) throws Exception
  {
    int size = in.readInt();
    out.writeInt(size);
    int length;
    for(int i = 0; i < size; i++)
    {
      out.writeUTF(in.readUTF());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      for(int j = 0; j < length; j++)
        out.writeInt(in.readInt());
      boolean n;
      length = in.readInt();
      out.writeInt(length);
      for(int j = 0; j < length; j++)
      {
        int sz = in.readInt();
        out.writeInt(sz);
        for(int k = 0; k < sz; k++)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
        n = in.readBoolean();
        out.writeBoolean(n);
        if(!n)
          out.writeObject(in.readObject());
      }
    }
  }

  private static void duplicateSubtables(TableElement current, TableElement duplicate)
  {
    Table sr = current.getSubtable(0);
    Table sc = duplicate.getSubtable(0);
    for(int i = 0; sr != null && sc != null && sc.size() < sr.size(); i++, sr = current.getSubtable(i), sc = duplicate.getSubtable(i))
    {
      for(int j = 0; j < sr.size(); j++)
      {
        TableElement cu = sr.at(j);
        TableElement du = cu.duplicate();
        sc.add(du);
        duplicateSubtables(cu, du);
      }
    }
  }

  public void send(MyObjectOutputStream out, MyObjectInputStream inn, ObjectZipOutputStream zout, User user) throws IOException, OutOfMemoryError
  {
    int userType;
    if(user instanceof element.EmployeeUser)
      userType = 0;
//    zout.prepare();
    out.setEncoding(false);
    zout.setRemember(true);
    long v = 0;
    System.out.println("Copying DB...");
    synchronized(Data.lock)
    {
      try
      {
        v = Data.version;
        for (int i = 0; i < this.tables.length; i++)
          this.tables[i].write(zout);
        user.isLoggedIn = true;
      }
      catch(Exception e) {}
    }
//    System.out.println("Starting heart beat");
//    heartBeat.start();
    System.out.println("Packing DB...");
    zout.release();
    System.out.println("Sending DB...");
    zout.close(false);
//    System.out.println("Stopping heart beat...");
//    heartBeat.halt();
//    while(heartBeat.isActive())
//    {
//      try
//      {
//        Thread.sleep(1000);
//      }
//      catch(Exception e) {}
//    }
    System.out.println("Synchronizing...");
    out.writeLong(v);
    out.flush();
    System.out.println("Sending settings...");
    try
    {
      userType = 0;
      File f = new File(Data.path + "\\" + new String(user.name) + userType);
      if(!f.exists())
        f.mkdir();
      f = new File(Data.path + "\\" + new String(user.name) + userType + "\\settings.dat");
      out.writeBoolean(f.exists() && f.length() > 0);
      out.flush();
      int tc = 0;
      if(f.exists() && f.length() > 0)
      {
        MyObjectInputStream in = new MyObjectInputStream(new FileInputStream(f));
        in.setEncoding(false);
        tc = in.readInt();
//        out.setEcho(true);
        out.writeInt(tc);
        for(int i = 0; i < tc; i++)
          try
          {
            this.readAndSendSettings(out, in);
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }
//        out.setEcho(false);
        in.close();
      }
      System.out.println("Sending defaults...");
      f = new File(Data.path + "\\" + new String(user.name) + userType + "\\defaults.dat");
      out.writeBoolean(f.exists() && f.length() > 0);
      out.flush();
      if(f.exists() && f.length() > 0)
      {
        MyObjectInputStream in = new MyObjectInputStream(new FileInputStream(f));
        in.setEncoding(false);
//        in.setEcho(true);
        for(int i = 0; i < tc; i++)
          try
          {
            this.readAndSendDefaults(out, in);
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }
//        out.setEcho(false);
        in.close();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Finishing up...");
    out.flush();
    System.out.println("Finished");
  }

  public Table[] getTables()
  {
    return this.tables;
  }

  public Object[] getTableNames()
  {
    Object[] o = new Object[this.tables.length];
    for(int i = 0; i < o.length; i++)
      o[i] = this.tables[i].getName();
    return o;
  }

  private int findTable(String name)
  {
    for(int i = 0; i < this.tables.length; i++)
      if(this.tables[i].getName().equals(name))
        return i;
    return -1;
  }

}

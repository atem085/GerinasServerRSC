package element;

import table.TableElementExample;
import table.TableElement;
import table.*;
import table.User;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import utils.Consts;
import data.Data;
import data.StopThread;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import utils.MyObjectOutputStream;

/**
 *
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert den Benutzer.<b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class EmployeeUserExample implements TableElementExample
{
  /**
   * der Name des Benutzers.
   */
  public char[] name;
  /**
   * das Kennwort des Benutzers.
   */
  public char[] password;
  /**
   * Das Status des Benutzers(ob er eingelogt ist).
   */
  public boolean isLoggedIn;
  private char[] realName;
  /**
   * Die zugeh&ouml;rige Kommanden.
   */
  public ArrayList commands;

  public Table settings;
  /**
   * Code.<br>
   * Code wird nach den folgenden Parametern berechnet:<br><br>
   * <b>Name des Benutzers<br>Kennwort des Benutzers</b>
   */
  private long code;

  public boolean needsUpdate;

  public EmployeeUserExample(String name, String password, String realName)
  {
    this.name = name.toCharArray();
    this.password = password.toCharArray();
    this.code = Table.dtJSHashCode(new String(name));
    this.isLoggedIn = false;
    this.realName = realName.toCharArray();
    this.commands = new ArrayList();
    this.needsUpdate = false;
    this.settings = new Table("settings_of_user_" + new String(this.name) + "_" + new String(this.password), new TableAuthorizationExample("", true), 1, 0, null, null);
  }

  /**
   * Diese Methode gibt den Namen der Spalten aus.<br>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Name<br>
   * 2. Kennwort<br>
   * 3. Status(Eingelogt(ja/nein))</b></font><br>
   * @param howToRead der Leseart
   * @param language die Sprache
   * @return die Namen der Spalten.
   */
  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clEmployeeUser(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public Class[] getColumnClasses(int howToRead)
  {
   Class[] c = new Class[5];
   c[0] = String.class;
   c[1] = String.class;
   c[2] = Boolean.class;
   c[3] = Boolean.class;
   c[4] = String.class;
   return c;
  }

  public Table getSubtable(int howToRead, int index)
  {
    return this.settings;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new EmployeeUser( (String)parameters[0], (String)parameters[1], (String)parameters[2]);
    }
    catch(Exception e)
    {
      //e.printStackTrace();
      return null;
    }
  }

  public boolean isAutoIncrementable(int howToRead)
  {
    return false;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    if(index == 0) return true;
    return false;
  }

  /**
   * Diese Methode wird f&uuml;r das Lesen eines Elementes verwendet.<br>
   * Es werden das kodierte Kennwort(anschliessend dekodiert) und alle Kommanden gelesen.
   * @param in MyObjectInputStream
   * @param howToRead der Leseart
   * @param parentTable die Obertabelle
   * @param parentElement das OberElement
   * @return das Element in der Tabelle
   */
  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      EmployeeUser eu = new EmployeeUser(in.readUTF(), in.readUTF(), in.readUTF());
      eu.isLoggedIn = in.readBoolean();
      eu.needsUpdate = in.readBoolean();
/*
      eu.a = in.readInt();
      eu.b = in.readInt();
      eu.c = in.readInt();*/
      eu.readCommands(in);
      eu.settings.load(in, false);
      for(int i = 0; i < eu.settings.size(); i++)
        try
        {
          Data.db.getSubtableExample(eu.settings.at(i).getValue(0, 1).toString());
        }
        catch(NullPointerException e)
        {
          eu.settings.remove(i);
          i--;
        }
       return eu;
     }
     catch(Exception e)
     {
//       e.printStackTrace();
       return null;
     }
   }


   public int getAmountColumnIndex(int howToRead)
   {
     return -2;
   }

   public boolean isSplittable(int columnIndex, int howToRead)
   {
     return false;
   }

   public String canBeMovedTo(int howToRead)
   {
     return "";
   }

   public int[] canBeMovedPath(int howToRead)
   {
     return null;
   }

   public int getMaxHowToRead()
   {
     return 1;
   }

   public void printTable(PrintWriter out, boolean view, int criteria, boolean ascending, User user)
   {

   }

   public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws Exception
   {
     if(howToRead == 1 && operationIndex == 0)
     {
       Table t = Data.db.getTable(Consts.tbUsers);
       for (int i = 0; i < t.size(); i++)
       {
         User user = (User) t.at(i);
         ( (StaticUser) user).needsUpdate = true;
         Table aut = t.at(i).getSubtable(0);
         for (int j = 0; j < aut.size(); j++)
         {
           TableAuthorization auth = (TableAuthorization) aut.at(j);
           String tableAuthorizationName = auth.getValue(0, 1).toString();
           Table tableExample = Data.db.getSubtableExample(tableAuthorizationName);
           Class[] columnClasses = tableExample.getExample().getColumnClasses(tableExample.getHowToRead());
           if (columnClasses.length == auth.columnSettings.size())
             continue;
           if (columnClasses.length > auth.columnSettings.size())
           {
             for (int k = auth.columnSettings.size(); k < columnClasses.length; k++)
             {
               ColumnAuthorization ca = new ColumnAuthorization(k, false, false, k);
               ca.filter = new Table("default_filter_of_column_" + ca.getCode().longValue() + "_of_table_" +
                                     tableAuthorizationName + "_of_user_" + user.getValue(0, 1) + "_" +
                                     user.getValue(1, 1), new ColumnFilterExample(columnClasses[k]), 1, 0, ca, null);
               auth.columnSettings.addTableElement(ca, "en", currentUser, date);
             }
           }
         }
       }
       t = Data.db.getTable(Consts.tbWebUsers);
       if (t != null)
         for (int i = 0; i < t.size(); i++)
         {
           User user = (User) t.at(i);
           Table aut = t.at(i).getSubtable(0);
           for (int j = 0; j < aut.size(); j++)
           {
             TableAuthorization auth = (TableAuthorization) aut.at(j);
             String tableAuthorizationName = auth.getValue(0, 1).toString();
             Table tableExample = Data.db.getSubtableExample(tableAuthorizationName);
             Class[] columnClasses = tableExample.getExample().getColumnClasses(tableExample.getHowToRead());
             if (columnClasses.length == auth.columnSettings.size())
               continue;
             if (columnClasses.length > auth.columnSettings.size())
             {
               for (int k = auth.columnSettings.size(); k < columnClasses.length; k++)
               {
                 ColumnAuthorization ca = new ColumnAuthorization(k, false, false, k);
                 ca.filter = new Table("default_filter_of_column_" + ca.getCode().longValue() + "_of_table_" +
                                       tableAuthorizationName + "_of_user_" + user.getValue(0, 1) + "_" +
                                       user.getValue(1, 1), new ColumnFilterExample(columnClasses[k]), 1, 0,
                                       ca, null);
                 auth.columnSettings.addTableElement(ca, "en", currentUser, date);
               }
             }
           }
         }
       t = Data.db.getTable(Consts.tbWebGuestUsers);
       if (t != null)
         for (int i = 0; i < t.size(); i++)
         {
           User user = (User) t.at(i);
           Table aut = t.at(i).getSubtable(0);
           for (int j = 0; j < aut.size(); j++)
           {
             TableAuthorization auth = (TableAuthorization) aut.at(j);
             String tableAuthorizationName = auth.getValue(0, 1).toString();
             Table tableExample = Data.db.getSubtableExample(tableAuthorizationName);
             Class[] columnClasses = tableExample.getExample().getColumnClasses(tableExample.getHowToRead());
             if (columnClasses.length == auth.columnSettings.size())
               continue;
             if (columnClasses.length > auth.columnSettings.size())
             {
               for (int k = auth.columnSettings.size(); k < columnClasses.length; k++)
               {
                 ColumnAuthorization ca = new ColumnAuthorization(k, false, false, k);
                 ca.filter = new Table("default_filter_of_column_" + ca.getCode().longValue() + "_of_table_" +
                                       tableAuthorizationName + "_of_user_" + user.getValue(0, 1) + "_" +
                                       user.getValue(1, 1), new ColumnFilterExample(columnClasses[k]), 1, 0,
                                       ca, null);
                 auth.columnSettings.addTableElement(ca, "en", currentUser, date);
               }
             }
           }
         }

       StopThread stopThread = new StopThread(false, Data.backup, Data.backupPath);
       stopThread.start();

       /*     File f = new File(Data.path);
            File[] files = f.listFiles();
            for(int i = 0; i < files.length; i++)
            {
              if(!files[i].isDirectory()) continue;
              File[] settings = files[i].listFiles();
              for(int j = 0; j < settings.length; j++)
              {
                ColumnSettings cs = new ColumnSettings(files[i].getAbsolutePath());
              }
            }*/
       return "";
     }
     EmployeeUser cu = (EmployeeUser)currentTable.findElement(selectedCode);
     String nn = parameters[0].toString();
     String np = new String(cu.password);
     EmployeeUser nu = new EmployeeUser(nn, np, "");
     Data.db.getTable(Consts.tbUsers).addTableElement(nu, "", currentUser, date);
     TableAuthorization nt;
     for(int i = 0; i < cu.getSubtable(0).size(); i++)
     {
       TableAuthorization ct = (TableAuthorization)cu.getSubtable(0).at(i);
       nt = null;
       if(!Character.isDigit(ct.getValue(0, 1).toString().charAt(0)))
       {
         nt = new TableAuthorization(ct.getValue(0, 1).toString(), ( (Boolean) ct.getValue(1, 1)).booleanValue());
         nu.getSubtable(0).addTableElement(nt, "en", currentUser, date);
       }
       else continue;
       for(int j = 2; j < 13; j++)
         nt.setValue(j, ct.getValue(j, 1), language, 1, currentUser, date);
       for(int j = 0; j < ct.getSubtable(0).size(); j++)
       {
         ColumnAuthorization cct = (ColumnAuthorization)ct.getSubtable(0).at(j);
         ColumnAuthorization nct = (ColumnAuthorization)nt.getSubtable(0).findElement(j);
         nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
         nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
         for(int k = 0; k < cct.getSubtable(0).size(); k++)
         {
           ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
           ColumnFilter nft = new ColumnFilter(cft.getValue(0, 1));
           nct.getSubtable(0).addTableElement(nft, "en", currentUser, date);
         }
       }
     }
     for(int i = 0; i < cu.getSubtable(0).size(); i++)
     {
       TableAuthorization ct = (TableAuthorization)cu.getSubtable(0).at(i);
       nt = null;
       if(!Character.isDigit(ct.getValue(0, 1).toString().charAt(0))) continue;
       nt = (TableAuthorization)nu.getSubtable(0).findElement(Table.dtJSHashCode(ct.getValue(0, 1).toString()));
       try
       {
         for (int j = 1; j < 13; j++)
           nt.setValue(j, ct.getValue(j, 1), language, 1, currentUser, date);
       }
       catch(Exception e)
       {
         e.printStackTrace();
         System.out.println(nt);
         System.out.println(ct.getValue(0, 1).toString());
         nt.setValue(1, ct.getValue(1, 1), language, 1, currentUser, date);
       }
       for(int j = 0; j < ct.getSubtable(0).size(); j++)
       {
         ColumnAuthorization cct = (ColumnAuthorization)ct.getSubtable(0).at(j);
         ColumnAuthorization nct = (ColumnAuthorization)nt.getSubtable(0).findElement(j);
         nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
         nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
         for(int k = 0; k < cct.getSubtable(0).size(); k++)
         {
           ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
           ColumnFilter nft = new ColumnFilter(cft.getValue(0, 1));
           nct.getSubtable(0).addTableElement(nft, "en", currentUser, date);
         }
       }
     }
     File cd = new File(Data.path + "\\" + new String(cu.name) + "0");
     if(!cd.exists()) return "";
     File nd = new File(Data.path + "\\" + new String(nu.name) + "0");
     if(!nd.exists() && !nd.mkdir())
       return "";
     File cdf = new File(cd.getAbsolutePath() + "\\defaults.dat");
     File ndf = new File(nd.getAbsolutePath() + "\\defaults.dat");
     FileInputStream fin = new FileInputStream(cdf);
     FileOutputStream fout = new FileOutputStream(ndf);
     while(fin.available() > 0)
       fout.write(fin.read());
     fin.close();
     fout.close();
     cdf = new File(cd.getAbsolutePath() + "\\settings.dat");
     ndf = new File(nd.getAbsolutePath() + "\\settings.dat");
     fin = new FileInputStream(cdf);
     fout = new FileOutputStream(ndf);
     while(fin.available() > 0)
       fout.write(fin.read());
     fin.close();
     fout.close();
     return "";
   }

   public String getOperationName(int howToRead, int operationIndex, String language)
   {
     if(howToRead == 1 && operationIndex == 0)
       return "Repare column settings";
     else if(howToRead == 1 && operationIndex == 1)
       return "Copy";
     return null;
   }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtEmployeeUser(language, 0);
  }

  public boolean isSaved(int column, int howToRead)
  {
    return true;
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code,
                               int operation, String language)
  {
  }

  public boolean isAddEnabled(int howToRead)
  {
    return false;
  }

  public boolean isRemoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isMoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isSetEnabled(int howToRead)
  {
    return false;
  }

  public String getAddConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getRemoveConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getSetConfirmation(long code, TableElement parentElement, String language, int howToRead,
                                   User user)
  {
    return "";
  }

  public String getMoveConfirmation(long code, TableElement parentElement, String language, int howToRead, User user)
  {
    return "";
  }

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement,
                                         String language, int howToRead, User user, String[] parameters)
  {
    return "";
  }
}
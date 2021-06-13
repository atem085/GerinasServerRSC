package element;

import table.TableElementExample;
import table.TableElement;
import table.*;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import java.io.IOException;
import table.User;
import java.util.ArrayList;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;
import data.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert den Benutzer.<b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DynamicGuestUserExample implements TableElementExample
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
  private TableElement customer;

  public Table settings;
  /**
   * Code.<br>
   * Code wird nach den folgenden Parametern berechnet:<br><br>
   * <b>Name des Benutzers<br>Kennwort des Benutzers</b>
   */
  private long code;

  public DynamicGuestUserExample(String name, String password, String realName, long codeCustomer)
  {
    this.name = name.toCharArray();
    this.password = password.toCharArray();
    this.isLoggedIn = false;
    this.realName = realName.toCharArray();
    this.code = Table.dtJSHashCode(new String(name));
    this.settings = new Table("settings_of_dynamic_guest_user_" + new String(this.name) + "_" + new String(this.password), new TableAuthorizationExample("", true), 1, 0, null, null);
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
    return Consts.clDynamicGuestUser(language);
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
   Class[] c = new Class[6];
   c[0] = String.class;
   c[1] = String.class;
   c[2] = Boolean.class;
   c[3] = String.class;
   c[4] = Long.class;
   c[5] = String.class;
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
      return new DynamicGuestUser( (String)parameters[0], (String)parameters[1], (String)parameters[2], ((Long)parameters[3]).longValue(), howToRead);
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
      DynamicGuestUser eu = new DynamicGuestUser(in.readUTF(), in.readUTF(), in.readUTF(), in.readLong(), howToRead);
      eu.isLoggedIn = in.readBoolean();
      eu.settings.load(in, false);
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
     if(operationIndex == 1)
     {
       long sourceCode = ((Long)parameters[0]).longValue();
       Object[] pars = new Object[4];
       TableElement ce = currentTable.findElement(selectedCode);
       pars[0] = ce.getValue(0, 1);
       pars[1] = ce.getValue(1, 1);
       pars[2] = ce.getValue(3, 1);
       pars[3] = ce.getValue(4, 1);
       currentTable.removeTableElement(selectedCode, "en", currentUser, date);
       Object[] exePars = new Object[3];
       exePars[0] = pars[0];
       exePars[1] = pars[3];
       this.executeOperation(howToRead, 0, sourceCode, selectedColumn, exePars, currentTable, language, currentUser, date);
       DynamicGuestUser cu = (DynamicGuestUser)currentTable.findElement(selectedCode);
       cu.password = pars[1].toString().toCharArray();
       cu.realName = pars[2].toString().toCharArray();
       return "";
     }
     if(operationIndex == 0)
     {
       DynamicGuestUser cu = (DynamicGuestUser)currentTable.findElement(selectedCode);
       String nn = parameters[0].toString();
       Long newCustomerCode = (Long)parameters[1];
       Long customerCode = cu.customer.getCode();
//       System.out.println("copying " + customerCode.longValue() + " to " + newCustomerCode.longValue());
       boolean sameCustomer = false;
       if(newCustomerCode.longValue() == customerCode.longValue())
         sameCustomer = true;
       String np = new String(cu.password);
       DynamicGuestUser nu = new DynamicGuestUser(nn, np, "", newCustomerCode.longValue(), howToRead);
       currentTable.addTableElement(nu, "", currentUser, date);
       TableAuthorization nt;
       ColumnFilter nft;
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
           if(nct == null)
             System.out.println(nt.toString(1));
           nct.order = cct.order;
           nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
           nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
           for(int k = 0; k < cct.getSubtable(0).size(); k++)
           {
             ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
             if(cct.getValue(0, 1).toString().equals("Login"))
               nft = new ColumnFilter(nn);
             else
             {
               if (!sameCustomer && cft.getValue(0, 1).equals(customerCode))
                 nft = new ColumnFilter(newCustomerCode);
               else
                 nft = new ColumnFilter(cft.getValue(0, 1));
             }
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
         for(int j = 1; j < 13; j++)
           nt.setValue(j, ct.getValue(j, 1), language, 1, currentUser, date);
         for(int j = 0; j < ct.getSubtable(0).size(); j++)
         {
           ColumnAuthorization cct = (ColumnAuthorization)ct.getSubtable(0).at(j);
           ColumnAuthorization nct = (ColumnAuthorization)nt.getSubtable(0).findElement(j);
           nct.order = cct.order;
           nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
           nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
           for(int k = 0; k < cct.getSubtable(0).size(); k++)
           {
             ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
             if(cct.getValue(0, 1).toString().equals("Login"))
               nft = new ColumnFilter(nn);
             else
             {
               if (!sameCustomer && cft.getValue(0, 1).equals(customerCode))
                 nft = new ColumnFilter(newCustomerCode);
               else
                 nft = new ColumnFilter(cft.getValue(0, 1));
             }
             nct.getSubtable(0).addTableElement(nft, "en", currentUser, date);
           }
         }
       }
       File cd = new File(Data.path + "\\" + new String(cu.name) + (howToRead + 1) + "1");
       if(!cd.exists()) return "";
       File nd = new File(Data.path + "\\" + new String(nu.name) + (howToRead + 1) + "1");
       if(!nd.exists() && !nd.mkdir())
         return "";
       for(int i = 0; i < cd.listFiles().length; i++)
       {
         File cdf = cd.listFiles()[i];
         if(cdf.isDirectory()) continue;
         String filename = cdf.getAbsolutePath().substring(cd.getAbsolutePath().length() + 1, cdf.getAbsolutePath().length());
         File ndf = new File(nd.getAbsolutePath() + "\\" + filename);
         FileInputStream fin = new FileInputStream(cdf);
         FileOutputStream fout = new FileOutputStream(ndf);
         while(fin.available() > 0)
           fout.write(fin.read());
         fin.close();
         fout.close();
       }
       return "";
     }
     return null;
   }

   public String getOperationName(int howToRead, int operationIndex, String language)
   {
     if(operationIndex == 0) return "Copy";
     if(operationIndex == 1) return "Apply";
     return null;
   }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtGuestUser(language, howToRead);
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

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
import data.Data;
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
public class GuestUserExample implements TableElementExample
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

  public GuestUserExample(String name, String password, String realName, long codeCustomer)
  {
    this.name = name.toCharArray();
    this.password = password.toCharArray();
    this.isLoggedIn = false;
    this.realName = realName.toCharArray();
    this.commands = new ArrayList();
    this.code = Table.dtJSHashCode(new String(name));
    this.needsUpdate = false;
    this.settings = new Table("settings_of_guest_user_" + new String(this.name) + "_" + new String(this.password), new TableAuthorizationExample("", true), 1, 0, null, null);
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
    return Consts.clGuestUser(language);
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
   Class[] c = new Class[7];
   c[0] = String.class;
   c[1] = String.class;
   c[2] = Boolean.class;
   c[3] = Boolean.class;
   c[4] = String.class;
   c[5] = Long.class;
   c[6] = String.class;
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
      return new GuestUser( (String)parameters[0], (String)parameters[1], (String)parameters[2], ((Long)parameters[3]).longValue());
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
    int comType;
    try
    {
      GuestUser eu = new GuestUser(in.readUTF(), in.readUTF(), in.readUTF(), in.readLong());
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
       //e.printStackTrace();
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
       if(operationIndex == 0)
       {
         TableElement user = this.newInstance(parameters, howToRead, currentTable.getParentElement());
         currentTable.addTableElement(user, language, currentUser, date);
         long customerCode = ( (Long) user.getValue(5, howToRead)).longValue();
         Table table = user.getSubtable(0);
         Object[] param;
         param = new Object[1];

         Table[] tables = Data.db.getTables();
         for(int i = 0; i < tables.length; i++)
         {
           param[0] = tables[i].getName();
           table.addTableElement(table.getExample().newInstance(param, table.getHowToRead(), table.getParentElement()), language, currentUser, date);
         }
         String[] canSee = {"Sent", "0.Sent", "1.Sent", "2.Sent", "Warehouse", "customers", "2.customers", "5.customers", "deliveries", "0.deliveries", "0.0.deliveries", "1.deliveries", "3.deliveries", "4.deliveries", "draftDeliveries", "0.draftDeliveries", "0.0.draftDeliveries", "1.draftDeliveries", "3.draftDeliveries", "4.draftDeliveries", "finalInvoicesA", "0.finalInvoicesA", "1.finalInvoicesA", "2.finalInvoicesA", "finalInvoicesB", "0.finalInvoicesB", "1.finalInvoicesB", "2.finalInvoicesB", "myDeliveries", "0.myDeliveries", "1.myDeliveries", "2.myDeliveries"};
         for(int i = 0; i < canSee.length; i++)
         {
           TableElement te = table.findElement(Table.dtJSHashCode(canSee[i]));
           if(canSee[i].equals("deliveries"))
             te.setValue(9, new Boolean(true), language, table.getHowToRead(), currentUser, date);
           else if(canSee[i].equals("myDeliveries"))
             te.setValue(10, new Boolean(true), language, table.getHowToRead(), currentUser, date);
           for(int j = 2; j < 9; j++)
             te.setValue(j, new Boolean(false), language, table.getHowToRead(), currentUser, date);
           if(canSee[i].equals("0.myDeliveries"))
             te.setValue(3, new Boolean(true), language, table.getHowToRead(), currentUser, date);
         }
         for(int i = 0; i < table.size(); i++)
         {
           boolean ok = true;
           TableElement te = table.at(i);
           for(int j = 0; j < canSee.length; j++)
             if(te.getValue(0, table.getHowToRead()).equals(canSee[j]))
               ok = false;
           if(!ok) continue;
           for(int j = 1; j < 9; j++)
             te.setValue(j, new Boolean(false), language, table.getHowToRead(), currentUser, date);
         }
         String[] needsFilter = {"3.Sent", "0.Sent", "1.Sent", "2.Sent", "Warehouse", "customers", "deliveries", "draftDeliveries", "finalInvoicesA", "2.finalInvoicesA", "finalInvoicesB", "2.finalInvoicesB", "0.myDeliveries", "1.myDeliveries", "2.myDeliveries", "3.myDeliveries"};
         for(int i = 0; i < needsFilter.length; i++)
         {
           TableElement te = table.findElement(Table.dtJSHashCode(needsFilter[i]));
           Table cs = te.getSubtable(0);
           int[] found = null;
           if(needsFilter[i].equals("customers"))
             found = cs.findTableElements("No.", 0, false);
           else
             found = cs.findTableElements("Customer no.", 0, false);
           if(found == null || found.length == 0 || found.length > 1)
           {
             found = cs.findTableElements("Cust. no.", 0, false);
             if(found == null || found.length == 0 || found.length > 1)
               return "Customer no. not found or found too many " + needsFilter[i];
           }
           cs = cs.at(found[0]).getSubtable(0);
           param[0] = new Long(customerCode);
           cs.addTableElement(cs.getExample().newInstance(param, cs.getHowToRead(), cs.getParentElement()), language, currentUser, date);
         }
         for(int i = 0; i < canSee.length; i++)
         {
           TableElement te = table.findElement(Table.dtJSHashCode(canSee[i]));
           Table cs = te.getSubtable(0);
           if(canSee[i].equals("Sent"))
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           else if(canSee[i].equals("0.Sent"))
           {
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(14).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(15).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("1.Sent"))
           {
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("2.Sent"))
           {
             cs.at(0).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(5).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(15).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("Warehouse"))
           {
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(14).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(15).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(28).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("customers"))
           {
             cs.at(0).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(5).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(6).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(7).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(11).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(12).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(13).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(16).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(17).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(18).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("deliveries"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(14).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(31).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(40).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(41).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(42).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("0.0.deliveries"))
           {
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(24).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("1.deliveries"))
           {
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
//             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(11).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(13).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("3.deliveries"))
           {
             cs.at(6).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("4.deliveries"))
           {
             cs.at(0).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(6).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(7).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(13).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("draftDeliveries"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("1.draftDeliveries"))
           {
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(11).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(13).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("3.draftDeliveries"))
           {
             cs.at(6).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("4.draftDeliveries"))
           {
             cs.at(0).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(6).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(7).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(13).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("finalInvoicesA"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(19).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("2.finalInvoicesA"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("finalInvoicesB"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(19).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("2.finalInvoicesB"))
           {
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("myDeliveries"))
           {
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(25).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("0.myDeliveries"))
           {
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(14).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(15).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("1.myDeliveries"))
           {
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
           else if(canSee[i].equals("2.myDeliveries"))
           {
             cs.at(0).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(1).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(2).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(3).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(4).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(5).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(8).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(9).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(10).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
             cs.at(15).setValue(1, new Boolean(false), language, cs.getHowToRead(), currentUser, date);
           }
         }
         return "";
       }
       GuestUser cu = (GuestUser)currentTable.findElement(selectedCode);
       String nn = parameters[0].toString();
       Long newCustomerCode = (Long)parameters[1];
       Long customerCode = cu.customer.getCode();
       boolean sameCustomer = false;
       if(newCustomerCode.longValue() == customerCode.longValue())
         sameCustomer = true;
       String np = new String(cu.password);
       GuestUser nu = new GuestUser(nn, np, "", newCustomerCode.longValue());
       Data.db.getTable(Consts.tbGuestUsers).addTableElement(nu, "", currentUser, date);
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
           nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
           nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
           for(int k = 0; k < cct.getSubtable(0).size(); k++)
           {
             ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
             if(!sameCustomer && cft.getValue(0, 1).equals(customerCode))
               nft = new ColumnFilter(newCustomerCode);
             else
               nft = new ColumnFilter(cft.getValue(0, 1));
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
           nct.setValue(1, cct.getValue(1, 1), language, 1, currentUser, date);
           nct.setValue(2, cct.getValue(2, 1), language, 1, currentUser, date);
           for(int k = 0; k < cct.getSubtable(0).size(); k++)
           {
             ColumnFilter cft = (ColumnFilter)cct.getSubtable(0).at(k);
             if(!sameCustomer && cft.getValue(0, 1).equals(customerCode))
               nft = new ColumnFilter(newCustomerCode);
             else
               nft = new ColumnFilter(cft.getValue(0, 1));
             nct.getSubtable(0).addTableElement(nft, "en", currentUser, date);
           }
         }
       }
       File cd = new File(Data.path + "\\" + new String(cu.name) + "1");
       if(!cd.exists()) return "";
       File nd = new File(Data.path + "\\" + new String(nu.name) + "1");
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
     if(operationIndex == 0)
       return "Add";
     else if(operationIndex == 1)
       return "Copy";
     return null;
   }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtGuestUser(language, 0);
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

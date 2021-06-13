package element;

import table.TableElementExample;
import table.TableElement;
import utils.*;
import table.User;
import table.Table;
import table.User;

import java.io.PrintWriter;
import utils.Consts;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class TableAuthorizationExample implements TableElementExample
{

  private long code;
  private char[] tableName;
  private boolean canSee;
  private boolean canEdit;
  private boolean canRemove;
  private boolean canAdd;
  private boolean canSet;
  private boolean canApply;
  private boolean canSplit;
  private boolean canMove;
  private Table columnSettings;

  public TableAuthorizationExample(String tableName, boolean canSee)
  {
    this.tableName = tableName.toCharArray();
    this.canSee = canSee;
    this.code = Table.dtJSHashCode(tableName);
    this.canEdit = true;
    this.canRemove = true;
    this.canAdd = true;
    this.canSet = true;
    this.canApply = true;
    this.canSplit = true;
    this.canMove = true;
    this.columnSettings = new Table("column_settings_of_table_" + tableName + "_of_user_", new ColumnAuthorizationExample(0, true), 1, 0, null, null);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clTableAuthorization(language);
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[15];
    c[0] = String.class;
    c[1] = Boolean.class;
    c[2] = Boolean.class;
    c[3] = Boolean.class;
    c[4] = Boolean.class;
    c[5] = Boolean.class;
    c[6] = Boolean.class;
    c[7] = Boolean.class;
    c[8] = Boolean.class;
    c[9] = Boolean.class;
    c[10] = Boolean.class;
    c[11] = Boolean.class;
    c[12] = Boolean.class;
    c[13] = Boolean.class;
    c[14] = String.class;
    return c;
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      TableAuthorization ta = new TableAuthorization(in.readUTF(), in.readBoolean());
/*
      if(ta.getValue(0, 1).toString().equals("0.0.draftDeliveries"))
      {
        System.out.println("correcting...");
        ta.tableName = "4.draftDeliveries".toCharArray();
        ta.code = Table.dtJSHashCode("4.draftDeliveries");
      }
      else if(ta.getValue(0, 1).toString().equals("0.0.deliveries"))
      {
        System.out.println("correcting...");
        ta.tableName = "4.deliveries".toCharArray();
        ta.code = Table.dtJSHashCode("4.deliveries");
      }*/
      ta.setParentElement(parentElement, howToRead);
      for(int i = 2; i < 14; i++)
        ta.setValue(i, new Boolean(in.readBoolean()), "en", 1, null, null);
//      for(int i = 2; i < 9; i++)
//        ta.setValue(i, new Boolean(in.readBoolean()), "en", 1);
      ta.columnSettings = new Table("column_settings_of_table_" + new String((String)ta.getValue(0, howToRead)) + "_of_user_" + new String(((User)parentElement).name) + "_" + new String(((User)parentElement).password), new ColumnAuthorizationExample(0, true), 1, -1, ta, parentTable);
      ta.columnSettings.load(in, false);
/*      if(value.equals("0.customers") || value.equals("0.buyers") || value.equals("0.firms") || value.equals("1.providers") || value.equals("1.suppliers"))
      {
        System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
//        TableAuthorization ta2 = new TableAuthorization("8.customers", false);
//        parentTable.addTableElement(ta2, "en");
        ta.columnSettings.remove(ta.columnSettings.size() - 1);
        ta.columnSettings.remove(ta.columnSettings.size() - 1);
      }
*/
/*
      String value = ta.getValue(0, 1).toString();
      if(value.equals("2.deliveries") || value.equals("3.deliveries"))
      {
        if(!foundDeliveries)
        {
          System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
          TableAuthorization ta2 = new TableAuthorization("4.deliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
          ta2 = new TableAuthorization("5.deliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
        }
        foundDeliveries = true;
      }
      else if(value.equals("2.draftDeliveries") || value.equals("3.draftDeliveries"))
      {
        if(!foundDraftDeliveries)
        {
          System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
          TableAuthorization ta2 = new TableAuthorization("4.draftDeliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
          ta2 = new TableAuthorization("5.draftDeliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
        }
        foundDraftDeliveries = true;
      }
      else if(value.equals("1.myDeliveries") || value.equals("2.myDeliveries") || value.equals("3.myDeliveries"))
      {
        if(!foundMyDeliveries)
        {
          System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
          TableAuthorization ta2 = new TableAuthorization("5.myDeliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
          ta2 = new TableAuthorization("6.myDeliveries",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
        }
        foundMyDeliveries = true;
      }
      else if(value.equals("1.Sent") || value.equals("2.Sent") || value.equals("3.Sent"))
      {
        if(!foundSent)
        {
          System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
          TableAuthorization ta2 = new TableAuthorization("5.Sent",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
          ta2 = new TableAuthorization("6.Sent",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
        }
        foundSent = true;
      }
      else if(value.equals("4.transportation") || value.equals("5.transportation") || value.equals("6.transportation") || value.equals("7.transportation"))
      {
        if(!foundTransportation)
        {
          System.out.println(parentElement.getValue(0, 1).toString() + " " + value);
          TableAuthorization ta2 = new TableAuthorization("8.transportation",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
          ta2 = new TableAuthorization("9.transportation",
              ( (Boolean) ta.getValue(1, 1)).booleanValue());
          parentTable.addTableElement(ta2, "en");
        }
        foundTransportation = true;
      }
*/
/*
      if(ta.getValue(0, 1).toString().equals(Consts.tbCategory))
      {
        TableAuthorization ta2 = new TableAuthorization(Consts.tbUniCategory, ((Boolean)ta.getValue(1, 1)).booleanValue());
        parentTable.addTableElement(ta2, "en");
      }
          */
/*
         if(ta.getValue(0, 1).equals("7.customers"))
      {
        if(found)
        {
          System.out.println(parentElement.getValue(0, 1).toString());
          return null;
        }
        found = true;
      }
          */
/*
         try
         {
           Table t = data.Data.db.getSubtableExample(ta.getValue(0, 1).toString());
         }
         catch(NullPointerException e)
         {
           System.out.println(parentElement.getValue(0, 1).toString() + " does not exist!!! Deleting...");
           return null;
         }*/
      return ta;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

//  boolean found = false;
  /*
  boolean foundDeliveries = false;
  boolean foundDraftDeliveries = false;
  boolean foundMyDeliveries = false;
  boolean foundSent = false;
  boolean foundTransportation = false;
*/
//  boolean userFound = false;

  public Table getSubtable(int howToRead, int index)
  {
    if(index == 0) return this.columnSettings;
    return null;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new TableAuthorization((String)parameters[0], true);
  }

  public boolean isSplittable(int columnIndex, int howToRead)
  {
    return false;
  }

  public int getAmountColumnIndex(int howToRead)
  {
    return -1;
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
    return null;
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    return null;
  }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtTableAuthorization(language);
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

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement, String language, int howToRead, User user, String[] parameters)
  {
    return "";
  }
}
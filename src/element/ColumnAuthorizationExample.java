package element;

import table.TableElementExample;
import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import table.Table;
import table.User;
import data.*;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class ColumnAuthorizationExample implements TableElementExample
{

  private int columnIndex;
  private boolean canSee;
  public Table filter;

  public ColumnAuthorizationExample(int columnIndex, boolean canSee)
  {
    this.canSee = canSee;
    this.columnIndex = columnIndex;
    this.filter = new Table("default_filter_of_column_" + this.columnIndex + "_of_table_" + "_of_user_", new ColumnFilterExample(null), 1, 0, null, null);
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clColumnAuthorization(language);
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[4];
    c[0] = String.class;
    c[1] = Boolean.class;
    c[2] = Boolean.class;
    c[3] = Integer.class;
    return c;
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

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      boolean ok = true;
//      int ci = in.readInt();
      ColumnAuthorization ta = new ColumnAuthorization(in.readInt(), in.readBoolean(), in.readBoolean(), in.readInt());
      Table t = null;
      try
      {
        t = Data.db.getSubtableExample((String)parentElement.getValue(0, 1));
      }
      catch(NullPointerException e)
      {
        System.out.println(parentElement.getValue(0, 1).toString() + " does not exist!!!");
//        ok = false;
      }
      Class c = null;
      if(t == null)
        c = Object.class;
      else
        try
        {
          c = t.getExample().getColumnClasses(t.getHowToRead())[ta.columnIndex];
        }
        catch(Exception e)
        {
          System.out.println("Column does not exist!!!");
          ok = false;
        }
      ta.filter = new Table("default_filter_of_column_" + ta.getCode().longValue() + "_of_table_" + parentElement.getValue(0, 1) + "_of_user_" + ((TableAuthorization)parentElement).getParentElement(1).getValue(0, 1) + "_" + ((TableAuthorization)parentElement).getParentElement(1).getValue(1, 1), new ColumnFilterExample(c), 1, 0, ta, parentElement.getParentElement(1).getSubtable(0));
      ta.filter.load(in, false);
      if(ok)
        return ta;
      return null;
    }
    catch(Exception e)
    {
//      e.printStackTrace();
      return null;
    }
  }

  public Table getSubtable(int howToRead, int index)
  {
    if(index == 0) return this.filter;
    return null;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new ColumnAuthorization(((Integer)parameters[0]).intValue(), true, true, ((Integer)parameters[0]).intValue());
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
    return new int[4];
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtColumnAuthorization(language);
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
    return true;
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
    Table t = parentElement.getSubtable(0);
    TableElement ca = t.findElement(code);
    String[] columnNames = this.getColumnNames(howToRead, language, user);
    String s = "<table>";
    s += "<tr>";
    s += "<td>" + columnNames[3] + " :</td>";
    s += "<td><input type=text name=0 value=\"" + ca.getValue(3, howToRead, user, language).toString() + "\"></td></tr>";
    s += "</table>";
    s += "<input type=hidden name=parameters value=1>";
    return s;
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

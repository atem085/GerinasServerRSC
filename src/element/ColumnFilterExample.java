package element;

import table.TableElementExample;
import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import table.User;
import table.Table;
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

public class ColumnFilterExample implements TableElementExample
{

  private Class c;

  public ColumnFilterExample(Class c)
  {
    this.c = c;
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clColumnFilter(language);
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[1];
    c[0] = this.c;
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
      return new ColumnFilter(in.readObject());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    String v = parameters[0].toString();
    try
    {
      if(this.c == String.class)
        return new ColumnFilter(v);
      else if(this.c == Integer.class)
        return new ColumnFilter(new Integer(v));
      else if(this.c == Long.class)
        return new ColumnFilter(new Long(v));
      else if(this.c == Double.class)
        return new ColumnFilter(new Double(v));
      if(this.c == Float.class)
        return new ColumnFilter(new Float(v));
      if(this.c == Boolean.class)
        return new ColumnFilter(new Boolean(v));
    }
    catch(Exception e) {}
    return null;
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
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    return null;
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
    return Consts.gtColumnFilter(language);
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
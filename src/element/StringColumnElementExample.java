package element;

import table.TableElementExample;
import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import table.Table;
import java.io.File;
import utils.Consts;
import java.io.PrintWriter;

import table.User;
import utils.MyObjectOutputStream;

public class StringColumnElementExample implements TableElementExample
{
  int howToRead;

  public StringColumnElementExample(int howToRead)
  {
    this.howToRead = howToRead;
  }

  public boolean isComparable(int howToRead)
  {
    return true;
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clStringColumnElement(language, howToRead);
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[1];
    c[0] = String.class;
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
      return new StringColumnElement(in.readUTF());
    }
    catch(Exception e)
    {
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new StringColumnElement(parameters[0].toString());
  }

  public int getAmountColumnIndex(int howToRead)
  {
    return -1;
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
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

  public boolean isSaved(int column, int howToRead)
  {
    return true;
  }

  public boolean isComplicated(int column, int howToRead)
  {
    return false;
  }

  public String canBeMovedTo(int howToRead)
  {
    return null;
  }

  public int[] canBeMovedPath(int howToRead)
  {
    return null;
  }

  public int getMaxHowToRead()
  {
    return 1;
  }

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable) throws Exception
  {
    return "";
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    return null;
  }

  public boolean isSplittable(int columnIndex, int howToRead)
  {
    return false;
  }

  public void printTable(PrintWriter out, boolean view, int criteria, boolean ascending, User user)
  {
  }

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn,
                                 Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date)
  {
    return "";
  }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtStringColumnElement(language, howToRead);
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

package element;

import table.TableElementExample;
import table.TableElement;
import table.Table;
import utils.MyObjectInputStream;
import java.io.IOException;
import table.User;
import data.*;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;
import utils.DateRepresentation;

public class Customer2Example implements TableElementExample
{
  public Customer2Example()
  {
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clCustomer(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[12];
    i[0] = 1;
    i[1] = 2;
    i[2] = 3;
    i[3] = 4;
    i[4] = 5;
    i[5] = 6;
    i[6] = 7;
    i[7] = 8;
    i[8] = 9;
    i[9] = 10;
    i[10] = 11;
    i[11] = 12;
    return i;
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[14];
    c[0] = Long.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    c[5] = String.class;
    c[6] = String.class;
    c[7] = String.class;
    c[8] = String.class;
    c[9] = String.class;
    c[10] = String.class;
    c[11] = String.class;
    c[12] = String.class;
    c[13] = Boolean.class;
    return c;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      DateRepresentation date = null;
      if (parameters[3].toString().length() < 10)
        date = new DateRepresentation(false);
      else
        date = new DateRepresentation(parameters[3].toString());
      int birthday = date.getDay();
      int birthmonth = date.getMonth();
      int birthyear = date.getYear();
      if (parameters[1].toString().length() == 0 && parameters[2].toString().length() == 0)
        return null;
      Customer2 c = new Customer2(Long.MAX_VALUE, (String) parameters[0], (String) parameters[1], (String) parameters[2], birthday, birthmonth, birthyear,
                                  (String) parameters[4], (String) parameters[5], (String) parameters[6], (String) parameters[7], (String) parameters[8],
                                  (String) parameters[9], (String) parameters[10], (String) parameters[11]);
      return c;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      Customer2 c2 = new Customer2(in.readLong(), in.readUTF(), in.readUTF(), in.readUTF(), in.readInt(), in.readInt(), in.readInt(), in.readUTF(), in.readUTF(),
                                   in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readBoolean() /*false*/);
      return c2;
    }
    catch (Exception e)
    {
      e.printStackTrace();
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

  public boolean isAutoIncrementable(int howToRead)
  {
    return true;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    switch (index)
    {
      default:
        return false;
    }
  }

  public Table getSubtable(int howToRead, int index)
  {
    switch (index)
    {
      default:
        return null;
    }
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

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws
      Exception
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
    return Consts.gtCustomer(language);
  }

  public boolean isSaved(int column, int howToRead)
  {
    return true;
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code, int operation, String language)
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

  public String getSetConfirmation(long code, TableElement parentElement, String language, int howToRead, User user)
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

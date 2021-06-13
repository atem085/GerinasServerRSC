package element;

import table.TableElementExample;
import table.TableElement;
import utils.MyObjectInputStream;
import table.Table;
import java.io.PrintWriter;
import table.User;
import utils.MyObjectOutputStream;
import utils.Consts;
import utils.DateRepresentation;

public class RoomPriceExample implements TableElementExample
{
  public RoomPriceExample()
  {
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clRoomPrice(language);
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
      return new RoomPrice(in.readInt(), in.readInt(), in.readInt(), in.readDouble(), in.readInt());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new RoomPrice(parameters[0].toString(), ((Double)parameters[1]).doubleValue(), ((Integer)parameters[2]).intValue());
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

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[4];
    c[0] = String.class;
    c[1] = Double.class;
    c[2] = String.class;
    c[3] = Integer.class;
    return c;
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

  public void printTable(PrintWriter out, boolean view, int criteria, boolean ascending, User user)
  {
  }

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws Exception
  {
    if(howToRead == 1 && operationIndex == 0)
    {
      int year = ( (Integer) parameters[0]).intValue();
      DateRepresentation currentDate = new DateRepresentation(1, 1, year);
      while(currentDate.getYear() == year)
      {
        RoomPrice rp = new RoomPrice(currentDate.getDay(), currentDate.getMonth(), currentDate.getYear(), 0, 1);
        currentTable.addTableElement(rp, "de", currentUser, date);
        currentDate.add(DateRepresentation.DATE, 1);
      }
    }
    return "";
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    if(howToRead == 1 && operationIndex == 0)
      return "Add";
    return null;
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public int[] getFractionDigits(int howToRead)
  {
    int[] i = new int[4];
    i[0] = 0;
    i[1] = 2;
    i[2] = 0;
    i[3] = 1;
    return i;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtRoomPrice(language);
  }

  public boolean isSaved(int column, int howToRead)
  {
    return false;
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
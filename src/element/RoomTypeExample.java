package element;

import table.TableElementExample;
import table.TableElement;
import utils.MyObjectInputStream;
import table.Table;
import java.io.File;
import utils.Consts;
import java.io.PrintWriter;
import table.User;
import utils.MyObjectOutputStream;
import utils.DateRepresentation;
import java.util.Vector;
import data.Data;

public class RoomTypeExample implements TableElementExample
{
  protected Table roomPrices;
  public RoomTypeExample()
  {
    this.roomPrices = new Table("prices of room type_", new RoomPriceExample(), 1, 1, null, null);
  }

  public boolean isComparable(int howToRead)
  {
    return true;
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clRoomType(language, howToRead);
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[2];
    c[0] = String.class;
    c[1] = Integer.class;
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
      RoomType r = new RoomType(in.readUTF());
      r.roomPrices.load(in, false);
      r.order = in.readInt();
      return r;
    }
    catch(Exception e)
    {
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new RoomType(parameters[0].toString());
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
    if(howToRead == 1 && index == 0) return true;
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    if(howToRead == 1 && index == 0) return this.roomPrices;
    return null;
  }

  public boolean isSaved(int column, int howToRead)
  {
    return false;
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
    if(operationIndex == 0)
      return Consts.opSetMinDays(language);
    if(operationIndex == 1)
      return Consts.opSetPrices(language);
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
    if (operationIndex == 0)
    {
      DateRepresentation dateFrom = new DateRepresentation(parameters[0].toString());
      DateRepresentation dateTill = new DateRepresentation(parameters[1].toString());
      int days = ((Integer)parameters[2]).intValue();
      Table tbRoomPrices = currentTable.findElement(selectedCode).getSubtable(0);
      for (int j = tbRoomPrices.size() - 1; j > -1; j--)
      {
        DateRepresentation currentDate = new DateRepresentation( ( (RoomPrice) tbRoomPrices.at(j)).day,
            ( (RoomPrice) tbRoomPrices.at(j)).month,
            ( (RoomPrice) tbRoomPrices.at(j)).year);
        if(currentDate.before(dateFrom))
          break;
        if ( ( (RoomPrice) tbRoomPrices.at(tbRoomPrices.size() - 1)).year < dateTill.getYear())
          return Consts.ermsAddLimit("en") + " " + Data.db.getTable(Consts.tbRoomTypes).findElement(selectedCode).getValue(0, 1);
        if (currentDate.equals(dateFrom) || currentDate.after(dateFrom) && currentDate.before(dateTill))
          ( (RoomPrice) tbRoomPrices.at(j)).minDays = days;
      }
    }

    if (operationIndex == 1)
    {
      DateRepresentation dateFrom = new DateRepresentation(parameters[0].toString());
      DateRepresentation dateTill = new DateRepresentation(parameters[1].toString());
      double price = ((Double)parameters[2]).doubleValue();
      Table tbRoomPrices = currentTable.findElement(selectedCode).getSubtable(0);
      for (int j = tbRoomPrices.size() - 1; j > -1; j--)
      {
        DateRepresentation currentDate = new DateRepresentation( ( (RoomPrice) tbRoomPrices.at(j)).day,
            ( (RoomPrice) tbRoomPrices.at(j)).month,
            ( (RoomPrice) tbRoomPrices.at(j)).year);
        if(currentDate.before(dateFrom))
          break;
        if ( ( (RoomPrice) tbRoomPrices.at(tbRoomPrices.size() - 1)).year < dateTill.getYear())
          return Consts.ermsAddLimit("en") + " " + Data.db.getTable(Consts.tbRoomTypes).findElement(selectedCode).getValue(0, 1);
        if (currentDate.equals(dateFrom) || currentDate.after(dateFrom) && currentDate.before(dateTill))
          ( (RoomPrice) tbRoomPrices.at(j)).price = price;
      }
    }
    return "";
  }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtRoomType(language);
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code,
                               int operation, String language)
  {
  }

  public boolean isAddEnabled(int howToRead)
  {
    return true;
  }

  public boolean isRemoveEnabled(int howToRead)
  {
    return true;
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

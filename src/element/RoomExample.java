package element;

import table.TableElementExample;
import table.TableElement;
import utils.MyObjectInputStream;
import table.User;
import java.io.IOException;
import table.Table;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;
import utils.DateRepresentation;
import data.Data;
import utils.Consts;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert ein Kontaktperson.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RoomExample implements TableElementExample
{

  public RoomExample()
  {
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clRoom(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[5];
    i[0] = 0;
    i[1] = 1;
    i[2] = 2;
    i[3] = 3;
    i[4] = 4;
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
    Class[] c = new Class[5];
    c[0] = Long.class;
    c[1] = Integer.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    return c;
  }

  public static boolean isBusy(TableElement room, DateRepresentation startDate, DateRepresentation endDate)
  {
    Table stay = Data.db.getTable(Consts.tbStay);
    for(int i = 0; i < stay.size(); i++)
    {
      Stay cstay = (Stay)stay.at(i);
      if(cstay.room != room) continue;
      DateRepresentation checkinDate = new DateRepresentation(cstay.getValue(0, 1).toString());
      if(startDate.compareTo(checkinDate) == 0) return true;
      DateRepresentation checkoutDate = new DateRepresentation(cstay.getValue(1, 1).toString());
      if(startDate.compareTo(checkoutDate) >= 0) continue;
      if(endDate.compareTo(checkinDate) <= 0) continue;
      return true;
    }
    return false;
  }

  public static boolean isBusy(TableElement auf, TableElement room, DateRepresentation startDate, DateRepresentation endDate)
  {
    Table stay = Data.db.getTable(Consts.tbStay);
    for(int i = 0; i < stay.size(); i++)
    {
      Stay cstay = (Stay)stay.at(i);
      if(cstay == auf) continue;
      if(cstay.room != room) continue;
      DateRepresentation checkinDate = new DateRepresentation(cstay.getValue(0, 1).toString());
      if(startDate.compareTo(checkinDate) == 0) return true;
      DateRepresentation checkoutDate = new DateRepresentation(cstay.getValue(1, 1).toString());
      if(startDate.compareTo(checkoutDate) >= 0) continue;
      if(endDate.compareTo(checkinDate) <= 0) continue;
      return true;
    }
    return false;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new Room(((Integer)parameters[0]).longValue(), ((Integer)parameters[1]).intValue(), (String)parameters[2], (String)parameters[3], ((Long)parameters[4]).longValue());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      Room r = new Room(in.readLong(), in.readInt(), in.readUTF(), in.readUTF(), in.readLong());
      return r;
    }
    catch(Exception e)
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
    return Consts.gtRoom(language);
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

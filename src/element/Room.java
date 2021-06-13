package element;

import table.TableElement;
import utils.MyObjectOutputStream;
import java.io.IOException;
import table.Table;
import table.User;
import utils.DateRepresentation;
import utils.Consts;
import data.Data;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert ein Kontaktperson.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Room implements TableElement
{
  private char[] name, description;
  private int capacity;
  protected TableElement roomType;
  protected long no;

  public Room(long no, int capacity, String name, String description, long roomTypeCode)
  {
    this.name = name.toCharArray();
    this.description = description.toCharArray();
    this.capacity = capacity;
    this.no = no;
    this.roomType = Data.db.getTable(Consts.tbRoomTypes).findElement(roomTypeCode);
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0:
        return new Long(this.no);
      case 1:
        return new Integer(this.capacity);
      case 2:
        return new String(this.name);
      case 3:
        return new String(this.description);
      case 4:
        return this.roomType.getValue(0, 1);
      default:
        return new Long(this.no).toString();
    }
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if(ascending)
      return this.getValue(column, howToRead).toString().compareTo(element.getValue(column, howToRead).toString());
    return element.getValue(column, howToRead).toString().compareTo(this.getValue(column, howToRead).toString());
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    if(this.compareTo(element, column, true, howToRead) == 0)
      return true;
    return false;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.no == ((Room)obj).no;
    }
    catch(ClassCastException e)
    {
      return false;
    }
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    switch(column)
    {
      case 1:
        this.capacity = ((Integer)value).intValue();
        break;
      case 2:
        this.name = ((String)value).toCharArray();
        break;
      case 3:
        this.description = ((String)value).toCharArray();
        break;
      case 4:
        this.roomType = Data.db.getTable(Consts.tbRoomTypes).findElement(((Long)value).longValue());
        break;
    }
    return "";
  }

  public TableElement duplicate()
  {
    Room r = new Room(this.no, this.capacity, new String((char[])this.name.clone()), new String((char[])this.description.clone()), this.roomType.getCode().longValue());
     return r;
  }

  public String toString(int howToRead)
  {
    return "" + this.no + "\t" + this.capacity + "\t" + new String(this.name) + "\t" + new String(this.description) + "\t" + this.roomType.getValue(0, 1).toString();
  }

  public Long getCode()
  {
    return new Long(this.no);
  }

  /**
   * <h3>Diese Methode wird zur Bearbeitung des Wertes in der Spalte "column" verwendet.</h3>
   * Folgende Werte d&uuml;rfen <b>nicht</b> bearbeitet werden:<br>
   * <font color=#ff1493><b>Code<br></b></font>
   * @param column die zur Bearbeitung stehende Spalte
   * @param howToRead der Leseart
   * @return false, falls den Wert in der Spalte "column" nicht bearbeitet werden darf,
   * true, sonst.
   */

  protected boolean isBusy(DateRepresentation date)
  {
    Table bookings = Data.db.getTable(Consts.tbStay);
    for(int i = 0; i < bookings.size(); i++)
    {
      Stay booking = (Stay) bookings.at(i);
      if (booking.room != this)
        continue;
      DateRepresentation startDate = new DateRepresentation(booking.checkinDay, booking.checkinMonth,
          booking.checkinYear);
      if(startDate.compareTo(date) > 0) continue;
      DateRepresentation endDate = new DateRepresentation(booking.checkoutDay, booking.checkoutMonth,
          booking.checkoutYear);
      if(endDate.compareTo(date) <= 0) continue;
      return true;
    }
    return false;
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    if(column == 0)
      return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.no);
      out.writeInt(this.capacity);
      out.writeUTF(new String(this.name));
      out.writeUTF(new String(this.description));
      out.writeLong(this.roomType.getCode().longValue());
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    Table t = Data.db.getTable(Consts.tbStay);
    int size = t.size();
    for(int i = 0; i < size; i++)
    {
      Stay st = (Stay)t.at(i);
      if(st.room == this)
        return false;
    }
    return true;
  }

  public boolean isMovable(int howToRead)
  {
    return false;
  }

  public String wasAdd(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    return "";
  }

  public String wasRemoved(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    return "";
  }

  public void setCode(long newCode)
  {

  }

  public void setParentTable(Table parentTable, int howToRead)
  {

  }

  public void setParentElement(TableElement parentElement, int howToRead)
  {
  }

  public Table getParentTable(int howToRead)
  {
    return null;
  }

  public TableElement getParentElement(int howToRead)
  {
    return null;
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return this.getValue(column, howToRead);
  }

  public String getPulldownMenuElement(User user)
  {
    return "";
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return "";
  }

  public String getCellColor(int howToRead, int column)
  {
    return "";
  }

  public Table getReferenceTable(int column, int howToRead)
  {
    return null;
  }

  public long getReferenceCode(int column, int howToRead, User user, String language)
  {
    return 0L;
  }
}

package element;

import table.TableElement;
import table.User;
import utils.MyObjectOutputStream;
import table.Table;
import utils.DateRepresentation;
import utils.Consts;

public class RoomPrice implements TableElement
{
  protected int day, month, year;
  protected double price;
  private long code;
  protected int minDays;

  public RoomPrice(int day, int month, int year, double price, int minDays)
  {
    this.day = day;
    this.month = month;
    this.year = year;
    this.price = price;
    DateRepresentation date = new DateRepresentation(day, month, year);
    String dt = date.toString();
    dt = dt.substring(0, 4) + dt.substring(5, 7) + dt.substring(8, 10);
    this.code = Long.parseLong(dt);
    this.minDays = minDays;
  }

  public RoomPrice(String date, double price, int minDays)
  {
    DateRepresentation d = new DateRepresentation(date);
    this.day = d.getDay();
    this.month = d.getMonth();
    this.year = d.getYear();
    this.price = price;
    this.code = Table.dtJSHashCode(date);
    String dt = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
    this.code = Long.parseLong(dt);
    this.minDays = minDays;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return new DateRepresentation(this.day, this.month, this.year).toString();
      case 1: return new Double(this.price);
      case 2: return Consts.varDayWeek(new DateRepresentation(this.day, this.month - 1, this.year).getDayWeek(), "en");
      case 3: return new Integer(this.minDays);
      default: return new Long(this.code);
    }
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return this.getValue(column, howToRead);
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    return this.getValue(column, howToRead).equals(element.getValue(column, howToRead));
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    switch(column)
    {
      case 1: this.price = ((Double)value).doubleValue();
        break;
      case 3: this.minDays = ((Integer)value).intValue();
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    String s = "";
    for(int i = 0; i < 4; i++)
      s += this.getValue(i, howToRead) + "\t";
    return s;
//    return new DateRepresentation(this.day, this.month, this.year).toString() + "\t" + this.price;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ( (RoomPrice) obj).code;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return false;
      case 1: return true;
      case 2: return false;
      case 3: return true;
    }
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeInt(this.day);
      out.writeInt(this.month);
      out.writeInt(this.year);
      out.writeDouble(this.price);
      out.writeInt(this.minDays);
  }

  public TableElement duplicate()
  {
    return new RoomPrice(this.day, this.month, this.year, this.price, this.minDays);
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    return false;
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
    return -1;
  }

}
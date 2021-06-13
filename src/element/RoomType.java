package element;

import table.TableElement;
import utils.MyObjectOutputStream;
import table.Table;
import data.Data;
import utils.Consts;
import table.User;
import utils.DateRepresentation;

public class RoomType implements TableElement
{
  private char[] type;
  private long code;
  protected int order = 0;
  protected Table roomPrices;

  public RoomType(String type)
  {
    this.type = type.toCharArray();
    this.code = Table.dtJSHashCode(type);
    this.roomPrices = new Table("prices of room type_" + type, new RoomPriceExample(), 1, 1, this, null);
  }

  public Object getValue(int column, int howToRead)
  {
    switch (column)
    {
      case 0: return new String(this.type);
      case 1: return new Integer(this.order);
      default: return new Long(this.code);
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
    return this.getValue(column, howToRead).equals(element.getValue(column, howToRead));
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ( (RoomType) obj).code;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    /*
    this.type = value.toString().toCharArray();
    this.code = Table.dtJSHashCode(new String(this.type));*/
    switch (column)
    {
      case 1:
        int ord = ((Integer) value).intValue();
        if (ord < 1)
        {
          this.order = ((Integer) value).intValue();
          break;
        }
        int curr = this.order;
        Table tm = Data.db.getTable(Consts.tbRoomTypes);
        for (int i = 0; i < tm.size() ; i++)
          if (((RoomType)tm.at(i)).order == ord)
            ((RoomType)tm.at(i)).order = curr;
        this.order = ord;
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    return new String(this.type) + "\t" + this.order;
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    switch (column)
    {
      case 1:
        return true;
    }
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeUTF(new String(this.type));
      this.roomPrices.write(out);
      out.writeInt(this.order);
  }

  public TableElement duplicate()
  {
    return new RoomType(new String((char[])this.type.clone()));
  }

  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.roomPrices;
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    Table rooms = Data.db.getTable(Consts.tbRooms);
    for(int i = 0; i < rooms.size(); i++)
      if(((Room)rooms.at(i)).roomType == this)
        return false;
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

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return Consts.getTitleRoomType(howToRead, subtableIndex, new String(this.type), language);
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return this.getValue(column, howToRead);
  }

  public String getPulldownMenuElement(User user)
  {
    return new String(this.type);
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

  public int getMinDays(DateRepresentation date)
  {
    Table tbPrices = this.getSubtable(0);
    DateRepresentation currentDate = new DateRepresentation(((RoomPrice)tbPrices.at(tbPrices.size() - 1)).day, ((RoomPrice)tbPrices.at(tbPrices.size() - 1)).month, ((RoomPrice)tbPrices.at(tbPrices.size() - 1)).year);
    if(currentDate.before(date))
      return 0;
    for(int i = tbPrices.size() - 1; i > 0; i--)
    {
      currentDate = new DateRepresentation(((RoomPrice)tbPrices.at(i)).day, ((RoomPrice)tbPrices.at(i)).month, ((RoomPrice)tbPrices.at(i)).year);
      if(date.equals(currentDate))
         return ( (RoomPrice) tbPrices.at(i)).minDays;
    }
    return 0;
  }

}

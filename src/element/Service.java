package element;

import table.TableElement;
import table.User;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import table.Table;
import java.io.IOException;

public class Service implements TableElement
{
  private long code;
  protected char[] name;
  protected double price;

  public Service(String name, double price)
  {
    this.name = name.toCharArray();
    this.price = price;
    this.code = Table.dtJSHashCode(name);
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return new String(this.name);
      case 1: return new Double(this.price);
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
      case 0:
        this.name = value.toString().toCharArray();
        this.code = Table.dtJSHashCode(value.toString());
        break;
      case 1:
        this.price = ((Double)value).doubleValue();
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    return new String(this.name) + "\t" + this.price;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((Service)obj).getCode().longValue();
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
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeUTF(new String(this.name));
      out.writeDouble(this.price);
  }

  public TableElement duplicate()
  {
    return new Service(new String((char[])this.name.clone()), this.price);
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    // dodelat
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
    return new String(this.name);
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return null;
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
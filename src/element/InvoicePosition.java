package element;

import table.TableElement;
import table.User;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import table.Table;
import utils.Consts;
import data.Data;
import java.io.IOException;

public class InvoicePosition implements TableElement
{
  protected TableElement service;
  protected double price;
  protected int amount;

  public InvoicePosition(long serviceCode, double price, int amount)
  {
    this.service = Data.db.getTable(Consts.tbService).findElement(serviceCode);
    this.price = price;
    this.amount = amount;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return this.service.getValue(0, 1);
      case 1: return new Double(this.price);
      case 2: return new Integer(this.amount);
      case 3: return new Double(this.price * this.amount);
      default: return this.service.getCode();
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
      case 1:
        this.price = ((Double)value).doubleValue();
        break;
      case 2:
        this.amount = ((Integer)value).intValue();
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    return this.service.getValue(0, 1) + "\t" + this.price + "\t" + this.getValue(2, 1).toString() + "\t" + this.getValue(3, 1);
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.service.getCode().longValue() == ((InvoicePosition)obj).getCode().longValue();
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public Long getCode()
  {
    return this.service.getCode();
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    if(howToRead == 1 && (column == 0 || column == 3)) return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.service.getCode().longValue());
      out.writeDouble(this.price);
      out.writeInt(this.amount);
  }

  public TableElement duplicate()
  {
    return new InvoicePosition(this.service.getCode().longValue(), this.price, this.amount);
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    // dodelat
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

  public String getPulldownMenuElement(User user)
  {
    return "";
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

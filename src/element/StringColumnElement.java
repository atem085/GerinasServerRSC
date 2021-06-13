package element;

import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import table.Table;
import data.Data;
import utils.Consts;
import table.User;
import java.io.IOException;

public class StringColumnElement implements TableElement
{
  private char[] value;
  private long code;

  public StringColumnElement(String value)
  {
    this.value = value.toCharArray();
    this.code = Table.dtJSHashCode(value);
  }

  public Object getValue(int column, int howToRead)
  {
    return new String(this.value);
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
      return this.code == ( (StringColumnElement) obj).code;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    this.value = value.toString().toCharArray();
    this.code = Table.dtJSHashCode(new String(this.value));
    return "";
  }

  public String toString(int howToRead)
  {
    return new String(this.value);
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
      out.writeUTF(new String(this.value));
  }

  public TableElement duplicate()
  {
    return new StringColumnElement(new String((char[])this.value.clone()));
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

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return "";
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return new String(this.value);
  }

  public String getPulldownMenuElement(User user)
  {
    return new String(this.value);
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

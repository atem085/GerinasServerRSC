package element;

import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import table.Table;
import table.User;
import utils.Consts;
import java.io.IOException;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class ColumnFilter implements TableElement
{
  private long code;
  private Object value;

  public ColumnFilter(Object value)
  {
    this.value = value;
    this.code = Table.dtJSHashCode(this.value.toString());
  }

  public Object getValue(int column, int howToRead)
  {
    return this.value;
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if(ascending)
      if(column == -1)
        return this.getCode().compareTo(element.getCode());
      else
      if(column == -1)
        return element.getCode().compareTo(this.getCode());
    return 0;
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    return false;
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    return "";
  }

  public String toString(int howToRead)
  {
    return this.value.toString();
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((TableElement)obj).getCode().longValue();
    }
    catch(ClassCastException e)
    {
      //e.printStackTrace();
      return false;
    }
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeObject(this.value);
  }

  public TableElement duplicate()
  {
    return new ColumnFilter(this.value);
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
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
    return this.value;
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
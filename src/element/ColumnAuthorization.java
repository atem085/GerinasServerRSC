package element;

import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import table.Table;
import data.Data;
import utils.Consts;
import table.User;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class ColumnAuthorization implements TableElement
{
  protected int columnIndex;
  private boolean canSee;
  protected boolean canEdit = false;
  protected int order;
  private TableElement parentElement;
  private Table parentTable;
  public Table filter;

  public ColumnAuthorization(int columnIndex, boolean canSee, boolean canEdit, int order)
  {
    this.canSee = canSee;
    this.columnIndex = columnIndex;
    this.canEdit = canEdit;
    this.order = order;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0:
        Table t = Data.db.getSubtableExample((String)this.parentElement.
                                             getValue(0, 1));
        return t.getExample().getColumnNames(t.getHowToRead(), "en", null)[(int)this.
            columnIndex];
      case 1:
        return new Boolean(this.canSee);
      case 2:
        return new Boolean(this.canEdit);
      case 3:
        return new Integer(this.order);
    }
    return null;
  }

  public int compareTo(TableElement element, int column, boolean ascending,
                       int howToRead)
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
    switch(column)
    {
      case 1:
        this.canSee = ((Boolean)value).booleanValue();
        break;
      case 2:
        this.canEdit = ((Boolean)value).booleanValue();
        break;
      case 3:
        this.order = ((Integer)value).intValue();
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    return "" + this.columnIndex + "\t" + this.canSee + "\t" + this.canEdit + "\t" + this.order;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.columnIndex == ((ColumnAuthorization)obj).columnIndex;
    }
    catch(ClassCastException e)
    {
      return false;
    }
  }

  public Long getCode()
  {
    return new Long(this.columnIndex);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    if(column == 0)
      return false;
    if(new String(((User)((TableAuthorization)this.parentElement).
                   getParentElement(1)).name).equals("admin"))
      return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeInt(this.columnIndex);
      out.writeBoolean(this.canSee);
      out.writeBoolean(this.canEdit);
      out.writeInt(this.order);
      this.filter.write(out);
  }

  public TableElement duplicate()
  {
    return new ColumnAuthorization(this.columnIndex, this.canSee, this.canEdit, this.order);
  }

  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.filter;
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
    Table t = Data.db.getSubtableExample((String)this.parentElement.getValue(0,
        1));
    Class c = t.getExample().getColumnClasses(t.getHowToRead())[(int)
        columnIndex];
    this.filter = new Table("default_filter_of_column_" + this.columnIndex +
                            "_of_table_" +
                            this.parentElement.getValue(0, 1) + "_of_user_" +
                            ((TableAuthorization)this.parentElement).
                            getParentElement(1).getValue(0, 1) +
                            "_" +
                            ((TableAuthorization)this.parentElement).
                            getParentElement(1).getValue(1, 1),
                            new ColumnFilterExample(c), 1, 0, this, this.parentElement.getParentElement(1).getSubtable(0));
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
    if(howToRead == 1)
      this.parentTable = parentTable;
  }

  public void setParentElement(TableElement parentElement, int howToRead)
  {
    if(howToRead == 1)
      this.parentElement = parentElement;
  }

  public Table getParentTable(int howToRead)
  {
    if(howToRead == 1)
      return this.parentTable;
    return null;
  }

  public TableElement getParentElement(int howToRead)
  {
    if(howToRead == 1)
      return this.parentElement;
    return null;
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    switch(column)
    {
      case 0:
        Table t = Data.db.getSubtableExample((String)this.parentElement.
                                             getValue(0, 1));
        return t.getExample().getColumnNames(t.getHowToRead(), language, null)[(int)this.
            columnIndex];
      case 1:
        return new Boolean(this.canSee);
      case 2:
        return new Boolean(this.canEdit);
      case 3:
        return new Integer(this.order);
    }
    return null;
  }

  public String getPulldownMenuElement(User user)
  {
    return "";
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return Consts.getTitleColumnAuthorization(howToRead, subtableIndex, language);
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

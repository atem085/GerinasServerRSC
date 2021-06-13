package table;

import utils.*;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class DynamicUser extends User
{

  public DynamicUser()
  {
  }

  public DynamicUser(String name, String password)
  {
    super(name, password);
  }

  public void write(MyObjectOutputStream out, int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
  }

  public String getPulldownMenuElement(User user)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getPulldownMenuElement() not yet implemented.");
  }
  public String wasRemoved(int parm1, TableElement parm2, String currentUser, DateRepresentation date)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method wasRemoved() not yet implemented.");
  }
  public Table getParentTable(int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getParentTable() not yet implemented.");
  }
  public TableElement getParentElement(int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getParentElement() not yet implemented.");
  }
  public boolean equals(TableElement parm1, int parm2, int parm3)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }
  public String getCellColor(int howToRead, int column)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getCellColor() not yet implemented.");
  }
  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getTitle() not yet implemented.");
  }
  public String toString(int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method toString() not yet implemented.");
  }
  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws java.lang.Exception
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method setValue() not yet implemented.");
  }
  public void setParentElement(TableElement parm1, int parm2)
  {
    /**@todo Implement this table.TableElement abstract method*/
  }
  public TableElement duplicate()
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method duplicate() not yet implemented.");
  }
  public Object getValue(int column, int howToRead, User user, String language)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getValue() not yet implemented.");
  }
  public boolean isCellEditable(int column, int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method isCellEditable() not yet implemented.");
  }
  public boolean isMovable(int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method isMovable() not yet implemented.");
  }
  public Table getSubtable(int subtableIndex)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getSubtable() not yet implemented.");
  }
  public Long getCode()
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getCode() not yet implemented.");
  }
  public String wasAdd(int parm1, TableElement parm2, String currentUser, DateRepresentation date)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method wasAdd() not yet implemented.");
  }
  public boolean isRemovable(int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method isRemovable() not yet implemented.");
  }
  public Object getValue(int column, int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getValue() not yet implemented.");
  }
  public void setParentTable(Table parentTable, int howToRead)
  {
    /**@todo Implement this table.TableElement abstract method*/
  }
  public void setCode(long newCode)
  {
    /**@todo Implement this table.TableElement abstract method*/
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
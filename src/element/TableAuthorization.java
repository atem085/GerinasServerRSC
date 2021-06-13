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

public class TableAuthorization implements TableElement
{
  public long code;
  public char[] tableName;
  private boolean canSee;
  private boolean canEdit;
  private boolean canRemove;
  private boolean canAdd;
  private boolean canSet;
  private boolean canApply;
  private boolean canSplit;
  private boolean canMove;
  private boolean[] op = new boolean[5];
  private Table parentTable;
  private TableElement parentElement;
  public Table columnSettings;

  public TableAuthorization(String tableName, boolean canSee)
  {
    this.tableName = tableName.toCharArray();
    this.canSee = canSee;
    this.code = Table.dtJSHashCode(tableName);
    this.canEdit = true;
    this.canRemove = true;
    this.canAdd = true;
    this.canSet = true;
    this.canApply = true;
    this.canSplit = true;
    this.canMove = true;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0:
        return new String(this.tableName);
      case 1:
        return new Boolean(this.canSee);
      case 2:
        return new Boolean(this.canEdit);
      case 3:
        return new Boolean(this.canRemove);
      case 4:
        return new Boolean(this.canAdd);
      case 5:
        return new Boolean(this.canSet);
      case 6:
        return new Boolean(this.canApply);
      case 7:
        return new Boolean(this.canSplit);
      case 8:
        return new Boolean(this.canMove);
      case 9:
        return new Boolean(this.op[column - 9]);
      case 10:
        return new Boolean(this.op[column - 9]);
      case 11:
        return new Boolean(this.op[column - 9]);
      case 12:
      case 13:
        return new Boolean(this.op[column - 9]);
      case 14: if(!Character.isDigit(this.tableName[0]))
               {
                 String tnn = new String(this.tableName);
                 for(int i = 0; i < Data.reportName.length; i++)
                   if(Data.reportName[i].startsWith(tnn))
                   {
                     int pos = Data.reportName[i].indexOf("-");
                     if(pos < 0) return tnn;
                     return Data.reportName[i].substring(pos + 1, Data.reportName[i].length());
                   }
                 Table t = Data.db.getTable(new String(tableName));
                 return t.getExample().getTitle("en", t.getHowToRead(), null);
               }
               String tn = new String(this.tableName);
               String rtn = tn.substring(tn.lastIndexOf(".") + 1, tn.length());
               Table t = Data.db.getTable(rtn);
               String mainTitle = t.getExample().getTitle("en", t.getHowToRead(), null);
               String stn = tn.substring(0, tn.lastIndexOf("."));
               for(;stn.indexOf(".") > 0; stn = stn.substring(0, stn.lastIndexOf(".")))
               {
                 String cstn = stn.substring(stn.lastIndexOf(".") + 1, stn.length());
                 t = t.getExample().getSubtable(t.getHowToRead(), Integer.parseInt(cstn));
               }
               t = t.getExample().getSubtable(t.getHowToRead(), Integer.parseInt(stn));
               return t.getExample().getTitle("en", t.getHowToRead(), null) + " " + mainTitle;
      default:
        return new Long(this.code);
    }
  }

  public boolean isAuthorized()
  {
    return this.canSee;
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if(ascending)
    {
      if(column == -1)
        return this.getCode().compareTo(element.getCode());
      else if(column == 0)
      {
        String s1 = new String(this.tableName);
        String s2 = (String)element.getValue(column, howToRead);
        String s3 = s1;
        String s4 = s2;
        s1 = s1.substring(s1.lastIndexOf(".") + 1);
        s2 = s2.substring(s2.lastIndexOf(".") + 1);
        s3 = s3.substring(0, s3.lastIndexOf(".") + 1);
        s4 = s4.substring(0, s4.lastIndexOf(".") + 1);
        int res = s1.compareTo(s2);
        boolean eq = res == 0;
        if(eq)
        {
          int r = s3.compareTo(s4);
          if(r == 0)
            return 0;
          if(r > 0)
            return 1;
          return -1;
        }
        return res + 1;
      }
      else
        return this.getValue(column, howToRead).toString().compareTo(element.getValue(column, howToRead).toString());
    }
    else
    {
      if(column == -1)
        return element.getCode().compareTo(this.getCode());
      else if(column == 0)
      {
        String s1 = new String(this.tableName);
        String s2 = (String)element.getValue(column, howToRead);
        String s3 = s1;
        String s4 = s2;
        s1 = s1.substring(s1.lastIndexOf(".") + 1);
        s2 = s2.substring(s2.lastIndexOf(".") + 1);
        s3 = s3.substring(0, s3.lastIndexOf(".") + 1);
        s4 = s4.substring(0, s4.lastIndexOf(".") + 1);
        int res = s2.compareTo(s1);
        boolean eq = res == 0;
        if(eq)
        {
          int r = s4.compareTo(s3);
          if(r == 0)
            return 0;
          if(r > 0)
            return 1;
          return -1;
        }
        return res + 1;
      }
      else
        return element.getValue(column, howToRead).toString().compareTo(this.getValue(column, howToRead).toString());
    }
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
        this.canRemove = ((Boolean)value).booleanValue();
        break;
      case 4:
        this.canAdd = ((Boolean)value).booleanValue();
        break;
      case 5:
        this.canSet = ((Boolean)value).booleanValue();
        break;
      case 6:
        this.canApply = ((Boolean)value).booleanValue();
        break;
      case 7:
        this.canSplit = ((Boolean)value).booleanValue();
        break;
      case 8:
        this.canMove = ((Boolean)value).booleanValue();
        break;
      case 9:
        this.op[column - 9] = ((Boolean)value).booleanValue();
        break;
      case 10:
        this.op[column - 9] = ((Boolean)value).booleanValue();
        break;
      case 11:
        this.op[column - 9] = ((Boolean)value).booleanValue();
        break;
      case 12:
      case 13:
        this.op[column - 9] = ((Boolean)value).booleanValue();
        break;
      default:
        this.code = ((Long)value).longValue();
    }
    return "";
  }

  public String toString(int howToRead)
  {
    return new String(this.tableName) + "\t" + this.canSee + "\t" + this.canEdit + "\t" + this.canRemove + "\t" + this.canAdd + "\t" +
        this.canSet + "\t" + this.canApply + "\t" + this.canSplit + "\t" + this.canMove + "\t" + this.op[0] + "\t"+ this.op[1] + "\t"+ this.op[2] + "\t"+ this.op[3] + "\t" + this.op[4] + "\t" + this.getValue(14, 1);
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((TableAuthorization)obj).code;
    }
    catch(ClassCastException e)
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
    if(column < 1)
      return false;
    if(column > 13) return false;
    if(new String(((User)this.parentElement).name).equals("admin"))
      return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeUTF(new String(this.tableName));
      out.writeBoolean(this.canSee);
      out.writeBoolean(this.canEdit);
      out.writeBoolean(this.canRemove);
      out.writeBoolean(this.canAdd);
      out.writeBoolean(this.canSet);
      out.writeBoolean(this.canApply);
      out.writeBoolean(this.canSplit);
      out.writeBoolean(this.canMove);
      for(int i = 0; i < 5; i++)
        out.writeBoolean(this.op[i]);
      this.columnSettings.write(out);
  }

  public TableElement duplicate()
  {
    return new TableAuthorization(new String((char[])this.tableName.clone()), this.canSee);
  }

  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.columnSettings;
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
    this.columnSettings = new Table("column_settings_of_table_" + new String(this.tableName) + "_of_user_" +
                                    new String(((User)parentElement).name) + "_" + new String(((User)parentElement).password),
                                    new ColumnAuthorizationExample(0, true), 1, -1, this, parentElement.getSubtable(0));
    Table t = Data.db.getSubtableExample(new String(this.tableName));
    String[] columnNames = t.getExample().getColumnNames(t.getHowToRead(), "en", null);
    for(int i = 0; i < columnNames.length; i++)
      this.columnSettings.addTableElement(new ColumnAuthorization(i, true, true, i), "en", currentUser, date);
    if(t == null)
      return Consts.ermsNoSuchTable("en");
    TableAuthorization ta;
    String ms = "";
    for(int i = 0; t.getExample().hasSubtable(t.getHowToRead(), i); i++)
    {
      ta = new TableAuthorization("" + i + "." + new String((char[])this.tableName.clone()), this.canSee);
      ms += this.parentTable.addTableElement(ta, "en", currentUser, date);
    }
    return ms;
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
    if(column == 14)
    {
      if(!Character.isDigit(this.tableName[0]))
               {
                 String tnn = new String(this.tableName);
                 for(int i = 0; i < Data.reportName.length; i++)
                   if(Data.reportName[i].startsWith(tnn))
                   {
                     int pos = Data.reportName[i].indexOf("-");
                     if(pos < 0) return tnn;
                     return Data.reportName[i].substring(pos + 1, Data.reportName[i].length());
                   }
                 Table t = Data.db.getTable(new String(tableName));
                 return t.getExample().getTitle(language, t.getHowToRead(), null);
               }
               String tn = new String(this.tableName);
               String rtn = tn.substring(tn.lastIndexOf(".") + 1, tn.length());
               Table t = Data.db.getTable(rtn);
               String mainTitle = t.getExample().getTitle(language, t.getHowToRead(), null);
               String stn = tn.substring(0, tn.lastIndexOf("."));
               for(;stn.indexOf(".") > 0; stn = stn.substring(0, stn.lastIndexOf(".")))
               {
                 String cstn = stn.substring(stn.lastIndexOf(".") + 1, stn.length());
                 t = t.getExample().getSubtable(t.getHowToRead(), Integer.parseInt(cstn));
               }
               t = t.getExample().getSubtable(t.getHowToRead(), Integer.parseInt(stn));
               return t.getExample().getTitle(language, t.getHowToRead(), null) + " " + mainTitle;
    }
    return this.getValue(column, howToRead);
  }

  public String getPulldownMenuElement(User user)
  {
    return "";
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return Consts.getTitleTableAuthorization(howToRead, subtableIndex, language);
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
package element;

import table.TableElement;
import utils.MyObjectOutputStream;
import java.io.IOException;
import table.Table;
import data.*;
import utils.Consts;
import table.User;
import utils.DateRepresentation;
import utils.Round;
import utils.MyException;

public class Customer2 implements TableElement
{
  private long code;
  protected char[] name, surname, firm;
  protected int birthday, birthmonth, birthyear;
  private char[] tel;
  private char[] fax;
  private char[] email;
  private char[] str;
  private char[] plz;
  private char[] ort;
  private char[] land;
  protected char[] note;
  protected boolean travelAgency;

  // constractor for newInstance()
  public Customer2(long code, String name, String surname, String firm, int birthday, int birthmonth, int birthyear, String str, String plz, String ort, String land,
                   String tel, String fax, String email, String note)
  {
    this.name = name.toCharArray();
    this.surname = surname.toCharArray();
    this.firm = firm.toCharArray();
    this.birthday = birthday;
    this.birthmonth = birthmonth;
    this.birthyear = birthyear;
    this.tel = tel.toCharArray();
    this.fax = fax.toCharArray();
    this.email = email.toCharArray();
    this.str = str.toCharArray();
    this.plz = plz.toCharArray();
    this.ort = ort.toCharArray();
    this.land = land.toCharArray();
    this.note = note.toCharArray();
    this.code = code;
  }

  // constractor for duplicate && read()
  public Customer2(long code, String name, String surname, String firm, int birthday, int birthmonth, int birthyear, String str, String plz, String ort, String land,
                   String tel, String fax, String email, String note, boolean travelAgency)
  {
    this.name = name.toCharArray();
    this.surname = surname.toCharArray();
    this.firm = firm.toCharArray();
    this.birthday = birthday;
    this.birthmonth = birthmonth;
    this.birthyear = birthyear;
    this.tel = tel.toCharArray();
    this.fax = fax.toCharArray();
    this.email = email.toCharArray();
    this.str = str.toCharArray();
    this.plz = plz.toCharArray();
    this.ort = ort.toCharArray();
    this.land = land.toCharArray();
    this.note = note.toCharArray();
    this.code = code;
    this.travelAgency = travelAgency;
  }

  public Object getValue(int column, int howToRead)
  {
    switch (column)
    {
      case 0:
        return new Long(this.code);
      case 1:
        return new String(this.name);
      case 2:
        return new String(this.surname);
      case 3:
        return new String(this.firm);
      case 4:
        return new DateRepresentation(this.birthday, this.birthmonth, this.birthyear).toString();
      case 5:
        return new String(this.str);
      case 6:
        return new String(this.plz);
      case 7:
        return new String(this.ort);
      case 8:
        return new String(this.land);
      case 9:
        return new String(this.tel);
      case 10:
        return new String(this.fax);
      case 11:
        return new String(this.email);
      case 12:
        return new String(this.note);
      case 13:
        return new Boolean(this.travelAgency);
      default:
        return (new Long(this.code)).toString();
    }
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if (ascending)
      return this.getValue(column, howToRead).toString().compareTo(element.getValue(column, howToRead).toString());
    else
      return element.getValue(column, howToRead).toString().compareTo(this.getValue(column, howToRead).toString());
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    if (this.compareTo(element, column, true, howToRead) == 0)
      return true;
    return false;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ( (Customer2) obj).code;
    }
    catch (ClassCastException e)
    {
      return false;
    }
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    switch (column)
    {
      case 1:
        this.name = ((String)value).toCharArray();
        break;
      case 2:
        this.surname = ((String)value).toCharArray();
        break;
      case 3:
        this.firm = ((String)value).toCharArray();
        break;
      case 4:
        DateRepresentation dt = new DateRepresentation(value.toString());
        this.birthday = dt.getDay();
        this.birthmonth = dt.getMonth();
        this.birthyear = dt.getYear();
        break;
      case 5:
        this.str = ((String)value).toCharArray();
        break;
      case 6:
        this.plz = ((String)value).toCharArray();
        break;
      case 7:
        this.ort = ((String)value).toCharArray();
        break;
      case 8:
        this.land = ((String)value).toCharArray();
        break;
      case 9:
        this.tel = ((String)value).toCharArray();
        break;
      case 10:
        this.fax = ((String)value).toCharArray();
        break;
      case 11:
        this.email = ((String)value).toCharArray();
        break;
      case 12:
        this.note = ((String)value).toCharArray();
        break;
      case 13:
        this.travelAgency = ((Boolean)value).booleanValue();
        break;
    }
    return "";
  }

  public TableElement duplicate()
  {
    Customer2 c = new Customer2(this.code, new String( (char[])this.name.clone()), new String( (char[])this.surname.clone()), new String( (char[])this.firm.clone()),
                                this.birthday, this.birthmonth, this.birthyear, new String( (char[])this.str.clone()), new String( (char[])this.plz.clone()),
                                new String( (char[])this.ort.clone()), new String( (char[])this.land.clone()), new String( (char[])this.tel.clone()),
                                new String( (char[])this.fax.clone()), new String( (char[])this.email.clone()), new String( (char[])this.note.clone()), this.travelAgency);
    return c;
  }

  public String toString(int howToRead)
  {
    String s = "";
    for (int i = 0; i < 14; i++)
      s += this.getValue(i, 1) + "\t";
    return s;
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    switch (column)
    {
      case 0:
        return false;
      case 1:
      case 2:
        if (this.firm.length > 0 && this.name.length == 0 && this.surname.length == 0)
          return false;
        return true;
      case 3:
        if (this.firm.length == 0 && this.name.length > 0 && this.surname.length > 0)
          return false;
        return true;
    }
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.code);
      out.writeUTF(new String(this.name));
      out.writeUTF(new String(this.surname));
      out.writeUTF(new String(this.firm));
      out.writeInt(this.birthday);
      out.writeInt(this.birthmonth);
      out.writeInt(this.birthyear);
      out.writeUTF(new String(this.str));
      out.writeUTF(new String(this.plz));
      out.writeUTF(new String(this.ort));
      out.writeUTF(new String(this.land));
      out.writeUTF(new String(this.tel));
      out.writeUTF(new String(this.fax));
      out.writeUTF(new String(this.email));
      out.writeUTF(new String(this.note));
      out.writeBoolean(this.travelAgency);
  }

  public Table getSubtable(int subtableIndex)
  {
    switch (subtableIndex)
    {
      default:
        return null;
    }
  }

  protected boolean isFirm()
  {
    if (this.firm.length > 0)
      return true;
    return false;
  }

  public boolean isRemovable(int howToRead)
  {
    Table t = Data.db.getTable(Consts.tbWebGuestUsers);
    int size = t.size();
    for (int i = 0; i < size; i++)
    {
      DynamicGuestUser te = (DynamicGuestUser) t.at(i);
      long custKey = te.customer.getCode().longValue();
      if (this.code == custKey)
        return false;
    }
    t = Data.db.getTable(Consts.tbStay);
    size = t.size();
    for (int i = 0; i < size; i++)
    {
      Stay st = (Stay) t.at(i);
      if (st.guest == this || st.payer == this || st.guest2 == this || st.payer2 == this || st.guest3 == this || st.payer3 == this)
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
    this.code = newCode;
    Table t = Data.db.getTable(Consts.tbCustomers);
    int[] foundName = new int[0];
    int[] foundSurname = new int[0];
    boolean f = false;
    if (this.firm.length == 0)
    {
      foundName = t.findTableElementsIgnoreCase(new String(this.name), 1);
      foundSurname = t.findTableElementsIgnoreCase(new String(this.surname), 2);
    }
    int[] foundFirm = new int[0];
    if (this.firm.length > 0)
    {
      foundFirm = t.findTableElementsIgnoreCase(new String(this.firm), 3);
      f = true;
    }
    if (f)
    {
      if (foundFirm.length > 0)
        this.code = t.at(t.size() - 1).getCode().longValue();
      return;
    }
    for (int i = 0; i < foundSurname.length; i++)
    {
      int pos = foundSurname[i];
      for (int j = 0; j < foundName.length; j++)
        if (foundName[j] == pos)
        {
          this.code = t.at(t.size() - 1).getCode().longValue();
          return;
        }
    }
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
    if (this.surname.length > 0)
      return new String(this.surname) + " " + new String(this.name) + "--" + this.code;
    return new String(this.firm) + "--" + this.code;
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

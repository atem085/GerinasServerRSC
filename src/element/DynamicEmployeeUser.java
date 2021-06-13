package element;

import table.*;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import java.io.IOException;
import utils.Consts;
import data.Data;

/**
 *
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert den Benutzer.<b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DynamicEmployeeUser extends User
{
  /**
   * Code.<br><br>Im leeren Konstruktor wird Code auf 0 gesetzt, <br>beim anderen Fall wird die Code nach
   * den folgenden Parametern berechnet:<br><br><b>- Name des Benutzers<br>- Passwort des Benutzers</b><br>
   */
  private long code;
  private char[] realName;

  public Table settings;
  private int howToRead;

  public DynamicEmployeeUser()
  {
    super();
    this.code = 0;
    this.settings = new Table("settings_of_user_" + new String(this.name) + "_" + new String(this.password),
                              new TableAuthorizationExample("", true), 1, 0, this, null);
  }

  public DynamicEmployeeUser(String name, String password, String realName, int howToRead)
  {
    super(name, password);
    this.code = Table.dtJSHashCode(new String(name));
    this.realName = realName.toCharArray();
    Table parentTable = null;
    if(howToRead == 1)
      parentTable = Data.db.getTable(Consts.tbDynamicUsers);
    else
      parentTable = Data.db.getTable(Consts.tbWebUsers);
    this.howToRead = howToRead;
    this.settings = new Table("settings_of_dynamic_user_" + new String(this.name) + "_" + new String(this.password),
                              new TableAuthorizationExample("", true), 1, 0, this, parentTable);
  }

  /**
   * Diese Methode wird zur Darstellung des Wertes in der Spalte "column" benutzt.<br>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Name<br>
   * 2. Kennwort<br>
   * 3. Status(Eingelogt(ja/nein))</b></font><br>
   * @param column die Spalte.
   * @param howToRead der Leseart.
   * @return der Wert in der Spalte "column".
   */
  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case -1:
        return new Long(this.code);
      case 0:
        return new String(name);
      case 1:
        return new String(this.password);
      case 2:
        return new Boolean(this.isLoggedIn);
      case 3:
        return new String(this.realName);
      default:
        return(new Long(this.code)).toString();
    }
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if(ascending)
      return this.getValue(column, howToRead).toString().compareTo(element.getValue(column, howToRead).toString());
    else
      return element.getValue(column, howToRead).toString().compareTo(this.getValue(column, howToRead).toString());
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    if(this.compareTo(element, column, true, howToRead) == 0)
      return true;
    return false;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((DynamicEmployeeUser)obj).code;
    }
    catch(ClassCastException e)
    {
      return false;
    }
  }

  /**
   * Diese Methode wird zum Einstellen des neuen Werten benutzt.<br>
   * Es d&uuml;rfen folgende Werte eingesetzt werden:<br>
   * <font color=#9400d3><b>der Name des Benuters<br>
   * das Kennwort<br>
   * das Status</b></font><br>
   * @param column die Spalte, in der der neue Wert eingestellt wird.
   * @param value der neue Wert
   * @param language die Sprache
   * @param howToRead der Leseart
   * @return die Fehlermeldung
   */
  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    switch(column)
    {
      case 0:
        this.name = ((String)value).toCharArray();
        this.recalculateKey();
        break;
      case 1:
        this.password = ((String)value).toCharArray();
        break;
      case 2:
        this.isLoggedIn = ((Boolean)value).booleanValue();
        break;
      case 3:
        this.realName = ((String)value).toCharArray();
        break;
    }
    return "";
  }

  private void recalculateKey()
  {
    this.code = Table.dtJSHashCode(new String(name));
  }

  public TableElement duplicate()
  {
    return new DynamicEmployeeUser(new String((char[])this.name.clone()), new String((char[])this.password.clone()),
                                   new String((char[])this.realName.clone()), this.howToRead);
  }

  public String toString(int howToRead)
  {
    String s = new String(this.name) + "\t" + new String(this.password) + "\t" +
        this.isLoggedIn + "\t" + this.realName;
    return s;
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  /**
   * Diese Methode erm&ouml;glicht die Bearbeitung eines Wertes in der Spalte "column".<br><br>
   * Folgende Werte d&uuml;rfen <b>nicht</b> bearbeitet werden:<br>
   * <font color=#ff1493><b>Code<br></b></font>
   * @param column die zur Bearbeitung stehende Spalte
   * @param howToRead der Leseart
   * @return false, falls die Zelle nicht bearbeitet werden darf,
   * true, sonst.
   */
  public boolean isCellEditable(int column, int howToRead)
  {
    if(column < 0)
      return false;
    if((column == 0 || column == 2) && new String(this.name).equals("admin"))
      return false;
    if(column == 2 && !this.isLoggedIn)
      return false;
    return true;
  }

  public void setCode(long newCode)
  {

  }

  /**
   * Diese Methode wird beim Schreiben eines Elementes benutzt.<br>
   * Es werden das kodierte Kennwort und Kommanden geschrieben.
   * @param out MyObjectOutputStream
   * @param howToRead der Leseart
   */
  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeUTF(new String(this.name));
      out.writeUTF(new String(this.password));
      out.writeUTF(new String(this.realName));
      out.writeBoolean(this.isLoggedIn);
      this.settings.write(out);
  }

  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.settings;
    return null;
  }

  /**
   * Diese Methode beschreibt, ob das Element l&ouml;schbar ist.<br><br>
   * <font color=#ff8c00><b>User "admin" darf nicht geloescht werden.</b></font>
   * @param howToRead der Leseart
   * @return false, wenn das Element nicht geloescht werden darf,
   * true, sonst.
   */
  public boolean isRemovable(int howToRead)
  {
    if(this.name.equals("admin"))
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
    switch(column)
    {
      case -1:
        return new Long(this.code);
      case 0:
        return new String(name);
      case 1:
        return new String(this.password);
      case 2:
        return new Boolean(this.isLoggedIn);
      case 3:
        return new String(this.realName);
      default:
        return(new Long(this.code)).toString();
    }
  }

  public String getPulldownMenuElement(User user)
  {
    return "";
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return Consts.getTitleEmployeeUser(howToRead, subtableIndex, language);
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

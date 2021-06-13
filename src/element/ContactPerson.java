package element;

import table.TableElement;
import utils.MyObjectOutputStream;
import java.io.IOException;
import table.Table;
import table.User;
import utils.DateRepresentation;
import utils.Consts;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert ein Kontaktperson.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ContactPerson implements TableElement
{
  /**
   * Name des Kontaktpersons.
   */
  private char[] name;
  /**
   * Vorstand des Konstaktpersons.
   */
  private char[] vorstand;
  /**
   * Telefonnummer des Kontaktpersons.
   */
  private char[] tel;
  /**
   * Faxnummer des Kontaktpersons.
   */
  private char[] fax;
  /**
   * Email des Konstaktpersons.
   */
  private char[] email;
  /**
   * Code.
   * <p>Code wird nach den folgenden Parametern berechnet:</p>
   * <b> - Name des Kontaktpersons.</b><br>
   */
  private long code;
  private TableElement parentElement;

  /**
   * Konstruktor.
   * <p>Code wird nach den folgenden Parametern berechnet:</p>
   * <b> - Name des Kontaktpersons.</b><br>
   * @param name der Name des Konstaktpersons.
   * @param vorstand der Vorstand des Konstaktpersons.
   * @param tel die Telefonnummer des Kontaktpersons.
   * @param fax die Faxnummer des Kontaktpersons.
   * @param email Email des Konstaktpersons.
   */
  public ContactPerson(String name, String vorstand, String tel, String fax, String email, Table parentTable)
  {
    this.name = name.toCharArray();
    this.vorstand = vorstand.toCharArray();
    this.tel = tel.toCharArray();
    this.fax = fax.toCharArray();
    this.email = email.toCharArray();
    this.code = Table.dtJSHashCode(new String(this.name));
  }

  /**
   * <h3>Diese Methode wird zur Darstellung des Wertes in der Spalte "column" benutzt.</h3>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Name<br>
   * 2. Vorstand<br>
   * 3. Telefonnr<br>
   * 4. Fax<br>
   * 5. Email</b></font><br>
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
        return new String(this.name);
      case 1:
        return new String(this.vorstand);
      case 2:
        return new String(this.tel);
      case 3:
        return new String(this.fax);
      case 4:
        return new String(this.email);
      default:
        return new Long(this.code).toString();
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
    if(this.compareTo(element, column, true, howToRead) == 0)
      return true;
    return false;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((ContactPerson)obj).code;
    }
    catch(ClassCastException e)
    {
      return false;
    }
  }

  /**
   * <h3>Diese Methode wird zum Einstellen des neuen Werten benutzt.</h3>
   * Es d&uuml;rfen folgende Werte eingesetzt werden:<br>
   * <font color=#9400d3><b>der Name des Kontaktpersons.<br>
   * der Vorstand<br>
   * die Telefonnummer<br>
   * die Faxnummer<br>
   * Email</b></font><br>
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
        this.vorstand = ((String)value).toCharArray();
        break;
      case 2:
        this.tel = ((String)value).toCharArray();
        break;
      case 3:
        this.fax = ((String)value).toCharArray();
        break;
      case 4:
        this.email = ((String)value).toCharArray();
        break;
      default:
        this.code = ((Long)value).longValue();
    }
    return "";
  }

  public TableElement duplicate()
  {
    ContactPerson c = new ContactPerson(new String((char[])this.name.clone()), new String((char[])this.vorstand.clone()),
                             new String((char[])this.tel.clone()), new String((char[])this.fax.clone()),
                             new String((char[])this.email.clone()), null);
     return c;
  }

  public String toString(int howToRead)
  {
    return new String(this.name) + "\t" + new String(this.vorstand) + "\t" + new String(this.tel) + "\t" +
        new String(this.fax) + "\t" + new String(this.email) + "\t" + this.getValue(5, 1) + "\t" + this.getValue(6, 1);
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  /**
   * <h3>Diese Methode wird zur Bearbeitung des Wertes in der Spalte "column" verwendet.</h3>
   * Folgende Werte d&uuml;rfen <b>nicht</b> bearbeitet werden:<br>
   * <font color=#ff1493><b>Code<br></b></font>
   * @param column die zur Bearbeitung stehende Spalte
   * @param howToRead der Leseart
   * @return false, falls den Wert in der Spalte "column" nicht bearbeitet werden darf,
   * true, sonst.
   */
  public boolean isCellEditable(int column, int howToRead)
  {
    if(column < 0)
      return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeUTF(new String(this.name));
      out.writeUTF(new String(this.vorstand));
      out.writeUTF(new String(this.tel));
      out.writeUTF(new String(this.fax));
      out.writeUTF(new String(this.email));
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

  private void recalculateKey()
  {
    this.code = Table.dtJSHashCode(new String(this.name));
  }

  public void setCode(long newCode)
  {

  }

  public void setParentTable(Table parentTable, int howToRead)
  {

  }

  public void setParentElement(TableElement parentElement, int howToRead)
  {
    if(howToRead == 1)
      this.parentElement = parentElement;
  }

  public Table getParentTable(int howToRead)
  {
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
    return this.getValue(column, howToRead);
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

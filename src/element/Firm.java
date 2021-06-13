package element;

import table.TableElement;
import utils.MyObjectOutputStream;
import java.io.IOException;
import table.Table;
import data.*;
import utils.DateRepresentation;
import utils.Consts;
import table.User;

public class Firm implements TableElement
{
  /**
   * Name einer Firma.
   */
  private char[] name;
  /**
   * Telefonnummer einer Firma.
   */
  private char[] tel;
  /**
   * Faxnummer einer Firma.
   */
  private char[] fax;
  /**
   * Email einer Firma.
   */
  private char[] email;
  /**
   * Strasse einer Firma.
   */
  private char[] str;
  /**
   * PLZ einer Firma.
   */
  private char[] plz;
  /**
   * Ort einer Firma.
   */
  private char[] ort;
  /**
   * die Land der Firma.
   */
  private char[] land;
  /**
   * beschreibt, ob eine Firma die Mehrwertsteuer bezahlen soll.
   */
  private boolean mwst;
  /**
   * Umsatzsteuer
   */
  private char[] uSt;
  /**
   * Steuernummer
   */
  private char[] stNr;
  /**
   * Handelsregister
   */
  private char[] web;
  /**
   * Mehrwertsteuer
   */
  protected double mehrWSt1, mwst2;

  protected char[] smtp, smtpAuthorization;

  protected char[] invoicesPath;

  protected double breakfastPrice;

  protected int nextInvoiceDay, nextInvoiceMonth, nextInvoiceYear;

  protected Table contactPersons;

  protected Table banks;

  /**
   * Code.
   * <b>Code wird vom Benutzer eingegeben.</b>
   */
  private long code;

  protected long nextInvoiceNo;

  /**
   * Konstruktor.
   * <b>Code wird vom Benutzer eingegeben.</b>
   * @param code Nummer einer Firma.
   * @param name Name einer Firma.
   * @param tel Telefonnummer einer Firma.
   * @param fax Faxnummer einer Firma.
   * @param email Email einer Firma.
   * @param str Strasse einer Firma.
   * @param plz PLZ einer Firma.
   * @param ort Ort einer Firma.
   * @param land Land einer Firma.
   * @param skonto zugehoeriges Rabatt
   * @param mwst die zu einer Firma zugeordnete Mehrwertsteuer
   */
  public Firm(long code, String name, String tel, String fax, String email, String str, String plz, String ort, String land,
              boolean mwst, String uSt, String stNr, String web, double mehrWSt, double mwst2, String smtp, String smtpAuthorization, double breakfastPrice)
  {
    this.name = name.toCharArray();
    this.tel = tel.toCharArray();
    this.fax = fax.toCharArray();
    this.email = email.toCharArray();
    this.str = str.toCharArray();
    this.plz = plz.toCharArray();
    this.ort = ort.toCharArray();
    this.land = land.toCharArray();
    this.mwst = mwst;
    this.uSt = uSt.toCharArray();
    this.stNr = stNr.toCharArray();
    this.web = web.toCharArray();
    this.mehrWSt1 = mehrWSt;
    this.mwst2 = mwst2;
    this.smtp = smtp.toCharArray();
    this.smtpAuthorization = smtpAuthorization.toCharArray();
    this.breakfastPrice = breakfastPrice;
    this.invoicesPath = new char[0];
    this.code = code;
    this.banks = new Table("banks_of_firm_" + code + "_" + name, new BankExample(), 1, 1, this, null);
    this.contactPersons = new Table("contact_persons_of_firm_" + code + "_" + name, new ContactPersonExample(), 1, 1, this, null);
  }

  /**
   * <h3>Diese Methode wird zur Darstellung des Wertes in der Spalte "column" benutzt.</h3>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Nummer<br>
   * 2. Name<br>
   * 3. Telefonnr<br>
   * 4. Fax<br>
   * 5. Email<br>
   * 6. Strasse<br>
   * 7. PLZ<br>
   * 8. Ort<br>
   * 9. Land<br>
   * 10. Skonto<br>
   * 11. Mehrwertsteuer(ja/nein)<br>
   * 12. Umsatzsteuer<br>
   * 13. Steuernummer<br>
   * 14. Handelsregister<br>
   * 15. Mehrwertsteuer(wert)</b></font><br>
   * @param column die Spalte.
   * @param howToRead der Leseart.
   * @return der Wert in der Spalte "column".
   */
  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0:
        return new Long(this.code);
      case 1:
        return new String(this.name);
      case 2:
        return new String(this.tel);
      case 3:
        return new String(this.fax);
      case 4:
        return new String(this.email);
      case 5:
        return new String(this.str);
      case 6:
        return new String(this.plz);
      case 7:
        return new String(this.ort);
      case 8:
        return new String(this.land);
      case 9:
        return new Boolean(this.mwst);
      case 10:
        return new String(this.uSt);
      case 11:
        return new String(this.stNr);
      case 12:
        return new String(this.web);
      case 13:
        return new Double(this.mehrWSt1);
      case 14:
        return new Double(this.mwst2);
      case 15:
        return new String(this.smtp);
      case 16:
        return new String(this.smtpAuthorization);
      case 17:
        return new Double(this.breakfastPrice);
      case 18:
        return new Long(this.nextInvoiceNo);
      case 19:
        return new String(this.invoicesPath);
      case 20:
        return new DateRepresentation(this.nextInvoiceDay, this.nextInvoiceMonth, this.nextInvoiceYear).toString();
      default:
        return(new Long(this.code)).toString();
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
      return this.code == ((Firm)obj).code;
    }
    catch(ClassCastException e)
    {
      return false;
    }
  }

  /**
   * <h3>Diese Methode wird zum Einstellen des neuen Werten benutzt.</h3>
   * Es d&uuml;rfen folgende Werte eingesetzt werden:<br>
   * <font color=#9400d3><b>Nummer<br>
   * Name<br>
   * Telefonnr<br>
   * Fax<br>
   * Email<br>
   * Strasse<br>
   * PLZ<br>
   * Ort<br>
   * Land<br>
   * Skonto<br>
   * Mehrwertsteuer(ja/nein)<br>
   * Umsatzsteuer<br>
   * Steuernummer<br>
   * Handelsregister<br>
   * Mehrwertsteuer(wert)</b></font><br>
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
      case 1:
        this.name = ((String)value).toCharArray();
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
        this.mwst = ((Boolean)value).booleanValue();
        break;
      case 10:
        this.uSt = ((String)value).toCharArray();
        break;
      case 11:
        this.stNr = ((String)value).toCharArray();
        break;
      case 12:
        this.web = ((String)value).toCharArray();
        break;
      case 13:
        this.mehrWSt1 = ((Double)value).doubleValue();
        break;
      case 14:
        this.mwst2 = ((Double)value).doubleValue();
        break;
      case 15:
        this.smtp = value.toString().toCharArray();
        break;
      case 16:
        this.smtpAuthorization = value.toString().toCharArray();
        break;
      case 17:
        this.breakfastPrice = ((Double)value).doubleValue();
        break;
      case 18:
        this.nextInvoiceNo = ((Long)value).longValue();
        break;
      case 19:
        this.invoicesPath = value.toString().toCharArray();
        break;
      case 20:
        DateRepresentation d = new DateRepresentation(value.toString());
        this.nextInvoiceDay = d.getDay();
        this.nextInvoiceMonth = d.getMonth();
        this.nextInvoiceYear = d.getYear();
        break;
    }
    return "";
  }

  public TableElement duplicate()
  {
    Firm f = new Firm(this.code, new String((char[])this.name.clone()),
                    new String((char[])this.tel.clone()), new String((char[])this.fax.clone()),
                    new String((char[])this.email.clone()), new String((char[])this.str.clone()),
                    new String((char[])this.plz.clone()), new String((char[])this.ort.clone()),
                    new String((char[])this.land.clone()), this.mwst,
                    new String((char[])this.uSt.clone()), new String((char[])this.stNr.clone()),
                    new String((char[])this.web.clone()), this.mehrWSt1, this.mwst2, new String((char[])this.smtp.clone()), new String((char[])this.smtpAuthorization), this.breakfastPrice);
    f.nextInvoiceNo = this.nextInvoiceNo;
    f.invoicesPath = (char[])this.invoicesPath.clone();
    f.nextInvoiceDay = this.nextInvoiceDay;
    f.nextInvoiceMonth = this.nextInvoiceMonth;
    f.nextInvoiceYear = this.nextInvoiceYear;
    return f;
  }

  public String toString(int howToRead)
  {
    String s = this.code + "\t" + new String(this.name) + "\t" + new String(this.tel) + "\t" +
        new String(this.fax) + "\t" + new String(this.email) + "\t" + new String(this.str) + "\t" +
        new String(this.plz) + "\t" + new String(this.ort) + "\t" + new String(this.land) + "\t" +
        "\t" + this.mwst + "\t" + this.uSt + "\t" + this.stNr + "\t" + this.web
        + "\t" + this.mehrWSt1 + "\t" + this.mwst2 + "\t" + new String(this.smtp) + "\t" + new String(this.smtpAuthorization) + "\t" + this.breakfastPrice + "\t" + this.nextInvoiceNo + "\t" + new String(this.invoicesPath) + this.getValue(20, 1);
    return s;
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  /**
   * <h3>Diese Methode wird zur Bearbeitung des Wertes in der Spalte "column" verwendet.</h3>
   * Folgende Werte d&uuml;rfen <b>nicht</b> bearbeitet werden:<br>
   * <font color=#ff1493><b>Code<br></b></font>
   * @param column die zur Bearbeitung stehnde Spalte
   * @param howToRead der Leseart
   * @return false, falls den Wert in der Spalte "column" nicht bearbeitet werden darf,
   * true, sonst.
   */
  public boolean isCellEditable(int column, int howToRead)
  {
    if(column == 0)
      return false;
    return true;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.code);
      out.writeUTF(new String(this.name));
      out.writeUTF(new String(this.tel));
      out.writeUTF(new String(this.fax));
      out.writeUTF(new String(this.email));
      out.writeUTF(new String(this.str));
      out.writeUTF(new String(this.plz));
      out.writeUTF(new String(this.ort));
      out.writeUTF(new String(this.land));
      out.writeBoolean(this.mwst);
      out.writeUTF(new String(this.uSt));
      out.writeUTF(new String(this.stNr));
      out.writeUTF(new String(this.web));
      out.writeDouble(this.mehrWSt1);
      out.writeDouble(this.mwst2);
      out.writeUTF(new String(this.smtp));
      out.writeUTF(new String(this.smtpAuthorization));
      out.writeDouble(this.breakfastPrice);
      out.writeLong(this.nextInvoiceNo);
      out.writeUTF(new String(this.invoicesPath));
      out.writeInt(this.nextInvoiceDay);
      out.writeInt(this.nextInvoiceMonth);
      out.writeInt(this.nextInvoiceYear);
      this.contactPersons.write(out);
      this.banks.write(out);
  }

  /**
   * <h3>Diese Methode gibt die Untertabellen aus.</h3>
   * <font color=#008b8b><b> 0 - die Untertabelle der Kontaktpersonen<br>
   * 1 - die Untertabelle der Banken<br>
   * 2 - die Untertabelle der Lieferanten.</b></font>
   * @param subtableIndex Index der Untertabelle
   * @return die zugehoerige Tabelle
   */
  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.contactPersons;
    if(subtableIndex == 1)
      return this.banks;
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    if(Data.db.getTable(Consts.tbHotels).size() < 2) return false;
    if(this.contactPersons.size() > 0 || this.banks.size() > 0) return false;
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

  /**
   * <h3>Diese Methode beschreibt was muss noch gemacht werden, wenn eine Firma gel&ouml;scht wurde.</h3>
       * <font color=#8b008b><b>Wenn der Klient geloescht wurde, dann muss er noch aus der Tabelle "clientDiscounts" geloescht werden.</b></font>
   * @param howToRead der Leseart
   * @return message die Fehlermeldung
   */
  public String wasRemoved(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    return "";
  }

  public void setCode(long newCode)
  {
    this.code = newCode;
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
    return this.code + " -"+ new String(this.name);
  }

  public String getTitle(int howToRead, int subtableIndex, String language, User user)
  {
    return Consts.getTitleHotel(howToRead, subtableIndex, language);
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
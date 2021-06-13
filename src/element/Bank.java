package element;

import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import java.io.IOException;
import table.Table;
import table.User;
import data.Data;
import utils.Consts;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert eine Bank.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Bank implements TableElement
{
  /**
   * Name der Bank.
   */
  private char[] bank;
  /**
   * BLZ.
   */
  private char[] blz;
  /**
   * Kontonummer.
   */
  private char[] konto;
  /**
   * SWIFT.
   */
  private char[] swift;
  /**
   * IBAN.
   */
  private char[] iban;
  /**
   * Plz.
   */
  private char[] plz;
  /**
   * Strasse.
   */
  private char[] str;
  /**
   * Ort
   */
  private char[] ort;
  /**
   * Land
   */
  private char[] land;
  /**
   * Code.
   * <p>Code wird nach den folgenden Parametern berechnet: <\p>
   * <b>- BLZ <\b><br>
   * <b>- Kontonummer <\b>
   */
  private long code;
  private TableElement parentElement;

  /**
   * Konstruktor.
   * <p>Code wird nach den folgenden Parametern berechnet: <\p>
   * <b>- BLZ <\b><br>
   * <b>- Kontonummer <\b>
   * @param bank die Name der Bank
   * @param blz die BLZ
   * @param konto die Kontonummer
   * @param swift SWIFT
   * @param iban IBAN
   * @param plz Plz
   * @param str Strasse
   * @param ort Ort
   * @param land Land
   */
  public Bank(String bank, String blz, String konto, String swift, String iban, String plz, String str, String ort, String land)
  {
    this.bank = bank.toCharArray();
    this.blz = blz.toCharArray();
    this.konto = konto.toCharArray();
    this.swift = swift.toCharArray();
    this.iban = iban.toCharArray();
    this.plz = plz.toCharArray();
    this.str = str.toCharArray();
    this.ort = ort.toCharArray();
    this.land = land.toCharArray();
    this.code = Table.dtJSHashCode(new String(this.blz) + new String(this.konto));
  }

  /**
   * <h3>Diese Methode wird zur Darstellung des Wertes in der Spalte "column" benutzt.</h3>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Bank<br>
   * 2. BLZ<br>
   * 3. Konto<br>
   * 4. SWIFT<br>
   * 5. IBAN<br>
   * 6. PLZ<br>
   * 7. Strasse<br>
   * 8. Ort<br>
   * 9. Land<br></b></font>
   * @param column die Spalte.
   * @param howToRead der Leseart.
   * @return der Wert in der Spalte "column".
   */
  public Object getValue(int column, int howToRead)
   {
     switch(column)
     {
       case -1: return new Long(this.code);
       case 0 : return new String(this.bank);
       case 1 : return new String(this.blz);
       case 2 : return new String(this.konto);
       case 3 : return new String(this.swift);
       case 4 : return new String(this.iban);
       case 5 : return new String(this.plz);
       case 6 : return new String(this.str);
       case 7 : return new String(this.ort);
       case 8 : return new String(this.land);
       default: return (new Long(this.code)).toString();
     }
   }

   public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
     {
       if(ascending)
         switch(column)
         {
       case -1: return this.getCode().compareTo(element.getCode());
       case 0 : return new String(this.bank).compareTo((String)element.getValue(column, howToRead));
       case 1 : return new String(this.blz).compareTo((String)element.getValue(column, howToRead));
       case 2 : return new String(this.konto).compareTo((String)element.getValue(column, howToRead));
       case 3 : return new String(this.swift).compareTo((String)element.getValue(column, howToRead));
       case 4 : return new String(this.iban).compareTo((String)element.getValue(column, howToRead));
       case 5 : return new String(this.plz).compareTo((String)element.getValue(column, howToRead));
       case 6 : return new String(this.str).compareTo((String)element.getValue(column, howToRead));
       case 7 : return new String(this.ort).compareTo((String)element.getValue(column, howToRead));
       case 8 : return new String(this.land).compareTo((String)element.getValue(column, howToRead));
       default: return new Long(this.code).compareTo((Long)element.getValue(column, howToRead));

      }
       else
         switch(column)
         {
           case -1: return element.getCode().compareTo(this.getCode());
           case 0 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.bank));
           case 1 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.blz));
           case 2 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.konto));
           case 3 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.swift));
           case 4 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.iban));
           case 5 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.plz));
           case 6 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.str));
           case 7 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.ort));
           case 8 : return ((String)element.getValue(column, howToRead)).compareTo(new String(this.land));
           default: return ((Long)element.getValue(column, howToRead)).compareTo(new Long(this.code));
         }
     }

     public boolean equals(TableElement element, int column, int howToRead)
       {
         if(this.compareTo(element, column, true, howToRead) == 0)
           return true;
         return false;
       }

       /**
        * <h3>Diese Methode wird zum Einstellen des neuen Werten benutzt.</h3>
        * Es d&uuml;rfen folgende Werte eingesetzt werden:<br>
        * <font color=#9400d3>Name der Bank<br>
        * BLZ<br>
        * Kontonummer<br>
        * SWIFT<br>
        * IBAN<br>
        * PLZ<br>
        * Strasse<br>
        * Ort<br>
        * Land<br></font>
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
      case 0 :
        this.bank = ((String)value).toCharArray();
        break;
      case 1 :
        this.blz = ((String)value).toCharArray();
        this.recalculateKey();
        break;
      case 2 :
        this.konto = ((String)value).toCharArray();
        this.recalculateKey();
        break;
      case 3 :
        this.swift = ((String)value).toCharArray();
        break;
      case 4 :
        this.iban = ((String)value).toCharArray();
        break;
      case 5 :
        this.plz = ((String)value).toCharArray();
        break;
      case 6:
        this.str = ((String)value).toCharArray();
        break;
      case 7 :
        this.ort = ((String)value).toCharArray();
        break;
      case 8 :
        this.land = ((String)value).toCharArray();
        break;
     }
     return "";
   }

   public TableElement duplicate()
     {
       return new Bank( new String((char[])this.bank.clone()), new String((char[])this.blz.clone()),
                        new String((char[])this.konto.clone()), new String((char[])this.swift.clone()),
                        new String((char[])this.iban.clone()), new String((char[])this.plz.clone()),
                        new String((char[])this.str.clone()), new String((char[])this.ort.clone()),
                        new String((char[])this.land.clone()) );
     }


   public String toString(int howToRead)
   {
     return new String(this.bank) + "\t" + new String(this.blz) + "\t" + new String(this.konto) + "\t" + new String(this.swift) + "\t" + new String(this.iban)
         + "\t" + new String(this.plz) + "\t" + new String(this.str) + "\t" + new String(this.ort) + "\t" + new String(this.land);
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
     if(column < 0) return false;
     return true;
   }

   public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
       out.writeUTF(new String(this.bank));
       out.writeUTF(new String(this.blz));
       out.writeUTF(new String(this.konto));
       out.writeUTF(new String(this.swift));
       out.writeUTF(new String(this.iban));
       out.writeUTF(new String(this.plz));
       out.writeUTF(new String(this.str));
       out.writeUTF(new String(this.ort));
       out.writeUTF(new String(this.land));
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
     this.code = Table.dtJSHashCode(new String(this.plz) + new String(this.konto));
   }

   public boolean equals(Object obj)
   {
     try
     {
       return this.code == ((Bank)obj).code;
     }
     catch(ClassCastException e)
     {
       return false;
     }
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

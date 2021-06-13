package table;

import utils.DateRepresentation;
import utils.MyObjectOutputStream;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Dennis Tuvia
 * @version 1.0
 * Eine Klasse, die dieses interface implementiert beschreibt eine Zeile aus
 * einer Tabelle mit Instazen solche Klassen.
 */

public interface TableElement
{
/**
 * Wert einer Zelle in Spalte "column"
 * Diese Methode wird fuer die Visualisierung der Werten in der Tabelle benutzt
*/
  public Object getValue(int column, int howToRead);
  public Object getValue(int column, int howToRead, User user, String language);
/**
 * Diese Methode wird fuer das Sortierverfahren von der Tabelle benutzt
 * Sortiert wird in der Spalte column und ascending steht fuer aufsteigend/absteigend
*/
//  public int compareTo(TableElement element, int column, boolean ascending, int howToRead);
  /**
   * Diese Methode wird fuer das Suchverfahren in der Tabelle benutzt
   * Gesucht wird in der Spalte column
  */
  public boolean equals(TableElement element, int column, int howToRead);
  /**
   * Wert einer Zelle in Spalte "column" einstellen
   * Diese Methode wird fuer die Aenderungen der Werten in der Tabelle benutzt
   * @return error code
  */
  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception;
  /**
   * Diese Methode wird fuer die Visualisierung der Werten in DOS modus benutzt
  */
  public String toString(int howToRead);

  public boolean equals(Object obj);
  /**
   * Hash code oder Nummer des Elements in der Tabelle
  */
  public Long getCode();
  /**
   * Ermoeglicht der Bearbeitung einer Zelle in Spalte column
  */
  public boolean isCellEditable(int column, int howToRead);
  /**
   * Eine Methode den Element zu schreiben
  */
  public void write(MyObjectOutputStream out, int howToRead) throws Exception;
  /**
   * Dupliziert den Element
   */
  public TableElement duplicate();

  /**
   * Gibt die Untertabelle aus
   * @param subtableIndex der Index, auf dem die Untertabelle ausgegeben wird
   * @return die Untertabelle
   */
  public Table getSubtable(int subtableIndex);

  /**
   * Diese Methode beschreibt, ob ein Element loeschbar ist.
   * @param howToRead der Leseart
   * @return false, falls das Element nicht geloescht werden darf,
   * true, sonst.
   */
  public boolean isRemovable(int howToRead);

  /**
   * Diese Methode beschreibt, ob ein Element verschiebbar ist.
   * @param howToRead der Leseart
   * @return false, falls das Element nicht verschoben werden darf,
   * true, sonst.
   */
  public boolean isMovable(int howToRead);

  /**
   * Wenn das neue Element hinzugefuegt wurde, wo soll noch was geaendert werden.
   * @param howToRead der Leseart
   * @return die Fehlermeldung
   */
  public String wasAdd(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date);

  /**
   * Diese Methode beschreibt Aenderungen, die im Fall des Loeschens des Element eintreten.
   * @param howToRead der Leseart
   * @return die Fehlermeldung
   */
  public String wasRemoved(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date);

  public void setCode(long newCode);

  public void setParentTable(Table parentTable, int howToRead);

  public void setParentElement(TableElement parentElement, int howToRead);

  public Table getParentTable(int howToRead);

  public TableElement getParentElement(int howToRead);

  public String getPulldownMenuElement(User user);

  public String getTitle(int howToRead, int subtableIndex, String language, User user);

  public String getCellColor(int howToRead, int column);

  public Table getReferenceTable(int column, int howToRead);

  public long getReferenceCode(int column, int howToRead, User user, String language);
}

package table;

import utils.DateRepresentation;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.util.Comparator;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Document;
import java.io.PrintWriter;

/**
 * <p>Title: Remote Sales Control</p>
 * <p>Description: Multi-user internet program</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Noname</p>
 * @author Dennis Tuvia
 * @version 3.0
 */

public interface TableElementExample
{
  /**
   * Namen der Spalten in Tabelle
   */
  public String[] getColumnNames(int howToRead, String language, User user);

  /**
   * Positionen der Spalten, dessen Werten werden f�r hash code benutzt
   */
  public int[] getKeyColumns(int howToRead);

  /**
   * Eine Methode den Element zu lesen
   */
  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable,
                           TableElement parentElement);

  /**
   * Diese Methode wird f�r instanziierung des Elements benutzt
   */
  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement);

  /**
   * Diese Methode beschreibt, ob ein Element teilbar ist.
   * @param columnIndex der Index der Spalte
   * @param howToRead der Leseart
   * @return false, falls das Element nach der Spalte "columnIndex" nicht geteilt werden darf,
   * true, sonst.
   */
  public boolean isSplittable(int columnIndex, int howToRead);

  /**
   * Es wird den Wert ausgegeben, nach dem geteilt wird.
   * @param howToRead der Leseart
   * @return der Wert
   */
  public int getAmountColumnIndex(int howToRead);

  public boolean isAutoIncrementable(int howToRead);

  public boolean hasSubtable(int howToRead, int index);

  public Table getSubtable(int howToRead, int index);

  public Class[] getColumnClasses(int howToRead);

  public String canBeMovedTo(int howToRead);

  public int[] canBeMovedPath(int howToRead);

  public int getMaxHowToRead();

  public void printTable(PrintWriter out, boolean view, int criteria, boolean ascending, User user);

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn,
                                 Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws Exception;

  public String getOperationName(int howToRead, int operationIndex, String language);

  public int[] getInitialParametersColumnIndices(int howToRead);

  public int[] getFractionDigits(int howToRead);

  public String getTitle(String language, int howToRead, User user);

  public boolean isSaved(int column, int howToRead);

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code,
                               int operation, String language);
  public boolean isAddEnabled(int howToRead);

  public boolean isRemoveEnabled(int howToRead);

  public boolean isMoveEnabled(int howToRead);

  public boolean isSetEnabled(int howToRead);

  public String getAddConfirmation(String language, int howToRead, User user);

  public String getRemoveConfirmation(String language, int howToRead, User user);

  public String getSetConfirmation(long code, TableElement parentElement, String language, int howToRead, User user);

  public String getMoveConfirmation(long code, TableElement parentElement, String language, int howToRead, User user);

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement, String language, int howToRead, User user, String[] parameters);

}
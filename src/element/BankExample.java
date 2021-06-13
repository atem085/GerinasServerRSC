package element;

import table.TableElementExample;
import table.TableElement;
import table.*;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import java.io.IOException;
import table.User;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;

/**
 *
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert eine</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class BankExample implements TableElementExample
{
  public BankExample()
  {
  }

  /**
   * <h3>Diese Methode gibt den Namen der Spalten aus.</h3>
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
   * @param howToRead der Leseart
   * @param language die Sprache
   * @return die Namen der Spalten.
   */
  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clBank(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[2];
    i[0] = 1;
    i[1] = 2;
    return i;
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[9];
    c[0] = String.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    c[5] = String.class;
    c[6] = String.class;
    c[7] = String.class;
    c[8] = String.class;
    return c;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new Bank( (String)parameters[0], (String)parameters[1], (String)parameters[2], (String)parameters[3], (String)parameters[4],
                       (String)parameters[5], (String)parameters[6], (String)parameters[7], (String)parameters[8] );
    }
    catch(Exception e)
    {
      //e.printStackTrace();
      return null;
    }
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      return new Bank(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF() );
    }
    catch(Exception e)
    {
      return null;
    }
  }


  public int getAmountColumnIndex(int howToRead)
  {
    return -2;
  }

  public boolean isSplittable(int columnIndex, int howToRead)
  {
    return false;
  }

  public boolean isAutoIncrementable(int howToRead)
  {
    return false;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    return null;
  }

  public String canBeMovedTo(int howToRead)
  {
    return "";
  }

  public int[] canBeMovedPath(int howToRead)
  {
    return null;
  }

  public int getMaxHowToRead()
  {
    return 1;
  }

  public void printTable(PrintWriter out, boolean view, int criteria, boolean ascending, User user)
  {

  }

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws Exception
  {
    return null;
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    return null;
  }

  public int[] getFractionDigits(int howToRead)
  {
    return null;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtBank(language);
  }

  public boolean isSaved(int column, int howToRead)
  {
    return true;
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code,
                               int operation, String language)
  {
  }

  public boolean isAddEnabled(int howToRead)
  {
    return false;
  }

  public boolean isRemoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isMoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isSetEnabled(int howToRead)
  {
    return false;
  }

  public String getAddConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getRemoveConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getSetConfirmation(long code, TableElement parentElement, String language, int howToRead,
                                   User user)
  {
    return "";
  }

  public String getMoveConfirmation(long code, TableElement parentElement, String language, int howToRead, User user)
  {
    return "";
  }

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement,
                                         String language, int howToRead, User user, String[] parameters)
  {
    return "";
  }
}

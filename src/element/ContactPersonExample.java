package element;

import table.TableElementExample;
import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import table.User;
import java.io.IOException;
import table.Table;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert ein Kontaktperson.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ContactPersonExample implements TableElementExample
{

  public ContactPersonExample()
  {
  }

  /**
   * <h3>Diese Methode gibt den Namen der Spalten aus.</h3>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Name<br>
   * 2. Vorstand<br>
   * 3. Telefonnr<br>
   * 4. Fax<br>
   * 5. Email</b></font><br>
   * @param howToRead der Leseart
   * @param language die Sprache
   * @return die Namen der Spalten.
   */
  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clContactPerson(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[5];
    i[0] = 0;
    i[1] = 1;
    i[2] = 2;
    i[3] = 3;
    i[4] = 4;
    return i;
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[5];
    c[0] = String.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
//    c[5] = String.class;
//    c[6] = String.class;
    return c;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new ContactPerson( (String)parameters[0], (String)parameters[1], (String)parameters[2], (String)parameters[3], (String)parameters[4], null);
    }
    catch(Exception e)
    {
//      e.printStackTrace();
      return null;
    }
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      ContactPerson c = new ContactPerson(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), null);
//      in.readUTF();
//      in.readInt();
//      in.readInt();
//      in.readInt();
      return c;
    }
    catch(Exception e)
    {
//      e.printStackTrace();
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
    return Consts.gtContactPerson(language);
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

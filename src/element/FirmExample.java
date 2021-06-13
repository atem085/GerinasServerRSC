package element;

import table.TableElementExample;
import table.TableElement;
import table.Table;
import table.User;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import java.io.IOException;
import data.*;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;

/**
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert einen Kunden.</b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class FirmExample implements TableElementExample
{

  protected Table contactPersons;

  protected Table banks;

  public FirmExample()
  {
    this.banks = new Table("banks_of_firm_", new BankExample(), 1, 1, null, null);
    this.contactPersons = new Table("contact_persons_of_firm_", new ContactPersonExample(), 1, 1, null, null);
  }

  /**
   * <h3>Diese Methode gibt den Namen der Spalten aus.</h3>
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
   * @param howToRead der Leseart
   * @param language die Sprache
   * @return die Namen der Spalten.
   */
  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clFirm(language);
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[14];
    i[0] = 1;
    i[1] = 2;
    i[2] = 3;
    i[3] = 4;
    i[4] = 5;
    i[5] = 6;
    i[6] = 7;
    i[7] = 8;
    i[8] = 9;
    i[9] = 10;
    i[10] = 11;
    i[11] = 12;
    i[12] = 13;
    i[13] = 14;
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
    Class[] c = new Class[20];
    c[0] = Long.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    c[5] = String.class;
    c[6] = String.class;
    c[7] = String.class;
    c[8] = String.class;
    c[9] = Boolean.class;
    c[10] = String.class;
    c[11] = String.class;
    c[12] = String.class;
    c[13] = Double.class;
    c[14] = Double.class;
    c[15] = String.class;
    c[16] = String.class;
    c[17] = Double.class;
    c[18] = Long.class;
    c[19] = String.class;
    c[20] = String.class;
    return c;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new Firm(Long.MAX_VALUE, (String)parameters[0],
                          (String)parameters[1], (String)parameters[2],
                          (String)parameters[3], (String)parameters[4],
                          (String)parameters[5], (String)parameters[6],
                          (String)parameters[7], ((Boolean)parameters[8]).booleanValue(), (String)parameters[9],
                          (String)parameters[10], (String)parameters[11], ((Double)parameters[12]).doubleValue(), ((Double)parameters[13]).doubleValue(), "", "", 4.8);
    }
    catch(Exception e)
    {
      //e.printStackTrace();
      return null;
    }
  }

  public static double getVAT()
  {
    return ((Double)Data.db.getTable(Consts.tbHotels).at(0).getValue(13, 1)).doubleValue();
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      Firm f = new Firm(in.readLong(), in.readUTF(), in.readUTF(),
                                in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(),
                                in.readUTF(), in.readUTF(), in.readBoolean(),
                                in.readUTF(), in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(), in.readUTF(), in.readUTF(), in.readDouble());
      f.nextInvoiceNo = in.readLong();
      f.invoicesPath = in.readUTF().toCharArray();
      f.nextInvoiceDay = in.readInt();
      f.nextInvoiceMonth = in.readInt();
      f.nextInvoiceYear = in.readInt();
      f.contactPersons.load(in, false);
      f.banks.load(in, false);
      return f;
    }
    catch(Exception e)
    {
      e.printStackTrace();
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
    return true;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    if(howToRead == 1 && index < 2) return true;
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    if(howToRead == 1 && index == 0)
      return this.contactPersons;
    if(howToRead == 1 && index == 1)
      return this.banks;
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
    return Consts.gtHotels(language);
  }

  public boolean isSaved(int column, int howToRead)
  {
    if(column == 0) return true;
    return false;
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code,
                               int operation, String language)
  {
  }

  public boolean isAddEnabled(int howToRead)
  {
    if(howToRead == 1)
      return true;
    return false;
  }

  public boolean isRemoveEnabled(int howToRead)
  {
    if(howToRead == 1) return true;
    return false;
  }

  public boolean isMoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isSetEnabled(int howToRead)
  {
    if(howToRead == 1) return true;
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

  public String getMoveConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement,
                                         String language, int howToRead, User user)
  {
    return "";
  }

  public String getMoveConfirmation(long code, TableElement parentElement, String language, int howToRead,
                                    User user)
  {
    return "";
  }

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement,
                                         String language, int howToRead, User user, String[] parameters)
  {
    return "";
  }
}

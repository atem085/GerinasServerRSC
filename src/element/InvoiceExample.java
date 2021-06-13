package element;

import table.TableElementExample;
import table.TableElement;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import table.Table;
import java.io.PrintWriter;
import table.User;
import utils.MyObjectOutputStream;
import utils.Consts;
import data.Data;

public class InvoiceExample implements TableElementExample
{
  protected Table services;

  public InvoiceExample()
  {
    this.services = new Table("services_of_invoice_", new InvoicePositionExample(), 1, 0, null, null);
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clInvoice(language);
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
//      in.setEcho(true);
      Invoice inv = new Invoice(in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readLong(), in.readLong(),
                         in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readUTF(),
                         in.readUTF());
      if(inv.booking == null)
      {
        if(in.readBoolean())
          inv.paymentMethod = Data.db.getTable(Consts.tbPayment).findElement(in.readLong());
      }
      inv.services.load(in, false);
      return inv;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    Firm h = (Firm)Data.db.getTable(Consts.tbHotels).at(0);
    return new Invoice(parameters[0].toString(), ((Long)parameters[1]).longValue(), ((Long)parameters[2]).longValue(), h.breakfastPrice, h.mehrWSt1, h.mwst2, parameters[3].toString(), ((Long)parameters[4]).longValue());
  }

  public boolean isSplittable(int columnIndex, int howToRead)
  {
    return false;
  }

  public int getAmountColumnIndex(int howToRead)
  {
    return -1;
  }

  public boolean isAutoIncrementable(int howToRead)
  {
    return true;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    if(howToRead == 1 && index == 0) return true;
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    if(howToRead == 1 && index == 0)
      return this.services;
    return null;
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[23];
    c[0] = Long.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    c[5] = String.class;
    c[6] = Double.class;
    c[7] = Double.class;
    c[8] = Integer.class;
    c[9] = Integer.class;
    c[10] = Double.class;
    c[11] = Double.class;
    c[12] = Double.class;
    c[13] = Double.class;
    c[14] = Double.class;
    c[15] = Double.class;
    c[16] = Double.class;
    c[17] = String.class;
    c[18] = String.class;
    c[19] = String.class;
    c[20] = Double.class;
    c[21] = Integer.class;
    c[22] = Integer.class;
    return c;
  }

  public String canBeMovedTo(int howToRead)
  {
    return null;
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
    if(howToRead == 1 && operationIndex == 0)
    {
      Stay st = (Stay)((Invoice)currentTable.findElement(selectedCode)).booking;
      currentTable.removeTableElement(selectedCode, language, currentUser, date);
      st.checkedOut = false;
      st.keyReturned = false;
      st.checkedIn = false;
      Data.db.getTable(Consts.tbStay).removeTableElement(st.getCode().longValue(), language, currentUser, date);
    }
    return "";
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    if(howToRead == 1 && operationIndex == 0)
      return "Remove invoice + booking";
    return "";
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[4];
    i[0] = 1;
    i[1] = 3;
    i[2] = 5;
    i[3] = 17;
    return i;
  }

  public int[] getFractionDigits(int howToRead)
  {
    int[] i = new int[23];
    i[0] = 0;
    i[1] = 0;
    i[2] = 0;
    i[3] = 0;
    i[4] = 0;
    i[5] = 0;
    i[6] = 3;
    i[7] = 3;
    i[8] = 0;
    i[9] = 0;
    i[10] = 2;
    i[11] = 2;
    i[12] = 2;
    i[13] = 2;
    i[14] = 2;
    i[15] = 2;
    i[16] = 2;
    i[17] = 0;
    i[18] = 0;
    i[19] = 0;
    i[20] = 0;
    i[21] = 0;
    i[22] = 0;
    return i;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtInoices(language);
  }

  public boolean isSaved(int column, int howToRead)
  {
    return false;
  }

  public void confirmOperation(MyObjectInputStream inStream, MyObjectOutputStream outStream, User user, int howToRead, long code, int operation, String language)
  {
  }

  public boolean isAddEnabled(int howToRead)
  {
    return true;
  }

  public boolean isRemoveEnabled(int howToRead)
  {
    return true;
  }

  public boolean isMoveEnabled(int howToRead)
  {
    return false;
  }

  public boolean isSetEnabled(int howToRead)
  {
    return true;
  }

  public String getAddConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getRemoveConfirmation(String language, int howToRead, User user)
  {
    return "";
  }

  public String getSetConfirmation(long code, TableElement parentElement, String language, int howToRead, User user)
  {
    return "";
  }

  public String getMoveConfirmation(long code, TableElement parentElement, String language, int howToRead, User user)
  {
    return "";
  }

  public String getOperationConfirmation(long code, int operationIndex, TableElement parentElement, String language, int howToRead, User user, String[] parameters)
  {
    return "";
  }

}
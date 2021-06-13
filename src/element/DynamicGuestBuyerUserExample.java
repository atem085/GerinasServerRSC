package element;

import table.TableElementExample;
import table.TableElement;
import table.*;
import table.User;
import utils.DateRepresentation;
import utils.MyObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import utils.Consts;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;

/**
 *
 * <p>Title: </p>
 * <p>Description: <font color=red><b>Diese Klasse repr&auml;sentiert den Benutzer.<b></font></p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DynamicGuestBuyerUserExample implements TableElementExample
{
  /**
   * der Name des Benutzers.
   */
  public char[] name;
  /**
   * das Kennwort des Benutzers.
   */
  public char[] password;
  /**
   * Das Status des Benutzers(ob er eingelogt ist).
   */
  public boolean isLoggedIn;
  private char[] realName;
  private TableElement buyer;
  public Table settings;
  /**
   * Code.<br>
   * Code wird nach den folgenden Parametern berechnet:<br><br>
   * <b>Name des Benutzers<br>Kennwort des Benutzers</b>
   */
  private long code;

  public DynamicGuestBuyerUserExample(String name, String password, String realName, long codeBuyer)
  {
    this.name = name.toCharArray();
    this.password = password.toCharArray();
    this.isLoggedIn = false;
    this.realName = realName.toCharArray();
    this.code = Table.dtJSHashCode(new String(name));
    this.settings = new Table("settings_of_dynamic_guest_buyer_user_" + new String(this.name) + "_" + new String(this.password), new TableAuthorizationExample("", true), 1, 0, null, null);
  }

  /**
   * Diese Methode gibt den Namen der Spalten aus.<br>
   * Die Tabelle hat folgende Spalten:<br>
   * <font color=#9400d3><b>1. Name<br>
   * 2. Kennwort<br>
   * 3. Status(Eingelogt(ja/nein))</b></font><br>
   * @param howToRead der Leseart
   * @param language die Sprache
   * @return die Namen der Spalten.
   */
  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clDynamicGuestBuyerUser(language);
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[1];
    i[0] = 0;
    return i;
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public Class[] getColumnClasses(int howToRead)
  {
   Class[] c = new Class[6];
   c[0] = String.class;
   c[1] = String.class;
   c[2] = Boolean.class;
   c[3] = String.class;
   c[4] = Long.class;
   c[5] = String.class;
   return c;
  }

  public Table getSubtable(int howToRead, int index)
  {
    return this.settings;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    try
    {
      return new DynamicGuestBuyerUser( (String)parameters[0], (String)parameters[1], (String)parameters[2], ((Long)parameters[3]).longValue(), howToRead);
    }
    catch(Exception e)
    {
      //e.printStackTrace();
      return null;
    }
  }

  public boolean isAutoIncrementable(int howToRead)
  {
    return false;
  }

  public boolean hasSubtable(int howToRead, int index)
  {
    if(index == 0) return true;
    return false;
  }

  /**
   * Diese Methode wird f&uuml;r das Lesen eines Elementes verwendet.<br>
   * Es werden das kodierte Kennwort(anschliessend dekodiert) und alle Kommanden gelesen.
   * @param in MyObjectInputStream
   * @param howToRead der Leseart
   * @param parentTable die Obertabelle
   * @param parentElement das OberElement
   * @return das Element in der Tabelle
   */
  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    int comType;
    try
    {
      DynamicGuestBuyerUser eu = new DynamicGuestBuyerUser(in.readUTF(), in.readUTF(), in.readUTF(), in.readLong(), howToRead);
      eu.isLoggedIn = in.readBoolean();
       eu.settings.load(in, false);
       return eu;
     }
     catch(Exception e)
     {
       //e.printStackTrace();
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
    return Consts.gtGuestBuyerUser(language, howToRead);
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

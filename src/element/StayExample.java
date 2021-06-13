package element;

import java.io.*;
import java.util.Hashtable;
import table.TableElementExample;
import table.TableElement;
import utils.MyObjectInputStream;
import table.Table;
import java.io.PrintWriter;
import table.User;
import utils.MyObjectOutputStream;
import utils.Consts;
import utils.DateRepresentation;
import data.Data;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import utils.Round;

public class StayExample implements TableElementExample
{
  public StayExample(int howToRead)
  {
    if (howToRead == 1)
      getFileBooking();
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clStay(language);
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[0];
    return i;
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    try
    {
      Stay st = new Stay(in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                         in.readBoolean(), in.readLong(), in.readLong(), in.readLong(), in.readInt(), in.readUTF(), in.readBoolean(), in.readInt(), in.readInt(),
                         in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readUTF(), in.readBoolean(), in.readBoolean(), in.readBoolean(), in.readLong(),
                         in.readDouble(), in.readLong(), in.readBoolean() ? in.readLong() : -1, in.readBoolean());
      if (in.readBoolean())
        st.payer2 = Data.db.getTable(Consts.tbCustomers).findElement(in.readLong());
      if (in.readBoolean())
        st.guest2 = Data.db.getTable(Consts.tbCustomers).findElement(in.readLong());
      if (in.readBoolean())
        st.payer3 = Data.db.getTable(Consts.tbCustomers).findElement(in.readLong());
      if (in.readBoolean())
        st.guest3 = Data.db.getTable(Consts.tbCustomers).findElement(in.readLong());
      st.combine = in.readBoolean();
      st.invoiceNo2 = in.readLong();
      st.invoiceNo3 = in.readLong();
      st.onlineBookingNo = in.readLong();
      st.color = in.readUTF().toCharArray();
      st.log = in.readUTF().toCharArray();
      return st;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    DateRepresentation inDate = new DateRepresentation(parameters[0].toString());
    DateRepresentation outDate = new DateRepresentation(parameters[1].toString());
    if (inDate.compareTo(outDate) >= 0)
    {
      System.out.println("Inkorrektes Datum");
      return null;
    }
    long roomCode = ( (Long) parameters[2]).longValue();
    TableElement room = Data.db.getTable(Consts.tbRooms).findElement(roomCode);
    if (RoomExample.isBusy(room, inDate, outDate))
    {
      System.out.println("Besetzt");
      return null;
    }
    return new Stay(parameters[0].toString(), parameters[1].toString(), ( (Long) parameters[2]).longValue(), ( (Long) parameters[3]).longValue(),
                    ( (Long) parameters[4]).longValue(), ( (Integer) parameters[5]).intValue(), ( (Boolean) parameters[6]).booleanValue(), parameters[7].toString(),
                    ( (Double) parameters[8]).doubleValue(), ( (Long) parameters[9]).longValue(), ( (Long) parameters[10]).longValue(), parameters[11].toString());
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
    return false;
  }

  public Table getSubtable(int howToRead, int index)
  {
    return null;
  }

  public Class[] getColumnClasses(int howToRead)
  {
    Class[] c = new Class[45];
    c[0] = String.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = Boolean.class;
    c[4] = Long.class;
    c[5] = Integer.class;
    c[6] = String.class;
    c[7] = String.class;
    c[8] = String.class;
    c[9] = String.class;
    c[10] = String.class;
    c[11] = String.class;
    c[12] = String.class;
    c[13] = Integer.class;
    c[14] = String.class;
    c[15] = Boolean.class;
    c[16] = String.class;
    c[17] = String.class;
    c[18] = String.class;
    c[19] = Boolean.class;
    c[20] = Boolean.class;
    c[21] = Boolean.class;
    c[22] = Long.class;
    c[23] = Double.class;
    c[24] = String.class;
    c[25] = String.class;
    c[26] = Boolean.class;
    c[27] = String.class;
    c[28] = String.class;
    c[29] = String.class;
    c[30] = String.class;
    c[31] = Boolean.class;
    c[32] = String.class;
    c[33] = String.class;
    c[34] = String.class;
    c[35] = String.class;
    c[36] = Long.class;
    c[37] = Long.class;
    c[38] = String.class;
    c[39] = Integer.class;
    c[40] = Double.class;
    c[41] = Long.class;
    c[42] = Long.class;
    c[43] = String.class;
    c[44] = String.class;
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

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date) throws
      Exception
  {
    if (howToRead == 1 && operationIndex == 4)
    {
      Stay stay = (Stay) currentTable.findElement(selectedCode);
      Table rooms = Data.db.getTable(Consts.tbRooms);
      RoomExample re = (RoomExample) rooms.getExample();
      DateRepresentation startDate = new DateRepresentation(stay.checkinDay, stay.checkinMonth, stay.checkinYear);
      DateRepresentation endDate = new DateRepresentation(stay.checkoutDay, stay.checkoutMonth, stay.checkoutYear);
      boolean roomFree = false;
      long lastRoomTypeCode = 0;
      long lastRoomCode = 0;
      Stay st = (Stay) stay.duplicate();
      st.combine = false;
      st.invoiceNo = 0;
      st.price = -stay.price;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        stay.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        stay.room = newRoom;
      }
      roomFree = false;
      lastRoomTypeCode = 0;
      lastRoomCode = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        st.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        st.room = newRoom;
      }
      currentTable.addTableElement(st, language, currentUser, date);
      st.log = (Consts.logTemplate(st.log, date, currentUser, "") + " Storno Zimmer \n").toCharArray();
      return "";
    }
    if (howToRead == 1 && operationIndex == 3)
    {
      Stay stay = (Stay) currentTable.findElement(selectedCode);
      Table rooms = Data.db.getTable(Consts.tbRooms);
      RoomExample re = (RoomExample) rooms.getExample();
      DateRepresentation startDate = new DateRepresentation(stay.checkinDay, stay.checkinMonth, stay.checkinYear);
      DateRepresentation endDate = new DateRepresentation(stay.checkoutDay, stay.checkoutMonth, stay.checkoutYear);
      boolean roomFree = false;
      long lastRoomTypeCode = 0;
      long lastRoomCode = 0;
      Stay st = (Stay) stay.duplicate();
      st.price = -stay.price;
      st.invoiceNo = 0;
      Stay st2 = (Stay) stay.duplicate();
      DateRepresentation chin = new DateRepresentation(parameters[0].toString());
      DateRepresentation chout = new DateRepresentation(parameters[1].toString());
      st2.checkinDay = chin.getDay();
      st2.checkinMonth = chin.getMonth();
      st2.checkinYear = chin.getYear();
      st2.checkoutDay = chout.getDay();
      st2.checkoutMonth = chout.getMonth();
      st2.checkoutYear = chout.getYear();
      chin.add(chin.DATE, 1);
      st2.breakfastFromDay = chin.getDay();
      st2.breakfastFromMonth = chin.getMonth();
      st2.breakfastFromYear = chin.getYear();
      st2.breakfastTillDay = chout.getDay();
      st2.breakfastTillMonth = chout.getMonth();
      st2.breakfastTillYear = chout.getYear();
      st2.invoiceNo = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        stay.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        stay.room = newRoom;
      }
      roomFree = false;
      lastRoomTypeCode = 0;
      lastRoomCode = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        st.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        st.room = newRoom;
      }
      currentTable.addTableElement(st, language, currentUser, date);
      currentTable.addTableElement(st2, language, currentUser, date);
      st.log = (Consts.logTemplate(st.log, date, currentUser, "") + " Storno Übernachtungstage [" + chin.toString() + " " + chout.toString() + "]\n").toCharArray();
      return "";
    }
    if (howToRead == 1 && operationIndex == 2)
    {
      Stay stay = (Stay) currentTable.findElement(selectedCode);
      Table rooms = Data.db.getTable(Consts.tbRooms);
      RoomExample re = (RoomExample) rooms.getExample();
      DateRepresentation startDate = new DateRepresentation(stay.checkinDay, stay.checkinMonth, stay.checkinYear);
      DateRepresentation endDate = new DateRepresentation(stay.checkoutDay, stay.checkoutMonth, stay.checkoutYear);
      boolean roomFree = false;
      long lastRoomTypeCode = 0;
      long lastRoomCode = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        stay.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        stay.room = newRoom;
      }
      Stay st = (Stay) stay.duplicate();
      st.price = -stay.price;
      st.invoiceNo = 0;
      roomFree = false;
      lastRoomTypeCode = 0;
      lastRoomCode = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        st.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 1, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        st.room = newRoom;
      }
      currentTable.addTableElement(st, language, currentUser, date);
      st.log = (Consts.logTemplate(st.log, date, currentUser, "") + " Storno \n").toCharArray();
      return "";
    }
    if (howToRead == 1 && operationIndex == 1)
    {
      double fee = Double.parseDouble(parameters[0].toString());
      Stay stay = (Stay) currentTable.findElement(selectedCode);
      Table rooms = Data.db.getTable(Consts.tbRooms);
      RoomExample re = (RoomExample) rooms.getExample();
      DateRepresentation startDate = new DateRepresentation(stay.checkinDay, stay.checkinMonth, stay.checkinYear);
      DateRepresentation endDate = new DateRepresentation(stay.checkinDay, stay.checkinMonth, stay.checkinYear);
      endDate.add(endDate.DATE, 1);
      stay.checkoutDay = endDate.getDay();
      stay.checkoutMonth = endDate.getMonth();
      stay.checkoutYear = endDate.getYear();
      stay.price = fee;
      if (stay.note.length == 0)
        stay.note = "STORNOGEBÜHR".toCharArray();
      else
        stay.note = (new String(stay.note) + " STORNOGEBÜHR").toCharArray();
      stay.breakfast = false;
      if (! ( (Customer2) stay.payer).isFirm())
        currentTable.executeOperation(new Object[0], selectedCode, selectedColumn, 0, language, currentUser, date);
      boolean roomFree = false;
      long lastRoomTypeCode = 0;
      long lastRoomCode = 0;
      for (int i = rooms.size() - 1; i > -1; i--)
      {
        Room room = (Room) rooms.at(i);
        if (i == rooms.size() - 1)
        {
          lastRoomCode = room.getCode().longValue();
          lastRoomTypeCode = room.roomType.getCode().longValue();
        }
        if (room.getCode().longValue() < 1000)
          break;
        if (re.isBusy(room, startDate, endDate))
          continue;
        stay.room = room;
        roomFree = true;
        break;
      }
      if (!roomFree)
      {
        Room newRoom = new Room(lastRoomCode + 1000, 0, "Stornozimmer", "Stornozimmer", lastRoomTypeCode);
        rooms.addTableElement(newRoom, language, currentUser, date);
        stay.room = newRoom;
      }
      stay.log = (Consts.logTemplate(stay.log, date, currentUser, "") + " Storno mit Gebühr \n").toCharArray();
      return "";
    }
    if (howToRead == 1 && operationIndex == 0)
    {
      Stay st = (Stay) currentTable.findElement(selectedCode);
      Table invoices = Data.db.getTable(Consts.tbInvoices);
      Table t = Data.db.getTable(Consts.tbCustomers);
      boolean invoiceExists = false;
      for (int i = 0; i < invoices.size(); i++)
        if ( ( (Invoice) invoices.at(i)).customer == st.payer)
        {
          invoiceExists = true;
          break;
        }
      if (invoiceExists)
      {
        Customer2 duplicate = (Customer2) st.payer.duplicate();
        duplicate.firm = (new String( (char[]) duplicate.name.clone()) + " " + new String( (char[]) duplicate.surname.clone())).toCharArray();
        duplicate.name = new char[0];
        duplicate.surname = new char[0];
        int[] foundFirms = t.findTableElementsIgnoreCase(new String(duplicate.firm), 3);
        if (foundFirms.length > 0)
          st.payer = t.at(foundFirms[0]);
        else
        {
          t.addTableElement(duplicate, language, currentUser, date);
          st.payer = duplicate;
        }
      }
      else
      {
        String fr = new String( (char[]) ( (Customer2) st.payer).name.clone()) + " " + new String( (char[]) ( (Customer2) st.payer).surname.clone());
        int[] foundFirms = t.findTableElementsIgnoreCase(fr, 3);
        if (foundFirms.length > 0)
          st.payer = t.at(foundFirms[0]);
        else
        {
          ( (Customer2) st.payer).firm = fr.toCharArray();
          ( (Customer2) st.payer).name = new char[0];
          ( (Customer2) st.payer).surname = new char[0];
        }
      }
      st.log = (Consts.logTemplate(st.log, date, currentUser, "") + " Selbstständig \n").toCharArray();
    }
    if (howToRead == 1 && operationIndex == 5)
    {
      Stay st = (Stay) currentTable.findElement(selectedCode);
      Table invoices = Data.db.getTable(Consts.tbInvoices);
      Table t = Data.db.getTable(Consts.tbCustomers);
      boolean invoiceExists = false;
      for (int i = 0; i < invoices.size(); i++)
        if ( ( (Invoice) invoices.at(i)).customer == st.payer)
        {
          invoiceExists = true;
          break;
        }
      if (invoiceExists)
      {
        Customer2 duplicate = (Customer2) st.payer.duplicate();
        String f = new String( (char[]) duplicate.firm.clone());
        String firstName = "";
        String lastName = "";
        if (f.indexOf(" ") > 0)
        {
          firstName = f.substring(0, f.indexOf(" "));
          lastName = f.substring(f.indexOf(" ") + 1, f.length());
        }
        else
          lastName = f;
        duplicate.firm = new char[0];
        duplicate.name = firstName.toCharArray();
        duplicate.surname = lastName.toCharArray();
        int[] foundFirms = t.findTableElementsIgnoreCase(new String(duplicate.name), 1);
        int[] foundFirms2 = t.findTableElementsIgnoreCase(new String(duplicate.surname), 2);
        boolean match = false;
        int matchIndex = -1;
        for (int i = 0; i < foundFirms.length; i++)
          for (int j = 0; j < foundFirms2.length; j++)
            if (foundFirms[i] == foundFirms2[j])
            {
              match = true;
              matchIndex = i;
            }
        if (match)
          st.payer = t.at(matchIndex);
        else
        {
          t.addTableElement(duplicate, "en", currentUser, date);
          st.payer = duplicate;
        }
      }
      else
      {
        String f = new String( (char[]) ( (Customer2) st.payer).firm.clone());
        String firstName = "";
        String lastName = "";
        if (f.indexOf(" ") > 0)
        {
          firstName = f.substring(0, f.indexOf(" "));
          lastName = f.substring(f.indexOf(" ") + 1, f.length());
        }
        else
          lastName = f;
        int[] foundFirms = t.findTableElementsIgnoreCase(firstName, 1);
        int[] foundFirms2 = t.findTableElementsIgnoreCase(lastName, 2);
        boolean match = false;
        int matchIndex = -1;
        for (int i = 0; i < foundFirms.length; i++)
          for (int j = 0; j < foundFirms2.length; j++)
            if (foundFirms[i] == foundFirms2[j])
            {
              match = true;
              matchIndex = i;
            }
        if (match)
          st.payer = t.at(matchIndex);
        else
        {
          ( (Customer2) st.payer).firm = new char[0];
          ( (Customer2) st.payer).name = firstName.toCharArray();
          ( (Customer2) st.payer).surname = lastName.toCharArray();
        }
      }
      st.log = (Consts.logTemplate(st.log, date, currentUser, "") + " Privat \n").toCharArray();
      return "";
    }
    return "";
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    if (howToRead == 1 && operationIndex == 0)
      return "Selbststaendig";
    if (howToRead == 1 && operationIndex == 1)
      return "Cancel with Fee";
    if (howToRead == 1 && operationIndex == 2)
      return "Cancel";
    if (howToRead == 1 && operationIndex == 3)
      return "Cancel day";
    if (howToRead == 1 && operationIndex == 4)
      return "Cancel room";
    if (howToRead == 1 && operationIndex == 5)
      return "Private";
    return null;
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    int[] i = new int[11];
    i[0] = 0;
    i[1] = 1;
    i[2] = 4;
    i[3] = 8;
    i[4] = 11;
    i[5] = 13;
    i[6] = 15;
    i[7] = 18;
    i[8] = 23;
    i[9] = 24;
    i[10] = 25;
    return i;
  }

  public int[] getFractionDigits(int howToRead)
  {
    int[] i = new int[41];
    i[0] = 0;
    i[1] = 0;
    i[2] = 0;
    i[3] = 0;
    i[4] = 0;
    i[5] = 0;
    i[6] = 0;
    i[7] = 0;
    i[8] = 0;
    i[9] = 0;
    i[10] = 0;
    i[11] = 0;
    i[12] = 0;
    i[13] = 0;
    i[14] = 0;
    i[15] = 0;
    i[16] = 0;
    i[17] = 0;
    i[18] = 0;
    i[19] = 0;
    i[20] = 0;
    i[21] = 0;
    i[22] = 0;
    i[23] = 2;
    i[24] = 0;
    i[25] = 0;
    i[26] = 0;
    i[27] = 0;
    i[28] = 0;
    i[29] = 0;
    i[30] = 0;
    i[31] = 0;
    i[32] = 0;
    i[33] = 0;
    i[34] = 0;
    i[35] = 0;
    i[36] = 0;
    i[37] = 0;
    return i;
  }

  public String getTitle(String language, int howToRead, User user)
  {
    if (howToRead == 1)
      return Consts.gtStay(language);
    return Consts.gtDraftBooking(language);
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
    if (howToRead == 1)
      return true;
    return false;
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

  public Hashtable doHashTableContingent(String dateFrom, String dateTo)
  {
    Table roomTypes = Data.db.getTable(Consts.tbRoomTypes);
    Table rooms = Data.db.getTable(Consts.tbRooms);
    Hashtable amountFreeRoomsInPeriod = new Hashtable();
    for (int j = roomTypes.size() - 1; j > -1; j--)
    {
      DateRepresentation startDate = new DateRepresentation(dateFrom);
      DateRepresentation finishDate = new DateRepresentation(dateTo);
      int differentDays = finishDate.calculateDifference(startDate);
      TableElement currentRoomType = roomTypes.at(j);
      String rt = currentRoomType.getValue(0, 1).toString();
      amountFreeRoomsInPeriod.put(rt, new Integer(1000));
      int free = 0;
      if(((RoomType)roomTypes.at(j)).getMinDays(startDate) > differentDays)
      {
        amountFreeRoomsInPeriod.put(rt, new Integer(0));
        continue;
      }
      for (int i = 0; i < differentDays; i++)
      {
        free = 0;
        if (i > 0)
         startDate.add(startDate.DATE, 1);
        if (rt.equalsIgnoreCase("leer") || rt.equalsIgnoreCase("Stornogebühr"))
          free = 1000;
        for (int k = 0; k < rooms.size(); k++)
        {
          Room currentRoom = (Room) rooms.at(k);
          if (currentRoom.roomType != currentRoomType) continue;
          if (!currentRoom.isBusy(startDate))
            free++;
        }
        amountFreeRoomsInPeriod.put(rt, new Integer(Math.min( ( (Integer) amountFreeRoomsInPeriod.get(rt)).intValue(), free)));
      }
    }
    return amountFreeRoomsInPeriod;
  }


  private void getFileBooking()
  {
    final File DIR = new File("C:\\RSCTomcatGate\\requests");
    new Thread()
    {
      public void run()
      {
       while(Data.db == null || !Data.dbLoaded)
          try
          {
            Thread.sleep(100);
          } catch (Exception e){}
        while(Data.canBeRunning)
        {
          if (DIR.listFiles().length > 0)
          {
            try
            {
              Thread.sleep(100);
            } catch (Exception e){}
            String basicFileName = "";
            try
            {
              if (DIR.listFiles().length > 0)
                basicFileName = DIR.listFiles()[DIR.listFiles().length - 1].getName().split("=")[0].split("\\.")[0];
            }
            catch(Exception e)
            {
              continue;
            }
            if (basicFileName.equals("checkinFile"))
            {
              while (!DIR.listFiles()[DIR.listFiles().length-1].canRead());
              Long idFile = new Long(DIR.listFiles()[DIR.listFiles().length-1].getName().split("=")[1].split("\\.")[0]);
              String checkinDate = null;
              String checkoutDate = null;
              try
              {
                FileReader fr = new FileReader(DIR.listFiles()[DIR.listFiles().length-1]);
                BufferedReader reader = new BufferedReader(fr);
                int rooms = 0;
                String line = reader.readLine();
                while (line != null)
                {
                  if (line.split("=")[0].equals("checkinDate"))
                    checkinDate = line.split("=")[1];
                  if (line.split("=")[0].equals("checkoutDate"))
                    checkoutDate = line.split("=")[1];
                  if (line.split("=")[0].equals("rooms"))
                    rooms = Integer.parseInt(line.split("=")[1]);
                  line = reader.readLine();
                }
                reader.close();
                writeCheckInFile(rooms, idFile, checkinDate, checkoutDate);
              }
              catch (Exception e){}
              finally
              {
                DIR.listFiles()[DIR.listFiles().length-1].delete();
              }
            }
          }
        }
      }
    }.start();
 }

  private void writeCheckInFile(int rooms, Long idFile, String checkinDate, String checkoutDate)
  {
    final String FILENAME = "c:\\RSCTomcatGate\\responds\\chAnswerFile=" + idFile + ".txt";
    Hashtable freeRooms = doHashTableContingent(checkinDate, checkoutDate);
    List keys = new ArrayList(freeRooms.keySet());
    BufferedWriter bw = null;
    FileWriter fw = null;
    try
    {
      fw = new FileWriter(FILENAME);
      bw = new BufferedWriter(fw);
      bw.write("checkinDate=" + checkinDate + "\n");
      bw.write("checkoutDate=" + checkoutDate + "\n");
      bw.write("rooms=" + rooms + "\n");
      for(int i = 0; i < keys.size(); i++)
      {
        if( Integer.parseInt(String.valueOf(freeRooms.get(keys.get(i).toString()))) < 999)
        bw.write(keys.get(i) + "=" + freeRooms.get(keys.get(i).toString())+ ";" + getPriceRoom(new DateRepresentation(checkinDate), new DateRepresentation(checkoutDate), keys.get(i).toString()) + "\n");
      }
    }
    catch (Exception e){}
    finally
    {
      try
      {
        if (bw != null)
            bw.close();
        if (fw != null)
            fw.close();
      }
      catch (Exception ex){}
    }
  }

  private double reverseSearchNonZeroCoast(int j, Table roomPrices)
  {
    double txt = 0;
    double startPrice = 0;
    for (int z = j; startPrice == 0.00 ; z--)
    {
      if (( (RoomPrice) roomPrices.at(z)).price > 0.00)
      {
        startPrice = ( (RoomPrice) roomPrices.at(z)).price;
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(2);
        txt = startPrice;
      }
    }
    return txt;
  }

  private double getPriceRoom(DateRepresentation startDate, DateRepresentation endDate, String typeRoom)
  {
    double txt = 0;
    int differentDays = endDate.calculateDifference(startDate);
    Table tbRoomTypes = Data.db.getTable(Consts.tbRoomTypes);
    for(int i = 0; i < tbRoomTypes.size(); i++)
    {
      RoomType elRoomType = (RoomType)tbRoomTypes.at(i);
      double startPrice = 0;
      double endPrice = 0;
      double interimPrice = 0;
      if(typeRoom.equals(elRoomType.getValue(0, 1).toString()))
      {
        Table roomPrices = tbRoomTypes.at(i).getSubtable(0);
        for (int j = 0; j < roomPrices.size(); j++)
        {
          RoomPrice rp = (RoomPrice) roomPrices.at(j);
          if (rp.month == startDate.getMonth() && rp.day == startDate.getDay() && rp.year == startDate.getYear())
          {
            if ( ( (RoomPrice) roomPrices.at(j)).price == 0.00)
              txt = reverseSearchNonZeroCoast(j, roomPrices);
            else
            {
              if (roomPrices.at(j + differentDays) == null)
                endPrice = startPrice;
              startPrice = ( (RoomPrice) roomPrices.at(j)).price;
              NumberFormat formatter = NumberFormat.getInstance();
              formatter.setMaximumFractionDigits(3);
              formatter.setMinimumFractionDigits(2);
              txt = startPrice;
              interimPrice = startPrice;
              for (int y = 1; y < differentDays; y++)
                interimPrice += ( (RoomPrice) roomPrices.at(j + y)).price;
              txt = Round.round(interimPrice / differentDays, 3);
              break;
            }
          }
          if (j + 1 == roomPrices.size())
           txt = reverseSearchNonZeroCoast(j, roomPrices);
        }
      }
    }
    return txt;
  }

}
package element;

import table.TableElement;
import table.User;
import utils.MyObjectOutputStream;
import table.Table;
import utils.DateRepresentation;
import java.io.*;
import utils.Consts;
import data.Data;

public class OnlineBooking implements TableElement
{
  protected TableElement roomType, elGuest;
  protected long code, bookingNo;
  protected char[] note, idTransaction;
  protected int checkinDay, checkinMonth, checkinYear, checkoutDay, checkoutMonth, checkoutYear, amountGuests;
  protected double sum, price;
  protected boolean babyBed, dogSmall, dogBig, payed, accepted;
  protected char[] log = new char[0];

  //new instance
  public OnlineBooking(long code, long guestCode, String checkinDate, String checkoutDate, long roomTypeCode, boolean babyBed, boolean dogSmall, boolean dogBig,
                       String note, int amountGuests, boolean payed, double price, String idTransaction)
  {
    this.code = code;
    this.elGuest = Data.db.getTable(Consts.tbCustomers).findElement(guestCode);
    this.note = note.toCharArray();
    this.roomType = Data.db.getTable(Consts.tbRoomTypes).findElement(roomTypeCode);
    this.idTransaction = idTransaction.toCharArray();
    this.price = price;
    DateRepresentation indate = new DateRepresentation(checkinDate);
    DateRepresentation outdate = new DateRepresentation(checkoutDate);
    this.checkinDay = indate.getDay();
    this.checkinMonth = indate.getMonth();
    this.checkinYear = indate.getYear();
    this.checkoutDay = outdate.getDay();
    this.checkoutMonth = outdate.getMonth();
    this.checkoutYear = outdate.getYear();
    this.babyBed = babyBed;
    this.dogSmall = dogSmall;
    this.dogBig = dogBig;
    this.payed = payed;
    this.amountGuests = amountGuests;
  }

  //read
  public OnlineBooking(long code, long guestCode, int checkinDay, int checkinMonth, int checkinYear, int checkoutDay, int checkoutMonth, int checkoutYear, long roomTypeCode, boolean babyBed,
                       boolean dogSmall, boolean dogBig, String note, int amountGuests, boolean payed, double price, String idTransaction, boolean accepted)
  {
    this.code = code;
    this.elGuest = Data.db.getTable(Consts.tbCustomers).findElement(guestCode);
    this.note = note.toCharArray();
    this.roomType = Data.db.getTable(Consts.tbRoomTypes).findElement(roomTypeCode);
    this.idTransaction = idTransaction.toCharArray();
    this.price = price;
    this.checkinDay = checkinDay;
    this.checkinMonth = checkinMonth;
    this.checkinYear = checkinYear;
    this.checkoutDay = checkoutDay;
    this.checkoutMonth = checkoutMonth;
    this.checkoutYear = checkoutYear;
    this.babyBed = babyBed;
    this.dogSmall = dogSmall;
    this.dogBig = dogBig;
    this.payed = payed;
    this.amountGuests = amountGuests;
    this.accepted = accepted;
    this.log = log;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return new Long(this.code);
      case 1: return this.elGuest.getValue(1, 1);
      case 2: return this.elGuest.getValue(2, 1);
      case 3: return new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear).toString();
      case 4: return new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).toString();
      case 5: return this.roomType.getValue(0, 1);
      case 6: return new Boolean(this.babyBed);
      case 7: return new Boolean(this.dogSmall);
      case 8: return new Boolean(this.dogBig);
      case 9: return this.elGuest.getValue(8, 1);
      case 10: return this.elGuest.getValue(7, 1);
      case 11: return this.elGuest.getValue(6, 1);
      case 12: return this.elGuest.getValue(5, 1);
      case 13: return this.elGuest.getValue(10, 1);
      case 14: return this.elGuest.getValue(9, 1);
      case 15: return this.elGuest.getValue(3, 1);
      case 16: return new String(this.note);
      case 17: return new Integer(this.amountGuests);
      case 18: return new Boolean(this.payed);
      case 19:
        double sum = 0;
        if (this.babyBed)
          sum += 5;
        if (this.dogSmall)
          sum += 5;
        if (this.dogBig)
          sum += 10;
        sum += this.price;
        return new Double(sum);
      case 20: return new String(this.idTransaction);
      case 21: return new Double(new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).calculateDifference(new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear)) * ((Double)this.getValue(19, 1)).doubleValue());
      case 22: return new Integer(new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).calculateDifference(new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear)));
      case 23: return new Boolean(this.accepted);
      case 24: return new Long(this.bookingNo);
      case 25: return new String(this.log);
      default: return new Long(this.code);
    }
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return this.getValue(column, howToRead);
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    return this.getValue(column, howToRead).equals(element.getValue(column, howToRead));
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date)
  {
    return "";
  }

  public String toString(int howToRead)
  {
    String s = "";
    for(int i = 0; i < 26; i++)
      s += this.getValue(i, howToRead) + "\t";
    return s;
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.code);
      out.writeLong(this.elGuest.getCode().longValue());
      out.writeInt(this.checkinDay);
      out.writeInt(this.checkinMonth);
      out.writeInt(this.checkinYear);
      out.writeInt(this.checkoutDay);
      out.writeInt(this.checkoutMonth);
      out.writeInt(this.checkoutYear);
      out.writeLong(this.roomType.getCode().longValue());
      out.writeBoolean(this.babyBed);
      out.writeBoolean(this.dogSmall);
      out.writeBoolean(this.dogBig);
      out.writeUTF(new String(this.note));
      out.writeInt(this.amountGuests);
      out.writeBoolean(this.payed);
      out.writeDouble(this.price);
      out.writeUTF(new String(this.idTransaction));
      out.writeBoolean(this.accepted);
      out.writeLong(this.bookingNo);
      out.writeUTF(new String(this.log));
  }

  public TableElement duplicate()
  {
    OnlineBooking ob = new OnlineBooking(this.code, this.elGuest.getCode().longValue(), this.checkinDay, this.checkinMonth, this.checkinYear, this.checkoutDay, this.checkoutMonth,
                                         this.checkoutYear, this.roomType.getCode().longValue(), this.babyBed, this.dogSmall, this.dogBig, new String(this.note), this.amountGuests,
                                         this.payed, this.price, new String(this.idTransaction), this.accepted);
    return ob;
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
    if(column == 24)
    {
      if(this.bookingNo == 0) return null;
      return Data.db.getTable(Consts.tbStay);
    }
    return null;
  }

  public long getReferenceCode(int column, int howToRead, User user, String language)
  {
    if(column == 24)
      return this.bookingNo;
    return 0L;
  }
}
package element;

import data.Data;
import table.*;
import utils.*;

import java.util.Random;

public class Stay implements TableElement
{
  private long code;
  protected int checkinDay, checkinMonth, checkinYear;
  protected int checkoutDay, checkoutMonth, checkoutYear;
  protected int optionDay, optionMonth, optionYear;
  protected boolean confirmed;
  protected TableElement room;
  protected TableElement payer, payer2, payer3;
  protected TableElement guest, guest2, guest3;
  protected int guestAmount;
  protected char[] receptionist = new char[0];
  protected boolean breakfast;
  protected int breakfastFromDay, breakfastFromMonth, breakfastFromYear;
  protected int breakfastTillDay, breakfastTillMonth, breakfastTillYear;
  protected char[] note;
  protected boolean checkedIn, keyReturned, checkedOut;
  protected long invoiceNo, invoiceNo2, invoiceNo3;
  protected double price;
  protected TableElement booking, payment;
  protected boolean payed;
  protected boolean combine;
  protected long onlineBookingNo;
  protected char[] color = new char[0];
  protected char[] log = new char[0];
  protected char[] userUUID = new char[0];
  protected char[] accessCode = new char[0];

  //new instance
  public Stay(String checkinDate, String checkoutDate, long roomCode, long payerCode, long guestCode, int guestAmount, boolean breakfast, String note, double price, long bookingCode, long paymentCode, String color)
  {
    DateRepresentation indate = new DateRepresentation(checkinDate);
    DateRepresentation outdate = new DateRepresentation(checkoutDate);
    this.checkinDay = indate.getDay();
    this.checkinMonth = indate.getMonth();
    this.checkinYear = indate.getYear();
    this.checkoutDay = outdate.getDay();
    this.checkoutMonth = outdate.getMonth();
    this.checkoutYear = outdate.getYear();
    this.room = Data.db.getTable(Consts.tbRooms).findElement(roomCode);
    this.payer = Data.db.getTable(Consts.tbCustomers).findElement(payerCode);
    this.guest = Data.db.getTable(Consts.tbCustomers).findElement(guestCode);
    this.guestAmount = guestAmount;
    this.breakfast = breakfast;
    if(this.breakfast)
    {
      indate.add(DateRepresentation.DATE, 1);
      this.breakfastFromDay = indate.getDay();
      this.breakfastFromMonth = indate.getMonth();
      this.breakfastFromYear = indate.getYear();
      this.breakfastTillDay = this.checkoutDay;
      this.breakfastTillMonth = this.checkoutMonth;
      this.breakfastTillYear = this.checkoutYear;
    }
    this.note = note.toCharArray();
    this.price = price;
    this.booking = Data.db.getTable(Consts.tbBooking).findElement(bookingCode);
    this.payment = Data.db.getTable(Consts.tbPayment).findElement(paymentCode);
    this.color = color.toCharArray();
  }

  //read
  public Stay(long code, int checkinDay, int checkinMonth, int checkinYear, int checkoutDay, int checkoutMonth, int checkoutYear, int optionDay, int optionMonth, int optionYear, boolean confirmed,
              long roomCode, long payerCode, long guestCode, int guestAmount, String receptionist, boolean breakfast, int breakfastFromDay, int breakfastFromMonth, int breakfastFromYear,
              int breakfastTillDay, int breakfastTillMonth, int breakfastTillYear, String note, boolean checkedIn, boolean keyReturned, boolean checkedOut, long invoiceNo, double price,
              long bookingCode, long paymentCode, boolean payed)
  {
    this.code = code;
    this.checkinDay = checkinDay;
    this.checkinMonth = checkinMonth;
    this.checkinYear = checkinYear;
    this.checkoutDay = checkoutDay;
    this.checkoutMonth = checkoutMonth;
    this.checkoutYear = checkoutYear;
    this.optionDay = optionDay;
    this.optionMonth = optionMonth;
    this.optionYear = optionYear;
    this.confirmed = confirmed;
    this.room = Data.db.getTable(Consts.tbRooms).findElement(roomCode);
    this.payer = Data.db.getTable(Consts.tbCustomers).findElement(payerCode);
    this.guest = Data.db.getTable(Consts.tbCustomers).findElement(guestCode);
    this.guestAmount = guestAmount;
    this.receptionist = receptionist.toCharArray();
    this.breakfast = breakfast;
    this.breakfastFromDay = breakfastFromDay;
    this.breakfastFromMonth = breakfastFromMonth;
    this.breakfastFromYear = breakfastFromYear;
    this.breakfastTillDay = breakfastTillDay;
    this.breakfastTillMonth = breakfastTillMonth;
    this.breakfastTillYear = breakfastTillYear;
    this.note = note.toCharArray();
    this.checkedIn = checkedIn;
    this.keyReturned = keyReturned;
    this.checkedOut = checkedOut;
    this.invoiceNo = invoiceNo;
    this.price = price;
    this.booking = Data.db.getTable(Consts.tbBooking).findElement(bookingCode);
    this.payment = Data.db.getTable(Consts.tbPayment).findElement(paymentCode);
    this.payed = payed;
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear).toString();
      case 1: return new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).toString();
      case 2: return new DateRepresentation(this.optionDay, this.optionMonth, this.optionYear).toString();
      case 3: return new Boolean(this.confirmed);
      case 4: return this.room.getValue(0, 1);
      case 5: return this.room.getValue(1, 1);
      case 6: return this.room.getValue(2, 1);
      case 7: return this.payer.getValue(1, 1);
      case 8: return this.payer.getValue(2, 1);
      case 9: return this.payer.getValue(3, 1);
      case 10: return this.guest.getValue(1, 1);
      case 11: return this.guest.getValue(2, 1);
      case 12: return this.guest.getValue(3, 1);
      case 13: return new Integer(this.guestAmount);
      case 14: return new String(this.receptionist);
      case 15: return new Boolean(this.breakfast);
      case 16: return new DateRepresentation(this.breakfastFromDay, this.breakfastFromMonth, this.breakfastFromYear).toString();
      case 17: return new DateRepresentation(this.breakfastTillDay, this.breakfastTillMonth, this.breakfastTillYear).toString();
      case 18: return new String(this.note);
      case 19: return new Boolean(this.checkedIn);
      case 20: return new Boolean(this.keyReturned);
      case 21: return new Boolean(this.checkedOut);
      case 22: return new Long(this.invoiceNo);
      case 23: return new Double(this.price);
      case 24: return this.booking.getValue(0, 1);
      case 25:
        if(this.payment == null)
          return "";
        return this.payment.getValue(0, 1);
      case 26: return new Boolean(this.payed);
      case 27: if(this.payer2 == null) return "";
               if(this.payer2.getValue(2, 1).toString().length() > 0)
                 return this.payer2.getValue(2, 1);
               return this.payer2.getValue(0, 1).toString() + " " + this.payer2.getValue(1, 1).toString();
      case 28: if(this.guest2 == null) return "";
               if(this.guest2.getValue(2, 1).toString().length() > 0)
                 return this.guest2.getValue(2, 1);
               return this.guest2.getValue(0, 1).toString() + " " + this.guest2.getValue(1, 1).toString();
      case 29: if(this.payer3 == null) return "";
               if(this.payer3.getValue(2, 1).toString().length() > 0)
                 return this.payer3.getValue(2, 1);
               return this.payer3.getValue(0, 1).toString() + " " + this.payer3.getValue(1, 1).toString();
      case 30: if(this.guest3 == null) return "";
               if(this.guest3.getValue(2, 1).toString().length() > 0)
                 return this.guest3.getValue(2, 1);
               return this.guest3.getValue(0, 1).toString() + " " + this.guest3.getValue(1, 1).toString();
      case 31: return new Boolean(this.combine);
      case 32: return this.payer.getValue(5, 1);
      case 33: return this.payer.getValue(6, 1);
      case 34: return this.payer.getValue(7, 1);
      case 35: return this.payer.getValue(8, 1);
      case 36: return new Long(this.invoiceNo2);
      case 37: return new Long(this.invoiceNo3);
      case 38: return this.payer.getValue(11, 1);
      case 39: return new Integer(new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).calculateDifference(new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear)));
      case 40: return new Double((double)new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear).calculateDifference(new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear)) * this.price);
      case 41:
        return new Long(this.code);
      case 42: return new Long(this.onlineBookingNo);
      case 43: return new String(this.color);
      case 44: return new String(this.log);
      case 45:
        return new String(this.userUUID);
      case 46:
        return new String(this.accessCode);
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

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    if (howToRead == 1) {
      String currentLog = Consts.logTemplate(this.log, date, currentUser, "");
      switch (column) {
        case 0:
          DateRepresentation checkinDate = new DateRepresentation(value.toString());
          DateRepresentation checkoutDate = new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear);
          boolean checkoutDateChanged = false;
          if (checkinDate.compareTo(checkoutDate) >= 0) {
            checkoutDate = new DateRepresentation(checkinDate.getDay(), checkinDate.getMonth(), checkinDate.getYear());
            checkoutDate.add(DateRepresentation.DATE, 1);
            checkoutDateChanged = true;
          }
          if (RoomExample.isBusy(this, this.room, checkinDate, checkoutDate))
            throw new MyException(Consts.ermsBusy(language));
          this.checkinDay = checkinDate.getDay();
          this.checkinMonth = checkinDate.getMonth();
          this.checkinYear = checkinDate.getYear();
          if (checkoutDateChanged) {
            this.checkoutDay = checkoutDate.getDay();
            this.checkoutMonth = checkoutDate.getMonth();
            this.checkoutYear = checkoutDate.getYear();
          }
          DateRepresentation optionDate = new DateRepresentation(this.optionDay, this.optionMonth, this.optionYear);
          if (optionDate.toString().length() > 0 && optionDate.compareTo(checkinDate) > 0) {
            this.optionDay = this.checkinDay;
            this.optionMonth = this.checkinMonth;
            this.optionYear = this.checkinYear;
          }
          if (this.breakfast) {
            checkinDate.add(DateRepresentation.DATE, 1);
            this.breakfastFromDay = checkinDate.getDay();
            this.breakfastFromMonth = checkinDate.getMonth();
            this.breakfastFromYear = checkinDate.getYear();
            this.breakfastTillDay = this.checkoutDay;
            this.breakfastTillMonth = this.checkoutMonth;
            this.breakfastTillYear = this.checkoutYear;
          }
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + checkinDate.toString() + "]\n").toCharArray();
          break;
        case 1:
          checkoutDate = new DateRepresentation(value.toString());
          checkinDate = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
          boolean checkinDateChanged = false;
          if (checkinDate.compareTo(checkoutDate) >= 0) {
            checkinDate = new DateRepresentation(checkoutDate.getDay(), checkoutDate.getMonth(), checkoutDate.getYear());
            checkinDate.add(DateRepresentation.DATE, -1);
            checkinDateChanged = true;
          }
          if (RoomExample.isBusy(this, this.room, checkinDate, checkoutDate))
            throw new MyException(Consts.ermsBusy(language));
          this.checkoutDay = checkoutDate.getDay();
          this.checkoutMonth = checkoutDate.getMonth();
          this.checkoutYear = checkoutDate.getYear();
          if (checkinDateChanged) {
            this.checkinDay = checkinDate.getDay();
            this.checkinMonth = checkinDate.getMonth();
            this.checkinYear = checkinDate.getYear();
            optionDate = new DateRepresentation(this.optionDay, this.optionMonth, this.optionYear);
            if (optionDate.toString().length() > 0 && optionDate.compareTo(checkinDate) > 0) {
              this.optionDay = this.checkinDay;
              this.optionMonth = this.checkinMonth;
              this.optionYear = this.checkinYear;
            }
          }
          if (this.breakfast) {
            checkinDate.add(DateRepresentation.DATE, 1);
            this.breakfastFromDay = checkinDate.getDay();
            this.breakfastFromMonth = checkinDate.getMonth();
            this.breakfastFromYear = checkinDate.getYear();
            this.breakfastTillDay = this.checkoutDay;
            this.breakfastTillMonth = this.checkoutMonth;
            this.breakfastTillYear = this.checkoutYear;
          }
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + checkoutDate.toString() + "]\n").toCharArray();
          break;
        case 2:
          optionDate = new DateRepresentation(value.toString());
          checkinDate = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
          if (checkinDate.compareTo(optionDate) < 0)
            throw new MyException(Consts.ermsInvDate(language));
          this.optionDay = optionDate.getDay();
          this.optionMonth = optionDate.getMonth();
          this.optionYear = optionDate.getYear();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + optionDate.toString() + "]\n").toCharArray();
          break;
        case 3:
          this.confirmed = new Boolean(value.toString()).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.confirmed + "]\n").toCharArray();
          break;
        case 4:
          checkinDate = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
          checkoutDate = new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear);
          TableElement newRoom = Data.db.getTable(Consts.tbRooms).findElement(((Long) value).longValue());
          if (RoomExample.isBusy(this, newRoom, checkinDate, checkoutDate))
            throw new MyException(Consts.ermsBusy(language));
          this.room = newRoom;
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + checkinDate.toString() + "]\n").toCharArray();
          break;
        case 8:
        case 9:
          if (((Long) value).longValue() == 0) return "Wrong customer";
          if (Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue()) == null)
            return "Wrong customer";
          this.payer = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 11:
        case 12:
          if (((Long) value).longValue() == 0) return "Wrong customer";
          if (Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue()) == null)
            return "Wrong customer";
          this.guest = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 13:
          this.guestAmount = ((Integer) value).intValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.guestAmount + "]\n").toCharArray();
          break;
        case 14:
          this.receptionist = value.toString().toCharArray();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + new String(this.receptionist) + "]\n").toCharArray();
          break;
        case 15:
          boolean br = ((Boolean) value).booleanValue();
          this.breakfast = br;
          if (br) {
            checkinDate = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
            checkinDate.add(DateRepresentation.DATE, 1);
            this.breakfastFromDay = checkinDate.getDay();
            this.breakfastFromMonth = checkinDate.getMonth();
            this.breakfastFromYear = checkinDate.getYear();
            this.breakfastTillDay = this.checkoutDay;
            this.breakfastTillMonth = this.checkoutMonth;
            this.breakfastTillYear = this.checkoutYear;
          } else {
            this.breakfastFromDay = 0;
            this.breakfastFromMonth = 0;
            this.breakfastFromYear = 0;
            this.breakfastTillDay = 0;
            this.breakfastTillMonth = 0;
            this.breakfastTillYear = 0;
          }
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + br + "]\n").toCharArray();
          break;
        case 16:
          DateRepresentation breakfastFromDate = new DateRepresentation(value.toString());
          checkinDate = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
          checkoutDate = new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear);
          if (breakfastFromDate.compareTo(checkinDate) <= 0 || breakfastFromDate.compareTo(checkoutDate) > 0)
            throw new MyException(Consts.ermsInvDate(language));
          this.breakfastFromDay = breakfastFromDate.getDay();
          this.breakfastFromMonth = breakfastFromDate.getMonth();
          this.breakfastFromYear = breakfastFromDate.getYear();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + breakfastFromDate.toString() + "]\n").toCharArray();
          break;
        case 17:
          DateRepresentation breakfastTillDate = new DateRepresentation(value.toString());
          breakfastFromDate = new DateRepresentation(this.breakfastFromDay, this.breakfastFromMonth, this.breakfastFromYear);
          if (breakfastTillDate.compareTo(breakfastFromDate) < 0)
            throw new MyException(Consts.ermsInvDate(language));
          checkoutDate = new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear);
          if (breakfastTillDate.compareTo(checkoutDate) > 0)
            throw new MyException(Consts.ermsInvDate(language));
          this.breakfastTillDay = breakfastTillDate.getDay();
          this.breakfastTillMonth = breakfastTillDate.getMonth();
          this.breakfastTillYear = breakfastTillDate.getYear();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + breakfastTillDate.toString() + "]\n").toCharArray();
          break;
        case 18:
          this.note = value.toString().toCharArray();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + new String(this.note) + "]\n").toCharArray();
          break;
        case 19:
          this.checkedIn = ((Boolean) value).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.checkedIn + "]\n").toCharArray();
          break;
        case 20:
          this.keyReturned = ((Boolean) value).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.keyReturned + "]\n").toCharArray();
          break;
        case 21:
          this.checkedOut = ((Boolean) value).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.keyReturned + "]\n").toCharArray();
          break;
        case 22:
          this.payment = Data.db.getTable(Consts.tbPayment).findElement(((Long) value).longValue());
          Firm h = (Firm) Data.db.getTable(Consts.tbHotels).at(0);
          Invoice inv = new Invoice(new DateRepresentation(false), this, h.breakfastPrice, h.mehrWSt1, h.mwst2);
          Data.db.getTable(Consts.tbInvoices).addTableElement(inv, "en", currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 23:
          this.price = ((Double) value).doubleValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.price + "]\n").toCharArray();
          break;
        case 24:
          this.booking = Data.db.getTable(Consts.tbBooking).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 25:
          this.payment = Data.db.getTable(Consts.tbPayment).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 26:
          this.payed = ((Boolean) value).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.payed + "]\n").toCharArray();
          break;
        case 27:
          this.payer2 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.guest2 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 28:
          this.guest2 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 29:
          this.payer3 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.guest3 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 30:
          this.guest3 = Data.db.getTable(Consts.tbCustomers).findElement(((Long) value).longValue());
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 31:
          this.combine = ((Boolean) value).booleanValue();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 32:
          this.payer.setValue(5, value, language, 1, currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 33:
          this.payer.setValue(6, value, language, 1, currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 34:
          this.payer.setValue(7, value, language, 1, currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 35:
          this.payer.setValue(8, value, language, 1, currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 38:
          this.payer.setValue(11, value, language, 1, currentUser, date);
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 43:
          this.color = value.toString().toCharArray();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 45:
          this.userUUID = value.toString().toCharArray();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
        case 46:
          this.accessCode = value.toString().toCharArray();
          this.log = (currentLog + Consts.clStay("de")[column] + " bearbeitet [" + this.getValue(column, 1).toString() + "]\n").toCharArray();
          break;
      }
    }
    return "";
  }

  public String toString(int howToRead)
  {
    String s = "";
    for(int i = 0; i < 47; i++)
      s += this.getValue(i, howToRead) + "\t";
    return s;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((Stay)obj).code;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public Long getCode()
  {
    return new Long(this.code);
  }

  public boolean isCellEditable(int column, int howToRead)
  {
    if (howToRead == 1) {
      switch (column) {
        case 0:
          if (this.checkedIn) return false;
          return true;
        case 1:
          if (this.checkedOut) return false;
          return true;
        case 2:
          if (this.checkedIn) return false;
          return true;
        case 3:
          if (this.checkedIn) return false;
          return true;
        case 4:
          if (this.checkedIn) return false;
          return true;
        case 5:
        case 6:
        case 7:
          return false;
        case 8:
        case 9:
          if (this.invoiceNo > 0) return false;
          return true;
        case 10:
          return false;
        case 11:
        case 12:
          if (this.invoiceNo > 0) return false;
          return true;
        case 13:
          if (this.checkedIn) return false;
          return true;
        case 14:
          return true;
        case 15:
        case 16:
        case 17:
        case 18:
          //if(this.invoiceNo > 0) return false;
          return true;
        case 19:
          if ((this.checkedOut || this.keyReturned) && this.checkedIn) return false;
          //if(this.invoiceNo > 0 && this.checkedIn) return false;
          return true;
        case 20:
          if (this.invoiceNo > 0 && this.keyReturned) return false;
          if (!this.checkedIn) return false;
          return true;
        case 21:
          if (this.invoiceNo > 0 && this.checkedOut) return false;
          if (!this.checkedIn) return false;
          return true;
        case 22:
          if (this.invoiceNo > 0) return false;
          return true;
        case 23:
          if (this.invoiceNo > 0) return false;
          return true;
        case 24:
          if (this.invoiceNo > 0) return false;
          return true;
        case 25:
          return true;
        case 26:
          if (this.invoiceNo > 0 && this.payed) return false;
          return true;
        case 27:
        case 28:
        case 29:
        case 30:
        case 31:
          if (this.invoiceNo > 0) return false;
          return true;
        case 32:
        case 33:
        case 34:
        case 35:
        case 38:
        case 43:
        case 45:
        case 46:
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.code);
      out.writeInt(this.checkinDay);
      out.writeInt(this.checkinMonth);
      out.writeInt(this.checkinYear);
      out.writeInt(this.checkoutDay);
      out.writeInt(this.checkoutMonth);
      out.writeInt(this.checkoutYear);
      out.writeInt(this.optionDay);
      out.writeInt(this.optionMonth);
      out.writeInt(this.optionYear);
      out.writeBoolean(this.confirmed);
      out.writeLong(this.room.getCode().longValue());
      if(this.payer == null)
      {
        this.payer = Data.db.getTable(Consts.tbCustomers).at(0);
        System.out.println(this.getValue(0, 1).toString() + " " + this.room.getCode());
      }
      out.writeLong(this.payer.getCode().longValue());
      if(this.guest == null)
      {
        this.guest = Data.db.getTable(Consts.tbCustomers).at(0);
        System.out.println(this.getValue(0, 1).toString() + " " + this.room.getCode());
      }
      out.writeLong(this.guest.getCode().longValue());
      out.writeInt(this.guestAmount);
      out.writeUTF(new String(this.receptionist));
      out.writeBoolean(this.breakfast);
      out.writeInt(this.breakfastFromDay);
      out.writeInt(this.breakfastFromMonth);
      out.writeInt(this.breakfastFromYear);
      out.writeInt(this.breakfastTillDay);
      out.writeInt(this.breakfastTillMonth);
      out.writeInt(this.breakfastTillYear);
      out.writeUTF(new String(this.note));
      out.writeBoolean(this.checkedIn);
      out.writeBoolean(this.keyReturned);
      out.writeBoolean(this.checkedOut);
      out.writeLong(this.invoiceNo);
      out.writeDouble(this.price);
      out.writeLong(this.booking.getCode().longValue());
      if(this.payment == null)
        out.writeBoolean(false);
      else
      {
        out.writeBoolean(true);
        out.writeLong(this.payment.getCode().longValue());
      }
      out.writeBoolean(this.payed);
      if(this.payer2 == null)
        out.writeBoolean(false);
      else
      {
        out.writeBoolean(true);
        out.writeLong(this.payer2.getCode().longValue());
      }
      if(this.guest2 == null)
        out.writeBoolean(false);
      else
      {
        out.writeBoolean(true);
        out.writeLong(this.guest2.getCode().longValue());
      }
      if(this.payer3 == null)
        out.writeBoolean(false);
      else
      {
        out.writeBoolean(true);
        out.writeLong(this.payer3.getCode().longValue());
      }
      if(this.guest3 == null)
        out.writeBoolean(false);
      else
      {
        out.writeBoolean(true);
        out.writeLong(this.guest3.getCode().longValue());
      }
      out.writeBoolean(this.combine);
      out.writeLong(this.invoiceNo2);
      out.writeLong(this.invoiceNo3);
      out.writeLong(this.onlineBookingNo);
      out.writeUTF(new String(this.color));
      out.writeUTF(new String(this.log));
      out.writeUTF(new String(this.userUUID));
      out.writeUTF(new String(this.accessCode));
  }

  public TableElement duplicate()
  {
    long pc = -1;
    if(this.payment != null)
      pc = this.payment.getCode().longValue();
    Stay st = new Stay(-1, this.checkinDay, this.checkinMonth, this.checkinYear, this.checkoutDay, this.checkoutMonth, this.checkoutYear, this.optionDay, this.optionMonth, this.optionYear,
            this.confirmed, this.room.getCode().longValue(), this.payer.getCode().longValue(), this.guest.getCode().longValue(), this.guestAmount, new String(this.receptionist), this.breakfast,
            this.breakfastFromDay, this.breakfastFromMonth, this.breakfastFromYear, this.breakfastTillDay, this.breakfastTillMonth, this.breakfastTillYear, new String(this.note), this.checkedIn,
            this.keyReturned, this.checkedOut, this.invoiceNo, this.price, this.booking.getCode().longValue(), pc, this.payed);
    st.payer2 = this.payer2;
    st.guest2 = this.guest2;
    st.payer3 = this.payer3;
    st.guest3 = this.guest3;
    st.combine = this.combine;
    st.color = this.color.clone();
    st.userUUID = this.userUUID.clone();
    st.accessCode = this.accessCode.clone();
//    st.invoiceNo2 = this.invoiceNo2;
//    st.invoiceNo3 = this.invoiceNo3;
    return st;
  }

  public Table getSubtable(int subtableIndex)
  {
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
    if(this.checkedIn || this.invoiceNo > 0) return false;
    return true;
  }

  public boolean isMovable(int howToRead)
  {
    return false;
  }

  public String wasAdd(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    if (howToRead == 1)
    {
      this.log = (Consts.logTemplate(this.log, date, currentUser, "") + "Buchungen eingetragen " + " [" + this.getValue(0, 1).toString() + ", " +
              this.getValue(1, 1).toString() + ", " + this.getValue(0, 4).toString() + ", " + this.getValue(8, 1).toString() + ", " +
              this.getValue(11, 1).toString() + ", " + this.guestAmount + ", " + this.breakfast + ", " + new String(this.note) + "; " + this.price + "; " +
              this.getValue(24, 1).toString() + ", " + this.getValue(25, 1).toString() + "; " + this.getValue(43, 1).toString() + "]\n").toCharArray();

      new Thread(() -> {
        try
        {
          Thread.sleep(1000);
          while (!Data.canBeRunning)
          {
            Thread.sleep(100);
          }
          System.out.println("Create user in Loxone");
          String newUserUUID = Loxone.createUser("" + this.getCode());
          Table tbStay = Data.db.getTable(Consts.tbStay);
          tbStay.setValueAt(newUserUUID, this.getCode(), 45, "en", "", date);
          System.out.println("Update userId in Stay");
          if (tbStay.lastOperationResult)
          {
            Data.version++;
            Command command = new Command9(Consts.tbStay, this.getCode(), 45, newUserUUID, "", date, Data.version);
            System.out.println("Send command to update");
            Data.commander.announce(command, "", "", "en", false, tbStay, null, null, Consts.tbStay);
          }
          System.out.println("Set Benutzer group");
          Loxone.setGroup(newUserUUID, "Benutzer_NFC" + this.room.getValue(0, 1).toString());
          System.out.println("Set valid date");
          DateRepresentation dateFrom = new DateRepresentation(this.checkinDay, this.checkinMonth, this.checkinYear);
          dateFrom.setTime(16, 0);
          DateRepresentation dateUntil = new DateRepresentation(this.checkoutDay, this.checkoutMonth, this.checkoutYear);
          dateUntil.setTime(12, 0);
          Loxone.setValidDate("" + this.code, newUserUUID, dateFrom, dateUntil);
          String newAccessCode = generateAccessCode();
          System.out.println("set access code to " + newAccessCode);
          Loxone.updateUserAccessCode(newUserUUID, newAccessCode);
          System.out.println("Send code by email");
          Table firms = Data.db.getTable(Consts.tbHotels);
          TableElement firm = firms.at(0);
          String email = firm.getValue(4, 1).toString();
          String smtp = firm.getValue(15, 1).toString();
          String smtpAuthorization = firm.getValue(16, 1).toString();
          String emailTo = payer.getValue(11, 1).toString();
          String text = "Your access code: " + newAccessCode;
          if (newAccessCode.length() == 8 && smtp.length() != 0 && smtpAuthorization.length() != 0 && email.length() != 0 && emailTo.length() != 0)
            Mail.sendMail(smtp, email, smtpAuthorization, email, emailTo, "New access code", text);

        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }).start();
    }


    return "";
  }

  private static String generateAccessCode()
  {
    Random rnd = new Random();
    int number = 10000000 + rnd.nextInt(89999999);
    return String.valueOf(number);
  }

  public String wasRemoved(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    if (howToRead == 1)
    {
      Data.db.getTable(Consts.tbDraftBooking).addTableElementSimple(this);
      this.log = "".toCharArray();
      if (this.userUUID.length > 0)
        new Thread(() -> {
          try
          {
            System.out.println("Delete user from Loxone");
            Loxone.deleteUser(String.valueOf(this.userUUID));
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }).start();
    }
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
    if(column == 42)
      return Data.db.getTable(Consts.tbOnlineBooking);
    return null;
  }

  public long getReferenceCode(int column, int howToRead, User user, String language)
  {
    if(column == 42)
      return onlineBookingNo;
    return -1;
  }

}
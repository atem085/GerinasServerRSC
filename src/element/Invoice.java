package element;

import table.TableElement;
import table.User;
import utils.MyObjectOutputStream;
import table.Table;
import utils.DateRepresentation;
import utils.Consts;
import data.Data;
import utils.Round;

public class Invoice implements TableElement
{
  protected long code;
  protected int day, month, year;
  protected TableElement customer;
  public TableElement booking;
  protected double breakfastPrice;
  protected double mwst1, mwst2;
  protected double accomodationTax = 5.0;
  protected char[] note;
  protected char[] lastRow;
  protected TableElement paymentMethod;
  protected Table services;

  public Invoice(String date, long customerCode, long roomCode, double breakfastPrice, double mwst1, double mwst2, String note, long payment)
  {
    DateRepresentation d = new DateRepresentation(date);
    this.day = d.getDay();
    this.month = d.getMonth();
    this.year = d.getYear();
    if(customerCode != -1)
      this.customer = Data.db.getTable(Consts.tbCustomers).findElement(customerCode);
    if(roomCode != -1)
    {
      Table t = Data.db.getTable(Consts.tbStay);
      DateRepresentation dt = new DateRepresentation(date);
      for (int i = t.size() - 1; i > -1; i--)
      {
        Stay st = (Stay) t.at(i);
        if(st.room.getCode().longValue() != roomCode)
          continue;
        if (st.invoiceNo > 0)
          continue;
        if (! (st.payer == customer || st.guest == customer))
          continue;
/*        DateRepresentation chd = new DateRepresentation(st.checkoutDay, st.checkoutMonth, st.checkoutYear);
        if (chd.after(dt))
          continue;*/
        this.booking = st;
        st.payment = Data.db.getTable(Consts.tbPayment).findElement(payment);
        break;
      }
    }
    else
      this.paymentMethod = Data.db.getTable(Consts.tbPayment).findElement(payment);
    if(this.booking != null && ((Stay)this.booking).breakfast)
    {
      if(((Stay)this.booking).price < 0)
        this.breakfastPrice = -breakfastPrice;
      else
        this.breakfastPrice = breakfastPrice;
    }
    this.mwst1 = mwst1;
    this.mwst2 = mwst2;
    this.note = note.toCharArray();
    this.lastRow = new char[0];
    this.services = new Table("services_of_invoice_" + d.toString() + "_" + customerCode + " ", new InvoicePositionExample(), 1, 0, this, null);
    if(this.getValue(4, 1).toString().length() > 0)
    {
      if(this.booking != null && !((Customer2)((Stay)this.booking).payer).travelAgency)
        this.accomodationTax = 0;
    }
  }

  public Invoice(DateRepresentation date, TableElement bookingE, double breakfastPrice, double mwst1, double mwst2)
  {
    this.day = date.getDay();
    this.month = date.getMonth();
    this.year = date.getYear();
    this.booking = bookingE;
    this.customer = ((Stay)bookingE).payer;
    if(((Stay)this.booking).breakfast)
    {
      if(((Stay)this.booking).price < 0)
        this.breakfastPrice = -breakfastPrice;
      else
        this.breakfastPrice = breakfastPrice;
    }
    this.mwst1 = mwst1;
    this.mwst2 = mwst2;
    this.note = "".toCharArray();
    this.lastRow = new char[0];
    long[] ec = new long[1];
    ec[0] = this.code;
    int[] si = new int[1];
    si[0] = 0;
    this.services = new Table("services_of_invoice_" + date.toString() + "_" + ((Stay)bookingE).payer.getCode().toString() + " ", new InvoicePositionExample(), 1, 0, this, null);
    if(this.getValue(4, 1).toString().length() > 0)
    {
      if(this.booking != null && !((Customer2)((Stay)this.booking).payer).travelAgency)
        this.accomodationTax = 0;
    }
  }

  public Invoice(long code, int day, int month, int year, long customerCode, long bookingCode, double breakfastPrice, double mwst1, double mwst2, double accomodationTax, String note, String lastRow)
  {
    this.code = code;
    this.day = day;
    this.month = month;
    this.year = year;
    if(customerCode != -1)
      this.customer = Data.db.getTable(Consts.tbCustomers).findElement(customerCode);
    if(bookingCode != -1)
      this.booking = Data.db.getTable(Consts.tbStay).findElement(bookingCode);
    this.breakfastPrice = breakfastPrice;
    this.mwst1 = mwst1;
    this.mwst2 = mwst2;
    this.accomodationTax = accomodationTax;
    this.note = note.toCharArray();
    this.lastRow = new char[0];
    this.services = new Table("services_of_invoice " + code, new InvoicePositionExample(), 1, 0, this, null);
  }

  public Object getValue(int column, int howToRead)
  {
    switch(column)
    {
      case 0: return new Long(this.code);
      case 1: return new DateRepresentation(this.day, this.month, this.year).toString();
      case 2: if(this.booking == null)
               return this.customer.getValue(1, 1);
              Stay st = (Stay)this.booking;
              if(st.invoiceNo == this.code)
                return st.payer.getValue(1, 1);
              if(st.invoiceNo2 == this.code)
                return st.payer2.getValue(1, 1);
              if(st.invoiceNo3 == this.code)
                return st.payer3.getValue(1, 1);
      case 3: if(this.booking == null)
                return this.customer.getValue(2, 1);
              st = (Stay)this.booking;
              if(st.invoiceNo == this.code)
                return st.payer.getValue(2, 1);
              if(st.invoiceNo2 == this.code)
                return st.payer2.getValue(2, 1);
              if(st.invoiceNo3 == this.code)
                return st.payer3.getValue(2, 1);
      case 4: if(this.booking == null)
                return this.customer.getValue(3, 1);
              st = (Stay)this.booking;
              if(st.invoiceNo == this.code)
                return st.payer.getValue(3, 1);
              if(st.invoiceNo2 == this.code)
                return st.payer2.getValue(3, 1);
              if(st.invoiceNo3 == this.code)
                return st.payer3.getValue(3, 1);
            case 5: if(this.booking == null)
                      return "";
                    Stay thisbooking = (Stay)this.booking;
                    String s = thisbooking.room.getCode().toString();
                    if(thisbooking.combine)
                    {
                      Table t = Data.db.getTable(Consts.tbStay);
                      int pos = t.findCode(thisbooking.getCode().longValue());
                      for(int i = pos + 1, j = 0; i < t.size()/* && j < 1000*/; i++, j++)
                      {
                        st = (Stay)t.at(i);
                        if(!st.combine) continue;
                        if(st.payer != thisbooking.payer) continue;
                        if(st.invoiceNo != thisbooking.invoiceNo) continue;
                        s += ";" + st.room.getCode().toString();
                      }
                      for(int i = pos - 1, j = 0; i > -1 && j < 1001; i--, j++)
                      {
                        st = (Stay)t.at(i);
                        if(!st.combine) continue;
                        if(st.payer != thisbooking.payer) continue;
                        if(st.invoiceNo != thisbooking.invoiceNo) continue;
                        s += ";" + st.room.getCode().toString();
                      }
                    }
                    return s;
            case 6: if(this.booking == null)
                      return new Double(0);
                    st = (Stay)this.booking;
                    if(st.invoiceNo2 == 0 && st.invoiceNo3 == 0)
                      return new Double((((Stay)this.booking).price - this.breakfastPrice * ((Stay)this.booking).guestAmount) / (1 + this.accomodationTax / 100) / (1 + this.mwst1 / 100));
                    else if(st.invoiceNo3 == 0)
                      return new Double((((Stay)this.booking).price - this.breakfastPrice * ((Stay)this.booking).guestAmount) / (1 + this.accomodationTax / 100) / (1 + this.mwst1 / 100) / 2);
                    else
                      return new Double((((Stay)this.booking).price - this.breakfastPrice * ((Stay)this.booking).guestAmount) / (1 + this.accomodationTax / 100) / (1 + this.mwst1 / 100) / 3);
            case 7: if(this.booking == null)
                      return new Double(0);
                    st = (Stay)this.booking;
                    if(st.invoiceNo2 == 0 && st.invoiceNo3 == 0)
                      return new Double(this.breakfastPrice * ((Stay)this.booking).guestAmount / (1 + this.mwst2 / 100));
                    if(st.invoiceNo3 == 0)
                      return new Double(this.breakfastPrice * ((Stay)this.booking).guestAmount / (1 + this.mwst2 / 100) / 2);
                    else
                      return new Double(this.breakfastPrice * ((Stay)this.booking).guestAmount / (1 + this.mwst2 / 100) / 3);
            case 8: if(this.booking == null)
                      return new Integer(0);
                    thisbooking = (Stay)this.booking;
                    DateRepresentation chout = new DateRepresentation(thisbooking.checkoutDay, thisbooking.checkoutMonth, thisbooking.checkoutYear);
                    DateRepresentation chin = new DateRepresentation(thisbooking.checkinDay, thisbooking.checkinMonth, thisbooking.checkinYear);
                    return new Integer(chout.calculateDifference(chin));
            case 9: if(this.booking == null)
                      return new Integer(0);
                    thisbooking = (Stay)this.booking;
                    DateRepresentation bout = new DateRepresentation(thisbooking.breakfastTillDay, thisbooking.breakfastTillMonth, thisbooking.breakfastTillYear);
                    DateRepresentation bin = new DateRepresentation(thisbooking.breakfastFromDay, thisbooking.breakfastFromMonth, thisbooking.breakfastFromYear);
                    return new Integer(bout.calculateDifference(bin) + 1);
            case 10: if(this.booking == null)
                       return new Double(0);
                     thisbooking = (Stay)this.booking;
                     double others = 0;
                     if(thisbooking.combine)
                     {
                       Table t = Data.db.getTable(Consts.tbStay);
                       int pos = t.findCode(thisbooking.getCode().longValue());
                       for(int i = pos + 1, j = 0; i < t.size()/* && j < 1000*/; i++, j++)
                       {
                         st = (Stay)t.at(i);
                         if(!st.combine) continue;
                         if(st.payer != thisbooking.payer) continue;
                         if(st.invoiceNo != thisbooking.invoiceNo) continue;
                         chout = new DateRepresentation(st.checkoutDay, st.checkoutMonth, st.checkoutYear);
                         chin = new DateRepresentation(st.checkinDay, st.checkinMonth, st.checkinYear);
                         others += (st.price - this.breakfastPrice * st.guestAmount) / (1 + this.accomodationTax / 100) / (1 + this.mwst1 / 100) * chout.calculateDifference(chin);
                       }
                       for(int i = pos - 1, j = 0; i > -1 && j < 1001; i--, j++)
                       {
                         st = (Stay)t.at(i);
                         if(!st.combine) continue;
                         if(st.payer != thisbooking.payer) continue;
                         if(st.invoiceNo != thisbooking.invoiceNo) continue;
                         chout = new DateRepresentation(st.checkoutDay, st.checkoutMonth, st.checkoutYear);
                         chin = new DateRepresentation(st.checkinDay, st.checkinMonth, st.checkinYear);
                         others += (st.price - this.breakfastPrice * st.guestAmount) / (1 + this.accomodationTax / 100) / (1 + this.mwst1 / 100) * chout.calculateDifference(chin);
                       }
                     }
                     return new Double(others + ((Double)this.getValue(6, 1)).doubleValue() * ((Integer)this.getValue(8, 1)).doubleValue());
            case 11: if(this.booking == null)
                      return new Double(0);
                    thisbooking = (Stay)this.booking;
                    others = 0;
                    if(thisbooking.combine)
                    {
                      Table t = Data.db.getTable(Consts.tbStay);
                      int pos = t.findCode(thisbooking.getCode().longValue());
                      for(int i = pos + 1, j = 0; i < t.size()/* && j < 1000*/; i++, j++)
                      {
                        st = (Stay)t.at(i);
                        if(!st.combine) continue;
                        if(st.payer != thisbooking.payer) continue;
                        if(st.invoiceNo != thisbooking.invoiceNo) continue;
                        bin = new DateRepresentation(st.breakfastFromDay, st.breakfastFromMonth, st.breakfastFromYear);
                        bout = new DateRepresentation(st.breakfastTillDay, st.breakfastTillMonth, st.breakfastTillYear);
                        others += this.breakfastPrice * st.guestAmount / (1 + this.mwst2 / 100) * (bout.calculateDifference(bin) + 1);
                      }
                      for(int i = pos - 1, j = 0; i > -1 && j < 1001; i--, j++)
                      {
                        st = (Stay)t.at(i);
                        if(!st.combine) continue;
                        if(st.payer != thisbooking.payer) continue;
                        if(st.invoiceNo != thisbooking.invoiceNo) continue;
                        bin = new DateRepresentation(st.breakfastFromDay, st.breakfastFromMonth, st.breakfastFromYear);
                        bout = new DateRepresentation(st.breakfastTillDay, st.breakfastTillMonth, st.breakfastTillYear);
                        others += this.breakfastPrice * st.guestAmount / (1 + this.mwst2 / 100) * (bout.calculateDifference(bin) + 1);
                      }
                    }
                     return new Double(others + ((Double)this.getValue(7, 1)).doubleValue() * ((Integer)this.getValue(9, 1)).doubleValue());
                   case 12: if(this.booking == null)
                              return new Double(0);
                            thisbooking = (Stay)this.booking;
                            if(thisbooking.combine)
                              return new Double(((Double)this.getValue(10, 1)).doubleValue() * this.accomodationTax / 100);
                            return new Double(((Double)this.getValue(10, 1)).doubleValue() * this.accomodationTax / 100);
      case 13: double sum = 0;
               for(int i = 0; i < this.services.size(); i++)
                 sum += ((Double)this.services.at(i).getValue(3, 1)).doubleValue();
               sum = sum / (1 + this.mwst2 / 100);
               return new Double(sum);
      case 14: if(this.booking == null)
                 return new Double(0);
               return new Double((((Double)this.getValue(10, 1)).doubleValue() + ((Double)this.getValue(12, 1)).doubleValue()) * this.mwst1 / 100);
      case 15: return new Double((((Double)this.getValue(13, 1)).doubleValue() + ((Double)this.getValue(11, 1)).doubleValue()) * this.mwst2 / 100);
      case 16: return new Double(((Double)this.getValue(10, 1)).doubleValue() + ((Double)this.getValue(11, 1)).doubleValue() + ((Double)this.getValue(12, 1)).doubleValue() + ((Double)this.getValue(13, 1)).doubleValue() + ((Double)this.getValue(14, 1)).doubleValue() + ((Double)this.getValue(15, 1)).doubleValue());
      case 17: return new String(this.note);
      case 18: return new String(this.lastRow);
      case 19: if(this.booking == null)
               {
                 if(this.paymentMethod == null)
                   return "";
                 return this.paymentMethod.getValue(0, 1);
               }
               return this.booking.getValue(25, 1);
      case 20: return new Double(((Double)this.getValue(10, 1)).doubleValue() * (1 + this.accomodationTax/100));
      case 21: if(this.booking == null) return new Integer(0);
        thisbooking = (Stay)this.booking;
        int othersA = 0;
        if(thisbooking.combine)
        {
          Table t = Data.db.getTable(Consts.tbStay);
          int pos = t.findCode(thisbooking.getCode().longValue());
          for(int i = pos + 1, j = 0; i < t.size()/* && j < 1000*/; i++, j++)
          {
            st = (Stay)t.at(i);
            if(!st.combine) continue;
            if(st.payer != thisbooking.payer) continue;
            if(st.invoiceNo != thisbooking.invoiceNo) continue;
            othersA += st.guestAmount;
          }
          for(int i = pos - 1, j = 0; i > -1 && j < 1001; i--, j++)
          {
            st = (Stay)t.at(i);
            if(!st.combine) continue;
            if(st.payer != thisbooking.payer) continue;
            if(st.invoiceNo != thisbooking.invoiceNo) continue;
            othersA += st.guestAmount;
          }
        }
         return new Integer(othersA + thisbooking.guestAmount);
      case 22: if(this.booking == null) return new Integer(0);
        thisbooking = (Stay)this.booking;
        othersA = 0;
        if(thisbooking.combine)
        {
          Table t = Data.db.getTable(Consts.tbStay);
          int pos = t.findCode(thisbooking.getCode().longValue());
          for(int i = pos + 1, j = 0; i < t.size()/* && j < 1000*/; i++, j++)
          {
            st = (Stay)t.at(i);
            if(!st.combine) continue;
            if(st.payer != thisbooking.payer) continue;
            if(st.invoiceNo != thisbooking.invoiceNo) continue;
            chout = new DateRepresentation(st.checkoutDay, st.checkoutMonth, st.checkoutYear);
            chin = new DateRepresentation(st.checkinDay, st.checkinMonth, st.checkinYear);
            othersA += st.guestAmount * chout.calculateDifference(chin);
          }
          for(int i = pos - 1, j = 0; i > -1 && j < 1001; i--, j++)
          {
            st = (Stay)t.at(i);
            if(!st.combine) continue;
            if(st.payer != thisbooking.payer) continue;
            if(st.invoiceNo != thisbooking.invoiceNo) continue;
            chout = new DateRepresentation(st.checkoutDay, st.checkoutMonth, st.checkoutYear);
            chin = new DateRepresentation(st.checkinDay, st.checkinMonth, st.checkinYear);
            othersA += st.guestAmount * chout.calculateDifference(chin);
          }
        }
         chout = new DateRepresentation(thisbooking.checkoutDay, thisbooking.checkoutMonth, thisbooking.checkoutYear);
         chin = new DateRepresentation(thisbooking.checkinDay, thisbooking.checkinMonth, thisbooking.checkinYear);
         return new Integer(othersA + thisbooking.guestAmount * chout.calculateDifference(chin));
      default: return new Long(this.code);
    }
  }

  public Object getValue(int column, int howToRead, User user, String language)
  {
    return this.getValue(column, howToRead);
  }

  public int compareTo(TableElement element, int column, boolean ascending, int howToRead)
  {
    if(ascending)
      return this.getValue(column, howToRead).toString().compareTo(element.getValue(column, howToRead).toString());
    return element.getValue(column, howToRead).toString().compareTo(this.getValue(column, howToRead).toString());
  }

  public boolean equals(TableElement element, int column, int howToRead)
  {
    if(this.compareTo(element, column, true, howToRead) == 0)
      return true;
    return false;
  }

  public String setValue(int column, Object value, String language, int howToRead, String currentUser, DateRepresentation date) throws Exception
  {
    switch(column)
    {
      case 1:
        DateRepresentation newDate = new DateRepresentation(value.toString());
        this.day = newDate.getDay();
        this.month = newDate.getMonth();
        this.year = newDate.getYear();
        break;
      case 14:
        this.mwst1 = (double) value;
        break;
      case 15:
        this.mwst2 = (double) value;
        break;
      case 17: this.note = value.toString().toCharArray();
               break;
      case 18: this.lastRow = value.toString().toCharArray();
               break;
      case 19:
        this.paymentMethod = Data.db.getTable(Consts.tbPayment).findElement(((Long)value).longValue());
        break;
    }
    return "";
  }

  public String toString(int howToRead)
  {
    String s = "";
    for(int i = 0; i < 23; i++)
      s += this.getValue(i, 1).toString();
    return s;
  }

  public boolean equals(Object obj)
  {
    try
    {
      return this.code == ((Invoice)obj).code;
    }
    catch(ClassCastException e)
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
    if(column == 14 || column == 15) return true;
    if(column == 17 || column == 18) return true;
    if(column == 19 && this.booking == null) return true;
    if (column == 1) return true;
    return false;
  }

  public void write(MyObjectOutputStream out, int howToRead) throws Exception
  {
      out.writeLong(this.code);
      out.writeInt(this.day);
      out.writeInt(this.month);
      out.writeInt(this.year);
      if(this.customer == null)
        out.writeLong(-1);
      else
        out.writeLong(this.customer.getCode().longValue());
      if(this.booking == null)
        out.writeLong(-1);
      else
        out.writeLong(this.booking.getCode().longValue());
      out.writeDouble(this.breakfastPrice);
      out.writeDouble(this.mwst1);
      out.writeDouble(this.mwst2);
      out.writeDouble(this.accomodationTax);
      out.writeUTF(new String(this.note));
      out.writeUTF(new String(this.lastRow));
      if(this.booking == null)
      {
        if(this.paymentMethod == null)
          out.writeBoolean(false);
        else
        {
          out.writeBoolean(true);
          out.writeLong(this.paymentMethod.getCode().longValue());
        }
      }
      this.services.write(out);
  }

  public TableElement duplicate()
  {
    long customerCode = -1;
    if(this.customer != null)
      customerCode = this.customer.getCode().longValue();
    long bookingCode = -1;
    if(this.booking != null)
      bookingCode = this.booking.getCode().longValue();
    Invoice inv = new Invoice(-1, this.day, this.month, this.year, customerCode, bookingCode, this.breakfastPrice, this.mwst1, this.mwst2, this.accomodationTax, new String((char[])this.note.clone()), new String((char[])this.lastRow.clone()));
    inv.paymentMethod = this.paymentMethod;
    return inv;
  }

  public Table getSubtable(int subtableIndex)
  {
    if(subtableIndex == 0)
      return this.services;
    return null;
  }

  public boolean isRemovable(int howToRead)
  {
/*
    Table t = Data.db.getTable(Consts.tbInvoices);
    if(t.at(t.size() - 1) == this) return true;
    return false;*/
    if(this.booking != null && (((Stay)this.booking).invoiceNo2 == this.code || ((Stay)this.booking).invoiceNo3 == this.code)) return false;
    return true;
  }
  public boolean isMovable(int howToRead)
  {
    return false;
  }

  public String wasAdd(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    if(this.booking != null)
    {
      Stay thisbooking = (Stay)this.booking;
      thisbooking.invoiceNo = this.code;
      if(new String(thisbooking.note).indexOf("STORNOGEBÜHR") > -1)
      {
        if(this.note.length == 0)
          this.note = "STORNOGEBÜHR".toCharArray();
        else
          this.note = (new String(this.note) + " STORNOGEBÜHR").toCharArray();
      }
      if(thisbooking.combine)
      {
        DateRepresentation thisbookingdate = new DateRepresentation(thisbooking.getValue(0, 1).toString());
        Table t = Data.db.getTable(Consts.tbStay);
        int pos = 0;
        for(int i = t.size() - 1; i > -1; i--)
        {
          Stay st = (Stay)t.at(i);
          if(!st.combine) continue;
          if(st == thisbooking)
          {
            pos = i;
            continue;
          }
          if(st.payer != thisbooking.payer) continue;
          if(st.invoiceNo > 0) continue;
          if(pos - i > 1000) break;
          DateRepresentation dt = new DateRepresentation(st.getValue(0, 1).toString());
          if(dt.calculateDifference(thisbookingdate) > 15 || dt.calculateDifference(thisbookingdate) < -15) continue;
          st.invoiceNo = this.code;
        }

      }
      if(thisbooking.payer != thisbooking.payer2 && thisbooking.payer2 != null)
      {
        if(thisbooking.payer2 != thisbooking.payer3 && thisbooking.payer3 != null)
        {
          TableElement inv1 = this.duplicate();
          TableElement inv2 = this.duplicate();
          Table invoices = Data.db.getTable(Consts.tbInvoices);
          long lc = invoices.at(invoices.size() - 1).getCode().longValue();
          if(invoices.findElement(this.code + 1) != null)
            inv1.setCode(lc + 1);
          else
            inv1.setCode(this.code + 1);
          if(invoices.findElement(this.code + 2) != null)
            inv2.setCode(lc + 2);
          else
            inv2.setCode(this.code + 2);
          invoices.addTableElementSimple(inv1);
          invoices.addTableElementSimple(inv2);
          thisbooking.invoiceNo2 = inv1.getCode().longValue();
          thisbooking.invoiceNo3 = inv2.getCode().longValue();
          ((Invoice)inv1).note = ("1. Splittrechnung von " + this.code).toCharArray();
          ((Invoice)inv2).note = ("2. Splittrechnung von " + this.code).toCharArray();
          ((Invoice)inv1).customer = thisbooking.payer2;
          ((Invoice)inv2).customer = thisbooking.payer3;
        }
        else
        {
          TableElement inv1 = this.duplicate();
          Table invoices = Data.db.getTable(Consts.tbInvoices);
          long lc = invoices.at(invoices.size() - 1).getCode().longValue();
          if(invoices.findElement(this.code + 1) != null)
            inv1.setCode(lc + 1);
          else
            inv1.setCode(this.code + 1);
          invoices.addTableElementSimple(inv1);
          thisbooking.invoiceNo2 = inv1.getCode().longValue();
          ((Invoice)inv1).note = ("Splittrechnung von " + this.code).toCharArray();
          ((Invoice)inv1).customer = thisbooking.payer2;
        }
      }
    }
    return "";
  }

  public String wasRemoved(int howToRead, TableElement parentElement, String currentUser, DateRepresentation date)
  {
    if(this.booking != null)
    {
      Stay thisbooking = (Stay)this.booking;
      thisbooking.invoiceNo = 0;
      if(thisbooking.combine)
      {
        DateRepresentation thisbookingdate = new DateRepresentation(thisbooking.getValue(0, 1).toString());
        Table t = Data.db.getTable(Consts.tbStay);
        int pos = 0;
        for(int i = t.size() - 1; i > -1; i--)
        {
          Stay st = (Stay)t.at(i);
          if(!st.combine) continue;
          if(st == thisbooking)
          {
            pos = i;
            continue;
          }
          if(st.payer != thisbooking.payer) continue;
          if(st.invoiceNo != this.code) continue;
          if(pos - i > 1000) break;
          DateRepresentation dt = new DateRepresentation(st.getValue(0, 1).toString());
          if(dt.calculateDifference(thisbookingdate) > 15 || dt.calculateDifference(thisbookingdate) < -15) continue;
          st.invoiceNo = 0;
        }
      }
      if(thisbooking.invoiceNo2 != 0 && thisbooking.invoiceNo3 != 0)
      {
        Table invoices = Data.db.getTable(Consts.tbInvoices);
        invoices.remove(invoices.findElement(thisbooking.invoiceNo2));
        thisbooking.invoiceNo2 = 0;
        invoices.remove(invoices.findElement(thisbooking.invoiceNo3));
        thisbooking.invoiceNo3 = 0;
      }
      else if(thisbooking.invoiceNo2 != 0)
      {
        Table invoices = Data.db.getTable(Consts.tbInvoices);
        invoices.remove(invoices.findElement(thisbooking.invoiceNo2));
        thisbooking.invoiceNo2 = 0;
      }
    }
    Table t = Data.db.getTable(Consts.tbInvoices);
    if(t.at(t.size() - 1) != this)
    {
      ( (Firm) Data.db.getTable(Consts.tbHotels).at(0)).nextInvoiceNo = this.code;
      ( (Firm) Data.db.getTable(Consts.tbHotels).at(0)).nextInvoiceDay = this.day;
      ( (Firm) Data.db.getTable(Consts.tbHotels).at(0)).nextInvoiceMonth = this.month;
      ( (Firm) Data.db.getTable(Consts.tbHotels).at(0)).nextInvoiceYear = this.year;
    }
    return "";
  }

  public void setCode(long newCode)
  {
    long cd = ((Firm)Data.db.getTable(Consts.tbHotels).at(0)).nextInvoiceNo;
    if(Data.db.getTable(Consts.tbInvoices).findElement(cd) == null)
    {
      this.code = cd;
      Firm f = (Firm)Data.db.getTable(Consts.tbHotels).at(0);
      this.day = f.nextInvoiceDay;
      this.month = f.nextInvoiceMonth;
      this.year = f.nextInvoiceYear;
    }
    else
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
    return Consts.getTitleInvoice(howToRead, subtableIndex, this.code, language);
  }

  public String getCellColor(int howToRead, int column)
  {
    return "";
  }

  public Table getReferenceTable(int column, int howToRead)
  {
    return null;
  }

  public long getReferenceCode(int column, int howToRead, User user, String language)
  {
    return -1;
  }

}
package utils;

import java.util.GregorianCalendar;

public class DateRepresentation extends GregorianCalendar
{
  private boolean isEmpty = false;

  public DateRepresentation(int day, int month, int year)
  {
    super(year, month - 1, day);
    if (year == 2)
    {
      day = 0;
      month = 0;
      year = 0;
    }
    if (day < 1 && month < 1 && year < 1)
      this.isEmpty = true;
  }

  public DateRepresentation(boolean realTime)
  {
    super();
    if (!realTime)
    {
      super.set(super.HOUR_OF_DAY, 0);
      super.set(super.MINUTE, 0);
      super.set(super.SECOND, 0);
      super.set(super.MILLISECOND, 0);
    }
  }

  public DateRepresentation(String date)
  {
    this(date.equals("") ? 0 : Integer.parseInt(date.substring(8, date.length())), date.equals("") ? 0 : Integer.parseInt(date.substring(5, 7)),
            date.equals("") ? 0 : Integer.parseInt(date.substring(0, 4)));
  }

  public static String getTime(int hour, int minute)
  {
    if (hour == -1 || minute == -1)
      return "";
    String s;
    if (hour < 10)
      s = "0" + hour + ":";
    else
      s = "" + hour + ":";
    if (minute < 10)
      s += "0" + minute;
    else
      s += "" + minute;
    return s;
  }

  public void setTime(String time)
  {
    if (time.length() < 5)
      return;
    int hour = Integer.valueOf(time.substring(0, 2)).intValue();
    int minute = Integer.valueOf(time.substring(3, 5)).intValue();
    super.set(super.HOUR_OF_DAY, hour);
    super.set(super.MINUTE, minute);
  }

  public void setTime(int hour, int minute, int second)
  {
    if (hour > -1)
      super.set(super.HOUR_OF_DAY, hour);
    if (minute > -1)
      super.set(super.MINUTE, minute);
    if (second > 0)
      super.set(super.SECOND, second);
    else
      super.set(super.SECOND, 0);
  }

  public void setTime(int hour, int minute)
  {
    if (hour > -1)
      super.set(super.HOUR_OF_DAY, hour);
    if (minute > -1)
      super.set(super.MINUTE, minute);
  }

  public String getThisTime()
  {
    int hour = this.getHour();
    int minute = this.getMinute();
    if (hour == -1 || minute == -1)
      return "";
    String s;
    if (hour < 10)
      s = "0" + hour + ":";
    else
      s = "" + hour + ":";
    if (minute < 10)
      s += "0" + minute;
    else
      s += "" + minute;
    return s;
  }

  public static int getHour(String time)
  {
    String hour = time.substring(0, time.indexOf(":"));
    if (hour.startsWith("0"))
      hour = hour.substring(1, hour.length());
    if (hour.length() == 0)
      return 0;
    return Integer.parseInt(hour);
  }

  public static int getMinute(String time)
  {
    if (time.indexOf(":") == time.length() - 1)
      return 0;
    String minute = time.substring(time.indexOf(":") + 1, time.length());
    if (minute.equals("0"))
      return 0;
    if (minute.startsWith("0"))
      minute = minute.substring(1, minute.length());
    return Integer.parseInt(minute);
  }

  public int getHour()
  {
    return super.get(super.HOUR_OF_DAY);
  }

  public int getMinute()
  {
    return super.get(super.MINUTE);
  }

  public int compareTo(DateRepresentation date)
  {
    if (this.isEmpty && date.isEmpty)
      return 0;
    if (this.isEmpty)
      return -1;
    if (super.after(date))
      return 1;
    if (super.before(date))
      return -1;
    return 0;
  }

  public int calculateDifference(DateRepresentation parameterDate)
  {
    if (parameterDate.getDay() == this.getDay() && parameterDate.getMonth() == this.getMonth() && parameterDate.getYear() == this.getYear())
      return 0;
    DateRepresentation thisDateCopy = new DateRepresentation(this.getDay(), this.getMonth(), this.getYear());
    DateRepresentation parameterDateCopy = new DateRepresentation(parameterDate.getDay(), parameterDate.getMonth(), parameterDate.getYear());
    int dif = 0;
    if (parameterDateCopy.after(thisDateCopy))
    {
      while (parameterDateCopy.after(thisDateCopy))
      {
        dif++;
        thisDateCopy.add(DateRepresentation.DATE, 1);
      }
      return -dif;
    }
    while (thisDateCopy.after(parameterDateCopy))
    {
      dif++;
      parameterDateCopy.add(DateRepresentation.DATE, 1);
    }
    return dif;
  }

  public double calculateDifferenceIncludingTime(DateRepresentation secondaryDate)
  {
    int dateDifference = this.calculateDifference(secondaryDate);
    int hourDifference = this.getHour() - secondaryDate.getHour();
    int minuteDifference = this.getMinute() - secondaryDate.getMinute();
    double timeDifference = hourDifference + minuteDifference / 60;

    return dateDifference + timeDifference / 24;
  }

  public int calculateDifferenceIgnoringWeekends(DateRepresentation date)
  {
    if (date.equals(this))
      return 0;
    int dif = 0;
    if (date.after(this))
    {
      DateRepresentation copy = new DateRepresentation(this.getDay(), this.getMonth(), this.getYear());
      while (date.after(copy))
      {
        int dow = copy.get(copy.DAY_OF_WEEK);
        if (dow != 7 && dow != 1)
          dif++;
        copy.add(DateRepresentation.DATE, 1);
      }
      return -dif;
    }
    DateRepresentation copy = new DateRepresentation(date.getDay(), date.getMonth(), date.getYear());
    while (this.after(copy))
    {
      int dow = copy.get(copy.DAY_OF_WEEK);
      if (dow != 7 && dow != 1)
        dif++;
      copy.add(DateRepresentation.DATE, 1);
    }
    return dif;
  }

  public String toString()
  {
    if (this.isEmpty)
      return "";
    int month = this.getMonth();
    String m;
    if (month < 10)
      m = "0" + month;
    else
      m = "" + month;
    int day = this.getDay();
    String d;
    if (day < 10)
      d = "0" + day;
    else
      d = "" + day;
    if (this.getYear() == 0)
      return "0000" + "." + "00" + "." + "00";
    return this.getYear() + "." + m + "." + d;
  }

  public String toString2()
  {
    if (this.isEmpty)
      return "";
    int month = this.getMonth();
    String m;
    if (month < 10)
      m = "0" + month;
    else
      m = "" + month;
    int day = this.getDay();
    String d;
    if (day < 10)
      d = "0" + day;
    else
      d = "" + day;
    if (this.getYear() == 0)
      return "0000" + "." + "00" + "." + "00";
    return d + "." + m + "." + this.getYear();
  }

  public String toString3()
  {
    if (this.isEmpty)
      return "";
    int month = this.getMonth();
    String m;
    if (month < 10)
      m = "0" + month;
    else
      m = "" + month;
    int day = this.getDay();
    String d;
    if (day < 10)
      d = "0" + day;
    else
      d = "" + day;
    if (this.getYear() == 0)
      return "0000" + "." + "00" + "." + "00";
    return d + "." + m;
  }

  public String getFullTime()
  {
    int second = super.get(super.SECOND);
    String s = getThisTime();
    if (second == -1)
      return s;
    if (second < 10)
      s += ":0" + second;
    else
      s += ":" + second;
    return s;
  }

  public int getDay()
  {
    return super.get(GregorianCalendar.DAY_OF_MONTH);
  }

  public int getMonth()
  {
    return super.get(GregorianCalendar.MONTH) + 1;
  }

  public int getYear()
  {
    return super.get(GregorianCalendar.YEAR);
  }

  public int getDayWeek()
  {
    return super.get(GregorianCalendar.DAY_OF_WEEK);
  }

  public static DateRepresentation getPreviousMonthFirstDay(DateRepresentation date)
  {
    DateRepresentation currentDate = new DateRepresentation(date.getDay(), date.getMonth(), date.getYear());
    currentDate.add(GregorianCalendar.DATE, -currentDate.getDay());
    currentDate.add(GregorianCalendar.DATE, -currentDate.getDay() + 1);
    currentDate.setTime(0, 1);
    return currentDate;
  }

  public static DateRepresentation getPreviousMonthLastDay(DateRepresentation date)
  {
    DateRepresentation currentDate = new DateRepresentation(date.getDay(), date.getMonth(), date.getYear());
    currentDate.add(GregorianCalendar.DATE, -currentDate.getDay());
    currentDate.setTime(23, 59);
    return currentDate;
  }
}

package utils;

public class Consts
{
//  public static String host = "localhost";
  public static String host = "217.91.53.50";

  private static boolean windows = true;

  public final static String www = "c:\\www";

  private final static String home = "/RSCgate/";

  public static String home()
  {
    if(windows) return home;
    return "/";
  }

  public final static String btAttach(String language)
  {
    if (language.equals("de"))
      return "Anhängen";
    else if (language.equals("en"))
      return "Attach";
    else
      return "Прикрепить";
  }

  public final static String btShow(String language)
  {
    if (language.equals("de"))
      return "Anzeigen";
    else if (language.equals("en"))
      return "Show";
    else
      return "Показать";
  }

  public final static String btSave(String language)
  {
    if(language.equals("de"))
      return "Speichern";
    else
    {
      if(language.equals("en"))
        return "Save";
      else
        return "Сохранить";
    }
  }

  public final static String btDetails(String language)
  {
    if (language.equals("de"))
    {
      return "Details";
    }
    else
    {
      if (language.equals("en"))
      {
        return "Details";
      }
      else
      {
        return "Подробно";
      }
    }
  }

  public static String[] clTableAuthorization(String language)
  {
    String[] s = new String[15];
    if (language.equals("de"))
    {
      s[0] = "Tabelle";
      s[1] = "Kann sehen";
      s[2] = "Kann bearbeiten";
      s[3] = "Kann entfernen";
      s[4] = "Kann anfügen";
      s[5] = "Kann setzen";
      s[6] = "Kann anwenden";
      s[7] = "Kann aufteilen";
      s[8] = "Kann verschieben";
      s[9] = "Operation1";
      s[10] = "Operation2";
      s[11] = "Operation3";
      s[12] = "Operation4";
      s[13] = "Operation5";
      s[14] = "Übersetzung";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Table";
        s[1] = "Can see";
        s[2] = "Can edit";
        s[3] = "Can remove";
        s[4] = "Can add";
        s[5] = "Can set";
        s[6] = "Can apply";
        s[7] = "Can split";
        s[8] = "Can move";
        s[9] = "Operation1";
        s[10] = "Operation2";
        s[11] = "Operation3";
        s[12] = "Operation4";
        s[13] = "Operation5";
        s[14] = "Translation";
      }
      else
      {
        s[0] = "Таблица";
        s[1] = "Может видеть";
        s[2] = "Может обрабатывать";
        s[3] = "Может удалять";
        s[4] = "Может добавлять";
        s[5] = "Может присвоить";
        s[6] = "Может применить";
        s[7] = "Может разбить";
        s[8] = "Может перенести";
        s[9] = "Операция1";
        s[10] = "Операция2";
        s[11] = "Операция3";
        s[12] = "Операция4";
        s[13] = "Операция5";
        s[14] = "Перевод";
      }
    }
    return s;
  }

  public static String[] clBank(String language)
  {
    String[] clname = new String[9];
    if (language.equals("de"))
    {
      clname[0] = "Bank";
      clname[1] = "BLZ";
      clname[2] = "Konto";
      clname[3] = "SWIFT";
      clname[4] = "IBAN";
      clname[5] = "PLZ";
      clname[6] = "Strasse";
      clname[7] = "Ort";
      clname[8] = "Land";
    }
    else
    {
      if (language.equals("en"))
      {
        clname[0] = "Bank";
        clname[1] = "Branch code";
        clname[2] = "Account";
        clname[3] = "SWIFT";
        clname[4] = "IBAN";
        clname[5] = "Zip code";
        clname[6] = "Street";
        clname[7] = "City";
        clname[8] = "Country";
      }
      else
      {
        clname[0] = "Банк";
        clname[1] = "Код филлиала";
        clname[2] = "Р/с";
        clname[3] = "БИК";
        clname[4] = "Междун.р/с";
        clname[5] = "Индекс";
        clname[6] = "Улица";
        clname[7] = "Город";
        clname[8] = "Страна";
      }
    }
    return clname;
  }

  public static String[] clColumnAuthorization(String language)
  {
    String[] s = new String[4];
    if (language.equals("de"))
    {
      s[0] = "Spalte";
      s[1] = "Kann sehen";
      s[2] = "Kann bearbeiten";
      s[3] = "Reihenfolge";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Column";
        s[1] = "Can see";
        s[2] = "Can edit";
        s[3] = "Order";
      }
      else
      {
        s[0] = "Колонка";
        s[1] = "Может видеть";
        s[2] = "Может обрабатывать";
        s[3] = "Последовательность";
      }
    }
    return s;
  }

  public static String[] clContactPerson(String language)
  {
    String[] s = new String[5];
    if (language.equals("de"))
    {
      s[0] = "Name";
      s[1] = "Abteilung";
      s[2] = "Tel.";
      s[3] = "Fax";
      s[4] = "Email";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Name";
        s[1] = "Department";
        s[2] = "Tel.";
        s[3] = "Fax";
        s[4] = "Email";
      }
      else
      {
        s[0] = "Имя";
        s[1] = "Отдел";
        s[2] = "Тел.";
        s[3] = "Факс";
        s[4] = "Эл.почта";
      }
    }
    return s;
  }

  public static String[] clStringColumnElement(String language, int howToRead)
  {
    String[] s = new String[1];
//    if(howToRead == 1)
//    {
      if (language.equals("de"))
        s[0] = "Bezeichung";
      else if (language.equals("en"))
        s[0] = "Name";
      else
        s[0] = "Название";
//    }
    return s;
  }

  public static String[] clCustomer(String language)
  {
    String[] s = new String[14];
    if (language.equals("de"))
    {
      s[0] = "Nr.";
      s[1] = "Vorname";
      s[2] = "Name";
      s[3] = "Firmenname";
      s[4] = "Geburtsdatum";
      s[5] = "Strasse";
      s[6] = "PLZ";
      s[7] = "Ort";
      s[8] = "Land";
      s[9] = "Tel.";
      s[10] = "Fax";
      s[11] = "Email";
      s[12] = "Notiz";
      s[13] = "Reisebüro";
    }
    else if (language.equals("en"))
    {
      s[0] = "Nr.";
      s[1] = "Name";
      s[2] = "Surname";
      s[3] = "Firm";
      s[4] = "Birth date";
      s[5] = "Street";
      s[6] = "Post code";
      s[7] = "City";
      s[8] = "Country";
      s[9] = "Tel.";
      s[10] = "Fax";
      s[11] = "Email";
      s[12] = "Note";
      s[13] = "Travel agency";
    }
    else
    {
      s[0] = "№";
      s[1] = "Имя";
      s[2] = "Фамилия";
      s[3] = "Название фирмы";
      s[4] = "Дата рождения";
      s[5] = "Улица";
      s[6] = "Почтовый код";
      s[7] = "Город";
      s[8] = "Страна";
      s[9] = "Тел.";
      s[10] = "Факс";
      s[11] = "Емэйл";
      s[12] = "Заметка";
      s[13] = "Бюро путешествий";
    }
    return s;
  }

  public static String[] clInvoicePosition(String language)
  {
    String[] s = new String[4];
    if (language.equals("de"))
    {
      s[0] = "Leistung";
      s[1] = "Preis";
      s[2] = "Menge";
      s[3] = "Summe";
    }
    else if (language.equals("en"))
    {
      s[0] = "Service";
      s[1] = "Price";
      s[2] = "Amount";
      s[3] = "Sum";
    }
    else
    {
      s[0] = "Услуга";
      s[1] = "Цена";
      s[2] = "Кол-во";
      s[3] = "Сумма";
    }
    return s;
  }

  public static String[] clService(String language)
  {
    String[] s = new String[2];
    if (language.equals("de"))
    {
      s[0] = "Leistung";
      s[1] = "Preis";
    }
    else if (language.equals("en"))
    {
      s[0] = "Service";
      s[1] = "Price";
    }
    else
    {
      s[0] = "Услуга";
      s[1] = "Цена";
    }
    return s;
  }

  public static String[] clRoomPrice(String language)
  {
    String[] s = new String[4];
    if (language.equals("de"))
    {
      s[0] = "Datum";
      s[1] = "Preis";
      s[2] = "Wochentag";
      s[3] = "Mindesttage";
    }
    else if (language.equals("en"))
    {
      s[0] = "Date";
      s[1] = "Price";
      s[2] = "Day of the week";
      s[3] = "Min days";
    }
    else
    {
      s[0] = "Дата";
      s[1] = "Цена";
      s[2] = "День недели";
      s[3] = "Минимум дней";
    }
    return s;
  }

  public static String[] clRoom(String language)
  {
    String[] s = new String[5];
    if (language.equals("de"))
    {
      s[0] = "Nr.";
      s[1] = "Kapazität";
      s[2] = "Bezeichnung";
      s[3] = "Beschreibung";
      s[4] = "Zimmertyp";
    }
    else if (language.equals("en"))
    {
      s[0] = "No.";
      s[1] = "Capacity";
      s[2] = "Name";
      s[3] = "Description";
      s[4] = "Room type";
    }
    else
    {
      s[0] = "№";
      s[1] = "Кол-во спальных мест";
      s[2] = "Название";
      s[3] = "Описание";
      s[4] = "Тип комнаты";
    }
    return s;
  }

  public static String[] clStay(String language)
  {
    String[] s = new String[47];
    if (language.equals("de"))
    {
      s[0] = "Anreisedatum";
      s[1] = "Abreisedatum";
      s[2] = "Optionsdatum";
      s[3] = "Bestätigt";
      s[4] = "Zimmer";
      s[5] = "Kapazität";
      s[6] = "Zimmername";
      s[7] = "Vorname1";
      s[8] = "Name1";
      s[9] = "Firma1";
      s[10] = "Vorname2";
      s[11] = "Name2";
      s[12] = "Firma2";
      s[13] = "Gästeanzahl";
      s[14] = "Rezeptionist";
      s[15] = "Frühstuck";
      s[16] = "Frühstuck ab";
      s[17] = "Frühstuck bis";
      s[18] = "Notiz";
      s[19] = "Angereist";
      s[20] = "Schlüssel zurückgegeben";
      s[21] = "Abgereist";
      s[22] = "Rechnungsnr";
      s[23] = "Preis";
      s[24] = "Gebucht über";
      s[25] = "Zahlungsmethode";
      s[26] = "Bezahlt";
      s[27] = "Rechnungsempfänger2";
      s[28] = "Gast2";
      s[29] = "Rechnungsempfänger3";
      s[30] = "Gast3";
      s[31] = "Zusammenfassen";
      s[32] = "Strasse";
      s[33] = "PLZ";
      s[34] = "Ort";
      s[35] = "Land";
      s[36] = "Rechnungsnr2";
      s[37] = "Rechnungsnr3";
      s[38] = "E-Mail";
      s[39] = "Anzahl der Tage";
      s[40] = "Summe";
      s[41] = "Buchungs-Nr.";
      s[42] = "Online Buchungs-Nr.";
      s[43] = "Farbe";
      s[44] = "Log";
      s[45] = "Benutzer UUID";
      s[46] = "Zugangscode";
    }
    else if (language.equals("en"))
    {
      s[0] = "Checkin date";
      s[1] = "Checkout date";
      s[2] = "Option date";
      s[3] = "Confirmed";
      s[4] = "Room";
      s[5] = "Capacity";
      s[6] = "Room name";
      s[7] = "Name1";
      s[8] = "Surname1";
      s[9] = "Firm1";
      s[10] = "Name2";
      s[11] = "Surname2";
      s[12] = "Firm2";
      s[13] = "Amount of guests";
      s[14] = "Receptionist";
      s[15] = "Breakfast";
      s[16] = "Breakfast from";
      s[17] = "Breakfast till";
      s[18] = "Note";
      s[19] = "Checked in";
      s[20] = "Key returned";
      s[21] = "Checked out";
      s[22] = "Invoice no.";
      s[23] = "Price";
      s[24] = "Booked over";
      s[25] = "Payment method";
      s[26] = "Payed";
      s[27] = "Payer2";
      s[28] = "Guest2";
      s[29] = "Payer3";
      s[30] = "Guest3";
      s[31] = "Combine";
      s[32] = "Street";
      s[33] = "Post code";
      s[34] = "City";
      s[35] = "Country";
      s[36] = "Invoice no.2";
      s[37] = "Invoice no.3";
      s[38] = "E-Mail";
      s[39] = "Amount of days";
      s[40] = "Sum";
      s[41] = "Booking no.";
      s[42] = "Online booking no.";
      s[43] = "Colour";
      s[44] = "Log";
      s[45] = "User UUID";
      s[46] = "Access code";
    }
    else
    {
      s[0] = "Дата приезда";
      s[1] = "Дата отъезда";
      s[2] = "Дата подтверждения";
      s[3] = "Подтверждено";
      s[4] = "Комната";
      s[5] = "Вместимость";
      s[6] = "Название комнаты";
      s[7] = "Имя1";
      s[8] = "Фамилия1";
      s[9] = "Фирма1";
      s[10] = "Имя2";
      s[11] = "Фамилия2";
      s[12] = "Фирма2";
      s[13] = "Кол-во гостей";
      s[14] = "Рецепционист";
      s[15] = "Завтрак";
      s[16] = "Завтрак с";
      s[17] = "Завтрак до";
      s[18] = "Заметка";
      s[19] = "Прибыл";
      s[20] = "Ключ отдан";
      s[21] = "Выехал";
      s[22] = "№ счёта";
      s[23] = "Цена";
      s[24] = "Заказанно через";
      s[25] = "Способ оплаты";
      s[26] = "Оплачено";
      s[27] = "Плательщик2";
      s[28] = "Гость2";
      s[29] = "Плательщик3";
      s[30] = "Гость3";
      s[31] = "Суммировать";
      s[32] = "Улица";
      s[33] = "Почт. код";
      s[34] = "Город";
      s[35] = "Страна";
      s[36] = "№ счёта2";
      s[37] = "№ счёта3";
      s[38] = "Эл. почта";
      s[39] = "Кол-во дней";
      s[40] = "Сумма";
      s[41] = "№ брони";
      s[42] = "№ онлайн брони";
      s[43] = "Цвет";
      s[44] = "Log";
      s[45] = "UUID пользователя";
      s[46] = "Код доступа";
    }
    return s;
  }

  public static String[] clInvoice(String language)
  {
    String[] s = new String[23];
    if (language.equals("de"))
    {
      s[0] = "Nr.";
      s[1] = "Datum";
      s[2] = "Vorname";
      s[3] = "Name";
      s[4] = "Firma";
      s[5] = "Zimmer";
      s[6] = "Übernachtung/Tag netto";
      s[7] = "Frühstuck/Tag netto";
      s[8] = "Übernachtungstage";
      s[9] = "Frühstuckstage";
      s[10] = "Übernachtung netto";
      s[11] = "Frühstuck netto";
      s[12] = "Übernachtungssteuer";
      s[13] = "Leistungen netto";
      s[14] = "MwSt1";
      s[15] = "MwSt2";
      s[16] = "Gesamtsumme";
      s[17] = "Notiz";
      s[18] = "Letzte Zeile";
      s[19] = "Zahlungsmethode";
      s[20] = "Übernachtung netto inkl. übernachtungssteuer";
      s[21] = "Gästeanzahl";
      s[22] = "Produkt";
    }
    else if (language.equals("en"))
    {
      s[0] = "No.";
      s[1] = "Date";
      s[2] = "Name";
      s[3] = "Surname";
      s[4] = "Firm";
      s[5] = "Room";
      s[6] = "Accomodation/day net";
      s[7] = "Breakfast/day net";
      s[8] = "Accomodation days";
      s[9] = "Breakfast days";
      s[10] = "Accomodation net";
      s[11] = "Breakfast net";
      s[12] = "Accomodation tax";
      s[13] = "Services net";
      s[14] = "V.A.T.1";
      s[15] = "V.A.T.2";
      s[16] = "Sum";
      s[17] = "Note";
      s[18] = "Last row";
      s[19] = "Payment method";
      s[20] = "Accomodation net incl. Accomodation tax";
      s[21] = "Guest amount";
      s[22] = "Product";
    }
    else
    {
      s[0] = "№";
      s[1] = "Дата";
      s[2] = "Имя";
      s[3] = "Фамилия";
      s[4] = "Фирма";
      s[5] = "Комната";
      s[6] = "Ночёвка/день нетто";
      s[7] = "Завтрак/день нетто";
      s[8] = "Дни ночёвок";
      s[9] = "Дни завтрака";
      s[10] = "Ночёвка нетто";
      s[11] = "Завтрак нетто";
      s[12] = "Налог на ночёвку";
      s[13] = "Услуги нетто";
      s[14] = "НДС1";
      s[15] = "НДС2";
      s[16] = "Общая сумма";
      s[17] = "Заметка";
      s[18] = "Последняя строчка";
      s[19] = "Способ оплаты";
      s[20] = "Сумма вкл. туристический налог";
      s[21] = "Кол-во гостей";
      s[22] = "Произведение";
    }
    return s;
  }

  public static String[] clOnlineBooking(String language)
  {
    String[] s = new String[26];
    if (language.equals("de"))
    {
      s[0] = "Buchungsnummer";
      s[1] = "Vorname";
      s[2] = "Name";
      s[3] = "Anreisedatum";
      s[4] = "Abreisedatum";
      s[5] = "Zimmertyp";
      s[6] = "Babybett";
      s[7] = "Hund klein";
      s[8] = "Hund groß";
      s[9] = "Land";
      s[10] = "Ort";
      s[11] = "PLZ";
      s[12] = "Strasse";
      s[13] = "Email";
      s[14] = "Telefonnummer";
      s[15] = "Firma";
      s[16] = "Notiz";
      s[17] = "Gästeanzahl";
      s[18] = "Bezahlt";
      s[19] = "Preis";
      s[20] = "Id Transaktion";
      s[21] = "Summe";
      s[22] = "Anzahl der Tage";
      s[23] = "Übernommen";
      s[24] = "Echte Rüstung Nr.";
      s[25] = "Log";
    }
    else if (language.equals("en"))
    {
      s[0] = "Reservation number";
      s[1] = "Name";
      s[2] = "Surname";
      s[3] = "Checkin date";
      s[4] = "Checkout date";
      s[5] = "Type of room";
      s[6] = "Baby bed";
      s[7] = "Dog small";
      s[8] = "Dog big";
      s[9] = "Country";
      s[10] = "City";
      s[11] = "Post code";
      s[12] = "Street";
      s[13] = "E-mail";
      s[14] = "Phone number";
      s[15] = "Firm";
      s[16] = "Note";
      s[17] = "Amount of guests";
      s[18] = "Payed";
      s[19] = "Price";
      s[20] = "Id transaction";
      s[21] = "Sum";
      s[22] = "Amount of days";
      s[23] = "Accepted";
      s[24] = "Real booking no.";
      s[25] = "Log";
    }
    else if (language.equals("ru"))
    {
      s[0] = "Номер брони";
      s[1] = "Имя";
      s[2] = "Фамилия";
      s[3] = "Дата приезда";
      s[4] = "Дата отъезда";
      s[5] = "Тип комнаты";
      s[6] = "Детская кровать";
      s[7] = "Мал. собака";
      s[8] = "Большая собака";
      s[9] = "Страна";
      s[10] = "Город";
      s[11] = "Почт.код";
      s[12] = "Улица";
      s[13] = "Эл. почта";
      s[14] = "Телефон";
      s[15] = "Фирма";
      s[16] = "Заметка";
      s[17] = "Кол-во гостей";
      s[18] = "Оплачено";
      s[19] = "Стоимость номера";
      s[20] = "Номер транзакции";
      s[21] = "Сумма";
      s[22] = "Кол-во дней";
      s[23] = "Принято";
      s[24] = "№ реальный";
      s[25] = "Log";
    }
    return s;
  }

  public static String[] clFirm(String language)
  {
    String[] s = new String[21];
    if (language.equals("de"))
    {
      s[0] = "Nr.";
      s[1] = "Name";
      s[2] = "Tel.";
      s[3] = "Fax";
      s[4] = "Email";
      s[5] = "Strasse";
      s[6] = "PLZ";
      s[7] = "Ort";
      s[8] = "Land";
      s[9] = "MwSt.";
      s[10] = "USt.Ident.Nr";
      s[11] = "Steuernr.";
      s[12] = "Web";
      s[13] = "MwSt. Wert 1";
      s[14] = "MwSt. Wert 2";
      s[15] = "SMTP";
      s[16] = "SMTP Authorization";
      s[17] = "Frühstuckspreis";
      s[18] = "Nächste Rechnungsnr.";
      s[19] = "Rechnungspfad";
      s[20] = "Nächste Rechnungsdatum";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "No.";
        s[1] = "Name";
        s[2] = "Tel.";
        s[3] = "Fax";
        s[4] = "Email";
        s[5] = "Street";
        s[6] = "Zip";
        s[7] = "City";
        s[8] = "Country";
        s[9] = "V.A.T.";
        s[10] = "V.A.T. no.";
        s[11] = "Tax no.";
        s[12] = "Web";
        s[13] = "V.A.T. value 1";
        s[14] = "V.A.T. value 2";
        s[15] = "SMTP";
        s[16] = "SMTP Authorization";
        s[17] = "Breakfast price";
        s[18] = "Next invoice";
        s[19] = "Invoice path";
        s[20] = "Next invoice date";
      }
      else
      {
        s[0] = "№";
        s[1] = "Имя";
        s[2] = "Тел.";
        s[3] = "Факс";
        s[4] = "Эл.почта";
        s[5] = "Улица";
        s[6] = "Индекс";
        s[7] = "Город";
        s[8] = "Страна";
        s[9] = "НДС";
        s[10] = "№ НДС";
        s[11] = "налоговый №";
        s[12] = "Веб";
        s[13] = "НДС значение 1";
        s[14] = "НДС значение 2";
        s[15] = "SMTP";
        s[16] = "SMTP Authorization";
        s[17] = "Цена завтрака";
        s[18] = "Следующий № счёта";
        s[19] = "Путь к счетам";
        s[20] = "Дата след. счёта";
      }
    }
    return s;
  }

  public final static String[] clDynamicEmployeeUser(String language)
  {
    String[] s = new String[4];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Name";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Name";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Фамилия";
      }
    }
    return s;
  }

  public static String[] clEmployeeUser(String language)
  {
    String[] s = new String[5];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Braucht Update";
      s[4] = "Name";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Needs update";
        s[4] = "Name";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Нужд. в актуал.";
        s[4] = "Фамилия";
      }
    }
    return s;
  }

  public static String[] clGuestUser(String language)
  {
    String[] s = new String[7];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Braucht Update";
      s[4] = "Name";
      s[5] = "Kundennr.";
      s[6] = "Kunde";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Needs update";
        s[4] = "Name";
        s[5] = "Customer no.";
        s[6] = "Customer";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Нужд. в актуал.";
        s[4] = "Фамилия";
        s[5] = "№ клиента";
        s[6] = "Клиент";
      }
    }
    return s;
  }

  public static String[] clDynamicGuestUser(String language)
  {
    String[] s = new String[6];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Name";
      s[4] = "Kundennr.";
      s[5] = "Kunde";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Name";
        s[4] = "Customer no.";
        s[5] = "Customer";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Фамилия";
        s[4] = "№ клиента";
        s[5] = "Клиент";
      }
    }
    return s;
  }

  public static String[] clGuestBuyerUser(String language)
  {
    String[] s = new String[7];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Braucht Update";
      s[4] = "Name";
      s[5] = "Empfängernr.";
      s[6] = "Empfänger";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Needs update";
        s[4] = "Name";
        s[5] = "Consignee no.";
        s[6] = "Consignee";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Нужд. в актуал.";
        s[4] = "Фамилия";
        s[5] = "№ получателя";
        s[6] = "Получатель";
      }
    }
    return s;
  }

  public static String[] clDynamicGuestBuyerUser(String language)
  {
    String[] s = new String[6];
    if (language.equals("de"))
    {
      s[0] = "Benutzername";
      s[1] = "Kennwort";
      s[2] = "Ist online";
      s[3] = "Name";
      s[4] = "Kundennr.";
      s[5] = "Kunde";
    }
    else
    {
      if (language.equals("en"))
      {
        s[0] = "Login";
        s[1] = "Password";
        s[2] = "Is online";
        s[3] = "Name";
        s[4] = "Customer no.";
        s[5] = "Customer";
      }
      else
      {
        s[0] = "Логин";
        s[1] = "Пароль";
        s[2] = "Онлайн";
        s[3] = "Фамилия";
        s[4] = "№ клиента";
        s[5] = "Клиент";
      }
    }
    return s;
  }

  public static String[] clColumnFilter(String language)
  {
    String[] s = new String[1];
    if (language.equals("de"))
      s[0] = "Wert";
    else
    {
      if (language.equals("en"))
        s[0] = "Value";
      else
        s[0] = "Значение";
    }
    return s;
  }

  public static String ermsExistsPos(String language)
  {
    String message = "";
    if (language.equals("de"))
      message = "Die Position existiert bereits";
    else
    {
      if (language.equals("en"))
        message = "This position already exists";
      else
        message = "Такая позиция уже существует";
    }
    return message;
  }

  public static String ermsAddTableElementPos(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Die Position existiert bereits. Die neue Menge wurde dazu addiert";
    else
    {
      if (language.equals("en"))
        ms =
            "This position already exists.  The new amount is added to the old.";
      else
        ms = "Такая позиция уже существует. Новое кол-во было прибавлено";
    }
    return ms;
  }

  public static String ermsErrorAddElem(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Das Element konnte nicht hinzugefügt werden";
    else
    {
      if (language.equals("en"))
        ms =
            "This element cannot be added";
      else
        ms = "Элемент добавить не получилось";
    }
    return ms;
  }

  public static String ermsNoAddElem(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Dieses Element konnte nicht hinzugefügt werden";
    else
    {
      if (language.equals("en"))
        ms =
            "This element cannot be added.";
      else
        ms = "Этот элемент не получилось добавить";
    }
    return ms;
  }

  public static String ermsZelleNotEdit(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Die Zelle darf nicht bearbeiten werden";
    else
    {
      if (language.equals("en"))
        ms = "The cell is not editable";
      else
        ms = "Эта клетка не может быть обработана";
    }
    return ms;
  }

  public static String ermsExistsElem(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Der element existiert bereits";
    else
    {
      if (language.equals("en"))
        ms = "The element already exists";
      else
        ms = "Такой элемент уже существует";
    }
    return ms;
  }

  public final static String ermsBusy(String language)
  {
    String errorMessage = "";
    if (language.equals("en"))
      errorMessage = "Busy";
    else if (language.equals("de"))
      errorMessage = "Besetzt";
    else if (language.equals("ru"))
      errorMessage = "Занято";
    return errorMessage;
  }

  public final static String ermsInvDate(String language)
  {
    String errorMessage = "";
    if (language.equals("en"))
      errorMessage = "Invalid date";
    else if (language.equals("de"))
      errorMessage = "Inkorrektes Datum";
    else if (language.equals("ru"))
      errorMessage = "Не правильная дата";
    return errorMessage;
  }

  public static String ermsInvAmount(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Ungültige Menge";
    else
    {
      if (language.equals("en"))
        ms = "Invalid amount";
      else
        ms = "Неверное кол-во";
    }
    return ms;
  }

  public static String ermsNotSplitColumn(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Nicht aufspaltbare Spalte";
    else
    {
      if (language.equals("en"))
        ms = "Unsplittable column";
      else
        ms = "По этой колонке делить нельзя";
    }
    return ms;
  }

  public static String ermsNotAmount(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Die Menge kann nicht geändert werden";
    else
    {
      if (language.equals("en"))
        ms = "The amount is not editable";
      else
        ms = "Кол-во не может быть измененно";
    }
    return ms;
  }

  public static String ermsNothingToShow(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Dokumente nicht vorhanden";
    else
    {
      if (language.equals("en"))
        ms = "No documents";
      else
        ms = "Документа нет";
    }
    return ms;
  }

  public static String ermsNoSuchTable(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Solche Tabelle könnte nicht gefunden werden";
    else
    {
      if (language.equals("en"))
        ms = "No such table found";
      else
        ms = "Такая таблица не существует";
    }
    return ms;
  }

  public static String ermsNoElemMove(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Der Element ist nicht verschiebbar";
    else
    {
      if (language.equals("en"))
        ms = "The element is not movable";
      else
        ms = "Элемент не может быть передвинут";
    }
    return ms;
  }

  public static String ermsWrongCustomer(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Falsche Kunde";
    else if (language.equals("en"))
      ms = "Wrong customer";
    else
      ms = "Не правильный клиент";
    return ms;
  }

  public static String ermsNoPosition(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Die position existiert nicht";
    else
    {
      if (language.equals("en"))
        ms = "The position does not exist";
      else
        ms = "Такая позиция не существует";
    }
    return ms;
  }


  public static String ermsWrongVersion(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Dies ist eine alte Version. Ein Update wird nun heruntergeladen";
    else
    {
      if (language.equals("en"))
        ms = "This is an old version. You will now be updated";
      else
        ms = "Это старая версия. Сейчас скачается новая версия.";
    }
    return ms;
  }

  public static String ermsWrongJavaVersion(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Java-Version ist unkompatibel";
    else
    {
      if (language.equals("en"))
        ms = "Java version ist incompatible";
      else
        ms = "Не подходящая версия Явы";
    }
    return ms;
  }

  public static String ermsUserNotLoged(String language)
  {
    String errorMessage = "";
    if (language.equals("de"))
      errorMessage = "Benutzer ist schon eingeloggt!";
    else if (language.equals("en"))
      errorMessage = "User already logged in!";
    else if (language.equals("ru"))
      errorMessage = "Пользователь уже залогован!";
    else
      errorMessage = "Fuck off";
    return errorMessage;
  }

  public static String ermsInvLogin(String language)
  {
    String errorMessage = "";
    if (language.equals("de"))
      errorMessage = "Inkorrekte Login";
    else if (language.equals("en"))
      errorMessage = "Invalid login";
    else if (language.equals("ru"))
      errorMessage =
          "Не правильный логин";
    else
      errorMessage = "Fuck off";
    return errorMessage;
  }

  public static String ermsNotTableDB(String language)
  {
    String errorMessage = "";
    if (language.equals("en"))
      errorMessage = "No users table in database";
    else if (language.equals("de"))
      errorMessage = "Keine Benutzertabelle in Datenbank vorhanden";
    else if (language.equals("ru"))
      errorMessage = "Таблица пользователей не существует";
    return errorMessage;
  }

  public static String ermsInvName(String language)
  {
    String errorMessage = "";
    if (language.equals("en"))
      errorMessage = "No such user found";
    else if (language.equals("de"))
      errorMessage = "Inkorrekte Benutzername eingegeben";
    else if (language.equals("ru"))
      errorMessage = "Такого пользователя не существует";
    return errorMessage;
  }

  public static String ermsInvParameter(String language)
  {
    String errorMessage = "";
    if (language.equals("en"))
      errorMessage = "Invalid parameter";
    else if (language.equals("de"))
      errorMessage = "Inkorrekte Parameter";
    else if (language.equals("ru"))
      errorMessage = "Не правильный параметр";
    return errorMessage;
  }

  public static String ermsRemovePosition(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Die Position darf nicht gelöscht werden";
    else
    {
      if (language.equals("en"))
        ms = "The position cannot be removed";
      else
        ms = "Эта позиция не может быть удалена";
    }
    return ms;
  }

  public static String version = "1620.5009";

  public static String tbWebGuestUsers = "webGuests";

  public static String tbCustomers = "customers";

  public static String tbRooms = "rooms";

  public static String tbRoomTypes = "roomTypes";

  public static String tbBooking = "booking";

  public static String tbPayment = "payment";

  public static String tbService = "service";

  public static String tbStay = "stay";

  public static String tbOnlineBooking = "onlineBooking";

  public static String tbInvoices = "invoices";

  public static String tbHotels = "hotels";

  public static String tbUsers = "users";

  public static String tbWebGuestBuyerUsers = "webGuestBuyer";

  public static String tbDynamicGuestBuyerUsers = "dynamicGuestBuyer";

  public static String tbDynamicGuestUsers = "dynamicGuests";

  public static String tbDynamicUsers = "dynamicUsers";

  public final static String tbBuyers = "buyers";

  public static String tbGuestUsers = "guests";

  public static String tbGuestBuyerUsers = "guestBuyer";

  public static String tbWebUsers = "webUsers";

  public static String tbDraftBooking = "draftBooking";

  public final static String gtBank(String language)
  {
    if (language.equals("de"))
      return "Banken";
    else
      if (language.equals("en"))
        return "Banks";
      else
        return "Банки";
  }

  public final static String gtColumnAuthorization(String language)
  {
    if (language.equals("de"))
      return "Spaltenzugriffsrechte";
    else
      if (language.equals("en"))
        return "Column authorizations";
      else
        return "Права доступа к колонкам";
  }

  public final static String gtContactPerson(String language)
  {
    if (language.equals("de"))
      return "Kontaktpersonen";
    else if (language.equals("en"))
      return "Contact persons";
    else
      return "Контактные лица";
  }

  public final static String gtDraftBooking(String language) {
    if (language.equals("de"))
      return "Buchungsentwürfe";
    else if (language.equals("en"))
      return "Booking drafts";
    else
      return "Заготовки брони";
  }

  public final static String gtRoom(String language)
  {
    if (language.equals("de"))
      return "Zimmer";
    else if (language.equals("en"))
      return "Rooms";
    else
      return "Комнаты";
  }

  public final static String gtInoices(String language)
  {
    if (language.equals("de"))
      return "Rechnungen";
    else if (language.equals("en"))
      return "Invoices";
    else
      return "Счета";
  }

  public final static String gtStringColumnElement(String language, int howToRead)
  {
    if(howToRead == 1)
    {
      if (language.equals("de"))
        return "Buchungsmethoden";
      else if (language.equals("en"))
        return "Booking method";
      else
        return "Способ резервации";
    }
    else
    {
      if (language.equals("de"))
        return "Zahlungsmethoden";
      else if (language.equals("en"))
        return "Payment method";
      else
        return "Способ оплаты";
    }
  }

  public final static String gtRoomType(String language)
  {
    if (language.equals("de"))
      return "Zimmertypen";
    else if (language.equals("en"))
      return "Room types";
    else
      return "Типы комнат";
  }

  public final static String gtRoomPrice(String language)
  {
    if (language.equals("de"))
      return "Preise";
    else if (language.equals("en"))
      return "Prices";
    else
      return "Цены";
  }

  public final static String gtService(String language)
  {
    if (language.equals("de"))
      return "Leistungen";
    else if (language.equals("en"))
      return "Services";
    else
      return "Услуги";
  }

  public final static String gtStay(String language)
  {
    if (language.equals("de"))
      return "Aufenthalt";
    else if (language.equals("en"))
      return "Stay";
    else
      return "Постояльцы";
  }

  public final static String gtOnlineBooking(String language)
  {
    if (language.equals("de"))
      return "Online-Buchung";
    else if (language.equals("en"))
      return "Online booking";
    else
      return "Онлайн бронирование";
  }


  public final static String gtCustomer(String language)
  {
    if (language.equals("de"))
      return "Kunden";
    else if (language.equals("en"))
      return "Customers";
    else
      return "Клиенты";
  }

  public final static String gtHotels(String language)
  {
    if (language.equals("de"))
      return "Hotels";
    else
    if (language.equals("en"))
      return "Hotels";
    else
      return "Гостинницы";
  }

  public final static String gtColumnFilter(String language)
  {
    if (language.equals("de"))
      return "Standartfilter";
    else
    if (language.equals("en"))
      return "Default filter";
    else
      return "Фильтр по умолчанию";
  }

  public final static String gtEmployeeUser(String language, int userType)
  {
    if (userType == 0)
      if (language.equals("de"))
        return "Statische Benutzer";
      else
      if (language.equals("en"))
        return "Static users";
      else
        return "Статические пользователи";
    else if (userType == 1)
    {
      if (language.equals("de"))
        return "Dynamische Benutzer";
      else
      if (language.equals("en"))
        return "Dynamic users";
      else
        return "Динамические пользователи";
    }
    else
    if (language.equals("de"))
      return "Web-Benutzer";
    else
    if (language.equals("en"))
      return "Web users";
    else
      return "Web пользователи";
  }

  public final static String gtGuestUser(String language, int userType)
  {
    if (userType == 0)
      if (language.equals("de"))
        return "Statische Kunden-Benutzer";
      else
      if (language.equals("en"))
        return "Static customer users";
      else
        return "Статические клиенты-пользователи";
    else if (userType == 1)
      if (language.equals("de"))
        return "Dynamische Kunden-Benutzer";
      else
      if (language.equals("en"))
        return "Dynamic customer users";
      else
        return "Динамические клиенты-пользователи";
    else
    if (language.equals("de"))
      return "Web-Kunden-Benutzer";
    else
    if (language.equals("en"))
      return "Web customer users";
    else
      return "Web клиенты-пользователи";
  }

  public final static String gtGuestBuyerUser(String language, int userType)
  {
    if (userType == 0)
      if (language.equals("de"))
        return "Statische Empfänger-Benutzer";
      else
      if (language.equals("en"))
        return "Static consignee users";
      else
        return "Статические пользователи-получатели";
    else if (userType == 1)
      if (language.equals("de"))
        return "Dynamische Empfänger-Benutzer";
      else
      if (language.equals("en"))
        return "Dynamic consignee users";
      else
        return "Динамические покупатели-получатели";
    else
    if (language.equals("de"))
      return "Web-Empfänger-Benutzer";
    else
    if (language.equals("en"))
      return "Web consignee users";
    else
      return "Web покупатели-получатели";
  }

  public final static String gtTableAuthorization(String language)
  {
    if (language.equals("de"))
      return "Tabellenzugriffsrechte";
    else
      if (language.equals("en"))
        return "Table authorizations";
      else
        return "Права доступа к таблицам";
  }

  public final static String getTitleHotel(int howToRead, int subtableIndex,
                                          String language)
  {
    String s = "";
    if (language.equals("de"))
      switch (subtableIndex)
      {
        case 0:
          s = "Kontaktpersonen von Hotel nr. ";
          break;
        case 1:
          s = "Banken von Hotel nr. ";
          break;
        default:
          break;
      }
    else
      if (language.equals("en"))
        switch (subtableIndex)
        {
          case 0:
            s = "Contact persons of hotel no. ";
            break;
          case 1:
            s = "Banks of hotel no. ";
            break;
          default:
            break;
        }
      else
        switch (subtableIndex)
        {
          case 0:
            s = "Контактные лица гостинницы № ";
            break;
          case 1:
            s = "Банки гостинницы № ";
            break;
          default:
            break;
        }
    return s;
  }

  public final static String getTitleRoomType(int howToRead, int subtableIndex, String roomType, String language)
  {
    String s = "";
    if (language.equals("de"))
      switch (subtableIndex)
      {
        case 0:
          s = "Preise von Zimmertyp " + roomType;
          break;
        default:
          break;
      }
    else
      if (language.equals("en"))
        switch (subtableIndex)
        {
          case 0:
            s = "Prices of room type " + roomType;
            break;
          default:
            break;
        }
      else
        switch (subtableIndex)
        {
          case 0:
            s = "Цены на " + roomType;
            break;
          default:
            break;
        }
    return s;
  }

  public final static String getTitleInvoice(int howToRead, int subtableIndex, long invoiceCode, String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Leistungen von Rechnung " + invoiceCode;
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Services of invoice " + invoiceCode;
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Услуги " + invoiceCode;
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleEmployeeUser(int howToRead, int subtableIndex, String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Tabellenzugriffsrechte vom Benutzer ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Table authorizations of user ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Права доступа к таблицам пользователя ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleWebUser(int howToRead, int subtableIndex,
                                             String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Tabellenzugriffsrechte vom Benutzer ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Table authorizations of user ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Права доступа к таблицам пользователя ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleGuestUser(int howToRead, int subtableIndex,
                                               String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Tabellenzugriffsrechte vom Kunden-Benutzer ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Table authorizations of customer user ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Права доступа к таблицам клиента-пользователя ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleGuestBuyerUser(int howToRead,
      int subtableIndex, String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Tabellenzugriffsrechte vom Empfänger-Benutzer ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Table authorizations of consignee user ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Права доступа к таблицам покупателя-получателя ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleTableAuthorization(int howToRead, int subtableIndex, String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Spaltenzugriffsrechte von d. Tabelle ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Column authorizations of table ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Права доступа к колонкам из таблицы ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String getTitleColumnAuthorization(int howToRead, int subtableIndex, String language)
  {
    String s = "";
    if (language.equals("de"))
    {
      switch (subtableIndex)
      {
        case 0:
          s = "Standartfilter von d.Spalte  ";
          break;
        default:
          break;
      }
    }
    else
    {
      if (language.equals("en"))
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Default filter for column ";
            break;
          default:
            break;
        }
      }
      else
      {
        switch (subtableIndex)
        {
          case 0:
            s = "Фильтр по умолчанию для колонки ";
            break;
          default:
            break;
        }
      }
    }
    return s;
  }

  public final static String varDayWeek(int day, String language)
  {
    if(language.equals("de"))
    {
      switch (day)
      {
        case 1:
          return "So";
        case 2:
          return "Mo";
        case 3:
          return "Di";
        case 4:
          return "Mi";
        case 5:
          return "Do";
        case 6:
          return "Fr";
        case 7:
          return "Sa";
      }
    }
    else if (language.equals("en"))
    {
      switch (day)
      {
        case 1:
          return "Su";
        case 2:
          return "Mo";
        case 3:
          return "Tu";
        case 4:
          return "We";
        case 5:
          return "Th";
        case 6:
          return "Fr";
        case 7:
          return "Sa";
      }
    }
    else
    {
      switch (day)
      {
        case 1:
          return "Вс";
        case 2:
          return "Пн";
        case 3:
          return "Вт";
        case 4:
          return "Ср";
        case 5:
          return "Чт";
        case 6:
          return "Пт";
        case 7:
          return "Сб";
      }
    }
    return "";
  }

  public final static String opApply(String language)
  {
    if (language.equals("de"))
      return "Übernehmen";
    else
    {
      if (language.equals("en"))
        return "Apply";
      else
        return "Принять";
    }
  }

  public final static String opSetMinDays(String language)
  {
    if (language.equals("de"))
      return "Mindesttage";
    else if (language.equals("en"))
      return "Set min days";
    else
      return "Установить мин.дней";
  }

  public final static String opSetPrices(String language)
  {
    if (language.equals("de"))
      return "Preise festlegen";
    else if (language.equals("en"))
      return "Set prices";
    else
      return "Устновить цены";
  }

  public final static String ermsAddLimit(String language)
  {
    String ms = "";
    if (language.equals("de"))
      ms = "Datum nicht in der Zimmerkategorie verfügbar:";
    else
    {
      if (language.equals("en"))
        ms = "Date not available in room category:";
      else
        ms = "Дата отсутствует в категории номера:";
    }
    return ms;
  }

  public static String[] clRoomType(String language, int howToRead)
  {
    String[] s = new String[2];
    if (language.equals("de"))
    {
      s[0] = "Bezeichung";
      s[1] = "Reihenfolge";
    }
    else if (language.equals("en"))
    {
      s[0] = "Name";
      s[1] = "Order";
    }
    else
    {
      s[0] = "Название";
      s[1] = "Последовательность";
    }
    return s;
  }

  public static String logTemplate(char[] log, DateRepresentation date, String currentUser, String gt)
  {
    if (date == null)
      date = new DateRepresentation(true);
    return new String(log) + date.toString() + " " + date.getFullTime() + " " + currentUser + " " + gt + " ";
  }

}
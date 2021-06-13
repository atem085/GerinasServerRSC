package element;

import table.TableElementExample;
import table.User;
import table.TableElement;
import utils.MyObjectInputStream;
import table.Table;
import java.io.PrintWriter;
import utils.MyObjectOutputStream;
import utils.Consts;
import java.io.*;
import data.Data;
import table.Command11;
import utils.DateRepresentation;

public class OnlineBookingExample implements TableElementExample
{
  public OnlineBookingExample()
  {
    createNewOnlineBooking();
  }

  public String[] getColumnNames(int howToRead, String language, User user)
  {
    return Consts.clOnlineBooking(language);
  }

  public int[] getKeyColumns(int howToRead)
  {
    int[] i = new int[0];
    return i;
  }

  public TableElement read(MyObjectInputStream in, int howToRead, Table parentTable, TableElement parentElement)
  {
    OnlineBooking ob = null;
    try
    {
      ob = new OnlineBooking(in.readLong(), in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readLong(), in.readBoolean(),
                             in.readBoolean(), in.readBoolean(), in.readUTF(), in.readInt(), in.readBoolean(),  in.readDouble(), in.readUTF(), in.readBoolean());
      ob.bookingNo = in.readLong();
      ob.log = in.readUTF().toCharArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return ob;
  }

  public TableElement newInstance(Object[] parameters, int howToRead, TableElement parentElement)
  {
    return new OnlineBooking(((Long)parameters[0]).longValue(), ((Long)parameters[1]).longValue(), parameters[2].toString(), parameters[3].toString(), ((Long)parameters[4]).longValue(),
                             ((Boolean)parameters[5]).booleanValue(), ((Boolean)parameters[6]).booleanValue(), ((Boolean)parameters[7]).booleanValue(),
                              parameters[8].toString(), ((Integer) parameters[9]).intValue(), ((Boolean) parameters[10]).booleanValue(), ((Double) parameters[11]).doubleValue(),
                              parameters[12].toString());
  }

  public boolean isSplittable(int columnIndex, int howToRead)
  {
    return false;
  }

  public int getAmountColumnIndex(int howToRead)
  {
    return 0;
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
    Class[] c = new Class[26];
    c[0] = Long.class;
    c[1] = String.class;
    c[2] = String.class;
    c[3] = String.class;
    c[4] = String.class;
    c[5] = String.class;
    c[6] = Boolean.class;
    c[7] = Boolean.class;
    c[8] = Boolean.class;
    c[9] = String.class;
    c[10] = String.class;
    c[11] = String.class;
    c[12] = String.class;
    c[13] = String.class;
    c[14] = String.class;
    c[15] = String.class;
    c[16] = String.class;
    c[17] = Integer.class;
    c[18] = Boolean.class;
    c[19] = Double.class;
    c[20] = String.class;
    c[21] = Double.class;
    c[22] = Integer.class;
    c[23] = Boolean.class;
    c[24] = Long.class;
    c[25] = String.class;
    return c;
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

  public String executeOperation(int howToRead, int operationIndex, long selectedCode, int selectedColumn, Object[] parameters, Table currentTable, String language, String currentUser, DateRepresentation date)
  {
    if(howToRead == 1 && operationIndex == 0)
    {
      OnlineBooking ob = (OnlineBooking)currentTable.findElement(selectedCode);
      long room = ((Long)parameters[0]).longValue();
      String mes = "";
      if(ob.babyBed)
        mes = "Babybett; ";
      if(ob.dogSmall)
        mes += "Hund klein; ";
      if(ob.dogBig)
        mes += "Hund groß;";
      Stay s = new Stay(new DateRepresentation(ob.checkinDay, ob.checkinMonth, ob.checkinYear).toString(), new DateRepresentation(ob.checkoutDay, ob.checkoutMonth, ob.checkoutYear).toString(),
                        room, ob.elGuest.getCode().longValue(), ob.elGuest.getCode().longValue(), ob.amountGuests, false, mes, ob.price, 4867273642678L, 210088959741L, "");
      Data.db.getTable(Consts.tbStay).addTableElement(s, "en", currentUser, date);
      TableElement te = Data.db.getTable(Consts.tbStay).at(Data.db.getTable(Consts.tbStay).size() - 1);
      ((Stay)te).onlineBookingNo = ob.getCode().longValue();
      ob.bookingNo = te.getCode().longValue();
      ob.accepted = true;
    }
    return "";
  }

  public String getOperationName(int howToRead, int operationIndex, String language)
  {
    if(operationIndex == 0)
      return Consts.opApply("en");
    return "";
  }

  public int[] getInitialParametersColumnIndices(int howToRead)
  {
    return null;
  }

  public int[] getFractionDigits(int howToRead)
  {
    int[] i = new int[24];
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
    i[19] = 2;
    i[20] = 0;
    i[21] = 2;
    i[22] = 0;
    i[23] = 0;
    return i;

  }

  public String getTitle(String language, int howToRead, User user)
  {
    return Consts.gtOnlineBooking(language);
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

  private void createNewOnlineBooking()
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
            }
            catch (Exception e){}
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
            if (basicFileName.equals("bookingFile"))
            {
              while (!DIR.listFiles()[DIR.listFiles().length-1].canRead());
              Long idFile = new Long(DIR.listFiles()[DIR.listFiles().length-1].getName().split("=")[1].split("\\.")[0]);
              try
              {
                FileReader fr = new FileReader(DIR.listFiles()[DIR.listFiles().length-1]);
                BufferedReader reader = new BufferedReader(fr);
                int rooms, persons = 0;
                String checkinDate, checkoutDate, country, city, zip, street, email, phone, fName, lName, category, idTransaction = null;
                String company = "";
                String comment = "";
                double price;
                boolean bBed, sDog, bDog;
                String line = reader.readLine();
                Table tbOnlineBooking = Data.db.getTable(Consts.tbOnlineBooking);
                OnlineBooking ob = null;
                long bookingNumb = 1;
                if (tbOnlineBooking.size() > 0)
                {
                  TableElement te = (OnlineBooking) tbOnlineBooking.get(tbOnlineBooking.size() - 1);
                  bookingNumb = ( (Long) te.getValue(0, 1)).longValue();
                  bookingNumb++;
                }
                while (line != null)
                {
                  checkinDate = line.split("=")[1];
                  line = reader.readLine();
                  checkoutDate = line.split("=")[1];
                  line = reader.readLine();
                  rooms = Integer.parseInt(line.split("=")[1]);
                  line = reader.readLine();
                  country = line.split("=")[1];
                  line = reader.readLine();
                  street = line.split("=")[1];
                  line = reader.readLine();
                  zip = line.split("=")[1];
                  line = reader.readLine();
                  city = line.split("=")[1];
                  line = reader.readLine();
                  phone = line.split("=")[1];
                  line = reader.readLine();
                  email = line.split("=")[1];
                  line = reader.readLine();
                  if (line.split("=").length > 1)
                    comment = line.split("=")[1];
                  line = reader.readLine();
                  fName = line.split("=")[1];
                  line = reader.readLine();
                  lName = line.split("=")[1];
                  line = reader.readLine();
                  if (line.split("=").length > 1)
                    company = line.split("=")[1];
                  line = reader.readLine();
                  idTransaction = line.split("=")[1];
                  line = reader.readLine();
                  long guestCode = findGuest(fName, lName, email, company);
                  if(guestCode < 0)
                    guestCode = createCustomer(fName, lName, company, street, zip, city, country, phone, email);
                  for(int i = 0; i < rooms; i++)
                  {
                    category = line.split("=")[0];
                    price = Double.parseDouble(line.split("=")[1]);
                    line = reader.readLine();
                    persons = Integer.parseInt(line.split("=")[1]);
                    line = reader.readLine();
                    bBed = !line.split("=")[1].equals("0");
                    line = reader.readLine();
                    sDog = !line.split("=")[1].equals("0");
                    line = reader.readLine();
                    bDog = !line.split("=")[1].equals("0");
                    line = reader.readLine();
                    DateRepresentation inDate = new DateRepresentation(checkinDate);
                    DateRepresentation outDate = new DateRepresentation(checkoutDate);
                    TableElement roomType = Data.db.getTable(Consts.tbRoomTypes).at(Data.db.getTable(Consts.tbRoomTypes).findTableElementsIgnoreCase(category, 0)[0]);
                    ob = new OnlineBooking(bookingNumb + i, guestCode, inDate.toString(), outDate.toString(), roomType.getCode().longValue(), bBed, sDog,
                                           bDog, comment, persons, false, price, idTransaction);
                    tbOnlineBooking.addTableElement(ob, "en", "", null);
                    if (tbOnlineBooking.lastOperationResult)
                    {
                      Data.version++;
                      Object[] parameters = new Object[13];
                      parameters[0] = new Long(bookingNumb + i);
                      parameters[1] = new Long(guestCode);
                      parameters[2] = inDate.toString();
                      parameters[3] = outDate.toString();
                      parameters[4] = roomType.getCode();
                      parameters[5] = new Boolean(bBed);
                      parameters[6] = new Boolean(sDog);
                      parameters[7] = new Boolean(bDog);
                      parameters[8] = comment;
                      parameters[9] = new Integer(persons);
                      parameters[10] = new Boolean(false);
                      parameters[11] = new Double(price);
                      parameters[12] = idTransaction;
                      Command11 command = new Command11(Consts.tbOnlineBooking, parameters, "", new DateRepresentation(true), Data.version);
                      Data.commander.announce(command, "web", "web", "en", true, tbOnlineBooking, null, null, Consts.tbOnlineBooking);
                    }
                  }
                }
                reader.close();
                final String FILENAME = "c:\\RSCTomcatGate\\responds\\chBookingNumb=" + idFile + ".txt";
                BufferedWriter bw = null;
                FileWriter fw = null;
                try
                {
                  fw = new FileWriter(FILENAME);
                  bw = new BufferedWriter(fw);
                  bw.write("bookingNumb=" + bookingNumb);
                }
                catch (Exception e) {}
                finally
                {
                 try
                 {
                   bw.close();
                   fw.close();
                 }
                 catch (Exception ex) {}
                }
              }
              catch (Exception e)
              {
                e.printStackTrace();
              }
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

 private long findGuest(String fName, String lName, String email, String company)
 {
   Table tbCustomers = Data.db.getTable(Consts.tbCustomers);
   int[] foundCustomer = null;
   if(company.length() > 0)
   {
     foundCustomer = tbCustomers.findTableElementsIgnoreCase(company, 3);
     if (foundCustomer.length == 0)
       return -1;
     else
       return tbCustomers.at(foundCustomer[0]).getCode().longValue();
   }

   foundCustomer = tbCustomers.findTableElementsIgnoreCase(lName, 2);
   TableElement elCustomer = null;
   for(int i = 0; i < foundCustomer.length; i++)
   {
     elCustomer = tbCustomers.at(foundCustomer[i]);
     if(elCustomer.getValue(1, 1).toString().equalsIgnoreCase(fName) && elCustomer.getValue(11, 1).toString().equalsIgnoreCase(email))
       return elCustomer.getCode().longValue();
   }
   return -1;
 }

 private long createCustomer(String name, String surname, String company, String str, String zip, String city, String country, String tel, String email)
 {
   Customer2 c = new Customer2(Long.MAX_VALUE, name, surname, company, 0, 0, 0, str, zip, city, country, tel, "", email, "");
   Table tbCustomers = Data.db.getTable(Consts.tbCustomers);
   tbCustomers.addTableElement(c, "en", "", null);

   if (tbCustomers.lastOperationResult)
   {
     Data.version++;
     Object[] parameters = new Object[12];
     parameters[0] = name;
     parameters[1] = surname;
     parameters[2] = company;
     parameters[3] = "2000.01.01";
     parameters[4] = str;
     parameters[5] = zip;
     parameters[6] = city;
     parameters[7] = country;
     parameters[8] = tel;
     parameters[9] = "";
     parameters[10] = email;
     parameters[11] = "";
     Command11 command = new Command11(Consts.tbCustomers, parameters, "", new DateRepresentation(true), Data.version);
     Data.commander.announce(command, "web", "web", "en", true, tbCustomers, null, null, Consts.tbCustomers);
   }
   return c.getCode().longValue();
 }
}
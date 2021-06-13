package data;

import table.TableElementExample;
import table.DB;
import table.Table;
import table.User;
import table.StaticUser;
import com.borland.jbcl.layout.VerticalFlowLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import table.Commander;
import table.CheckAnswersThread;
import table.IncomingClientThread;
import table.ConnectionThread;
import table.OutgoingChannel;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import utils.DateRepresentation;
import utils.NotStandardPrice;
import utils.NotStandardWeight;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import utils.ObjectZipOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.EOFException;
import element.*;
import utils.Consts;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import table.Command25;
import utils.Progress;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.StringTokenizer;
import utils.MyInputStreamReader;
import javax.swing.JTable;
import table.TableElement;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Data
{
  public static ArrayList euCountries = new ArrayList();
  public static boolean guestInvolved = false;
  public static boolean guestBuyerInvolved = false;
  public static SSLServerSocket incomingServerSocket = null;
  public static SSLServerSocket dbServerSocket = null;
  public static SSLServerSocket updateServerSocket = null;
  public static SSLServerSocket outgoingServerSocket = null;
  public static SSLServerSocket connectionServerSocket = null;
  public static SSLServerSocket abcServerSocket = null;
  public static SSLServerSocket dynamicConnectionServerSocket = null;
  public static DB db = null;
  public static ArrayList incomingClientThreads;
  public static ArrayList outgoingChannels;
  public static Commander commander;
  public static CheckAnswersThread cat;
  public static WebThread webThread;
  public static boolean canBeRunning;
  public static ObjectOutputStream logStream;
  public static PrintWriter logWriter;
  public static DateRepresentation date;
  public static String path = "c:\\GerinasRSCData";
  public static JFrame mainFrame;
  public static Progress progress;
  public static long version = Long.MIN_VALUE;
  public static String[] names;
  public static int[] howToRead;
  public static long[] firstCodes;
  public static TableElementExample[] elementExamples;
  public static JProgressBar progressBar;
  public static JButton ssButton;
  public static String[] reportTableName = null;
  public static String[] reportName = null;
  public static int[][] reportSubtableIndex = null;
  public static TableElementExample[] reportExample = null;
  public static int[] reportHowToRead;
  public static Object lock = new Object();
  public static String clientFile, clientCustomerFile, clientBuyerFile;
  public static String clientFile64, clientCustomerFile64, clientBuyerFile64;
  public static String versionID;
  public static String licencedTo;
  public static String licenceDestination;
  public static String licenceDeadline;
  public static boolean trial;
  public static int day, month, year, hour, minute, every;
  public static boolean backup;
  public static String backupPath = "";
  public static int connectionPort = 4108;
  public static int incomingPort = 4109;
  public static int outgoingPort = 4110;
  public static int dbPort = 4111;
  public static int updatePort = 4112;
  public static int abcPort = 4113;
  public static boolean dbLoaded = false;
//  public static int dynamicConnectionPort = 4014;
  public static Thread hook = new Thread()
  {
    public void run()
    {
      try
      {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler C:\\Soft\\RSC\\GerinasServerRSC.jar");
      }
      catch(Exception e) {}
    }
  };

  private static void setLicence()
  {
    versionID = "igdthotelhs4";
    licencedTo = "Hotel38";
    licenceDestination = Consts.tbCustomers;
    trial = false;
    licenceDeadline = new DateRepresentation(true).toString();
  }

  public static void makeExamples()
  {
    clientFile = "C:\\Soft\\RSC\\GerinasClientRSC.jar";
    clientFile64 = "C:\\Soft\\RSC\\GerinasClientRSC.jar";
    clientCustomerFile = "C:\\Soft\\RSC\\GerinasClientRSCcustomer.jar";
    clientCustomerFile64 = "C:\\Soft\\RSC\\GerinasClientRSCcustomer.jar";
    clientBuyerFile = "C:\\Soft\\RSC\\GerinasClientRSCbuyer.jar";
    clientBuyerFile64 = "C:\\Soft\\RSC\\GerinasClientRSCbuyer.jar";

    names = new String[14];
    names[0] = Consts.tbUsers;
    names[1] = Consts.tbWebUsers;
    names[2] = Consts.tbHotels;
    names[3] = Consts.tbCustomers;
    names[4] = Consts.tbWebGuestUsers;
    names[5] = Consts.tbRoomTypes;
    names[6] = Consts.tbRooms;
    names[7] = Consts.tbBooking;
    names[8] = Consts.tbPayment;
    names[9] = Consts.tbService;
    names[10] = Consts.tbStay;
    names[11] = Consts.tbInvoices;
    names[12] = Consts.tbOnlineBooking;
    names[13] = Consts.tbDraftBooking;

    howToRead = new int[14];
    howToRead[0] = 1;
    howToRead[1] = 2;
    howToRead[2] = 1;
    howToRead[3] = 1;
    howToRead[4] = 2;
    howToRead[5] = 3;
    howToRead[6] = 1;
    howToRead[7] = 1;
    howToRead[8] = 2;
    howToRead[9] = 1;
    howToRead[10] = 1;
    howToRead[11] = 1;
    howToRead[12] = 1;
    howToRead[13] = 2;

    elementExamples = new TableElementExample[14];
    elementExamples[0] = new EmployeeUserExample("", "", "");
    elementExamples[1] = new DynamicEmployeeUserExample("", "", "");
    elementExamples[2] = new FirmExample();
    elementExamples[3] = new Customer2Example();
    elementExamples[4] = new DynamicGuestUserExample("", "", "", 0);
    elementExamples[5] = new RoomTypeExample();
    elementExamples[6] = new RoomExample();
    elementExamples[7] = new StringColumnElementExample(1);
    elementExamples[8] = new StringColumnElementExample(2);
    elementExamples[9] = new ServiceExample();
    elementExamples[10] = new StayExample(1);
    elementExamples[11] = new InvoiceExample();
    elementExamples[12] = new OnlineBookingExample();
    elementExamples[13] = new StayExample(2);

    firstCodes = new long[14];
    firstCodes[0] = 1;
    firstCodes[1] = 1;
    firstCodes[2] = 1;
    firstCodes[3] = 100000;
    firstCodes[4] = 1;
    firstCodes[5] = 1;
    firstCodes[6] = 1;
    firstCodes[7] = 1;
    firstCodes[8] = 1;
    firstCodes[9] = 1;
    firstCodes[10] = 1;
    firstCodes[11] = 100000;
    firstCodes[12] = 1;
    firstCodes[13] = 1;

    reportTableName = new String[0];
    reportSubtableIndex = new int[0][];
    reportExample = new TableElementExample[0];
    reportHowToRead = new int[0];
    reportName = new String[0];
/*
    reportTableName[0] = Consts.tbBuyers;
    reportSubtableIndex[0] = new int[2];
    reportSubtableIndex[0][0] = 1;
    reportSubtableIndex[0][1] = 0;
    reportExample[0] = new PaymentPlanningExample(0, "", 0, 0, 0, 0, "", 0, 0, 0, 0, "", 0);
    reportHowToRead[0] = 2;
    reportName[0] = "transactions";
*/
 }

  public static void main(String[] args)
  {
    euCountries = new ArrayList();
    euCountries.add("Belgien");
    euCountries.add("BE");
    euCountries.add("Bulgarien");
    euCountries.add("BG");
    euCountries.add("Dänemark");
    euCountries.add("DK");
    euCountries.add("Deutschland");
    euCountries.add("DE");
    euCountries.add("Estland");
    euCountries.add("EE");
    euCountries.add("Finnland");
    euCountries.add("FI");
    euCountries.add("Frankreich");
    euCountries.add("FR");
    euCountries.add("Griechenland");
    euCountries.add("GR");
    euCountries.add("Irland");
    euCountries.add("IE");
    euCountries.add("Italien");
    euCountries.add("IT");
    euCountries.add("Lettland");
    euCountries.add("LV");
    euCountries.add("Litauen");
    euCountries.add("LT");
    euCountries.add("Luxemburg");
    euCountries.add("LU");
    euCountries.add("Malta");
    euCountries.add("MT");
    euCountries.add("Niederlande");
    euCountries.add("NL");
    euCountries.add("Österreich");
    euCountries.add("AT");
    euCountries.add("Polen");
    euCountries.add("PL");
    euCountries.add("Portugal");
    euCountries.add("PT");
    euCountries.add("Rumänien");
    euCountries.add("RO");
    euCountries.add("Schweden");
    euCountries.add("SE");
    euCountries.add("Slowakei");
    euCountries.add("SK");
    euCountries.add("Slowenien");
    euCountries.add("SI");
    euCountries.add("Spanien");
    euCountries.add("ES");
    euCountries.add("Tschechien");
    euCountries.add("CZ");
    euCountries.add("Ungarn");
    euCountries.add("HU");
    euCountries.add("Vereinigtes Königreich");
    euCountries.add("GB");
    euCountries.add("Zypern");
    euCountries.add("CY");
    Runtime.getRuntime().removeShutdownHook(hook);
    setLicence();
    canBeRunning = false;
    File ff = new File(path);
    if(!ff.exists())
      ff.mkdir();

    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    if(trial && new DateRepresentation(true).compareTo(new DateRepresentation(licenceDeadline)) > 0)
    {
      System.out.println("You have reached the deadline of your licence.  Please prolong it calling +49 176 34928125");
      System.exit(0);
    }
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dim = toolkit.getScreenSize();

    File f = new File(path + "\\backup.dat");
    if(f.exists())
    {
      MyObjectInputStream in = null;
      try
      {
        in = new MyObjectInputStream(new FileInputStream(f));
        in.setEncoding(false);
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        every = in.readInt();
        backup = in.readBoolean();
        backupPath = in.readUTF();
        in.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }

    mainFrame = new JFrame("HotelServerRSC DB " + Consts.version + " Java version " + System.getProperty("java.version"));
    JPanel topPanel = new JPanel(new FlowLayout());
    mainFrame.getContentPane().setLayout(new VerticalFlowLayout());
    mainFrame.getContentPane().add(topPanel);
    mainFrame.getContentPane().add(new CurrentInfoTable());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ssButton = new JButton("Start");
    final JButton importButton = new JButton("Import");
    importButton.setEnabled(false);
    final JButton backupButton = new JButton("Backup");
    final JButton restartButton = new RestartButton("Restart settings");
    final JButton userButton = new JButton("Users");
    userButton.addActionListener(new ActionListener()
                                 {
                                   public void actionPerformed(ActionEvent e)
                                   {
                                     Table users = db.getTable(Consts.tbUsers);
                                     final Object[][] us = new Object[users.size()][5];
                                     for(int i = 0; i < users.size(); i++)
                                     {
                                       EmployeeUser eu = (EmployeeUser)users.at(i);
                                       us[i][0] = eu.getValue(0, 1);
                                       us[i][1] = eu.getValue(1, 1);
                                       us[i][2] = eu.getValue(2, 1);
                                       us[i][3] = eu.getValue(3, 1);
                                       us[i][4] = eu.getValue(4, 1);
                                     }
                                     Object[] header = new Object[5];
                                     for(int i = 0; i < 5; i++)
                                       header[i] = users.getColumnName(i);
                                     JTable table = new JTable(us, header)
                                     {
                                       public void setValueAt(Object aValue, int row, int column)
                                       {
                                         String name = us[row][0].toString();
                                         long code = Table.dtJSHashCode(name);
                                         super.setValueAt(aValue, row, column);
                                         if(column < 5)
                                           db.getTable(Consts.tbUsers).setValueAt(aValue, code, column, "en", "", null);
                                       }
                                     };
                                     JDialog dialog = new JDialog(mainFrame, "Users");
                                     JScrollPane jsc = new JScrollPane(table);
                                     dialog.getContentPane().add(jsc);
                                     dialog.pack();
                                     dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
                                     dialog.setLocationRelativeTo(mainFrame);
                                     dialog.setVisible(true);
                                   }
                                 });
    progressBar = new JProgressBar();
    progressBar.setPreferredSize(new Dimension(toolkit.getScreenSize().width / 3, 20));
    ssButton.addActionListener(new ActionListener()
                               {
                                 public void actionPerformed(ActionEvent e)
                                 {
                                   JButton sourceButton = (JButton)e.getSource();
                                   if(sourceButton.getText().equals("Start"))
                                   {
                                     canBeRunning = true;
                                     sourceButton.setText("Stop");
                                     importButton.setEnabled(true);
                                     backupButton.setEnabled(false);
                                   }
                                   else
                                   {
                                     StopThread stopThread = new StopThread();
                                     stopThread.start();
                                     backupButton.setEnabled(true);
                                     sourceButton.setText("Start");
                                     sourceButton.setEnabled(false);
                                   }
                                 }
                               }
                               );
                               importButton.addActionListener(new ActionListener()
                               {
                                 public void actionPerformed(ActionEvent e)
                                 {
                                   String stn = (String)JOptionPane.showInputDialog(null, "Choose table", "Choose table", JOptionPane.DEFAULT_OPTION, null, db.getTableNames(), null);
                                   if(stn == null) return;
                                   Table tabb = db.getTable(stn);
                                   if(tabb.getExample().hasSubtable(tabb.getHowToRead(), 0))
                                   {
                                     Object[] so = new Long[tabb.size()];
                                     for(int m = 0; m < tabb.size(); m++)
                                       so[m] = tabb.at(m).getCode();
                                     Long substn = (Long)JOptionPane.showInputDialog(null,
                                         "Choose subtable", "Choose subtable",
                                         JOptionPane.INFORMATION_MESSAGE, null, so, null);
                                     if(substn == null) return;
                                     tabb = tabb.findElement(substn.longValue()).getSubtable(0);
                                   }
                                   final Table tab = tabb;

                                   JFileChooser fileChooser = new JFileChooser();
                                   fileChooser.showOpenDialog(mainFrame);
                                   File f = fileChooser.getSelectedFile();
                                   if(f != null && f.exists())
                                   {
                                     Object[] options = new Object[2];
                                     options[0] = ";";
                                     options[1] = "\t";
                                     final String opt = (String)JOptionPane.showInputDialog(Data.mainFrame, "Choose the separation sign", "Choose the separation sign", JOptionPane.QUESTION_MESSAGE, null, options, ";");
                                     try
                                     {
                                       final long size = f.length();
                                       final MyInputStreamReader fin = new MyInputStreamReader(new FileInputStream(f), "Unicode");
                                       File nf = new File(f.getAbsolutePath() + ".ni");
                                       final PrintWriter fout = new PrintWriter(new FileOutputStream(nf));
                                       final String line = fin.readLine();
                                       StringTokenizer st = new StringTokenizer(line, opt);
                                       final JDialog d = new JDialog(Data.mainFrame, true);
                                       final int[] initialParametersColumnIndices = tab.getExample().getInitialParametersColumnIndices(tab.getHowToRead());
                                       String[] columnNames = tab.getExample().getColumnNames(tab.getHowToRead(), "en", null);
                                       final int c = st.countTokens();
                                       String[] filteredColumnNames = new String[initialParametersColumnIndices.length + 1];
                                       filteredColumnNames[0] = "Ignore";
                                       for(int i = 1; i < filteredColumnNames.length; i++)
                                         filteredColumnNames[i] = columnNames[initialParametersColumnIndices[i - 1]];
                                       d.getContentPane().setLayout(new VerticalFlowLayout());
                                       JPanel dataPanel = new JPanel(new GridLayout(2, c));
                                       JScrollPane scrollPane = new JScrollPane(dataPanel);
                                       JPanel buttonPanel = new JPanel(new FlowLayout());
                                       final JComboBox[] jcb = new JComboBox[c];
                                       String[] cn = new String[c];
                                       for(int i = 0; i < c; i++)
                                         cn[i] = st.nextToken();
                                       for(int i = 0; i < c; i++)
                                       {
                                         jcb[i] = new JComboBox(filteredColumnNames);
                                         jcb[i].setSelectedItem(cn[i]);
                                       }
                                       for(int i = 0; i < c; i++)
                                         dataPanel.add(jcb[i]);
                                       for(int i = 0; i < c; i++)
                                         dataPanel.add(new JLabel(cn[i]));
                                       final JComboBox[] encs = new JComboBox[c];
                                       d.getContentPane().add(scrollPane);
                                       JButton okButton = new JButton("OK");
                                       JButton simulateButton = new JButton("Simulate");
                                       JButton cancelButton = new JButton("Cancel");
                                       buttonPanel.add(okButton);
                                       buttonPanel.add(simulateButton);
                                       buttonPanel.add(cancelButton);
                                       final JCheckBox jchkbx = new JCheckBox("Clear");
                                       buttonPanel.add(jchkbx);
                                       okButton.addActionListener(new ActionListener()
                                       {
                                         public void actionPerformed(ActionEvent ev)
                                         {
                                           int[] selectedColumns = new int[c];
                                           for(int i = 0; i < c; i++)
                                             selectedColumns[i] = jcb[i].getSelectedIndex();
                                           for(int i = 0; i < selectedColumns.length; i++)
                                             if(selectedColumns[i] == 0)
                                               selectedColumns[i] = -1;
                                             else
                                               selectedColumns[i] = initialParametersColumnIndices[selectedColumns[i] - 1];
                                           if(!DB.canBeInstatiated(selectedColumns, initialParametersColumnIndices))
                                           {
                                             JOptionPane.showConfirmDialog(Data.mainFrame, "Invalid columns", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                                             return;
                                           }
                                           String[] encodings = new String[c];
                                           progressBar.setMaximum((int)size);
                                           tab.handleImportEvent(fin, fout, selectedColumns, line, size, jchkbx.isSelected(), opt, "admin", new DateRepresentation(true)).start();
                                           d.dispose();
                                         }
                                       }
                                       );
                                       simulateButton.addActionListener(new ActionListener()
                                       {
                                         public void actionPerformed(ActionEvent ev)
                                         {
                                           int[] selectedColumns = new int[c];
                                           for(int i = 0; i < c; i++)
                                             selectedColumns[i] = jcb[i].getSelectedIndex();
                                           for(int i = 0; i < selectedColumns.length; i++)
                                             if(selectedColumns[i] == 0)
                                               selectedColumns[i] = -1;
                                             else
                                               selectedColumns[i] = initialParametersColumnIndices[selectedColumns[i] - 1];
                                           if(!DB.canBeInstatiated(selectedColumns, initialParametersColumnIndices))
                                           {
                                             JOptionPane.showConfirmDialog(Data.mainFrame, "Invalid columns", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                                             return;
                                           }
                                           progressBar.setMaximum((int)size);
                                           tab.handleImportSimulateEvent(fin, selectedColumns, line, size, opt).start();
                                           d.dispose();
                                         }
                                       }
                                       );
                                       cancelButton.addActionListener(new ActionListener()
                                       {
                                         public void actionPerformed(ActionEvent ev)
                                         {
                                           try
                                           {
                                             fin.close();
                                           }
                                           catch(Exception eee)
                                           {
                                             eee.printStackTrace();
                                           }
                                           d.dispose();
                                         }
                                       }
                                       );
                                       d.getContentPane().add(buttonPanel);
                                       d.pack();
                                       scrollPane.setPreferredSize(new Dimension(scrollPane.getSize().width,
                                           scrollPane.getSize().height + 40));
                                       d.pack();
                                       d.setVisible(true);
                                     }
                                     catch(Exception eee)
                                     {
                                       eee.printStackTrace();
                                     }
                                   }

                                 }
                               }
                               );
    backupButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        backup();
      }
    }
    );
    topPanel.add(ssButton);
    topPanel.add(importButton);
    topPanel.add(backupButton);
    topPanel.add(restartButton);
    topPanel.add(userButton);
    topPanel.add(progressBar);
    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setResizable(false);
    mainFrame.setVisible(true);
/*    if(args != null && args.length > 0)
    {*/
     ssButton.doClick();
/*    }
    else
    {
      int opt = JOptionPane.showConfirmDialog(mainFrame, "Would you like to back the current db up?",
                                              "Backup", JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE);
      if (opt == 0)
        backup();
    }*/
    while(!canBeRunning)
      try
      {
        Thread.sleep(100);
      }
      catch(Exception e) {}
    System.out.println("Starting server...");

    makeExamples();

    load(path, names, elementExamples, firstCodes, howToRead);
    dbLoaded = true;
/*
    Table stay = Data.db.getTable(Consts.tbStay);
    Table invoices = Data.db.getTable(Consts.tbInvoices);
    for(int i = 0; i < stay.size(); i++)
    {
      TableElement thisbooking = stay.at(i);
      long invoiceNo = ((Long)thisbooking.getValue(22, 1)).longValue();
      if(invoiceNo > 0 && thisbooking.getValue(18, 1).toString().indexOf("STORNOGEB�HR") > -1)
      {
        TableElement inv = invoices.findElement(invoiceNo);
        try
        {
          if (inv.getValue(17, 1).toString().length() == 0)
          {
            inv.setValue(17, "STORNOGEB�HR", "en", 1);
            System.out.println("inv " + inv.getCode().toString());
          }
          else if(inv.getValue(17, 1).toString().indexOf("STORNOGEB�HR") < 0)
          {
            inv.setValue(17, inv.getValue(17, 1).toString() + " STORNOGEB�HR", "en", 1);
            System.out.println("inv " + inv.getCode().toString());
          }
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    }*/
/*
    Table stay = Data.db.getTable(Consts.tbStay);
    DateRepresentation endDate = new DateRepresentation(31, 12, 2015);
    ArrayList invoices = new ArrayList();
    ArrayList bookings = new ArrayList();
    for(int i = stay.size() - 1; i > -1; i--)
    {
      TableElement currentStay = stay.at(i);
      DateRepresentation checkoutDate = new DateRepresentation(currentStay.getValue(1, 1).toString());
      if(checkoutDate.after(endDate)) continue;
      Long invoiceNo = (Long)currentStay.getValue(22, 1);
      if(!bookings.contains(currentStay.getCode()))
        bookings.add(currentStay.getCode());
      if(invoiceNo.longValue() == 0) continue;
      if(!invoices.contains(invoiceNo))
        invoices.add(invoiceNo);
      Long invoiceNo2 = (Long)currentStay.getValue(36, 1);
      if(invoiceNo2.longValue() == 0) continue;
      if(!invoices.contains(invoiceNo2))
        invoices.add(invoiceNo2);
      Long invoiceNo3 = (Long)currentStay.getValue(37, 1);
      if(invoiceNo3.longValue() == 0) continue;
      if(!invoices.contains(invoiceNo3))
        invoices.add(invoiceNo3);
    }
    Table inv = Data.db.getTable(Consts.tbInvoices);
    for(int i = 0; i < invoices.size(); i++)
    {
      System.out.println("Removing invoice " + invoices.get(i).toString());
      inv.removeTableElement( ( (Long) invoices.get(i)).longValue(), "en");
      if(inv.findElement(((Long)invoices.get(i)).longValue()) == null)
        System.out.println("Successfully removed...");
      else
      {
        java.awt.Toolkit.getDefaultToolkit().beep();
        return;
      }
    }
    for(int i = 0; i < bookings.size(); i++)
    {
      System.out.println("Removing booking " + bookings.get(i).toString());
      stay.removeTableElement( ( (Long) bookings.get(i)).longValue(), "en");
      if(stay.findElement(((Long)bookings.get(i)).longValue()) == null)
        System.out.println("Successfully removed...");
      else
      {
        java.awt.Toolkit.getDefaultToolkit().beep();
        return;
      }
    }
    for(int i = inv.size() - 1; i > -1; i--)
    {
      TableElement currentInvoice = inv.at(i);
      DateRepresentation checkoutDate = new DateRepresentation(currentInvoice.getValue(1, 1).toString());
      if (checkoutDate.after(endDate))
        continue;
      if(((Invoice)currentInvoice).booking == null)
      {
        System.out.println("Removing invoice " + currentInvoice.getCode().toString());
        inv.removeTableElement(currentInvoice.getCode().longValue(), "en");
        if(inv.findElement(currentInvoice.getCode().longValue()) == null)
        {
          System.out.println("Successfully removed...");
          i++;
        }
        else
        {
          java.awt.Toolkit.getDefaultToolkit().beep();
          return;
        }
      }
      else if(stay.findElement(((Invoice)currentInvoice).booking.getCode().longValue()) == null)
      {
        System.out.println("Removing invoice " + currentInvoice.getCode().toString());
        inv.removeTableElement(currentInvoice.getCode().longValue(), "en");
        if(inv.findElement(currentInvoice.getCode().longValue()) == null)
        {
          System.out.println("Successfully removed...");
          i++;
        }
        else
        {
          java.awt.Toolkit.getDefaultToolkit().beep();
          return;
        }
      }
    }
*/
    File pathDir = new File(path);
    File[] files = pathDir.listFiles();
    for(int i = 0; i < files.length; i++)
      if(files[i].getAbsolutePath().indexOf("copy") > -1)
        files[i].delete();
    if(!db.success)
      System.exit(1);

    f = new File(path + "\\version.dat");
    if(f.exists())
    {
      MyObjectInputStream in = null;
      try
      {
        in = new MyObjectInputStream(new FileInputStream(f));
        in.setEncoding(false);
        version = in.readLong();
        in.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }

    f = new File(path + "\\log.dat");
    if(f.length() > 0)
    {
      backupLog(true);
      MyObjectInputStream in = null;
      FileInputStream fi = null;
      long rest = f.length();
      try
      {
        fi = new FileInputStream(f);
        in = new MyObjectInputStream(fi);
        in.setEncoding(false);
//        in.setEcho(true);
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.exit(1);
      }
      int requestType = -1;
      try
      {
        for(int i = 0; true; i++)
        {
//          System.out.println("Command " + i);
          try
          {
            requestType = in.readInt();
          }
          catch(EOFException e1)
          {
//            e1.printStackTrace();
            break;
          }
          catch(IOException e)
          {
            e.printStackTrace();
            requestType = -1;
            break;
          }
          commander.readAndExecute(null, in, null, requestType, false);
          try
          {
            rest -= in.available();
          }
          catch(Exception e)
          {
            e.printStackTrace();
            continue;
          }
//          System.out.println("available " + rest);
        }
        in.close();
        Table tt = db.getTable(Consts.tbUsers);
        for(int i = 0; i < tt.size(); i++)
          ((StaticUser)tt.at(i)).needsUpdate = true;
      }
      catch(EOFException e1) {e1.printStackTrace();}
      catch(Exception e)
      {
        e.printStackTrace();
      }
      try
      {
        db.save();
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.exit(1);
      }
    }

    Table tt = db.getTable(Consts.tbUsers);
    for(int i = 0; i < tt.size(); i++)
    {
      ( (User) tt.at(i)).isLoggedIn = false;
    }

    db.getTable(Consts.tbUsers).addTableElement(new EmployeeUser("admin", "letoh", "Dennis Tuvia"), "en", "admin", new DateRepresentation(true));

    f = new File(path + "\\log.dat");
    if(f.exists() && !f.delete())
    {
      System.out.println("Error deleting log");
      System.exit(1);
    }

    try
    {
      logStream = new ObjectOutputStream(new FileOutputStream(path +
          "\\log.dat"));
//      date = new DateRepresentation(true);
//      logWriter = new PrintWriter(new FileOutputStream(path + "\\" + date.toString() + "log.txt", true), true);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

    try
    {
      KeyStore ks = KeyStore.getInstance("JKS");
      ks.load(new FileInputStream("c:\\serverRSCkeysHotel"), "bemologistics".toCharArray());
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, "bemologistics".toCharArray());
      SSLContext sslcontext = SSLContext.getInstance("TLSv1");
      sslcontext.init(kmf.getKeyManagers(), null, null);
      SSLServerSocketFactory ssf = sslcontext.getServerSocketFactory();
      connectionServerSocket = (SSLServerSocket)ssf.createServerSocket(connectionPort);
      incomingServerSocket = (SSLServerSocket)ssf.createServerSocket(incomingPort);
      outgoingServerSocket = (SSLServerSocket)ssf.createServerSocket(outgoingPort);
      dbServerSocket = (SSLServerSocket)ssf.createServerSocket(dbPort);
      updateServerSocket = (SSLServerSocket)ssf.createServerSocket(updatePort);
      abcServerSocket = (SSLServerSocket)ssf.createServerSocket(abcPort);
//      dynamicConnectionServerSocket = (SSLServerSocket)ssf.createServerSocket(dynamicConnectionPort);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    cat = new CheckAnswersThread();
    cat.start();
    StaticThread staticThread = new StaticThread();
    staticThread.start();
/*
    try
    {
      webThread = new WebThread();
      webThread.start();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Failed to start WEB Thread");
    }*/

    RestartThread restartThread = new RestartThread();
    restartThread.start();

    System.out.println("Server is running...");
  }

  public static void load(String path, String[] names, TableElementExample[] elementExamples, long[] firstCodes, int[] howToRead)
  {
    db = new DB(path, names, elementExamples, firstCodes, howToRead);
    db.load();
    incomingClientThreads = new ArrayList();
    outgoingChannels = new ArrayList();
    commander = new Commander();
    canBeRunning = true;
  }

  public static void backupLog(boolean ask)
  {
    File sf = null;
    DateRepresentation dtr = new DateRepresentation(true);
    if(ask)
    {
      JOptionPane.showConfirmDialog(Data.mainFrame, "You must back the log file up", "Warning",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      JFileChooser jf = new JFileChooser();
      sf = new File(jf.getCurrentDirectory().getAbsolutePath() + "\\log" + dtr.toString() + ".zip");
      jf.setSelectedFile(sf);
      int ap = jf.showSaveDialog(Data.mainFrame);
      if (ap != JFileChooser.APPROVE_OPTION)
      {
        backupLog(ask);
        return;
      }
      sf = jf.getSelectedFile();
      int ok = 0;
      if (sf.exists())
        ok = JOptionPane.showConfirmDialog(Data.mainFrame, "File already exists, overwrite?", "Exists",
                                           JOptionPane.YES_NO_OPTION);
      if (ok != 0)
      {
        backup();
        return;
      }
    }
    else
    {
      sf = new File(backupPath + "\\log" + dtr.toString() + ".zip");
      for(int i = 0; sf.exists(); i++)
        sf = new File(backupPath + "\\log" + dtr.toString() + i + ".zip");
    }
    ZipOutputStream zout = null;
    FileInputStream fin = null;
    File f = new File(path + "\\log.dat");
    try
    {
      zout = new ZipOutputStream(new FileOutputStream(sf));
      fin = new FileInputStream(f);
      zout.putNextEntry(new ZipEntry(f.getAbsolutePath()));
      byte[] b = new byte[1024];
      progressBar.setMaximum((int)f.length());
      long n = f.length() / 1024 + 1;
      for(int j = 0; j < n ; j++)
      {
        try
        {
          fin.read(b);
          zout.write(b);
        }
        catch(Exception e)
        {
          break;
        }
        progressBar.setValue(progressBar.getValue() + 1024);
      }
      fin.close();
      zout.closeEntry();
      zout.close();
      progressBar.setValue(0);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void backup()
  {
    DateRepresentation dtr = new DateRepresentation(true);
    JFileChooser jf = new JFileChooser();
    File sf = new File(jf.getCurrentDirectory().getAbsolutePath() + "\\" + dtr.toString() + ".zip");
    jf.setSelectedFile(sf);
    int ap = jf.showSaveDialog(Data.mainFrame);
    if(ap != JFileChooser.APPROVE_OPTION)
      return;
    sf = jf.getSelectedFile();
    int ok = 0;
    if(sf.exists())
      ok = JOptionPane.showConfirmDialog(Data.mainFrame, "File already exists, overwrite?", "Exists", JOptionPane.YES_NO_OPTION);
    if(ok != 0)
    {
      backup();
      return;
    }
    ZipOutputStream zout = null;
    FileInputStream fin = null;
    File f = new File(path);
    try
    {
      zout = new ZipOutputStream(new FileOutputStream(sf));
      long size = 0;
      ArrayList files = new ArrayList();
      File[] lf = f.listFiles();
      for(int i = 0; i < lf.length; i++)
      {
        if(lf[i].isDirectory())
        {
          files.add(lf[i].getAbsolutePath());
          File[] lff = lf[i].listFiles();
          for(int j = 0; j < lff.length; j++)
          {
            files.add(lff[j].getAbsolutePath());
            size += lff[j].length();
          }
        }
        else
        {
          if(lf[i].getAbsolutePath().indexOf("copy") > -1) continue;
          files.add(lf[i].getAbsolutePath());
          size += lf[i].length();
        }
      }
      byte[] b = new byte[1024];
      progressBar.setMaximum((int)size);
      for(int i = 0; i < files.size(); i++)
      {
        File ff = new File(files.get(i).toString());
        if(ff.isDirectory())
        {
//          zout.closeEntry();
          continue;
        }
        ZipEntry ze = new ZipEntry(ff.getAbsolutePath());
        zout.putNextEntry(ze);
        fin = new FileInputStream(ff);
        long length = ff.length();
        long n = length / 1024 + 1;
        for(int j = 0; j < n ; j++)
        {
          try
          {
            fin.read(b);
          }
          catch(EOFException e)
          {
            break;
          }
          zout.write(b);
          progressBar.setValue(progressBar.getValue() + 1024);
        }
        fin.close();
        zout.closeEntry();
      }
      zout.close();
      progressBar.setValue(0);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }

  public static void backup(String backupPath)
  {
    if(backupPath == null || backupPath.equals("")) return;
    DateRepresentation dtr = new DateRepresentation(true);
    File sf = new File(backupPath + "\\" + dtr.toString() + ".zip");
    int add = 0;
    while(sf.exists())
    {
      sf = new File(backupPath + "\\" + dtr.toString() + add + ".zip");
      add++;
    }
    ZipOutputStream zout = null;
    FileInputStream fin = null;
    File f = new File(path);
    try
    {
      zout = new ZipOutputStream(new FileOutputStream(sf));
      long size = 0;
      ArrayList files = new ArrayList();
      File[] lf = f.listFiles();
      for(int i = 0; i < lf.length; i++)
      {
        if(lf[i].isDirectory())
        {
          files.add(lf[i].getAbsolutePath());
          File[] lff = lf[i].listFiles();
          for(int j = 0; j < lff.length; j++)
          {
            files.add(lff[j].getAbsolutePath());
            size += lff[j].length();
          }
        }
        else
        {
          if(lf[i].getAbsolutePath().indexOf("copy") > -1) continue;
          files.add(lf[i].getAbsolutePath());
          size += lf[i].length();
        }
      }
      byte[] b = new byte[1024];
      progressBar.setMaximum((int)size);
      for(int i = 0; i < files.size(); i++)
      {
        File ff = new File(files.get(i).toString());
        if(ff.isDirectory())
        {
//          zout.closeEntry();
          continue;
        }
        ZipEntry ze = new ZipEntry(ff.getAbsolutePath());
        zout.putNextEntry(ze);
        fin = new FileInputStream(ff);
        long length = ff.length();
        long n = length / 1024 + 1;
        for(int j = 0; j < n ; j++)
        {
          try
          {
            fin.read(b);
          }
          catch(EOFException e)
          {
            break;
          }
          zout.write(b);
          progressBar.setValue(progressBar.getValue() + 1024);
        }
        fin.close();
        zout.closeEntry();
      }
      zout.close();
      progressBar.setValue(0);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }

}

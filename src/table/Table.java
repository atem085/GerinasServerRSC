package table;

import utils.MyObjectInputStream;
import utils.MyObjectOutputStream;
import java.util.Arrays;
import java.util.Vector;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.IOException;
import data.*;
import utils.Consts;
import java.util.StringTokenizer;
import utils.Progress;
import utils.MyInputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import utils.DateRepresentation;
import element.ColumnAuthorization;
import utils.MyInputStreamReader;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import element.TableAuthorization;

/**
 * <p>�berschrift: Warehouse Control</p>
 * <p>Beschreibung: Server-Client</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Dennis Tuvia
 * @version 3.0
 */

public class Table extends ArrayList implements Comparable
{

  private String name;
  private TableElementExample example;
  private int howToRead;
  public boolean lastOperationResult;
  private long lastCode;
  private TableElement parentElement;
  private String[] columnNames;
  private boolean lastContains = false;
  private TableElement wasAddTo = null;
  public Table parentTable;

  public Table(String name, TableElementExample example, int howToRead, long firstCode,
               TableElement parentElement, Table parentTable)
  {
    this.name = name;
    this.example = example;
    this.howToRead = howToRead;
    this.lastOperationResult = true;
    this.lastCode = firstCode;
    this.parentElement = parentElement;
    this.parentTable = parentTable;
    this.columnNames = this.example.getColumnNames(howToRead, "en", null);
  }

  public static int compare(String str1, String str2)
  {
    int si1 = 0;
    int si2 = 0;
    boolean ok = true;
    try
    {
      si1 = Integer.parseInt(str1);
    }
    catch(Exception e)
    {
      ok = false;
    }
    if(ok)
      try
      {
        si2 = Integer.parseInt(str2);
      }
      catch(Exception e)
      {
        ok = false;
      }
    if(ok)
      return si1 - si2;
    int weight = 10;
    String s1 = str1.toUpperCase();
    String s2 = str2.toUpperCase();
    if(s1.equals(s2)) return 0;
    int res = 0;
    int min = Math.min(s1.length(), s2.length());
    int max = Math.max(s1.length(), s2.length());
    int current;
    for(int i = 0; i < min; i++)
    {
      if(i > s1.length() - 1)
      {
        if(Character.isDigit(s2.charAt(i)))
          current = - Integer.parseInt("" + s2.charAt(i)) * (int)(Math.pow(weight, (s2.length() - i)));
        else
          current = - s2.charAt(i) * (int)(Math.pow(weight, (s2.length() - i)));
      }
      else if(i > s2.length() - 1)
      {
        if(Character.isDigit(s1.charAt(i)))
          current = Integer.parseInt("" + s1.charAt(i)) * (int)(Math.pow(weight, (s1.length() - i)));
        else
          current = s1.charAt(i) * (int)(Math.pow(weight, (s1.length() - i)));
      }
      else
      {
        if(Character.isDigit(s2.charAt(i)) && Character.isDigit(s1.charAt(i)))
        {
          current = (Integer.parseInt("" + s1.charAt(i)) -
                     Integer.parseInt("" + s2.charAt(i))) *
                     (int)(Math.pow(weight, (s2.length() - i)));
        }
        else
          current = (s1.charAt(i) - s2.charAt(i)) * (int)(Math.pow(weight, (s2.length() - i)));
      }
      res += current;
    }
    return res;
  }

  public int getColumnCount()
  {
    return this.columnNames.length;
  }

  public String getColumnName(int index)
  {
    return this.columnNames[index];
  }

  public Number getVar(int column)
  {
    Class[] columnClasses = this.example.getColumnClasses(this.howToRead);
    if(column >= columnClasses.length || column < 0) return new Integer(0);
    Class c = columnClasses[column];
    ArrayList lst = new ArrayList();
    if(c == String.class)
    {
      for(int i = 0; i < this.size(); i++)
        if(!lst.contains(this.at(i).getValue(column, this.getHowToRead()).toString()))
          lst.add(this.at(i).getValue(column, this.getHowToRead()).toString());
      return new Integer(lst.size());
    }
    if(c == Integer.class)
    {
      for(int i = 0; i < this.size(); i++)
        if(!lst.contains((Integer)this.at(i).getValue(column, this.getHowToRead())))
          lst.add((Integer)this.at(i).getValue(column, this.getHowToRead()));
      return new Integer(lst.size());
    }
    if(c == Long.class)
    {
      for(int i = 0; i < this.size(); i++)
        if(!lst.contains((Long)this.at(i).getValue(column, this.getHowToRead())))
          lst.add((Long)this.at(i).getValue(column, this.getHowToRead()));
      return new Long(lst.size());
    }
    else if(c == Double.class)
    {
      for(int i = 0; i < this.size(); i++)
        if(!lst.contains((Double)this.at(i).getValue(column, this.getHowToRead())))
          lst.add((Double)this.at(i).getValue(column, this.getHowToRead()));
      return new Integer(lst.size());
    }
    return new Integer(0);
  }

  public String executeOperation(Object[] parameters, long selectedCode, int selectedColumn, int operationIndex, String language, String currentUser, DateRepresentation date) throws Exception
  {
    TableElement te = this.at(this.findCode(selectedCode));
    String message = this.example.executeOperation(this.howToRead, operationIndex, selectedCode, selectedColumn, parameters, this, language, currentUser, date);
    if(te != null && selectedCode != te.getCode().longValue())
      this.fixParentTablesInclusive(te);
    int maxHowToRead = this.example.getMaxHowToRead();
    boolean add = false;
    if(te != null)
      for (int j = 1; j <= maxHowToRead; j++)
      {
        if (te.getParentElement(j) == null && te.getParentTable(j) == null)
          continue;
        if (te.getParentTable(j) == null)
        {
          TableElement tet = te.getParentElement(j);
          for (int k = 0; tet.getSubtable(k) != null; k++)
          {
            if (tet.getSubtable(k).getExample().getClass().equals(this.example.
                getClass()))
            {
              if (add)
                tet.getSubtable(k).removeDuplicates(te);
              else
                add = tet.getSubtable(k).removeDuplicatesAddingThem(te);
            }
          }
        }
        else
        {
          if (add)
            te.getParentTable(j).removeDuplicates(te);
          else
            add = te.getParentTable(j).removeDuplicatesAddingThem(te);
        }
      }
    return message;
  }

  private void fillWithDeepestTables(int[] subIndex, Table tds)
  {
    if (this.size() == 0)
      return;
    if (subIndex == null || subIndex.length == 0)
    {
      tds.addAll(this);
      return;
    }
    int[] si = new int[subIndex.length - 1];
    for (int i = 0; i < si.length; i++)
      si[i] = subIndex[i + 1];
    for (int i = 0; i < super.size(); i++)
      this.at(i).getSubtable(subIndex[0]).fillWithDeepestTables(si, tds);
  }

  public Table getReport(int[] subIndex, TableElementExample ex, int hwtr, int reportIndex)
  {
    Table tds = new Table("", ex, hwtr, 0, null, null);
    this.fillWithDeepestTables(subIndex, tds);
    tds.name = Data.reportName[reportIndex];
    return tds;
  }

  private void loadWithProgress(MyObjectInputStream in)
  {
    int size = 0;
    TableElement current;
    try
    {
      this.lastCode = in.readLong();
      size = in.readInt();
//      if(this.name.equals("transportation"))
//        JOptionPane.showConfirmDialog(null, "Size " + size + " " + lastCode);
    }
    catch (Exception e)
    {
      size = 0;
      System.out.println("Error while reading table " + this.name);
      System.out.println("Aborting reading the table");
      e.printStackTrace();
      this.lastOperationResult = false;
      return;
    }
    Data.progress.setMinimum(0);
    Data.progress.setMaximum(size - 1);
    for (int i = 0; i < size; i++)
    {
      Data.progress.setProgressNow(i);
      current = this.example.read(in, this.howToRead, this, parentElement);
//      if(current == null) break;
      if (current == null)
      {
        System.out.println("Error while reading table " + this.name);
        System.out.println("Aborting reading the table " + i);
        this.lastOperationResult = false;
        return;
      }
/*      if(this.contains(current))
      {
        System.out.println(parentElement);
        System.out.println("" + current.getCode().longValue());
        continue;
      }*/
      current.setParentElement(this.parentElement, this.howToRead);
      current.setParentTable(this, this.howToRead);
      super.add(current);
    }
  }

  private void loadWithoutProgress(MyObjectInputStream in)
  {
    int size = 0;
    TableElement current;
    try
    {
      this.lastCode = in.readLong();
      size = in.readInt();
    }
    catch (Exception e)
    {
      size = 0;
      System.out.println("Error while reading table " + this.name);
      System.out.println("Aborting reading the table");
      e.printStackTrace();
      this.lastOperationResult = false;
      return;
    }
    for (int i = 0; i < size; i++)
    {
      current = this.example.read(in, this.howToRead, this, parentElement);
//      if(current == null && this.example instanceof element.ColumnAuthorizationExample) continue;
//      if(current == null && this.example instanceof element.TableAuthorizationExample) continue;
//      if(current == null && this.example instanceof element.ColumnFilter) continue;
//      if(current == null && this.example instanceof element.AusgangExample) continue;
//      if(current == null && this.example instanceof element.SourceDestinationExample) continue;
//      if(current == null && this.example instanceof element.Eingang2Example) continue;
      if (current == null)
      {
//        if(this.parentElement instanceof element.OrderSupplier || this.parentElement instanceof element.Eingang)
//        {
        System.out.println("Error while reading table " + this.name);
        System.out.println("Aborting reading the table " + i);
//          continue;
//        }
        this.lastOperationResult = false;
//        System.out.println(parentElement.toString(1));
//        System.exit(1);
        return;
      }
/*      if(this.contains(current))
      {
        System.out.println(parentElement);
        System.out.println("" + current.getCode().longValue());
        continue;
      }*/
      current.setParentElement(this.parentElement, this.howToRead);
      current.setParentTable(this, this.howToRead);
      super.add(current);
/*
      if(this.example instanceof element.ContactPersonExample && this.parentElement instanceof element.Customer2 && current.getSubtable(0).size() > 0)
      {
        System.out.println("Kommunikation");
        System.out.println("Customer " + this.parentElement.getCode().longValue() + " " + this.parentElement.getValue(1, 1));
        try
        {
          java.io.OutputStreamWriter ow = new java.io.OutputStreamWriter(new java.io.FileOutputStream(
              "c:\\LogisticRSCData\\communication.txt", true), "Unicode");
          ow.write("Customer " + this.parentElement.getCode().longValue() + " " + this.parentElement.getValue(1, 1) + "\r\n");
          for(int j = 0; j < current.getSubtable(0).size(); j++)
            ow.write(current.getSubtable(0).at(j).toString(1) + "\r\n");
          ow.close();
        }
        catch(Exception ee)
        {
          ee.printStackTrace();
        }
      }*/
    }
  }

  public void load(MyObjectInputStream in, boolean showProgress)
  {
    if (showProgress)
      this.loadWithProgress(in);
    else
      this.loadWithoutProgress(in);
     this.sort(-1, true);
  }

  private void fixParentTablesExclusive(TableElement el)
  {
    int maxHowToRead = this.example.getMaxHowToRead();
    for(int i = 1; i <= maxHowToRead; i++)
    {
      if(i == this.howToRead)
        continue;
      if(el.getParentElement(i) == null && el.getParentTable(i) == null) continue;
      if(el.getParentTable(i) == null)
      {
        TableElement te = el.getParentElement(i);
        for(int j = 0; te.getSubtable(j) != null; j++)
        {
          Table stm = te.getSubtable(j);
          if(stm.getExample().getClass().equals(this.example.getClass()))
          {
            stm.sort(-1, true);
            stm.changePath(el);
            break;
          }
        }
      }
      else
      {
        Table ptm = el.getParentTable(i);
        ptm.sort(-1, true);
        ptm.changePath(el);
      }
    }
  }

  private void fixParentTablesInclusive(TableElement el)
  {
    if(el == null) return;
    this.sort(-1, true);
    this.changePath(el);
    int maxHowToRead = this.example.getMaxHowToRead();
    for(int i = 1; i <= maxHowToRead; i++)
    {
      if(i == howToRead) continue;
      if(el.getParentElement(i) == null && el.getParentTable(i) == null) continue;
      if(el.getParentTable(i) == null)
      {
        TableElement te = el.getParentElement(i);
        for(int j = 0; te.getSubtable(j) != null; j++)
        {
          Table stm = te.getSubtable(j);
          if(stm.getExample().getClass().equals(this.example.getClass()))
          {
            stm.sort(-1, true);
            stm.changePath(el);
            break;
          }
        }
      }
      else
      {
        Table ptm = el.getParentTable(i);
        ptm.sort(-1, true);
        ptm.changePath(el);
      }
    }
  }

  public TableElement[] getElements(int sortCriteria, boolean ascending)
  {
    if(sortCriteria == -1 && ascending)
    {
      TableElement[] tes = new TableElement[super.size()];
      for(int i = 0; i < super.size(); i++)
        tes[i] = this.at(i);
      return tes;
    }
    Object[] expressions = this.toArray();
    Arrays.sort(expressions, new BasicComparator(sortCriteria, ascending, this.howToRead));
    TableElement[] tes = new TableElement[expressions.length];
    for(int i = 0; i < expressions.length; i++)
      tes[i] = (TableElement)expressions[i];
    return tes;
  }

  public TableElement[] getElements(int sortCriteria, boolean ascending, int filterColumn, Object filterValue)
  {
    if(sortCriteria == -1 && ascending)
    {
      ArrayList a = new ArrayList();
      for(int i = 0; i < this.size(); i++)
      {
        TableElement current = this.at(i);
        if(current.getValue(filterColumn, this.howToRead).equals(filterValue))
          a.add(current);
      }
      TableElement[] tes = new TableElement[a.size()];
      for(int i = 0; i < a.size(); i++)
        tes[i] = (TableElement)a.get(i);
      return tes;
    }
    Object[] expressions = this.toArray();
    Arrays.sort(expressions, new BasicComparator(sortCriteria, ascending, this.howToRead));
    ArrayList a = new ArrayList();
    for(int i = 0; i < expressions.length; i++)
    {
      TableElement current = (TableElement)expressions[i];
      if(current.getValue(filterColumn, this.howToRead).equals(filterValue))
        a.add(current);
    }
    TableElement[] tes = new TableElement[a.size()];
    for(int i = 0; i < a.size(); i++)
      tes[i] = (TableElement)a.get(i);
    return tes;
  }

  public int getHowToRead()
  {
    return this.howToRead;
  }

/*  public Thread myImport(File file, boolean ts)
  {
    final File f = file;
    final boolean tss = ts;
    Data.progressBar.setMaximum( (int) file.length());
    Data.progressBar.setValue(0);
    Thread t = new Thread()
    {
      public void run()
      {
        DateRepresentation dtr1 = new DateRepresentation();
        System.out.println("Importing " + name + "...");
        MyInputStreamReader bfr = null;
        try
        {
          bfr = new MyInputStreamReader(new FileInputStream(f), "Unicode");
        }
        catch (Exception e)
        {
          System.out.println("Error openning file");
          return;
        }
        long pr = 0;
        StringTokenizer st;
        Object[] currentParameters = null;
        String line = null;
        try
        {
          line = bfr.readLine();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.out.println("Nothing to import!");
          try
          {
            bfr.close();
          }
          catch (Exception ee)
          {}
          return;
        }
        st = new StringTokenizer(line, ";");
        currentParameters = new Object[st.countTokens()];
        int readRows = 0;
        int previousSize = size();
        try
        {
          line = bfr.readLine();
          readRows++;
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.out.println("Nothing to import!");
          try
          {
            bfr.close();
          }
          catch (Exception ee)
          {}
          return;
        }
        String tk;
        while (line != null)
        {
          pr += line.getBytes().length;
          Data.progressBar.setValue( (int) pr);
          st = new StringTokenizer(line, ";", true);
          for (int i = 0; i < currentParameters.length; i++)
            currentParameters[i] = "";
          for (int i = 0; i < currentParameters.length; i++)
          {
            if (st.hasMoreTokens())
              tk = st.nextToken();
            else
              break;
            if (tk.equals(";"))
              continue;
            currentParameters[i] = tk;
            if (st.hasMoreTokens())
              st.nextToken();
            else
              break;
          }
          String message = addTableElement(example.newInstance(currentParameters, howToRead, null), "en");
          if (lastOperationResult)
            Data.version++;
          else if (tss)
            System.out.println("Row " + readRows + " " + message + " " + line);
          try
          {
            line = bfr.readLine();
            readRows++;
          }
          catch (Exception e)
          {
            line = null;
          }
        }
        Data.progressBar.setValue(0);
        System.out.println("Finished importing");
        System.out.println("Previous size " + previousSize);
        System.out.println("Current size " + size());
        System.out.println("Rows read " + readRows);
        DateRepresentation dtr2 = new DateRepresentation();
        System.out.println("Time taken " + (dtr2.getTimeInMillis() - dtr1.getTimeInMillis()));
        try
        {
          bfr.close();
        }
        catch (Exception ee)
        {}
      }
    };
    return t;
  }
*/

public Vector simulateMyImport(MyInputStreamReader in, int[] selectedColumns, String line, long size, String sign)
{
  String message;
  TableElement te;
  int[] initialParametersColumnIndexes = this.example.getInitialParametersColumnIndices(this.getHowToRead());
  int[] accordance = new int[initialParametersColumnIndexes.length];
  for(int i = 0, j = 0; i < selectedColumns.length; i++)
  {
    if(selectedColumns[i] == -1) continue;
    accordance[j] = DB.getPosOfDependingIndex(initialParametersColumnIndexes, selectedColumns[i]);
    j++;
  }
  Vector notImportedRows = new Vector();
  StringTokenizer st;
  st = new StringTokenizer(line, sign);
  boolean wasImported = true;
  String[] currentStringParameters = new String[selectedColumns.length];
  for(int i = 0; i < selectedColumns.length; i++)
    currentStringParameters[i] = st.nextToken();
  Object[] currentParameters = new Object[initialParametersColumnIndexes.length];
  Class[] columnClasses = this.example.getColumnClasses(this.getHowToRead());
  for(int i = 0, j = 0; i < selectedColumns.length; i++)
  {
    if(selectedColumns[i] == -1) continue;
    Class c = columnClasses[selectedColumns[i]];
    if(c == String.class)
      currentParameters[accordance[j]] = currentStringParameters[i];
    else if(c == Integer.class)
      try
      {
        currentParameters[accordance[j]] = new Integer(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Long.class)
      try
      {
        currentParameters[accordance[j]] = new Long(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Double.class)
      try
      {
        currentStringParameters[i] = currentStringParameters[i].replaceAll(",", ".");
        currentParameters[accordance[j]] = new Double(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Float.class)
      try
      {
        currentStringParameters[i] = currentStringParameters[i].replaceAll(",", ".");
        currentParameters[accordance[j]] = new Float(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Boolean.class)
      try
      {
        currentParameters[accordance[j]] = new Boolean(currentStringParameters[i]);
      }
      catch(Exception e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    j++;
  }
/*    if(wasImported)
    this.addRow(currentParameters, Data.language);
  if(!this.dataSet.lastOperationResult)
    notImportedRows.add(new Integer(1));*/
  try
  {
    line = in.readLine();
  }
  catch(Exception e)
  {
    line = null;
  }
  int k = 2;
  while(line != null)
  {
    Data.progressBar.setValue(Data.progressBar.getValue() + line.getBytes().length);
    st = new StringTokenizer(line, sign, true);
    wasImported = true;
    message = "";
    for(int i = 0; i < selectedColumns.length; i++)
    {
      if(selectedColumns[i] == -1)
      {
        try
        {
          if(!st.nextToken().equals(sign))
            st.nextToken();
        }
        catch(Exception e) {}
        continue;
      }
      Class c = columnClasses[selectedColumns[i]];
      if(!st.hasMoreTokens())
      {
        if(c == Integer.class || c == Long.class || c == Double.class)
          currentStringParameters[i] = "0";
        else if(c == Boolean.class)
          currentStringParameters[i] = "false";
        else
          currentStringParameters[i] = "";
        continue;
      }
      String tk = sign;
      try
      {
        tk = st.nextToken();
      }
      catch(Exception e) {}
      if(tk.equals(sign))
      {
        if(c == Integer.class || c == Long.class || c == Double.class)
          currentStringParameters[i] = "0";
        else if(c == Boolean.class)
          currentStringParameters[i] = "false";
        else
          currentStringParameters[i] = "";
        continue;
      }
      currentStringParameters[i] = tk;
      try
      {
        if(i != selectedColumns.length - 1)
          st.nextToken();
      }
      catch(Exception e) {}
    }
    for(int i = 0, j = 0; i < selectedColumns.length; i++)
    {
      if(selectedColumns[i] == -1) continue;
      Class c = columnClasses[selectedColumns[i]];
      if(c == String.class)
        currentParameters[accordance[j]] = currentStringParameters[i];
      else if(c == Integer.class)
        try
        {
          currentParameters[accordance[j]] = new Integer(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Long.class)
        try
        {
          currentParameters[accordance[j]] = new Long(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Double.class)
        try
        {
          currentParameters[accordance[j]] = new Double(currentStringParameters[i].replaceAll(",", "."));
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Float.class)
        try
        {
          currentParameters[accordance[j]] = new Float(currentStringParameters[i].replaceAll(",", "."));
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Boolean.class)
        try
        {
          currentParameters[accordance[j]] = new Boolean(currentStringParameters[i]);
        }
        catch(Exception e)
        {
          message += e.toString();
          wasImported = false;
        }
      j++;
    }
    if(wasImported)
    {
      te = this.getExample().newInstance(currentParameters, this.getHowToRead(), this.getParentElement());
      message = this.tryToAddTableElement(te);
      if(!this.lastOperationResult)
        notImportedRows.add("" + k + " " + message + " " + line);
    }
    else
      notImportedRows.add("" + k + " " + message + " " + line);
    k++;
    try
    {
      line = in.readLine();
    }
    catch(Exception e)
    {
      line = null;
    }
  }
  return notImportedRows;
}

public Vector myImport(MyInputStreamReader in, PrintWriter out, int[] selectedColumns, String line, long size, boolean clear, String sign, String currentUser, DateRepresentation date)
{
  String message;
  int[] initialParametersColumnIndexes = this.example.getInitialParametersColumnIndices(this.getHowToRead());
  int[] accordance = new int[initialParametersColumnIndexes.length];
  for(int i = 0, j = 0; i < selectedColumns.length; i++)
  {
    if(selectedColumns[i] == -1) continue;
    accordance[j] = DB.getPosOfDependingIndex(initialParametersColumnIndexes, selectedColumns[i]);
    j++;
  }
  Vector notImportedRows = new Vector();
  StringTokenizer st;
  st = new StringTokenizer(line, sign);
  boolean wasImported = true;
  String[] currentStringParameters = new String[selectedColumns.length];
  for(int i = 0; i < selectedColumns.length; i++)
    currentStringParameters[i] = st.nextToken();
  Object[] currentParameters = new Object[initialParametersColumnIndexes.length];
  Class[] columnClasses = this.example.getColumnClasses(this.getHowToRead());
  for(int i = 0, j = 0; i < selectedColumns.length; i++)
  {
    if(selectedColumns[i] == -1) continue;
    Class c = columnClasses[selectedColumns[i]];
    if(c == String.class)
      currentParameters[accordance[j]] = currentStringParameters[i];
    else if(c == Integer.class)
      try
      {
        currentParameters[accordance[j]] = new Integer(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Long.class)
      try
      {
        currentParameters[accordance[j]] = new Long(currentStringParameters[i]);
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Double.class)
      try
      {
        currentParameters[accordance[j]] = new Double(currentStringParameters[i].replaceAll(",", "."));
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Float.class)
      try
      {
        currentParameters[accordance[j]] = new Float(currentStringParameters[i].replaceAll(",", "."));
      }
      catch(NumberFormatException e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    else if(c == Boolean.class)
      try
      {
        currentParameters[accordance[j]] = new Boolean(currentStringParameters[i]);
      }
      catch(Exception e)
      {
//          notImportedRows.add(new Integer(1));
        wasImported = false;
      }
    j++;
  }
/*    if(wasImported)
    this.addRow(currentParameters, Data.language);
  if(!this.dataSet.lastOperationResult)
    notImportedRows.add(new Integer(1));*/
  String header = line;
  try
  {
    line = in.readLine();
  }
  catch(Exception e)
  {
    line = null;
  }
  int k = 2;
  while(line != null)
  {
    Data.progressBar.setValue(Data.progressBar.getValue() + line.getBytes().length);
    st = new StringTokenizer(line, sign, true);
    wasImported = true;
    message = "";
    for(int i = 0; i < selectedColumns.length; i++)
    {
      if(selectedColumns[i] == -1)
      {
        try
        {
          if(!st.nextToken().equals(sign))
            st.nextToken();
        }
        catch(Exception e) {}
        continue;
      }
      Class c = columnClasses[selectedColumns[i]];
      if(!st.hasMoreTokens())
      {
        if(c == Integer.class || c == Long.class || c == Double.class)
          currentStringParameters[i] = "0";
        else if(c == Boolean.class)
          currentStringParameters[i] = "false";
        else
          currentStringParameters[i] = "";
        continue;
      }
      String tk = sign;
      try
      {
        tk = st.nextToken();
      }
      catch(Exception e) {}
      if(tk.equals(sign))
      {
        if(c == Integer.class || c == Long.class || c == Double.class)
          currentStringParameters[i] = "0";
        else if(c == Boolean.class)
          currentStringParameters[i] = "false";
        else
          currentStringParameters[i] = "";
        continue;
      }
      currentStringParameters[i] = tk;
      try
      {
        if(i != selectedColumns.length - 1)
          st.nextToken();
      }
      catch(Exception e) {}
    }
    for(int i = 0, j = 0; i < selectedColumns.length; i++)
    {
      if(selectedColumns[i] == -1) continue;
      Class c = columnClasses[selectedColumns[i]];
      if(c == String.class)
      {
        currentParameters[accordance[j]] = currentStringParameters[i];
      }
      else if(c == Integer.class)
        try
        {
          currentParameters[accordance[j]] = new Integer(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Long.class)
        try
        {
          currentParameters[accordance[j]] = new Long(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Double.class)
        try
        {
          currentStringParameters[i] = currentStringParameters[i].replaceAll(",", ".");
          currentParameters[accordance[j]] = new Double(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Float.class)
        try
        {
          currentStringParameters[i] = currentStringParameters[i].replaceAll(",", ".");
          currentParameters[accordance[j]] = new Float(currentStringParameters[i]);
        }
        catch(NumberFormatException e)
        {
          message += e.toString();
          wasImported = false;
        }
      else if(c == Boolean.class)
        try
        {
          currentParameters[accordance[j]] = new Boolean(currentStringParameters[i]);
        }
        catch(Exception e)
        {
          message += e.toString();
          wasImported = false;
        }
      j++;
    }
    if(wasImported)
    {
      TableElement toadd = null;
      try
      {
        toadd = this.example.newInstance(currentParameters, this.getHowToRead(), this.parentElement);
      }
      catch(Exception eee)
      {
        message = eee.toString();
        this.lastOperationResult = false;
      }
      if(toadd != null)
        message = this.addTableElement(toadd, "en", currentUser, date);
      if(!this.lastOperationResult)
      {
        notImportedRows.add("" + k + " " + message + " " + line);
        if(notImportedRows.size() == 1)
          out.println(header);
        out.println(line);
      }
    }
    else
    {
      notImportedRows.add("" + k + " " + message + " " + line);
      if(notImportedRows.size() == 1)
        out.println(header);
      out.println(line);
    }
    k++;
    try
    {
      line = in.readLine();
    }
    catch(Exception e)
    {
      line = null;
    }
  }
  return notImportedRows;
}

protected String tryToAddTableElement(TableElement el)
{
  boolean ok = true;
  try
  {
    el.toString(this.howToRead);
  }
  catch(Exception e)
  {
    try
    {
      el.setParentElement(this.parentElement, this.howToRead);
      el.setParentTable(this, this.howToRead);
      el.toString(this.howToRead);
    }
    catch(Exception e1)
    {
      e1.printStackTrace();
      ok = false;
    }
  }

  if(el == null || !ok)
  {
    this.lastOperationResult = false;
    return "Error adding element";
  }
  if(this.contains(el))
  {
    int ca = this.example.getAmountColumnIndex(this.howToRead);
    if(ca < 0)
    {
      this.lastOperationResult = false;
      return "Position already exists";
    }
  }
  this.lastOperationResult = true;
  return "";
}


/*  public void setHowToRead(int howToRead)
  {
    this.howToRead = howToRead;
    if (this.parentElement != null)
      this.name += " " + this.parentElement.getCode().longValue();
    if (this.parentElement != null)
      this.changePath(this.parentElement);
    this.sort( -1, true);
  }
*/
  public TableElement getParentElement()
  {
    return this.parentElement;
  }

  public void write(MyObjectOutputStream out) throws Exception
  {
    out.writeLong(this.lastCode);
    out.writeInt(this.size());
    for (int i = 0; i < super.size(); i++)
    {
      this.at(i).write(out, this.howToRead);
    }
  }

  public void removeAllRemovable()
  {
    Data.progressBar.setMinimum(0);
    Data.progressBar.setMaximum(super.size());
    System.out.println("Current size = " + super.size());
    for (int i = 0; i < super.size(); i++)
    {
      Data.progressBar.setValue(i);
      if (this.at(i).isRemovable(this.getHowToRead()))
      {
        super.remove(i);
        i--;
      }
    }
    System.out.println("Finished");
  }

  public void save(MyObjectOutputStream out, boolean progress) throws Exception
  {
    if(progress)
      this.saveWithProgress(out);
    else
      this.saveWithoutProgress(out);
  }

  private void saveWithProgress(MyObjectOutputStream out) throws Exception
  {
    out.writeLong(this.lastCode);
    out.writeInt(this.size());
    Data.progress.setMinimum(0);
    Data.progress.setMaximum(this.size() - 1);
    for (int i = 0; i < super.size(); i++)
    {
      Data.progress.setProgressNow(i);
      this.at(i).write(out, this.howToRead);
    }
  }

  private void saveWithoutProgress(MyObjectOutputStream out) throws Exception
  {
    out.writeLong(this.lastCode);
    out.writeInt(this.size());
    for (int i = 0; i < super.size(); i++)
    {
      this.at(i).write(out, this.howToRead);
    }
  }

  public boolean hasValue(int column, Object value)
  {
    for (int i = 0; i < super.size(); i++)
      if (this.at(i).getValue(column, this.howToRead).equals(value))
        return true;
    return false;
  }

  public int compareTo(Table dataSet)
  {
    return this.name.compareTo(dataSet.name);
  }

  public int compareTo(Object o)
  {
    return this.compareTo( (Table) o);
  }

  public boolean equals(Table dataSet)
  {
    return this.name.equals(dataSet.name);
  }

  public Thread handleImportSimulateEvent(MyInputStreamReader fin, int[] selectedColumns, String line, long size, String sign)
  {
    final MyInputStreamReader finn = fin;
    final int[] sc = selectedColumns;
    final String ln = line;
    final long sz = size;
    final String si = sign;
    Thread thread = new Thread()
    {
      public void run()
      {
        Data.mainFrame.setEnabled(false);
        DateRepresentation dt = new DateRepresentation(true);
        Vector notImportedRows = null;
        try
        {
          notImportedRows = simulateMyImport(finn, sc, ln, sz, si);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          Data.mainFrame.setEnabled(true);
        }
        Data.mainFrame.setEnabled(true);
        DateRepresentation dt2 = new DateRepresentation(true);
        JOptionPane.showConfirmDialog(Data.mainFrame,
                                      "Time taken " + (dt2.getTimeInMillis() - dt.getTimeInMillis()), "Time",
                                      JOptionPane.OK_OPTION);
        if(notImportedRows.size() > 0)
          JOptionPane.showInputDialog(Data.mainFrame, "Following rows could not be imported", "Warning", JOptionPane.WARNING_MESSAGE, null, notImportedRows.toArray(), null);
        Data.progressBar.setValue(0);
        try
        {
          finn.close();
        }
        catch(Exception eee)
        {
          eee.printStackTrace();
        }
        notImportedRows = null;
      }
    };
    return thread;
  }

  public Thread handleImportEvent(MyInputStreamReader fin, PrintWriter fout, int[] selectedColumns, String line,
                                  long size, boolean clear, String sign, final String currentUser, final DateRepresentation date)
  {
    final MyInputStreamReader finn = fin;
    final PrintWriter foutt = fout;
    final int[] sc = selectedColumns;
    final String ln = line;
    final long sz = size;
    final boolean cl = clear;
    final String si = sign;
    Thread thread = new Thread()
    {
      public void run()
      {
        DateRepresentation dt = new DateRepresentation(true);
        Data.mainFrame.setEnabled(false);
        Vector notImportedRows = null;
        try
        {
          notImportedRows = myImport(finn, foutt, sc, ln, sz, cl, si, currentUser, date);
        }
        catch(Exception ee)
        {
          ee.printStackTrace();
          Data.mainFrame.setEnabled(true);
        }
        Data.mainFrame.setEnabled(true);
        DateRepresentation dt2 = new DateRepresentation(true);
//        JOptionPane.showConfirmDialog(Data.mainFrame, "Time taken " + (dt2.getTimeInMillis() - dt.getTimeInMillis()), "Time", JOptionPane.OK_OPTION);
        if(notImportedRows.size() > 0)
          JOptionPane.showInputDialog(Data.mainFrame, "Following rows could not be imported", "Warning", JOptionPane.WARNING_MESSAGE, null, notImportedRows.toArray(), null);
        Data.progressBar.setValue(0);
        try
        {
          finn.close();
          if(notImportedRows.size() > 0)
            foutt.close();
        }
        catch(Exception eee)
        {
          eee.printStackTrace();
        }
        notImportedRows = null;
      }
    };
    return thread;
  }

  public boolean equals(Object o)
  {
    try
    {
      return this.equals( (Table) o);
    }
    catch (ClassCastException e)
    {
      return super.equals(o);
    }
  }

  public Number getSum(int column)
  {
    Class[] columnClasses = this.getExample().getColumnClasses(this.getHowToRead());
    if(column >= columnClasses.length || column < 0) return new Double(0);
    Class c = columnClasses[column];
    if(c == String.class) return new Double(0);
    if(c == Integer.class)
    {
      int sum = 0;
      for(int i = 0; i < this.size(); i++)
        sum += ((Integer)this.at(i).getValue(column, this.getHowToRead())).intValue();
      return new Integer(sum);
    }
    if(c == Long.class)
    {
      long sum = 0;
      for(int i = 0; i < this.size(); i++)
        sum += ((Long)this.at(i).getValue(column, this.getHowToRead())).longValue();
      return new Long(sum);
    }
    else if(c == Double.class)
    {
      double sum = 0;
      for(int i = 0; i < this.size(); i++)
        sum += ((Double)this.at(i).getValue(column, this.getHowToRead())).doubleValue();
      return new Double(sum);
    }
    return new Double(0);
  }

  public Number getMax(int column)
  {
    if (column >= this.getColumnCount() || column < 0)
      return new Integer(0);
    Class c = this.example.getColumnClasses(this.howToRead)[column];
    if (c == String.class)
      return new Integer(0);
    if (c == Integer.class)
    {
      if (this.size() == 0)
        return new Integer(0);
      int max = - (Integer.MAX_VALUE - 1);
      for (int i = 0; i < this.size(); i++)
        if ( ( (Integer)this.at(i).getValue(column, this.getHowToRead())).intValue() > max)
          max = ( (Integer)this.at(i).getValue(column, this.getHowToRead())).intValue();
      return new Integer(max);
    }
    if (c == Long.class)
    {
      if (this.size() == 0)
        return new Long(0);
      long max = - (Long.MAX_VALUE - 1);
      for (int i = 0; i < this.size(); i++)
        if ( ( (Long)this.at(i).getValue(column, this.getHowToRead())).longValue() > max)
          max = ( (Long)this.at(i).getValue(column, this.getHowToRead())).longValue();
      return new Long(max);
    }
    if (c == Double.class)
    {
      if (this.size() == 0)
        return new Double(0);
      double max = 0;
      for (int i = 0; i < this.size(); i++)
        if ( ( (Double)this.at(i).getValue(column, this.getHowToRead())).doubleValue() > max)
          max = ( (Double)this.at(i).getValue(column, this.getHowToRead())).doubleValue();
      return new Double(max);
    }
    return new Integer(0);
  }

  public TableElementExample getExample()
  {
    return this.example;
  }

  public String getName()
  {
    return this.name;
  }

  private void changeTableName(TableElement el, long newCode)
  {
    for(int i = 0; this.example.hasSubtable(howToRead, i); i++)
    {
      if(el.getSubtable(i) == null) continue;
      String n = el.getSubtable(i).name;
      int p = n.lastIndexOf(" ");
      if(p < 0)
        p = n.length();
      if(p == n.length())
        n = n.substring(0, p) + " ";
      else
        n = n.substring(0, p + 1);
      n += newCode;
      el.getSubtable(i).name = n;
      for(int j = 0; j < el.getSubtable(i).size(); j++)
        el.getSubtable(i).changeTableName(el.getSubtable(i).at(j), newCode);
    }
  }

  public String addTableElement(TableElement el, String language, String currentUser, DateRepresentation date)
  {
    if (el == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsNoAddElem(language);
    }
    long code = el.getCode().longValue();
    el.setParentElement(this.parentElement, this.howToRead);
    el.setParentTable(this, this.howToRead);
    if(this.example.isAutoIncrementable(this.howToRead))
    {
      if(this.size() > 0)
       el.setCode(this.at(this.size() - 1).getCode().longValue() + 1);
      else el.setCode(this.lastCode);
      this.changeTableName(el, el.getCode().longValue());
    }
    this.changePath(el);
    if (this.contains(el))
    {
      int ca = this.example.getAmountColumnIndex(this.howToRead);
      if (ca < 0)
      {
        this.lastOperationResult = false;
        return Consts.ermsExistsPos(language);
      }
      else
      {
        TableElement te = this.at(this.findCode(el.getCode().longValue()));
        this.wasAddTo = te;
        String message = "";
        try
        {
          message = te.setValue(ca,
                                new Integer( ( (Integer) te.getValue(ca, this.howToRead)).intValue() +
                                            ( (Integer) el.getValue(ca, this.howToRead)).
                                            intValue()), language, this.howToRead, currentUser, date );
        }
        catch (Exception e)
        {
          message = e.toString();
          this.lastOperationResult = false;
          return message;
        }
        this.lastOperationResult = true;
        return Consts.ermsAddTableElementPos(language) + message;
      }
    }
    int pos = this.findPos(el.getCode().longValue());
    super.add(pos, el);
    this.lastOperationResult = true;
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    code = el.getCode().longValue();
    String ms = el.wasAdd(this.howToRead, this.parentElement, currentUser, date);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    return ms;
  }

  public void addTableElementSimple(TableElement el)
  {
    int pos = this.findPos(el.getCode().longValue());
    super.add(pos, el);
    this.lastOperationResult = true;
  }

  private void changePath(TableElement el)
  {
    Table subTable;
    TableElement subElement;
    long code;
    boolean needsSort = false;
    TableElement parentElement = null;
    Table parentTable = null;
    for (int i = 0; this.example.hasSubtable(this.howToRead, i); i++)
    {
      subTable = el.getSubtable(i);
      if(subTable == null) continue;
      Table subTableExample = this.example.getSubtable(this.howToRead, i);
      if(subTable.howToRead != subTableExample.getHowToRead())
      {
        subTable.howToRead = subTableExample.getHowToRead();
        subTable.name += " moved";
        subTable.example = subTableExample.getExample();
        subTable.columnNames = subTable.example.getColumnNames(subTable.getHowToRead(), "en", null);
      }
      for (int j = 0; j < subTable.size(); j++)
      {
        subElement = subTable.at(j);
  /*      if(i == 0 && j == 0)
        {
          parentElement = subElement.getParentElement(subTable.getHowToRead());
          parentTable = subElement.getParentTable(subTable.getHowToRead());
        }*/
        code = subElement.getCode().longValue();
        subElement.setParentElement(el, subTable.getHowToRead());
        subElement.setParentTable(subTable, subTable.getHowToRead());
//        if(i == 0 && j == 0 && parentElement == subElement.getParentElement(subTable.getHowToRead()) && parentTable == subElement.getParentTable(subTable.getHowToRead()))
//           return;
        if(code != subElement.getCode().longValue())
          needsSort = true;
        subTable.changePath(subElement);
      }
      if(needsSort)
      {
//        el.getSubtable(0).fixParentTablesInclusive(el);
        subTable.sort(-1, true);
        needsSort = false;
      }
    }
  }

  public String addTableElementComplete(TableElement el, String language, String currentUser, DateRepresentation date)
  {
    if (el == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsErrorAddElem(language);
    }
    long code = el.getCode().longValue();
    el.setParentElement(this.parentElement, this.howToRead);
    el.setParentTable(this, this.howToRead);
    if(this.example.isAutoIncrementable(this.howToRead))
    {
      if(this.size() > 0)
       el.setCode(this.at(this.size() - 1).getCode().longValue() + 1);
      else el.setCode(this.lastCode);
      this.changeTableName(el, el.getCode().longValue());
    }
    this.changePath(el);
    if (this.contains(el))
    {
      int ca = this.example.getAmountColumnIndex(this.howToRead);
      if (ca < 0)
      {
        this.lastOperationResult = false;
        return Consts.ermsExistsPos(language);
      }
      else
      {
        TableElement te = this.at(this.findCode(el.getCode().longValue()));
        this.wasAddTo = te;
        String message = "";
        try
        {
          message = te.setValue(ca,
                                new Integer( ( (Integer) te.getValue(ca, this.howToRead)).intValue() +
                                            ( (Integer) el.getValue(ca, this.howToRead)).
                                            intValue()), language, this.howToRead, currentUser, date);
        }
        catch (Exception e)
        {
          this.lastOperationResult = false;
          message = e.toString();
          return message;
        }
        int maxHowToRead = this.example.getMaxHowToRead();
        for (int i = 1; i <= maxHowToRead; i++)
        {
          if (el.getParentElement(i) == null && el.getParentTable(i) == null)
            continue;
          if (el.getParentTable(i) == null)
          {
            TableElement pp = el.getParentElement(i);
            for (int j = 0; pp.getSubtable(j) != null; j++)
              pp.getSubtable(j).removeElementByReference(el);
          }
          else
            el.getParentTable(i).removeElementByReference(el);
        }
        this.lastOperationResult = true;
        return Consts.ermsAddTableElementPos(language) + message;
      }
    }
    int position = this.findPos(el.getCode().longValue());
    super.add(position, el);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    code = el.getCode().longValue();
    String message = el.wasAdd(this.howToRead, this.parentElement, currentUser, date);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    int maxHowToRead = this.example.getMaxHowToRead();
    for (int i = 1; i <= maxHowToRead; i++)
    {
      if (i == this.howToRead)
        continue;
      if (el.getParentElement(i) == null && el.getParentTable(i) == null)
        continue;
      if (el.getParentTable(i) == null)
      {
        TableElement te = el.getParentElement(i);
        for (int j = 0; te.getSubtable(j) != null; j++)
        {
          if (te.getSubtable(j).getExample().getClass().equals(this.example.
              getClass()) &&
              te.getSubtable(j).findCode(el.getCode().longValue()) < 0)
          {
            te.getSubtable(j).addTableElementSimple(el);
            break;
          }
        }
      }
      else
      {
        if (el.getParentTable(i).findCode(el.getCode().longValue()) < 0)
        {
          el.getParentTable(i).addTableElementSimple(el);
        }
      }
    }
    this.lastOperationResult = true;
    return message;
  }

  private boolean isFilterSet(TableAuthorization ta)
  {
    for(int i = 0; i < ta.columnSettings.size(); i++)
    {
      if (!this.getExample().isSaved(i, this.getHowToRead()))
        continue;
      Table tm = ta.columnSettings.at(i).getSubtable(0);
      if (tm.size() > 0)
        return true;
    }
    return false;
  }

  public Table filteredByDefault(TableAuthorization ta, String tn, int userType)
  {
    int[] filteredIndices = this.getFilteredIndicesByDefault(ta, tn);
    Table t = new Table(this.name, this.example, this.howToRead, 0, this.parentElement, this.parentTable);
    int j = 0;
//    System.out.println("filtered Indices " + filteredIndices.length);
//    for(int i = 0; i < filteredIndices.length; i++)
//      System.out.println("" + i + " " + filteredIndices[i]);
    for(int i = 0; i < this.size(); i++)
    {
      if(j < filteredIndices.length && i == filteredIndices[j])
      {
        j++;
        continue;
      }
//      System.out.println("FILTER SET " + this.isFilterSet(ta));
      if(this.isFilterSet(ta))
        t.add(this.at(i));
      else if(this.isAuthorizedSubtables(this.at(i), ta, userType))
       t.add(this.at(i));
    }
    return t;
  }

  private int[] getFilteredIndicesByDefault(TableAuthorization ta, String tn)
  {
    if(ta == null) return new int[0];
    ArrayList filteredIndices = new ArrayList();
    for(int i = 0; i < ta.columnSettings.size(); i++)
    {
      if(!this.getExample().isSaved(i, this.getHowToRead())) continue;
      Table tm = ta.columnSettings.at(i).getSubtable(0);
      if(tm.size() == 0) continue;
//      System.out.println("Checking column " + i);
      for(int j = 0; j < this.size(); j++)
      {
        TableElement current = this.at(j);
//        System.out.println(current.getValue(i, this.getHowToRead()) + " " + tm.at(0).getValue(0, 1));
//        System.out.println("has value " + tm.hasValue(0, current.getValue(i, this.getHowToRead())));
/*        if(i == 4)
        {
          System.out.println(current.getValue(i, this.getHowToRead()) + " " + tm.at(0).getValue(0, 1));
          System.out.println("has value " + tm.hasValue(0, current.getValue(i, this.getHowToRead())));
        }*/
        if(tm.hasValue(0, current.getValue(i, this.getHowToRead()))) continue;
//        System.out.println("Adding index because of column " + i + " tm value " + tm.at(0).getValue(0, 1));
        Integer toFilter = new Integer(j);
        if(!filteredIndices.contains(toFilter))
          filteredIndices.add(toFilter);
      }
    }
//    System.out.println("Filtered indices size " + tn + " " + filteredIndices.size());
    if(!this.getExample().hasSubtable(this.getHowToRead(), 0))
    {
      int[] indices = new int[filteredIndices.size()];
      for(int i = 0; i < indices.length; i++)
        indices[i] = ((Integer)filteredIndices.get(i)).intValue();
      Arrays.sort(indices);
      return indices;
    }
    ArrayList[] subTableFilteredIndices = new ArrayList[this.size()];
    boolean[] isFilterSet = new boolean[this.size()];
    for (int j = 0; j < this.size(); j++)
    {
      subTableFilteredIndices[j] = new ArrayList();
      for(int i = 0; this.getExample().hasSubtable(this.getHowToRead(), i); i++)
      {
        String tn2 = i + "." + tn;
        try
        {
          Table tm = this.at(j).getSubtable(i);
          if (tm == null)
            continue;
          TableAuthorization ta2 = (TableAuthorization) ta.getParentTable(1).findElement(Table.dtJSHashCode(
              tn2));
          subTableFilteredIndices[j].add(tm.getFilteredIndicesByDefault(ta2, tn2));
          isFilterSet[j] = tm.isFilterSet(ta2);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    boolean wasAdd = false;
    for(int j = 0; j < this.size(); j++)
    {
      TableElement current = this.at(j);
      boolean toFilter = true;
      ArrayList currentElementSubtableIndices = subTableFilteredIndices[j];
      for (int i = 0; this.getExample().hasSubtable(this.getHowToRead(), i); i++)
      {
        Table cm = current.getSubtable(i);
        if(cm == null) continue;
        if(cm.size() == 0)
        {
          if(!isFilterSet[j])
            toFilter = false;
        }
        else
        {
          int[] currentSubTableFilteredIndices = (int[])currentElementSubtableIndices.get(i);
          if(currentSubTableFilteredIndices.length < cm.size())
            toFilter = false;
        }
      }
      if(toFilter)
      {
        Integer kj = new Integer(j);
        if(!filteredIndices.contains(kj))
        {
          filteredIndices.add(kj);
          wasAdd = true;
        }
      }
    }
    int[] indices = new int[filteredIndices.size()];
    for(int i = 0; i < indices.length; i++)
      indices[i] = ((Integer)filteredIndices.get(i)).intValue();
//    if(wasAdd)
      Arrays.sort(indices);
    return indices;
  }

  public Table filteredByDefault(TableAuthorization ta, String tn)
  {
    int[] filteredIndices = this.getFilteredIndicesByDefault(ta, tn);
    Table t = new Table(this.name, this.example, this.howToRead, 0, this.parentElement, this.parentTable);
    int j = 0;
    for(int i = 0; i < this.size(); i++)
    {
      if(j < filteredIndices.length && i == filteredIndices[j])
      {
        j++;
        continue;
      }
      t.add(this.at(i));
    }
    return t;
  }

  protected long getTC(int subtableIndex)
  {
    String nn = this.name;
    nn = "" + subtableIndex + "." + nn;
    return Table.dtJSHashCode(nn);
  }

  protected boolean isAuthorizedSubtables(TableElement el, TableAuthorization uta, int userType)
  {
    if(!this.example.hasSubtable(this.howToRead, 0)) return true;
    boolean aef = true;
    boolean ae = true;
    for (int i = 0; this.example.hasSubtable(this.howToRead, i); i++)
    {
      Table cm = el.getSubtable(i);
      if(cm == null) continue;
      TableAuthorization ta = (TableAuthorization) uta.getParentTable(1).findElement(this.getTC(i));
      if(ta == null) return true;
      if(!ta.isAuthorized() && userType != 0) continue;
      String nn = this.name;
      nn = "" + i + "." + nn;
      Table fcm = cm.filteredByDefault(ta, nn, userType);
      boolean ecfbd = (fcm.size() == 0 && cm.size() > 0);
      boolean fs = false;
      if(cm.size() == 0)
      {
        for (int j = 0; j < ta.columnSettings.size(); j++)
        {
          if (!cm.getExample().isSaved(j, cm.getHowToRead()))
            continue;
          Table tm = ta.columnSettings.at(j).getSubtable(0);
          if (tm.size() == 0)
            continue;
          fs = true;
          break;
        }
      }
      if(!ecfbd && !fs) return true;
    }
    return false;
  }

  public String addTableElementToAllParentTables(TableElement el, String language, String currentUser, DateRepresentation date)
  {
    if (el == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsErrorAddElem(language);
    }
    long code = el.getCode().longValue();
    el.setParentElement(this.parentElement, this.howToRead);
    el.setParentTable(this, this.howToRead);
    if(this.example.isAutoIncrementable(this.howToRead))
    {
      if(this.size() > 0)
       el.setCode(this.at(this.size() - 1).getCode().longValue() + 1);
      else el.setCode(this.lastCode);
      this.changeTableName(el, el.getCode().longValue());
    }
    this.changePath(el);
    if (this.contains(el))
    {
      int ca = this.example.getAmountColumnIndex(this.howToRead);
      if (ca < 0)
      {
        this.lastOperationResult = false;
        return Consts.ermsExistsPos(language);
      }
      else
      {
        TableElement te = this.at(this.findCode(el.getCode().longValue()));
        this.wasAddTo = te;
        String message = "";
        try
        {
          message = te.setValue(ca,
                                new Integer( ( (Integer) te.getValue(ca, this.howToRead)).intValue() +
                                            ( (Integer) el.getValue(ca, this.howToRead)).
                                            intValue()), language, this.howToRead, currentUser, date);
        }
        catch (Exception e)
        {
          this.lastOperationResult = false;
          message = e.toString();
          return message;
        }
        this.lastOperationResult = true;
        return Consts.ermsAddTableElementPos(language) + message;
      }
    }
    int position = this.findPos(el.getCode().longValue());
    super.add(position, el);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    code = el.getCode().longValue();
    String message = el.wasAdd(this.howToRead, this.parentElement, currentUser, date);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    int maxHowToRead = this.example.getMaxHowToRead();
    for (int i = 1; i <= maxHowToRead; i++)
    {
      if (i == this.howToRead)
        continue;
      if (el.getParentElement(i) == null && el.getParentTable(i) == null)
        continue;
      if (el.getParentTable(i) == null)
      {
        TableElement te = el.getParentElement(i);
        for (int j = 0; te.getSubtable(j) != null; j++)
        {
          if (te.getSubtable(j).getExample().getClass().equals(this.example.
              getClass()) &&
              te.getSubtable(j).findCode(el.getCode().longValue()) < 0)
          {
            te.getSubtable(j).addTableElementSimple(el);
            break;
          }
        }
      }
      else
      {
        if (el.getParentTable(i).findCode(el.getCode().longValue()) < 0)
          el.getParentTable(i).addTableElementSimple(el);
      }
    }
    this.lastOperationResult = true;
    return message;
  }

  public String addTableElementRemovingIfContains(TableElement el, String language, String currentUser, DateRepresentation date)
  {
    if (el == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsErrorAddElem(language);
    }
    long code = el.getCode().longValue();
    el.setParentElement(this.parentElement, this.howToRead);
    el.setParentTable(this, this.howToRead);
    if(this.example.isAutoIncrementable(this.howToRead))
    {
      if(this.size() > 0)
       el.setCode(this.at(this.size() - 1).getCode().longValue() + 1);
      else el.setCode(this.lastCode);
      this.changeTableName(el, el.getCode().longValue());
    }
    this.changePath(el);
    if (this.contains(el))
    {
      int ca = this.example.getAmountColumnIndex(this.howToRead);
      if (ca < 0)
      {
        this.lastOperationResult = false;
        return Consts.ermsExistsPos(language);
      }
      else
      {
        TableElement te = this.at(this.findCode(el.getCode().longValue()));
        this.wasAddTo = te;
        String message = "";
        try
        {
          message = te.setValue(ca,
                                new Integer( ( (Integer) te.getValue(ca, this.howToRead)).intValue() +
                                            ( (Integer) el.getValue(ca, this.howToRead)).
                                            intValue()), language, this.howToRead, currentUser, date);
        }
        catch (Exception e)
        {
          this.lastOperationResult = false;
          message = e.toString();
          return message;
        }
        int maxHowToRead = this.example.getMaxHowToRead();
        for (int i = 1; i <= maxHowToRead; i++)
        {
          if (el.getParentElement(i) == null && el.getParentTable(i) == null)
            continue;
          if (el.getParentTable(i) == null)
          {
            TableElement pp = el.getParentElement(i);
            for (int j = 0; pp.getSubtable(j) != null; j++)
              pp.getSubtable(j).removeElementByReference(el);
          }
          else
            el.getParentTable(i).removeElementByReference(el);
        }
        this.lastOperationResult = true;
        return Consts.ermsAddTableElementPos(language) + message;
      }
    }
    int position = this.findPos(el.getCode().longValue());
    super.add(position, el);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    code = el.getCode().longValue();
    String message = el.wasAdd(this.howToRead, this.parentElement, currentUser, date);
    if(code != el.getCode().longValue())
      this.fixParentTablesExclusive(el);
    this.lastOperationResult = true;
    return message;
  }

  private boolean removeElementByReference(TableElement el)
  {
    for (int i = 0; i < super.size(); i++)
      if (this.at(i) == el)
      {
        super.remove(i);
        return true;
      }
    return false;
  }

  private boolean removeElementByCodeOnly(TableElement el)
  {
    for (int i = 0; i < super.size(); i++)
      if (this.at(i) != el && this.at(i).getCode().longValue() == el.getCode().longValue())
      {
        super.remove(i);
        return true;
      }
    return false;
  }

  private boolean removeElementByCode(TableElement el)
  {
    for (int i = 0; i < super.size(); i++)
      if (this.at(i).getCode().longValue() == el.getCode().longValue())
      {
        super.remove(i);
        return true;
      }
    return false;
  }

  public Table getSubtable(String tableName)
  {
    if (this.name.equals(tableName))
      return this;
    StringTokenizer st = new StringTokenizer(tableName, ".");
    String[] res = new String[st.countTokens()];
    for(int i = 0; st.hasMoreTokens(); i++)
      res[i] = st.nextToken();
    if(res.length < 2) return this;
    int sip = res.length - 2;
    String si = res[sip];
    String rest = "";
    for(int i = 0; i < res.length; i++)
      if(i != sip)
      {
        rest += res[i];
        if(i != res.length - 1)
          rest += ".";
      }
    Table t = this.example.getSubtable(this.howToRead, Integer.parseInt(si));
    return t.getSubtable(rest);
  }

  public TableElement at(int index)
  {
    if (index < 0 || index >= super.size())
      return null;
    return (TableElement)super.get(index);
  }

  public boolean isCellEditable(int row, int column)
  {
    if (row < 0 || row >= super.size())
      return false;
    return this.at(row).isCellEditable(column, this.howToRead);
  }

  public int[] findTableElementsIgnoreCase(String expr, int column)
  {
    ArrayList found = new ArrayList();
    for(int i = 0; i < this.size(); i++)
      if(((TableElement)this.get(i)).getValue(column, this.howToRead).toString().equalsIgnoreCase(expr))
        found.add(new Integer(i));
    int[] res = new int[found.size()];
    for(int i = 0; i < res.length; i++)
      res[i] = ((Integer)found.get(i)).intValue();
    return res;
  }

  public int[] findTableElements(Object expr, int column, boolean exprIsKey)
  {
    ArrayList found = new ArrayList();
    if (column == -1)
    {
      long ex;
      try
      {
        ex = Long.parseLong( (String) expr);
      }
      catch (NumberFormatException e)
      {
        return new int[0];
      }
      for (int i = 0; i < this.size(); i++)
        if (this.at(i).getCode().longValue() == ex)
          found.add(new Integer(i));
    }
    else if (exprIsKey)
    {
      long ex = this.dtJSHashCode(expr.toString());
      for (int i = 0; i < this.size(); i++)
        if (this.at(i).getCode().longValue() == ex)
          found.add(new Integer(i));
    }
    else
    {
      for (int i = 0; i < super.size(); i++)
        if (this.at(i).getValue(column, this.howToRead).equals(expr))
          found.add(new Integer(i));
    }
    int[] res = new int[found.size()];
    for (int i = 0; i < res.length; i++)
      res[i] = ( (Integer) found.get(i)).intValue();
    return res;
  }

  public int[] findTableElementsIgnoreLastSpaces(String value, int column)
  {
    ArrayList found = new ArrayList();
    value = value.replaceAll(" ", "");
    if(value.length() == 0) return new int[0];
    for (int i = 0; i < super.size(); i++)
    {
      String currentValue = this.at(i).getValue(column, this.howToRead).toString();
      currentValue = currentValue.replaceAll(" ", "");
      if (currentValue.equals(value))
        found.add(new Integer(i));
    }
    int[] res = new int[found.size()];
    for (int i = 0; i < res.length; i++)
      res[i] = ( (Integer) found.get(i)).intValue();
    return res;
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    if (rowIndex < 0 || rowIndex >= super.size())
      return null;
    return this.at(rowIndex).getValue(columnIndex, this.howToRead);
  }

  public String setValueAt(Object aValue, long code, int columnIndex, String language, String currentUser, DateRepresentation date)
  {
    int row = this.findCode(code);
    TableElement el = this.at(row);
    if (!el.isCellEditable(columnIndex, this.howToRead))
    {
      this.lastOperationResult = false;
      return Consts.ermsZelleNotEdit(language);
    }
    if (el.getValue(columnIndex, this.howToRead).equals(aValue))
    {
      this.lastOperationResult = false;
      return "";
    }
    int[] keyColumns = this.example.getKeyColumns(this.howToRead);
    boolean keyColumn = false;
    for (int i = 0; i < keyColumns.length; i++)
      keyColumn = keyColumn || (keyColumns[i] == columnIndex);
    if (!keyColumn)
    {
      this.lastOperationResult = true;
      try
      {
        return el.setValue(columnIndex, aValue, language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        this.lastOperationResult = false;
        return e.toString();
      }
    }
    TableElement toSet = el.duplicate();
    try
    {
      toSet.setValue(columnIndex, aValue, language, this.howToRead, currentUser, date);
    }
    catch (Exception e)
    {
      return e.toString();
    }
    int pos = this.findCode(toSet.getCode().longValue());
    String message;
    if (pos < 0)
    {
      try
      {
        message = el.setValue(columnIndex, aValue, language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message = e.toString();
      }
      if(code != el.getCode().longValue())
        this.fixParentTablesInclusive(el);
      this.lastOperationResult = message.length() == 0;
      TableElement parentElement;
      return message;
    }
    int ac = this.example.getAmountColumnIndex(this.howToRead);
    if (ac < 0)
    {
      this.lastOperationResult = false;
      return Consts.ermsExistsElem(language);
    }
    TableElement duplicate = this.at(pos);
    try
    {
      message = duplicate.setValue(ac,
                                   new Integer( ( (Integer) duplicate.getValue(ac,
          this.howToRead)).intValue() +
                                               ( (Integer) el.getValue(ac, this.howToRead)).
                                               intValue()), language, this.howToRead, currentUser, date);
    }
    catch (Exception e)
    {
      message = e.toString();
    }
    int maxHowToRead = this.example.getMaxHowToRead();
    for (int i = 1; i <= maxHowToRead; i++)
    {
      if (el.getParentElement(i) == null && el.getParentTable(i) == null)
        continue;
      if (el.getParentTable(i) == null)
      {
        TableElement pp = el.getParentElement(i);
        for (int j = 0; pp.getSubtable(j) != null; j++)
          pp.getSubtable(j).removeElementByReference(el);
      }
      else
        el.getParentTable(i).removeElementByReference(el);
    }
    this.lastOperationResult = message.length() == 0;
    return message;
  }

  public String toString()
  {
    String s = this.name + " table, lastCode= " + this.lastCode + "\n";
    for (int i = 0; i < this.size(); i++)
      s += this.at(i).toString(this.howToRead) + "\n";
    return s;
  }

  public long getLastCode()
  {
    return this.lastCode;
  }

/*  public void setLastCode(long c)
  {
    this.lastCode = c;
  }*/

  private int findPos(long code)
  {
    int size = super.size();
    if (size == 0)
      return 0;
    return this.findPos(code, 0, size - 1);
  }

  private int findPos(long code, int fromIndex, int tillIndex)
  {
    int dif = tillIndex - fromIndex;
    if (dif < 2)
      if (code >= this.at(tillIndex).getCode().longValue())
        return tillIndex + 1;
      else if (code >= this.at(fromIndex).getCode().longValue())
        return tillIndex;
      else
        return fromIndex;
    int ind = fromIndex + dif / 2;
    long c = this.at(ind).getCode().longValue();
    if (code < c)
      return this.findPos(code, fromIndex, ind);
    return this.findPos(code, ind, tillIndex);
  }

  public int findCode(long code)
  {
    int size = super.size();
    if (size == 0)
      return -1;
    return this.findCode(code, 0, size - 1);
  }

  public TableElement findElement(long code)
  {
    int pos = this.findCode(code);
    if (pos < 0)
      return null;
    return this.at(pos);
  }

  public boolean contains(TableElement el)
  {
    int pos = this.findCode(el.getCode().longValue());
    if (pos < 0)
    {
      this.lastContains = false;
      return false;
    }
    this.lastContains = true;
    return true;
  }

  private int findCode(long code, int fromIndex, int tillIndex)
  {
    int dif = tillIndex - fromIndex;
    if (dif < 2)
      if (this.at(fromIndex).getCode().longValue() == code)
        return fromIndex;
      else if (this.at(tillIndex).getCode().longValue() == code)
        return tillIndex;
      else
        return -1;
    int ind = fromIndex + dif / 2;
    long c = this.at(ind).getCode().longValue();
    if (code < c)
    {
      return this.findCode(code, fromIndex, ind);
    }
    if (code == c)
      return ind;
    return this.findCode(code, ind, tillIndex);
  }

  public String splitTableElement(long code, int column, int amount, Object aValue, String language, String currentUser, DateRepresentation date)
  {
    String message = "";
    int[] keyColumns = this.example.getKeyColumns(this.howToRead);
    boolean columnIsAKey = false;
    for (int i = 0; i < keyColumns.length; i++)
      columnIsAKey = columnIsAKey || column == keyColumns[i];
    if (!columnIsAKey)
    {
      this.lastOperationResult = false;
      return Consts.ermsNotSplitColumn(language);
    }
    TableElement te = this.at(this.findCode(code));
    if (!te.isCellEditable(column, this.howToRead))
    {
      this.lastOperationResult = false;
      return Consts.ermsZelleNotEdit(language);
    }
    int oldamount = ( (Integer) te.getValue(this.example.getAmountColumnIndex(this.howToRead), this.howToRead)).
        intValue();
    if (amount > oldamount)
    {
      this.lastOperationResult = false;
      return Consts.ermsInvAmount(language);
    }
    if (oldamount == amount || amount == 0)
    {
      this.lastOperationResult = false;
      return "";
    }
    else
    {
      if (!this.example.isSplittable(column, this.howToRead))
      {
        this.lastOperationResult = false;
        return Consts.ermsNotSplitColumn(language);
      }
      TableElement te2 = te.duplicate();
      String message2 = "";
      try
      {
        message2 = te2.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      try
      {
        message2 = te2.setValue(column, aValue, language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      try
      {
        message2 = te.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(oldamount - amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      this.lastOperationResult = true;
      message += this.addTableElementToAllParentTables(te2, language, currentUser, date);
      if (!this.lastOperationResult)
        try
        {
          message += te.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(oldamount + amount), language, this.howToRead, currentUser, date);
        }
        catch (Exception e)
        {
          message += e.toString();
        }
    }
    return message;
  }

  public String moveTableElement(long code, int amount, String language, String tableNameTo, String currentUser, DateRepresentation date)
  {
    Table t = Data.db.getTable(tableNameTo);
    if (t == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsNoSuchTable(language);
    }
    String message = "";
    int pos = this.findCode(code);
    TableElement te = this.at(pos);
    if (!te.isMovable(this.howToRead))
    {
      this.lastOperationResult = false;
      return Consts.ermsNoElemMove(language);
    }
    int oldamount = 0;
    if (amount != 0)
      oldamount = ( (Integer) te.getValue(this.example.getAmountColumnIndex(this.howToRead), this.howToRead)).
          intValue();
    if (amount > oldamount)
    {
      this.lastOperationResult = false;
      return Consts.ermsInvAmount(language);
    }
    if (oldamount == amount)
    {
      this.lastOperationResult = true;
      super.remove(pos);
      message += t.addTableElementRemovingIfContains(te, language, currentUser, date);
      if (!t.lastOperationResult)
      {
        message += this.addTableElement(te, language, currentUser, date);
        this.lastOperationResult = false;
      }
    }
    else
    {
      TableElement te2 = te.duplicate();
      String message2 = "";
      try
      {
        message2 = te2.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      try
      {
        message2 = te.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(oldamount - amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      message += t.addTableElementToAllParentTables(te2, language, currentUser, date);
      if (!t.lastOperationResult)
      {
        message += this.addTableElement(te2, language, currentUser, date);
        this.lastOperationResult = false;
      }
    }
    return message;
  }

  public String moveTableElement(long code, int amount, String language, String tableNameTo, long elementCode[], int[] subtableIndex, String currentUser, DateRepresentation date)
  {
    Table t = Data.db.getTable(tableNameTo);
    if (t == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsNoSuchTable(language);
    }
    for (int i = 0; i < elementCode.length; i++)
      t = t.at(t.findCode(elementCode[i])).getSubtable(subtableIndex[i]);
    if (t == null)
    {
      this.lastOperationResult = false;
      return Consts.ermsNoSuchTable(language);
    }
    String message = "";
    int pos = this.findCode(code);
    TableElement te = this.at(pos);
    if (!te.isMovable(this.howToRead))
    {
      this.lastOperationResult = false;
      return Consts.ermsNoElemMove(language);
    }
    int oldamount = 0;
    if (amount != 0)
      oldamount = ( (Integer) te.getValue(this.example.getAmountColumnIndex(this.howToRead), this.howToRead)).
          intValue();
    if (amount > oldamount)
    {
      this.lastOperationResult = false;
      return Consts.ermsInvAmount(language);
    }
    if (oldamount == amount)
    {
      super.remove(pos);
      message += t.addTableElementRemovingIfContains(te, language, currentUser, date);
      if (!t.lastOperationResult)
      {
        message += this.addTableElement(te, language, currentUser, date);
        this.lastOperationResult = false;
      }
    }
    else
    {
      TableElement te2 = te.duplicate();
      String message2 = "";
      try
      {
        message2 = te2.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      try
      {
        message2 = te.setValue(this.example.getAmountColumnIndex(this.howToRead), new Integer(oldamount - amount), language, this.howToRead, currentUser, date);
      }
      catch (Exception e)
      {
        message2 = e.toString();
      }
      if (message2.length() > 0)
      {
        this.lastOperationResult = false;
        return message2;
      }
      message += t.addTableElementToAllParentTables(te2, language, currentUser, date);
      if (!t.lastOperationResult)
      {
        this.lastOperationResult = false;
        message += this.addTableElement(te2, language, currentUser, date);
      }
    }
    return message;
  }

  private boolean removeDuplicatesAddingThem(TableElement toRemove)
  {
    int ac = this.example.getAmountColumnIndex(this.howToRead);
    if (ac < 0)
      return false;
    boolean containsToRemove = false;
    boolean containsDuplicate = false;
    for (int i = 0; i < super.size(); i++)
    {
      containsToRemove = containsToRemove || this.at(i) == toRemove;
      containsDuplicate = containsDuplicate ||
          (this.at(i) != toRemove && this.at(i).getCode().longValue() == toRemove.getCode().longValue());
    }
    if (!containsToRemove || !containsDuplicate)
      return false;
    TableElement currentElement;
    boolean done1 = false;
    boolean done2 = false;
    for (int i = 0; i < super.size(); i++)
    {
      if (done1 && done2)
        return true;
      currentElement = this.at(i);
      if (currentElement == toRemove)
      {
        super.remove(i);
        i--;
        done1 = true;
        continue;
      }
      if (currentElement != toRemove && currentElement.getCode().longValue() == toRemove.getCode().longValue())
      {
        try
        {
          currentElement.setValue(ac,
                                  new Integer( ( (Integer) currentElement.
                                                getValue(ac, this.howToRead)).
                                              intValue() +
                                              ( (Integer) toRemove.
                                               getValue(ac, this.howToRead)).
                                              intValue()), "en", this.howToRead, "", null);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        done2 = true;
      }
    }
    return done1 && done2;
  }

  private boolean removeDuplicates(TableElement toRemove)
  {
    int ac = this.example.getAmountColumnIndex(this.howToRead);
    if (ac < 0)
      return false;
    boolean containsToRemove = false;
    boolean containsDuplicate = false;
    for (int i = 0; i < super.size(); i++)
    {
      containsToRemove = containsToRemove || this.at(i) == toRemove;
      containsDuplicate = containsDuplicate ||
          (this.at(i) != toRemove && this.at(i).getCode().longValue() == toRemove.getCode().longValue());
    }
    if (!containsToRemove || !containsDuplicate)
      return false;
    TableElement currentElement;
    for (int i = 0; i < super.size(); i++)
    {
      currentElement = this.at(i);
      if (currentElement == toRemove)
      {
        super.remove(i);
        return true;
      }
    }
    return false;
  }

  public String removeTableElement(long code, String language, String currentUser, DateRepresentation date)
  {
    int i = this.findCode(code);
    if (i < 0)
    {
      this.lastOperationResult = false;
      return Consts.ermsNoPosition(language);
    }
    TableElement te = this.at(i);
    if (!te.isRemovable(this.howToRead))
    {
      this.lastOperationResult = false;
      return Consts.ermsRemovePosition(language);
    }
    super.remove(i);
    this.lastOperationResult = true;
    te.setParentElement(null, this.howToRead);
    te.setParentTable(null, this.howToRead);
    if(code != te.getCode().longValue())
      this.fixParentTablesExclusive(te);
    code = te.getCode().longValue();
    String message = te.wasRemoved(this.howToRead, this.parentElement, currentUser, date);
    if(code != te.getCode().longValue())
      this.fixParentTablesExclusive(te);
    int maxHowToRead = this.example.getMaxHowToRead();
    boolean add = false;
    for (int j = 1; j <= maxHowToRead; j++)
    {
      if (j == this.howToRead)
        continue;
      if (te.getParentElement(j) == null && te.getParentTable(j) == null)
        continue;
      if (te.getParentTable(j) == null)
      {
        TableElement tet = te.getParentElement(j);
        for (int k = 0; tet.getSubtable(k) != null; k++)
        {
          if (tet.getSubtable(k).getExample().getClass().equals(this.example.
              getClass()))
          {
            if (add)
              tet.getSubtable(k).removeDuplicates(te);
            else
              add = tet.getSubtable(k).removeDuplicatesAddingThem(te);
          }
        }
      }
      else
      {
        if (add)
          te.getParentTable(j).removeDuplicates(te);
        else
          add = te.getParentTable(j).removeDuplicatesAddingThem(te);
      }
    }
    return message;
  }

  public boolean sort(int criteria, boolean ascending)
  {
    if (this.size() < 2)
      return false;
    Object[] expressions = this.toArray();
    Arrays.sort(expressions, new BasicComparator(criteria, ascending, this.howToRead));
    super.clear();
    for (int i = 0; i < expressions.length; i++)
      super.add(expressions[i]);
    expressions = null;
    return true;
  }

  /*  public int sortByRemovable()
    {
      Object[] expressions = this.toArray();
      Arrays.sort(expressions, new RemovableComparator(this.howToRead));
      super.clear();
      for(int i = 0; i < expressions.length; i++)
        super.add(expressions[i]);
      expressions = null;
      for(int i = 0; i < super.size(); i++)
      {
        Data.progressBar.setValue(i);
        if(this.at(i).isRemovable(this.howToRead))
          return i;
      }
      return super.size();
    }
   */
// 9.999999
  public static long dtJSHashCode(String str)
  {
    int hash = 1315423911;
    int code = 0;
    for (int i = 0, j = 1; i < str.length(); i++, j++)
    {
      hash ^= ( (hash << 5) + str.charAt(i) + (hash >> 2));
      code += j * str.charAt(i);
    }
    hash = (hash & 0x7FFFFFFF);
    return Long.parseLong("" + hash + code);
  }

// 9.999989
  public static long dtBKDRHashCode(String str)
  {
    int seed = 1313131313; // 31 131 1313 13131 131313 etc..
    int hash = 0;
    int code = 0;
    for (int i = 0, j = 1; i < str.length(); i++, j++)
    {
      code += j * str.charAt(i);
      hash = (hash * seed) + str.charAt(i);
    }
    hash = (hash & 0x7FFFFFFF);
    return Long.parseLong("" + hash + code);
  }

// 9.99999
  public static long dtAPHashCode(String str)
  {
    int hash = 0;
    int code = 0;
    for (int i = 0, j = 1; i < str.length(); i++, j++)
    {
      if ( (i & 1) == 0)
        hash ^= ( (hash << 7) ^ str.charAt(i) ^ (hash >> 3));
      else
        hash ^= (~ ( (hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
      code += j * str.charAt(i);
    }
    hash = (hash & 0x7FFFFFFF);
    return Long.parseLong("" + hash + code);
  }

// 9.999993
  public static long dtRSHashCode(String str)
  {
    int b = 378551;
    int a = 63689;
    int hash = 0;
    int code = 0;
    for (int i = 0, j = 1; i < str.length(); i++, j++)
    {
      hash = hash * a + str.charAt(i);
      a = a * b;
      code += j * str.charAt(i);
    }
    hash = (hash & 0x7FFFFFFF);
    return Long.parseLong("" + hash + code);
  }

// 2
  public static int DTHashCode(String str)
  {
    int code = 0;
    for (int i = 0, j = 1; i < str.length(); i++, j++)
      code += j * str.charAt(i);
    return code;
  }

// 8
  public static int BKDRHashCode(String str)
  {
    int seed = 1313131313; // 31 131 1313 13131 131313 etc..
    int hash = 0;
    for (int i = 0; i < str.length(); i++)
      hash = (hash * seed) + str.charAt(i);
    return (hash & 0x7FFFFFFF);
  }

// 8
  public static int SDBMHashCode(String str)
  {
    int hash = 0;
    for (int i = 0; i < str.length(); i++)
      hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
    return (hash & 0x7FFFFFFF);
  }

  public BasicComparator getComparator(int criteria, boolean ascending, int howToRead)
  {
    return new BasicComparator(criteria, ascending, howToRead);
  }

  public class BasicComparator implements Comparator
  {
    private int criteria;
    private boolean ascending;
    private int howToRead;

    BasicComparator(int criteria, boolean ascending, int howToRead)
    {
      this.criteria = criteria;
      this.ascending = ascending;
      this.howToRead = howToRead;
    }

    public int compare(Object obj1, Object obj2)
    {
      TableElement te1 = (TableElement) obj1;
      TableElement te2 = (TableElement) obj2;
      Object value = te1.getValue(this.criteria, this.howToRead);
      if (this.ascending)
      {
        if(this.criteria == -1)
          return te1.getCode().compareTo(te2.getCode());
        if (value instanceof Comparable)
        {
          return ( (Comparable) value).compareTo( (Comparable) te2.getValue(this.criteria, this.howToRead));
        }
        else if (value instanceof Boolean)
        {
          return ( ( (Boolean) value)).toString().compareTo( ( (Boolean) te2.getValue(this.criteria, this.howToRead)).
              toString());
        }
        else
        {
          return ( (Long) value).compareTo( (Long) te2.getValue(this.criteria, this.howToRead));
        }
      }
      else
      {
        if(this.criteria == -1)
          return te1.getCode().compareTo(te2.getCode());
        if (value instanceof Comparable)
        {
          return ( (Comparable) te2.getValue(this.criteria, this.howToRead)).compareTo( (Comparable) value);
        }
        else if (value instanceof Boolean)
        {
          return ( ( (Boolean) te2.getValue(this.criteria, this.howToRead)).toString()).compareTo( (value).toString());
        }
        else
        {
          return ( (Long) te2.getValue(this.criteria, howToRead)).compareTo( (Long) value);
        }
      }

//      return (int) ( (TableElement) obj1).compareTo( (TableElement) obj2, this.criteria, this.ascending,
//          this.howToRead);
    }

    public boolean equals(Object obj1, Object obj2)
    {
      return ( (TableElement) obj1).equals( (TableElement) obj2, this.criteria, this.howToRead);
    }
  }

  public static String getTimeValue(int hour, int minute)
  {
    if(hour < 0) return "";
    String hr = "" + hour;
    if(hour < 10) hr = "0" + hour;
    String mn = "" + minute;
    if(minute < 10) mn = "0" + minute;
    return hr + ":" + mn;
  }

  /*
    public class RemovableComparator implements Comparator
    {
      private int howToRead;
      RemovableComparator(int howToRead)
      {
        this.howToRead = howToRead;
      }
      public int compare(Object obj1, Object obj2)
      {
        Data.progressBar.setValue(Data.progressBar.getValue() + 1);
        boolean b1 = ((TableElement)obj1).isRemovable(this.howToRead);
        boolean b2 = ((TableElement)obj2).isRemovable(this.howToRead);
        if(b1 == b2)
          return 0;
        if(b1 && !b2)
          return -1;
        return 1;
      }
      public boolean equals(Object obj1, Object obj2)
      {
         return this.compare(obj1, obj2) == 0;
      }
    }
   */
}

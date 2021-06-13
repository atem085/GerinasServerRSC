package data;

import java.net.ServerSocket;
import java.net.Socket;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import utils.Consts;
import table.Table;
import table.User;
import element.DynamicGuestUser;
import element.DynamicEmployeeUser;
import element.DynamicGuestBuyerUser;
import element.TableAuthorization;
import java.util.Arrays;
import table.TableElement;
import java.util.Comparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Hashtable;
import table.Command10;
import table.Command9;
import table.Command;
import table.Command11;
import table.Command12;
import table.Command13;
import table.Command14;
import table.Command15;
import table.Command16;
import table.Command31;
import table.Command32;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import java.util.StringTokenizer;
import java.util.Hashtable;
import jxl.write.Formula;
import jxl.write.WritableCellFormat;
import utils.DateRepresentation;
import jxl.write.DateFormat;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class WebThread extends Thread
{

  private ServerSocket webServerSocket = null;
  private Hashtable userSortCriterias = new Hashtable();
  private Hashtable userCurrentTableName = new Hashtable();
  private Hashtable userCurrentTableName2 = new Hashtable();
  private Hashtable userCurrentSi = new Hashtable();
  private Hashtable userCurrentSi2 = new Hashtable();
  private Hashtable userCurrentEc = new Hashtable();
  private Hashtable userCurrentEc2 = new Hashtable();
  private Hashtable userLastTableNames = new Hashtable();
  private Hashtable userLastSis = new Hashtable();
  private Hashtable userLastEcs = new Hashtable();
  private Hashtable userLastPages = new Hashtable();
  private Hashtable userLastFilterColumn = new Hashtable();
  private Hashtable userLastFilterValue = new Hashtable();
  private Hashtable userLastFilterFrom = new Hashtable();
  private Hashtable userLastSortCriteria = new Hashtable();
  private Hashtable userLastAscending = new Hashtable();
  private Hashtable userLastSelectedCode = new Hashtable();
  private Hashtable userCurrentPosition = new Hashtable();

  public String[] alphabet = new String[26];


  public WebThread() throws Exception
  {
    this.webServerSocket = new ServerSocket(4015);
    this.webServerSocket.setReuseAddress(true);
    alphabet[0] = "a";
    alphabet[1] = "b";
    alphabet[2] = "c";
    alphabet[3] = "d";
    alphabet[4] = "e";
    alphabet[5] = "f";
    alphabet[6] = "g";
    alphabet[7] = "h";
    alphabet[8] = "i";
    alphabet[9] = "j";
    alphabet[10] = "k";
    alphabet[11] = "l";
    alphabet[12] = "m";
    alphabet[13] = "n";
    alphabet[14] = "o";
    alphabet[15] = "p";
    alphabet[16] = "q";
    alphabet[17] = "r";
    alphabet[18] = "s";
    alphabet[19] = "t";
    alphabet[20] = "u";
    alphabet[21] = "v";
    alphabet[22] = "w";
    alphabet[23] = "x";
    alphabet[24] = "y";
    alphabet[25] = "z";
  }

  public void halt()
  {
    try
    {
      this.webServerSocket.close();
    }
    catch(Exception e) {}
  }

  public void run()
  {
    Socket webSocket = null;
    int counter = 0;
    int userType;
    int so = 90000;
    int i;
    MyObjectOutputStream outStream = null;
    MyObjectInputStream inStream = null;
    User webUser = null;
    while(Data.canBeRunning)
    {
      try
      {
        webSocket = this.webServerSocket.accept();
        InetAddress  webAddress = webSocket.getInetAddress();
        int webPort = webSocket.getPort();
        System.out.println("web\t" + webAddress + "\t" + webPort);
        webSocket.setSoTimeout(so);
        outStream = new MyObjectOutputStream(webSocket.getOutputStream(), false);
        outStream.setEncoding(false);
//        System.out.println("webOutStream ok");
        inStream = new MyObjectInputStream(webSocket.getInputStream());
        inStream.setEncoding(false);
//        outStream.setEcho(true);
//        inStream.setEcho(true);
//        System.out.println("webInStream ok");
      }
      catch (IOException e)
      {
        System.out.println("Aborting accepting WEB user");
        continue;
      }
      catch (Exception e2)
      {
        System.out.println("Something is wrong in WEB Thread!!!");
        e2.printStackTrace();
        continue;
      }

      System.out.println("WEB User " + counter + " has been successfully connected");
      counter++;
      String language = "en";
      String userName = "";
      String password = null;
      try
      {
        userType = inStream.readInt();
        userName = inStream.readUTF();
        password = inStream.readUTF();
        language = inStream.readUTF();
        Table users = null;
        if(userType == 0)
          users = Data.db.getTable(Consts.tbWebUsers);
        else if(userType == 1)
          users = Data.db.getTable(Consts.tbWebGuestUsers);
        else
          users = Data.db.getTable(Consts.tbWebGuestBuyerUsers);
        long userCode = Table.dtJSHashCode(userName);
        webUser = (User)users.findElement(userCode);
        if(webUser == null)
        {
          System.out.println("Wrong login for web user " + userName + " " + password);
          outStream.writeBoolean(false);
          outStream.writeUTF(Consts.ermsInvName(language));
          outStream.flush();
          try
          {
            inStream.close();
          }
          catch(Exception e) {}
          try
          {
            outStream.close();
          }
          catch(Exception e) {}
          try
          {
            webSocket.close();
          }
          catch(Exception e) {}
          continue;
        }
        if(!password.equals(new String(webUser.password)))
        {
          System.out.println("Wrong password for web user " + userName + " " + password);
          outStream.writeBoolean(false);
          outStream.writeUTF(Consts.ermsInvName(language));
          outStream.flush();
          try
          {
            inStream.close();
          }
          catch(Exception e) {}
          try
          {
            outStream.close();
          }
          catch(Exception e) {}
          try
          {
            webSocket.close();
          }
          catch(Exception e) {}
          continue;
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.out.println("Failed to authorize WEB user");
        try
        {
          inStream.close();
        }
        catch(Exception ee) {}
        try
        {
          outStream.close();
        }
        catch(Exception ee) {}
        try
        {
          webSocket.close();
        }
        catch(Exception ee) {}
        continue;
      }

      Hashtable sortCriterias = (Hashtable)this.userSortCriterias.get(userName);
      if(sortCriterias == null)
      {
        sortCriterias = new Hashtable();
        this.userSortCriterias.put(userName, sortCriterias);
      }
      ArrayList lastTableNames = (ArrayList)this.userLastTableNames.get(userName);
      if(lastTableNames == null)
      {
        lastTableNames = new ArrayList();
        this.userLastTableNames.put(userName, lastTableNames);
      }
      ArrayList lastSis = (ArrayList)this.userLastSis.get(userName);
      if(lastSis == null)
      {
        lastSis = new ArrayList();
        this.userLastSis.put(userName, lastSis);
      }
      ArrayList lastEcs = (ArrayList)this.userLastEcs.get(userName);
      if(lastEcs == null)
      {
        lastEcs = new ArrayList();
        this.userLastEcs.put(userName, lastEcs);
      }
      ArrayList lastPages = (ArrayList)this.userLastPages.get(userName);
      if(lastPages == null)
      {
        lastPages = new ArrayList();
        this.userLastPages.put(userName, lastPages);
      }
      ArrayList lastFilterColumn = (ArrayList)this.userLastFilterColumn.get(userName);
      if(lastFilterColumn == null)
      {
        lastFilterColumn = new ArrayList();
        this.userLastFilterColumn.put(userName, lastFilterColumn);
      }
      ArrayList lastFilterValue = (ArrayList)this.userLastFilterValue.get(userName);
      if(lastFilterValue == null)
      {
        lastFilterValue = new ArrayList();
        this.userLastFilterValue.put(userName, lastFilterValue);
      }
      ArrayList lastFilterFrom = (ArrayList)this.userLastFilterFrom.get(userName);
      if(lastFilterFrom == null)
      {
        lastFilterFrom = new ArrayList();
        this.userLastFilterFrom.put(userName, lastFilterFrom);
      }
      ArrayList lastSortCriteria = (ArrayList)this.userLastSortCriteria.get(userName);
      if(lastSortCriteria == null)
      {
        lastSortCriteria = new ArrayList();
        this.userLastSortCriteria.put(userName, lastSortCriteria);
      }
      ArrayList lastAscending = (ArrayList)this.userLastAscending.get(userName);
      if(lastAscending == null)
      {
        lastAscending = new ArrayList();
        this.userLastAscending.put(userName, lastAscending);
      }
      ArrayList lastSelectedCode = (ArrayList)this.userLastSelectedCode.get(userName);
      if(lastSelectedCode == null)
      {
        lastSelectedCode = new ArrayList();
        this.userLastSelectedCode.put(userName, lastSelectedCode);
      }

      try
      {
        outStream.writeBoolean(true);
        if(userType == 0)
          outStream.writeUTF(((DynamicEmployeeUser)webUser).getValue(3, 1, webUser, language).toString());
        else if(userType == 1)
          outStream.writeUTF(((DynamicGuestUser)webUser).getValue(3, 1, webUser, language).toString());
        else
          outStream.writeUTF(((DynamicGuestBuyerUser)webUser).getValue(3, 1, webUser, language).toString());
        outStream.flush();
        System.out.println("WEB user " + new String(webUser.name) + " successfully logged in");
        webUser.isLoggedIn = true;

        Table tas = null;
        if(userType == 0)
          tas = ((DynamicEmployeeUser)webUser).getSubtable(0);
        else if(userType == 1)
          tas = ((DynamicGuestUser)webUser).getSubtable(0);
        else
          tas = ((DynamicGuestBuyerUser)webUser).getSubtable(0);
        Table[] tables = Data.db.getTables();
        ArrayList menu = new ArrayList();
        ArrayList tableNames = new ArrayList();
        for(int j = 0; j < tables.length; j++)
        {
          String tableName = tables[j].getName();
          TableAuthorization currentTa = (TableAuthorization)tas.findElement(Table.dtJSHashCode(tableName));
          if(currentTa == null)
          {
            menu.add(tables[j].getExample().getTitle(language, tables[j].getHowToRead(), webUser));
            tableNames.add(tables[j].getName());
          }
          else if(((Boolean)currentTa.getValue(1, 1, webUser, language)).booleanValue())
          {
            menu.add(tables[j].getExample().getTitle(language, tables[j].getHowToRead(), webUser));
            tableNames.add(tables[j].getName());
          }
        }
        outStream.writeInt(menu.size());
        for(int j = 0; j < menu.size(); j++)
        {
          outStream.writeUTF(menu.get(j).toString());
          outStream.writeUTF(tableNames.get(j).toString());
        }
        outStream.flush();

        boolean cont = inStream.readBoolean();
        if(!cont)
        {
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }
        String linkcol = inStream.readUTF();
//        System.out.println("Linkcol " + linkcol);
        boolean back = inStream.readBoolean();
//        System.out.println("Back " + back);
        Integer currentPos = (Integer)this.userCurrentPosition.get(userName);
        if(currentPos == null)
        {
          currentPos = new Integer(-1);
          this.userCurrentPosition.put(userName, currentPos);
        }
        int currentPosition = currentPos.intValue();
        if(back)
        {
          if(currentPosition > 0)
          {
            currentPosition--;
            currentPos = new Integer(currentPosition);
            this.userCurrentPosition.put(userName, currentPos);
          }
          else
          {
            currentPosition = lastTableNames.size() - 2;
            currentPos = new Integer(currentPosition);
            this.userCurrentPosition.put(userName, currentPos);
          }
          back = currentPosition > -1 && lastTableNames.size() > 0;
          if(currentPosition < -1)
          {
            currentPosition = -1;
            currentPos = new Integer(currentPosition);
            this.userCurrentPosition.put(userName, currentPos);
          }
          if(currentPosition >= lastTableNames.size())
            currentPosition = lastTableNames.size() - 1;
        }
        int siSize = 0;
        String currentTableName = (String)this.userCurrentTableName.get(userName);
        long[] currentEc = (long[])this.userCurrentEc.get(userName);
        int[] currentSi = (int[])this.userCurrentSi.get(userName);
        if(!back)
        {
          currentTableName = inStream.readUTF();
          this.userCurrentTableName.put(userName, currentTableName);
          currentPosition++;
          currentPos = new Integer(currentPosition);
          this.userCurrentPosition.put(userName, currentPos);
          try
          {
            lastTableNames.add(currentPosition, new String( (char[]) currentTableName.toCharArray().clone()));
          }
          catch(Exception e) {}
//        System.out.println("tableName " + currentTableName);
          siSize = inStream.readInt();
//        System.out.println("SI " + siSize);
          currentEc = new long[siSize];
          for (int j = 0; j < siSize; j++)
            currentEc[j] = inStream.readLong();
          this.userCurrentEc.put(userName, currentEc);
          currentSi = new int[siSize];
          for (int j = 0; j < siSize; j++)
            currentSi[j] = inStream.readInt();
          this.userCurrentSi.put(userName, currentSi);
          try
          {
            lastSis.add(currentPosition, currentSi.clone());
            lastEcs.add(currentPosition, currentEc.clone());
          }
          catch(Exception e) {}
        }
        else
        {
          currentTableName = lastTableNames.get(currentPosition).toString();
          this.userCurrentTableName.put(userName, currentTableName);
          currentEc = (long[])lastEcs.get(currentPosition);
          this.userCurrentEc.put(userName, currentEc);
          currentSi = (int[])lastSis.get(currentPosition);
          this.userCurrentSi.put(userName, currentSi);
          siSize = currentSi.length;
        }
//        for(int j = 0; j < siSize; j++)
//          System.out.println("SI " + j + " " + this.currentSi[j]);
//        for(int j = 0; j < this.currentEc.length; j++)
//          System.out.println("EC " + j + " " + this.currentEc[j]);
        String currentTableName2 = (String)this.userCurrentTableName2.get(userName);
        long[] currentEc2 = (long[])this.userCurrentEc2.get(userName);
        int[] currentSi2 = (int[])this.userCurrentSi2.get(userName);
        boolean ecSiEqual = !back && currentTableName2 != null && currentEc2 != null && currentSi2 != null && currentEc.length > 0 && currentEc.length == currentEc2.length && currentSi.length == currentSi2.length;
        if(ecSiEqual)
        {
          for(int j = 0; j < currentEc.length; j++)
            ecSiEqual = ecSiEqual && currentEc[j] == currentEc2[j];
          for(int j = 0; j < currentSi.length; j++)
            ecSiEqual = ecSiEqual && currentSi[j] == currentSi2[j];
        }
        if(ecSiEqual)
        {
          String ctn2 = currentTableName;
          currentTableName = currentTableName2;
          this.userCurrentTableName.put(userName, currentTableName);
          currentTableName2 = ctn2;
          this.userCurrentTableName2.put(userName, currentTableName2);
          try
          {
            lastTableNames.remove(currentPosition);
            lastTableNames.add(currentPosition, new String( (char[]) currentTableName.toCharArray().clone()));
          }
          catch(Exception e) {}
        }
        boolean expb = false;
        boolean op = false;
        if(!back)
        {
          expb = inStream.readBoolean();
          op = inStream.readBoolean();
        }
        int operation = 0;
        int arsm = -1;
        long code = -1;
        if(!back)
          try
          {
            lastSelectedCode.add(currentPosition, new Long(code));
          }
          catch(Exception e) {}
        if(!back && !linkcol.equals("-1"))
          code = inStream.readLong();
        else if(!back && op)
        {
          operation = inStream.readInt();
          code = inStream.readLong();
          try
          {
            lastSelectedCode.remove(currentPosition);
            lastSelectedCode.add(currentPosition, new Long(code));
          }
          catch(Exception e) {}
        }
        else if(!back)
        {
          boolean add = inStream.readBoolean();
          if(!add)
          {
            boolean remove = inStream.readBoolean();
            if(!remove)
            {
              boolean set = inStream.readBoolean();
              if(!set)
              {
                boolean move = inStream.readBoolean();
                if(move)
                {
                  arsm = 3;
                  code = inStream.readLong();
                  try
                  {
                    lastSelectedCode.add(currentPosition, new Long(code));
                    lastSelectedCode.remove(currentPosition);
                  }
                  catch(Exception e) {}
                }
              }
              else
              {
                arsm = 2;
//                this.radio = inStream.readBoolean();
//                if(this.radio)
                  code = inStream.readLong();
                  try
                  {
                    lastSelectedCode.remove(currentPosition);
                    lastSelectedCode.add(currentPosition, new Long(code));
                  }
                  catch(Exception e) {}
/*                else
                {
                  String codes = inStream.readUTF();
                  StringTokenizer st = new StringTokenizer(codes, ".");
                  int codesSize = st.countTokens();
                  this.selectedCodes = new long[codesSize];
                  for(int j = 0; st.hasMoreTokens(); j++)
                    this.selectedCodes[j] = Long.parseLong(st.nextToken());
                }*/
              }
            }
            else
            {
              arsm = 1;
            }
          }
          else
          {
            arsm = 0;
          }
        }
/*
        if(currentTableName == null || currentTableName2 == null)
        {
          System.out.println(currentTableName);
          System.out.println(currentTableName2);
          System.out.println(this.lastTableNames.get(this.currentPosition));
          System.out.println("last EC " + ((long[])this.lastEcs.get(this.currentPosition)).length);
          System.out.println("current EC " + this.currentEc.length);
        }*/
//        System.out.println("CURRENT TABLE NAME " + currentTableName);
        Object[] res = this.getTable(currentTableName, currentEc, currentSi, webUser, userType);
        if(res == null)
        {
          String ctn2 = currentTableName;
          currentTableName = currentTableName2;
          this.userCurrentTableName.put(userName, currentTableName);
          try
          {
            lastTableNames.remove(currentPosition);
            lastTableNames.add(currentPosition, new String( (char[]) currentTableName.toCharArray().clone()));
          }
          catch(Exception e) {}
          currentTableName2 = ctn2;
          this.userCurrentTableName2.put(userName, currentTableName2);
          long[] ec2 = currentEc;
          currentEc = currentEc2;
          this.userCurrentEc.put(userName, currentEc);
          currentEc2 = currentEc;
          this.userCurrentEc2.put(userName, currentEc2);
          try
          {
            lastEcs.remove(currentPosition);
            lastEcs.add(currentPosition, currentEc.clone());
          }
          catch(Exception e) {}
          res = this.getTable(currentTableName, currentEc, currentSi, webUser, userType);
          ecSiEqual = true;
          if(res == null)
          {
            currentEc = new long[0];
            this.userCurrentEc.put(userName, currentEc);
            try
            {
              lastEcs.remove(lastEcs.size() - 1);
              lastEcs.add(currentEc.clone());
            }
            catch(Exception e) {}
            currentSi = new int[0];
            this.userCurrentSi.put(userName, currentSi);
            try
            {
              lastSis.remove(lastSis.size() - 1);
              lastSis.add(currentSi.clone());
            }
            catch(Exception e) {}
            currentTableName2 = null;
            currentEc2 = null;
            currentSi2 = null;
            this.userCurrentEc2.remove(userName);
            this.userCurrentSi2.remove(userName);
            this.userCurrentTableName2.remove(userName);
            res = this.getTable(currentTableName, currentEc, currentSi, webUser, userType);
          }
        }
        Table currentTable = (Table)res[0];
        int lc = -1;
        long linkCode = -1;
//        System.out.println("linkcol " + linkcol);
//        javax.swing.JOptionPane.showConfirmDialog(Data.mainFrame, linkcol);
        if(!linkcol.equals("-1"))
        {
          String[] columnNames = currentTable.getExample().getColumnNames(currentTable.getHowToRead(), language, webUser);
          lc = this.getColumnPos(columnNames, linkcol);
//          System.out.println("lc " + lc);
        }
        if(!back && lc > -1)
        {
//          System.out.println("Linking table " + currentTable.getName() + " code " + code);
          linkCode = currentTable.findElement(code).getReferenceCode(lc, currentTable.getHowToRead(), webUser, language);
          currentTableName2 = currentTableName;
          this.userCurrentTableName2.put(userName, currentTableName2);
          currentEc2 = currentEc;
          this.userCurrentEc2.put(userName, currentEc2);
          currentSi2 = currentSi;
          this.userCurrentSi2.put(userName, currentSi2);
          currentTable = currentTable.findElement(code).getReferenceTable(lc, currentTable.getHowToRead());
          res[0] = currentTable;
//          System.out.println(currentTable.getName());
          currentEc = this.getEC(currentTable, new long[0]);
          this.userCurrentEc.put(userName, currentEc);
          try
          {
            lastEcs.remove(lastEcs.size() - 1);
            lastEcs.add(currentEc.clone());
          }
          catch(Exception e) {}
          res[1] = currentEc;
          currentSi = this.getSI(currentTable, new int[0]);
          this.userCurrentSi.put(userName, currentSi);
          try
          {
            lastSis.remove(lastSis.size() - 1);
            lastSis.add(currentSi.clone());
          }
          catch(Exception e) {}
          res[2] = currentSi;
          siSize = currentSi.length;
          currentTableName = this.getTableName(currentTable);
          this.userCurrentTableName.put(userName, currentTableName);
          try
          {
            lastTableNames.remove(currentPosition);
            lastTableNames.add(currentPosition, new String( (char[]) currentTableName.toCharArray().clone()));
          }
          catch(Exception e) {}
//          System.out.println("Table name " + tableName);
        }
        long[] ec2 = (long[])res[1];
        int[] si2 = (int[])res[2];
        if(!back)
          outStream.writeBoolean(ec2.length < currentEc.length || lc > -1 || ecSiEqual);
        int page = 1;
        if(back)
          try
          {
            page = ( (Integer) lastPages.get(currentPosition)).intValue();
          }
          catch(Exception e)
          {
            if(lastPages.size() > 0)
            {
              page = ( (Integer) lastPages.get(lastPages.size() - 1)).intValue();
              currentPosition = lastPages.size() - 1;
            }
          }
        if(back || (ec2.length < currentEc.length || lc > -1 || ecSiEqual))
        {
          outStream.writeUTF(currentTableName);
//          System.out.println("Writing table name " + this.currentTableName);
          outStream.writeInt(currentEc.length);
          for(int j = 0; j < currentEc.length; j++)
          {
            outStream.writeLong(currentEc[j]);
            outStream.writeInt(currentSi[j]);
          }
          outStream.writeInt(page);
//          System.out.println("currentPosition " + this.currentPosition);
//          System.out.println("Sending lastSelectedCode " + this.lastSelectedCode.get(this.currentPosition));
//          System.out.println("currentEC " + this.currentEc);
          outStream.writeLong(((Long)lastSelectedCode.get(currentPosition)).longValue());
        }
        outStream.flush();

        if(currentTable == null)
        {
          System.out.println("Table " + currentTableName + "for WEB user " + new String(webUser.name) + " not found");
          for(int j = 0; j < currentEc.length; j++)
            System.out.print(currentEc[j] + " ");
          System.out.println();
          for(int j = 0; j < currentSi.length; j++)
            System.out.print(currentSi[j] + " ");
          System.out.println();
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }
        String nn = currentTableName;
        boolean isAuthorized = false;
        TableAuthorization ta = null;
//        System.out.println("SI " + siSize);
        if(siSize == 0)
        {
          ta = (TableAuthorization) tas.findElement(Table.dtJSHashCode(currentTableName));
          if(ta == null)
            isAuthorized = true;
          else if(((Boolean)ta.getValue(1, 1, webUser, language)).booleanValue())
            isAuthorized = true;
        }
        else
          for (int j = 0; j < siSize; j++)
          {
            nn = "" + currentSi[j] + "." + nn;
//            System.out.println("NN " + nn);
            ta = (TableAuthorization)tas.findElement(Table.dtJSHashCode(nn));
            if(ta == null)
            {
              isAuthorized = true;
              break;
            }
            if(!((Boolean)ta.getValue(1, 1, webUser, language)).booleanValue())
            {
              isAuthorized = false;
              break;
            }
            else
              isAuthorized = true;
          }

        int l = 0;
        if(ta != null)
          for(int j = 0; j < ta.columnSettings.size(); j++)
            if(!((Boolean)ta.columnSettings.at(j).getValue(1, ta.columnSettings.getHowToRead(), webUser, language)).
               booleanValue())
              l++;
        int[] hiddenColumnsByDefault = new int[l];
        l = 0;
        if(ta != null)
          for(int j = 0; j < ta.columnSettings.size(); j++)
            if(!((Boolean)ta.columnSettings.at(j).getValue(1, ta.columnSettings.getHowToRead(), webUser, language)).
               booleanValue())
            {
              hiddenColumnsByDefault[l] = j;
              l++;
            }

        outStream.writeBoolean(isAuthorized);
        String[] columnNames = currentTable.getExample().getColumnNames(currentTable.getHowToRead(), language, webUser);
        Class[] columnClasses = currentTable.getExample().getColumnClasses(currentTable.getHowToRead());
        int columnsAmount = columnNames.length - hiddenColumnsByDefault.length;
        outStream.writeInt(columnsAmount);
        for(int j = 0; j < columnNames.length; j++)
          if(!this.isColumnHidden(hiddenColumnsByDefault, j))
          {
            outStream.writeUTF(columnNames[j]);
            outStream.writeObject(columnClasses[j]);
          }
        File userSettingsFile = new File(Data.path + "\\" + new String(webUser.name) + "3" + userType);
        if(!userSettingsFile.exists())
          userSettingsFile.mkdir();
        String realCurrentTableName = currentTable.getName();
        String usedCurrentTableName = "";
        if(Character.isDigit(realCurrentTableName.charAt(realCurrentTableName.length() - 1)))
          for(int z = 0; z < realCurrentTableName.length(); z++)
          {
            if(Character.isDigit(realCurrentTableName.charAt(z)))
              break;
            usedCurrentTableName += realCurrentTableName.charAt(z);
          }
        userSettingsFile = new File(userSettingsFile.getAbsolutePath() + "\\" + usedCurrentTableName + "settings.dat");
        if(!userSettingsFile.exists())
          outStream.writeInt(0);
        else
        {
          try
          {
            ObjectInputStream userSettingsStream = new ObjectInputStream(new FileInputStream(userSettingsFile));
            int length = userSettingsStream.readInt();
            outStream.writeInt(length);
            for (int k = 0; k < length; k++)
              outStream.writeUTF(userSettingsStream.readUTF());
            userSettingsStream.close();
          }
          catch(Exception e)
          {
            e.printStackTrace();
            outStream.writeInt(0);
          }
        }

        String[] operations = null;
        int[] operationIndices = null;
        if(ta != null)
        {
          int operationNames = 0;
          for(int j = 9; j < 13; j++)
            if(((Boolean)ta.getValue(j, 1, webUser, language)).booleanValue() && currentTable.getExample().getOperationName(currentTable.getHowToRead(), j - 9, language) != null)
              operationNames++;
          operations = new String[operationNames];
          operationIndices = new int[operationNames];
          if(operationNames > 0)
          {
            int index = 0;
            for (int j = 9; j < 13; j++)
              if ( ( (Boolean) ta.getValue(j, 1, webUser, language)).booleanValue() && currentTable.getExample().getOperationName(currentTable.getHowToRead(), j - 9, language) != null)
              {
                operations[index] = currentTable.getExample().getOperationName(currentTable.getHowToRead(), j - 9, language);
                operationIndices[index] = j - 9;
                index++;
              }
          }
        }
        if(operations == null)
          outStream.writeInt(0);
        else
          outStream.writeInt(operations.length);
        if(operations != null)
        for(int j = 0; j < operations.length; j++)
        {
          outStream.writeUTF(operations[j]);
          outStream.writeInt(operationIndices[j]);
        }
        outStream.flush();
        if(!isAuthorized)
        {
          System.out.println("Table " + currentTableName + " " + this.longArrayToString(currentEc) + " " + this.intArrayToString(currentSi) + " " + "for WEB user " + new String(webUser.name) + " IS NOT AUTHORIZED");
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }

        boolean ropb = false;
        boolean rarsmb = false;
        ropb = inStream.readBoolean();
        if(ropb)
        {
// operation execute
          int rop = inStream.readInt();
//          System.out.println("ROP " + rop);
          code = inStream.readLong();
          try
          {
            lastSelectedCode.remove(currentPosition);
            lastSelectedCode.add(currentPosition, new Long(code));
          }
          catch(Exception e) {}
//          System.out.println("code " + code);
          boolean par = inStream.readBoolean();
//          System.out.println("parameters " + par);
          String[] parameters = null;
          boolean file = false;
          if(par)
          {
            int parl = inStream.readInt();
            parameters = new String[parl];
//            System.out.println("Parameters " + parl);
            for(int j = 0; j < parl; j++)
            {
              parameters[j] = inStream.readUTF();
//              System.out.println(parameters[j]);
              if(parameters[j].indexOf("c:\\imports") > -1)
                file = true;
            }
          }
            String message = currentTable.executeOperation(parameters, code, 0, rop, language, webUser.getValue(3, 1).toString(), new DateRepresentation(true));
//            System.out.println("Length " + message.length());
            if(message.length() > 40000)
              message = message.substring(0, 40000);
            outStream.writeUTF(message);
            outStream.flush();
            if(!file)
            {
              if(currentTable.lastOperationResult)
                Data.version++;
              if(currentTable.lastOperationResult)
              {
                Command command = null;
                if(siSize == 0)
                  command = new Command31(currentTableName, parameters, code, 0, rop, webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                else
                  command = new Command32(currentTableName, currentEc, currentSi, parameters, code, 0, rop,  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                if(siSize == 0)
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                else
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName);
              }
            }
        }
        else
          rarsmb = inStream.readBoolean();
        if(rarsmb)
        {
          int rarsm = inStream.readInt();
          switch(rarsm)
          {
            case 0:
              boolean par = inStream.readBoolean();
              if(par)
              {
                int parl = inStream.readInt();
                String[] parameters = new String[parl];
                for(int j = 0; j < parl; j++)
                  parameters[j] = inStream.readUTF();
                int[] ipci = currentTable.getExample().getInitialParametersColumnIndices(currentTable.getHowToRead());
                Object[] pars = new Object[parl];
                try
                {
                  for(int j = 0; j < ipci.length; j++)
                  {
                    Class c = columnClasses[ipci[j]];
                    if(c == String.class)
                      pars[j] = parameters[j];
                    else if(c == Integer.class)
                      pars[j] = new Integer(parameters[j]);
                    else if(c == Double.class)
                    {
                      int index = parameters[j].indexOf(",");
                      if(index != -1)
                        parameters[j] = parameters[j].replace(',', '.');
                      pars[j] = new Double(parameters[j]);
                    }
                    else if(c == Long.class)
                      pars[j] = new Long(parameters[j]);
                    else if(c == Boolean.class)
                      pars[j] = new Boolean(parameters[j].equals("1") || parameters[j].equals("true") || parameters[j].equals("on"));
                  }
                }
                catch(Exception e)
                {
                  outStream.writeUTF(e.toString());
                  outStream.flush();
                  break;
                }
                TableElement el = currentTable.getExample().newInstance(pars, currentTable.getHowToRead(), currentTable.getParentElement());
                String message = currentTable.addTableElementComplete(el, language, webUser.getValue(3, 1).toString(), new DateRepresentation(true));
                if(currentTable.lastOperationResult)
                  Data.version++;
                outStream.writeUTF(message);
                outStream.flush();
                if(currentTable.lastOperationResult)
                {
                  Command command = null;
                  if(siSize == 0)
                    command = new Command11(currentTableName, pars, webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                  else
                    command = new Command12(currentTableName, currentEc, currentSi, pars,  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                  if(siSize == 0)
                    Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                  else
                    Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName);
                }
              }
              break;
            case 1:
              code = inStream.readLong();
              try
              {
                lastSelectedCode.remove(currentPosition);
                lastSelectedCode.add(currentPosition, new Long(code));
              }
              catch(Exception e) {}
              String message = currentTable.removeTableElement(code, language, webUser.getValue(3, 1).toString(), new DateRepresentation(true));
              if(currentTable.lastOperationResult)
                Data.version++;
              outStream.writeUTF(message);
              outStream.flush();
              if(currentTable.lastOperationResult)
              {
                Command command = null;
                if(siSize == 0)
                  command = new Command13(currentTableName, code,  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                else
                  command = new Command14(currentTableName, currentEc, currentSi, code,  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                if(siSize == 0)
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                else
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName);
              }
              break;
            case 2:
              boolean radio = inStream.readBoolean();
              long[] selectedCodes = null;
              lastSelectedCode.remove(currentPosition);
              if(radio)
              {
                code = inStream.readLong();
                try
                {
                  lastSelectedCode.remove(currentPosition);
                  lastSelectedCode.add(currentPosition, new Long(code));
                }
                catch(Exception e) {}
              }
              else
              {
                String codes = inStream.readUTF();
//                System.out.println(codes);
                StringTokenizer st = new StringTokenizer(codes, ".");
                int codesSize = st.countTokens();
                selectedCodes = new long[codesSize];
                for(int j = 0; st.hasMoreTokens(); j++)
                  selectedCodes[j] = Long.parseLong(st.nextToken());
                if(selectedCodes.length > 0)
                {
                  try
                  {
                    lastSelectedCode.remove(currentPosition);
                    lastSelectedCode.add(currentPosition, new Long(selectedCodes[0]));
                  }
                  catch(Exception e) {}
                }
              }
              par = inStream.readBoolean();
              if(par)
              {
                int parl = inStream.readInt();
                String[] parameters = new String[parl];
                for(int j = 0; j < parl; j++)
                {
                  parameters[j] = inStream.readUTF();
                  System.out.println("par " + parameters[j]);
                }

                TableElement order = null;
                if(radio)
                  order = currentTable.findElement(code);
                else
                  order = currentTable.findElement(selectedCodes[0]);
                String[] cn = currentTable.getExample().getColumnNames(currentTable.getHowToRead(), language, webUser);
                TableAuthorization ta3 = null;
                if(siSize == 0)
                  ta3 = (TableAuthorization)webUser.getSubtable(0).findElement(Table.dtJSHashCode(currentTable.getName()));
                else
                {
                  nn = currentTableName;
                  for (int j = 0; j < siSize; j++)
                    nn = "" + currentSi[j] + "." + nn;
                  ta3 = (TableAuthorization)webUser.getSubtable(0).findElement(Table.dtJSHashCode(nn));
                }
                Table ca = ta3.getSubtable(0);
                int c = -1;
                Object[] pars = new Object[parl];
                int[] indices = new int[parl];
                try
                {
                  for(int j = 0; j < columnClasses.length; j++)
                  {
                    if(!((Boolean)ca.at(j).getValue(1, 1)).booleanValue())
                      continue;
                    if(!((Boolean)ca.at(j).getValue(2, 1)).booleanValue()) continue;
                    if(!order.isCellEditable(j, currentTable.getHowToRead()))
                      continue;
                    c++;
                    indices[c] = j;
                    Class cl = columnClasses[j];
                    if(cl == String.class)
                      pars[c] = parameters[c];
                    else if(cl == Integer.class)
                      pars[c] = new Integer(parameters[c]);
                    else if(cl == Double.class)
                    {
                      int index = parameters[j].indexOf(",");
                      if(index != -1)
                        parameters[j] = parameters[j].replace(',', '.');
                      pars[c] = new Double(parameters[c]);
                    }
                    else if(cl == Long.class)
                      pars[c] = new Long(parameters[c]);
                    else if(cl == Boolean.class)
                      pars[c] = new Boolean(parameters[c].equals("1") || parameters[c].equals("true") || parameters[c].equals("on"));
                  }
                }
                catch(Exception e)
                {
                  e.printStackTrace();
                  outStream.writeUTF(e.toString());
                  outStream.flush();
                  break;
                }
                message = "";
                for(int j = 0; j < pars.length; j++)
                {
                  if(radio)
                  {
                    if (order.getValue(indices[j], currentTable.getHowToRead(), webUser,
                        language).equals(pars[j]))
                      continue;
                    try
                    {
                      message += currentTable.setValueAt(pars[j], code, indices[j], language, webUser.getValue(3, 1).toString(), new DateRepresentation(true));
                    }
                    catch (Exception e)
                    {
                      e.printStackTrace();
                      message += e.toString();
                    }
                    if (currentTable.lastOperationResult)
                    {
                      Data.version++;
                      Command command = null;
                      if (siSize == 0)
                        command = new Command9(currentTableName, code, indices[j], pars[j],  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                      else
                        command = new Command10(currentTableName, currentEc, currentSi, code, indices[j], pars[j],  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                      if(siSize == 0)
                        Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                      else
                        Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName);
                    }
                  }
                  else
                  {
                    boolean ve = false;
                    for(int ind = 0; ind < selectedCodes.length; ind++)
                    {
//                      System.out.println("" + selectedCodes[ind]);
                      order = currentTable.findElement(selectedCodes[ind]);
                      if (order.getValue(indices[j], currentTable.getHowToRead(), webUser,
                          language).equals(pars[j]))
                       {
                         ve = true;
                         continue;
                       }
                      try
                      {
                        message += currentTable.setValueAt(pars[j], selectedCodes[ind], indices[j], language, webUser.getValue(3, 1).toString(), new DateRepresentation(true));
                      }
                      catch (Exception e)
                      {
                        e.printStackTrace();
                        message += e.toString();
                      }
                      if (currentTable.lastOperationResult)
                      {
                        Data.version++;
                        Command command = null;
                        if (siSize == 0)
                          command = new Command9(currentTableName, selectedCodes[ind], indices[j], pars[j],  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                        else
                          command = new Command10(currentTableName, currentEc, currentSi, selectedCodes[ind], indices[j], pars[j],  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                        if(siSize == 0)
                          Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                        else
                          Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName);
                      }
                    }
                    if(ve) continue;
                  }
                }
                outStream.writeUTF(message);
                System.out.println("Mesage " + message);
                outStream.flush();
              }
              break;
            case 3:
              code = inStream.readLong();
              try
              {
                lastSelectedCode.remove(currentPosition);
                lastSelectedCode.add(currentPosition, new Long(code));
              }
              catch(Exception e) {}
              message = "";
              TableElement element = null;
              if(siSize == 0)
              {
                element = currentTable.findElement(code);
                try
                {
                  message = currentTable.moveTableElement(code, 0, language, currentTable.getExample().canBeMovedTo(currentTable.getHowToRead()),
                          webUser.getValue(3, 1).toString(), new DateRepresentation(true));
                }
                catch(Exception e)
                {
                  message = e.toString();
                  currentTable.lastOperationResult = false;
                }
              }
              else
              {
/*                message = currentTable.moveTableElement(code, 0, language,
                    currentTable.getExample().canBeMovedTo(currentTable.getHowToRead()), ,
                    currentTable.getExample().canBeMovedPath(currentTable.getHowToRead()))*/

              }
//              System.out.println("Move " + message + " " + currentTable.lastOperationResult);
              if(currentTable.lastOperationResult)
                Data.version++;
              outStream.writeUTF(message);
              outStream.flush();
              if(currentTable.lastOperationResult)
              {
                Command command = null;
                if(siSize == 0)
                  command = new Command15(currentTableName, code, element, 0, currentTable.getExample().canBeMovedTo(currentTable.getHowToRead()),  webUser.getValue(3, 1).toString(), new DateRepresentation(true), Data.version);
                else
                {
                  break;
//                  command = new Command14(tableName, ec, si, code, Data.version);
                }
//                System.out.println("Announcing");
                if(siSize == 0)
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName), null, null, currentTableName);
                else
                  Data.commander.announce(command, new String(webUser.name), new String(webUser.password), language, true, Data.db.getTable(currentTableName, currentEc, currentSi), currentEc, currentSi, currentTableName
                                          );
              }
              break;
          }
        }
        if(op)
        {
          int pSize = inStream.readInt();
          String[] parameters = new String[pSize];
          for(int k = 0; k < parameters.length; k++)
            parameters[k] = inStream.readUTF();
          outStream.writeUTF(currentTable.getExample().getOperationConfirmation(code, operation,
              currentTable.getParentElement(), language, currentTable.getHowToRead(), webUser, parameters));
        }
        else
        switch(arsm)
        {
          case 0:
            outStream.writeUTF(currentTable.getExample().getAddConfirmation(language, currentTable.getHowToRead(), webUser));
            break;
          case 1:
            outStream.writeUTF(currentTable.getExample().getRemoveConfirmation(language, currentTable.getHowToRead(), webUser));
            break;
          case 2:
            outStream.writeUTF(currentTable.getExample().getSetConfirmation(code, currentTable.getParentElement(), language, currentTable.getHowToRead(), webUser));
            break;
          case 3:
            outStream.writeUTF(currentTable.getExample().getMoveConfirmation(code, currentTable.getParentElement(), language, currentTable.getHowToRead(), webUser));
            break;
        }
        outStream.flush();
        if(op)
        {
          System.out.println("Table " + currentTableName + " " + this.longArrayToString(currentEc) + " " + intArrayToString(currentSi) + " " + "for WEB user " + new String(webUser.name) + " operation " + operation);
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }
        if(arsm > -1)
        {
          System.out.println("Table " + currentTableName + " " + this.longArrayToString(currentEc) + " " + this.intArrayToString(currentSi) + " " + "for WEB user " + new String(webUser.name) + " ARSM " + arsm);
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }

        int sortCriteria = -1;
        boolean ascending = false;
        if(!back && linkcol.equals("-1"))
        {
          sortCriteria = inStream.readInt();
          ascending = inStream.readBoolean();
          page = inStream.readInt();
        }
        else
        {
          try
          {
            sortCriteria = ( (Integer) lastSortCriteria.get(currentPosition)).intValue();
            ascending = ((Boolean)lastAscending.get(currentPosition)).booleanValue();
          }
          catch(Exception e)
          {
            e.printStackTrace();
            currentPosition--;
            this.userCurrentPosition.put(userName, new Integer(currentPosition));
            try
            {
              sortCriteria = ((Integer)lastSortCriteria.get(currentPosition)).intValue();
              ascending = ((Boolean)lastAscending.get(currentPosition)).booleanValue();
            }
            catch(Exception ee)
            {
              sortCriteria = 0;
              ascending = false;
            }
          }
//          System.out.println("Page " + page);
        }
        int viewSize = inStream.readInt();
        int hiddenColumnsSize = 0;
        String[] hiddenColumns = null;
        realCurrentTableName = currentTable.getName();
        usedCurrentTableName = "";
        if(Character.isDigit(realCurrentTableName.charAt(realCurrentTableName.length() - 1)))
          for(int z = 0; z < realCurrentTableName.length(); z++)
          {
            if(Character.isDigit(realCurrentTableName.charAt(z)))
              break;
            usedCurrentTableName += realCurrentTableName.charAt(z);
          }
        if(!back && linkcol.equals("-1"))
        {
          hiddenColumnsSize = inStream.readInt();
          hiddenColumns = new String[hiddenColumnsSize];
          for (int j = 0; j < hiddenColumnsSize; j++)
            hiddenColumns[j] = inStream.readUTF();
          userSettingsFile = new File(Data.path + "\\" + new String(webUser.name) + "3" + userType);
          if (!userSettingsFile.exists())
            userSettingsFile.mkdir();
          userSettingsFile = new File(userSettingsFile.getAbsolutePath() + "\\" + usedCurrentTableName +
                                      "settings.dat");
          if (!userSettingsFile.exists())
            userSettingsFile.createNewFile();
          ObjectOutputStream userSettingsOutputStream = new ObjectOutputStream(new FileOutputStream(
              userSettingsFile));
          userSettingsOutputStream.writeInt(hiddenColumns.length);
          for (int k = 0; k < hiddenColumns.length; k++)
            userSettingsOutputStream.writeUTF(hiddenColumns[k]);
          userSettingsOutputStream.close();
        }
        else
        {
          userSettingsFile = new File(Data.path + "\\" + new String(webUser.name) + "3" + userType);
          if (!userSettingsFile.exists())
          {
            userSettingsFile.mkdir();
            hiddenColumns = new String[0];
          }
          userSettingsFile = new File(userSettingsFile.getAbsolutePath() + "\\" + usedCurrentTableName +
                                      "settings.dat");
          if (!userSettingsFile.exists())
            userSettingsFile.createNewFile();
          ObjectInputStream userSettingsInputStream = new ObjectInputStream(new FileInputStream(
              userSettingsFile));
          if(userSettingsFile.length() > 0)
          {
            hiddenColumnsSize = userSettingsInputStream.readInt();
            hiddenColumns = new String[hiddenColumnsSize];
            for (int k = 0; k < hiddenColumns.length; k++)
              hiddenColumns[k] = userSettingsInputStream.readUTF();
          }
          userSettingsInputStream.close();
        }
        boolean openx = inStream.readBoolean();
        if(!openx)
        {
          try
          {
            lastSelectedCode.remove(currentPosition - 1);
            lastSelectedCode.add(currentPosition - 1, new Long(currentEc[currentEc.length - 1]));
          }
          catch(Exception e) {}
        }
        int columnPositionsLength = 0;
        int[] columnPositions = null;
        if(!back && linkcol.equals("-1"))
        {
          columnPositionsLength = inStream.readInt();
          columnPositions = new int[columnPositionsLength];
          for (int k = 0; k < columnPositionsLength; k++)
            columnPositions[k] = inStream.readInt();
        }
        int filterColumn = -1;
        String filterValue = "-1";
        boolean filterFrom = false;
        if(!back)
        {
          filterColumn = inStream.readInt();
          filterValue = inStream.readUTF();
          filterFrom = inStream.readBoolean();
        }
        if(filterValue.equals("_"))
          filterValue = "";

        currentTable = currentTable.filteredByDefault(ta, nn, userType);
        if(currentTable.getExample() instanceof element.ColumnAuthorizationExample)
        {
          sortCriteria = 3;
          ascending = true;
        }
        if(!back)
        {
          try
          {
            lastPages.add(currentPosition, new Integer(page));
          }
          catch(Exception e)
          {
//            e.printStackTrace();
            currentPosition--;
            this.userCurrentPosition.put(userName, new Integer(currentPosition));
          }
//          System.out.println("adding page " + page);
          if(lc > -1)
            try
            {
              lastFilterColumn.add(currentPosition, new Integer(Integer.MAX_VALUE));
            }
            catch(Exception e) {}
          else
            try
            {
              lastFilterColumn.add(currentPosition, new Integer(filterColumn));
            }
            catch(Exception e) {}
          if(lc > -1)
            try
            {
              lastFilterValue.add(currentPosition, "" + linkCode);
            }
            catch(Exception e) {}
          else
            try
            {
              lastFilterValue.add(currentPosition, filterValue);
            }
            catch(Exception e) {}
          try
          {
            lastFilterFrom.add(currentPosition, new Boolean(filterFrom));
            lastSortCriteria.add(currentPosition, new Integer(sortCriteria));
            lastAscending.add(currentPosition, new Boolean(ascending));
          }
          catch(Exception e) {}
        }
        else
        {
          filterColumn = ((Integer)lastFilterColumn.get(currentPosition)).intValue();
          filterValue = lastFilterValue.get(currentPosition).toString();
          filterFrom = ((Boolean)lastFilterFrom.get(currentPosition)).booleanValue();
        }
        Object[] result = null;
        try
        {
          result = this.getView(currentTable, currentTableName, sortCriteria, ascending, page,
                                hiddenColumns, columnPositions, hiddenColumnsByDefault, filterColumn,
                                filterValue, filterFrom, language, new String(webUser.name), userType,
                                webUser, expb, viewSize, lc, linkCode, ta, openx);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          outStream.writeBoolean(false);
          try
          {
            inStream.close();
          }
          catch(Exception ee) {}
          try
          {
            outStream.close();
          }
          catch(Exception ee) {}
          try
          {
            webSocket.close();
          }
          catch(Exception ee) {}
          continue;
        }
        outStream.writeBoolean(true);
        outStream.flush();
        ArrayList view = (ArrayList)result[0];
        columnPositions = (int[])result[5];
        Class[] viewColumnClasses = (Class[])result[6];
        int lastPage = ((Integer)result[1]).intValue();
        outStream.writeInt(lastPage);
        String titleAdd = "";
        Table ct = currentTable;
        while(ct != null && ct.getParentElement() != null)
        {
          titleAdd += " " + ct.getParentElement().getValue(0, 1, webUser, language).toString();
          ct = ct.getParentElement().getParentTable(ct.getHowToRead());
        }
        outStream.writeUTF(currentTable.getExample().getTitle(language, currentTable.getHowToRead(), webUser) + titleAdd);
        outStream.writeInt(columnPositions.length);
        for(int k = 0; k < columnPositions.length; k++)
          outStream.writeInt(columnPositions[k]);
        for(int k = 0; k < columnPositions.length; k++)
          outStream.writeObject(viewColumnClasses[k]);
        outStream.writeInt(view.size() - 1);
        ArrayList codes = (ArrayList)result[2];
        for(int j = 0; j < codes.size(); j++)
          outStream.writeLong(((Long)codes.get(j)).longValue());
        ArrayList colors = (ArrayList)result[3];
        ArrayList links = (ArrayList)result[4];
        ArrayList subtables = new ArrayList();
        ArrayList subtableIndices = new ArrayList();
        for(int j = 0; currentTable.getExample().hasSubtable(currentTable.getHowToRead(), j); j++)
        {
          String tn = j + "." + nn;
          TableAuthorization ta2 = (TableAuthorization)tas.findElement(Table.dtJSHashCode(tn));
          if(ta2 == null || ((Boolean)ta2.getValue(1, 1, webUser, language)).booleanValue())
          {
            Table st = currentTable.getExample().getSubtable(currentTable.getHowToRead(), j);
            subtables.add(st.getExample().getTitle(language, st.getHowToRead(), webUser));
            subtableIndices.add(new Integer(j));
          }
        }
        outStream.writeInt(subtables.size());
        for(int j = 0; j < subtables.size(); j++)
        {
          outStream.writeUTF(subtables.get(j).toString());
          outStream.writeInt(((Integer)subtableIndices.get(j)).intValue());
        }
        String[] cols = (String[])view.get(0);
        outStream.writeInt(cols.length);
        outStream.flush();
        for(int k = 0; k < cols.length; k++)
          try
          {
            outStream.writeUTF(cols[k]);
          }
          catch(Exception e)
          {
//            e.printStackTrace();
            outStream.writeUTF("");
          }
        for(int j = 0; j < view.size(); j++)
        {
          String[] values = (String[])view.get(j);
          String[] cellColors = (String[])colors.get(j);
          String[] linkTables = (String[])links.get(j);
          for(int k = 0; k < cols.length; k++)
          {
            try
            {
              outStream.writeUTF(values[k]);
              outStream.writeUTF(cellColors[k]);
              if(linkTables[k] == null)
                outStream.writeBoolean(false);
              else
                outStream.writeBoolean(true);
            }
            catch(Exception e)
            {
              e.printStackTrace();
              outStream.writeUTF("");
              outStream.writeUTF("black");
              outStream.writeBoolean(false);
            }
//            System.out.println(values[k]);
          }
        }
        outStream.writeBoolean(((Boolean)ta.getValue(4, 1, webUser, language)).booleanValue() && currentTable.getExample().isAddEnabled(currentTable.getHowToRead()));
        outStream.writeBoolean(((Boolean)ta.getValue(3, 1, webUser, language)).booleanValue() && currentTable.getExample().isRemoveEnabled(currentTable.getHowToRead()));
        outStream.writeBoolean(((Boolean)ta.getValue(2, 1, webUser, language)).booleanValue() && currentTable.getExample().isSetEnabled(currentTable.getHowToRead()));
        outStream.writeBoolean(((Boolean)ta.getValue(8, 1, webUser, language)).booleanValue() && currentTable.getExample().isMoveEnabled(currentTable.getHowToRead()));
        outStream.flush();
        inStream.readBoolean();
        try
        {
          inStream.close();
        }
        catch(Exception ee) {}
        try
        {
          outStream.close();
        }
        catch(Exception ee) {}
        try
        {
          webSocket.close();
        }
        catch(Exception ee) {}
      }
      catch(Exception e)
      {
        e.printStackTrace();
        try
        {
          inStream.close();
        }
        catch(Exception ee) {}
        try
        {
          outStream.close();
        }
        catch(Exception ee) {}
        try
        {
          webSocket.close();
        }
        catch(Exception ee) {}
        continue;
      }
    }
  }

  private String longArrayToString(long[] ec)
  {
    String s = "";
    for(int i = 0; i < ec.length; i++)
      s += ec[i] + " ";
    return s;
  }

  private String intArrayToString(int[] si)
  {
    String s = "";
    for(int i = 0; i < si.length; i++)
      s += si[i] + " ";
    return s;
  }

  private String getTableName(Table table)
  {
    if(table.parentTable == null) return table.getName();
    return this.getTableName(table.parentTable);
  }

  private long[] getEC(Table table, long[] ec)
  {
    if(table.getParentElement() == null) return ec;
    long[] ec1 = new long[ec.length + 1];
    for(int i = 0; i < ec.length; i++)
      ec1[i + 1] = ec[i];
    ec1[0] = table.getParentElement().getCode().longValue();
    if(table.getParentElement().getParentTable(table.getHowToRead()) == null)
      return ec1;
    return this.getEC(table.getParentElement().getParentTable(table.getHowToRead()), ec1);
  }

  private int[] getSI(Table table, int[] si)
  {
    if(table.parentTable == null) return si;
    int[] si1 = new int[si.length + 1];
    for(int i = 0; i < si.length; i++)
      si1[i + 1] = si[i];
    int i = 0;
    for(; table.getParentElement().getSubtable(i) != null; i++)
    {
      if(table.getParentElement().getSubtable(i).getExample() == table.getExample())
        break;
    }
    si1[0] = i;
    if(table.getParentElement().getParentTable(table.getHowToRead()) == null)
      return si1;
    return this.getSI(table.getParentElement().getParentTable(table.getHowToRead()), si1);
  }

  private Object[] getTable(String tableName, long[] ec, int[] si, User webUser, int userType) throws IOException
  {
    Table ct = null;
    try
    {
      ct = Data.db.getTable(tableName, ec, si, webUser, userType);
    }
    catch(NullPointerException e)
    {
      if(ec.length < 2) return null;
      long[] ec2 = new long[ec.length - 1];
      int[] si2 = new int[si.length - 1];
      for(int i = 0; i < ec2.length; i++)
      {
        ec2[i] = ec[i];
        si2[i] = si[i];
      }
      return this.getTable(tableName, ec2, si2, webUser, userType);
    }
    Object[] result = new Object[3];
    result[0] = ct;
    result[1] = ec;
    result[2] = si;
    return result;
  }

  private Object[] getColumnNames(String[] toHide, int[] hiddenColumnsByDefault, String[] columnNames)
  {
    Object[] res = new Object[2];
    ArrayList cn = new ArrayList();
    ArrayList ci = new ArrayList();
    for(int i = 0; i < columnNames.length; i++)
      if(!this.isColumnHidden(hiddenColumnsByDefault, i))
      {
        cn.add(columnNames[i]);
        ci.add(new Integer(i));
      }
    for(int i = 0; i < toHide.length; i++)
    {
      int pos = -1;
      for(int j = 0; j < cn.size(); j++)
        if(cn.get(j).toString().equals(toHide[i]))
        {
          pos = j;
          break;
        }
      if(pos == -1) continue;
      cn.remove(pos);
      ci.remove(pos);
    }
    String[] coln = new String[cn.size()];
    for(int i = 0; i < cn.size(); i++)
      coln[i] = cn.get(i).toString();
    int[] coli = new int[ci.size()];
    for(int i = 0; i < ci.size(); i++)
      coli[i] = ((Integer)ci.get(i)).intValue();
    res[0] = coln;
    res[1] = coli;
    return res;
  }

  private boolean isColumnHidden(int[] hiddenColumns, int column)
  {
    for(int i = 0; i < hiddenColumns.length; i++)
      if(hiddenColumns[i] == column)
        return true;
    return false;
  }

  private int getColumnPos(String[] columnNames, String column)
  {
    int i;
    for(i = 0; i < columnNames.length; i++)
      if(columnNames[i].equals(column)) return i;
    return -1;
  }

  private Object[] getView(Table table, String tableName, int sortCriteria, boolean ascending, int page, String[] toHide, int[] columnPositions, int[] hiddenColumnsByDefault, int filterColumn, String filterValue, boolean filterFrom, String language, String userName, int userType, User webUser, boolean export, int viewSize, int lc, long linkCode, TableAuthorization ta, boolean openx)
  {
    long currentTime = System.currentTimeMillis();
    System.out.println("" + currentTime);
//    for(int i = 0; i < columnPositions.length; i++)
//      System.out.println("Sent column position " + columnPositions[i]);
    boolean filterCode = filterColumn == Integer.MAX_VALUE;
    boolean allZero = true;
    if(columnPositions == null)
      columnPositions = new int[0];
    for(int i = 0; i < columnPositions.length; i++)
      if(columnPositions[i] != 0)
      {
        allZero = false;
        break;
      }
//    System.out.println("All zero " + allZero);
    ArrayList acolumnPositions = new ArrayList();
    for(int i = 0; i < ta.columnSettings.size(); i++)
    {
      if(this.isColumnHidden(hiddenColumnsByDefault, i)) continue;
//      System.out.println("Settings column " + ta.columnSettings.at(i).getValue(0, 1, webUser, language).toString());
      if(this.getColumnPos(toHide, ta.columnSettings.at(i).getValue(0, 1, webUser, language).toString()) > -1)
      {
//        System.out.println("hidden " + i);
        continue;
      }
//      System.out.println("column positions length " + columnPositions.length + " column positions size " + acolumnPositions.size());
      if(columnPositions.length > acolumnPositions.size())
      {
//        System.out.println("column positions length " + columnPositions.length + " column positions size " + acolumnPositions.size());
//        System.out.println("SETTING " + ta.columnSettings.at(i).getValue(3, 1).toString() + " to ");
        if(!allZero && openx)
        try
        {
//          System.out.println("" + columnPositions[acolumnPositions.size()]);
          ta.columnSettings.at(i).setValue(3, new Integer(columnPositions[acolumnPositions.size()]), "en", 1, null, null);
//          System.out.println(ta.columnSettings.at(i).getValue(0, 1, webUser, language).toString() + " " + ta.columnSettings.at(i).getValue(3, 1).toString());
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      acolumnPositions.add(ta.columnSettings.at(i).getValue(3, 1));
//      System.out.println(ta.columnSettings.at(i).getValue(3, 1).toString());
    }
    int[] colPos = new int[acolumnPositions.size()];
    for(int i = 0; i < colPos.length; i++)
      colPos[i] = ((Integer)acolumnPositions.get(i)).intValue();
    for(int k = 0; k < colPos.length; k++)
    for(int i = 0; i < colPos.length; i++)
    {
//      if(colPos[i] == 0) continue;
      int ccp = colPos[i];
      int nextPrevious = -1;
      boolean valueExistsBefore = false;
      boolean valueExistsAfter = false;
      for(int j = 0; j < colPos.length; j++)
      {
        if(j == i) continue;
        if(colPos[j] == ccp && j < i)
        {
          valueExistsBefore = true;
          break;
        }
        else if(colPos[j] == ccp && j > i)
        {
          valueExistsAfter = true;
          break;
        }
        if(colPos[j] > ccp) continue;
        nextPrevious = Math.max(nextPrevious, colPos[j]);
      }
      if(valueExistsBefore)
        colPos[i]++;
      else
        colPos[i] = nextPrevious + 1;
    }
//    for(int i = 0; i < colPos.length; i++)
//      System.out.println("colPos" + colPos[i]);
    Class[] columnClasses = table.getExample().getColumnClasses(table.getHowToRead());
//    System.out.println("Column classes " + columnClasses.length);
    Object[] result = new Object[7];
    result[5] = colPos;
    Class[] viewColumnClasses = new Class[colPos.length];
//    System.out.println("sortcriteria " + sortCriteria);
    boolean sortCriteriaFound = false;
    boolean filterColumnFound = filterCode;
    for(int i = 0, c = 0; i < columnClasses.length; i++)
    {
//      System.out.println("i " + i + " " + columnClasses[i].toString());
      if(this.isColumnHidden(hiddenColumnsByDefault, i)) continue;
      try
      {
        if (this.getColumnPos(toHide, ta.columnSettings.at(i).getValue(0, 1, webUser, language).toString()) >
            -1)
          continue;
      }
      catch(Exception e)
      {
//        e.printStackTrace();
        continue;
      }
//      System.out.println("c " + c + " colPos[c] " + colPos[c]);
      viewColumnClasses[colPos[c]] = columnClasses[i];
      if(!sortCriteriaFound && colPos[c] == sortCriteria)
      {
//        System.out.println("c " + c);
        sortCriteria = i;
        sortCriteriaFound = true;
      }
      if(filterColumn > -1 && !filterColumnFound && colPos[c] == filterColumn)
      {
//        System.out.println("c " + c);
        filterColumn = i;
        filterColumnFound = true;
      }
      c++;
    }
//    System.out.println("sortcriteria " + sortCriteria);
//    System.out.println("filterColumn " + sortCriteria);
    result[6] = viewColumnClasses;
    WritableWorkbook wb = null;
    WritableSheet sheet = null;
    Label label;
    jxl.write.Number number;
    jxl.write.NumberFormat nformatter3 = new jxl.write.NumberFormat("#,##0.000", jxl.write.NumberFormat.COMPLEX_FORMAT);
    WritableCellFormat formatNumber3 = new WritableCellFormat(nformatter3);
    jxl.write.NumberFormat nformatter2 = new jxl.write.NumberFormat("#,##0.00", jxl.write.NumberFormat.COMPLEX_FORMAT);
    WritableCellFormat formatNumber2 = new WritableCellFormat(nformatter2);
    WritableCellFormat dateFormat = new WritableCellFormat(new DateFormat("dd.mm.yyyy"));
    if(export)
    {
      try
      {
        File dirFile = new File(Consts.www);
        if(!dirFile.exists())
          dirFile.mkdir();
        dirFile = new File(Consts.www + "\\rscgate");
        if(!dirFile.exists())
          dirFile.mkdir();
        dirFile = new File(Consts.www + "\\rscgate\\exports");
        if(!dirFile.exists())
          dirFile.mkdir();
        dirFile = new File(Consts.www + "\\rscgate\\exports\\" + new String(webUser.name) + userType);
        if(!dirFile.exists())
          dirFile.mkdir();
        final File ef = new File(Consts.www + "\\rscgate\\exports\\" + new String(webUser.name) + userType + "\\" + tableName + ".xls");
        if(ef.exists())
          ef.delete();
        Thread deleteThread = new Thread()
        {
          public void run()
          {
            try
            {
              Thread.sleep(180000);
            }
            catch(Exception e) {}
            try
            {
              ef.delete();
            }
            catch(Exception e) {}
          }
        };
        deleteThread.start();
        wb = Workbook.createWorkbook(ef);
        sheet = wb.createSheet(table.getExample().getTitle(language, table.getHowToRead(), webUser), 0);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    Object[] expressions = null;
    if(lc > -1 || filterCode)
    {
      expressions = new Object[1];
      if(filterCode)
        linkCode = Long.parseLong(filterValue);
      expressions[0] = table.findElement(linkCode);
    }
    else
      expressions = table.toArray();
    String[] columnNames = table.getExample().getColumnNames(table.getHowToRead(), language, webUser);
    boolean toFilter = filterColumn > -1;
//    if(filterFrom && toFilter)
//      sortCriteria = filterColumn;
    int sc = filterColumn;
//    System.out.println("SC " + sc);
    if(sc != -1 && !filterCode)
    {
//      System.out.println("Sorting " + sc);
      Arrays.sort(expressions, new BasicComparator(sc, ascending, table.getHowToRead(), webUser, language));
    }
//    int sca = this.getColumnPos(columnNames, sortCriteria);
    Integer scao = new Integer(sortCriteria);
    String key = new String(webUser.name) + table.getName();
    Hashtable sortCriterias = (Hashtable)this.userSortCriterias.get(userName);
    ArrayList scos = (ArrayList)sortCriterias.get(key);
    if(scos == null)
    {
      scos = new ArrayList();
      sortCriterias.put(key, scos);
      if(sortCriteria != -1)
        scos.add(scao);
    }
    else if(scos.size() > 0 && !filterCode)
    {
//      System.out.println("Sorting...");
      for(int i = 0; i < scos.size(); i++)
      {
        int scs = ((Integer)scos.get(i)).intValue();
        if(scs == sortCriteria) continue;
//        System.out.println("Sorting " + scs);
        Arrays.sort(expressions,
                    new BasicComparator(scs, ascending, table.getHowToRead(),
                                        webUser, language));
      }
      if(!scos.contains(scao))
        scos.add(scao);
      else
        scos.add(0, scos.remove(scos.size() - 1));
    }
    sc = sortCriteria;
    if(sortCriteria != filterColumn && sc != -1 && !filterCode)
    {
//      System.out.println("Sorting " + sc);
      Arrays.sort(expressions, new BasicComparator(sc, ascending, table.getHowToRead(), webUser, language));
    }
    ArrayList view = new ArrayList();
    ArrayList codes = new ArrayList();
    ArrayList colors = new ArrayList();
    ArrayList links = new ArrayList();
    codes.add(new Long(-1));
    int additionalRows = 0;
    Object[] res = this.getColumnNames(toHide, hiddenColumnsByDefault, columnNames);
    String[] cn = (String[])res[0];
    String[] headerColors = new String[cn.length];
    for(int i = 0; i < headerColors.length; i++)
      headerColors[i] = "";
    colors.add(headerColors);
    links.add(headerColors);
    int[] ci = (int[])res[1];
    int[] fractionDigits = table.getExample().getFractionDigits(table.getHowToRead());
    String[] cn2 = new String[cn.length];
//    System.out.println("CN length " + cn.length);
    for(int i = 0; i < cn.length; i++)
    {
//      System.out.println("colPos " + colPos[i]);
//      System.out.println(cn[i]);
      try
      {
        cn2[colPos[i]] = cn[i];
      }
      catch(Exception e)
      {
//        e.printStackTrace();
        break;
      }
    }
//    for(int i = 0; i < cn2.length; i++)
//      System.out.println(cn2[i]);
    view.add(cn2);
    NumberFormat formatter = null;
    if(language.equals("en"))
      formatter = NumberFormat.getInstance(Locale.ENGLISH);
    else
      formatter = NumberFormat.getInstance(Locale.GERMAN);
    int start = (page - 1) * viewSize + 1;
    int finish = start + viewSize + 1;
    if(start >= expressions.length)
    {
      start = 0;
      finish = start + viewSize + 1;
    }
    if(sheet != null)
    {
      for(int j = 0, k = 0; j < columnNames.length; j++)
      {
        if (!this.isColumnHidden(ci, j))
          continue;
        label = new Label(k, 0, cn2[k]);
        k++;
        try
        {
          sheet.addCell(label);
        }
        catch(Exception xe)
        {
          xe.printStackTrace();
          return null;
        }
      }
//      Hashtable sums = new Hashtable();
      if(toFilter)
      {
//        Object[] lastSums = new Object[columnNames.length];
        for(int i = 0, c = 1; i < expressions.length; i++)
        {
          TableElement ce = (TableElement)expressions[i];
          String[] values = new String[cn.length];
          String[] cellColors = new String[cn.length];
          String[] linkTables = new String[cn.length];
          boolean filtered = false;
          for(int j = 0, k = 0; j < columnNames.length; j++)
          {
            if(!this.isColumnHidden(ci, j)) continue;
            values[colPos[k]] = ce.getValue(j, table.getHowToRead(), webUser, language).toString();
            cellColors[colPos[k]] = ce.getCellColor(table.getHowToRead(), j);
            if(columnClasses[j] == Double.class)
            {
              if(fractionDigits != null && j < fractionDigits.length)
              {
                formatter.setMinimumFractionDigits(fractionDigits[j]);
                formatter.setMaximumFractionDigits(fractionDigits[j]);
              }
              double cuv = ( (Double) ce.getValue(j, table.getHowToRead(), webUser, language)).doubleValue();
              values[colPos[k]] = formatter.format(cuv);
              if(j == filterColumn)
              {
                double fV = 0;
                try
                {
                  fV = formatter.parse(filterValue).doubleValue();
                }
                catch (Exception e)
                {
                  e.printStackTrace();
                }
                if ((filterFrom && cuv < fV) || (!filterFrom && cuv != fV))
                {
                  filtered = true;
                  sheet.removeRow(c);
                  break;
                }
              }
              number = null;
              try
              {
                number = new jxl.write.Number(colPos[k], c, formatter.parse(values[colPos[k]]).doubleValue());
                if(fractionDigits[j] == 2)
                  number.setCellFormat(formatNumber2);
                else if(fractionDigits[j] == 3)
                  number.setCellFormat(formatNumber3);
              }
              catch(Exception e)
              {
                e.printStackTrace();
                return null;
              }
              try
              {
                sheet.addCell(number);
                if(i == expressions.length - 1)
                {
                  String column = this.alphabet[colPos[k]];
                  String startRow = "2";
                  String endRow = "" + (c + 1);
                  Formula fr = new Formula(colPos[k], c + 2, "SUM(" + column + startRow + ":" + column + endRow + ")");
                  if(fractionDigits[j] == 2)
                    fr.setCellFormat(formatNumber2);
                  else if(fractionDigits[j] == 3)
                    fr.setCellFormat(formatNumber3);
                  sheet.addCell(fr);
                }
              }
              catch(Exception xe)
              {
                xe.printStackTrace();
                return null;
              }
              k++;
              continue;
            }
            else if(columnClasses[j] == Integer.class || columnClasses[j] == Long.class)
            {
              long cuv = ((Number)ce.getValue(j, table.getHowToRead(), webUser, language)).longValue();
              if(j == filterColumn)
              {
                long fV = 0;
                try
                {
                  fV = Long.parseLong(filterValue);
                }
                catch (Exception e)
                {
                  e.printStackTrace();
                }
                if ((filterFrom && cuv < fV) || (!filterFrom && cuv != fV))
                {
                  filtered = true;
                  sheet.removeRow(c);
                  break;
                }
              }
              number = null;
              try
              {
                number = new jxl.write.Number(colPos[k], c, cuv);
                if(fractionDigits[j] == 2)
                  number.setCellFormat(formatNumber2);
                else if(fractionDigits[j] == 3)
                  number.setCellFormat(formatNumber3);
              }
              catch(Exception e)
              {
                e.printStackTrace();
                return null;
              }
              try
              {
                sheet.addCell(number);
                if(i == expressions.length - 1 && columnClasses[j] == Integer.class)
                {
                  String column = this.alphabet[colPos[k]];
                  String startRow = "2";
                  String endRow = "" + (c + 1);
                  Formula fr = new Formula(colPos[k], c + 2, "SUM(" + column + startRow + ":" + column + endRow + ")");
                  if(fractionDigits[j] == 2)
                    fr.setCellFormat(formatNumber2);
                  else if(fractionDigits[j] == 3)
                    fr.setCellFormat(formatNumber3);
                  sheet.addCell(fr);
                }
              }
              catch(Exception xe)
              {
                xe.printStackTrace();
                return null;
              }
              k++;
              continue;
            }
            else if(columnClasses[j] == Boolean.class)
            {
              if(((Boolean)ce.getValue(j, table.getHowToRead(), webUser, language)).booleanValue())
                values[colPos[k]] = "V";
              else
                values[colPos[k]] = "O";
            }
            if(j == filterColumn)
            {
              if(filterValue.length() == 0)
              {
                if(values[colPos[k]].length() > 0)
                {
                  filtered = true;
                  sheet.removeRow(c);
                  break;
                }
              }
              else if((filterFrom && values[colPos[k]].compareTo(filterValue) < 0) || (!filterFrom && values[colPos[k]].toUpperCase().indexOf(filterValue.toUpperCase()) < 0))
              {
                filtered = true;
                sheet.removeRow(c);
                break;
              }
            }
            label = new Label(colPos[k], c, values[colPos[k]]);
            label.setCellFormat(dateFormat);
            try
            {
              sheet.addCell(label);
            }
            catch(Exception xe)
            {
              xe.printStackTrace();
              return null;
            }
            k++;
          }
          if(filtered) continue;
          c++;
        }
      }
      else
      {
        for(int i = 0, c = 1; i < expressions.length; i++)
        {
          TableElement ce = (TableElement)expressions[i];
          String[] values = new String[cn.length];
          String[] cellColors = new String[cn.length];
          String[] linkTables = new String[cn.length];
          for(int j = 0, k = 0; j < columnNames.length; j++)
          {
            if(!this.isColumnHidden(ci, j)) continue;
            values[colPos[k]] = ce.getValue(j, table.getHowToRead(), webUser, language).toString();
            cellColors[colPos[k]] = ce.getCellColor(table.getHowToRead(), j);
            Table rt = ce.getReferenceTable(j, table.getHowToRead());
            if(rt == null)
              linkTables[colPos[k]] = null;
            else
              linkTables[colPos[k]] = rt.getName();
            if(columnClasses[j] == Double.class)
            {
              if(fractionDigits != null && j < fractionDigits.length)
              {
                formatter.setMinimumFractionDigits(fractionDigits[j]);
                formatter.setMaximumFractionDigits(fractionDigits[j]);
              }
              double cuv = ( (Double) ce.getValue(j, table.getHowToRead(), webUser, language)).doubleValue();
              values[colPos[k]] = formatter.format(cuv);
/*
              Double cv = (Double)sums.get(new Integer(j));
              if(cv == null)
              {
                cv = new Double(cuv);
                sums.put(new Integer(j), cv);
              }
              else
              {
                cv = new Double(cuv + cv.doubleValue());
                sums.put(new Integer(j), cv);
              }*/
              number = null;
              try
              {
                number = new jxl.write.Number(colPos[k], c, formatter.parse(values[colPos[k]]).doubleValue());
                if(fractionDigits[j] == 2)
                  number.setCellFormat(formatNumber2);
                else if(fractionDigits[j] == 3)
                  number.setCellFormat(formatNumber3);
              }
              catch(Exception e)
              {
                e.printStackTrace();
                return null;
              }
              try
              {
                sheet.addCell(number);
                if(i == expressions.length - 1)
                {
                  String column = this.alphabet[colPos[k]];
                  String startRow = "2";
                  String endRow = "" + (c + 1);
                  Formula fr = new Formula(colPos[k], c + 2, "SUM(" + column + startRow + ":" + column + endRow + ")");
                  if(fractionDigits[j] == 2)
                    fr.setCellFormat(formatNumber2);
                  else if(fractionDigits[j] == 3)
                    fr.setCellFormat(formatNumber3);
                  sheet.addCell(fr);
                }
              }
              catch(Exception xe)
              {
                xe.printStackTrace();
                return null;
              }
              k++;
              continue;
            }
            else if(columnClasses[j] == Integer.class || columnClasses[j] == Long.class)
            {
              number = null;
/*
                if(columnClasses[j] == Integer.class)
                {
                  Integer cv = (Integer)sums.get(new Integer(j));
                  if(cv == null)
                  {
                    cv = (Integer) ce.getValue(j, table.getHowToRead(), webUser, language);
                    sums.put(new Integer(j), cv);
                  }
                  else
                  {
                    cv = new Integer(((Integer)ce.getValue(j, table.getHowToRead(), webUser, language)).intValue() + cv.intValue());
                    sums.put(new Integer(j), cv);
                  }
                }*/
              try
              {
                number = new jxl.write.Number(colPos[k], c, ((Number)ce.getValue(j, table.getHowToRead(), webUser, language)).intValue());
                if(fractionDigits[j] == 2)
                  number.setCellFormat(formatNumber2);
                else if(fractionDigits[j] == 3)
                  number.setCellFormat(formatNumber3);
              }
              catch(Exception e)
              {
                e.printStackTrace();
                return null;
              }
              try
              {
                sheet.addCell(number);
                if(i == expressions.length - 1 && columnClasses[j] == Integer.class)
                {
                  String column = this.alphabet[colPos[k]];
                  String startRow = "2";
                  String endRow = "" + (c + 1);
                  Formula fr = new Formula(colPos[k], c + 2, "SUM(" + column + startRow + ":" + column + endRow + ")");
                  if(fractionDigits[j] == 2)
                    fr.setCellFormat(formatNumber2);
                  else if(fractionDigits[j] == 3)
                    fr.setCellFormat(formatNumber3);
                  sheet.addCell(fr);
                }
              }
              catch(Exception xe)
              {
                xe.printStackTrace();
                return null;
              }
              k++;
              continue;
            }
            else if(columnClasses[j] == Boolean.class)
            {
              if(((Boolean)ce.getValue(j, table.getHowToRead(), webUser, language)).booleanValue())
                values[colPos[k]] = "V";
              else
                values[colPos[k]] = "O";
            }
            label = new Label(colPos[k], c, values[colPos[k]]);
            try
            {
              sheet.addCell(label);
            }
            catch(Exception xe)
            {
              xe.printStackTrace();
              return null;
            }
            k++;
          }
          c++;
        }
      }
      try
      {
        wb.write();
        wb.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    if(toFilter)
    {
//      System.out.println("Filter column " + filterColumn);
      for(int i = 0, c = 0; i < expressions.length; i++)
      {
        TableElement ce = (TableElement)expressions[i];
        String[] values = new String[cn.length];
        String[] cellColors = new String[cn.length];
        String[] linkTables = new String[cn.length];
        boolean filtered = false;
        for(int j = 0, k = 0; j < columnNames.length; j++)
        {
          if(!this.isColumnHidden(ci, j)) continue;
          values[colPos[k]] = ce.getValue(j, table.getHowToRead(), webUser, language).toString();
          cellColors[colPos[k]] = ce.getCellColor(table.getHowToRead(), j);
          Table rt = ce.getReferenceTable(j, table.getHowToRead());
          if(rt == null)
            linkTables[colPos[k]] = null;
          else
            linkTables[colPos[k]] = rt.getName();
          if(columnClasses[j] == Double.class)
          {
/*            String sclasses = "";
            String sviewclasses = "";
            String sviewclassessorted = "";
            String sclassessorted = "";
            for(int sti = 0; sti < columnClasses.length; sti++)
              sclasses += columnClasses[sti].toString() + " ";
            for(int sti = 0; sti < viewColumnClasses.length; sti++)
              sviewclasses += viewColumnClasses[sti].toString() + " ";
            System.out.println(sclasses);
            System.out.println(sviewclasses);
            System.out.println("" + j + " " + colPos[k]);
            System.out.println(columnClasses[j].toString());
            System.out.println(viewColumnClasses[colPos[k]].toString());
            System.out.println(columnClasses[colPos[k]].toString());
            System.out.println(viewColumnClasses[k]);*/
//            System.out.println("j " + j);
            if(fractionDigits != null && j < fractionDigits.length)
            {
              formatter.setMinimumFractionDigits(fractionDigits[j]);
              formatter.setMaximumFractionDigits(fractionDigits[j]);
            }
            double cuv = ( (Double) ce.getValue(j, table.getHowToRead(), webUser, language)).doubleValue();
            values[colPos[k]] = formatter.format(cuv);
            if(j == filterColumn)
            {
              double fV = 0;
              try
              {
                fV = formatter.parse(filterValue).doubleValue();
              }
              catch (Exception e)
              {
                e.printStackTrace();
                return null;
              }
              if ((filterFrom && cuv < fV) || (!filterFrom && cuv != fV /*!values[k].equals(filterValue)*/))
              {
                filtered = true;
                break;
              }
            }
          }
          else if(columnClasses[j] == Boolean.class)
          {
            if(((Boolean)ce.getValue(j, table.getHowToRead(), webUser, language)).booleanValue())
              values[colPos[k]] = "V";
            else
              values[colPos[k]] = "O";
          }
          else if(columnClasses[j] == Integer.class || columnClasses[j] == Long.class)
          {
            long cuv = ((Number)ce.getValue(j, table.getHowToRead(), webUser, language)).longValue();
            if(j == filterColumn)
            {
              long fV = 0;
              try
              {
                fV = Long.parseLong(filterValue);
              }
              catch(Exception e)
              {
//              e.printStackTrace();
              }
              if ((filterFrom && cuv < fV) || (!filterFrom && cuv != fV))
              {
                filtered = true;
                break;
              }
            }
          }
          else
          if(j == filterColumn)
          {
            if(filterValue.length() == 0)
            {
              if(values[colPos[k]].length() > 0)
              {
                filtered = true;
                break;
              }
            }
            if((filterFrom && values[colPos[k]].compareTo(filterValue) < 0) || (!filterFrom && values[colPos[k]].toUpperCase().indexOf(filterValue.toUpperCase()) < 0))
            {
              filtered = true;
              break;
            }
          }
          k++;
        }
        if(filtered) continue;
        c++;
        if(view.size() - 1 == viewSize)
        {
          additionalRows++;
          continue;
        }
        else
          additionalRows++;
        if(c >= start)
        {
          view.add(values);
          codes.add(ce.getCode());
          colors.add(cellColors);
          links.add(linkTables);
        }
      }
    }
    else
    {
      for (int i = 0, c = 0; i < expressions.length; i++)
      {
        if(view.size() - 1 == viewSize)
        {
          additionalRows++;
          continue;
        }
        else
          additionalRows++;
        TableElement ce = (TableElement) expressions[i];
        String[] values = new String[cn.length];
        String[] cellColors = new String[cn.length];
        String[] linkTables = new String[cn.length];
        for (int j = 0, k = 0; j < columnNames.length; j++)
        {
          if (!this.isColumnHidden(ci, j))
            continue;
          try
          {
            values[colPos[k]] = ce.getValue(j, table.getHowToRead(), webUser, language).toString();
          }
          catch(Exception e)
          {
//            e.printStackTrace();
            break;
          }
          cellColors[colPos[k]] = ce.getCellColor(table.getHowToRead(), j);
          Table rt = ce.getReferenceTable(j, table.getHowToRead());
          if(rt == null)
            linkTables[colPos[k]] = null;
          else
            linkTables[colPos[k]] = rt.getName();
          if (columnClasses[j] == Double.class)
          {
            if (fractionDigits != null && j < fractionDigits.length)
            {
              formatter.setMinimumFractionDigits(fractionDigits[j]);
              formatter.setMaximumFractionDigits(fractionDigits[j]);
            }
//            try
//            {
              values[colPos[k]] = formatter.format(((Double)ce.getValue(j, table.getHowToRead(), webUser, language)).doubleValue());
//            }
/*            catch(Exception ee)
            {
              System.out.println(ce.getValue(j, table.getHowToRead(), webUser, language).toString() + " " + columnClasses[j].toString() + " j " + j);
              return null;
            }*/
          }
          if(columnClasses[j] == Boolean.class)
          {
            if(((Boolean)ce.getValue(j, table.getHowToRead(), webUser, language)).booleanValue())
              values[colPos[k]] = "V";
            else
              values[colPos[k]] = "O";
          }
          k++;
        }
        c++;
        if(c >= start)
        {
          view.add(values);
          codes.add(ce.getCode());
          colors.add(cellColors);
          links.add(linkTables);
        }
      }
    }
    result[0] = view;
    result[1] = new Integer((int)Math.ceil((double)additionalRows / (double)viewSize));
    result[2] = codes;
    result[3] = colors;
    result[4] = links;
    long endTime = System.currentTimeMillis();
    System.out.println("" + endTime);
    System.out.println("" + (endTime - currentTime));
    return result;
  }

  public class BasicComparator implements Comparator
  {
    private int criteria;
    private boolean ascending;
    private int howToRead;
    private User webUser;
    private String language;

    BasicComparator(int criteria, boolean ascending, int howToRead, User webUser, String language)
    {
      this.criteria = criteria;
      this.ascending = ascending;
      this.howToRead = howToRead;
      this.webUser = webUser;
      this.language = language;
    }

    public int compare(Object obj1, Object obj2)
    {
      TableElement te1 = (TableElement) obj1;
      TableElement te2 = (TableElement) obj2;
      Object value = te1.getValue(this.criteria, this.howToRead, this.webUser, language);
      if (this.ascending)
      {
        if(this.criteria == -1)
          return te1.getCode().compareTo(te2.getCode());
        if (value instanceof Comparable)
        {
          return ((Comparable)value).compareTo( (Comparable) te2.getValue(this.criteria, this.howToRead, webUser, language));
        }
        else if (value instanceof Boolean)
        {
          return ( ( (Boolean) value)).toString().compareTo( ( (Boolean) te2.getValue(this.criteria, this.howToRead, webUser, language)).
              toString());
        }
        else
        {
          return ( (Long) value).compareTo( (Long) te2.getValue(this.criteria, this.howToRead, webUser, language));
        }
      }
      else
      {
        if(this.criteria == -1)
          return te1.getCode().compareTo(te2.getCode());
        if (value instanceof Comparable)
        {
          return ( (Comparable) te2.getValue(this.criteria, this.howToRead, webUser, language)).compareTo( (Comparable) value);
        }
        else if (value instanceof Boolean)
        {
          return ( ( (Boolean) te2.getValue(this.criteria, this.howToRead, webUser, language)).toString()).compareTo( (value).toString());
        }
        else
        {
          return ( (Long) te2.getValue(this.criteria, howToRead, webUser, language)).compareTo( (Long) value);
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
}

package data;

import com.borland.dbswing.TableScrollPane;
import javax.swing.table.TableModel;
import com.borland.dbswing.JdbTable;
import java.util.Vector;
import java.io.PrintStream;
import java.io.FileOutputStream;
import utils.DateRepresentation;
import javax.swing.event.TableModelListener;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import com.borland.dx.dataset.DataChangeEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class CurrentInfoTable extends TableScrollPane
{
  public JdbTable table;
  private DateRepresentation today;
  private PrintStream outAndErr;
  private InfoModel data;

  private class InfoRecord
  {
    protected String time;
    protected String info;

    public InfoRecord(String info)
    {
      this.time = today.get(today.HOUR_OF_DAY) + ":" + today.get(today.MINUTE) + ":" + today.get(today.SECOND);
      this.info = info;
    }
  }

  private class InfoModel extends Vector implements TableModel
  {

    public InfoModel()
    {
      super();
    }

    public int getColumnCount()
    {
      return 2;
    }

    public int getRowCount()
    {
      return super.size();
    }

    public boolean isCellEditable(int row, int column)
    {
      return false;
    }

    public Class getColumnClass(int column)
    {
      return String.class;
    }

    public Object getValueAt(int row, int column)
    {
      if(column == 0)
        return ((InfoRecord)super.get(row)).time;
      return ((InfoRecord)super.get(row)).info;
    }

    public void setValueAt(Object obj, int row, int column)
    {
    }

    public String getColumnName(int index)
    {
      if(index == 0) return "Time";
      return "Info";
    }

    public void addTableModelListener(TableModelListener l)
    {
    }
    public void removeTableModelListener(TableModelListener l)
    {
    }

    public boolean add(Object obj)
    {
      DateRepresentation realtoday = new DateRepresentation(true);
      if(outAndErr != null && !today.toString().equals(realtoday.toString()))
      {
        try
        {
          outAndErr.close();
          outAndErr = null;
          today = realtoday;
        }
        catch(Exception e)
        {
          JOptionPane.showConfirmDialog(Data.mainFrame, e.toString(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
      }
      if(outAndErr == null)
      {
        try
        {
          outAndErr = new PrintStream(new FileOutputStream(Data.path + "\\info" + today.toString() + ".csv", true))
          {
            public void println(String str)
            {
              if(str == null) str = "null";
              add(new InfoRecord(str));
              super.print(today.get(today.HOUR_OF_DAY) + ":" + today.get(today.MINUTE) + ":" + today.get(today.SECOND) + "\t");
              super.println(str);
            }

            public void println(Object obj)
            {
              if(obj == null) obj = "null";
              add(new InfoRecord(obj.toString()));
              super.print(today.get(today.HOUR_OF_DAY) + ":" + today.get(today.MINUTE) + ":" + today.get(today.SECOND) + ";");
              super.println(obj.toString());
            }
          };
          System.setOut(outAndErr);
          System.setErr(outAndErr);
        }
        catch(Exception e)
        {
          JOptionPane.showConfirmDialog(Data.mainFrame, e.toString(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
      }
      if(realtoday.get(realtoday.SECOND) != today.get(today.SECOND) || realtoday.get(realtoday.MINUTE) != today.get(today.MINUTE) || realtoday.get(realtoday.HOUR_OF_DAY) != today.get(today.HOUR_OF_DAY))
         today = realtoday;
      boolean b = super.add(obj);
      table.tableChanged(new TableModelEvent(table.getModel()));
      if(table.getColumnModel().getColumn(0).getMinWidth() != getViewport().getWidth() / 6 - 20)
      {
        table.getColumnModel().getColumn(0).setMinWidth(getViewport().getWidth() / 6 - 20);
        table.getColumnModel().getColumn(1).setMinWidth(getViewport().getWidth() -
            (getViewport().getWidth() / 6 - 20) - 20);
      }
      table.getRowHeader().repaintRows(this.size() - 1, this.size() - 1);
      if(this.size() == 10 || this.size() == 100 || this.size() == 1000 || this.size() == 10000 || this.size() == 100000 || this.size() == 1000000)
        table.getRowHeader().setBounds(table.getRowHeader().getX(), table.getRowHeader().getY(), 100, 100);
      table.ensureRowIsVisible(this.size() - 1);
      return b;
    }
  }

  public CurrentInfoTable()
  {
    this.today = new DateRepresentation(true);
    this.data = new InfoModel();
    this.table = new JdbTable(this.data);
    super.setViewportView(this.table);
    super.setVerticalScrollBarPolicy(this.VERTICAL_SCROLLBAR_ALWAYS);
    super.setHorizontalScrollBarPolicy(this.HORIZONTAL_SCROLLBAR_ALWAYS);

    try
    {
      this.outAndErr = new PrintStream(new FileOutputStream(Data.path + "\\info" + today.toString() + ".csv", true))
      {
        public void println(String str)
        {
          if(str == null) str = "null";
          data.add(new InfoRecord(str));
          super.print(today.get(today.HOUR_OF_DAY) + ":" + today.get(today.MINUTE) + ":" +
                      today.get(today.SECOND) + ";");
          super.println(str);
        }

        public void println(Object obj)
        {
          if(obj == null) obj = "null";
          data.add(new InfoRecord(obj.toString()));
          super.print(today.get(today.HOUR_OF_DAY) + ":" + today.get(today.MINUTE) + ":" +
                      today.get(today.SECOND) + ";");
          super.println(obj.toString());
        }
      };
    }
    catch(Exception e)
    {
      JOptionPane.showConfirmDialog(Data.mainFrame, e.toString(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    System.setOut(outAndErr);
    System.setErr(outAndErr);

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    this.table.setPreferredScrollableViewportSize(new Dimension(screenSize.width - 200, screenSize.height - 200));
  }

}
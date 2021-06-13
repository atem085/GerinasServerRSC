package data;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

public class RestartButton extends JButton
{

  public RestartButton(String text)
  {
    super(text);
    super.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent event)
        {
          final JDialog dialog = new JDialog(Data.mainFrame, true);
          dialog.setTitle("Restart settings");
          dialog.getContentPane().setLayout(new GridLayout(5, 2, 10, 10));

          JLabel startDate = new JLabel("Start date and time :");
          GregorianCalendar gc = new GregorianCalendar(Data.year, Data.month, Data.day, Data.hour, Data.minute, 0);
          final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(gc.getTime(), null, null, 0));
          dialog.getContentPane().add(startDate);
          dialog.getContentPane().add(dateSpinner);

          JLabel everyLabel = new JLabel("Frequency in days :");
          final JSpinner everySpinner = new JSpinner(new SpinnerNumberModel(Data.every, 0, 366, 1));
          dialog.getContentPane().add(everyLabel);
          dialog.getContentPane().add(everySpinner);

          JLabel backupLabel = new JLabel("Backup automatically :");
          final JCheckBox backupBox = new JCheckBox();
          if(Data.backup)
            backupBox.setSelected(true);
          dialog.getContentPane().add(backupLabel);
          dialog.getContentPane().add(backupBox);

          JButton pathButton = new JButton("Backup path :");
          final JTextField pathField = new JTextField(Data.backupPath, 50);
          pathButton.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent event2)
              {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showDialog(dialog, "OK");
                File file = fileChooser.getSelectedFile();
                pathField.setText(file.getAbsolutePath());
              }
            }
          );
          dialog.getContentPane().add(pathButton);
          dialog.getContentPane().add(pathField);

          JButton okButton = new JButton("OK");
          JButton cancelButton = new JButton("Cancel");
          okButton.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent event1)
              {
                Date dt = (Date)dateSpinner.getValue();
                GregorianCalendar date = new GregorianCalendar();
                date.setTime(dt);
                Data.day = date.get(GregorianCalendar.DAY_OF_MONTH);
                Data.month = date.get(GregorianCalendar.MONTH);
                Data.year = date.get(GregorianCalendar.YEAR);
                Data.hour = date.get(GregorianCalendar.HOUR_OF_DAY);
                Data.minute = date.get(GregorianCalendar.MINUTE);
                Data.every = ((Integer)everySpinner.getValue()).intValue();
                Data.backup = backupBox.isSelected();
                Data.backupPath = pathField.getText();
                dialog.dispose();
              }
            }
          );
          cancelButton.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent event1)
              {
                dialog.dispose();
              }
            }
          );
          dialog.getContentPane().add(okButton);
          dialog.getContentPane().add(cancelButton);

          dialog.setLocation(300, 300);
          dialog.pack();
          dialog.setVisible(true);
        }
      }
    );
  }

}
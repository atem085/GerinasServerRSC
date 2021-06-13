package utils;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import com.borland.jbcl.layout.VerticalFlowLayout;

/**
 * <p>Title: ClientRSC</p>
 * <p>Description: RSC Client</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Noname</p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class Progress extends JFrame
{

  private JProgressBar overall, now;
  private JLabel ov, n;

  public Progress(String title, int min, int max)
  {
    super(title);
    super.getContentPane().setLayout(new VerticalFlowLayout());
    this.overall = new JProgressBar(min, max);
    this.now = new JProgressBar();
    this.ov = new JLabel("Overall progress");
    this.n = new JLabel();
    super.getContentPane().add(ov);
    super.getContentPane().add(overall);
    super.getContentPane().add(n);
    super.getContentPane().add(now);
    super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    super.setSize(500, 150);
  }

  public void setProgressOverall(int progress)
  {
    this.overall.setValue(progress);
  }

  public void setMinimum(int minimum)
  {
    this.now.setMinimum(minimum);
  }

  public void setMaximum(int maximum)
  {
    this.now.setMaximum(maximum);
  }

  public void setProgressNow(int progress)
  {
    this.now.setValue(progress);
  }

  public void setNote(String note)
  {
    this.n.setText(note);
  }

}

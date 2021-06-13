package utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/** * @author zeja */

public class Mail
{
  public static void sendMail(String smtpHost,String username,String password,String senderAddress,String recipientsAddress,String subject,String text ) throws Exception
  {
    MailAuthenticator auth = new MailAuthenticator(username, password);
    Properties properties = new Properties();
    // Den Properties wird die ServerAdresse hinzugefügt
    properties.put("mail.smtp.host", smtpHost);
    // !!Wichtig!! Falls der SMTP-Server eine Authentifizierung
    // verlangt
    // muss an dieser Stelle die Property auf "true" gesetzt
    // werden
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//    properties.put("mail.smtp.starttls.enable","true");
    // Hier wird mit den Properties und dem implements Contructor
    // erzeugten
    // MailAuthenticator eine Session erzeugt
    Session session = Session.getDefaultInstance(properties, auth);
      // Eine neue Message erzeugen
      Message msg = new MimeMessage(session);
      // Hier werden die Absender- und Empfängeradressen gesetzt
      msg.setFrom(new InternetAddress(senderAddress));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsAddress, false));
      // Der Betreff und Body der Message werden gesetzt
      msg.setSubject(subject);
      msg.setText(text);
      // Hier lassen sich HEADER-Informationen hinzufügen
      msg.setHeader("Recall", "Recall");
      msg.setSentDate(new Date( ));
      // Zum Schluss wird die Mail natürlich noch verschickt
      Transport.send(msg);
  }
}

class MailAuthenticator extends Authenticator
{
  /**
   * Ein String, der den Usernamen nach der Erzeugung eines
   * Objektes<br>
   * dieser Klasse enthalten wird.
   */
  private final String user;
  /**         * Ein String, der das Passwort nach der Erzeugung eines
   * Objektes<br>
   * dieser Klasse enthalten wird.
   */
  private final String password;
  /**
   * Der Konstruktor erzeugt ein MailAuthenticator Objekt<br>
   * aus den beiden Parametern user und passwort.
   * @param user
   * String, der Username fuer den Mailaccount.
   * @param password
   * String, das Passwort fuer den Mailaccount.
   */
  public MailAuthenticator(String user, String password)
  {
    this.user = user;
    this.password = password;
  }
  /**
   * Diese Methode gibt ein neues PasswortAuthentication
   * Objekt zurueck.
   * @see javax.mail.Authenticator#getPasswordAuthentication()
   */
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.user, this.password);
  }
}

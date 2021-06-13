package table;

import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;

public interface HttpCommand
{
  public void write(HttpClientThread clientThread, MyObjectOutputStream out) throws IOException;
  public void write(HttpClientThread clientThread, OutgoingChannel out) throws IOException;
  public HttpCommand read(MyObjectInputStream in) throws IOException;
  public void log(HttpClientThread clientThread, String path, String language);
  public boolean execute(HttpClientThread clientThread, DB db, String language);
  public String getErrorMessage();

}

package table;

import utils.DateRepresentation;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import java.io.IOException;

public interface Command
{
  public void write(IncomingClientThread clientThread, MyObjectOutputStream out, boolean flush) throws IOException;
  public void write(IncomingClientThread clientThread, OutgoingChannel out, boolean flush) throws IOException;
  public Command read(MyObjectInputStream in, boolean local) throws IOException;
  public void log(IncomingClientThread clientThread, String path, String language);
  public boolean execute(IncomingClientThread clientThread, DB db, String language);
  public String getErrorMessage();
  public long getVersionAfterExecuting();
  public DateRepresentation getTimeStamp();
}
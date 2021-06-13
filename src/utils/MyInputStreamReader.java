package utils;

import java.io.InputStreamReader;
import java.io.InputStream;

public class MyInputStreamReader
{
  private InputStreamReader in;
  private char[] toread;

  public MyInputStreamReader(InputStream is, String charset) throws Exception
  {
    this.in = new InputStreamReader(is, charset);
    this.toread = new char[1];
  }

  public void close() throws Exception
  {
    this.in.close();
  }

  public String readLine() throws Exception
  {
    String line = null;
    String rd;
    while(true)
    {
      if(this.in.read(this.toread) == -1) return line;
      if(this.toread == null) return line;
      if(line == null) line = "";
      rd = new String(this.toread);
      if(rd.equals("\r"))
      {
         this.in.read(this.toread);
         return line;
      }
      if(rd.equals("\n"))
        return line;
      line += rd;
    }
  }
}

package utils;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream.*;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class MyObjectOutputStream
{

  private ObjectOutputStream out;
  public int a, b, c;
  private int des, cc, res;
  private int x1, x2;
  private boolean encoding;
  public boolean echo = false;
  private boolean flush = true;
  public boolean toDisk;
  private Object busy = null;

  public MyObjectOutputStream()
  {

  }

  public MyObjectOutputStream(OutputStream outputStream, boolean toDisk) throws IOException
  {
    this.toDisk = toDisk;
    this.out = new ObjectOutputStream(outputStream);
    this.a = 2;
    this.b = -4;
    this.c = -6;
    this.encoding = true;
  }
/*
  public synchronized void take(Object o)
  {
    this.busy = o;
  }
*/
//  public synchronized void

  public void setEcho(boolean echo)
  {
    this.echo = echo;
  }

  public void setFlush(boolean flush)
  {
    this.flush = flush;
  }

  public void writeBoolean(boolean value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing boolean " + value);
    this.out.writeBoolean(value);
  }

  public void writeInt(int value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing int " + value);
    this.out.writeInt(value);
  }

  public void writeDouble(double value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing double " + value);
    this.out.writeDouble(value);
  }

  public void writeFloat(float value) throws IOException
  {
    this.out.writeFloat(value);
  }

  public void writeLong(long value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing long " + value);
    this.out.writeLong(value);
  }

  public void writeObject(Object value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing object of class " + value.getClass() + " " + value.toString());
    this.out.writeObject(value);
  }

  public void setEncoding(boolean encoding)
  {
    this.encoding = encoding;
  }

  public void writeUTF(String value) throws IOException
  {
    if(this.encoding)
    {
      byte[] b = value.getBytes("Unicode");
      int length = b.length;
      this.out.writeInt(length);
      for(int i = 0; i < length; i++)
      {
        this.x1 = b[i];
        this.res = this.a * (int)Math.pow((double)this.x1, 2.0) +
            this.b * this.x1 + this.c;
        this.out.writeInt(res);
        this.cc = this.c - this.res;
        this.des = (int)Math.pow((double)this.b, 2.0) - 4 * this.a * this.cc;
        this.x2 = ( -this.b + (int)Math.sqrt((double)this.des)) / (2 * this.a);
        if(this.x2 == this.x1)
          this.x2 = ( -this.b - (int)Math.sqrt((double)this.des)) / (2 * this.a);
        out.writeInt(this.x2);
      }
    }
    else this.out.writeUTF(value);
    if(this.echo)
      System.out.println("Writing UTF " + value);
  }

  public void writeShort(int value) throws IOException
  {
    this.out.writeShort(value);
  }

  public void writeUnshared(Object value) throws IOException
  {
    this.out.writeUnshared(value);
  }

  public void writeByte(int value) throws IOException
  {
    this.out.writeByte(value);
  }

  public void writeBytes(String value) throws IOException
  {
    this.out.writeBytes(value);
  }

  public void writeChar(int value) throws IOException
  {
    this.out.writeChar(value);
  }

  public void writeChars(String value) throws IOException
  {
    this.out.writeChars(value);
  }

  public void writeFields() throws IOException
  {
    this.out.writeFields();
  }

  public void write(int value) throws IOException
  {
    this.out.write(value);
  }

  public void write(byte[] buf, int off, int len) throws IOException
  {
    this.out.write(buf, off, len);
  }

  public void write(byte[] buf) throws IOException
  {
    this.out.write(buf);
  }

  public void close() throws IOException
  {
    this.out.close();
  }

  public void defaultWriteObject()  throws IOException
  {
    this.out.defaultWriteObject();
  }

  public void flush() throws IOException
  {
    if(this.echo && this.flush)
      System.out.println("Flush");
    if(this.flush)
      this.out.flush();
  }

  public PutField putFields() throws IOException
  {
    return this.out.putFields();
  }

  public void reset() throws IOException
  {
    this.out.reset();
  }

  public void useProtocolVersion(int version) throws IOException
  {
    this.out.useProtocolVersion(version);
  }


}

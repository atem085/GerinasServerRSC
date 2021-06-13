package utils;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.*;
import java.io.ObjectInputValidation;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class MyObjectInputStream
{

  private ObjectInputStream in;
  public int len, res, x1, x2, a, b, c, cc, des;
  private byte[] buf;
  private boolean encoding;
  public boolean echo = false;

  public MyObjectInputStream(InputStream inputStream) throws IOException
  {
    this.in = new ObjectInputStream(inputStream);
    this.a = 2;
    this.b = -4;
    this.c = -6;
    this.encoding = true;
  }

  public ObjectInputStream getIn()
  {
    return this.in;
  }

  public void setEcho(boolean echo)
  {
    this.echo = echo;
  }

  public boolean readBoolean() throws IOException
  {
    if(this.echo)
    {
      boolean b = this.in.readBoolean();
      System.out.println("Reading boolean " + b);
      return b;
    }
    return this.in.readBoolean();
  }

  public int readInt() throws IOException
  {
    if(this.echo)
    {
      int i = this.in.readInt();
      System.out.println("Reading int " + i);
      return i;
    }
    return this.in.readInt();
  }

  public double readDouble() throws IOException
  {
    if(this.echo)
    {
      double d = this.in.readDouble();
      System.out.println("Reading double " + d);
      return d;
    }
    return this.in.readDouble();
  }

  public float readFloat() throws IOException
  {
    return this.in.readFloat();
  }

  public long readLong() throws IOException
  {
    if(this.echo)
    {
      long v = this.in.readLong();
      System.out.println("Reading long " + v);
      return v;
    }
    return this.in.readLong();
  }

  public Object readObject() throws IOException, ClassNotFoundException
  {
    if(this.echo)
    {
      Object o = this.in.readObject();
      System.out.println("Reading object of class " + o.getClass() + " " + o.toString());
      return o;
    }
    return this.in.readObject();
  }

  public void setEncoding(boolean encoding)
  {
    this.encoding = encoding;
  }

  public String readUTF() throws IOException
  {
    if(this.encoding)
    {
      this.len = in.readInt();
      this.buf = new byte[len];
      for(int i = 0; i < len; i++)
      {
        this.res = in.readInt();
        this.x1 = in.readInt();
        this.cc = this.c - this.res;
        this.des = (int)Math.pow((double)this.b, 2.0) - 4 * this.a * this.cc;
        this.x2 = ( -this.b + (int)Math.sqrt((double)this.des)) / (2 * this.a);
        if(this.x2 == this.x1)
          this.x2 = ( -this.b - (int)Math.sqrt((double)this.des)) / (2 * this.a);
        this.buf[i] = (byte)x2; //nuschno decodieren
      }
      if(this.echo)
        System.out.println("Reading UTF " + new String(this.buf, "Unicode"));
      return new String(this.buf, "Unicode");
    }
    if(this.echo)
    {
      String s = this.in.readUTF();
      System.out.println("Reading UTF " + s);
      return s;
    }
    return this.in.readUTF();
  }

  public short readShort() throws IOException
  {
    return this.in.readShort();
  }

  public Object readUnshared() throws IOException, ClassNotFoundException
  {
    return this.in.readUnshared();
  }

  public byte readByte() throws IOException
  {
    return this.in.readByte();
  }

  public char readChar() throws IOException
  {
    return this.in.readChar();
  }

  public GetField readFields() throws IOException, ClassNotFoundException
  {
    return this.in.readFields();
  }

  public int read() throws IOException
  {
    return this.in.read();
  }

  public void read(byte[] buf, int off, int len) throws IOException
  {
    this.in.read(buf, off, len);
  }

  public int read(byte[] buf) throws IOException
  {
    return this.in.read(buf);
  }

  public void close() throws IOException
  {
    this.in.close();
  }

  public void defaultReadObject()  throws IOException, ClassNotFoundException
  {
    this.in.defaultReadObject();
  }

  public int available() throws IOException
  {
    return this.in.available();
  }

  public void readFully(byte[] buf, int off, int len) throws IOException
  {
    this.in.readFully(buf, off, len);
  }

  public void readFully(byte[] buf) throws IOException
  {
    this.in.readFully(buf);
  }

  public int skipBytes(int len) throws IOException
  {
    return this.in.skipBytes(len);
  }

  public void registerValidation(ObjectInputValidation obj, int prio) throws IOException
  {
    this.in.registerValidation(obj, prio);
  }

  public void reset() throws IOException
  {
    this.in.reset();
  }

}

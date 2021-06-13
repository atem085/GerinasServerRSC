package utils;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.util.ArrayList;
import data.Data;

/**
 * <p>Title: ServerRSC</p>
 * <p>Description: Server side RSC</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Dennis Tuvia
 * @version 1.0
 */

public class ObjectZipOutputStream extends MyObjectOutputStream
{

  private ObjectOutputStream out = null;
  private boolean echo = false;
  private boolean remember = false;
  private boolean flush = true;
  public int a, b, c;
  private int des, cc, res;
  private int x1, x2;
  private boolean encoding;
//  private ArrayList virtualBuffer = new ArrayList();
//  private ArrayList objectVirtualBuffer = new ArrayList();
  private DeflaterOutputStream zout;
  private ObjectOutputStream copy = null;
  private int copySize;
  private File f;

  public ObjectZipOutputStream(OutputStream outputStream) throws IOException
  {
    this.zout = new DeflaterOutputStream(outputStream, new Deflater(Deflater.BEST_COMPRESSION));
    System.out.println("deflater created");
//    this.out = new ObjectOutputStream(zout);
    super.setEcho(false);
    this.setEcho(false);
  }

  public void setEncoding(boolean encoding)
  {
    this.encoding = encoding;
  }

  public void prepare() throws IOException
  {
    this.out = new ObjectOutputStream(this.zout);

  }

  public void setEcho(boolean echo)
  {
    this.echo = echo;
  }

  public boolean getRemember()
  {
    return this.remember;
  }

  public void setRemember(boolean remember)
  {
    this.remember = remember;
    String s = Data.path + "\\copy" + System.currentTimeMillis();
    this.f = new File(s);
/*
    while(f.exists())
    {
      boolean b = f.delete();
      System.out.println("Delete while remembering " + b);
      if(!b)
        s = s + "1";
      else
        break;
      this.f = new File(s);
    }*/
    try
    {
      this.copy = new ObjectOutputStream(new FileOutputStream(f));
      this.copySize = 0;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      this.remember = false;
    }
  }

  public void setFlush(boolean flush)
  {
    this.flush = flush;
  }

  public void flush() throws IOException
  {
    if(this.flush)
      this.out.flush();
  }

  public void writeBoolean(boolean value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing boolean " + value);
    if(this.remember)
    {
      this.copy.writeInt(0);
      this.copy.writeBoolean(value);
      this.copySize++;
//      this.virtualBuffer.add(new Boolean(value));
    }
    else
      this.out.writeBoolean(value);
  }

  public void writeInt(int value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing int " + value);
    if(this.remember)
    {
      this.copy.writeInt(1);
      this.copy.writeInt(value);
      this.copySize++;
//      this.virtualBuffer.add(new Integer(value));
    }
    else
      this.out.writeInt(value);
  }

  public void writeDouble(double value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing double " + value);
    if(this.remember)
    {
      this.copy.writeInt(2);
      this.copy.writeDouble(value);
      this.copySize++;
//      this.virtualBuffer.add(new Double(value));
    }
    else
      this.out.writeDouble(value);
  }

  public void writeFloat(float value) throws IOException
  {
    if(this.remember)
    {
      this.copy.writeInt(10);
      this.copy.writeFloat(value);
      this.copySize++;
//      this.virtualBuffer.add(new Float(value));
    }
    else
      this.out.writeFloat(value);
  }

  public void writeLong(long value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing long " + value);
    if(this.remember)
    {
      this.copy.writeInt(3);
      this.copy.writeLong(value);
      this.copySize++;
//      this.virtualBuffer.add(new Long(value));
    }
    else
      this.out.writeLong(value);
  }

  public void writeObject(Object value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing object of class " + value.getClass() + " " + value.toString());
    if(this.remember)
    {
      this.copy.writeInt(5);
      this.copy.writeObject(value);
      this.copySize++;
//      this.virtualBuffer.add(new Object());
//      this.objectVirtualBuffer.add(value);
    }
    else
      this.out.writeObject(value);
  }

  public void writeUTF(String value) throws IOException
  {
    if(this.echo)
      System.out.println("Writing UTF " + value);
    if(this.remember)
    {
      this.copy.writeInt(4);
      this.copy.writeUTF(value);
      this.copySize++;
//      this.virtualBuffer.add(new String(value));
    }
    else
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
  }

  public void writeShort(int value) throws IOException
  {
    if(this.remember)
    {
      this.copy.writeInt(9);
      this.copy.writeShort(value);
      this.copySize++;
//      this.virtualBuffer.add(new Short((short)value));
    }
    else
      this.out.writeShort(value);
  }

  public void writeUnshared(Object value) throws IOException
  {
    this.out.writeUnshared(value);
  }

  public void writeByte(int value) throws IOException
  {
    if(this.remember)
    {
      this.copy.writeInt(8);
      this.copy.writeByte(value);
      this.copySize++;
//      this.virtualBuffer.add(new Byte((byte)value));
    }
    else
     this.out.writeByte(value);
  }

  public void writeBytes(String value) throws IOException
  {
    this.out.writeBytes(value);
  }

  public void writeChar(int value) throws IOException
  {
    if(this.remember)
    {
      this.copy.writeInt(7);
      this.copy.writeChar(value);
      this.copySize++;
//      this.virtualBuffer.add(new Character((char)value));
    }
    else
      this.out.writeChar(value);
  }

  public void release() throws IOException
  {
    this.copy.close();
    ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.f));
    if(this.out == null)
      this.prepare();
    int type;
    for(int i = 0; i < this.copySize; i++)
    {
      type = in.readInt();
      switch(type)
      {
        case 0: this.out.writeBoolean(in.readBoolean());
                break;
        case 1: this.out.writeInt(in.readInt());
                break;
        case 2: this.out.writeDouble(in.readDouble());
                break;
        case 3: this.out.writeLong(in.readLong());
                break;
        case 4:
          if(this.encoding)
          {
            String value = in.readUTF();
            byte[] b = value.getBytes("Unicode");
            int length = b.length;
            this.out.writeInt(length);
            for(int j = 0; j < length; j++)
            {
              this.x1 = b[j];
              this.res = this.a * (int)Math.pow((double)this.x1, 2.0) +
              this.b * this.x1 + this.c;
              this.out.writeInt(res);
              this.cc = this.c - this.res;
              this.des = (int)Math.pow((double)this.b, 2.0) - 4 * this.a * this.cc;
              this.x2 = ( -this.b + (int)Math.sqrt((double)this.des)) / (2 * this.a);
              if(this.x2 == this.x1)
              this.x2 = ( -this.b - (int)Math.sqrt((double)this.des)) / (2 * this.a);
              this.out.writeInt(this.x2);
            }
            if(this.echo)
              System.out.println("Writing UTF " + value);
          }
          else
            this.out.writeUTF(in.readUTF());
          break;
        case 5: try
                {
                  this.out.writeObject(in.readObject());
                }
                catch(ClassNotFoundException e)
                {
                  this.out.writeObject(new Long(0));
                }
                break;
        case 7: this.out.writeChar(in.readChar());
                break;
        case 8: this.out.writeByte(in.readByte());
                break;
        case 9: this.out.writeShort(in.readShort());
                break;
        case 10: this.out.writeFloat(in.readFloat());
                break;
      }
    }
    in.close();
    System.out.println("Delete " + this.f.delete());
    this.remember = false;
  }

/*  public void clean()
  {
    this.virtualBuffer.clear();
    this.objectVirtualBuffer.clear();
    this.remember = false;
    System.gc();
    try
    {
      this.out.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
*/
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

  public void close(boolean collectGarbage) throws IOException
  {
    this.out.close();
    if(collectGarbage)
      System.gc();
  }

  public void defaultWriteObject()  throws IOException
  {
    this.out.defaultWriteObject();
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

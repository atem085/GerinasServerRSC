package table;

import java.util.Vector;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import utils.ObjectZipOutputStream;
import java.io.IOException;
import element.TableAuthorizationExample;
import data.Data;

public abstract class StaticUser extends User
{
  public boolean needsUpdate;

  public boolean hasDB;

  private Vector commands;

//  public int a, b, c;

  public StaticUser()
  {
    super();
    this.needsUpdate = false;
    this.hasDB = false;
    this.commands = new Vector();
  }

  public StaticUser(String name, String password)
  {
    super(name, password);
    this.commands = new Vector();
    this.needsUpdate = false;
    this.hasDB = false;
  }

  public void addCommand(Command command)
  {
    if(this.needsUpdate) return;
    else if(this.commands.size() > 50000)
    {
      this.commands.clear();
      this.needsUpdate = true;
      return;
    }
//    if(command instanceof Command19 || command instanceof Command20 || command instanceof Command21 || command instanceof Command22 || command instanceof Command28 || command instanceof Command29)
//      return;
    this.commands.add(command);
  }

  public void addCommand(HttpCommand command)
  {
    if(this.needsUpdate) return;
    else if(this.commands.size() > 500000)
    {
      this.commands.clear();
      this.needsUpdate = true;
      return;
    }
    this.commands.add(command);
  }

  public void clearCommands()
  {
    this.commands.clear();
  }

  public void sendCommands(ObjectZipOutputStream out) throws IOException
  {
//    Data.cat.checkForExistingCommands(this);
//    out.prepare();
    out.setFlush(false);
    System.out.println("Copying commands...");
    out.writeLong(Data.version);
    out.writeInt(this.commands.size());
    Object command;
    for(int i = 0; i < this.commands.size(); i++)
    {
      command = this.commands.get(i);
      try
      {
        ((Command)command).write(null, out, false);
      }
      catch(ClassCastException e)
      {
        ((HttpCommand)command).write(null, out);
      }
    }
    out.flush();
    this.commands.clear();
  }

  public int getCommandsSize()
  {
    return this.commands.size();
  }

  public void writeCommands(MyObjectOutputStream out) throws IOException
  {
    out.writeInt(this.commands.size());
    Object command;
    for(int i = 0; i < this.commands.size(); i++)
    {
      command = this.commands.get(i);
      try
      {
        ((Command)command).write(null, out, false);
      }
      catch(ClassCastException e)
      {
        ((HttpCommand)command).write(null, out);
      }
    }
  }

  public void readCommands(MyObjectInputStream in) throws IOException
  {
    int comType;
    int size = in.readInt(); //kol-vo kommand
    Command com, reader;
    for(int i = 0; i < size; i++)
    {
      comType = in.readInt();
      switch(comType)  //tip kommand
      {
        case 9:
          reader = new Command9();
          com = reader.read(in, true);
          this.commands.add(com);
          break;
        case 10:
          reader = new Command10();
          com = reader.read(in, true);
          this.commands.add(com);
           break;
         case 11:
           reader = new Command11();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 12:
           reader = new Command12();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 13:
           reader = new Command13();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 14:
           reader = new Command14();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 15:
           reader = new Command15();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 16:
           reader = new Command16();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 17:
           reader = new Command17();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 18:
           reader = new Command18();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 19:
           reader = new Command19();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 20:
           reader = new Command20();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 21:
           reader = new Command21();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 22:
           reader = new Command22();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 23:
           reader = new Command23();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 24:
           reader = new Command24();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 27:
           reader = new Command27();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 28:
           reader = new Command28();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 29:
           reader = new Command29();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 31:
           reader = new Command31();
           com = reader.read(in, true);
           this.commands.add(com);
           break;
         case 32:
           reader = new Command32();
           com = reader.read(in, true);
           this.commands.add(com);
         break;
         case 219:
           HttpCommand hreader = new HttpCommand219();
           HttpCommand hcom = hreader.read(in);
           this.commands.add(hcom);
           break;
       }
     }
  }

}

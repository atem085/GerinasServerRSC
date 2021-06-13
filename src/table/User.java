package table;

import java.util.Vector;
import utils.MyObjectOutputStream;
import utils.MyObjectInputStream;
import utils.ObjectZipOutputStream;
import java.io.IOException;
import element.TableAuthorizationExample;
import data.Data;

public abstract class User implements TableElement
{
  public char[] name, password;

  public boolean isLoggedIn;

  public User()
  {
    this.name = null;
    this.password = null;
    this.isLoggedIn = false;
  }

  public User(String name, String password)
  {
    this.name = name.toCharArray();
    this.password = password.toCharArray();
    this.isLoggedIn = false;
  }

}

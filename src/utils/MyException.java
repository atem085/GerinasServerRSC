package utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MyException extends Exception
{
  public MyException(String message)
  {
    super(message);
  }

  public String toString()
  {
    return super.getMessage();
  }

}

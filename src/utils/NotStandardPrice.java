package utils;

import java.math.BigInteger;
import java.io.Serializable;

public class NotStandardPrice extends NotStandardDecimal implements Serializable
{
 private String germanPrice;
 private String normalPrice;
 private String language;

 public NotStandardPrice()
 {
   super();
   this.germanPrice = "0,00";
   this.normalPrice = "0.00";
   this.language = "en";
 }

 public NotStandardPrice(double price, String language)
 {
  super(Round.round(price, 2));
  this.language = language;
  germanPrice = super.getGermanValue();
  if(Double.isNaN(super.getNormalValue()))
   normalPrice = "N";
  else
   normalPrice = "" + super.getNormalValue();
  normalize1();
  normalize2();
 }
 public NotStandardPrice(String price, String language)
 {
  super(Round.round(price, 2));
  this.language = language;
  germanPrice = super.getGermanValue();
  if(Double.isNaN(super.getNormalValue()))
   normalPrice = "N";
  else
   normalPrice = "" + super.getNormalValue();
  normalize1();
  normalize2();
 }

 private void normalize1()
 {
  if(germanPrice.equalsIgnoreCase("N")) return;
  int length = germanPrice.length();
  int afterComma = -1;
  int i = 0;
  for(; i < length; i++)
   if(germanPrice.charAt(i) == ',') break;
  if(i == length) germanPrice = germanPrice + ",00";
  else if(i == length - 1) germanPrice = germanPrice + "00";
  else if(i == length - 2) germanPrice = germanPrice + "0";
  else if(i < length - 3) germanPrice = germanPrice.substring(0, i + 3);
  if(germanPrice.length() > 6)
  {
    long c = 1;
    BigInteger bi;
    BigInteger comp = BigInteger.valueOf(3);
    StringBuffer ger = new StringBuffer(germanPrice);
    for(i = germanPrice.length() - 4; i > 0; i--, c++)
    {
      bi = BigInteger.valueOf(c);
      if(bi.mod(comp).intValue() == 0)
       ger.insert(i, ".");
    }
    if(ger.charAt(0) == '-' && ger.charAt(1) == '.')
      ger.deleteCharAt(1);
    germanPrice = new String(ger);
  }
 }

 private void normalize2()
 {
  if(normalPrice.equalsIgnoreCase("N")) return;
  int length = normalPrice.length();
  int afterComma = -1;
  int i = 0;
  for(; i < length; i++)
   if(normalPrice.charAt(i) == '.') break;
  if(i == length) normalPrice = normalPrice + ".00";
  else if(i == length - 1) normalPrice = normalPrice + "00";
  else if(i == length - 2) normalPrice = normalPrice + "0";
  else if(i < length - 3) normalPrice = normalPrice.substring(0, i + 3);
  if(normalPrice.length() > 6)
  {
    long c = 1;
    BigInteger bi;
    BigInteger comp = BigInteger.valueOf(3);
    StringBuffer ger = new StringBuffer(normalPrice);
    for(i = normalPrice.length() - 4; i > 0; i--, c++)
    {
      bi = BigInteger.valueOf(c);
      if(bi.mod(comp).intValue() == 0)
       ger.insert(i, ",");
    }
    if(ger.charAt(0) == '-' && ger.charAt(1) == ',')
      ger.deleteCharAt(1);
    normalPrice = new String(ger);
  }
 }

 public String getPrice(String language)
 {
  if(language.equals("en"))
   return normalPrice;
  return germanPrice;
 }

 public String toString()
 {
   return this.getPrice(this.language);
 }

}

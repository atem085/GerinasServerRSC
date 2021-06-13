package utils;

import java.math.BigInteger;
import java.io.Serializable;

public class NotStandardWeight extends NotStandardDecimal implements Serializable
{
 private String germanWeight;
 private String normalWeight;
 private String language;

 public NotStandardWeight()
 {
   super();
   this.germanWeight = "0,000";
   this.normalWeight = "0.000";
   this.language = "en";
 }

 public NotStandardWeight(double weight, String language)
 {
  super(Round.round(weight, 3));
  this.language = language;
  germanWeight = super.getGermanValue();
  if(Double.isNaN(super.getNormalValue()))
   normalWeight = "N";
  else
   normalWeight = "" + super.getNormalValue();
  normalize1();
  normalize2();
 }
 public NotStandardWeight(String weight, String language)
 {
  super(Round.round(weight, 3));
  this.language = language;
  germanWeight = super.getGermanValue();
  if(Double.isNaN(super.getNormalValue()))
   normalWeight = "N";
  else
   normalWeight = "" + super.getNormalValue();
  normalize1();
  normalize2();
 }

 private void normalize1()
 {
  if(germanWeight.equalsIgnoreCase("N")) return;
  int length = germanWeight.length();
  int afterComma = -1;
  int i = 0;
  for(; i < length; i++)
   if(germanWeight.charAt(i) == ',') break;
  if(i == length) germanWeight = germanWeight + ",000";
  if(i == length - 1) germanWeight = germanWeight + "000";
  if(i == length - 2) germanWeight = germanWeight + "00";
  if(i == length - 3) germanWeight = germanWeight + "0";
  if(i < length - 4) germanWeight = germanWeight.substring(0, i + 4);
  if(germanWeight.length() > 7)
  {
    long c = 1;
    BigInteger bi;
    BigInteger comp = BigInteger.valueOf(3);
    StringBuffer ger = new StringBuffer(germanWeight);
    for(i = germanWeight.length() - 5; i > 0; i--, c++)
    {
      bi = BigInteger.valueOf(c);
      if(bi.mod(comp).intValue() == 0)
       ger.insert(i, ".");
    }
    if(ger.charAt(0) == '-' && ger.charAt(1) == '.')
      ger.deleteCharAt(1);
    germanWeight = new String(ger);
  }
 }

 private void normalize2()
 {
  if(normalWeight.equalsIgnoreCase("N")) return;
  int length = normalWeight.length();
  int afterComma = -1;
  int i = 0;
  for(; i < length; i++)
   if(normalWeight.charAt(i) == '.') break;
  if(i == length) normalWeight = normalWeight + ".000";
  if(i == length - 1) normalWeight = normalWeight + "000";
  if(i == length - 2) normalWeight = normalWeight + "00";
  if(i == length - 3) normalWeight = normalWeight + "0";
  if(i < length - 4) normalWeight = normalWeight.substring(0, i + 4);
  if(normalWeight.length() > 7)
  {
    long c = 1;
    BigInteger bi;
    BigInteger comp = BigInteger.valueOf(3);
    StringBuffer ger = new StringBuffer(normalWeight);
    for(i = normalWeight.length() - 5; i > 0; i--, c++)
    {
      bi = BigInteger.valueOf(c);
      if(bi.mod(comp).intValue() == 0)
       ger.insert(i, ",");
    }
    if(ger.charAt(0) == '-' && ger.charAt(1) == ',')
      ger.deleteCharAt(1);
    normalWeight = new String(ger);
  }
 }

 public String getWeight(String language)
 {
  if(language.equals("en")) return this.normalWeight;
  return germanWeight;
 }

 public String toString()
 {
   return this.getWeight(this.language);
 }

}

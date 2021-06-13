package utils;

import java.io.Serializable;

public class NotStandardDecimal implements Comparable, Serializable
{
 protected double decimalNumber;
 protected String decimalString;
 protected boolean germanDecimal;

 public NotStandardDecimal()
 {
   this.decimalNumber = 0;
   this.decimalString = "0";
   this.germanDecimal = false;
 }

 public NotStandardDecimal(double d)
 {
  decimalNumber = d;
  decimalString = null;
  germanDecimal = false;
 }
 public NotStandardDecimal(String d, boolean g)
 {
  decimalString = d;
  germanDecimal = g;
  this.decimalNumber = 0;
  this.decimalNumber = this.getNormalValue();
 }

 public double getNormalValue()
 {
  if(decimalString == null || decimalString.equals("null")) return decimalNumber;
  if(!germanDecimal)
  {
   String nnv = "";
   int length = decimalString.length();
   char c;
   boolean afterPoint = false;
   for(int i = 0; i < length; i++)
    {
     c = decimalString.charAt(i);
     if(c !=',') nnv = nnv + c;
     if((c == '.' || c == ',') && afterPoint) return Double.NaN;
     if(c == '.') afterPoint = true;
    }
   try
   {
    return Double.parseDouble(nnv);
   }
   catch(NumberFormatException e)
   {
    return Double.NaN;
   }
  }
  return convertToNormalValue();
 }

 private double convertToNormalValue()
 {
  String nv = "";
  boolean afterComma = false;
  int length = decimalString.length();
  char c;
  for(int i = 0; i < length; i++)
  {
   c = decimalString.charAt(i);
   if((c == '.' || c == ',') && afterComma) return Double.NaN;
   else if(c == ',')
         {
          nv = nv + ".";
          afterComma = true;
         }
   else if(c != '.') nv = nv + c;
  }
  try
  {
   return Double.parseDouble(nv);
  }
  catch(NumberFormatException e)
  {
   return Double.NaN;
  }
 }

 private String convertToGermanValue(double d)
 {
  Double dou = new Double(d);
  if(dou.isNaN()) return "N";
  String gv = "";
  String n = "" + d;
  int length = n.length();
  char c;
  for(int i = 0; i < length; i++)
  {
   c = n.charAt(i);
   if(c == '.') gv = gv + ",";
   else if(c == ',') gv = gv + "";
   else gv = gv + c;
  }
  return gv;
 }

 public String getGermanValue()
 {
  return convertToGermanValue(getNormalValue());
 }

 public int compareTo(NotStandardDecimal d)
 {
   if(Double.isNaN(this.getNormalValue()) && Double.isNaN(d.getNormalValue())) return 0;
   if(Double.isNaN(this.getNormalValue()) && !Double.isNaN(d.getNormalValue())) return -1;
   if(Double.isNaN(d.getNormalValue())) return 1;
   return new Double(this.getNormalValue()).compareTo(new Double(d.getNormalValue()));
 }

 public int compareTo(Object d)
 {
   double value = 0;
   try
   {
     value = ((NotStandardDecimal)d).getNormalValue();
   }
   catch(ClassCastException e)
   {
     return 0;
   }
   return new Double(this.getNormalValue()).compareTo(new Double(value));
 }

 public boolean equals(NotStandardDecimal d)
 {
   return this.compareTo(d) == 0;
 }

 public boolean equals(Object d)
 {
   try
   {
     return this.compareTo((NotStandardDecimal)d) == 0;
   }
   catch(ClassCastException e)
   {
     return true;
   }
 }

}

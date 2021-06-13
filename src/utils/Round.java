package utils;

public final class Round
{
 public static double round(double d, int afterPoint)
 {
   if(Double.isNaN(d)) return Double.NaN;
   double res = 0;
   if(d < 0)
   {
     d = d * -1;
     res = (double) Math.round(d * (int) Math.pow(10, afterPoint)) /
         (int) Math.pow(10, afterPoint);
     res = res * -1;
   }
   else
     res = (double) Math.round(d * (int) Math.pow(10, afterPoint)) /
       (int) Math.pow(10, afterPoint);
   return res;
 }

 static double round(String d, int afterComma)
 {
  if(d.equals("N")) return Double.NaN;
  double dd = 0;
  try
  {
   dd = Double.parseDouble(d);
  }
  catch(NumberFormatException e)
  {
   NotStandardDecimal g = new NotStandardDecimal(d, true);
   dd = g.getNormalValue();
  }
  return round(dd, afterComma);
 }

}

/*
 * History (tab separated):-
 * Vers Date    By  Spec                        Description
 * 1    2/4/05                                  Base
 * 2    2/4/05  MD  Need to take care of        Added parameter to locales.cfg to
 *                  dbl byte encoding issues    specify encoding string.  This must
 *                                              be one of the valid xerces encoding schemes.
 */


/*
 * @copyright (c) 1998-2002 Retek Inc
 */


package  com.chelseasystems.cr.xml;

import java.awt.*;
import java.io.*;
import java.util.*;

import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;

import org.apache.xerces.parsers.*;
import org.apache.xml.serialize.*;
import org.w3c.dom.*;
import org.xml.sax.*;


/**
 *
 */
public class XMLUtil {
    /**
     * Default encoding
     */
    public static String DEFAULT_CHAR_ENCODING = "UTF-8";

    // Encoding to use for XML parsing
    private static String encoding = null;

    static {
        ConfigMgr configMgr= new ConfigMgr("locales.cfg");
        if (configMgr == null) {
            encoding = DEFAULT_CHAR_ENCODING;
        }
        encoding = configMgr.getString("XML_ENCODING");
        if (encoding == null) {
            encoding = DEFAULT_CHAR_ENCODING;
        }
    }

   /**
    *
    * @param xml
    * @return
    * @exception SAXException, IOException
    */
   public static Document getDocumentFromXMLString (String xml)
         throws SAXException, IOException {
      DOMParser parser = new DOMParser();
      byte[] buff = xml.getBytes();
      parser.parse(new InputSource(new ByteArrayInputStream(buff)));
      return  parser.getDocument();
   }

   /**
    *
    * @param fileName
    * @return
    * @exception SAXException, IOException
    */
   public static Document getDocumentFromFile (String fileName)
         throws SAXException, IOException {
      DOMParser parser = new DOMParser();
      FileInputStream fileInputStream = new FileInputStream(fileName);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
      parser.parse(new InputSource(inputStreamReader));
      inputStreamReader.close();
      return  parser.getDocument();
   }

   /**
    *
    * @param document
    * @return
    * @exception IOException
    */
   public static String getXMLStringFromDocument (Document document) throws IOException {
      OutputFormat format = new OutputFormat(document);
      format.setEncoding(encoding);
      format.setIndenting(true);
      StringWriter stringOut = new StringWriter();
      XMLSerializer serial = new XMLSerializer(stringOut, format);
      serial.asDOMSerializer();
      serial.serialize(document.getDocumentElement());
      return  stringOut.toString();
   }

   /**
    *
    * @param parent
    * @param tagName
    * @return
    */
   public static Element[] getChildElements (Element parent, String tagName) {
      Vector elements = new Vector();
      NodeList nodeList = parent.getChildNodes();
      if (nodeList != null && nodeList.getLength() > 0)
         for (int i = 0; i < nodeList.getLength(); i++)
            if (((Node)nodeList.item(i)).getNodeType() == Node.ELEMENT_NODE
                  && ((Element)nodeList.item(i)).getTagName().equals(tagName))
               elements.add((Element)nodeList.item(i));
      return  (Element[])elements.toArray(new Element[0]);
   }

   /**
    *
    * @param parent
    * @param tagName
    * @return
    */
   public static Element getFirstChildElement (Element parent, String tagName) {
      NodeList nodeList = parent.getChildNodes();
      if (nodeList != null && nodeList.getLength() > 0)
         for (int i = 0; i < nodeList.getLength(); i++)
            if (((Node)nodeList.item(i)).getNodeType() == Node.ELEMENT_NODE
                  && ((Element)nodeList.item(i)).getTagName().equals(tagName))
               return  (Element)nodeList.item(i);
      return  null;
   }

   /**
    *
    * @param parent
    * @return
    */
   public static Element getFirstChildElement (Element parent) {
      NodeList nodeList = parent.getChildNodes();
      if (nodeList != null && nodeList.getLength() > 0)
         for (int i = 0; i < nodeList.getLength(); i++)
            if (((Node)nodeList.item(i)).getNodeType() == Node.ELEMENT_NODE)
               return  (Element)nodeList.item(i);
      return  null;
   }

   /**
    *
    * @param parent
    * @param tagName
    * @return
    */
   public static boolean removeFirstChild (Element parent, String tagName) {
      NodeList nodeList = parent.getChildNodes();
      if (nodeList != null && nodeList.getLength() > 0)
         for (int i = 0; i < nodeList.getLength(); i++)
            if (((Node)nodeList.item(i)).getNodeType() == Node.ELEMENT_NODE
                  && ((Element)nodeList.item(i)).getTagName().equals(tagName)) {
               parent.removeChild(nodeList.item(i));
               return  true;
            }
      return  false;
   }

   /**
    *
    * @param doc
    * @param parent
    * @param itemName
    * @param itemValue
    * @return
    */
   public static Element addItem (Document doc, Element parent, String itemName,
         int itemValue) {
      return  addItem(doc, parent, itemName, new Integer(itemValue));
   }

   /**
    *
    * @param doc
    * @param parent
    * @param itemName
    * @param itemValue
    * @return
    */
   public static Element addItem (Document doc, Element parent, String itemName,
         long itemValue) {
      return  addItem(doc, parent, itemName, new Long(itemValue));
   }

   /**
    *
    * @param doc
    * @param parent
    * @param itemName
    * @param itemValue
    * @return
    */
   public static Element addItem (Document doc, Element parent, String itemName,
         double itemValue) {
      return  addItem(doc, parent, itemName, new Double(itemValue));
   }

   /**
    *
    * @param doc
    * @param parent
    * @param itemName
    * @param itemValue
    * @return
    */
   public static Element addItem (Document doc, Element parent, String itemName,
         boolean itemValue) {
      return  addItem(doc, parent, itemName, new Boolean(itemValue));
   }

   /**
    *
    * @param doc
    * @param parent
    * @param itemName
    * @param itemValue
    * @return
    */
   public static Element addItem (Document doc, Element parent, String itemName, Object itemValue) {
      Element item = doc.createElement(itemName);
      String itemValueInString = "";
      if (itemValue instanceof java.util.Date) {
         itemValueInString = XMLUtil.convertTimestampToString((java.util.Date)itemValue);
      } else if (itemValue instanceof java.util.Calendar) {
         itemValueInString = XMLUtil.convertCalendarToString((java.util.Calendar)itemValue);
      } else if (itemValue instanceof ArmCurrency) {
         itemValueInString = ((ArmCurrency)itemValue).toDelimitedString();
      } else if (itemValue instanceof CurrencyType) {
         itemValueInString = ((CurrencyType)itemValue).getCode();
      } else if (itemValue instanceof java.awt.Dimension) {
         java.awt.Dimension d = (java.awt.Dimension)itemValue;
         itemValueInString = d.width + "," + d.height;
      }  else if (itemValue instanceof java.awt.Point) {
         java.awt.Point p = (java.awt.Point)itemValue;
         itemValueInString = p.x + "," + p.y;
      }  else if (itemValue instanceof java.awt.Rectangle) {
         java.awt.Rectangle r = (java.awt.Rectangle)itemValue;
         itemValueInString = r.x + "," + r.y + "," + r.width + "," + r.height;
      }  else if (itemValue instanceof java.awt.Color) {
         java.awt.Color color = (java.awt.Color)itemValue;
         itemValueInString = "" + color.getRGB();
      }  else if (itemValue instanceof Telephone) {
         Telephone telephone = (Telephone)itemValue;
         StringBuffer sb = new StringBuffer();
         sb.append(telephone.getTelephoneType().getType());
         sb.append("-");
         sb.append(telephone.getCountryCode());
         sb.append("-");
         sb.append(telephone.getAreaCode().length() == 0?" ":telephone.getAreaCode());
         sb.append("-");
         sb.append(telephone.getTelephoneNumber().length() == 0?" ":telephone.getTelephoneNumber());
         sb.append("-");
         sb.append(telephone.getExtension().length() == 0?" ":telephone.getExtension());
         itemValueInString = sb.toString();
      }  else if (itemValue != null) {
         itemValueInString = itemValue.toString();
      }
      item.appendChild(doc.createTextNode(itemValueInString));
      parent.appendChild(item);
      return  item;
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static String getValueAsString (Element element, String tagName) {
      return  (String)getValue(element, tagName, String.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static String getValueAsFileName (Element element, String tagName) {
      String fileName = (String)getValue(element, tagName, String.class);
      return  FileUtil.convertFileSeperators(fileName);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Integer getValueAsInteger (Element element, String tagName) {
      return  (Integer)getValue(element, tagName, Integer.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static int getValueAs_int (Element element, String tagName) {
      return  ((Integer)getValue(element, tagName, Integer.class)).intValue();
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Long getValueAsLong (Element element, String tagName) {
      return  (Long)getValue(element, tagName, Long.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static long getValueAs_long (Element element, String tagName) {
      return  ((Long)getValue(element, tagName, Long.class)).longValue();
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Double getValueAsDouble (Element element, String tagName) {
      return  (Double)getValue(element, tagName, Double.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static double getValueAs_double (Element element, String tagName) {
      return  ((Double)getValue(element, tagName, Double.class)).doubleValue();
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Boolean getValueAsBoolean (Element element, String tagName) {
      return  (Boolean)getValue(element, tagName, Boolean.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static boolean getValueAs_boolean (Element element, String tagName) {
      return  ((Boolean)getValue(element, tagName, Boolean.class)).booleanValue();
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.awt.Point getValueAsPoint (Element element, String tagName) {
      return  (java.awt.Point)getValue(element, tagName, java.awt.Point.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.awt.Dimension getValueAsPDimension (Element element, String tagName) {
      return  (java.awt.Dimension)getValue(element, tagName, java.awt.Dimension.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.awt.Rectangle getValueAsRectangle (Element element, String tagName) {
      return  (java.awt.Rectangle)getValue(element, tagName, java.awt.Rectangle.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.util.Date getValueAsDateOrNull (Element element, String tagName) {
      Object date = getValue(element, tagName, java.util.Date.class);
      return  (date == null) ? null : (java.util.Date)date;
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.util.Date getValueAsDate (Element element, String tagName) {
      Object date = getValue(element, tagName, java.util.Date.class);
      return  (date == null) ? new java.util.Date() : (java.util.Date)date;
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.util.Calendar getValueAsCalendarOrNull (Element element, String tagName) {
      Object calendar = getValue(element, tagName, java.util.Calendar.class);
      return  (calendar == null) ? null : (java.util.Calendar)calendar;
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static java.util.Calendar getValueAsCalendar (Element element, String tagName) {
      Object calendar = getValue(element, tagName, java.util.Calendar.class);
      return  (calendar == null) ? Calendar.getInstance() : (java.util.Calendar)calendar;
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static ArmCurrency getValueAsCurrency (Element element, String tagName) {
      return  (ArmCurrency)getValue(element, tagName, ArmCurrency.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Color getValueAsColor (Element element, String tagName) {
      return  (Color)getValue(element, tagName, Color.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static CurrencyType getValueAsCurrencyType (Element element, String tagName) {
      return  (CurrencyType)getValue(element, tagName, CurrencyType.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @return
    */
   public static Telephone getValueAsTelephone (Element element, String tagName) {
      return  (Telephone)getValue(element, tagName, Telephone.class);
   }

   /**
    *
    * @param element
    * @param tagName
    * @param dataType
    * @return
    */
   public static Object getValue (Element element, String tagName, Class dataType) {
      String string = "";
      NodeList nodeList = element.getElementsByTagName(tagName);
      if (nodeList != null && nodeList.getLength() > 0) {
         NodeList textNodeList = nodeList.item(0).getChildNodes();
         if (textNodeList != null && textNodeList.getLength() > 0) {
            for (int i = 0; i < textNodeList.getLength(); i++) {
               string = string.concat(textNodeList.item(i).getNodeValue().trim());
            }
         }
      }
      return  getValueFromString(dataType, string);
   }

   /**
    *
    * @param returnType
    * @param value
    * @return
    */
   public static Object getValueFromString (Class returnType, String value) {
      if (returnType == String.class)
         return  (String)value;
      else if (returnType == Integer.class || returnType == Integer.TYPE)
         return  (value == "")? new Integer(0) : new Integer(value);
      else if (returnType == Long.class || returnType == Long.TYPE)
         return  (value == "")? new Long(0l) : new Long(value);
      else if (returnType == Double.class || returnType == Double.TYPE)
         return  (value == "")? new Double(0d) : new Double(value);
      else if (returnType == Boolean.class || returnType == Boolean.TYPE)
         return  (value == "")? new Boolean("false") : new Boolean(value);
      else if (returnType == java.awt.Point.class)
         return  XMLUtil.convertStringToPoint(value);
      else if (returnType == java.awt.Rectangle.class)
         return  XMLUtil.convertStringToRectangle(value);
      else if (returnType == java.util.Date.class)
         return  XMLUtil.convertStringToTimestamp(value);
      else if (returnType == java.util.Calendar.class)
         return XMLUtil.convertStringToCalendar(value);
      else if (returnType == ArmCurrency.class)
         try {
            if (value == null || value.length() == 0)
              return null;
            else
              return  ArmCurrency.valueOf(value);
         } catch (UnsupportedCurrencyTypeException ex) {
            ex.printStackTrace();
            return  null;
         }
      else if (returnType == CurrencyType.class)
         try {
            return  CurrencyType.getCurrencyType(value);
         } catch (UnsupportedCurrencyTypeException ex) {
            ex.printStackTrace();
            return  null;
         }
      else if (returnType == Color.class)
        return XMLUtil.convertStringToColor(value);
      else if (returnType == Telephone.class)
        return XMLUtil.convertStringToTelephone(value);
      else
         return  "";
   }

   /**
    *
    * @param fileName
    * @param document
    * @exception IOException
    */
   public static void writeToFile (String fileName, Document document) throws IOException {
      OutputFormat format = new OutputFormat(document);
      format.setEncoding(encoding);
      format.setIndenting(true);
      StringWriter stringOut = new StringWriter();
      XMLSerializer serial = new XMLSerializer(stringOut, format);
      serial.asDOMSerializer();
      serial.serialize(document.getDocumentElement());
      writeToFile(fileName, stringOut.toString());
   }

   /**
    *
    * @param fileName
    * @param xml
    * @exception IOException
    */
   public static void writeToFile (String fileName, String xml) throws IOException {
      FileOutputStream fileOutputStream = new FileOutputStream(fileName);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, encoding);
      BufferedWriter writer = new BufferedWriter(outputStreamWriter);
      System.out.println("Writing XML file " + fileName);
      writer.write(xml);
      writer.close();
   }

   /**
    *
    * @param date
    * @return
    */
   public static String convertTimestampToString (java.util.Date date) {
      if (date == null) return "";
      return StandardDateFormat.formatTimeWithTimezone(date);
   }

   /**
    *
    * @param s
    * @return
    */
   public static java.util.Date convertStringToTimestamp (String s) {
      java.util.Date date = null;
      if (s != null && s.length() > 0) {
         try {
            return StandardDateFormat.parseUsingTimeWithTimezoneFormat(s);
         } catch (java.text.ParseException exp) {
            System.out.println("Can't parse date " + s + ". Not in format " + StandardDateFormat.TIME_TIMEZONE_FORMAT);
         }
      }
      return  date;
   }


   /**
    *
    * @param calendar
    * @return
    */
  static public String convertCalendarToString(Calendar calendar) {
    if (calendar == null) return null;
    return convertTimestampToString(calendar.getTime()) + "@" + calendar.getTimeZone().getID();
  }

   /**
    *
    * @param string
    * @return
    */
  static public Calendar convertStringToCalendar(String string) {
    int indexOfAt = string.indexOf("@");
    Date date = convertStringToTimestamp(string.substring(0, indexOfAt));
    TimeZone timeZone = TimeZone.getTimeZone(string.substring(indexOfAt + 1));
    Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTime(date);
    return calendar;
  }

   /**
    *
    * @param s
    * @return
    */
   public static java.awt.Point convertStringToPoint (String s) {
      java.awt.Point p = new java.awt.Point();
      StringTokenizer tok = new StringTokenizer(s, ",");
      try {
         if (tok.hasMoreTokens())
            p.x = Integer.parseInt(tok.nextToken());
         if (tok.hasMoreTokens())
            p.y = Integer.parseInt(tok.nextToken());
      } catch (NumberFormatException e) {}
      return  p;
   }

   /**
    *
    * @param s
    * @return
    */
   public static java.awt.Dimension convertStringToDimension (String s) {
      java.awt.Dimension d = new java.awt.Dimension();
      StringTokenizer tok = new StringTokenizer(s, ",");
      try {
         if (tok.hasMoreTokens())
            d.width = Integer.parseInt(tok.nextToken());
         if (tok.hasMoreTokens())
            d.height = Integer.parseInt(tok.nextToken());
      } catch (NumberFormatException e) {}
      return  d;
   }

   /**
    *
    * @param s
    * @return
    */
   public static java.awt.Rectangle convertStringToRectangle (String s) {
      java.awt.Rectangle r = new java.awt.Rectangle();
      StringTokenizer tok = new StringTokenizer(s, ",");
      try {
         if (tok.hasMoreTokens())
            r.x = Integer.parseInt(tok.nextToken());
         if (tok.hasMoreTokens())
            r.y = Integer.parseInt(tok.nextToken());
         if (tok.hasMoreTokens())
            r.width = Integer.parseInt(tok.nextToken());
         if (tok.hasMoreTokens())
            r.height = Integer.parseInt(tok.nextToken());
      } catch (NumberFormatException e) {}
      return  r;
   }

  /**
  *
  * @param string
  * @return
  */
  public static java.awt.Color convertStringToColor(String string) {
    if (string == null) return null;
    int rgb = new Integer(string).intValue();
    return new Color(rgb);
  }

  /**
  *
  * @param string
  * @return
  */
  public static Telephone convertStringToTelephone(String string) {
    if (string == null || string.length() == 0) return null;
    TelephoneType telephoneType = TelephoneType.UNKNOWN;
    String countryCode = "";
    String areaCode = "";
    String phoneNumber = "";
    String extension = "";
    StringTokenizer st = new StringTokenizer(string, "-");
    if (st.hasMoreTokens()) telephoneType = new TelephoneType(st.nextToken());
    if (st.hasMoreTokens()) countryCode = st.nextToken().trim();
    if (st.hasMoreTokens()) areaCode = st.nextToken().trim();
    if (st.hasMoreTokens()) phoneNumber = st.nextToken().trim();
    if (st.hasMoreTokens()) extension = st.nextToken().trim();
    return new Telephone(telephoneType, countryCode, areaCode, phoneNumber, extension);
  }

   /**
    *
    * @param aType
    * @return
    */
   public static boolean isTypeSupported (Class aType) {
      return  (
         (aType.isPrimitive()) ||
         (aType == String.class) ||
         (aType == Integer.class) ||
         (aType == Long.class) ||
         (aType == Double.class) ||
         (aType == Boolean.class) ||
         (aType == java.util.Date.class) ||
         (aType == java.util.Calendar.class) ||
         (aType == ArmCurrency.class) ||
         (aType == CurrencyType.class) ||
         (aType == java.awt.Dimension.class) ||
         (aType == java.awt.Point.class) ||
         (aType == java.awt.Rectangle.class) ||
         (aType == java.awt.Color.class) ||
         (aType == Telephone.class)
      );
   }

   /**
    *
    * @param strComponentType
    * @return
    * @throws Exception
    */
   public static Class parseClass (String strComponentType) throws  Exception
   {
      Class componentType = null;
      if (strComponentType.equals(Double.TYPE.getName()))
         componentType = Double.TYPE;
      else if (strComponentType.equals(Float.TYPE.getName()))
         componentType = Float.TYPE;
      else if (strComponentType.equals(Integer.TYPE.getName()))
         componentType = Integer.TYPE;
      else if (strComponentType.equals(Long.TYPE.getName()))
         componentType =  Long.TYPE;
      else if (strComponentType.equals(Boolean.TYPE.getName()))
         componentType = Boolean.TYPE;
      else
         componentType = Class.forName(strComponentType);
      return  componentType;
   }

   /**
    * <blockquote><pre>
    * B            byte
    * C            char
    * D            double
    * F            float
    * I            int
    * J            long
    * L<i>classname;</i>  class or interface
    * S            short
    * Z            boolean
    * </pre></blockquote>
    * @param aPrimitiveArray
    * @return class name of primitive type of array
    */
   public static String getTagNameFromArrayClass (Class aPrimitiveArray)
   {
      char lastCharOfName = aPrimitiveArray.getName().charAt(aPrimitiveArray.getName().length() - 1);
      if (lastCharOfName == 'D')
         return  Double.TYPE.getName();
      else if (lastCharOfName == 'F')
         return  Float.TYPE.getName();
      else if (lastCharOfName == 'I')
         return  Integer.TYPE.getName();
      else if (lastCharOfName == 'J')
         return  Long.TYPE.getName();
      else if (lastCharOfName == 'Z')
         return  Boolean.TYPE.getName();
      else
      {
         String name = aPrimitiveArray.getName();
         return  name.substring(name.indexOf('L') + 1, name.length() - 1);
      }
   }
   
   public static String getEncoding() {
	   return encoding;
   }

}




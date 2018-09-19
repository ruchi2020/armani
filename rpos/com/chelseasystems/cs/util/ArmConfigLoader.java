/*
 * Creato il 29-nov-05
 *
 * Per modificare il modello associato a questo file generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
package com.chelseasystems.cs.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;


/**
 * @author Aguiari_S
 *
 * Per modificare il modello associato al commento di questo tipo generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
public class ArmConfigLoader {

  private static Hashtable armHash = new Hashtable();

  static{
    loadArmaniConfig();
//    System.out.println("****ArmConfigLoader Done!!!");
  }

  /**
   *
   */
  public ArmConfigLoader() {
    super();
  }

  private static void loadArmaniConfig() {

    boolean EOF = false;
    String line = "";
    String code = null;
    String label = null;
    String value = null;

    FileReader in;
    try {
      in = new FileReader("..//files//prod//config//ArmaniCommon.cfg");
      BufferedReader buff = new BufferedReader(in);
      while (!EOF) {
        try {
          line = buff.readLine();
          if (line == null) {
            in.close();
            break;
          }
          if (line != null) {
            if (line.indexOf("COUNTRY") > 0) {
              int a = 0;
            }
            code = getCode(line);
            if (code != null)
              value = getValue(line);
          }
          if (code != null) {

            armHash.put(code, value);
            line = null;
          }
          line = buff.readLine();
          if (line != null) {
            label = getCode(line);
            if (label != null)

              value = getValue(line);
          }
          if (label != null) {
            armHash.put(label, value);
            line = null;
          }
//          System.out.println(label);
//          System.out.println(value);
          code = null;
          label = null;
        }
        catch (IOException e1) {
          // TODO Blocco catch generato automaticamente
          e1.printStackTrace();
        }catch (Exception ex){
          ex.printStackTrace();
        }
      }
    }
    catch (FileNotFoundException e) {
      // TODO Blocco catch generato automaticamente
      e.printStackTrace();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  private static String getCode(String line) {

    String c = null;
    int i = line.indexOf("=");

    if (i > 0) {
      c = line.substring(0, i);
    }
    else {
      int x = 0;

    }

    return c;
  }

  private static String getValue(String line) {

    String c = null;
    int i = line.indexOf("=");
    c = line.substring(i + 1, line.length());
    c = c.replace('^',' ');
    return c;
  }

  /**
   * @return
   */
  public Hashtable getArmHash() {
    return armHash;
  }

  /**
   * @param hashtable
   */
  public void setArmHash(Hashtable hashtable) {
    armHash = hashtable;

  }

  /**
   *
   * @param key String
   * @return String
   */
  public String getString(String key){
    return (String)armHash.get(key);
  }


}

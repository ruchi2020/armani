/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.utils;

import java.util.Properties;

/**
 *
 * @author Tagbeyves
 */
public class ConfigurationManager {

    public String getConfig(String file, String key) {
        Properties configFile;
        String value = "[CONFIG MANAGER] NO VALUE FOUND FOR KEY '" + key + "' IN '" + file + "'";
        configFile = new java.util.Properties();
        try {
            configFile.load(getClass().getClassLoader().getResourceAsStream(file));
            value = configFile.getProperty(key);

        } catch (Exception eta) {
            eta.printStackTrace();
            System.out.println("[CONFIG MANAGER] ERRORS IN GETTING THE VALUE FOR THE KEY '" + key + "' IN '" + file + "'");
            return value;
        }

        return value;

    }

    public static void main(String[] args) {

        ConfigurationManager config = new ConfigurationManager();
        String value = (config.getConfig("arm.cfg", "LOCALE")).substring(3);
        System.out.println(value);
    }

}

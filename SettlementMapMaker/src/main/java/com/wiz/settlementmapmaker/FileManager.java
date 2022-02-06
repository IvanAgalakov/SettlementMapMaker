/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 904187003
 */
public class FileManager {
    
    
    public static void saveSettlement(Settlement settle, String path) {
        Gson settleGson = new Gson();
        String save = settleGson.toJson(settle);
        try {
            Files.writeString(Path.of(path), save);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Settlement openSettlement(String path) {
        try {
            String save = Files.readString(Path.of(path));
            Gson settleGson = new Gson();
            Settlement settle = settleGson.fromJson(save, Settlement.class);
            return settle;
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}

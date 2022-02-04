/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author 904187003
 */
public class FileManager {
    
    
    public static void saveSettlement(Settlement settle, String path) throws IOException {
        Gson settleGson = new Gson();
        String save = settleGson.toJson(settle);
        Files.writeString(Path.of(path), save);
    }
    
    public static Settlement openSettlement(String path) throws IOException {
        String save = Files.readString(Path.of(path));
        Gson settleGson = new Gson();
        Settlement settle = settleGson.fromJson(save, Settlement.class);
        return settle;
    }
    
    
}

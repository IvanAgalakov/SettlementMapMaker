/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class SettlementNameGenerator {
    
    private static HashMap<String, ArrayList<String>> randomNameLists = new HashMap();
    
    public static void loadAllNames() {
        String dir = System.getProperty("user.dir");
        dir += "\\Name Generation";
        System.out.println(dir);
        ArrayList<String> allPaths = new ArrayList<>();
        //try {
      //      URI uri = SettlementNameGenerator.class.getResource("/Name Generation").toURI();
            Path dirPath = Paths.get(dir);
            try {
                Files.list(dirPath)
                        .forEach(p -> allPaths.add(p.toString()));
            } catch (IOException ex) {
                Logger.getLogger(SettlementNameGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
       // } catch (URISyntaxException ex) {
      //      Logger.getLogger(SettlementNameGenerator.class.getName()).log(Level.SEVERE, null, ex);
      //  }

        for (int i = 0; i < allPaths.size(); i++) {
            try {
                File f = new File(allPaths.get(i));
                Scanner scan = new Scanner(f);
                
                ArrayList<String> toAdd = new ArrayList<>();
                randomNameLists.put(f.getName(), toAdd);
                
                while(scan.hasNextLine()) {
                    toAdd.add(scan.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SettlementNameGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public static String getRandomSettlementName() {
        Random ran = new Random();
        ArrayList<String> first = randomNameLists.get(Constants.SETTLEMENT_NAME_FIRST);
        ArrayList<String> second = randomNameLists.get(Constants.SETTLEMENT_NAME_SECOND);
        
        if (first != null && second != null) {
            return first.get(ran.nextInt(first.size())) + second.get(ran.nextInt(second.size()));
        }
        return "";
    }
    
}

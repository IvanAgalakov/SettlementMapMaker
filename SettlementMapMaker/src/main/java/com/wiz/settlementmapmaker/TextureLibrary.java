/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class TextureLibrary {

    public List<String> textureNames = new ArrayList<>();

    public void loadAllTextures() {
        List<String> filenames = new ArrayList<>();

        try (
            InputStream in = this.getClass().getResourceAsStream("/Texture/");
            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (IOException ex) {
            Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        textureNames = filenames;
        
        for(String s : textureNames) {
            System.out.println(s);
        }
    }
}

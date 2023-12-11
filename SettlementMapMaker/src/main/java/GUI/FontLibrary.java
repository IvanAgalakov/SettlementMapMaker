/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import imgui.ImFont;
import imgui.ImGuiIO;

/**
 *
 * @author Ivan
 */
public class FontLibrary {
    private static ArrayList<String> fontLocations = new ArrayList<>();
    
    private static HashMap<String, ImFont> fonts = new HashMap<>();
    private static ArrayList<String> fontNames = new ArrayList<>();

    public static void loadAllFonts(ImGuiIO io) {
        String dir = System.getProperty("user.dir");
        dir += "\\Fonts";
        System.out.println(dir);


        //try {
            //URI uri = TextureLibrary.class.getResource("/Fonts").toURI();
            Path dirPath = Paths.get(dir);
            
            try (Stream<Path> paths = Files.list(dirPath)) {
                for (Path p : paths.collect(Collectors.toList())) {
                    fontLocations.add(p.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        //} catch (URISyntaxException ex) {
        //    Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        //}

        for (int i = 0; i < fontLocations.size(); i++) {
            String[] nameLoc = fontLocations.get(i).split("\\\\");
            String name = nameLoc[nameLoc.length - 1];

            fontNames.add(name);
            ImFont font = io.getFonts().addFontFromFileTTF(fontLocations.get(i), 20); 
            fonts.put(name, font);
        }

    }
    
    public static ImFont getFont(int i) {
        return fonts.get(fontNames.get(i));
    }
    
    public static ArrayList<String> getFontNames() {
        return fontNames;
    }
    
}

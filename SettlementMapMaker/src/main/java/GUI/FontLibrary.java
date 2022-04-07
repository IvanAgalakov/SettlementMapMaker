/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import imgui.ImFont;
import imgui.ImGuiIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ivan
 */
public class FontLibrary {
    private static ArrayList<String> fontLocations = new ArrayList<>();
    
    private static HashMap<String, ImFont> fonts = new HashMap<>();
    private static ArrayList<String> fontNames = new ArrayList<>();

    public static void loadAllFonts(ImGuiIO io) {
        try {
            URI uri = TextureLibrary.class.getResource("/Fonts").toURI();
            Path dirPath = Paths.get(uri);
            try {
                Files.list(dirPath)
                        .forEach(p -> fontLocations.add(p.toString()));
            } catch (IOException ex) {
                Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }

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

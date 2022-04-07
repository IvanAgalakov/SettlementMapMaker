/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ivan
 */
public class TextureLibrary {

    private static ArrayList<String> textureLocations = new ArrayList<>();
    private static HashMap<String, Texture> textures = new HashMap<>();

    public static void loadAllTextures() {
        try {
            URI uri = TextureLibrary.class.getResource("/Textures").toURI();
            Path dirPath = Paths.get(uri);
            try {
                Files.list(dirPath)
                        .forEach(p -> textureLocations.add(p.toString()));
            } catch (IOException ex) {
                Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(TextureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < textureLocations.size(); i++) {
            String[] nameLoc = textureLocations.get(i).split("\\\\");
            String name = nameLoc[nameLoc.length - 1];

            try {
                BufferedImage image = ImageIO.read(new File(textureLocations.get(i)));
                Texture tex = new Texture(((DataBufferByte) image.getRaster().getDataBuffer()).getData(), 0, image.getWidth(), image.getHeight(), image);
                textures.put(name, tex);
            } catch (Exception e) {
                System.err.println("Unsuccessful creation of texture");
            }
        }

    }
    
    public static int getTexture(String texture) {
        return textures.get(texture).texture;
    }
}

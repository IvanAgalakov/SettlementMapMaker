/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33C;

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

    public static void saveScreen(int width, int height) {
        int[] pixels = new int[width * height * 3];

        GL33C.glReadPixels(0, 0, width, height, GL33C.GL_RGB, GL33C.GL_UNSIGNED_BYTE, pixels);

        try {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            img.setRGB(0, 0, width, height, pixels, 0, width);
            if (img != null) {
                File outputFile = new File("D:\\image.jpg");
                ImageIO.write(img, "jpg", outputFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

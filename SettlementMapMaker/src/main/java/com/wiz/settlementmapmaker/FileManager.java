/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GsonExtras.RuntimeTypeAdapterFactory;
import Shapes.Building;
import Shapes.EditorShape;
import Shapes.Obstacle;
import Shapes.QuadBezierCurve;
import Shapes.Zone;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImColor;
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
import javax.swing.filechooser.FileSystemView;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33C;

/**
 *
 * @author 904187003
 */
public class FileManager {

    private static RuntimeTypeAdapterFactory<EditorShape> shapeAdaptorFactory = RuntimeTypeAdapterFactory.of(EditorShape.class, "EditorShape")
            .registerSubtype(Zone.class, "Zone")
            .registerSubtype(Building.class, "Building")
            .registerSubtype(Obstacle.class, "Obstacle")
            .registerSubtype(QuadBezierCurve.class, "QuadBezierCurve");

    public static void saveSettlement(Settlement settle, String path) {
        Gson settleGson = new GsonBuilder().registerTypeAdapterFactory(shapeAdaptorFactory).create();
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
            Gson settleGson = new GsonBuilder().registerTypeAdapterFactory(shapeAdaptorFactory).create();
            Settlement settle = settleGson.fromJson(save, Settlement.class);
            return settle;
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveScreen(int width, int height, String path) {
        float[] pixels = new float[width * height * 3];

        GL33C.glReadPixels(0, 0, width, height, GL33C.GL_RGB, GL33C.GL_FLOAT, pixels);

        try {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int x = width - 1;
            int y = 0;
            for (int i = pixels.length - 1; i > 2; i -= 3) {
                int col = ImColor.floatToColor(pixels[i], pixels[i - 1], pixels[i - 2]);
                img.setRGB(x, y, col);
                x--;
                if (x < 0) {
                    y++;
                    x = width - 1;
                }
            }

            if (img != null) {
                File outputFile = new File(path);
                ImageIO.write(img, "png", outputFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

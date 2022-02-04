/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30C;

/**
 *
 * @author 904187003
 */
public class Texture {

    private int texture = GL30C.GL_NONE;
    private int slot = GL30C.GL_TEXTURE0;
    private int intSlot = GL30C.GL_TEXTURE0 - GL30C.GL_TEXTURE0;

    public Texture(byte[] pixels, int mipMapLevel, int width, int height, BufferedImage b) {
        //bind();
        texture = GL30C.glGenTextures();
        bind();
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * Integer.BYTES);
        //buffer.put(pixels);
        //buffer.flip();
        //buffer.rewind();
        buffer = convertImage(b);

        GL30C.glTexImage2D(GL30C.GL_TEXTURE_2D, mipMapLevel, GL30C.GL_RGBA, width, height, 0, GL30C.GL_RGBA, GL30C.GL_UNSIGNED_BYTE, buffer);

        GL30C.glTexParameteri(GL30C.GL_TEXTURE_2D, GL30C.GL_TEXTURE_MIN_FILTER, GL30C.GL_NEAREST);
        //GL30C.glGenerateMipmap(GL30C.GL_TEXTURE_2D);
    }

    public void bind() {
        GL30C.glActiveTexture(slot);
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, texture);
    }

    public void unBind() {
        GL30C.glActiveTexture(slot);
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, GL30C.GL_NONE);
    }

    public static ByteBuffer convertImage(BufferedImage image) {
        System.out.println(image.getColorModel().toString());
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * Integer.BYTES);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        return buffer;
    }

}

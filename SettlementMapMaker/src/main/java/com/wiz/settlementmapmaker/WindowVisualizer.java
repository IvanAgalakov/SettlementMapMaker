/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.opengl.GL33C;

/**
 *
 * @author 904187003
 */
public class WindowVisualizer {

    private static int buffer;
    private static int vertexArray;
    
    private static float[] vert;

    public WindowVisualizer() {

    }

   
    public static void WindowVisualizerInit() {
    
        
        //GL33C.glVertexAttribPointer(1, 2, GL33C.GL_FLOAT, false, stride, 2 * Float.BYTES);

//        GL33C.glBindVertexArray(0);
//        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, 0);
//        GL33C.glUseProgram(0);
//        GL33C.glDisableVertexAttribArray(0);
//        GL33C.glDisableVertexAttribArray(1);
    }
    
    public static void drawEnclosedLines(Shape[] shapes, int lineWidth) {
        
        int amount = 0;
        for (int i = 0; i < shapes.length; i++) {
            amount += calculateVertices(shapes[i].getEnclosedLinesFromPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight()).length;

        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.length; i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes[i].getEnclosedLinesFromPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight()).length; i2++) {
                vert[count] = calculateVertices(shapes[i].getEnclosedLinesFromPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight())[i2];
                count++;
            }
        }
        
        
        
        
        vertexArray = GL33C.glGenVertexArrays();
        GL33C.glBindVertexArray(vertexArray);
        
        
        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();
        
        buffer = GL33C.glGenBuffers();
        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;

        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);
        
        
        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);
        
        GL33C.glLineWidth(lineWidth);
        GL33C.glDrawArrays(GL33C.GL_LINES, 0, amount/2);
        
        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }
    
    
    public static void drawShapes(Shape[] shapes) {
        
        int amount = 0;
        for (int i = 0; i < shapes.length; i++) {
            amount += calculateVertices(shapes[i].getPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight()).length;

        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.length; i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes[i].getPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight()).length; i2++) {
                vert[count] = calculateVertices(shapes[i].getPoints(), shapes[i].getBottomLeft(), shapes[i].getTopRight())[i2];
                count++;
            }
        }
        
        
        
        
        vertexArray = GL33C.glGenVertexArrays();
        GL33C.glBindVertexArray(vertexArray);
        
        
        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();
        
        buffer = GL33C.glGenBuffers();
        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;

        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);
        
        
        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);
        
        GL33C.glLineWidth(4);
        GL33C.glDrawArrays(GL33C.GL_LINE, 0, amount/2);
        
        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }

    public static void freeResources() {
        GL33C.glDeleteVertexArrays(vertexArray);
        GL33C.glDeleteBuffers(buffer);
    }

    public static float[] calculateVertices(Point[] points, Point bottomLeft, Point topRight) {
        float[] vert = new float[points.length * 2];
        int count = 0;
        for (int i = 0; i < points.length; i++) {
            vert[count] = points[i].x;
            vert[count + 1] = points[i].y;

            count += 2;
        }

        return vert;
    }

    public static float scaleBetween(float min, float max, float spot) {
        return (spot - min) / (max - min);
    }
}

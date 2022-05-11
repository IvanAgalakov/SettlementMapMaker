/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GUI.DrawColor;
import Shapes.EditorShape;
import Shapes.Point;
import imgui.app.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
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

    private static Window window;

    public WindowVisualizer() {

    }

    public static void WindowVisualizerInit(Window win) {

        window = win;

        vertexArray = GL33C.glGenVertexArrays(); // keep these up here and not running every fram, this creates a memory leak if this persistes
        buffer = GL33C.glGenBuffers();

//        GL33C.glEnable(GL33C.GL_LINE_SMOOTH);
//        GL33C.glEnable(GL33C.GL_POLYGON_SMOOTH);
//        GL33C.glHint(GL33C.GL_LINE_SMOOTH_HINT, GL33C.GL_NICEST);
//        GL33C.glHint(GL33C.GL_POLYGON_SMOOTH_HINT, GL33C.GL_NICEST);
//
//        GL33C.glEnable(GL33C.GL_BLEND);
//        GL33C.glBlendFunc(GL33C.GL_SRC_ALPHA, GL33C.GL_ONE_MINUS_SRC_ALPHA);
        GL33C.glBlendFunc(GL33C.GL_SRC_ALPHA, GL33C.GL_ONE);
        GL33C.glBlendFuncSeparate(GL33C.GL_SRC_ALPHA, GL33C.GL_ONE_MINUS_SRC_ALPHA, GL33C.GL_ONE, GL33C.GL_ONE_MINUS_SRC_ALPHA);

        GL33C.glDepthMask(false);

        //GL33C.glVertexAttribPointer(1, 2, GL33C.GL_FLOAT, false, stride, 2 * Float.BYTES);
//        GL33C.glBindVertexArray(0);
//        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, 0);
//        GL33C.glUseProgram(0);
//        GL33C.glDisableVertexAttribArray(0);
//        GL33C.glDisableVertexAttribArray(1);
    }

    public static void drawLines(ArrayList<EditorShape> shapes, float lineWidth, DrawColor color, boolean enclose) {

        GL33C.glUniform3f(GL33C.glGetUniformLocation(window.getProgram(), "col"), color.getRed(), color.getGreen(), color.getBlue());

        int amount = 0;
        for (int i = 0; i < shapes.size(); i++) {
            amount += calculateVertices(shapes.get(i).getLinesFromPoints(lineWidth,enclose)).length;

        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.size(); i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes.get(i).getLinesFromPoints(lineWidth, enclose)).length; i2++) {
                vert[count] = calculateVertices(shapes.get(i).getLinesFromPoints(lineWidth, enclose))[i2];
                count++;
            }
        }

        GL33C.glBindVertexArray(vertexArray);

        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();

        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;
        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);

        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);

        GL33C.glLineWidth(lineWidth);
        GL33C.glDrawArrays(GL33C.GL_TRIANGLES, 0, amount / 2);

        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }
    
    public static void drawDottedLines(ArrayList<EditorShape> shapes, float lineWidth, DrawColor color, boolean enclose) {

        float dev = 0.01f;
        
        GL33C.glUniform3f(GL33C.glGetUniformLocation(window.getProgram(), "col"), color.getRed(), color.getGreen(), color.getBlue());

        int amount = 0;
        for (int i = 0; i < shapes.size(); i++) {
            amount += calculateVertices(shapes.get(i).getDottedLinesFromPoints(lineWidth,enclose)).length;

        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.size(); i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes.get(i).getDottedLinesFromPoints(lineWidth, enclose)).length; i2++) {
                vert[count] = calculateVertices(shapes.get(i).getDottedLinesFromPoints(lineWidth, enclose))[i2];
                count++;
            }
        }

        GL33C.glBindVertexArray(vertexArray);

        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();

        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;
        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);

        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);

        GL33C.glLineWidth(lineWidth);
        GL33C.glDrawArrays(GL33C.GL_TRIANGLES, 0, amount / 2);

        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }
    
    public static void drawGlLines(ArrayList<EditorShape> shapes, float lineWidth, DrawColor color, boolean enclose) {

        GL33C.glUniform3f(GL33C.glGetUniformLocation(window.getProgram(), "col"), color.getRed(), color.getGreen(), color.getBlue());

        int amount = 0;
        for (int i = 0; i < shapes.size(); i++) {
            amount += calculateVertices(shapes.get(i).getGlLines(enclose)).length;

        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.size(); i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes.get(i).getGlLines(enclose)).length; i2++) {
                vert[count] = calculateVertices(shapes.get(i).getGlLines(enclose))[i2];
                count++;
            }
        }

        GL33C.glBindVertexArray(vertexArray);

        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();

        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;
        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);

        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);

        GL33C.glLineWidth(lineWidth);
        GL33C.glDrawArrays(GL33C.GL_LINES, 0, amount / 2);

        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }

    public static void drawTriangles(ArrayList<EditorShape> shapes, DrawColor color) {

        GL33C.glUniform3f(GL33C.glGetUniformLocation(window.getProgram(), "col"), color.getRed(), color.getGreen(), color.getBlue());

        int amount = 0;
        for (int i = 0; i < shapes.size(); i++) {
            amount += calculateVertices(shapes.get(i).getTrianglesFromPoints()).length;
        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.size(); i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes.get(i).getTrianglesFromPoints()).length; i2++) {
                vert[count] = calculateVertices(shapes.get(i).getTrianglesFromPoints())[i2];
                count++;
            }
        }

        GL33C.glBindVertexArray(vertexArray);

        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();

        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;
        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);

        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);

        GL33C.glDrawArrays(GL33C.GL_TRIANGLES, 0, amount / 2);

        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }

    public static void drawPoints(ArrayList<EditorShape> shapes, float pointSize, DrawColor color) {
        //System.out.println(color.toString());
        GL33C.glUniform3f(GL33C.glGetUniformLocation(window.getProgram(), "col"), color.getRed(), color.getGreen(), color.getBlue());

        int amount = 0;
        for (int i = 0; i < shapes.size(); i++) {
            amount += calculateVertices(shapes.get(i).getPoints()).length;
        }

        vert = new float[amount];
        int count = 0;
        for (int i = 0; i < shapes.size(); i++) {
            for (int i2 = 0; i2 < calculateVertices(shapes.get(i).getPoints()).length; i2++) {
                vert[count] = calculateVertices(shapes.get(i).getPoints())[i2];
                count++;
            }
        }

        GL33C.glBindVertexArray(vertexArray);

        //vert = new float[]{-1,0,1,0};
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vert.length);
        verticesBuffer.put(vert).flip();

        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, buffer);
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, verticesBuffer, GL33C.GL_STATIC_DRAW);

        //int stride = 4 * Float.BYTES;
        GL33C.glVertexAttribPointer(0, 2, GL33C.GL_FLOAT, false, 0, 0);
        GL33C.glBindVertexArray(0);

        GL30C.glBindVertexArray(vertexArray);
        GL33C.glEnableVertexAttribArray(0);

        GL33C.glPointSize(pointSize);

        GL33C.glDrawArrays(GL33C.GL_POINTS, 0, amount / 2);

        GL33C.glDisableVertexAttribArray(0);
        GL33C.glBindVertexArray(0);
    }

    public static void freeResources() {
        GL33C.glDeleteVertexArrays(vertexArray);
        GL33C.glDeleteBuffers(buffer);
    }

    public static float[] calculateVertices(Point[] points) {
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

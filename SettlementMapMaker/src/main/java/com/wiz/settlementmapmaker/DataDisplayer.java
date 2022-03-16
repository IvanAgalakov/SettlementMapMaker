/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Point;
import Shape.Shape;
import imgui.ImGuiIO;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33C;

/**
 *
 * @author Ivan
 */
public class DataDisplayer {

    private boolean lastMiddleState = false;
    private float initMouseX = 0f, initMouseY = 0f;
    private float initCameraX = 0f, initCameraY = 0f;
    private float cameraX = 0f, cameraY = 0f;

    private float realMouseX = 0f;
    private float realMouseY = 0f;

    private Point editPoint = null;
    private EditorShape editShape = null;

    private ImGuiIO io;
    private RuntimeManager runMan;
    private Window window;

    private HashMap<String, ArrayList<Shape>> shapesByStyle = new HashMap<>();

    public DataDisplayer(RuntimeManager runMan, ImGuiIO io, Window window) {
        this.runMan = runMan;
        this.io = io;
        this.window = window;
    }

    public void display() {
        GL33C.glUseProgram(window.getProgram());

        if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && io.getKeyCtrl()) {

            if (lastMiddleState == false) {
                initMouseX = io.getMousePosX();
                initMouseY = io.getMousePosY();
                initCameraX = cameraX;
                initCameraY = cameraY;
            }
            cameraX = initCameraX + (io.getMousePosX() - initMouseX);
            cameraY = initCameraY + (io.getMousePosY() - initMouseY);
            lastMiddleState = true;
        } else {
            lastMiddleState = false;
        }

        float normx = cameraX / runMan.getWidth(),
                normy = 1 - cameraY / (float) runMan.getHeight();
        GL33C.glUniform2f(GL33C.glGetUniformLocation(window.getProgram(), "offset"), normx * 2 - 1, normy * 2 - 1);

        realMouseX = (((io.getMousePosX() / runMan.getWidth()) * 2) - (normx * 2));
        realMouseY = -(((io.getMousePosY() / runMan.getHeight()) * 2) + (normy * 2 - 2));

        //System.out.println(realMouseX + ", " + realMouseY);
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(realMouseX,realMouseY), new Point(0,-2)})}, new Color(realMouseX/2f,0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(realMouseX,realMouseY), new Point(2,0)})}, new Color(-realMouseY/2f,0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,-2), new Point(realMouseX,realMouseY), new Point(2,-2)})}, new Color(1+(realMouseY/2f),0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(2,-2), new Point(realMouseX,realMouseY), new Point(2,0)})}, new Color(1-(realMouseX/2f),0f,0f,1f));
        if (editPoint != null) {
            editPoint.setX(realMouseX);
            editPoint.setY(realMouseY);

            WindowVisualizer.drawPoints(new Shape[]{new Shape(new Point[]{editPoint})}, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !io.getKeyCtrl()) {
                editPoint = null;
                editShape = null;
            }
            if (editShape != null) {
                WindowVisualizer.drawPoints(new Shape[]{editShape}, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            }
        }

        if (runMan.getCurrentSettlement() != null) {
            drawStyleGroups();
        }
    }

    public void drawStyleGroups() {
        String[] styles = runMan.getStyles();
        for (int i = 0; i < styles.length; i++) {
            if (this.shapesByStyle.containsKey(styles[i])) {
                Shape[] shapes = new Shape[this.shapesByStyle.get(styles[i]).size()];
                shapes = this.shapesByStyle.get(styles[i]).toArray(shapes);
                WindowVisualizer.drawEnclosedLines(shapes, 5, runMan.getCurrentSettlement().getStyle(styles[i]).getColor());
            }

        }
    }

    public void updateShapeStyleGroupings() {
        this.shapesByStyle = new HashMap<>();
        String[] styles = runMan.getStyles();
        System.out.println(styles[0]);
        for (int i = 0; i < styles.length; i++) {
            shapesByStyle.put(styles[i], new ArrayList<>());
        }

        EditorShape[] shapes = runMan.currentSettlement.getRawEditorShapes();

        for (int i = 0; i < shapes.length; i++) {
            this.shapesByStyle.get(styles[shapes[i].getStyle().get()]).add(shapes[i]);
        }
    }

    public void setEditPoint(Point editPoint) {
        this.editPoint = editPoint;
    }

    public void setEditShape(EditorShape shape) {
        this.editShape = shape;
    }

}

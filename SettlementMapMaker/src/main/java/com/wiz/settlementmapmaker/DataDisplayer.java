/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Point;
import Shape.Shape;
import Shape.Zone;
import imgui.ImGuiIO;
import java.util.ArrayList;
import java.util.Arrays;
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

    private SettlementGenerator settleGen;

    public DataDisplayer(RuntimeManager runMan, ImGuiIO io, Window window) {
        this.runMan = runMan;
        this.io = io;
        this.window = window;
        this.settleGen = new SettlementGenerator();
    }

    private boolean editMode = false;

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

        float normx = cameraX / (float) runMan.getWidth(),
                normy = 1 - cameraY / (float) runMan.getHeight();
        GL33C.glUniform2f(GL33C.glGetUniformLocation(window.getProgram(), "offset"), normx * 2 - 1, normy * 2 - 1);

        GL33C.glUniform1f(GL33C.glGetUniformLocation(window.getProgram(), "zoom"), runMan.getZoom()[0]);

        realMouseX = -(-1 + 1f / runMan.getZoom()[0]) + (((io.getMousePosX() / runMan.getWidth()) * 2) / runMan.getZoom()[0] - (normx * 2));
        realMouseY = (-1 + 1f / runMan.getZoom()[0]) - (((io.getMousePosY() / runMan.getHeight()) * 2) / runMan.getZoom()[0] + (normy * 2 - 2));

        if (editPoint != null) {
            editMode = true;
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
        } else if (editMode == true) {
            updateShapeStyleGroupings();
            editMode = false;
        }

        if (runMan.getCurrentSettlement() != null) {
            drawStyleGroups();
        }
    }

    public void drawStyleGroups() {
        //WindowVisualizer.drawEnclosedLines(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(1,0)})}, 5, new DrawColor(0,0,0,0));
        String[] styles = runMan.getStyles();
        for (int i = 0; i < styles.length; i++) {
            if (this.shapesByStyle.containsKey(styles[i])) {

                ArrayList shapeList = this.shapesByStyle.get(styles[i]);

                Shape[] shapes = new Shape[shapeList.size()];
                shapes = this.shapesByStyle.get(styles[i]).toArray(shapes);
                
                
                // chooses the drawing type based on the style you have selected
                Style style = runMan.getStyle(styles[i]);
                if(Style.styleTypes[(style.getSelectedStyle().get())].equals("point")) {
                    WindowVisualizer.drawPoints(shapes, 8, style.getColor());
                }
                else if (Style.styleTypes[(style.getSelectedStyle().get())].equals("solid")) {
                    WindowVisualizer.drawTriangles(shapes, style.getColor());
                }
                else {
                    WindowVisualizer.drawEnclosedLines(shapes, 5, style.getColor());
                }
                
            }

        }
    }

    // updates the style groups to be rendered, this changes when a change is made to a shape such as a when a new shape is made, and a style is changed
    // optimize later (:
    // make a EditorShape that holds the shape to be updated, to just update that shape and no other shapes to be effected.
    public void updateShapeStyleGroupings() {
        this.shapesByStyle = new HashMap<>();
        String[] styles = runMan.getStyles();
        for (int i = 0; i < styles.length; i++) {
            shapesByStyle.put(styles[i], new ArrayList<>());
        }

        ArrayList<EditorShape> shapes = runMan.currentSettlement.getRawEditorShapes();

        for (int x = 0; x < shapes.size(); x++) {
            ArrayList currentStyleShapes = new ArrayList<Shape>();

            currentStyleShapes.add(shapes.get(x));

            if (shapes.get(x) instanceof Zone) {
                Zone zone = (Zone) shapes.get(x);
                if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings")) {
                    //currentStyleShapes.remove(shapes.get(x));
                    //currentStyleShapes.addAll(settleGen.generateVoronoi(zone));
                    //currentStyleShapes.addAll(Arrays.asList(settleGen.convertToBlock(zone, 0.01f, 0.1f)));
                }
            }

            this.shapesByStyle.get(styles[shapes.get(x).getStyle().get()]).addAll(currentStyleShapes);
        }

    }

    public void setEditPoint(Point editPoint) {
        this.editPoint = editPoint;
    }

    public void setEditShape(EditorShape shape) {
        this.editShape = shape;
    }

}

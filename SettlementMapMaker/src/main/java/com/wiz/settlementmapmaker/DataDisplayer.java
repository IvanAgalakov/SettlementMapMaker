/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GUI.Style;
import GUI.GUILayer;
import Shapes.EditorShape;
import Shapes.Point;
import Shapes.Zone;
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

    private HashMap<String, ArrayList<EditorShape>> shapesByStyle = new HashMap<>();

    private SettlementGenerator settleGen;

    private GUILayer gui;

    public DataDisplayer(RuntimeManager runMan, ImGuiIO io, Window window, GUILayer gui) {
        this.runMan = runMan;
        this.io = io;
        this.window = window;
        this.gui = gui;
        this.settleGen = new SettlementGenerator();
    }

    private boolean editMode = false;

    private float normx;
    private float normy;

    private float aspect;

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

        if (runMan.savePlease == 0) {
            normx = cameraX / (float) runMan.getWidth();
            normy = 1 - cameraY / (float) runMan.getHeight();
        } else {
            normx = cameraX / (float) runMan.getImageResX();
            normy = 1 - cameraY / (float) runMan.getImageResY();
        }
        GL33C.glUniform2f(GL33C.glGetUniformLocation(window.getProgram(), "offset"), normx * 2 - 1, normy * 2 - 1);

        GL33C.glUniform1f(GL33C.glGetUniformLocation(window.getProgram(), "zoom"), runMan.getZoom()[0]);

        if (runMan.savePlease == 0) {
            aspect = runMan.getWidth() / (float) runMan.getHeight();
        } else {
            aspect = runMan.getImageResX() / (float) runMan.getImageResY();
        }
        GL33C.glUniform1f(GL33C.glGetUniformLocation(window.getProgram(), "aspectX"), aspect);

        Point mouse = this.screenPointToWorldPoint(new Point(io.getMousePosX(), io.getMousePosY()), runMan.getWidth(), runMan.getHeight());
        realMouseX = mouse.x;
        realMouseY = mouse.y;
        //Point p = this.worldPointToScreenPoint(new Point(1, -1));
        //System.out.println(io.getMousePos() + " : " + p.toString());
        //gui.textPopup("test", p.x, p.y);

        if (editPoint != null) {
            editMode = true;
            editPoint.setX(realMouseX);
            editPoint.setY(realMouseY);

            WindowVisualizer.drawPoints(new EditorShape[]{new EditorShape(new Point[]{editPoint})}, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !io.getKeyCtrl()) {
                editShape.CalculateCenter();
                editPoint = null;
                editShape = null;
            }
            if (editShape != null) {
                WindowVisualizer.drawPoints(new EditorShape[]{editShape}, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            }
        } else if (editMode == true) {
            updateShapeStyleGroupings();
            editMode = false;
        }

        if (runMan.getCurrentSettlement() != null) {
            drawStyleGroups();
        }
    }

    private Point screenPointToWorldPoint(Point screen, int width, int height) {
        Point world = new Point(-(-1 + 1f / runMan.getZoom()[0]) + (((screen.x / width) * 2f) / runMan.getZoom()[0] - (normx * 2)),
                (-1 + 1f / runMan.getZoom()[0]) / aspect - (((screen.y / height) * 2f) / aspect / runMan.getZoom()[0] + (normy * 2 - 2) / aspect));
        return world;
    }

    private Point worldPointToScreenPoint(Point world, int width, int height) {
        Point screen = new Point((((world.x + (-1 + 1f / runMan.getZoom()[0]) + (normx * 2)) * runMan.getZoom()[0]) / 2f) * width,
                -((((world.y - (-1 + 1f / runMan.getZoom()[0]) / aspect + (normy * 2 - 2) / aspect) * runMan.getZoom()[0]) * aspect / 2f) * height));
        return screen;
    }

    public void drawStyleGroups() {

        //WindowVisualizer.drawEnclosedLines(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(1,0)})}, 5, new DrawColor(0,0,0,0));
        String[] styles = runMan.getStyles();
        for (int i = 0; i < styles.length; i++) {
            if (this.shapesByStyle.containsKey(styles[i])) {

                ArrayList shapeList = this.shapesByStyle.get(styles[i]);

                EditorShape[] shapes = new EditorShape[shapeList.size()];
                shapes = this.shapesByStyle.get(styles[i]).toArray(shapes);

                // chooses the drawing type based on the style you have selected
                Style style = runMan.getStyle(styles[i]);
                if (Style.styleTypes[(style.getSelectedStyle().get())].equals("point")) {
                    WindowVisualizer.drawPoints(shapes, 8, style.getColor());
                } else if (Style.styleTypes[(style.getSelectedStyle().get())].equals("solid")) {
                    WindowVisualizer.drawTriangles(shapes, style.getColor());
                } else {
                    WindowVisualizer.drawEnclosedLines(shapes, 5, style.getColor());
                }

                for (int x = 0; x < shapes.length; x++) {
                    if (shapes[x].getShowLabel().get() && !shapes[x].getName().get().equals("")) {
                        if (runMan.savePlease == 0) {
                            Point textPoint = this.worldPointToScreenPoint(shapes[x].getCenter(), runMan.getWidth(), runMan.getHeight());
                            gui.textPopup(shapes[x].getName().get(), textPoint.x, textPoint.y, i + x + 1);
                        } else {
                            Point textPoint = this.worldPointToScreenPoint(shapes[x].getCenter(), runMan.getImageResX(), runMan.getImageResY());
                            //System.out.println(textPoint);
                            gui.textPopup(shapes[x].getName().get(), textPoint.x, textPoint.y, i + x + 1);
                        }
                    }
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
            ArrayList currentStyleShapes = new ArrayList<EditorShape>();

            currentStyleShapes.add(shapes.get(x));

            if (shapes.get(x) instanceof Zone) {
                Zone zone = (Zone) shapes.get(x);
                if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings")) {
                    currentStyleShapes.remove(shapes.get(x));
                    //currentStyleShapes.addAll(settleGen.generateVoronoi(zone));
                    if (!zone.getPointList().isEmpty()) {
                        //currentStyleShapes.addAll(Arrays.asList(settleGen.convertToBlock(zone, 0.01f, 0.1f)));
                        currentStyleShapes.addAll(settleGen.generateSettlementBlock(zone, 0.01f, 0.02f));
                    }
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

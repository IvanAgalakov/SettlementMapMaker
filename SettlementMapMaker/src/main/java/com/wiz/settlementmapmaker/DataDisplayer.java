/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GUI.DrawColor;
import GUI.Style;
import GUI.GUILayer;
import Shapes.EditorShape;
import Shapes.Line;
import Shapes.Obstacle;
import Shapes.Point;
import Shapes.QuadBezierCurve;
import Shapes.River;
import Shapes.Zone;
import com.wiz.settlementmapmaker.Utilities.Utils;
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
    
    private ArrayList<EditorShape> editingShapes = new ArrayList<>();

    private ImGuiIO io;
    private RuntimeManager runMan;
    private Window window;

    private HashMap<String, ArrayList<EditorShape>> shapesByStyle = new HashMap<>();

    private GUILayer gui;

    public DataDisplayer(RuntimeManager runMan, ImGuiIO io, Window window, GUILayer gui) {
        this.runMan = runMan;
        this.io = io;
        this.window = window;
        this.gui = gui;
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

            ArrayList<EditorShape> v = new ArrayList<>();
            v.add(new EditorShape(new Point[]{editPoint}));

            WindowVisualizer.drawPoints(v, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            if (runMan.getLeftClickState() && !io.getKeyCtrl() && !runMan.imGuiWantCaptureMouse()) {
                editShape.CalculateCenter();
                editPoint = null;
                editShape = null;
            }
            if (editShape != null) {
                v.clear();
                v.add(editShape);
                WindowVisualizer.drawLines(v, 5, runMan.getCurrentSettlement().getEditStyle().getColor(), true);
            }
        } else if (editMode == true) {
            updateShapeStyleGroupings();
            editMode = false;
        }

        if (runMan.getCurrentSettlement() != null) {
            drawStyleGroups();
        }
        
//        ArrayList<Point> bezier = new ArrayList();
//        int divisions = 10;
//        Point start = new Point(0.1f, -1f);
//        
////        for (float i = 0; i < 1.0; i += 1f/divisions) {
////            bezier.add(Utils.quadraticBezier(start, control, mouse, i));
////        }
////        bezier.add(mouse);
//        River curve = new River(new Line(start, mouse), 20, 0.02f,0.07f, 0.02f);
//        
//        WindowVisualizer.drawTriangles(curve.getCurves(), DrawColor.BLACK);
        
    }

    public Point screenPointToWorldPoint(Point screen, int width, int height) {
        Point world = new Point(-(-1 + 1f / runMan.getZoom()[0]) + (((screen.x / width) * 2f) / runMan.getZoom()[0] - (normx * 2)),
                (-1 + 1f / runMan.getZoom()[0]) / aspect - (((screen.y / height) * 2f) / aspect / runMan.getZoom()[0] + (normy * 2 - 2) / aspect));
        return world;
    }

    public Point worldPointToScreenPoint(Point world, int width, int height) {
        Point screen = new Point((((world.x + (-1 + 1f / runMan.getZoom()[0]) + (normx * 2)) * runMan.getZoom()[0]) / 2f) * width,
                -((((world.y - (-1 + 1f / runMan.getZoom()[0]) / aspect + (normy * 2 - 2) / aspect) * runMan.getZoom()[0]) * aspect / 2f) * height));
        return screen;
    }

    public void drawStyleGroups() {

        //WindowVisualizer.drawEnclosedLines(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(1,0)})}, 5, new DrawColor(0,0,0,0));
        String[] st = runMan.getStyles();
        ArrayList<String> styles = new ArrayList();
        for (String s : st) {
            styles.add(s);
        }
        styles.add("water");
        
        
        for (int i = 0; i < styles.size(); i++) {
            if (this.shapesByStyle.containsKey(styles.get(i))) {

                ArrayList<EditorShape> shapeList = new ArrayList(this.shapesByStyle.get(styles.get(i)));

                for (int x = 0; x < shapeList.size(); x++) {
                    if (shapeList.get(x) instanceof Zone) {
                        Zone zone = (Zone) shapeList.get(x);
                        
                        //System.out.println(zone.getPointList().size() + " - " + Constants.ZONE_TYPES[zone.getZoneType().get()]);
                        if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings")) {
                            shapeList.remove(shapeList.get(x));
                            x--;
                            shapeList.addAll(zone.getContainedShapes());
                        }
                    }
                    if (shapeList.get(x) instanceof Obstacle) {
                        Obstacle obs = (Obstacle) shapeList.get(x);
                        
                        if (Constants.OBSTACLE_TYPES[obs.getObstacleType().get()].equals("River")) {
                            ArrayList<River> rivers = new ArrayList();
                            ArrayList<Line> lines = obs.getLines(false);
                            for (int a = 0; a < lines.size(); a++) {
                                rivers.add(new River(lines.get(a), 4,0.02f,0.03f,0.01f));
                            }
                            if (!rivers.isEmpty()) {
                                shapeList.remove(shapeList.get(x));
                                x--;
                                for (int a = 0; a < rivers.size(); a++) {
                                    shapeList.addAll(rivers.get(a).getCurves());
                                }
                            }
                        }
                    }
                }

                // chooses the drawing type based on the style you have selected
                Style style = runMan.getStyle(styles.get(i));
                if (Style.styleTypes[(style.getSelectedStyle().get())].equals("point")) {
                    WindowVisualizer.drawPoints(shapeList, 8, style.getColor());
                } else if (Style.styleTypes[(style.getSelectedStyle().get())].equals("solid")) {
                    WindowVisualizer.drawTriangles(shapeList, style.getColor());
                } else {
                    WindowVisualizer.drawLines(shapeList, 5, style.getColor(), true);
                }

                for (int x = 0; x < shapeList.size(); x++) {
                    if (shapeList.get(x).getShowLabel().get() && !shapeList.get(x).getName().get().equals("")) {
                        if (runMan.savePlease == 0) {
                            Point textPoint = this.worldPointToScreenPoint(shapeList.get(x).getCenter(), runMan.getWidth(), runMan.getHeight());
                            gui.textPopup(shapeList.get(x).getName().get(), textPoint.x, textPoint.y, i + x + 1);
                        } else {
                            Point textPoint = this.worldPointToScreenPoint(shapeList.get(x).getCenter(), runMan.getImageResX(), runMan.getImageResY());
                            //System.out.println(textPoint);
                            gui.textPopup(shapeList.get(x).getName().get(), textPoint.x, textPoint.y, i + x + 1);
                        }
                    }
                }

            }

        }
        
        
        WindowVisualizer.drawLines(editingShapes, 5, runMan.getEditStyle().getColor(), true);
        
//        ArrayList<EditorShape> mousePoint = new ArrayList();
//        mousePoint.add(new EditorShape(runMan.getMouseWorldPoint()));
//        WindowVisualizer.drawPoints(mousePoint, 10, runMan.getEditStyle().getColor());
        
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
        shapesByStyle.put("water", new ArrayList<>());

        ArrayList<EditorShape> shapes = runMan.currentSettlement.getRawEditorShapes();

        for (int x = 0; x < shapes.size(); x++) {
            ArrayList currentStyleShapes = new ArrayList<EditorShape>();

            if (shapes.get(x) instanceof Obstacle obs) {
                if (Constants.OBSTACLE_TYPES[obs.getObstacleType().get()].equals("River")) {
                    this.shapesByStyle.get("water").add(shapes.get(x));
                }
                continue;
            }
            
            currentStyleShapes.add(shapes.get(x));

//            if (shapes.get(x) instanceof Zone) {
//                Zone zone = (Zone) shapes.get(x);
//                if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings")) {
//                    currentStyleShapes.remove(shapes.get(x));
//                    //currentStyleShapes.addAll(settleGen.generateVoronoi(zone));
//                    if (!zone.getPointList().isEmpty()) {
//                        //currentStyleShapes.addAll(Arrays.asList(settleGen.convertToBlock(zone, 0.01f, 0.1f)));
//                        //currentStyleShapes.addAll(settleGen.generateSettlementBlock(zone, 0.01f, 0.02f));
//                        currentStyleShapes.addAll(zone.getContainedShapes());
//                    }
//                }
//            }
            this.shapesByStyle.get(styles[shapes.get(x).getStyle().get()]).addAll(currentStyleShapes);
        }

    }

    public void setEditPoint(Point editPoint) {
        this.editPoint = editPoint;
    }

    public void setEditShape(EditorShape shape) {
        this.editShape = shape;
    }
    
    public void addEditingShape(EditorShape shape) {
        this.editingShapes.add(shape);
    }
    
    public void removeEditingShape(EditorShape shape) {
        this.editingShapes.remove(shape);
    }
    
    public void clearEditingShapes() {
        this.editingShapes.clear();
    }
    
    public boolean containsEditingShape(EditorShape shape) {
        return this.editingShapes.contains(shape);
    }

}

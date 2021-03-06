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
import Shapes.Wall;
import Shapes.Zone;
import com.wiz.settlementmapmaker.Utilities.Utils;
import imgui.ImGuiIO;
import imgui.type.ImBoolean;
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

    private double realMouseX = 0f;
    private double realMouseY = 0f;

    private Point editPoint = null;
    private EditorShape editShape = null;
    private boolean moveShape = false;

    private ArrayList<EditorShape> editingShapes = new ArrayList<>();
    private ArrayList<Point> editingPoints = new ArrayList<>();

    private ImGuiIO io;
    private RuntimeManager runMan;
    private Window window;

    private HashMap<String, ArrayList<EditorShape>> shapesByStyle = new HashMap<>();

    private HashMap<Obstacle, ArrayList<EditorShape>> obstacles = new HashMap<>();
    private HashMap<Obstacle, ImBoolean> updateObstacle = new HashMap<>();

    private GUILayer gui;
    
    private ArrayList<EditorShape> blockingShapes = new ArrayList<>();

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

    private int selectedProgram = GL33C.GL_NONE;

    public void display() {
        
        blockingShapes.clear();

        //System.out.println("Camera: " + this.cameraX + ", " + this.cameraY);
        //GL33C.glUseProgram(ShaderManager.getProgram(ShaderManager.ProgramNames.DEFAULT));
        this.selectProgram(ShaderManager.ProgramNames.DEFAULT);

        if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && io.getKeyCtrl()) {

            if (lastMiddleState == false) {
                initMouseX = io.getMousePosX();
                initMouseY = io.getMousePosY();
                initCameraX = cameraX;
                initCameraY = cameraY;
            }
            cameraX = initCameraX + (io.getMousePosX() - initMouseX) / runMan.getZoom()[0];
            cameraY = initCameraY + (io.getMousePosY() - initMouseY) / runMan.getZoom()[0];
            lastMiddleState = true;
        } else {
            lastMiddleState = false;
        }

        updateCalculations();

        ArrayList<Integer> allPrograms = ShaderManager.getAllPrograms();
        for (int i = 0; i < allPrograms.size(); i++) {
            this.selectProgram(allPrograms.get(i));
            GL33C.glUniform2f(GL33C.glGetUniformLocation(allPrograms.get(i), "offset"), normx * 2 - 1, normy * 2 - 1);
            GL33C.glUniform1f(GL33C.glGetUniformLocation(allPrograms.get(i), "zoom"), runMan.getZoom()[0]);
            GL33C.glUniform1f(GL33C.glGetUniformLocation(allPrograms.get(i), "aspect"), aspect);
            GL33C.glUniform1f(GL33C.glGetUniformLocation(allPrograms.get(i), "iTime"), (System.currentTimeMillis() - runMan.getStartTime()) / 1000f);
            GL33C.glUniform2f(GL33C.glGetUniformLocation(allPrograms.get(i), "windowSize"), (float) runMan.getWidth(), (float) runMan.getHeight());
            GL33C.glUniform1f(GL33C.glGetUniformLocation(allPrograms.get(i), "normX"), normx);
            GL33C.glUniform1f(GL33C.glGetUniformLocation(allPrograms.get(i), "normY"), normy);
        }

        Point mouse = this.screenPointToWorldPoint(new Point(io.getMousePosX(), io.getMousePosY()), runMan.getWidth(), runMan.getHeight());
        realMouseX = mouse.x;
        realMouseY = mouse.y;
        //Point p = this.worldPointToScreenPoint(new Point(1, -1));
        //System.out.println(io.getMousePos() + " : " + p.toString());
        //gui.textPopup("test", p.x, p.y);

        this.selectProgram(ShaderManager.ProgramNames.DEFAULT);
        if (editPoint != null) {
            editMode = true;
            editPoint.setX(realMouseX);
            editPoint.setY(realMouseY);

            ArrayList<EditorShape> v = new ArrayList<>();
            v.add(new EditorShape(new Point[]{editPoint}));
            v.get(0).calculatePointsAsPoints();
            this.setColor(runMan.getCurrentSettlement().getDefaultStyle().getColor());
            WindowVisualizer.drawPoints(v, 5, runMan.getCurrentSettlement().getDefaultStyle().getColor());
            if (runMan.getLeftClickState() && !io.getKeyCtrl() && !runMan.imGuiWantCaptureMouse()) {
                editShape.CalculateCenter();
                runMan.updateShape(editShape);

                if (!runMan.isSettingCamera() || editShape.size() >= 2) {
                    if (editShape instanceof Zone zone && moveShape) {
                        zone.finishTranslation();
                    }
                    editPoint = null;
                    editShape = null;
                    moveShape = false;

                } else {
                    Point p = new Point(mouse.x, mouse.y);
                    editShape.addPoints(p);
                    editPoint = p;
                }
            }
            if (editShape != null) {
                v.clear();
                // for moving shapes around
                if (this.moveShape) {
                    editShape.moveCenterTo(editPoint);
                }
                if (!runMan.isSettingCamera()) {
                    v.add(editShape);
                } else {
                    v.add(Utils.boxPoints(editShape));
                }
                v.get(0).calculateGlLines(true);
                this.setColor(runMan.getCurrentSettlement().getEditStyle().getColor());
                WindowVisualizer.drawGlLines(v, 6, runMan.getCurrentSettlement().getEditStyle().getColor(), true);
                if (editShape instanceof Obstacle obs) {
                    runMan.updateObstacle(obs);
                }
            }
        } else if (editMode == true) {
            //updateShapeStyleGroupings();
            editMode = false;
        }

        if (runMan.getCurrentSettlement() != null) {
            drawStyleGroups();
        }

//        ArrayList<EditorShape> shapes = new ArrayList();
//        EditorShape p = new EditorShape(new Point(0,0), new Point(2,-1));
//        p.calculatePointsAsPoints();
//        shapes.add(p);
//        WindowVisualizer.drawPoints(shapes, 100, DrawColor.BLACK);
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

    public void updateCalculations() {
        if (runMan.savePlease == 0) {
            normx = cameraX / (float) runMan.getWidth();
            normy = 1 - cameraY / (float) runMan.getHeight();
        } else {
            normx = cameraX / (float) runMan.getImageResX();
            normy = 1 - cameraY / (float) runMan.getImageResY();
        }

        if (runMan.savePlease == 0) {
            aspect = runMan.getWidth() / (float) runMan.getHeight();
        } else {
            aspect = runMan.getImageResX() / (float) runMan.getImageResY();
        }
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
                    if (shapeList.get(x) instanceof Zone zone) {
                        //System.out.println(zone.getPointList().size() + " - " + Constants.ZONE_TYPES[zone.getZoneType().get()]);
                        if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings") || Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate City")) {
                            shapeList.remove(shapeList.get(x));
                            x--;
                            shapeList.addAll(zone.getContainedShapes());
                        } else if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Block Generation")) {
                            this.blockingShapes.add(shapeList.get(x));
                            shapeList.remove(shapeList.get(x));
                            x--;
                        }
                    } else if (shapeList.get(x) instanceof Obstacle obs) {

                        if (this.updateObstacle.containsKey(obs)) {

                            if (this.updateObstacle.get(obs).get()) {
                                if (Constants.OBSTACLE_TYPES[obs.getObstacleType().get()].equals("River")) {
                                    ArrayList<River> rivers = new ArrayList();
                                    ArrayList<Line> lines = obs.getLines(false);
                                    for (int a = 0; a < lines.size(); a++) {
                                        River previous = null;
                                        if (!rivers.isEmpty()) {
                                            previous = rivers.get(rivers.size() - 1);
                                        }
                                        rivers.add(new River(lines.get(a), obs, previous));
                                    }
                                    if (!rivers.isEmpty()) {
                                        shapeList.remove(shapeList.get(x));
                                        x--;
                                        ArrayList<EditorShape> drawData = new ArrayList();
                                        for (int a = 0; a < rivers.size(); a++) {
                                            drawData.addAll(rivers.get(a).getCurves());
                                        }

                                        shapeList.addAll(drawData);
                                        this.obstacles.put(obs, drawData);
                                        this.updateObstacle.get(obs).set(false);

                                    }
                                }
                                
                                System.out.println(obs.getName().get());
                                if (Constants.OBSTACLE_TYPES[obs.getObstacleType().get()].equals("Wall")) {
                                    //System.out.println("updating" + System.currentTimeMillis());
                                    //System.out.println("runs walls");
                                    ArrayList<Wall> walls = new ArrayList();
                                    ArrayList<Line> lines = obs.getLines(false);
                                    for (int a = 0; a < lines.size(); a++) {
                                        Wall previous = null;
                                        if (!walls.isEmpty()) {
                                            previous = walls.get(walls.size() - 1);
                                        }
                                        walls.add(new Wall(lines.get(a), obs, previous));
                                    }
                                    if (!walls.isEmpty()) {
                                        shapeList.remove(shapeList.get(x));
                                        x--;
                                        ArrayList<EditorShape> drawData = new ArrayList();
                                        for (int a = 0; a < walls.size(); a++) {
                                            drawData.addAll(walls.get(a).getWallSegments());
                                        }

                                        shapeList.addAll(drawData);
                                        this.obstacles.put(obs, drawData);
                                        this.updateObstacle.get(obs).set(false);

                                    }
                                }

                            } else {
                                shapeList.remove(shapeList.get(x));
                                x--;
                                shapeList.addAll(this.obstacles.get(obs));
                            }
                        } else {
                            this.updateObstacle.put(obs, new ImBoolean(true));
                        }
                    }
                }

                // chooses the drawing type based on the style you have selected
                Style style = runMan.getStyle(styles.get(i));

                int program = ShaderManager.getProgram(ShaderManager.ProgramNames.DEFAULT);
                DrawColor color = style.getColor();

                if (styles.get(i).equals("water")) {
                    program = ShaderManager.getProgram(ShaderManager.ProgramNames.WATER);
                }

                this.selectProgram(program);
                GL33C.glUniform3f(GL33C.glGetUniformLocation(program, "col"), color.getRed(), color.getGreen(), color.getBlue());

                
                // make sure that the editShape is not included in the display
                shapeList.remove(this.editShape);
                
                
                if (Style.styleTypes[(style.getSelectedStyle().get())].equals("point")) {
                    WindowVisualizer.drawPoints(shapeList, 8, style.getColor());
                } else if (Style.styleTypes[(style.getSelectedStyle().get())].equals("solid")) {
                    WindowVisualizer.drawTriangles(shapeList, style.getColor());
                } else {
                    WindowVisualizer.drawLines(shapeList, 0.01f, style.getColor(), true);
                }

                for (int x = 0; x < shapeList.size(); x++) {
                    if (shapeList.get(x).getShowLabel().get() && !shapeList.get(x).getName().get().equals("") && shapeList.get(x).getCenter() != null) {
                        if (runMan.savePlease == 0) {
                            // making sure that the label doesn't block a shape that is being edited
                            if (this.editShape != shapeList.get(x)) {
                                Point textPoint = this.worldPointToScreenPoint(shapeList.get(x).getCenter(), runMan.getWidth(), runMan.getHeight());
                                gui.textPopup(shapeList.get(x).getName().get(), (float) textPoint.x, (float) textPoint.y, i + x + 1);
                            }

                        } else {
                            Point textPoint = this.worldPointToScreenPoint(shapeList.get(x).getCenter(), runMan.getImageResX(), runMan.getImageResY());
                            //System.out.println(textPoint);
                            gui.textPopup(shapeList.get(x).getName().get(), (float) textPoint.x, (float) textPoint.y, i + x + 1);
                        }
                    }
                }

            }

        }

        for (int i = 0; i < editingShapes.size(); i++) {
            editingShapes.get(i).calculateGlLines(true);
        }

        //System.out.println(editingShapes.size());
        if (runMan.savePlease == 0) {
            this.selectProgram(ShaderManager.ProgramNames.DEFAULT);
            this.setColor(runMan.getEditStyle().getColor());
            WindowVisualizer.drawGlLines(editingShapes, 6, runMan.getEditStyle().getColor(), true);

            // for visualization of vertices and other things
            WindowVisualizer.drawPointsWithPoints(this.editingPoints, 10, runMan.getEditStyle().getColor());
        }

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
                    continue;
                }
            }

            currentStyleShapes.add(shapes.get(x));

            this.shapesByStyle.get(styles[shapes.get(x).getStyle().get()]).addAll(currentStyleShapes);
        }

        System.out.println("updated style groupings");

    }

    public void updateAnObstacle(Obstacle ob) {
        if (this.updateObstacle.containsKey(ob)) {
            this.updateObstacle.get(ob).set(true);
        } else {
            this.updateObstacle.put(ob, new ImBoolean(true));
        }
        System.out.println(this.updateObstacle.get(ob));
    }

    public void setEditPoint(Point editPoint) {
        this.editPoint = editPoint;
    }

    public void setEditShape(EditorShape shape) {
        this.editShape = shape;
    }

    public void setEditShapeMoveMode(boolean b) {
        this.moveShape = b;
    }

    public EditorShape getEditShape() {
        return this.editShape;
    }

    public void addEditingShape(EditorShape shape) {
        this.editingShapes.add(new EditorShape(shape));
    }

    public void addEditingPoint(Point p) {
        editingPoints.add(p);
    }

    public void removeEditingShape(EditorShape shape) {
        this.editingShapes.remove(shape);
    }

    public void clearEditingShapes() {
        this.editingShapes.clear();
        this.editingPoints.clear();
    }

    public boolean containsEditingShape(EditorShape shape) {
        return this.editingShapes.contains(shape);
    }

    public ArrayList<EditorShape> getBlockingShapes() {
        ArrayList<ArrayList<EditorShape>> shapes = new ArrayList<>(this.obstacles.values());
        ArrayList<EditorShape> singleList = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++) {
            for (int a = 0; a < shapes.get(i).size(); a++) {
                singleList.add(new EditorShape(shapes.get(i).get(a).getVisualPoints()));
            }
        }
        singleList.addAll(this.blockingShapes);
        return singleList;
    }

    public void setColor(DrawColor color) {
        GL33C.glUniform3f(GL33C.glGetUniformLocation(selectedProgram, "col"), color.getRed(), color.getGreen(), color.getBlue());
    }

    public void selectProgram(ShaderManager.ProgramNames name) {
        this.selectedProgram = ShaderManager.getProgram(name);
        GL33C.glUseProgram(this.selectedProgram);
    }

    public void selectProgram(int program) {
        this.selectedProgram = program;
        GL33C.glUseProgram(this.selectedProgram);
    }

    public void clearObstacleList() {
        this.obstacles.clear();
        this.updateObstacle.clear();
    }

    public void removeObstacleEntry(Obstacle obs) {
        if (this.obstacles.containsKey(obs)) {
            this.obstacles.remove(obs);
        }
        if (this.updateObstacle.containsKey(obs)) {
            this.updateObstacle.remove(obs);
        }
    }

    public void setCameraX(float x) {
        this.cameraX = x;
    }

    public void setCameraY(float y) {
        this.cameraY = y;
    }

    public Point getCameraPosition() {
        return new Point(cameraX, cameraY);
    }

}

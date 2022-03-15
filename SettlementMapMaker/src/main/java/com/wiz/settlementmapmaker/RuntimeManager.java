/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Shape;
import Shape.Point;
import com.wiz.settlementmapmaker.Actions.Action;
import com.wiz.settlementmapmaker.Actions.AlterListAction;
import com.wiz.settlementmapmaker.Actions.CombinedAction;
import com.wiz.settlementmapmaker.Actions.MethodRunAction;
import com.wiz.settlementmapmaker.Actions.ImBooleanChangeAction;
import com.wiz.settlementmapmaker.Actions.Method1ArgAction;
import imgui.ImGuiIO;
import imgui.app.Color;

import imgui.flag.ImGuiKey;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.opengl.GL33C;

/**
 *
 * @author 904187003
 */
public class RuntimeManager {

    public Settlement currentSettlement;
    private ImString pendingSettlementName = new ImString();
    private ImString pendingSettlementFolderPath = new ImString(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());

    private ImString settlementFilePath = new ImString();

    private Window window;
    private GUILayer gui;
    private int[] windowWidth = new int[1];
    private int[] windowHeight = new int[1];

    // adjustable values
    private float[] zoom = new float[]{1f};
    private float[] minBuildingSize = new float[]{0.1f};
    private float[] maxBuildingSize = new float[]{0.3f};
    private int[] lineWidth = new int[]{4};

    private boolean windowFocused = false;

    private ImString selectedDrawMenu = new ImString();

    private ImInt selectedShape = new ImInt();
    private ImString selectedDrawingType = new ImString();
    private Hashtable<String, String[]> shapeListVisuals = new Hashtable<>();

    private String[] styles = new String[]{};

    // Settlement Value Alterations
    private ImString settlementName = new ImString();

    private FixedStack<Action> undoHistory = new FixedStack<>(20);
    private FixedStack<Action> redoHistory = new FixedStack<>(20);

    public RuntimeManager(Window window, GUILayer gui) {
        this.window = window;
        this.gui = gui;
    }

    public void init() {
        GLFW.glfwSetWindowFocusCallback(window.getWindowPointer(), new WindowFocus());
    }

    public void initIO(ImGuiIO io) {
        this.io = io;
    }

    public void update() {
        GLFW.glfwGetWindowSize(window.getWindowPointer(), windowWidth, windowHeight);
        this.controls();
        this.displayData();
    }

    boolean lastSPress = false;

    public void controls() {
        if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Z)) && io.getKeyCtrl()) {
            if (this.canUndo()) {
                this.undo();
            }
        }

        if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Y)) && io.getKeyCtrl()) {
            if (this.canRedo()) {
                this.redo();
            }
        }

        if (io.getKeysDown(GLFW.GLFW_KEY_S) && io.getKeyCtrl() && lastSPress == false) {
            this.saveCurrentSettlement();
        }

        lastSPress = io.getKeysDown(GLFW.GLFW_KEY_S);

    }

    private boolean lastMiddleState = false;
    private float initMouseX = 0f, initMouseY = 0f;
    private float initCameraX = 0f, initCameraY = 0f;
    private float cameraX = 0f, cameraY = 0f;
    private ImGuiIO io;

    private float realMouseX = 0f;
    private float realMouseY = 0f;

    private Point editPoint = null;
    private EditorShape editShape = null;

    public void displayData() {
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

        float normx = cameraX / windowWidth[0],
                normy = 1 - cameraY / (float) windowHeight[0];
        GL33C.glUniform2f(GL33C.glGetUniformLocation(window.getProgram(), "offset"), normx * 2 - 1, normy * 2 - 1);

        realMouseX = (((io.getMousePosX() / windowWidth[0]) * 2) - (normx * 2));
        realMouseY = -(((io.getMousePosY() / windowHeight[0]) * 2) + (normy * 2 - 2));

        //System.out.println(realMouseX + ", " + realMouseY);
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(realMouseX,realMouseY), new Point(0,-2)})}, new Color(realMouseX/2f,0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,0), new Point(realMouseX,realMouseY), new Point(2,0)})}, new Color(-realMouseY/2f,0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(0,-2), new Point(realMouseX,realMouseY), new Point(2,-2)})}, new Color(1+(realMouseY/2f),0f,0f,1f));
//        WindowVisualizer.drawTriangles(new Shape[]{new Shape(new Point[]{new Point(2,-2), new Point(realMouseX,realMouseY), new Point(2,0)})}, new Color(1-(realMouseX/2f),0f,0f,1f));
        if (editPoint != null) {
            editPoint.setX(realMouseX);
            editPoint.setY(realMouseY);
            
            WindowVisualizer.drawPoints(new Shape[]{new Shape(new Point[]{editPoint})}, 5, this.currentSettlement.getDefaultStyle().getColor());
            if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !io.getKeyCtrl()) {
                editPoint = null;
                editShape = null;
            }
            if (editShape != null) {
                WindowVisualizer.drawPoints(new Shape[]{editShape}, 5, this.currentSettlement.getDefaultStyle().getColor());
            }
        }
    }

    public void setEditPoint(Point editPoint) {
        this.editPoint = editPoint;
    }

    public void setEditShape(EditorShape editShape) {
        this.editShape = editShape;
    }

    public void createNewSettlement(String name, String path) {
        System.out.println(name);
        setupSettlement(new Settlement(name));
        settlementFilePath.set(path);
        String fileDir = pendingSettlementFolderPath.get() + "\\" + name + ".stmap";
        FileManager.saveSettlement(currentSettlement, fileDir);
        this.setSettlementFileDirectory(fileDir);
    }

    public void saveCurrentSettlement() {
        currentSettlement.setName(settlementName.get());
        FileManager.saveSettlement(currentSettlement, this.settlementFilePath.get());
    }

    public void setupSettlement(Settlement s) { // allows the setup of additional valeus which will be altered later by gui interaction
        currentSettlement = s;
        settlementName.set(currentSettlement.getName());

        // the location for the initial updating of lists for gui visualization
        this.updateStyleList();
        this.updateShapeList();
    }

    public Settlement getCurrentSettlement() {
        return currentSettlement;
    }

    public int getWidth() {
        return windowWidth[0];
    }

    public int getHeight() {
        return windowHeight[0];
    }

    public float[] getZoom() {
        return zoom;
    }

    public float[] minBuildingSize() {
        return minBuildingSize;
    }

    public float[] maxBuildingSize() {
        return maxBuildingSize;
    }

    public int[] lineWidth() {
        return lineWidth;
    }

    public ImString getPendingSettlementName() {
        return pendingSettlementName;
    }

    public ImString getPendingSettlementFolderDirectory() {
        return pendingSettlementFolderPath;
    }

    public void setPendingSettlementFolderDirectory(String loc) {
        pendingSettlementFolderPath.set(loc);
    }

    public ImString getSettlementFileDirectory() {
        return settlementFilePath;
    }

    public void setSettlementFileDirectory(String loc) {
        settlementFilePath.set(loc);
    }

    public void openSettlementFile(String loc) {
        settlementFilePath.set(loc);
        setupSettlement(FileManager.openSettlement(loc));
    }

    public boolean getWindowFocus() {
        return windowFocused;
    }

    public ImString getSettlementName() {
        return this.settlementName;
    }

    public ImInt getSelectedShape() {
        return selectedShape;
    }

    public String[] getShapeList(String shapeType) {
        return shapeListVisuals.get(shapeType);
    }

    public List<EditorShape> getShapes(String shapeType) {
        return currentSettlement.getShapes(shapeType);
    }

    public void addShape(EditorShape shape, String shapeType) {
        useAction(new CombinedAction(new AlterListAction(currentSettlement.getShapes(shapeType), shape, false), new MethodRunAction(() -> updateShapeList())));
    }

    public void removeShape(int selectedShape, String shapeType) {
        EditorShape shape = currentSettlement.getShapes(shapeType).get(selectedShape);
        useAction(new CombinedAction(new AlterListAction(currentSettlement.getShapes(shapeType), shape, true), new MethodRunAction(() -> updateShapeList())));
    }

    public void addPoint(EditorShape addTo) {
        Point newPoint = new Point(0, 0);
        useAction(new CombinedAction(new AlterListAction(addTo.getPointList(), newPoint, false)));
        this.setEditPoint(newPoint);
        this.setEditShape(addTo);
    }
    
    public void removePoint(EditorShape removeFrom, Point point) {
        useAction(new AlterListAction(removeFrom.getPointList(), point, true));
    }

    public ImString getSelectedDrawingType() {
        return this.selectedDrawingType;
    }

    public void updateShapeList() {
        //zones = currentSettlement.getZones().stream().map(s -> s.getName().get()).toArray(sz -> new String[sz]);
        //if (zones.length <= this.selectedZone.get()) {
        //    this.selectedZone.set(zones.length - 1);
        //}
        String[] shapeTypes = Constants.CITY_SHAPE_TYPES;
        for (int i = 0; i < shapeTypes.length; i++) {
            String[] sPut = this.currentSettlement.getShapes(shapeTypes[i]).stream().map(s -> s.getName().get()).toArray(sz -> new String[sz]);
            if (sPut == null) {
                sPut = new String[0];
            }
            this.shapeListVisuals.put(shapeTypes[i], sPut);
        }
    }

    public String[] getStyles() {
        return this.styles;
    }

    public void updateStyleList() {
        this.styles = currentSettlement.getCityStyles().stream().map(s -> s).toArray(sz -> new String[sz]);
        String[] styleHold = new String[this.styles.length + 1];
        for (int i = 0; i < styleHold.length; i++) {
            if (i == 0) {
                styleHold[i] = "default";
            } else {
                styleHold[i] = styles[i - 1];
            }
        }
        styles = styleHold;
    }

    public ImString getSelectedDrawMenu() {
        return this.selectedDrawMenu;
    }

    public boolean canUndo() {
        return 0 < this.undoHistory.length;
    }

    public void undo() {
        redoHistory.push(this.undoHistory.popTop().revert());
    }

    public boolean canRedo() {
        return 0 < this.redoHistory.length;
    }

    public void redo() {
        undoHistory.push(this.redoHistory.popTop().revert());
    }

    public void setSelectedDrawMenu(String sDM) {
        this.selectedDrawMenu.set(sDM);
    }

    public void useAction(Action action) {
        redoHistory.clear();
        undoHistory.push(action);
    }

    public float[] getColor(String key) {
        return this.currentSettlement.getColor(key);
    }

    public Style getStyle(String key) {
        return this.currentSettlement.getStyle(key);
    }

    public ArrayList<String> getCityStyles() {
        return this.currentSettlement.getCityStyles();
    }

    public Style getDefaultStyle() {
        return this.currentSettlement.getDefaultStyle();
    }

    public void addStyle(String style) {
        this.currentSettlement.addStyle(style);
    }

    public void removeStyle(String style) {
        this.currentSettlement.removeStyle(style);
    }

    public class WindowFocus implements GLFWWindowFocusCallbackI {

        @Override
        public void invoke(long l, boolean bln) {
            if (l == window.getWindowPointer()) {
                windowFocused = bln;
            }
        }

    }

}

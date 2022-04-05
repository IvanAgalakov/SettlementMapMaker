/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import com.wiz.settlementmapmaker.Utilities.FixedStack;
import Shape.EditorShape;
import Shape.Shape;
import Shape.Point;
import com.wiz.settlementmapmaker.Actions.Action;
import com.wiz.settlementmapmaker.Actions.AlterListAction;
import com.wiz.settlementmapmaker.Actions.CombinedAction;
import com.wiz.settlementmapmaker.Actions.MethodRunAction;
import com.wiz.settlementmapmaker.Actions.ImBooleanChangeAction;
import com.wiz.settlementmapmaker.Actions.ImStringChangeAction;
import imgui.ImFont;
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
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30C;
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

    private ImGuiIO io;
    private DataDisplayer dataDis;

    private ImInt imageXRes = new ImInt(8000);
    private ImInt imageYRes = new ImInt(8000);

    public RuntimeManager(Window window, GUILayer gui) {
        this.window = window;
        this.gui = gui;
    }

    public void init() {
        GLFW.glfwSetWindowFocusCallback(window.getWindowPointer(), new WindowFocus());
        GLFW.glfwSetWindowSizeCallback(window.getWindowPointer(), new WindowResizeHandler());
        dataDis = new DataDisplayer(this, io, window, gui);
    }

    // runs before init
    public void initIO(ImGuiIO io) {
        this.io = io;
        ImFont defaultFont = io.getFonts().addFontFromFileTTF("C:\\Users\\904187003\\Downloads\\Palanquin\\Palanquin-Regular.ttf", 20);
        io.setFontDefault(defaultFont);
    }

    public ImGuiIO getIO() {
        return this.io;
    }

    public void update() {
        GLFW.glfwGetWindowSize(window.getWindowPointer(), windowWidth, windowHeight);

        this.controls();
        dataDis.display();
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

        if (io.getMouseWheel() != 0) {
            this.zoom[0] += io.getMouseWheel() * Constants.MOUSE_WHEEL_SENSITIVITY;
            if (this.zoom[0] > Constants.MAX_ZOOM) {
                this.zoom[0] = Constants.MAX_ZOOM;
            }
            if (this.zoom[0] < Constants.MIN_ZOOM) {
                this.zoom[0] = Constants.MIN_ZOOM;
            }
        }

        lastSPress = io.getKeysDown(GLFW.GLFW_KEY_S);

    }

    public void setEditPoint(Point editPoint) {
        dataDis.setEditPoint(editPoint);
    }

    public void setEditShape(EditorShape editShape) {
        dataDis.setEditShape(editShape);
    }

    public void createNewSettlement(String name, String path) {
        System.out.println(name);
        setupSettlement(new Settlement(name));
        settlementFilePath.set(path);
        String fileDir = pendingSettlementFolderPath.get() + "\\" + name + ".stmap";
        FileManager.saveSettlement(currentSettlement, fileDir);
        this.setSettlementFileDirectory(fileDir);
    }

    public int savePlease = 0;

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
        this.updateDataDisplay();
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
        addTo.CalculateCenter();
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

    public void updateShapeName(EditorShape shapeToEdit, String oldName) {
        this.useAction(new CombinedAction(new ImStringChangeAction(shapeToEdit.getName(), shapeToEdit.getName().get(), oldName), new MethodRunAction(() -> updateShapeList())));
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

    public Style getBackdropStyle() {
        return this.currentSettlement.getBackdropStyle();
    }

    public void addStyle(String style) {
        this.currentSettlement.addStyle(style);
        this.updateStyleList();
    }

    public void removeStyle(String style) {
        this.currentSettlement.removeStyle(style);
        this.updateStyleList();
    }

    public void updateDataDisplay() {
        dataDis.updateShapeStyleGroupings();
    }

    public int getImageResX() {
        return this.imageXRes.get();
    }

    public ImInt getImageResXArray() {
        return this.imageXRes;
    }

    public int getImageResY() {
        return this.imageYRes.get();
    }

    public ImInt getImageResYArray() {
        return this.imageYRes;
    }

    public class WindowFocus implements GLFWWindowFocusCallbackI {

        @Override
        public void invoke(long l, boolean bln) {
            if (l == window.getWindowPointer()) {
                windowFocused = bln;
            }
        }

    }

    private class WindowResizeHandler implements GLFWWindowSizeCallbackI {

        @Override
        public void invoke(long window, int width, int height) {
            GL.createCapabilities();
            GL30C.glViewport(0, 0, width, height);
        }
    }

}

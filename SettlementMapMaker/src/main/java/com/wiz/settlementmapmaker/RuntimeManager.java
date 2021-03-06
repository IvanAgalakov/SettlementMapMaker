/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GUI.FontLibrary;
import GUI.TextureLibrary;
import GUI.Style;
import GUI.GUILayer;
import Shapes.Building;
import com.wiz.settlementmapmaker.Utilities.FixedStack;
import Shapes.EditorShape;
import Shapes.Line;
import Shapes.Obstacle;
import Shapes.Point;
import Shapes.Zone;
import com.wiz.settlementmapmaker.Actions.Action;
import com.wiz.settlementmapmaker.Actions.AlterListAction;
import com.wiz.settlementmapmaker.Actions.CombinedAction;
import com.wiz.settlementmapmaker.Actions.MethodRunAction;
import com.wiz.settlementmapmaker.Actions.ImBooleanChangeAction;
import com.wiz.settlementmapmaker.Actions.ImIntChangeAction;
import com.wiz.settlementmapmaker.Actions.ImStringChangeAction;
import com.wiz.settlementmapmaker.Actions.SetListAction;
import com.wiz.settlementmapmaker.Utilities.Utils;
import imgui.ImFont;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.app.Color;
import imgui.flag.ImGuiCol;

import imgui.flag.ImGuiKey;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
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

    private boolean settingCamera = false;

    public RuntimeManager(Window window, GUILayer gui) {
        this.window = window;
        this.gui = gui;
    }

    public void init() {
        GLFW.glfwSetWindowFocusCallback(window.getWindowPointer(), new WindowFocus());
        GLFW.glfwSetWindowSizeCallback(window.getWindowPointer(), new WindowResizeHandler());
        dataDis = new DataDisplayer(this, io, window, gui);
    }

    public void initStyle() {

        ImGuiStyle style = ImGui.getStyle();
        style.setFrameRounding(2.3f);
        style.setWindowRounding(10f);
        style.setWindowBorderSize(2);
        style.setWindowPadding(5, 5);

        style.setColor(ImGuiCol.TitleBg, 75, 54, 11, 255);
        style.setColor(ImGuiCol.TitleBgActive, 95, 60, 20, 255);
        style.setColor(ImGuiCol.TitleBgCollapsed, 95, 47, 20, 255);
        style.setColor(ImGuiCol.FrameBg, 95, 45, 11, 255);
        style.setColor(ImGuiCol.HeaderHovered, 117, 64, 19, 255);
        style.setColor(ImGuiCol.Header, 101, 62, 28, 255);
        style.setColor(ImGuiCol.Border, 255, 255, 255, 255);
        style.setColor(ImGuiCol.WindowBg, 0, 0, 0, 200);
    }

    // runs before init
    public void initIO(ImGuiIO io) {
        this.io = io;
        FontLibrary.loadAllFonts(io);
        TextureLibrary.loadAllTextures();
        SettlementNameGenerator.loadAllNames();

        io.setFontDefault(FontLibrary.getFont(0));
        initStyle();
        //ImFont defaultFont = io.getFonts().addFontFromFileTTF("C:\\Users\\904187003\\Downloads\\Palanquin\\Palanquin-Regular.ttf", 20);
        //io.setFontDefault(defaultFont);
    }

    public long getStartTime() {
        return window.getStartTime();
    }

    public ImGuiIO getIO() {
        return this.io;
    }

    public void update() {
        GLFW.glfwGetWindowSize(window.getWindowPointer(), windowWidth, windowHeight);

        this.controls();
        dataDis.display();
    }

    private boolean lastSPress = false;
    private boolean rightClick = false;
    private boolean lastRightClick = false;
    private boolean leftClick = false;
    private boolean lastLeftClick = false;

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

        // checks if a mouse press just happened, later would be a good idea to make this a general thing
        if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && rightClick == false && lastRightClick == false) {
            rightClick = true;
        } else if (rightClick == true) {
            rightClick = false;
        }
        lastRightClick = io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

        if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && leftClick == false && lastLeftClick == false) {
            leftClick = true;
        } else if (leftClick == true) {
            leftClick = false;
        }
        lastLeftClick = io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);

        if (io.getMouseWheel() != 0 && !this.imGuiWantCaptureMouse()) {
            this.zoom[0] += io.getMouseWheel() * Constants.MOUSE_WHEEL_SENSITIVITY;
            if (this.zoom[0] > Constants.MAX_ZOOM) {
                this.zoom[0] = Constants.MAX_ZOOM;
            }
            if (this.zoom[0] < Constants.MIN_ZOOM) {
                this.zoom[0] = Constants.MIN_ZOOM;
            }
        }

        lastSPress = io.getKeysDown(GLFW.GLFW_KEY_S);

        if (this.settingCamera) {
            if (this.getCurrentSettlement().getCamera().size() >= 2 && this.getCurrentSettlement().getCamera() != dataDis.getEditShape()) {
                this.setCamera(false);
            }
        }

    }

    public boolean getRightClickState() {
        return rightClick;
    }

    public boolean getLeftClickState() {
        return leftClick;
    }

    public boolean imGuiWantCaptureMouse() {
        return io.getWantCaptureMouse();
    }

    public void setEditPoint(Point editPoint) {
        dataDis.setEditPoint(editPoint);
    }

    public void setEditShape(EditorShape editShape) {
        dataDis.setEditShape(editShape);
    }

    public void setEditShapeMoveMode(boolean b) {
        this.dataDis.setEditShapeMoveMode(b);
    }

    public void addEditingPoint(Point p) {
        dataDis.addEditingPoint(p);
    }

    public void addEditingShape(EditorShape editShape) {
        dataDis.addEditingShape(editShape);
    }

    public void removeEditingShape(EditorShape editShape) {
        dataDis.removeEditingShape(editShape);
    }

    public void clearEditingShapes() {
        dataDis.clearEditingShapes();
    }

    public boolean containsEditingShape(EditorShape editShape) {
        return dataDis.containsEditingShape(editShape);
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

        dataDis.clearObstacleList();
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

    public void moveShape(int dir, List<EditorShape> moveIn, ImInt toMove) {
        int swapWith = -1;
        if (toMove.get() + dir > moveIn.size() - 1 || toMove.get() + dir < 0) {
            return;
        }

        swapWith = toMove.get() + dir;

        EditorShape temp = moveIn.get(swapWith);

        System.out.println(toMove.get() + " -- " + swapWith);
        this.useAction(new CombinedAction(new SetListAction(moveIn, swapWith, moveIn.get(toMove.get())),
                new SetListAction(moveIn, toMove.get(), temp),
                new ImIntChangeAction(toMove, toMove.get() + dir),
                new MethodRunAction(() -> updateShapeList())));
    }

    public void removeShape(int selectedShape, String shapeType) {
        EditorShape shape = currentSettlement.getShapes(shapeType).get(selectedShape);
        useAction(new CombinedAction(new AlterListAction(currentSettlement.getShapes(shapeType), shape, true), new MethodRunAction(() -> updateShapeList()), new MethodRunAction(() -> updateDataDisplay())));
    }

    public void addPoint(EditorShape addTo) {
        Point newPoint = new Point(0, 0);
        useAction(new CombinedAction(new AlterListAction(addTo.getPointList(), newPoint, false), new MethodRunAction(() -> this.updateShape(addTo))));
        addTo.CalculateCenter();
        this.setEditPoint(newPoint);
        this.setEditShape(addTo);
        dataDis.updateShapeStyleGroupings();
    }

    public void movePoint(int dir, EditorShape moveIn, ImInt toMove) {
        int swapWith = -1;
        if (toMove.get() + dir > moveIn.size() - 1 || toMove.get() + dir < 0) {
            return;
        }

        swapWith = toMove.get() + dir;

        Point temp = moveIn.getPoint(swapWith);

        System.out.println(toMove.get() + " -- " + swapWith);
        this.useAction(new CombinedAction(new SetListAction(moveIn.getPointList(), swapWith, moveIn.getPoint(toMove.get())),
                new SetListAction(moveIn.getPointList(), toMove.get(), temp),
                new ImIntChangeAction(toMove, toMove.get() + dir)));
    }

    public void removePoint(EditorShape removeFrom, Point point) {
        useAction(new CombinedAction(new AlterListAction(removeFrom.getPointList(), point, true), new MethodRunAction(() -> this.updateShape(removeFrom))));
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

    public Style getEditStyle() {
        return this.currentSettlement.getEditStyle();
    }

    public Style getWaterStyle() {
        return this.currentSettlement.getWaterStye();
    }

    public void addStyle(String style) {
        this.currentSettlement.addStyle(style);
        this.updateStyleList();
    }

    public void removeStyle(String style) {
        ArrayList<String> cityStyles = this.currentSettlement.getCityStyles();
        int pos = cityStyles.indexOf(style);
        Style styleToGo = this.currentSettlement.getStyle(style);

        this.useAction(new CombinedAction(new MethodRunAction(() -> this.currentSettlement.removeStyle(style), () -> this.currentSettlement.addStyle(style, styleToGo, pos)), new MethodRunAction(() -> this.updateStyleList())));
        //this.currentSettlement.removeStyle(style);
        //this.updateStyleList();
    }

    public void updateDataDisplay() {
        dataDis.updateShapeStyleGroupings();
    }

    public void updateShape(EditorShape shape) {
        this.calculateShape(shape, this.getStyle(this.getStyles()[shape.getStyle().get()]));
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

    public void generateBlockInZone(Zone zone, long seed) {
        SettlementGenerator.setRandomSeed(seed);

        zone.clearContainedShapes();
        ArrayList<Building> toCut = new ArrayList();
        toCut.add(new Building((EditorShape) zone));

        ArrayList<Building> buildings = SettlementGenerator.cutUpShape(toCut, zone.getDivisions(), zone.getMinPerimeter());

        ArrayList<EditorShape> block = dataDis.getBlockingShapes();
        for (int i = buildings.size() - 1; i >= 0; i--) {
            if (buildings.get(i).getSmallestSide() < zone.getMinSideLength()) {
                buildings.remove(i);
            } else {
                for (int a = 0; a < block.size(); a++) {
                    if (buildings.get(i).overlaps(block.get(a))) {
                        buildings.remove(i);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).setName(buildings.get(i).getName().get() + i);
            calculateShape(buildings.get(i), this.getStyle(this.getStyles()[zone.getStyle().get()]));
        }
        zone.addBuildings(buildings);
    }

    public void generateCitySectionsInZone(Zone zone, long seed, boolean keepSeed) {

        zone.clearContainedShapes();
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Line> borderLines = new ArrayList<>();
        // needed to make sure that the old regions are used if we are not generating a new city
        if (!keepSeed) {
            buildings = SettlementGenerator.getMultipleBlocks(zone);
            zone.setOldCityRegions(buildings);
        }

        buildings = zone.getOldCityRegions();

        for (int i = buildings.size() - 1; i >= 0; i--) {

            // needs to be done here so that regions are scaled even if they aren't regenerated
            buildings.get(i).ScaleByNumber(zone.getRoadSize().get());

            if (buildings.get(i).getPerimeter() < zone.getMinPerimeter()) {
                buildings.remove(i);
            }
        }

        for (int i = 0; i < buildings.size(); i++) {
            borderLines.addAll(buildings.get(i).getLines(true));
        }

        SettlementGenerator.setRandomSeed(seed);
        buildings = SettlementGenerator.cutUpShape(buildings, zone.getDivisions(), zone.getMinPerimeter());

        ArrayList<EditorShape> block = dataDis.getBlockingShapes();

        for (int i = buildings.size() - 1; i >= 0; i--) {
            if (buildings.get(i).getSmallestSide() < zone.getMinSideLength()) {
                buildings.remove(i);
            } else {
                boolean remove = true;
                for (int x = 0; x < borderLines.size(); x++) {
                    for (int y = 0; y < buildings.get(i).size(); y++) {
                        if (borderLines.get(x).isPointOnLine(buildings.get(i).getPoint(y))) {
                            remove = false;
                            break;
                        }
                    }
                    if (!remove) {
                        break;
                    }
                }
                if (remove) {
                    buildings.remove(i);
                    continue;
                }

                //checks for overlaps with blocking shapes
                for (int a = 0; a < block.size(); a++) {
                    if (buildings.get(i).overlaps(block.get(a))) {
                        buildings.remove(i);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < buildings.size(); i++) {
            calculateShape(buildings.get(i), this.getStyle(this.getStyles()[zone.getStyle().get()]));
            buildings.get(i).setName(buildings.get(i).getName().get() + i);
        }
        zone.getContainedShapes().addAll(buildings);
    }

    public void calculateShape(EditorShape shape, Style style) {
        switch (Style.styleTypes[style.getSelectedStyle().get()]) {
            case "line" ->
                shape.calculateLinesFromPoints(currentSettlement.getLineThickness(), true);
            case "dashed line" ->
                shape.calculateDottedLinesFromPoints(currentSettlement.getLineThickness(), true);
            case "point" ->
                shape.calculatePointsAsPoints();
            case "solid" ->
                shape.calculateTrianglesFromPoints();
        }
    }

    public Point screenPointToWorldPoint(Point screen, int width, int height) {
        return dataDis.screenPointToWorldPoint(screen, width, height);
    }

    public Point getMouseWorldPoint() {
        Point p = new Point(io.getMousePosX(), io.getMousePosY());
        return screenPointToWorldPoint(p, this.getWidth(), this.getHeight());
    }

    public void updateObstacle(Obstacle obs) {
        dataDis.updateAnObstacle(obs);
    }

    public void removeObstacle(Obstacle obs) {
        dataDis.removeObstacleEntry(obs);
    }

    public boolean isSettingCamera() {
        return this.settingCamera;
    }

    public void setCamera(boolean b) {
        this.settingCamera = b;
        if (b) {
            dataDis.setEditShape(this.currentSettlement.getCamera());
            Point p = new Point(0, 0);

            this.currentSettlement.getCamera().addPoints(p);

            dataDis.setEditPoint(p);
        } else {
            dataDis.setEditShape(null);
        }
    }

    public void clearCameraShape() {
        this.currentSettlement.clearCameraShape();
    }

    public EditorShape getCameraShape() {
        return this.currentSettlement.getCamera();
    }

    public DataDisplayer getDataDisplay() {
        return this.dataDis;
    }

    public ImString getExportFilePath() {
        return this.currentSettlement.getExportFilePath();
    }

    public void setExportFilePath(String s) {
        this.currentSettlement.setExportFilePath(s);
    }

    public ImString getExportFileName() {
        return this.currentSettlement.getExportFileName();
    }

    public ImFloat getLineThickness() {
        return this.currentSettlement.getLineThicknessDisplay();
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

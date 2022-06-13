/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Shapes.EditorShape;
import Shapes.Obstacle;
import Shapes.Point;
import Shapes.Zone;
import com.wiz.settlementmapmaker.Actions.ImStringChangeAction;
import com.wiz.settlementmapmaker.Constants;
import com.wiz.settlementmapmaker.RuntimeManager;
import com.wiz.settlementmapmaker.SettlementNameGenerator;
import com.wiz.settlementmapmaker.Utilities.CityEditorState;
import com.wiz.settlementmapmaker.Utilities.MethodPass;
import com.wiz.settlementmapmaker.Utilities.Utils;
import com.wiz.settlementmapmaker.Window;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImColor;
import imgui.ImFont;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 904187003
 */
public class GUILayer {

    private boolean filePopupShow = false;

    private Window myWindow;
    private RuntimeManager runMan;

    private boolean fileChooserOpen = false;

    public GUILayer() {

    }

    public void initLayer(Window window, RuntimeManager runMan) {
        this.myWindow = window;
        this.runMan = runMan;
    }

    private boolean openConfirmationPopup = false;
    private boolean openErrorPopup = false;
    public void imgui() {
        toolBar();
        
        
        if (this.settlementOpen()) {
            settlementManagement();
        }
        
        if (openConfirmationPopup) {
            ImGui.openPopup("Confirmation Popup");
            openConfirmationPopup = false;
        }
        
        if (openErrorPopup) {
            ImGui.openPopup("Error Popup");
            openErrorPopup = false;
        }
        
        modalPopups();
    }

    public void textPopup(String text, float x, float y, int number) {

        ImVec2 result = new ImVec2();
        ImGui.calcTextSize(result, text);

        ImGui.setNextWindowPos(x - result.x / 2, y - result.y / 2);
        ImGui.setNextWindowSize(0, 0);

        ImGui.begin(text + "##" + number, ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoFocusOnAppearing);

        ImGui.text(text);

        //ImGui.setWindowPos(x-ImGui.getWindowSizeX()/2, y);
        ImGui.end();
    }

    private boolean showDrawMenu = false;

    private boolean showRightClickMenu = false;
    private ImVec2 rightClickPosition = new ImVec2();

    private ImBoolean showCamera = new ImBoolean(false);
    
    
    
    public void settlementManagement() {
        runMan.clearEditingShapes();

//        if (runMan.getRightClickState() && !runMan.imGuiWantCaptureMouse()) {
//            showRightClickMenu = true;
//            rightClickPosition = new ImVec2(runMan.getIO().getMousePos());
//        }
//        else if (runMan.getLeftClickState()) {
//            showRightClickMenu = false;
//        }
//        if (showRightClickMenu) {
//            rightClick();
//        }

        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(runMan.getWidth() - 500, 20, ImGuiCond.Once);
        ImGui.begin("Management");

        ImGui.inputText("Settlement Name: ", runMan.getSettlementName());
        ImGui.sliderFloat("Zoom", runMan.getZoom(), Constants.MIN_ZOOM, Constants.MAX_ZOOM);

        if (!runMan.isSettingCamera()) {
            if (ImGui.button("Toggle Draw Menu")) {
                showDrawMenu = !showDrawMenu;
            }

            if (ImGui.button("Set Export Camera")) {
                showDrawMenu = false;
                runMan.clearCameraShape();
                runMan.setCamera(true);
            }
            ImGui.sameLine();
            ImGui.checkbox("Show Camera", showCamera);
            
        } else {
            ImGui.beginDisabled();
            ImGui.button("Toggle Draw Menu");
            ImGui.button("Set Export Camera");
            ImGui.sameLine();
            ImGui.checkbox("Show Camera", showCamera);
            ImGui.endDisabled();
        }
        
        ImGui.sliderFloat("Line Thickness", runMan.getLineThickness().getData(), 0.005f, 0.05f);
        
        if (this.showCamera.get()) {
            runMan.addEditingShape(Utils.boxPoints(runMan.getCameraShape()));
        }

        if (showDrawMenu) {
            drawMenu();
        }
        
        
        
//        if (ImGui.button("test")) {
//            yesMethod = () -> System.out.println("working");
//            ImGui.openPopup("Confirmation Popup");
//        }
        
        
        

        ImGui.end();
        
    }
    
    String confirmationMessage = "Are you sure about blah blah blah?";
    MethodPass yesMethod;
    String errorMessage = "ERROR: SOMETHING WENT WRONG, PLEASE TRY AGAIN.";
    
    public void modalPopups() {
        
        int width = 150;
        int height = 100;
        float buttonWidth = 50;
        float buttonHeight = 40;
        ImVec2 result = new ImVec2();
        ImGui.calcTextSize(result, confirmationMessage);
        if (width < result.x+10) {
            width = (int)result.x+10;
        }
        ImGui.setNextWindowSize(width, height);
        ImGui.setNextWindowPos(runMan.getWidth()/2-width/2, runMan.getHeight()/2-height/2);
        if (ImGui.beginPopupModal("Confirmation Popup", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse)) {
            ImGui.text(confirmationMessage);
            if (ImGui.button("yes", buttonWidth, buttonHeight)) {
                if (yesMethod != null) {
                    yesMethod.myMethod();
                }
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine(width-buttonWidth);
            if(ImGui.button("no", buttonWidth, buttonHeight)) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
        
        
        width = 150;
        height = 100;
        buttonWidth = 50;
        buttonHeight = 40;
        ImGui.calcTextSize(result, errorMessage);
        if (width < result.x+10) {
            width = (int)result.x+10;
        }
        ImGui.setNextWindowSize(width, height);
        ImGui.setNextWindowPos(runMan.getWidth()/2-width/2, runMan.getHeight()/2-height/2);
        if (ImGui.beginPopupModal("Error Popup", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse)) {
            ImGui.textColored(Constants.COLOR_RED, errorMessage);
            ImGui.indent(width/2-buttonWidth/2);
            if (ImGui.button("Ok", buttonWidth, buttonHeight)) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }
    
    public void openConfirmationPopup(MethodPass method, String message) {
        this.confirmationMessage = message;
        this.yesMethod = method;
        this.openConfirmationPopup = true;
    }
    
    public void openErrorPopup(String message) {
        this.errorMessage = message;
        this.openErrorPopup = true;
    }

    public void rightClick() {
        ImGui.setNextWindowPos(rightClickPosition.x, rightClickPosition.y);
        ImGui.begin("Right Click", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove);

        if (shapeToEdit != null) {
            if (ImGui.button("New Point")) {
                Point p = runMan.screenPointToWorldPoint(new Point(rightClickPosition.x, rightClickPosition.y), runMan.getWidth(), runMan.getHeight());
                runMan.addPoint(shapeToEdit);
                showRightClickMenu = false;
            }
        }

        ImGui.setWindowSize(120, 100);
        ImGui.end();
    }

    public void drawMenu() {

        // runs the shape editor
        shapeEdit(new ImVec2(runMan.getWidth(), runMan.getHeight()));

        ImGui.setNextWindowSize(250, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(0, 30, ImGuiCond.Once);
        ImGui.begin("Draw Menu");

        if (ImGui.button("Shape Drawing Menu")) {
            if (runMan.getSelectedDrawMenu().get().equals("shape")) {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), ""));
            } else {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), "shape"));
            }
        }

        ImVec2 pos = ImGui.getWindowPos();
        pos.set(pos.x + ImGui.getWindowSizeX(), pos.y);

        switch (runMan.getSelectedDrawMenu().get()) {
            case "shape":
                this.editorShapeDrawingMenu(pos);
                break;
        }

        ImGui.end();
    }

    ImString editorType = new ImString(Constants.CITY_SHAPE_TYPES[0]);

    public void editorShapeDrawingMenu(ImVec2 pos) {
        // start of editor window
        ImGui.setNextWindowSize(250, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        ImGui.begin("Shape Drawing Menu");

        ImGui.textColored(Constants.COLOR_CALM_GREEN, "Drawing: " + editorType + "s");

        // start of tab bar
        ImGui.beginTabBar("Edit Type");
        for (int i = 0; i < Constants.CITY_SHAPE_TYPES.length; i++) {
            if (ImGui.tabItemButton(Constants.CITY_SHAPE_TYPES[i])) {
                runMan.useAction(new ImStringChangeAction(editorType, Constants.CITY_SHAPE_TYPES[i]));
                runMan.getSelectedShape().set(0);
            }
        }
        ImGui.endTabBar();
        // end of tab bar

        // makes sure the selected shape stays within the possible range of selected shapes
        if (runMan.getSelectedShape().get() >= runMan.getShapeList(editorType.get()).length) {
            runMan.getSelectedShape().set(runMan.getShapeList(editorType.get()).length - 1);
        }
        if (runMan.getSelectedShape().get() < 0) {
            runMan.getSelectedShape().set(0);
        }
        // -----------------------------------------------------------------------------------

        ImGui.listBox(editorType.get(), runMan.getSelectedShape(), runMan.getShapeList(editorType.get()), 4);

        if (runMan.getShapeList(editorType.get()).length != 0) {
            shapeToEdit = runMan.getShapes(editorType.get()).get(runMan.getSelectedShape().get());
        } else {
            shapeToEdit = null;
        }

        if (ImGui.button("Draw New " + editorType)) {
            int count = 1;
            while (Arrays.stream(runMan.getShapeList(editorType.get())).anyMatch((editorType.get() + count)::equals)) {
                count++;
            }
            EditorShape newShape = Constants.CityShapeTypes.valueOf(editorType.get().toUpperCase()).supply(new CityEditorState());
            newShape.setName(editorType.get() + count);
            runMan.addShape(newShape, editorType.get());
            runMan.getSelectedShape().set(runMan.getShapeList(editorType.get()).length - 1);
        }

        if (runMan.getShapeList(editorType.get()).length != 0) {
            // moving of shapes
            if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_UP), 20, 20)) {
                runMan.moveShape(-1, runMan.getShapes(editorType.get()), runMan.getSelectedShape());
            }
            ImGui.sameLine();
            if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_DOWN), 20, 20)) {
                runMan.moveShape(1, runMan.getShapes(editorType.get()), runMan.getSelectedShape());
            }
            ImGui.sameLine();
            if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_MOVE_ARROW), 20, 20)) {
                runMan.setEditPoint(new Point(0,0));
                runMan.setEditShape(runMan.getShapes(editorType.get()).get(runMan.getSelectedShape().get()));
                runMan.setEditShapeMoveMode(true);
            }
            // moving of shapes end

            if (ImGui.button("Delete Selected " + editorType)) {
                if (runMan.getShapes(editorType.get()).get(runMan.getSelectedShape().get()) instanceof Obstacle obs) {
                    runMan.removeObstacle(obs);
                }
                runMan.removeShape(runMan.getSelectedShape().get(), editorType.get());
            }
        } else {
            ImGui.beginDisabled();
            ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_UP), 20, 20);
            ImGui.sameLine();
            ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_DOWN), 20, 20);
            ImGui.sameLine();
            ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_MOVE_ARROW), 20, 20);
            ImGui.button("Delete Selected " + editorType);
            ImGui.endDisabled();
        }

        ImGui.end();
    }

    // allows for the editing of shapes, no matter what type of shape they are.
    // changing of styles and other drawing modes is also chosen here, per shape
    ImInt selectedPoint = new ImInt();
    ImInt selectedStyleForShape = new ImInt();
    EditorShape shapeToEdit = null;

    boolean shapeNameBeingChanged = false;
    String oldName = "";

    public void shapeEdit(ImVec2 pos) {
        ImGui.setNextWindowSize(400, 500, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x - 700, pos.y - 550, ImGuiCond.Once);
        ImGui.begin("Shape Edit");
        if (shapeToEdit != null) {

            // Input Text for Shape Name ---------------------------
            ImGui.inputText("Name", shapeToEdit.getName());
            if (!ImGui.isItemActive() && shapeNameBeingChanged) {
                if (!oldName.equals(shapeToEdit.getName().get())) {
                    runMan.updateShapeName(shapeToEdit, oldName);
                }
            } else if (!ImGui.isItemActive()) {
                oldName = shapeToEdit.getName().get();
            }
            // checks the state of the inputText, allows for the undoing of changes to the text and better updating
            shapeNameBeingChanged = ImGui.isItemActive();
            // -----------------------------------------------------

            ImGui.checkbox("Show Name", shapeToEdit.getShowLabel());

            ImGui.listBox("Points", selectedPoint, shapeToEdit.toStringArray(), 4);
            if (ImGui.button("Draw Point")) {
                runMan.addPoint(shapeToEdit);
                selectedPoint.set(shapeToEdit.size()-1);
            }
            if (selectedPoint.get() < shapeToEdit.getPointList().size() && !shapeToEdit.getPointList().isEmpty()) {
                if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_UP), 20, 20)) {
                    runMan.movePoint(-1, shapeToEdit, selectedPoint);
                }
                ImGui.sameLine();
                if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_DOWN), 20, 20)) {
                    runMan.movePoint(1, shapeToEdit, selectedPoint);
                }
                ImGui.sameLine();
                if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_MOVE_ARROW), 20, 20)) {
                    runMan.setEditPoint(shapeToEdit.getPointList().get(selectedPoint.get()));
                    runMan.setEditShape(shapeToEdit);
                }
                
                if (ImGui.button("Delete Point")) {
                    runMan.removePoint(shapeToEdit, shapeToEdit.getPointList().get(selectedPoint.get()));
                }
                
                // adding a highlighted point for the shape currently being edited
                Point thePoint = new Point(shapeToEdit.getPointList().get(selectedPoint.get()));
                runMan.addEditingPoint(thePoint);
            } else {
                ImGui.beginDisabled();
                ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_UP), 20, 20);
                ImGui.sameLine();
                ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_ARROW_DOWN), 20, 20);
                ImGui.sameLine();
                ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_MOVE_ARROW), 20, 20);
                ImGui.button("Delete Point");
                ImGui.endDisabled();
            }
            if (ImGui.combo("Style", shapeToEdit.getStyle(), runMan.getStyles())) {
                runMan.updateShape(shapeToEdit);
                runMan.updateDataDisplay();
            }

            if (shapeToEdit instanceof Zone) {
                zoneShapeEditOptions(shapeToEdit);
            }

            if (shapeToEdit instanceof Obstacle) {
                obstacleShapeEditOptions(shapeToEdit);
            }

        } else {
            ImGui.text("No shape selected, please select a shape!");
        }
        ImGui.end();

    }

    private void generateBlock(Zone zone) {
        for (int i = 0; i < zone.getContainedShapes().size(); i++) {
            runMan.removeEditingShape(zone.getContainedShapes().get(i));
        }
        runMan.generateBlockInZone(zone);
    }

    private void generateCity(Zone zone, boolean sameSeed) {
        if (!zone.isConvex()) {
            this.openErrorPopup("City generation only available with convex zones.");
            return;
        }
        for (int i = 0; i < zone.getContainedShapes().size(); i++) {
            runMan.removeEditingShape(zone.getContainedShapes().get(i));
        }
        Random r = new Random();
        if (!sameSeed) {
            zone.setHiddenSeed(r.nextLong(0, Long.MAX_VALUE));
        }
        runMan.generateCitySectionsInZone(zone, zone.getHiddenSeed());
    }

    private void generate(int gen, Zone zone, boolean sameSeed) {
        if (gen == 0) {
            generateBlock(zone);
        } else {
            generateCity(zone, sameSeed);
        }
    }

    public void zoneShapeEditOptions(EditorShape toParse) {
        Zone zone = (Zone) toParse;
        if (ImGui.combo("Zone Types", zone.getZoneType(), Constants.ZONE_TYPES)) {

        }

        int gen = -1;
        if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate Buildings")) {
            gen = 0;
            if (ImGui.button("Generate Block")) {
                generateBlock(zone);
            }
        } else if (Constants.ZONE_TYPES[zone.getZoneType().get()].equals("Generate City")) {
            gen = 1;
            if (ImGui.button("Generate City")) {
                generateCity(zone, false);
            }

            if (ImGui.sliderInt("Regions", zone.getRegions().getData(), 2, 100)) {
                generateCity(zone, true);
            }

            if (ImGui.sliderFloat("Road Size", zone.getRoadSize().getData(), 0.001f, 0.1f)) {
                generateCity(zone, true);
            }
        }

        if (ImGui.sliderFloat("Minimum Perimeter", zone.getMinPerimeterData(), 0.001f, 3f)) {
            generate(gen, zone, true);
        }

        if (ImGui.sliderFloat("Minimum Side Length", zone.getMinSideLengthData(), 0.0001f, 0.1f)) {
            generate(gen, zone, true);
        }

        if (ImGui.sliderInt("Block Divisions", zone.getDivisionData(), 1, 15)) {
            generate(gen, zone, true);
        }

        ImVec2 pos = ImGui.getWindowPos();
        pos.set(pos.x + ImGui.getWindowSizeX(), pos.y);
        containedZoneList(zone, pos);
    }

    public void obstacleShapeEditOptions(EditorShape toParse) {
        Obstacle obst = (Obstacle) toParse;
        if (ImGui.combo("Obstacle Types", obst.getObstacleType(), Constants.OBSTACLE_TYPES)) {

        }

        if (Constants.OBSTACLE_TYPES[obst.getObstacleType().get()].equals("River")) {
            if (ImGui.inputScalar("seed", ImGuiDataType.S64, obst.getSeed())) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderFloat("Minimum Wiggle", obst.getDevMin().getData(), 0.0f, obst.getDevMax().get() - 0.01f)) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderFloat("Maximum Wiggle", obst.getDevMax().getData(), obst.getDevMin().get() + 0.01f, 0.2f)) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderFloat("Section Deviation", obst.getSectionDev().getData(), 0.001f, 0.1f)) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderFloat("Divisions", obst.getDivisions().getData(), 0.01f, 10f)) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderInt("Resolution", obst.getResolution().getData(), 3, 10)) {
                runMan.updateObstacle(obst);
            }
            if (ImGui.sliderFloat("thickness", obst.getThickness().getData(), 0.01f, 1f)) {
                runMan.updateObstacle(obst);
            }

        }
    }

    public void containedZoneList(Zone zone, ImVec2 pos) {
        ImGui.begin("Contained Buildings List");
        ImGui.setWindowPos(pos.x, pos.y);
        ImGui.listBox("Contained Buildings", zone.getSelectedContainedBuilding(), zone.getContainedBuildingNames());

        if (zone.getSelectedContainedBuilding().get() >= zone.getContainedShapes().size() && !zone.getContainedShapes().isEmpty()) {
            zone.getSelectedContainedBuilding().set(zone.getContainedShapes().size() - 1);
        }

        if (!zone.getContainedShapes().isEmpty()) {
            ImGui.checkbox("Show Name", zone.getContainedShapes().get(zone.getSelectedContainedBuilding().get()).getShowLabel());
            ImGui.inputText("Building Name", zone.getContainedShapes().get(zone.getSelectedContainedBuilding().get()).getName());
        }
        for (int i = 0; i < zone.getContainedShapes().size(); i++) {
            if (i == zone.getSelectedContainedBuilding().get() || zone.getContainedShapes().get(i).isPointInside(runMan.getMouseWorldPoint())) {
                runMan.addEditingShape(zone.getContainedShapes().get(i));
                if (runMan.getLeftClickState() && !runMan.imGuiWantCaptureMouse()) {
                    zone.getSelectedContainedBuilding().set(i);
                }
            }
        }
        ImGui.end();
    }

    private ImBoolean showNewSetWin = new ImBoolean(false);
    private ImBoolean showPreferencesWin = new ImBoolean(false);
    ImBoolean showExportWin = new ImBoolean(false);

    public void toolBar() { // the gui for the bar at the top of the screen, file, edit, window, etc.
        ImGui.beginMainMenuBar();

        if (showNewSetWin.get()) {
            newSettlementWindow();
        }

        if (showPreferencesWin.get()) {
            preferencesWindow();
        }

        if (showExportWin.get()) {
            this.exportWindow();
        }

        if (ImGui.beginMenu("File")) {
            fileMenu();
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Edit")) {
            editMenu();
            ImGui.endMenu();
        }

        if (!this.settlementOpen()) {
            ImGui.textColored(Constants.COLOR_PARCHMENT, "No File Open");
        } else {
            ImGui.textColored(Constants.COLOR_PARCHMENT, "File Opened: " + runMan.getSettlementFileDirectory().get());
        }

        ImGui.endMainMenuBar();
    }

    public void editMenu() {

        if (runMan.canUndo()) {
            if (ImGui.menuItem("Undo", "Ctrl+Z")) {
                runMan.undo();
            }
        } else {
            ImGui.beginDisabled();
            ImGui.menuItem("Undo", "Ctrl+Z");
            ImGui.endDisabled();
        }

        if (runMan.canRedo()) {
            if (ImGui.menuItem("Redo", "Ctrl+Y")) {
                runMan.redo();
            }
        } else {
            ImGui.beginDisabled();
            ImGui.menuItem("Redo", "Ctrl+Y");
            ImGui.endDisabled();
        }

        if (ImGui.menuItem("Preferences")) {
            showPreferencesWin.set(true);
        }

    }

    public void fileMenu() {

        if (ImGui.menuItem("New")) {
            showNewSetWin.set(true);
        }

        if (ImGui.menuItem("Open") && !this.fileChooserOpen) {
            SwingUtilities.invokeLater(() -> {
                JFrame j = new JFrame();
                j.setAlwaysOnTop(true);
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Settlement Map Files", "stmap", "settlement map");
                fileChooser.setFileFilter(filter);
                this.fileChooserOpen = true;
                //fileChooser.setFileSelectionMode(JFileChooser.);
                int choice = fileChooser.showOpenDialog(j);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file
                    runMan.openSettlementFile(fileChooser.getSelectedFile().getAbsolutePath());
                }

                this.fileChooserOpen = false;
            });

        }

        if (!this.settlementOpen()) {
            ImGui.beginDisabled();
            ImGui.menuItem("Save", "Ctrl+S");
            ImGui.menuItem("Export");
            ImGui.endDisabled();
        } else {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                runMan.saveCurrentSettlement();
            }
            if (ImGui.menuItem("Export")) {
                if (runMan.getCameraShape().size() > 0) {
                    showExportWin.set(true);
                } else {
                    this.openErrorPopup("Please set an Export Camera before attempting export.");
                }
            }
        }

    }

    boolean exportJustOpened = true;
    public void exportWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("Export", showExportWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {
            
            EditorShape camera = runMan.getCameraShape();
            double width = camera.getTopRight().x - camera.getBottomLeft().x;
            double height = camera.getTopRight().y - camera.getBottomLeft().y;
            
            double ratio = height/width;
            
            if (ImGui.inputInt("Width", runMan.getImageResXArray())) {
                runMan.getImageResYArray().set((int)(ratio*runMan.getImageResXArray().get()));
            }
            if (ImGui.inputInt("Height", runMan.getImageResYArray())) {
                runMan.getImageResXArray().set((int)(runMan.getImageResYArray().get() / ratio));
            }
            
            if (exportJustOpened) {
                runMan.getImageResXArray().set(1000);
                runMan.getImageResYArray().set((int)(ratio*runMan.getImageResXArray().get()));
                exportJustOpened = false;
            }
            
            ImGui.inputText("File Location",runMan.getExportFilePath());
            ImGui.sameLine();
            if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_FOLDER), 25, 22) && !this.fileChooserOpen) {
                SwingUtilities.invokeLater(() -> {
                    JFrame j = new JFrame();
                    j.setAlwaysOnTop(true);
                    JFileChooser fileChooser = new JFileChooser();
                    this.fileChooserOpen = true;
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int choice = fileChooser.showOpenDialog(j);

                    if (choice == JFileChooser.APPROVE_OPTION) {
                        // set the label to the path of the selected file
                        runMan.setExportFilePath(fileChooser.getSelectedFile().getAbsolutePath());
                    }

                    this.fileChooserOpen = false;
                });
            }
            
            ImGui.inputText("Name", runMan.getExportFileName());
            
            
            
            if (ImGui.button("Save Image")) {
                File f = new File(runMan.getExportFilePath().get());
                if (f.canWrite()) {
                    runMan.savePlease = 1;
                } else {
                    this.openErrorPopup("Invalid Path");
                }
            }
            ImGui.end();
        }
    }

    int selectedTab = 0;
    ImString newStyle = new ImString();

    public void preferencesWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("Preferences", showPreferencesWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {
            ImGui.beginTabBar("Select");
            if (ImGui.tabItemButton("Test")) {
                selectedTab = 0;
            }
            if (this.settlementOpen()) {
                if (ImGui.tabItemButton("Style Select")) {
                    selectedTab = 1;
                }
            } else {
                ImGui.beginDisabled();
                ImGui.tabItemButton("Style Select");
                ImGui.endDisabled();
            }
            ImGui.endTabBar();

            if (selectedTab == 0) {

            }

            if (selectedTab == 1) {
                ImGui.colorEdit4("Default", runMan.getDefaultStyle().getColor().getFloatOfColor());
                ImGui.colorEdit4("Backdrop", runMan.getBackdropStyle().getColor().getFloatOfColor());
                ImGui.colorEdit4("Water", runMan.getWaterStyle().getColor().getFloatOfColor());
                ImGui.colorEdit4("Edit", runMan.getEditStyle().getColor().getFloatOfColor());
                ImGui.dummy(0, 5f);
                ImGui.separator();
                ImGui.dummy(0, 5f);

                int remove = -1;

                ImGui.beginChild("Styles", ImGui.getWindowContentRegionMaxX() - 20f, 300);
                for (int i = 0; i < runMan.getCityStyles().size(); i++) {

                    if (ImGui.button("Remove##" + i)) {
                        remove = i;
                    }
                    ImGui.sameLine();
                    ImGui.colorEdit4(runMan.getCityStyles().get(i), runMan.getColor(runMan.getCityStyles().get(i)));
                    ImGui.combo("Style Select##" + i, runMan.getStyle(runMan.getCityStyles().get(i)).getSelectedStyle(), Style.styleTypes);
                    ImGui.dummy(0, 5f);
                    ImGui.separator();
                    ImGui.dummy(0, 5f);
                }
                ImGui.endChild();

                if (remove != -1) {
                    //runMan.removeStyle(runMan.getCityStyles().get(remove));
                    final int pass = remove;
                    //yesMethod = () -> runMan.removeStyle(runMan.getCityStyles().get(pass));
                    //openConfirmationPopup = true;
                    this.openConfirmationPopup(() -> runMan.removeStyle(runMan.getCityStyles().get(pass)), "Are you sure you want to delete this style?");
                }

                ImGui.separator();
                if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_PLUS), 20, 20) && !newStyle.get().equals("")) {
                    runMan.addStyle(newStyle.get());
                }

                ImGui.inputText("New Style Name", newStyle);
            }

            ImGui.end();
        }
    }

    public void newSettlementWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("Create Settlement", showNewSetWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {

            ImGui.text("To start a new project give your new settlement a name:\n"
                    + "(or just hit the \"Generate Name\" button to generate a random name.)");
            ImGui.inputText("File Location", runMan.getPendingSettlementFolderDirectory());
            ImGui.sameLine();

            if (ImGui.imageButton(TextureLibrary.getTexture(Constants.TEXTURE_FOLDER), 25, 22) && !this.fileChooserOpen) {
                SwingUtilities.invokeLater(() -> {
                    JFrame j = new JFrame();
                    j.setAlwaysOnTop(true);
                    JFileChooser fileChooser = new JFileChooser();
                    this.fileChooserOpen = true;
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int choice = fileChooser.showOpenDialog(j);

                    if (choice == JFileChooser.APPROVE_OPTION) {
                        // set the label to the path of the selected file
                        runMan.setPendingSettlementFolderDirectory(fileChooser.getSelectedFile().getAbsolutePath());
                    }

                    this.fileChooserOpen = false;
                });
            }

            ImGui.inputText("Name", runMan.getPendingSettlementName());
            if (ImGui.button("Generate Name")) {
                runMan.getPendingSettlementName().set(SettlementNameGenerator.getRandomSettlementName());
            }
            if (ImGui.button("Create")) {
                File f = new File(runMan.getPendingSettlementFolderDirectory().get());
                if (f.canWrite()) {
                    runMan.createNewSettlement(runMan.getPendingSettlementName().get(), runMan.getPendingSettlementFolderDirectory().get());
                    showNewSetWin.set(false);
                } else {
                    this.openErrorPopup("Invalid Path");
                }
            }
            ImGui.end();
        }
    }

    private boolean settlementOpen() {
        return runMan.getCurrentSettlement() != null;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Point;
import Shape.Zone;
import com.wiz.settlementmapmaker.Actions.ImStringChangeAction;
import com.wiz.settlementmapmaker.Utilities.CityEditorState;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImColor;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
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

    public void imgui() {
        toolBar();
        if (!runMan.getSettlementFileDirectory().get().equals("")) {
            settlementManagement();
        }

    }

    public void textPopup(String text, float x, float y, int number) {
        ImGui.setNextWindowPos(x, y);
        ImGui.setNextWindowSize(0, 0);
        ImGui.begin(text + "##" + number, ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoFocusOnAppearing);
        ImGui.text(text);
        ImGui.end();
    }

    private boolean showDrawMenu = false;

    public void settlementManagement() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(runMan.getWidth() - 500, 20, ImGuiCond.Once);
        ImGui.begin("Management");

        
        
        
        ImGui.inputText("Settlement Name: ", runMan.getSettlementName());
        ImGui.sliderFloat("Zoom", runMan.getZoom(), Constants.MIN_ZOOM, Constants.MAX_ZOOM);
        if (ImGui.button("Toggle Draw Menu")) {
            showDrawMenu = !showDrawMenu;
        }
        if (showDrawMenu) {
            drawMenu();
        }

        ImGui.end();
    }

    public void drawMenu() {

        // runs the shape editor
        shapeEdit(new ImVec2(runMan.getWidth(), runMan.getHeight()));

        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(0, 20, ImGuiCond.Once);
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
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        ImGui.begin("Shape Drawing Menu");

        ImGui.textColored(Constants.CALM_GREEN, "Drawing: " + editorType + "s");

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
            if (ImGui.button("Delete Selected " + editorType)) {
                runMan.removeShape(runMan.getSelectedShape().get(), editorType.get());
            }
        } else {
            ImGui.beginDisabled();
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
        ImGui.setNextWindowSize(400, 350, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x - 500, pos.y - 400, ImGuiCond.Once);
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

            ImGui.listBox("Points", selectedPoint, shapeToEdit.toStringArray());
            if (ImGui.button("Draw Point")) {
                runMan.addPoint(shapeToEdit);
            }
            if (selectedPoint.get() < shapeToEdit.getPointList().size() && shapeToEdit.getPointList().size() > 0) {
                if (ImGui.button("Delete Point")) {
                    runMan.removePoint(shapeToEdit, shapeToEdit.getPointList().get(selectedPoint.get()));
                }
            } else {
                ImGui.beginDisabled();
                ImGui.button("Delete Point");
                ImGui.endDisabled();
            }
            if (ImGui.combo("Style", shapeToEdit.getStyle(), runMan.getStyles())) {
                runMan.updateDataDisplay();
            }

            if (shapeToEdit instanceof Zone) {
                zoneShapeEditOptions(shapeToEdit);
            }

        } else {
            ImGui.text("No shape selected, please select a shape!");
        }
        ImGui.end();

    }

    void zoneShapeEditOptions(EditorShape toParse) {
        Zone zone = (Zone) toParse;
        if (ImGui.combo("Zone Types", zone.getZoneType(), Constants.ZONE_TYPES)) {

        }
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
                showExportWin.set(true);
            }
        }

    }

    public void exportWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("Export", showExportWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {
            ImGui.inputInt("Width", runMan.getImageResXArray());
            ImGui.inputInt("Height", runMan.getImageResYArray());
            if (ImGui.button("Save Image")) {
                runMan.savePlease = 1;
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
                ImGui.colorEdit4("default", runMan.getDefaultStyle().getColor().getFloatOfColor());
                ImGui.colorEdit4("backdrop", runMan.getBackdropStyle().getColor().getFloatOfColor());
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
                    runMan.removeStyle(runMan.getCityStyles().get(remove));
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
            ImGui.button("Generate Name");
            if (ImGui.button("Create")) {
                runMan.createNewSettlement(runMan.getPendingSettlementName().get(), runMan.getPendingSettlementFolderDirectory().get());
                showNewSetWin.set(false);
            }
            ImGui.end();
        }
    }

    private boolean settlementOpen() {
        return runMan.getCurrentSettlement() != null;
    }

}
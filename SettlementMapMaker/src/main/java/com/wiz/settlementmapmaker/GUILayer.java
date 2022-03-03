/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import com.wiz.settlementmapmaker.Actions.ImStringChangeAction;
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
import java.util.Arrays;
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

    private int warmParchment = ImColor.intToColor(150, 109, 80, 255);
    private int parchment = ImColor.intToColor(230, 203, 156, 255);

    private boolean fileChooserOpen = false;

    public GUILayer() {

    }

    public void initLayer(Window window, RuntimeManager runMan) {
        this.myWindow = window;
        this.runMan = runMan;

//        ImGuiStyle style = ImGui.getStyle();
//        style.setColor(ImGuiCol.Button, warmParchment);
//        style.setColor(ImGuiCol.WindowBg, parchment);
    }

    public void imgui() {
        toolBar();
        if (!runMan.getSettlementFileDirectory().get().equals("")) {
            settlementManagement();
        }
    }

    private boolean showDrawMenu = false;

    public void settlementManagement() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(runMan.getWidth() - 500, 20, ImGuiCond.Once);
        ImGui.begin("management");

        ImGui.inputText("Settlement Name: ", runMan.getSettlementName());
        if (ImGui.button("toggle draw menu")) {
            showDrawMenu = !showDrawMenu;
        }
        if (showDrawMenu) {
            drawMenu();
        }

        ImGui.end();
    }

    

    public void drawMenu() {
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(0, 20, ImGuiCond.Once);
        ImGui.begin("Draw Menu");

        if (ImGui.button("Building Drawing Menu")) {
            showShapeEdit.set(false);
            if (runMan.getSelectedDrawMenu().get().equals("building")) {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), ""));
            } else {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), "building"));
            }
        }
        if (ImGui.button("Zone Drawing Menu")) {
            showShapeEdit.set(false);
            if (runMan.getSelectedDrawMenu().get().equals("zone")) {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), ""));
            } else {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), "zone"));
            }
        }
        if (ImGui.button("Obstacle Drawing Menu")) {
            showShapeEdit.set(false);
            if (runMan.getSelectedDrawMenu().get().equals("obstacle")) {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), ""));
            } else {
                runMan.useAction(new ImStringChangeAction(runMan.getSelectedDrawMenu(), "obstacle"));
            }
        }

        ImVec2 pos = ImGui.getWindowPos();
        pos.set(pos.x + ImGui.getWindowSizeX(), pos.y);

        switch (runMan.getSelectedDrawMenu().get()) {
            case "building":
                this.buildingDrawingMenu(pos);
                break;
            case "zone":
                this.zoneDrawingMenu(pos);
                break;
            case "obstacle":
                this.obstacleDrawingMenu(pos);
                break;
            default:
                break;
        }

        ImGui.end();
    }

    public void zoneDrawingMenu(ImVec2 pos) {
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        ImGui.begin("Zone Drawing Menu");
        ImGui.listBox("Zones", runMan.getSelectedZone(), runMan.getZonesList(), 3);
        
        if (ImGui.button("edit zone")) {
            if (runMan.getZones().size() > 0) {
                showShapeEdit.set(true);
            }
        }
        if (ImGui.button("draw new zone")) {
            int count = 1;
            while(Arrays.stream(runMan.getZonesList()).anyMatch(("zone"+count)::equals)) {
                count++;
            }
            Zone newZone = new Zone("zone" + count, Zone.ZoneType.BUILDING_GENERATION);
            runMan.addZone(newZone, showShapeEdit);
            runMan.getSelectedZone().set(runMan.getZones().size()-1);
        }

        if (showShapeEdit.get()) {
            shapeEdit(new ImVec2(pos.x + ImGui.getWindowSizeX(), pos.y), runMan.getZones().get(runMan.getSelectedZone().get()));
        }
        ImGui.end();
    }

    public void buildingDrawingMenu(ImVec2 pos) {
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        ImGui.begin("Building Drawing Menu");
        ImGui.listBox("Buildings", runMan.getSelectedZone(), runMan.getZonesList(), 3);
        ImGui.button("draw new building");
        ImGui.end();
    }

    public void obstacleDrawingMenu(ImVec2 pos) {
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        ImGui.begin("Obstacle Drawing Menu");
        ImGui.listBox("Obstacles", runMan.getSelectedZone(), runMan.getZonesList(), 3);
        ImGui.button("draw new obstacle");
        ImGui.end();
    }

    private ImBoolean showShapeEdit = new ImBoolean(false);
    private ImInt selectedPoint = new ImInt();
    
    // allows for the editing of shapes, no matter what type of shape they are.
    // changing of styles and other drawing modes is also chosen here, per shape
    public void shapeEdit(ImVec2 pos, EditorShape shapeToEdit) {
        ImGui.setNextWindowSize(250, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Always);
        if (!ImGui.begin("Shape Edit", showShapeEdit, ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {
            ImGui.text(shapeToEdit.getName());
            ImGui.listBox("points", selectedPoint, shapeToEdit.toStringArray(), 3);
            if(ImGui.button("Draw Point")) {
                Point newPoint = new Point(0,0);
                shapeToEdit.addPoints(newPoint);
                runMan.setEditPoint(newPoint);
                runMan.setEditShape(shapeToEdit);
            }
            ImGui.end();
        }
    }

    private ImBoolean showNewSetWin = new ImBoolean(false);
    private ImBoolean showPreferencesWin = new ImBoolean(false);

    public void toolBar() { // the gui for the bar at the top of the screen, file, edit, window, etc.
        ImGui.beginMainMenuBar();

        if (showNewSetWin.get()) {
            newSettlementWindow();
        }

        if (showPreferencesWin.get()) {
            preferencesWindow();
        }

        if (ImGui.beginMenu("file")) {
            fileMenu();
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("edit")) {
            editMenu();
            ImGui.endMenu();
        }

        if (!this.settlementOpen()) {
            ImGui.textColored(parchment, "no file open");
        } else {
            ImGui.textColored(parchment, "file opened: " + runMan.getSettlementFileDirectory().get());
        }

        ImGui.endMainMenuBar();
    }

    public void editMenu() {

        if (runMan.canUndo()) {
            if (ImGui.menuItem("undo", "Ctrl+Z")) {
                runMan.undo();
            }
        } else {
            ImGui.beginDisabled();
            ImGui.menuItem("undo", "Ctrl+Z");
            ImGui.endDisabled();
        }

        if (runMan.canRedo()) {
            if (ImGui.menuItem("redo", "Ctrl+Y")) {
                runMan.redo();
            }
        } else {
            ImGui.beginDisabled();
            ImGui.menuItem("redo", "Ctrl+Y");
            ImGui.endDisabled();
        }

        if (ImGui.menuItem("preferences")) {
            showPreferencesWin.set(true);
        }

    }

    public void fileMenu() {

        if (ImGui.menuItem("new")) {
            showNewSetWin.set(true);
        }

        if (ImGui.menuItem("open") && !this.fileChooserOpen) {
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
            ImGui.menuItem("save", "Ctrl+S");
            ImGui.endDisabled();
        } else {
            if (ImGui.menuItem("save", "Ctrl+S")) {
                runMan.saveCurrentSettlement();
            }
        }

    }

    int selectedTab = 0;
    ImString newStyle = new ImString();

    public void preferencesWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("preferences", showPreferencesWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {
            ImGui.beginTabBar("select");
            if (ImGui.tabItemButton("test")) {
                selectedTab = 0;
            }
            if (this.settlementOpen()) {
                if (ImGui.tabItemButton("style select")) {
                    selectedTab = 1;
                }
            } else {
                ImGui.beginDisabled();
                ImGui.tabItemButton("style select");
                ImGui.endDisabled();
            }
            ImGui.endTabBar();

            if (selectedTab == 1) {
                ImGui.colorEdit4("default", runMan.getDefaultStyle().getColor().getFloatOfColor());
                ImGui.dummy(0, 5f);
                ImGui.separator();
                ImGui.dummy(0, 5f);

                String styleRemove = "";

                ImGui.beginChild("styles", ImGui.getWindowContentRegionMaxX() - 20f, 300);
                for (int i = 0; i < runMan.getCityStyles().size(); i++) {

                    if (ImGui.button("remove##" + i)) {
                        if (!ImGui.isPopupOpen("StyleRemovePopup")) {
                            System.out.println("active");
                            styleRemove = runMan.getCityStyles().get(i);
                            this.stringPopupString = "remove " + styleRemove;
                            ImGui.openPopup("StyleRemovePopup");
                        }
                    }
                    ImGui.sameLine();
                    ImGui.colorEdit4(runMan.getCityStyles().get(i), runMan.getColor(runMan.getCityStyles().get(i)));
                    ImGui.combo("Style Select##" + i, runMan.getStyle(runMan.getCityStyles().get(i)).getSelectedStyle(), Style.styleTypes);
                    ImGui.dummy(0, 5f);
                    ImGui.separator();
                    ImGui.dummy(0, 5f);
                }
                ImGui.endChild();

                this.confirmationStringPopup("StyleRemovePopup", s -> runMan.removeStyle(s));

                ImGui.separator();
                if (ImGui.button("Add Style") && !newStyle.get().equals("")) {
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
        if (!ImGui.begin("settlementCreate", showNewSetWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {

            ImGui.text("To start a new project give your new settlement a name:\n"
                    + "(or just hit the \"Generate Name\" button to generate a random name.)");
            ImGui.inputText("File Location", runMan.getPendingSettlementFolderDirectory());
            ImGui.sameLine();

            if (ImGui.button("choose file location") && !this.fileChooserOpen) {
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

    private String stringPopupString = "";

    private void confirmationStringPopup(String confirmationName, I myMethodInterface) {
        if (ImGui.beginPopup(confirmationName)) {
            ImGui.text("Are you sure you want to " + stringPopupString + "?");
            if (ImGui.button("yes", 40, 20)) {
                myMethodInterface.myMethod(stringPopupString.replace("remove ", ""));
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("no", 40, 20)) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

}

interface I {
    public void myMethod(String s);
}

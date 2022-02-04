/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImColor;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author 904187003
 */
public class GUILayer {

    private boolean show = false;

    private boolean editMode = false;

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
        // adjustmentsWindow();
        toolBar();
        // editWindow();

    }

    public void adjustmentsWindow() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.begin("Adjustments", new ImBoolean(true));

        ImGui.sliderFloat("Zoom", runMan.getZoom(), 0f, 1f);

        //ImGui.sliderFloat("Minimum Building Size", minBuildingSize, 0.01f, maxBuildingSize[0] - 0.001f);
        //ImGui.sliderFloat("Maximum Building Size", maxBuildingSize, minBuildingSize[0] + 0.001f, 1f);
        ImGui.sliderInt("Line Width", runMan.lineWidth(), 1, 20);

        if (ImGui.button("Show Buildings")) {
            show = !show;
        }
        if (show) {
            ImGui.text("Showing Buildings");
        }

        ImGui.end();
    }

    private ImBoolean showNewSetWin = new ImBoolean(false);

    public void toolBar() {
        ImGui.beginMainMenuBar();

        ImGui.setNextWindowPos(0, 20);

        if (ImGui.beginPopup("fileMenu")) {
            if (ImGui.button("new", 50, 20)) {
                showNewSetWin.set(true);
            }
            ImGui.button("open", 50, 20);
            ImGui.endPopup();
        }

        if (showNewSetWin.get()) {
            newSettlementWindow();
        }

        if (ImGui.button("file")) {
            ImGui.openPopup("fileMenu");
        }

        ImGui.textColored(parchment, "no file open");

        ImGui.endMainMenuBar();
    }

    public void newSettlementWindow() {
        ImGui.setNextWindowPos((runMan.getWidth() / 4), runMan.getHeight() / 4);
        ImGui.setNextWindowSize((runMan.getWidth() / 2), runMan.getHeight() / 2);
        if (!ImGui.begin("settlementCreate", showNewSetWin, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize)) {
            ImGui.end();
        } else {

            ImGui.text("To start a new project give your new settlement a name:\n"
                    + "(or just hit the \"Generate Name\" button to generate a random name.)");
            ImGui.inputText("File Location", runMan.getSettlementFolderDirectory());
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
                        runMan.setSettlementFolderDirectory(fileChooser.getSelectedFile().getAbsolutePath());
                    }

                    this.fileChooserOpen = false;
                });
            }

            ImGui.inputText("Name", runMan.getSettlementName());
            ImGui.button("Generate Name");
            ImGui.button("Create");
            ImGui.end();
        }
    }

    public void editWindow() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.begin("Tools", new ImBoolean(true));
        ImGui.text("Your middle mouse button allows you to drag the screen,\n"
                + "turning on the \"edit\" button below allows you to start placing\n"
                + "a city block");

        if (ImGui.button("edit")) {
            editMode = !editMode;
            myWindow.changeleftPressDelta(0.1f);

        }

        if (editMode) {
//            if(ImGui.button("enclose")) {
//                
//            }

            //ImGui.listBox("City Blocks", currentBlock, cityBlocks);
        }

        ImGui.end();
    }

    public boolean getEditMode() {
        return editMode;
    }

    public boolean renderBuildings() {
        return show;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package settlementmapmaker;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

/**
 *
 * @author 904187003
 */
public class GUILayer {

    private boolean show = false;
    private float[] zoom = new float[]{1f};
    private float[] minBuildingSize = new float[]{0.1f};
    private float[] maxBuildingSize = new float[]{0.3f};
    private int[] lineWidth = new int[]{4};
    private String[] cityBlocks = new String[]{"test"};
    private ImInt currentBlock = new ImInt(0);

    private boolean editMode = false;

    private boolean filePopupShow = false;

    private Window myWindow;
    private RuntimeManager runMan;

    private int blue = ImGui.colorConvertFloat4ToU32(0.6f, 0.6f, 1, 1);

    public GUILayer() {

    }

    public void initLayer(Window window, RuntimeManager runMan) {
        this.myWindow = window;
        this.runMan = runMan;
    }

    public void imgui() {
        // adjustmentsWindow();
        toolBar();
        // editWindow();

    }

    public void adjustmentsWindow() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.begin("Adjustments", new ImBoolean(true));

        ImGui.sliderFloat("Zoom", zoom, 0f, 1f);

        ImGui.sliderFloat("Minimum Building Size", minBuildingSize, 0.01f, maxBuildingSize[0] - 0.001f);
        ImGui.sliderFloat("Maximum Building Size", maxBuildingSize, minBuildingSize[0] + 0.001f, 1f);

        ImGui.sliderInt("Line Width", lineWidth, 1, 20);

        if (ImGui.button("Show Buildings")) {
            show = !show;
        }
        if (show) {
            ImGui.text("Showing Buildings");
        }

        ImGui.end();
    }

    public void toolBar() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginPopup("newSettle")) {
            ImGui.text("this is a test message this will be replaced later.");
            ImGui.endPopup();
        }

        ImGui.setNextWindowPos(0, 20);
        boolean openNewSettle = false;
        if (ImGui.beginPopup("fileMenu")) {
            if (ImGui.button("new", 50, 20)) {
                openNewSettle = true;

            }
            ImGui.button("open", 50, 20);
            ImGui.endPopup();
        }

        if (openNewSettle) {
            ImGui.openPopup("newSettle");
        }

        if (ImGui.button("file")) {
            ImGui.openPopup("fileMenu");
        }

        ImGui.textColored(blue, "no file open");

        ImGui.endMainMenuBar();
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

    public float getZoom() {
        return zoom[0];
    }

    public float getMinimumBuildingSize() {
        return minBuildingSize[0];
    }

    public float getMaximumBuildingSize() {
        return maxBuildingSize[0];
    }

    public int getLineWidth() {
        return lineWidth[0];
    }

    public boolean getEditMode() {
        return editMode;
    }

    public boolean renderBuildings() {
        return show;
    }
}

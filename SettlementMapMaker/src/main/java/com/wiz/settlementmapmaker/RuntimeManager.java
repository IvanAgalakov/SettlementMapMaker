/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.type.ImString;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;

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
    
    // Settlement Value Alterations
    private ImString settlementName = new ImString();
    

    public RuntimeManager(Window window, GUILayer gui) {
        this.window = window;
        this.gui = gui;

    }

    public void init() {
        GLFW.glfwSetWindowFocusCallback(window.getWindowPointer(), new WindowFocus());
    }

    public void update() {
        GLFW.glfwGetWindowSize(window.getWindowPointer(), windowWidth, windowHeight);

    }

    public void createNewSettlement(String name, String path) {
        System.out.println(name);
        setupSettlement(new Settlement(name));
        settlementFilePath.set(path);
        String fileDir = pendingSettlementFolderPath.get() + "\\" + name + ".stmap";
        FileManager.saveSettlement(currentSettlement, fileDir);
        this.setSettlementFileDirectory(fileDir);
    }
    
    public void setupSettlement(Settlement s) { // allows the setup of additional valeus which will be altered later by gui interaction
        currentSettlement = s;
        settlementName.set(currentSettlement.getName());
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

    public class WindowFocus implements GLFWWindowFocusCallbackI {

        @Override
        public void invoke(long l, boolean bln) {
            if (l == window.getWindowPointer()) {
                windowFocused = bln;
            }
        }

    }

}

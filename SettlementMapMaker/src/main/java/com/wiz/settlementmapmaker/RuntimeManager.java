/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;

/**
 *
 * @author 904187003
 */
public class RuntimeManager {

    public Settlement currentSettlement;
    private ImString settlementName = new ImString();

    private ImString settlementFolderDirectory = new ImString();
    private ImString settlementFileDirectory = new ImString();
    
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

    public void makeNewSettlement(String name) {
        currentSettlement = new Settlement(name);
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

    public ImString getSettlementName() {
        return settlementName;
    }

    public ImString getSettlementFolderDirectory() {
        return settlementFolderDirectory;
    }
    
    public void setSettlementFolderDirectory(String loc) {
        settlementFolderDirectory.set(loc);
    }
    
    public ImString getSettlementFileDirectory() {
        return settlementFileDirectory;
    }
    
    public void setSettlementFileDirectory(String loc) {
        settlementFileDirectory.set(loc);
    }

    public boolean getWindowFocus() {
        return windowFocused;
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

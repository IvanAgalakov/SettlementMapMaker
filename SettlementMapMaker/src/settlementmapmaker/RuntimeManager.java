/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package settlementmapmaker;

import org.lwjgl.glfw.GLFW;

/**
 *
 * @author 904187003
 */
public class RuntimeManager {
    
    public Settlement currentSettlement;
    private Window window;
    private GUILayer gui;
    private int[] windowWidth = new int[1];
    private int[] windowHeight = new int[1];
    
    public RuntimeManager(Window window, GUILayer gui) {
        this.window = window;
        this.gui = gui;
    }
    
    
    public void makeNewSettlement(String name) {
        currentSettlement = new Settlement(name);
    }
    
    public void update() {
        GLFW.glfwGetWindowSize(window.getWindowPointer(), windowWidth, windowHeight);
    }
    
    public int getWidth() {
        return windowWidth[0];
    }
    
    public int getHeight() {
        return windowHeight[0];
    }
    
}

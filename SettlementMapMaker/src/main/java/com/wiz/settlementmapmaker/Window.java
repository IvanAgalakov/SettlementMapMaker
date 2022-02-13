/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiz.settlementmapmaker;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33C;
import static org.lwjgl.opengl.GL33C.*;

/**
 *
 * @author Ivan
 */
public class Window {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPtr;
    private int program;

    private Texture tex;

    private final GUILayer imGuiLayer;
    private final RuntimeManager runMan;

    private SettlementGenerator setGen;
    
    private ArrayList<Point> currentShape = new ArrayList<Point>();

    private Shape[] draw;

    public Window(GUILayer layer) {
        this.imGuiLayer = layer;
        runMan = new RuntimeManager(this, this.imGuiLayer);

        this.setGen = new SettlementGenerator();
    }

    public void init() {
        this.initWindow();
        WindowVisualizer.WindowVisualizerInit(this);
        this.initImGui();
        this.imGuiLayer.initLayer(this, runMan);
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init();
        this.shadersInit();
        runMan.init();
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();
    }

    private void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glslVersion = "#version 330 core";

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowPtr = glfwCreateWindow(1920, 1070, "my window", 0, 0);

        if (windowPtr == 0) {
            System.out.println("Unable to create window");
            System.exit(-1);
        }

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1);
        glfwShowWindow(windowPtr);

        GL.createCapabilities();
    }

    private void initImGui() {
        ImGui.createContext();
        runMan.initIO(imgui.internal.ImGui.getIO());
    }

    private void shadersInit() {
        Shader basicVertexShader = ShaderManager.shaderNames.BASIC_VERTEX.SHADER;
        Shader basicFragmentShader = ShaderManager.shaderNames.BASIC_FRAGMENT.SHADER;
        this.program = ShaderManager.programFromShaders(basicVertexShader.getShader(), basicFragmentShader.getShader());
//
//        try {
//            BufferedImage i = ImageIO.read(new File("C:\\Users\\Ivan\\Downloads\\house.png"));
//            tex = new Texture(((DataBufferByte) i.getRaster().getDataBuffer()).getData(), 0, i.getWidth(), i.getHeight(), i);
//        } catch (Exception e) {
//            System.err.println("Unsuccessful");
//        }
    }

    public void run() {
        while (!glfwWindowShouldClose(windowPtr)) {
            glClearColor(0.92f, 0.83f, 0.7f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            runMan.update();
            imGuiGlfw.newFrame();
            ImGui.newFrame();
            imGuiLayer.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

            GLFW.glfwSwapBuffers(windowPtr);
            GLFW.glfwPollEvents();
        }
    }
    
    public long getWindowPointer() {
        return this.windowPtr;
    }
    
    public int getProgram() {
        return this.program;
    }

}

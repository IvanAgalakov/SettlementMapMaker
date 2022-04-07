/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiz.settlementmapmaker;

import GUI.DrawColor;
import GUI.GUILayer;
import GUI.Texture;
import Shapes.EditorShape;
import Shapes.Point;
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
    private int frameBuffer;
    private int renderBuffer;

    private Texture tex;

    private final GUILayer imGuiLayer;
    private final RuntimeManager runMan;

    private SettlementGenerator setGen;

    private ArrayList<Point> currentShape = new ArrayList<Point>();

    private EditorShape[] draw;

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
        this.shadersInit();
        runMan.init();
    }

    public void destroy() {
        GL33C.glDeleteFramebuffers(this.frameBuffer);
        GL33C.glDeleteRenderbuffers(this.renderBuffer);

        this.destroyImGui();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();
    }

    public void destroyImGui() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
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

        windowPtr = glfwCreateWindow(1920, 1070, "Settlement Map Maker", 0, 0);

        if (windowPtr == 0) {
            System.out.println("Unable to create window");
            System.exit(-1);
        }

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1);
        glfwShowWindow(windowPtr);

        GL.createCapabilities();

        renderBuffer = GL33C.glGenRenderbuffers();
        GL33C.glBindRenderbuffer(GL_RENDERBUFFER, this.renderBuffer);
        GL33C.glRenderbufferStorage(GL_RENDERBUFFER, GL_RGB, runMan.getImageResX(), runMan.getImageResY());
        GL33C.glBindRenderbuffer(GL_RENDERBUFFER, 0);

        frameBuffer = GL33C.glGenFramebuffers();
        GL33C.glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        GL33C.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL33C.GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, this.renderBuffer);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("all good!");
        }

        GL33C.glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    private void initImGui() {
        ImGui.createContext();
        runMan.initIO(imgui.internal.ImGui.getIO());
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init();
    }

    private void shadersInit() {
        Shader basicVertexShader = ShaderManager.ShaderNames.BASIC_VERTEX.SHADER;
        Shader basicFragmentShader = ShaderManager.ShaderNames.BASIC_FRAGMENT.SHADER;
        this.program = ShaderManager.programFromShaders(basicVertexShader.getShader(), basicFragmentShader.getShader());
//
//        try {
//            BufferedImage i = ImageIO.read(new File("C:\\Users\\Ivan\\Downloads\\house.png"));
//            tex = new Texture(((DataBufferByte) i.getRaster().getDataBuffer()).getData(), 0, i.getWidth(), i.getHeight(), i);
//        } catch (Exception e) {
//            System.err.println("Unsuccessful");
//        }
    }

    int displayX = 1920;
    int displayY = 1080;

    public void run() {
        while (!glfwWindowShouldClose(windowPtr)) {

            int[] windowView = new int[4];
            if (runMan.savePlease == 1) {
                GL33C.glGetIntegerv(GL_VIEWPORT, windowView);
                GL33C.glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);

                GL33C.glViewport(0, 0, runMan.getImageResX(), runMan.getImageResY());
                //System.out.println(windowView[2] / (float) runMan.getImageResX() + ", " + windowView[3] / (float) runMan.getImageResY());
                displayX = runMan.getImageResX();
                displayY = runMan.getImageResY();
                //runMan.getIO().setDisplayFramebufferScale(2f, 2f);
                runMan.savePlease = 2;
            }

            if (runMan.getCurrentSettlement() == null) {
                glClearColor(0.92f, 0.83f, 0.7f, 1.0f);
            } else {
                DrawColor back = runMan.getBackdropStyle().getColor();
                glClearColor(back.getRed(), back.getGreen(), back.getBlue(), 1.0f);
            }

            glClear(GL_COLOR_BUFFER_BIT);

            imGuiGlfw.newFrame();

            
            if (runMan.savePlease == 2) {
                runMan.getIO().setDisplaySize(displayX, displayY);
            }

            ImGui.newFrame();

            runMan.update();

            if (runMan.savePlease <= 0) {
                imGuiLayer.imgui();
            }

            ImGui.render();

            imGuiGl3.renderDrawData(ImGui.getDrawData());

//            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
//                ImGui.updatePlatformWindows();
//                ImGui.renderPlatformWindowsDefault();
//                GLFW.glfwMakeContextCurrent(backupWindowPtr);
//            }
            GLFW.glfwSwapBuffers(windowPtr);
            GLFW.glfwPollEvents();

            if (runMan.savePlease == 2) {
                GL33C.glReadBuffer(GL33C.GL_COLOR_ATTACHMENT0);
                FileManager.saveScreen(runMan.getImageResX(), runMan.getImageResY());
                GL33C.glBindFramebuffer(GL_FRAMEBUFFER, 0);
                GL33C.glViewport(windowView[0], windowView[1], windowView[2], windowView[3]);
                displayX = windowView[2];
                displayY = windowView[3];

                runMan.savePlease = 0;
            }

        }
    }

    public long getWindowPointer() {
        return this.windowPtr;
    }

    public int getProgram() {
        return this.program;
    }

}

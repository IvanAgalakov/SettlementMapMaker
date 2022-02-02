/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settlementmapmaker;

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
        this.imGuiLayer.initLayer(this, runMan);
        this.setGen = new SettlementGenerator();
//        Shape s = new Shape(new Point[]{
//            new Point(0f, 0f), new Point(1f, 0f),
//            new Point(0f, 1f),
//            new Point(1f, 0f), new Point(1f, 1f),
//            new Point(0f, 1f)
//        });

        //draw = new Shape[]{s,s.translateCopyShape(0.2f, 0), s.translateCopyShape(0.4f, 0),s.translateCopyShape(0.6f, 0),s.translateCopyShape(0.8f, 0)};
    }

    public void init() {
        this.initWindow();
        WindowVisualizer.WindowVisualizerInit();
        this.initImGui();

        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init();
        this.shadersInit();
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
        io = imgui.internal.ImGui.getIO();
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

            openGlRun();

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

    private boolean lastMiddleState = false;
    private float initMouseX = 0f, initMouseY = 0f;
    private float initCameraX = 0f, initCameraY = 0f;
    private float cameraX = 0f, cameraY = 0f;
    private ImGuiIO io;
    
    private float realMouseX = 0f;
    private float realMouseY = 0f;
    
    private float lastLeftPressDelta = 0f;

    private void openGlRun() {
        int[] width = new int[1], height = new int[1];
        GLFW.glfwGetWindowSize(windowPtr, width, height);
        
        GL33C.glUseProgram(program);

        

        if (io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)) {

            if (lastMiddleState == false) {
                initMouseX = io.getMousePosX();
                initMouseY = io.getMousePosY();
                initCameraX = cameraX;
                initCameraY = cameraY;
            }
            cameraX = initCameraX + (io.getMousePosX() - initMouseX);
            cameraY = initCameraY + (io.getMousePosY() - initMouseY);
            lastMiddleState = true;
        } else {
            lastMiddleState = false;
        }
        
        float normx = cameraX / width[0],
                normy = 1 - cameraY / (float) height[0];
        GL33C.glUniform2f(GL33C.glGetUniformLocation(program, "offset"), normx * 2 - 1, normy * 2 - 1);

        
        
        
        
        
        
        
        realMouseX = (((io.getMousePosX()/width[0])*2) - (normx * 2))/imGuiLayer.getZoom();
        realMouseY = -(((io.getMousePosY()/height[0])*2) - (normy * 2 - 2))/imGuiLayer.getZoom();
        
        if(imGuiLayer.getEditMode()) {
            editMode();
        }
        
        int mod = 0;
        if(imGuiLayer.getEditMode()) {
            mod = 1;
        }
        
        Point[] cur = new Point[currentShape.size()+mod];
        cur = currentShape.toArray(cur);
        
        if(imGuiLayer.getEditMode()) {
            cur[currentShape.size()] = new Point(realMouseX, realMouseY);
        }
        
        
        Shape s = new Shape(cur);
        
//        Shape s = new Shape( new Point[] {
//            new Point(1,0),
//            new Point(0,0),
//            new Point(0,1),
//            new Point(1,0),
//            new Point(2,3),
//            new Point(0.1f,2),
//            new Point(3,2),
//            new Point(1.2f,3f),
//            new Point(1,0),
//            new Point(2,0),
//            new Point(3,0),
//            new Point(4,0),
//            new Point(5,0),
//            new Point(6,0),
//            new Point(7,0),
//            new Point(8,0),
//            new Point(9,0),
//            new Point(10,0),
//        }
//        );
        
        draw = new Shape[]{s};
        if(imGuiLayer.renderBuildings()) {
            draw = setGen.convertToBlock(s, imGuiLayer.getMinimumBuildingSize(), imGuiLayer.getMaximumBuildingSize());
        }
        
        Shape[] scaledDraw = new Shape[draw.length];
        
        for (int i = 0; i < draw.length; i++) {
            scaledDraw[i] = draw[i].SimulateScaleAroundPoint(imGuiLayer.getZoom(), imGuiLayer.getZoom(), new Point(cameraX / (float) width[0], cameraY / (float) height[0]));
        }

        
        WindowVisualizer.drawEnclosedLines(scaledDraw, imGuiLayer.getLineWidth());
       
    }
    
    public void editMode() {
        if(io.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && lastLeftPressDelta <= 0) {
            this.currentShape.add(new Point(realMouseX, realMouseY));
            lastLeftPressDelta = 0.1f;
        } else {
            lastLeftPressDelta -= io.getDeltaTime();
        }
    }
    
    public void changeleftPressDelta(float time) {
        lastLeftPressDelta = 0.3f;
    }
    
    public long getWindowPointer() {
        return this.windowPtr;
    }

}

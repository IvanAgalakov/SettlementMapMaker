/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL33C;

/**
 *
 * @author 904187003
 */
public class ShaderManager {
    
    public static HashMap<ProgramNames, Integer> programs = new HashMap<>();
    
    public static enum ShaderNames {
        BASIC_FRAGMENT(new Shader(Shader.shaderTypes.FRAGMENT, "BasicFragmentShader.txt")),
        WATER_FRAGMENT(new Shader(Shader.shaderTypes.FRAGMENT, "WaterShader.txt")),
        BASIC_VERTEX(new Shader(Shader.shaderTypes.VERTEX, "BasicVertexShader.txt"));
        
        public final Shader SHADER;
        private ShaderNames(Shader shader) {
           SHADER = shader;
        }
    }
    
    public static enum ProgramNames {
        WATER,
        DEFAULT,
    }
    
    public static int programFromShaders(int... shaders) {
        int program = GL33C.glCreateProgram();
        for (int i = 0; i < shaders.length; i++) {
            GL33C.glAttachShader(program, shaders[i]);
        }
        GL33C.glLinkProgram(program);
        GL33C.glValidateProgram(program);
        for (int i = 0; i < shaders.length; i++) {
            GL33C.glDeleteShader(shaders[i]);
        }
        return program;
    }
    
    public static void makePrograms() {
        programs.put(ProgramNames.DEFAULT, programFromShaders(ShaderNames.BASIC_FRAGMENT.SHADER.getShader(), ShaderNames.BASIC_VERTEX.SHADER.getShader()));
        programs.put(ProgramNames.WATER, programFromShaders(ShaderNames.WATER_FRAGMENT.SHADER.getShader(), ShaderNames.BASIC_VERTEX.SHADER.getShader()));
    }
    
    public static int getProgram(ProgramNames name) {
        return programs.get(name);
    }
    
    public static ArrayList<Integer> getAllPrograms() {
        return new ArrayList(programs.values());
    }
    
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import org.lwjgl.opengl.GL33C;

/**
 *
 * @author 904187003
 */
public class ShaderManager {
    
    public static enum shaderNames {
        BASIC_FRAGMENT(new Shader(Shader.shaderTypes.FRAGMENT, "BasicFragmentShader.txt")),
        BASIC_VERTEX(new Shader(Shader.shaderTypes.VERTEX, "BasicVertexShader.txt"));
        
        public final Shader SHADER;
        private shaderNames(Shader shader) {
           SHADER = shader;
        }
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
    
}

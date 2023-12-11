/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import java.nio.ByteBuffer;
import java.util.Scanner;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author 904187003
 */
public class Shader {
    
    enum shaderTypes {
        FRAGMENT(GL30C.GL_FRAGMENT_SHADER), 
        VERTEX(GL30C.GL_VERTEX_SHADER);
        //COMPUTE(GL30C.gl_);
        //GEOMETRY(GL30C.gl_);

        public final int GLSHADER_VALUE;
        private shaderTypes(int value) {
           GLSHADER_VALUE = value;
        }
    }
    
    private int shader;
    
    public Shader(shaderTypes type, String path) {
        String code = "";
        System.out.println(path);
        Scanner sc = new Scanner(this.getClass().getResourceAsStream("/"+path));
        while(sc.hasNext()) {
            code += sc.nextLine() + "\n";
        }
        
        this.shader = compileShader(type.GLSHADER_VALUE, code);
    }
    
    public int getShader() {
        return this.shader;
    }
    
    
    private int compileShader(int type, String source) {
        int shader = GL30C.glCreateShader(type);
        GL30C.glShaderSource(shader, source);
        GL30C.glCompileShader(shader);
        int[] i = new int[1];
        GL30C.glGetShaderiv(shader, GL30C.GL_COMPILE_STATUS, i);
        if (i[0] == GL30C.GL_FALSE) {
            GL30C.glGetShaderiv(shader, GL30C.GL_INFO_LOG_LENGTH, i);
            ByteBuffer bb = BufferUtils.createByteBuffer(i[0]);
            GL30C.glGetShaderInfoLog(shader, i, bb);
            System.err.println("Shader compilation error:\n" + MemoryUtil.memASCII(bb));
        }
        return shader;
    }
    
}

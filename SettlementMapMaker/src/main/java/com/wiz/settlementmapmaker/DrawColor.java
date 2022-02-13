/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

/**
 *
 * @author Ivan
 */
public class DrawColor {
    private float[] color = new float[4];
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
    public DrawColor(float[] color) {
        this.color = color;
        r = this.color[0];
        g = this.color[1];
        b = this.color[2];
        a = this.color[3];
    }
    
    public DrawColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }
    
    public float[] getColor() {
        return this.color;
    }
    
}

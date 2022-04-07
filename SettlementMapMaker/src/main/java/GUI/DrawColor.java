/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Ivan
 */
public class DrawColor {
    private float[] color = new float[4];
    
    public DrawColor(float[] color) {
        this.color = color;
    }
    
    public DrawColor(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }
    
    public float[] getFloatOfColor() {
        return this.color;
    }
    
    public float getRed() {
        return color[0];
    }
    
    public float getGreen() {
        return color[1];
    }
    
    public float getBlue() {
        return color[2];
    }
    
    @Override
    public String toString() {
        return "(" + color[0] + ", " + color[1] + ", " + color[2] + ", " + color[3] + ")" ;
    }
    
}

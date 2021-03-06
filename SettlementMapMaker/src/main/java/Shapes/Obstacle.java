/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImLong;
import java.util.ArrayList;

/**
 *
 * @author 904187003
 */
public class Obstacle extends EditorShape{
    
    private final ImInt obstacleType = new ImInt();
    private final ImLong seed = new ImLong(0);
    private final ImFloat devMax = new ImFloat(0.02f);
    private final ImFloat devMin = new ImFloat(0.01f);
    private final ImFloat divisions = new ImFloat(0.05f);
    private final ImInt resolution = new ImInt(3);
    private final ImFloat thickness = new ImFloat(0.025f);
    private final ImFloat sectionDev = new ImFloat(0.01f);
    
    private final ImFloat wallEdgeThickness = new ImFloat(0.02f);
    private final ImFloat wallEdgeWidth = new ImFloat(0.01f);
    
    
    public Obstacle(String name) {
        super(name);
    }
    
    public ImInt getObstacleType() {
        return obstacleType;
    }
    
    public ImLong getSeed() {
        return seed;
    }
    
    public ImFloat getDevMin() {
        return devMin;
    }
    
    public ImFloat getDevMax() {
        return devMax;
    }
    
    public ImFloat getSectionDev() {
        return sectionDev;
    }
    
    public ImFloat getDivisions() {
        return divisions;
    }
    
    public ImFloat getThickness() {
        return thickness;
    }
    
    public ImInt getResolution() {
        return resolution;
    }
    
    public ImFloat getWallEdgeThickness() {
        return this.wallEdgeThickness;
    }
    
    public ImFloat getWallEdgeWidth() {
        return this.wallEdgeWidth;
    }
    
    
}

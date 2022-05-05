/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import imgui.type.ImFloat;
import imgui.type.ImInt;
import java.util.ArrayList;

/**
 *
 * @author 904187003
 */
public class Obstacle extends EditorShape{
    
    private final ImInt obstacleType = new ImInt();
    
    public Obstacle(String name) {
        super(name);
    }
    
    public ImInt getObstacleType() {
        return obstacleType;
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import java.util.ArrayList;

/**
 *
 * @author 904187003
 */
public class Building extends EditorShape {
    
    public Building(String name) {
        super(name);
    }

    public Building(EditorShape base) {
        super("");
        this.points = new ArrayList<>(base.getPointList());
    }
    
    public Building(ArrayList<Point> points) {
        super(points);
        setName("");
    }

    public void setName() {

    }

    public void generateName() {

    }

    public String getType() {
        return null;
    }

    public void setType(String type) {

    }
}

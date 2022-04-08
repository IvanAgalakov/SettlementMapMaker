/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;

/**
 *
 * @author 904187003
 */
public class EditorShape extends Shape {

    private ImString name = new ImString();
    private ImInt style = new ImInt(0);
    private ImBoolean showLabel = new ImBoolean(true);

    public EditorShape(String name) {
        super();
        this.name.set(name);
    }

    public EditorShape(EditorShape shape) {
        for (int i = 0; i < shape.getPoints().length; i++) {
            points.add(new Point(shape.getPoints()[i]));
        }
        CalculateCenter();
    }

    public EditorShape(Point[] points) {
        super(points);
    }

    public ImString getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setStyle(int style) {
        this.style.set(style);
    }

    public ImInt getStyle() {
        return this.style;
    }

    public ImBoolean getShowLabel() {
        return this.showLabel;
    }
    
    public int size() {
        return this.points.size();
    }
    
    public Point getPoint(int i) {
        return this.points.get(i);
    }

}

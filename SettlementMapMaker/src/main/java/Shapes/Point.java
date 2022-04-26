/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

/**
 *
 * @author 904187003
 */
public class Point {

    public float x;
    public float y;

    public static Point zero = new Point(0, 0);

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public void set(Point p) {
        this.y = p.y;
        this.x = p.x;
    }

    public void minClampX(float cX) {
        if (cX > x) {
            x = cX;
        }
    }

    public void maxClampX(float cX) {
        if (cX < x) {
            x = cX;
        }
    }

    public void minClampY(float cY) {
        if (cY > y) {
            y = cY;
        }
    }

    public void maxClampY(float cY) {
        if (cY < y) {
            y = cY;
        }
    }

    public float getDistanceToPoint(Point p) {
        return (float) Math.sqrt(Math.pow((this.x - p.x), 2) + Math.pow((this.y - p.y), 2));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public boolean equals(Point p) {
        if (p.x == this.x && p.y == this.y) {
            return true;
        }
        return false;
    }
}

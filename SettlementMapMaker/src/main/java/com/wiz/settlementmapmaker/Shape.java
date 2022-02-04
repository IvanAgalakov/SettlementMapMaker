/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

/**
 *
 * @author 904187003
 */
public class Shape {
    
    
    private Point[] points;
    private Point center;
    private Point topRight;
    private Point bottomLeft;
    
    public Shape(Point[] points) {
        this.points = points;
        this.CalculateCenter();
    }
    
    public Shape() {
        this.points = new Point[0];
    }
    
    public Shape(Shape s) {
        points = new Point[s.getPoints().length];
        for(int i = 0; i < s.getPoints().length; i++) {
            points[i] = new Point(s.getPoints()[i]);
        }
        this.CalculateCenter(); // change this to a copy as well, currently this is a lazy way to do it since calculation is already done in s
    }
    
    public Point[] getPoints() {
        return points;
    }
    
    public Point[] getEnclosedLinesFromPoints() {
        Point[] lines = new Point[points.length*2];
        for(int i = 0; i < lines.length; i++) {
            //System.out.println(i);
            if((int)Math.ceil(i/2.0) < points.length) {
                lines[i] = points[(int)Math.ceil(i/2.0)];
            } else {
                lines[i] = points[0];
            }
        }
        return lines;
    }
    
    public Point[] getTrianglesFromPoints() {
        return null;
    }
    
    public void addPoints(Point[] points) {
        
    }
    
    private void CalculateCenter() {
        float averageX = 0, averageY = 0;
        for(int i = 0; i < points.length; i++) {
            averageX += points[i].x;
            averageY += points[i].y;
        }
        center = new Point(averageX/(points.length), averageY/(points.length));
    }
    
    public Point getTopRight() {
        return topRight;
    }
    
    public Point getBottomLeft() {
        return bottomLeft;
    }
    
    public Point getCenter() {
        return center;
    }
    
    public void ScaleShape(float x, float y) {
        for(int i = 0; i < points.length; i++) {
            points[i].setX(center.x+((points[i].x-center.x)*x));
            points[i].setY(center.y+((points[i].y-center.y)*y));
        }
    }
    
    public void ScaleAroundPoint(float x, float y, Point p) {
        for(int i = 0; i < points.length; i++) {
            points[i].setX(p.x+((points[i].x-p.x)*x));
            points[i].setY(p.y+((points[i].y-p.y)*y));
        }
    }
    
    public Shape SimulateScaleAroundPoint(float x, float y, Point p) {
        Shape s = new Shape(this);
        for(int i = 0; i < s.getPoints().length; i++) {
            s.getPoints()[i].setX(p.x+((s.getPoints()[i].x-p.x)*x));
            s.getPoints()[i].setY(p.y+((s.getPoints()[i].y-p.y)*y));
        }
        return s;
    }
    
    public Shape translateCopyShape(float x, float y) {
        Shape copy = new Shape(this);
        for(int i = 0; i < copy.getPoints().length; i++) {
            copy.getPoints()[i].setX(copy.getPoints()[i].x+x);
            copy.getPoints()[i].setY(copy.getPoints()[i].y+y);
        }
        return copy;
    }
    
    public void SetDrawType(String type) {
        
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < points.length; i++) {
            s += "(" + points[i].x + ", " + points[i].y + ")\n";
        }
        return s;
    }
    
}

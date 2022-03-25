/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shape;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author 904187003
 */
public class Shape {
    
    
    private ArrayList<Point> points = new ArrayList();
    private Point center;
    private Point topRight;
    private Point bottomLeft;
    
    private float width;
    private float height;
    
    public Shape(Point[] points) {
        this.points.addAll(Arrays.asList(points));
        this.CalculateCenter();
    }
    
    public Shape(ArrayList<Point> points) {
        this.points.addAll(points);
        this.CalculateCenter();
    }
    
    public Shape() {
        this.points = new ArrayList<>();
    }
    
    public Shape(Shape s) {
        //points = new Point[s.getPoints().size()];
        for(int i = 0; i < s.getPoints().length; i++) {
            points.add(new Point(s.getPoints()[i]));
        }
        this.CalculateCenter(); // change this to a copy as well, currently this is a lazy way to do it since calculation is already done in s
    }
    
    public Point[] getPoints() {
        Point[] p = points.stream().map(s -> s).toArray(sz -> new Point[sz]);
        return p;
    }
    
    public ArrayList<Point> getPointList() {
        return points;
    }
    
    
    public Point[] getEnclosedLinesFromPoints() {
        Point[] lines = new Point[points.size()*2];
        for(int i = 0; i < lines.length; i++) {
            //System.out.println(i);
            if((int)Math.ceil(i/2.0) < points.size()) {
                lines[i] = points.get((int)Math.ceil(i/2.0));
            } else {
                lines[i] = points.get(0);
            }
        }
        return lines;
    }
    
    public Point[] getTrianglesFromPoints() {
        return null;
    }
    
    public void addPoints(Point... addedPoints) {
        //Point[] newPoints = Arrays.copyOf(points, points.size()+addPoints.length);
        for(int i = 0; i < addedPoints.length; i++) {
            points.add(addedPoints[i]);
        }
        this.CalculateCenter();
    }
    
    public void removePoints() {
        
    }
    
    public void CalculateCenter() {
        float averageX = 0, averageY = 0;
        Float bigX = null, bigY = null, smallX = null, smallY = null;
        for(int i = 0; i < points.size(); i++) {
            averageX += points.get(i).x;
            averageY += points.get(i).y;
            
            if(i == 0) {
               bigX = points.get(i).x;
               bigY = points.get(i).y;
               smallX = points.get(i).x;
               smallY = points.get(i).y;
            }
            
            if(points.get(i).x > bigX)
                bigX = points.get(i).x;
            if(points.get(i).y > bigY)
                bigY = points.get(i).y;
            
            if(points.get(i).x < smallX)
                smallX = points.get(i).x;
            if(points.get(i).y < smallY)
                smallY = points.get(i).y;
        }
        center = new Point(averageX/(points.size()), averageY/(points.size()));
        if(bigX != null) {
            topRight = new Point(bigX, bigY);
            bottomLeft = new Point(smallX, smallY);
        } else {
            topRight = new Point(center);
            bottomLeft = new Point(center);
        }
        
        width = Math.abs(bottomLeft.x) + Math.abs(topRight.x);
        height = Math.abs(bottomLeft.y) + Math.abs(topRight.y);
    }
    
    public Point getTopRight() {
        return topRight;
    }
    
    public Point getBottomLeft() {
        return bottomLeft;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public Point getCenter() {
        return center;
    }
    
    public void ScaleShape(float x, float y) {
        for(int i = 0; i < points.size(); i++) {
            points.get(i).setX(center.x+((points.get(i).x-center.x)*x));
            points.get(i).setY(center.y+((points.get(i).y-center.y)*y));
        }
    }
    
    public void ScaleAroundPoint(float x, float y, Point p) {
        for(int i = 0; i < points.size(); i++) {
            points.get(i).setX(p.x+((points.get(i).x-p.x)*x));
            points.get(i).setY(p.y+((points.get(i).y-p.y)*y));
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
    
    public String[] toStringArray() {
        String[] s = new String[points.size()];
        for(int i = 0; i < s.length; i++) {
            s[i] = points.get(i).toString();
        }
        return s;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < points.size(); i++) {
            s += "(" + points.get(i).x + ", " + points.get(i).y + ")\n";
        }
        return s;
    }
    
}

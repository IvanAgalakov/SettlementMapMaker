/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Utilities;

import Shapes.Line;
import Shapes.Point;
import imgui.ImVec4;

/**
 *
 * @author Ivan
 */
public class Utils {

    public static ImVec4 integerRGBAtoVec4(int r, int g, int b, int a) {
        return new ImVec4(r / 255f, g / 255f, b / 255f, a / 255f);
    }
    
    public static Point normalPointToPoint(Point p, float rise, float run, float deviate) {
        float hypo = (float) Math.sqrt((rise * rise) + (run * run));
        // System.out.println(p.toString() + " | " + -run + " | " + rise + " | " + hypo + " | " + deviate);

        return getPointAlongLine(p, -run, rise, hypo, deviate);
    }

    public static Point getPointAlongLine(Point start, float rise, float run, float hypo, float deviate) {
        float runSq = (float) Math.pow(run, 2);
        float riseSq = (float) Math.pow(rise, 2);
        float formula = (float) (Math.signum(deviate) * Math.sqrt(Math.pow(deviate, 2) / (runSq + riseSq)));

        return new Point(start.x + run * formula, start.y + rise * formula);
    }
    
    public static Point getPointAlongLine(Line line, float deviate) {
        Point start = line.getStart();
        float rise = line.getRise();
        float run = line.getRun();
        float hypo = line.getLength();
        return getPointAlongLine(start, rise, run, hypo, deviate);
    }

    public static Point calculateBorderPoint(Point start, Point middle, Point end) {
        float hypa = middle.getDistanceToPoint(start);
        Point a = getPointAlongLine(middle, start.y - middle.y, start.x - middle.x, hypa, hypa / 2);

        float hypb = middle.getDistanceToPoint(end);
        Point b = getPointAlongLine(middle, end.y - middle.y, end.x - middle.x, hypb, hypb / 2);

        float hypc = a.getDistanceToPoint(b);
        Point c = getPointAlongLine(a, b.y - a.y, b.x - a.x, hypc, hypc / 2);

        return c;
    }
    
    
    public static Point quadraticBezier(Point p0, Point p1, Point p2, float t) {
        Point finalPoint = new Point(0,0);
        finalPoint.setX((float)Math.pow(1-t, 2) * p0.x +
                (1-t) * 2 * t * p1.x +
                t * t * p2.x);
        finalPoint.setY((float)Math.pow(1-t, 2) * p0.y +
                (1-t) * 2 * t * p1.y +
                t * t * p2.y);
        
        return finalPoint;
    }
    
}

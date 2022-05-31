/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Utilities;

import Shapes.Line;
import Shapes.Point;
import imgui.ImVec4;
import java.util.List;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Ivan
 */
public class Utils {
    
    private static final DecimalFormat twoPlaceRound = new DecimalFormat("0.00");

    public static String roundToTwoPlaces(double d) {
        return twoPlaceRound.format(d);
    }
    
    public static ImVec4 integerRGBAtoVec4(int r, int g, int b, int a) {
        return new ImVec4(r / 255f, g / 255f, b / 255f, a / 255f);
    }
    
    public static Point normalPointToPoint(Point p, double rise, double run, double deviate) {
        double hypo = Math.sqrt((rise * rise) + (run * run));
        // System.out.println(p.toString() + " | " + -run + " | " + rise + " | " + hypo + " | " + deviate);

        return getPointAlongLine(p, -run, rise, hypo, deviate);
    }

    public static Point getPointAlongLine(Point start, double rise, double run, double hypo, double deviate) {
        double runSq = Math.pow(run, 2);
        double riseSq = Math.pow(rise, 2);
        double formula = (Math.signum(deviate) * Math.sqrt(Math.pow(deviate, 2) / (runSq + riseSq)));

        return new Point(start.x + run * formula, start.y + rise * formula);
    }
    
    public static Point getPointAlongLine(Line line, double deviate) {
        Point start = line.getStart();
        double rise = line.getRise();
        double run = line.getRun();
        double hypo = line.getLength();
        return getPointAlongLine(start, rise, run, hypo, deviate);
    }

    public static Point calculateBorderPoint(Point start, Point middle, Point end) {
        double hypa = middle.getDistanceToPoint(start);
        Point a = getPointAlongLine(middle, start.y - middle.y, start.x - middle.x, hypa, hypa / 2);

        double hypb = middle.getDistanceToPoint(end);
        Point b = getPointAlongLine(middle, end.y - middle.y, end.x - middle.x, hypb, hypb / 2);

        double hypc = a.getDistanceToPoint(b);
        Point c = getPointAlongLine(a, b.y - a.y, b.x - a.x, hypc, hypc / 2);

        return c;
    }
    
    
    public static Point quadraticBezier(Point p0, Point p1, Point p2, double t) {
        Point finalPoint = new Point(0,0);
        finalPoint.setX(Math.pow(1-t, 2) * p0.x +
                (1-t) * 2 * t * p1.x +
                t * t * p2.x);
        finalPoint.setY(Math.pow(1-t, 2) * p0.y +
                (1-t) * 2 * t * p1.y +
                t * t * p2.y);
        
        return finalPoint;
    }
    
    public static void addPointsToList(List l, Point... p) {
        for (int i = 0; i < p.length; i++) {
            l.add(p[i]);
        }
    }
    
}

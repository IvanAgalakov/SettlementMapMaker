/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shapes.Building;
import Shapes.EditorShape;
import Shapes.Line;

import Shapes.Point;
import Shapes.Zone;
import com.wiz.settlementmapmaker.Utilities.Utils;

import java.util.ArrayList;
import java.util.Random;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

/**
 *
 * @author Ivan
 */
public class SettlementGenerator {

    private static Random rand = new Random();

    public static ArrayList<Building> generateBlockThroughCutting(Building v) {
        if (v.size() < 3) {
            ArrayList<Building> base = new ArrayList<>();
            base.add(v);
            return base;
        }
        EditorShape copy = new EditorShape(v);
        ArrayList<Point> cutPoints = copy.getPointList();

        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 1; i <= cutPoints.size(); i++) {
            if (i < cutPoints.size()) {
                lines.add(new Line(cutPoints.get(i - 1), cutPoints.get(i)));
            } else {
                lines.add(new Line(cutPoints.get(i - 1), cutPoints.get(0)));
            }
        }

        // connects the lines together
        for (int i = 0; i < lines.size(); i++) {
            if (i + 1 != lines.size()) {
                lines.get(i).setNextLine(lines.get(i + 1));
            } else {
                lines.get(i).setNextLine(lines.get(0));
            }
        }

        
        int ranLine = lines.size() - 1;//rand.nextInt(lines.size());
        if (ranLine >= lines.size()) {
            ranLine = 0;
        }

        // noise so that buildings are not perfectly uniform
        float noise = rand.nextFloat(-0.2f, 0.2f);
        
        Line wL = lines.get(ranLine);
        Point tempStart = Utils.getPointAlongLine(wL.getStart(), wL.getRise(), wL.getRun(), wL.getLength(), wL.getLength() / (2f + noise));

        Point end = Utils.normalPointToPoint(tempStart, wL.getRise(), wL.getRun(), v.getPerimeter());
        Point start = Utils.normalPointToPoint(tempStart, wL.getRise(), wL.getRun(), -v.getPerimeter());

        Line cutLine = new Line(start, end);

        ArrayList<Point> interPoints = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            Point intersect = lines.get(i).getIntersection(cutLine);

            if (intersect != null) {
                Line insert = new Line(intersect, lines.get(i).getEnd());
                insert.setNextLine(lines.get(i).getNextLine());

                lines.add(i + 1, insert);
                lines.get(i).setEnd(intersect);
                lines.get(i).setNextLine(insert);
                i++;

                interPoints.add(intersect);
            }

        }

        // makes sure that any strange occurences are caught and not let out into the system
        if (interPoints.isEmpty()) {
            ArrayList<Building> base = new ArrayList<>();
            base.add(new Building(v));
            return base;
        }

        ArrayList<Building> blockShapes = new ArrayList<>();
        int intersectCount = 0;
        Point firstIntersection = null;
        ArrayList<Point> shapeProgress = new ArrayList<>();
        boolean done = false;

        Line currentLine = lines.get(0);

        int safety = 0;
        while (!done) {

            // System.out.println("lines: " + currentLine);
            if (interPoints.contains(currentLine.getStart())) {
                if (intersectCount == 0) {
                    firstIntersection = currentLine.getStart();
                    shapeProgress.add(new Point(currentLine.getStart()));
                }
                intersectCount++;
            }

            if (intersectCount == 2) {
                //System.out.println(shapeProgress.size());
                Building build = new Building(shapeProgress);
                build.setName("building");
                build.getShowLabel().set(false);
                blockShapes.add(build);
                shapeProgress.clear();

                shapeProgress.add(new Point(currentLine.getStart()));
                intersectCount = 1;

                if (firstIntersection == currentLine.getStart()) {
                    intersectCount = 0;
                    done = true;
                }
            }

            if (intersectCount == 1) {
                shapeProgress.add(new Point(currentLine.getEnd()));
            }

            currentLine = currentLine.getNextLine();
            safety++;
        }

        for (int i = 0; i < blockShapes.size(); i++) {
            blockShapes.get(i).squarizeShape();
        }
        return blockShapes;
    }
    
    public static ArrayList<Building> cutUpShape(ArrayList<Building> v, int times, float minPerimeter) {
        
        if (times <= 0) {
            return v;
        }
        ArrayList<Building> cutup = new ArrayList<>();
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).getPerimeter() >= minPerimeter) {
                //cutupIndex++;
                cutup.addAll(generateBlockThroughCutting(v.get(i)));
            } else {
                cutup.add(v.get(i));
            }
        }
        ArrayList<Building> ret = cutUpShape(cutup, times - 1, minPerimeter);
        
        return ret;
    }
    

    public static ArrayList<Building> getMultipleBlocks(Zone base) {
        ArrayList<Building> zones = new ArrayList<>();

        PowerDiagram diagram = new PowerDiagram();
        OpenList sites = new OpenList();

        PolygonSimple rootPolygon = new PolygonSimple();
        

        for (int i = 0; i < base.size(); i++) {
            rootPolygon.add(base.getPoint(i).x, base.getPoint(i).y);
        }

        double width = (base.getTopRight().x - base.getBottomLeft().x);
        double height = (base.getTopRight().y - base.getBottomLeft().y);
        double addx = base.getBottomLeft().x;
        double addy = base.getBottomLeft().y;
        // Open List for whatever reason doesn't keep the order in check properly :/
        ArrayList<Site> sitesInOrder = new ArrayList<>();
        for (int i = 0; i < base.getRegions().get(); i++) {
            Point place = new Point(addx + rand.nextDouble(width), addy + rand.nextDouble(height));
            Site site = new Site(place.x,place.y);
//            while (!base.contains(place)) {
//                site = new Site(place.x,place.y);
//                place.set(addx + rand.nextDouble(width), addy + rand.nextDouble(height));
//            }
            // too slow ):  ^^^^^^^^ add a decomposition of the shape into triangles for random placement within the triangles

            // we could also set a different weighting to some sites
            //site.setWeight(rand.nextDouble(10));
            sites.add(site);
            sitesInOrder.add(site);
        }

        diagram.setSites(sites);

        diagram.setClipPoly(rootPolygon);

        diagram.computeDiagram();

        for (int i = 0; i < sitesInOrder.size(); i++) {
            Site site = sitesInOrder.get(i);
            PolygonSimple polygon = site.getPolygon();
            if (polygon != null) {
                Building build = new Building(polygon);
                build.removeAllOfPoint(Point.zero);
                zones.add(build);
                //System.out.println(build.size());
            }
        }
        
        

        return zones;
    }

    public static EditorShape[] toShapeArray(EditorShape[][] ar) {
        int count = 0;
        for (int x = 0; x < ar.length; x++) {
            for (int y = 0; y < ar[x].length; y++) {
                count++;
            }
        }

        EditorShape[] s = new EditorShape[count];
        count = 0;
        for (int x = 0; x < ar.length; x++) {
            for (int y = 0; y < ar[x].length; y++) {
                s[count] = ar[x][y];
                count++;
            }
        }

        return s;
    }
    
    public static void setRandomSeed(long seed) {
        rand.setSeed(seed);
    }

}

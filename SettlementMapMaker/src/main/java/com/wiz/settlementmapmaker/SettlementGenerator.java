/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.Shape;
import Shape.Point;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

/**
 *
 * @author Ivan
 */
public class SettlementGenerator {

    public void runGeneration(Settlement settlement) {

    }

    public void alterGeneration(String property) {

    }

    public String generateBuildingName() {
        return null;
    }

    public String generateRiverName() {
        return null;
    }

    public String generateSettlementName() {
        return null;
    }

    public Shape[] convertToBlock(Shape blockBase, float minSize, float maxSize) {

        blockBase = new Shape(blockBase);
        blockBase.addPoints(new Point(blockBase.getPointList().get(0)));

        Random ran = new Random();
        ran.setSeed(1);

        ArrayList<Shape> block = new ArrayList<>();
        Point[] segments = blockBase.getPoints();

        float startDistance = 0f;

        for (int i = 1; i < segments.length; i++) {

            Point startSeg = segments[i - 1];
            Point endSeg = segments[i];

            float run = endSeg.x - startSeg.x;
            float rise = endSeg.y - startSeg.y;
            float hypo = startSeg.getDistanceToPoint(endSeg);

            boolean doneSegment = false;
            float distanceDownSegment = startDistance;

            int count = 0;
            while (!doneSegment) {
                // Shape newBuilding = new Shape(new Point[]{startSeg, endSeg, this.normalPointToPoint(endSeg, rise, run, 0.1f), this.normalPointToPoint(startSeg, rise, run, 0.1f), startSeg, endSeg});
                float deviate = minSize + (ran.nextFloat() * (maxSize - minSize));

                if (hypo - (deviate + distanceDownSegment) < minSize) {
                    deviate = hypo;
                }
                if (deviate + distanceDownSegment >= hypo) {
                    deviate = hypo - distanceDownSegment;
                    doneSegment = true;
                }

                Point begin;
                begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);

                Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);

                Point endInset = this.normalPointToPoint(end, rise, run, deviate);

                Point beginInset = this.normalPointToPoint(begin, rise, run, deviate);

                Shape newBuilding = new Shape(new Point[]{begin, end, endInset, beginInset});
                //Shape newBuilding = new Shape(new Point[]{begin, end, end, endInset, endInset, beginInset, beginInset, begin});
                //Shape newBuilding = new Shape(new Point[]{beginInset, endInset, begin, endInset, end, begin});
                //System.out.println(deviate + " : " + newBuilding.getCenter());
                //newBuilding.ScaleShape(0.9f, 0.9f);
                block.add(newBuilding);

                distanceDownSegment += deviate;

                if (doneSegment) {
                    if (i + 1 < segments.length) {
                        segments[i] = end;
                    }
                }

//                if (i - 1 == 0 && count == 0) {
//                    segments[segments.length - 1] = beginInset;
//                }
                count++;
            }

        }

        Shape[] blockArray = new Shape[block.size()];
        blockArray = block.toArray(blockArray);
        return blockArray;
    }

    public Shape generateVoronoi(Shape v) {
        PowerDiagram diagram = new PowerDiagram();
        OpenList sites = new OpenList();

        ArrayList<Point> points = v.getPointList();
        if (points.size() <= 2) {
            return new Shape(points);
        }

        PolygonSimple rootPolygon = new PolygonSimple();
        for (int i = 0; i < points.size(); i++) {
            rootPolygon.add(points.get(i).x, points.get(i).y);
        }

        Random rand = new Random(100);
        for (int i = 0; i < 20; i++) {
            if (v.getWidth() != 0 && v.getHeight() != 0) {
                Site site = new Site(v.getBottomLeft().x + rand.nextFloat(v.getWidth()), v.getBottomLeft().y + rand.nextFloat(v.getHeight()));
                sites.add(site);
            } else {
                return new Shape();
            }
            // we could also set a different weighting to some sites
            // site.setWeight(30)
        }

        diagram.setSites(sites);

        diagram.setClipPoly(rootPolygon);

        diagram.computeDiagram();

        ArrayList<Point> generatedShapePoints = new ArrayList();
        for (int i = 0; i < sites.size; i++) {
            Site site = sites.array[i];
            PolygonSimple polygon = site.getPolygon();

            // for each site we can no get the resulting polygon of its cell. 
            // note that the cell can also be empty, in this case there is no polygon for the corresponding site.
            if (polygon != null) {
                double[] xPoints = polygon.getXPoints();
                double[] yPoints = polygon.getYPoints();
                Point first = null;
                for (int a = 0; a < xPoints.length; a++) {
                    if (xPoints[a] != 0 || yPoints[a] != 0) {
                        generatedShapePoints.add(new Point((float) xPoints[a], (float) yPoints[a]));
                        if(first == null) {
                            first = new Point((float) xPoints[a], (float) yPoints[a]);
                        }
                    } else {
                        if(first != null) {
                            generatedShapePoints.add(first);
                        }
                    }
                }
            }
        }

//        for (Point p : generatedShapePoints) {
//            System.out.println(p.toString());
//        }
        return new Shape(generatedShapePoints);
    }

    public Point normalPointToPoint(Point p, float rise, float run, float deviate) {
        float hypo = (float) Math.sqrt((rise * rise) + (run * run));
        // System.out.println(p.toString() + " | " + -run + " | " + rise + " | " + hypo + " | " + deviate);

        return getPointAlongLine(p, -run, rise, hypo, deviate);
    }

    public Point getPointAlongLine(Point start, float rise, float run, float hypo, float deviate) {
        float runSq = (float) Math.pow(run, 2);
        float riseSq = (float) Math.pow(rise, 2);
        float formula = (float) (Math.signum(deviate) * Math.sqrt(Math.pow(deviate, 2) / (runSq + riseSq)));

        return new Point(start.x + run * formula, start.y + rise * formula);
    }

    public Point calculateBorderPoint(Point start, Point middle, Point end) {
        float hypa = middle.getDistanceToPoint(start);
        Point a = getPointAlongLine(middle, start.y - middle.y, start.x - middle.x, hypa, hypa / 2);

        float hypb = middle.getDistanceToPoint(end);
        Point b = getPointAlongLine(middle, end.y - middle.y, end.x - middle.x, hypb, hypb / 2);

        float hypc = a.getDistanceToPoint(b);
        Point c = getPointAlongLine(a, b.y - a.y, b.x - a.x, hypc, hypc / 2);

        return c;
    }

    public Shape[] toShapeArray(Shape[][] ar) {
        int count = 0;
        for (int x = 0; x < ar.length; x++) {
            for (int y = 0; y < ar[x].length; y++) {
                count++;
            }
        }

        Shape[] s = new Shape[count];
        count = 0;
        for (int x = 0; x < ar.length; x++) {
            for (int y = 0; y < ar[x].length; y++) {
                s[count] = ar[x][y];
                count++;
            }
        }

        return s;
    }

}

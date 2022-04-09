/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shapes.EditorShape;

import Shapes.Point;

import java.util.ArrayList;
import java.util.Random;

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

    public EditorShape[] convertToBlock(EditorShape blockBase, float minSize, float maxSize) {

        blockBase = new EditorShape(blockBase);
        blockBase.addPoints(new Point(blockBase.getPointList().get(0)));

        Random ran = new Random();
        ran.setSeed(1);

        ArrayList<EditorShape> block = new ArrayList<>();
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

                EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});
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

        EditorShape[] blockArray = new EditorShape[block.size()];
        blockArray = block.toArray(blockArray);
        return blockArray;
    }

    public ArrayList<EditorShape> generateSettlementBlock(EditorShape blockBase, float minSize, float maxSize) {
        if (blockBase.size() < 2) {
            return new ArrayList<>();
        }
        
        blockBase = new EditorShape(blockBase);
        blockBase.addPoints(new Point(blockBase.getPointList().get(0)));

        Random ran = new Random();
        ran.setSeed(1);

        ArrayList<EditorShape> block = new ArrayList<>();
        Point[] segments = blockBase.getPoints();

        float startDistance = 0f;
        float firstSegmentDistance = ran.nextFloat(minSize, maxSize);
        Point firstSegment = new Point(segments[0]);

        for (int i = 1; i < segments.length; i++) {

            Point startSeg = segments[i - 1];
            Point endSeg = segments[i];

            float run = endSeg.x - startSeg.x;
            float rise = endSeg.y - startSeg.y;
            float hypo = startSeg.getDistanceToPoint(endSeg);

            if (i == 1) {
                startSeg = this.getPointAlongLine(startSeg, rise, run, hypo, firstSegmentDistance);
                firstSegment = startSeg;
                run = endSeg.x - startSeg.x;
                rise = endSeg.y - startSeg.y;
                hypo = startSeg.getDistanceToPoint(endSeg);
            }
            
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

                    Point begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);
                    Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);
                    Point beginInset = this.normalPointToPoint(begin, rise, run, -deviate);

                    Point endInset;
                    if (i != segments.length - 1) {
                        run = segments[i + 1].x - segments[i].x;
                        rise = segments[i + 1].y - segments[i].y;
                        hypo = segments[i].getDistanceToPoint(segments[i + 1]);
                        endInset = this.getPointAlongLine(end, rise, run, hypo, deviate);
                        startDistance = deviate;
                    } else {
                        endInset = firstSegment;
                        beginInset = this.normalPointToPoint(begin, rise, run, -firstSegmentDistance);
                    }

                    EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});
                    block.add(newBuilding);

                    break;
                }

                Point begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);
                Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);
                Point endInset = this.normalPointToPoint(end, rise, run, -deviate);
                Point beginInset = this.normalPointToPoint(begin, rise, run, -deviate);

                EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});

                block.add(newBuilding);

                distanceDownSegment += deviate;

                count++;
            }

        }

        return block;
    }

    public ArrayList<EditorShape> generateVoronoi(EditorShape v) {

        if (v.getPointList().size() <= 2) {
            return new ArrayList();
        }

        //Voronoi voronoi = new Voronoi();
        return new ArrayList();
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

    public EditorShape[] toShapeArray(EditorShape[][] ar) {
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

}

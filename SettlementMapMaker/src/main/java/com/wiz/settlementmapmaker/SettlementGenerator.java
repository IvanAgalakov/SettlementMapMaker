/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shapes.EditorShape;
import Shapes.Line;

import Shapes.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class SettlementGenerator {
    
    private Random rand = new Random();
    
    public SettlementGenerator() {
        
    }

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

//    public EditorShape[] convertToBlock(EditorShape blockBase, float minSize, float maxSize) {
//
//        blockBase = new EditorShape(blockBase);
//        blockBase.addPoints(new Point(blockBase.getPointList().get(0)));
//
//        Random ran = new Random();
//        ran.setSeed(1);
//
//        ArrayList<EditorShape> block = new ArrayList<>();
//        Point[] segments = blockBase.getPoints();
//
//        float startDistance = 0f;
//
//        for (int i = 1; i < segments.length; i++) {
//
//            Point startSeg = segments[i - 1];
//            Point endSeg = segments[i];
//
//            float run = endSeg.x - startSeg.x;
//            float rise = endSeg.y - startSeg.y;
//            float hypo = startSeg.getDistanceToPoint(endSeg);
//
//            boolean doneSegment = false;
//            float distanceDownSegment = startDistance;
//
//            int count = 0;
//            while (!doneSegment) {
//                // Shape newBuilding = new Shape(new Point[]{startSeg, endSeg, this.normalPointToPoint(endSeg, rise, run, 0.1f), this.normalPointToPoint(startSeg, rise, run, 0.1f), startSeg, endSeg});
//                float deviate = minSize + (ran.nextFloat() * (maxSize - minSize));
//
//                if (hypo - (deviate + distanceDownSegment) < minSize) {
//                    deviate = hypo;
//                }
//                if (deviate + distanceDownSegment >= hypo) {
//                    deviate = hypo - distanceDownSegment;
//                    doneSegment = true;
//                }
//
//                Point begin;
//                begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);
//
//                Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);
//
//                Point endInset = this.normalPointToPoint(end, rise, run, deviate);
//
//                Point beginInset = this.normalPointToPoint(begin, rise, run, deviate);
//
//                EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});
//                //Shape newBuilding = new Shape(new Point[]{begin, end, end, endInset, endInset, beginInset, beginInset, begin});
//                //Shape newBuilding = new Shape(new Point[]{beginInset, endInset, begin, endInset, end, begin});
//                //System.out.println(deviate + " : " + newBuilding.getCenter());
//                //newBuilding.ScaleShape(0.9f, 0.9f);
//                block.add(newBuilding);
//
//                distanceDownSegment += deviate;
//
//                if (doneSegment) {
//                    if (i + 1 < segments.length) {
//                        segments[i] = end;
//                    }
//                }
//
////                if (i - 1 == 0 && count == 0) {
////                    segments[segments.length - 1] = beginInset;
////                }
//                count++;
//            }
//
//        }
//
//        EditorShape[] blockArray = new EditorShape[block.size()];
//        blockArray = block.toArray(blockArray);
//        return blockArray;
//    }

//    public ArrayList<EditorShape> generateSettlementBlock(EditorShape blockBase, float minSize, float maxSize) {
//        if (blockBase.size() < 2) {
//            return new ArrayList<>();
//        }
//
//        blockBase = new EditorShape(blockBase);
//        blockBase.addPoints(new Point(blockBase.getPointList().get(0)));
//
//        Random ran = new Random();
//        ran.setSeed(1);
//
//        ArrayList<EditorShape> block = new ArrayList<>();
//        Point[] segments = blockBase.getPoints();
//
//        float startDistance = 0f;
//        float firstSegmentDistance = ran.nextFloat(minSize, maxSize);
//        Point firstSegment = new Point(segments[0]);
//
//        for (int i = 1; i < segments.length; i++) {
//
//            Point startSeg = segments[i - 1];
//            Point endSeg = segments[i];
//
//            float run = endSeg.x - startSeg.x;
//            float rise = endSeg.y - startSeg.y;
//            float hypo = startSeg.getDistanceToPoint(endSeg);
//
//            if (i == 1) {
//                startSeg = this.getPointAlongLine(startSeg, rise, run, hypo, firstSegmentDistance);
//                firstSegment = startSeg;
//                run = endSeg.x - startSeg.x;
//                rise = endSeg.y - startSeg.y;
//                hypo = startSeg.getDistanceToPoint(endSeg);
//            }
//
//            boolean doneSegment = false;
//            float distanceDownSegment = startDistance;
//
//            int count = 0;
//            while (!doneSegment) {
//                // Shape newBuilding = new Shape(new Point[]{startSeg, endSeg, this.normalPointToPoint(endSeg, rise, run, 0.1f), this.normalPointToPoint(startSeg, rise, run, 0.1f), startSeg, endSeg});
//                float deviate = minSize + (ran.nextFloat() * (maxSize - minSize));
//
//                if (hypo - (deviate + distanceDownSegment) < minSize) {
//                    deviate = hypo;
//                }
//                if (deviate + distanceDownSegment >= hypo) {
//                    deviate = hypo - distanceDownSegment;
//
//                    Point begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);
//                    Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);
//                    Point beginInset = this.normalPointToPoint(begin, rise, run, -deviate);
//
//                    Point endInset;
//                    if (i != segments.length - 1) {
//                        run = segments[i + 1].x - segments[i].x;
//                        rise = segments[i + 1].y - segments[i].y;
//                        hypo = segments[i].getDistanceToPoint(segments[i + 1]);
//                        endInset = this.getPointAlongLine(end, rise, run, hypo, deviate);
//                        startDistance = deviate;
//                    } else {
//                        endInset = firstSegment;
//                        beginInset = this.normalPointToPoint(begin, rise, run, -firstSegmentDistance);
//                    }
//
//                    EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});
//                    block.add(newBuilding);
//
//                    break;
//                }
//
//                Point begin = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment);
//                Point end = this.getPointAlongLine(startSeg, rise, run, hypo, distanceDownSegment + deviate);
//                Point endInset = this.normalPointToPoint(end, rise, run, -deviate);
//                Point beginInset = this.normalPointToPoint(begin, rise, run, -deviate);
//
//                EditorShape newBuilding = new EditorShape(new Point[]{begin, end, endInset, beginInset});
//
//                block.add(newBuilding);
//
//                distanceDownSegment += deviate;
//
//                count++;
//            }
//
//        }
//
//        return block;
//    }

    public ArrayList<EditorShape> generateVoronoi(EditorShape v) {

        if (v.getPointList().size() <= 2) {
            return new ArrayList();
        }

        //Voronoi voronoi = new Voronoi();
        return new ArrayList();
    }

    public ArrayList<EditorShape> generateBlockThroughCutting(EditorShape v) {
        if (v.size() < 3) {
            ArrayList<EditorShape> base = new ArrayList<>();
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

        
        //rand.setSeed(0);
        
        int ranLine = 1;//rand.nextInt(lines.size());
        if (ranLine >= lines.size()) {
            ranLine = 0;
        }
        
        Line wL = lines.get(ranLine);
        Point tempStart = getPointAlongLine(wL.getStart(), wL.getRise(), wL.getRun(), wL.getLength(), wL.getLength() / 2f);

        Point end = normalPointToPoint(tempStart, wL.getRise(), wL.getRun(), v.getPerimeter());
        Point start = normalPointToPoint(tempStart, wL.getRise(), wL.getRun(), -v.getPerimeter());

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
            ArrayList<EditorShape> base = new ArrayList<>();
            base.add(v);
            return base;
        }
        
        
        
        
        
        ArrayList<EditorShape> blockShapes = new ArrayList<>();
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
                blockShapes.add(new EditorShape(shapeProgress));
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
            blockShapes.get(i).ScaleShape(0.99f, 0.99f);
        }
       

        return blockShapes;
    }
    
    public ArrayList<EditorShape> cutUpShape(ArrayList<EditorShape> v, int times) {
        if (times <= 0) {
            return v;
        }
        ArrayList<EditorShape> cutup = new ArrayList<>();
        for (int i = 0; i < v.size(); i++) {
            cutup.addAll(this.generateBlockThroughCutting(v.get(i)));
        }
        ArrayList<EditorShape> ret = cutUpShape(cutup, times-1);
        
        return ret;
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

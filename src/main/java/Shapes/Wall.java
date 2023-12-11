/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import com.wiz.settlementmapmaker.Utilities.Utils;
import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class Wall extends EditorShape {

    private Wall previous = null;
    private final Obstacle obs;
    private final Line myLine;
    private final double thickness;
    private final double edgeThick;
    private final double edgeWidth;
    private final float divisions;
    private ArrayList<EditorShape> wallSegments = new ArrayList();

    public Wall(Line line, Obstacle obs) {
        this.obs = obs;
        myLine = line;
        thickness = this.obs.getThickness().get();
        edgeThick = this.obs.getWallEdgeThickness().get();
        edgeWidth = this.obs.getWallEdgeWidth().get();
        divisions = obs.getDivisions().get();
        this.calculate();
    }

    public Wall(Line line, Obstacle obs, Wall previous) {
        this.obs = obs;
        this.previous = previous;
        myLine = line;
        thickness = this.obs.getThickness().get();
        edgeThick = this.obs.getWallEdgeThickness().get();
        edgeWidth = this.obs.getWallEdgeWidth().get();
        divisions = this.obs.getDivisions().get();
        this.calculate();
    }

    public final void calculate() {
        double fullLength = myLine.getLength();
        double partLength = divisions;
        double walk = 0;
        //System.out.println(fullLength);
        while (walk+partLength <= fullLength) {
            Line l = new Line(Utils.getPointAlongLine(myLine, walk), Utils.getPointAlongLine(myLine, walk+partLength));
            EditorShape wall = new EditorShape();
            wall.addPoints(l.getStart(), l.getEnd());
            wall.setVisualPoints(this.getWallPoints(l));
            wallSegments.add(wall);
            walk += partLength;
        }
        if (walk < fullLength) {
            Line l = new Line(Utils.getPointAlongLine(myLine, walk), Utils.getPointAlongLine(myLine, fullLength));
            EditorShape wall = new EditorShape();
            wall.addPoints(l.getStart(), l.getEnd());
            wall.setVisualPoints(this.getWallPoints(l));
            wallSegments.add(wall);
        }
    }

    public ArrayList<EditorShape> getWallSegments() {
        return this.wallSegments;
    }

    private ArrayList<Point> getWallPoints(Line l) {
        ArrayList<Point> points = new ArrayList<>();
        EditorShape nLine = new EditorShape(Utils.getPointAlongLine(l, edgeWidth), Utils.getPointAlongLine(l, l.getLength() - edgeWidth));
        nLine.calculateLinesFromPoints(this.thickness, false);
        points.addAll(nLine.getVisualPoints());

        points.addAll(this.getBoxAroundStartPoint(l));
        points.addAll(this.getBoxAroundStartPoint(l.reverse()));

        return points;
    }

    private ArrayList<Point> getBoxAroundStartPoint(Line l) {
        ArrayList<Point> points = new ArrayList();
        Point start = l.getStart();
        Point tLStart = Utils.normalPointToPoint(start, l.getRise(), l.getRun(), this.edgeThick);
        Point bLStart = Utils.normalPointToPoint(start, l.getRise(), l.getRun(), -this.edgeThick);;
        Point bRStart = Utils.getPointAlongLine(l, edgeWidth);
        bRStart = Utils.normalPointToPoint(bRStart, l.getRise(), l.getRun(), -this.edgeThick);
        Point tRStart = Utils.getPointAlongLine(l, edgeWidth);
        tRStart = Utils.normalPointToPoint(tRStart, l.getRise(), l.getRun(), this.edgeThick);

        Utils.addPointsToList(points, tLStart, bLStart, bRStart, bRStart, tLStart, tRStart);

        return points;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import com.wiz.settlementmapmaker.Utilities.Utils;
import imgui.type.ImInt;
import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class QuadBezierCurve extends EditorShape {

    private Point start;
    private Point end;
    private Point control;
    private ImInt divisions = new ImInt();
    private float thickness;

    private QuadBezierCurve previous = null;

    public QuadBezierCurve(Point start, Point end, Point control, int divisions, float thickness) {
        this.start = start;
        this.end = end;
        this.control = control;
        this.divisions.set(divisions);
        this.thickness = thickness;
        this.calculate();
    }

    public QuadBezierCurve(Point start, Point end, Point control, int divisions, float thickness, QuadBezierCurve previous) {
        this(start, end, control, divisions, thickness);
        this.previous = previous;
    }

    public void calculate() {
        this.points.clear();
        for (float i = 0; i <= 1f; i += 1f / divisions.get()) {
            this.points.add(Utils.quadraticBezier(start, control, end, i));
        }
        this.points.add(end);
    }

    @Override
    public void calculateTrianglesFromPoints() {
        
        ArrayList<Line> lines = this.getLines(false);

        Point lastBotRight = new Point(0, 0);
        Point lastTopRight = new Point(0, 0);
        if (previous != null) {
            ArrayList<Line> preLines = previous.getLines(false);
            Line preLine = preLines.get(preLines.size() - 2);
            lastBotRight = Utils.normalPointToPoint(preLine.getEnd(), preLine.getRise(), preLine.getRun(), -thickness / 2);
            lastTopRight = Utils.normalPointToPoint(preLine.getEnd(), preLine.getRise(), preLine.getRun(), thickness / 2);
        }

        for (int i = 0; i < lines.size(); i++) {
            Line curLine = lines.get(i);
            if (i == 0 && previous == null) {
                Point topLeft = Utils.normalPointToPoint(curLine.getStart(), curLine.getRise(), curLine.getRun(), thickness / 2);
                Point botLeft = Utils.normalPointToPoint(curLine.getStart(), curLine.getRise(), curLine.getRun(), -thickness / 2);
                Point topRight = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), thickness / 2);
                Point botRight = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), -thickness / 2);
                Utils.addPointsToList(this.visualPoints, topLeft, botLeft, botRight, botRight, topRight, topLeft);
                lastBotRight = botRight;
                lastTopRight = topRight;
            } else {
                Point topRight = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), thickness / 2);
                Point botRight = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), -thickness / 2);
                Utils.addPointsToList(this.visualPoints, lastTopRight, lastBotRight, botRight, botRight, topRight, lastTopRight);
                lastBotRight = botRight;
                lastTopRight = topRight;
            }
        }

        
    }

}

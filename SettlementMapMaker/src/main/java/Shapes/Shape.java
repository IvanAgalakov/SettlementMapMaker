/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import com.wiz.settlementmapmaker.SettlementGenerator;
import com.wiz.settlementmapmaker.Utilities.Utils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author 904187003
 */
public class Shape {

    protected ArrayList<Point> points = new ArrayList();
    protected ArrayList<Point> visualPoints = new ArrayList();

    private Point center;
    private Point topRight;
    private Point bottomLeft;

    private double width;
    private double height;

    public Shape(Point... points) {
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
        for (int i = 0; i < s.getPoints().length; i++) {
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

    public boolean hasSharedPoints(Shape shape, int number) {
        int count = 0;
        for (int i = 0; i < shape.getPointList().size(); i++) {
            for (int x = 0; x < this.points.size(); x++) {
                if (shape.getPointList().get(i).equals(this.points.get(x))) {
                    count++;
                }
            }
        }

        if (count >= number) {
            return true;
        }
        return false;
    }

    public void calculateLinesFromPoints(double thickness, boolean enclose) {
        visualPoints.clear();

        ArrayList<Line> lines = this.getLines(enclose);

        for (int i = 0; i < lines.size(); i++) {
            Line curLine = lines.get(i);
            Point topLeft = Utils.normalPointToPoint(curLine.getStart(), curLine.getRise(), curLine.getRun(), thickness / 2);
            Point botLeft = Utils.normalPointToPoint(curLine.getStart(), curLine.getRise(), curLine.getRun(), -thickness / 2);
            Point topR = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), thickness / 2);
            Point botRight = Utils.normalPointToPoint(curLine.getEnd(), curLine.getRise(), curLine.getRun(), -thickness / 2);
            Utils.addPointsToList(visualPoints, topLeft, botLeft, botRight, botRight, topR, topLeft);
        }
    }

    public void calculateDottedLinesFromPoints(double thickness, boolean enclose) {
        visualPoints.clear();

        ArrayList<Line> lines = this.getLines(enclose);

        double dis = thickness * 2;

        for (int i = 0; i < lines.size(); i++) {
            Line curLine = lines.get(i);
            double walk = 0;
            while (dis + walk < curLine.getLength()) {

                Point simStart = Utils.getPointAlongLine(curLine, walk);
                Point simEnd = Utils.getPointAlongLine(curLine, walk + dis);

                Point topLeft = Utils.normalPointToPoint(simStart, curLine.getRise(), curLine.getRun(), thickness / 2);
                Point botLeft = Utils.normalPointToPoint(simStart, curLine.getRise(), curLine.getRun(), -thickness / 2);
                Point topR = Utils.normalPointToPoint(simEnd, curLine.getRise(), curLine.getRun(), thickness / 2);
                Point botRight = Utils.normalPointToPoint(simEnd, curLine.getRise(), curLine.getRun(), -thickness / 2);
                Utils.addPointsToList(visualPoints, topLeft, botLeft, botRight, botRight, topR, topLeft);

                walk += dis * 2;
            }
        }
    }

    public void calculateGlLines(boolean enclose) {
        visualPoints.clear();

        int amount = points.size() * 2;
        for (int i = 0; i < amount; i++) {
            if ((int) Math.ceil(i / 2.0) < points.size()) {
                visualPoints.add(points.get((int) Math.ceil(i / 2.0)));
            } else {
                if (!enclose) {
                    visualPoints.add(points.get(points.size() - 1));
                } else {
                    visualPoints.add(points.get(0));
                }
            }
        }
    }

    public void calculateTrianglesFromPoints() {

        visualPoints.clear();

        if (points.size() < 3) {
            return;
        }

        int count = 0;
        for (int i = 0; i < points.size(); i++) {
            visualPoints.add(points.get(i));
            if (count == 2) {
                visualPoints.add(points.get(i));
                count = -1;
            }
            count++;
        }
        visualPoints.add(points.get(0));

    }

    public void addPoints(Point... addedPoints) {
        //Point[] newPoints = Arrays.copyOf(points, points.size()+addPoints.length);
        for (int i = 0; i < addedPoints.length; i++) {
            points.add(addedPoints[i]);
        }
        this.CalculateCenter();
    }

    public void removeAllOfPoint(Point p) {
        for (int i = points.size() - 1; i >= 0; i--) {
            if (points.get(i).equals(p)) {
                points.remove(i);
            }
        }
    }

    public void CalculateCenter() {
        double averageX = 0, averageY = 0;
        Double bigX = null, bigY = null, smallX = null, smallY = null;
        for (int i = 0; i < points.size(); i++) {
            averageX += points.get(i).x;
            averageY += points.get(i).y;

            if (i == 0) {
                bigX = points.get(i).x;
                bigY = points.get(i).y;
                smallX = points.get(i).x;
                smallY = points.get(i).y;
            }

            if (points.get(i).x > bigX) {
                bigX = points.get(i).x;
            }
            if (points.get(i).y > bigY) {
                bigY = points.get(i).y;
            }

            if (points.get(i).x < smallX) {
                smallX = points.get(i).x;
            }
            if (points.get(i).y < smallY) {
                smallY = points.get(i).y;
            }
        }
        center = new Point(averageX / (points.size()), averageY / (points.size()));
        if (bigX != null) {
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

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Point getCenter() {
        return center;
    }

    public void ScaleShape(double x, double y) {
        if (center == null) {
            this.CalculateCenter();
        }
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setX(center.x + ((points.get(i).x - center.x) * x));
            points.get(i).setY(center.y + ((points.get(i).y - center.y) * y));
        }
    }

    public void ScaleByNumber(double number) {
        if (center == null) {
            this.CalculateCenter();
        }
        for (int i = 0; i < points.size(); i++) {
            Line line = new Line(points.get(i), center);
            Point p = Utils.getPointAlongLine(line, number);

            points.get(i).set(p);
        }
    }

    public void ScaleAroundPoint(double x, double y, Point p) {
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setX(p.x + ((points.get(i).x - p.x) * x));
            points.get(i).setY(p.y + ((points.get(i).y - p.y) * y));
        }
    }

    public Shape SimulateScaleAroundPoint(double x, double y, Point p) {
        Shape s = new Shape(this);
        for (int i = 0; i < s.getPoints().length; i++) {
            s.getPoints()[i].setX(p.x + ((s.getPoints()[i].x - p.x) * x));
            s.getPoints()[i].setY(p.y + ((s.getPoints()[i].y - p.y) * y));
        }
        return s;
    }

    public Shape translateCopyShape(double x, double y) {
        Shape copy = new Shape(this);
        for (int i = 0; i < copy.getPoints().length; i++) {
            copy.getPoints()[i].setX(copy.getPoints()[i].x + x);
            copy.getPoints()[i].setY(copy.getPoints()[i].y + y);
        }
        return copy;
    }

    public void translate(double x, double y) {
        this.CalculateCenter();
        Point translation = new Point(x, y);

        ArrayList<Point> doNotTranslate = new ArrayList();
        for (int i = 0; i < this.points.size(); i++) {
            if (!doNotTranslate.contains(this.points.get(i))) {
                this.points.get(i).add(translation);
                doNotTranslate.add(this.points.get(i));
            }
        }
        for (int i = 0; i < this.visualPoints.size(); i++) {
            if (!doNotTranslate.contains(this.visualPoints.get(i))) {
                this.visualPoints.get(i).add(translation);
                doNotTranslate.add(this.visualPoints.get(i));
            }
        }
    }

    public void moveCenterTo(Point p) {
        this.CalculateCenter();
        double xChange = p.x - this.center.x;
        double yChange = p.y - this.center.y;

        Point change = new Point(xChange, yChange);
        for (int i = 0; i < this.points.size(); i++) {
            this.points.get(i).add(change);
        }
    }

    public String[] toStringArray() {
        String[] s = new String[points.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = points.get(i).toString();
        }
        return s;
    }

    public boolean contains(Point test) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).y > test.y) != (points.get(j).y > test.y)
                    && (test.x < (points.get(j).x - points.get(i).x) * (test.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    public double getPerimeter() {
        Point last = null;
        double perimeter = 0;
        for (Point p : points) {
            if (last != null) {
                perimeter += p.getDistanceToPoint(last);
            } else {
                perimeter += p.getDistanceToPoint(points.get(points.size() - 1));
            }
            last = p;
        }
        return perimeter;
    }

    public double getFootprint() {
        if (topRight == null || bottomLeft == null) {
            this.CalculateCenter();
        }
        return (topRight.y - bottomLeft.y) * (topRight.x - bottomLeft.x);
    }

    public double getSmallestSide() {
        ArrayList<Line> lines = this.getLines(true);
        double f = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                f = lines.get(i).getLength();
            } else if (lines.get(i).getLength() < f) {
                f = lines.get(i).getLength();
            }
        }
        return f;
    }

    public boolean isPointInside(Point p) {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 1; i <= points.size(); i++) {
            if (i < points.size()) {
                lines.add(new Line(points.get(i - 1), points.get(i)));
            } else {
                lines.add(new Line(points.get(i - 1), points.get(0)));
            }
        }

        Line testLine = new Line(p, new Point(p.x - this.getPerimeter(), p.y - this.getPerimeter()));
        
        int numberOfIntersections = 0;
        for (int i = 0; i < lines.size(); i++) {
            Point inter = lines.get(i).getIntersection(testLine);
            if (inter != null) {
                if (inter.x > p.x) {
                    if (p.x < this.topRight.x && p.y < this.topRight.y && p.x > this.bottomLeft.y && p.y > this.bottomLeft.y) {
                        numberOfIntersections++;
                    }
                }
            }
        }

//        if (numberOfIntersections != 0) {
//            System.out.println(numberOfIntersections + "   \n" + this + "\nPoint: " + p);
//        }
        return numberOfIntersections%2 == 1;
    }

    public boolean hasPointsInside(Shape s) {
        for (int i = 0; i < this.points.size(); i++) {
            if (s.isPointInside(this.points.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean overlaps(Shape s) {
        ArrayList<Line> myLines = this.getLines(true);
        ArrayList<Line> theirLines = s.getLines(true);
        for (int i = 0; i < myLines.size(); i++) {
            for (int a = 0; a < theirLines.size(); a++) {
                Point p = myLines.get(i).getIntersection(theirLines.get(a));
                if (p != null) {
                    if (myLines.get(i).isPointOnLine(p) && theirLines.get(a).isPointOnLine(p)) {
                        return true;
                    }
                }
            }
        }
        
        if (s.hasPointsInside(this) || this.hasPointsInside(s)) {
            return true;
        }
        
        return false;
    }

    public ArrayList<Line> getLines(boolean reconnect) {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 1; i <= points.size(); i++) {
            if (i < points.size()) {
                lines.add(new Line(points.get(i - 1), points.get(i)));
            } else if (reconnect) {
                lines.add(new Line(points.get(i - 1), points.get(0)));
            }
        }

        // connects the lines together
        for (int i = 0; i < lines.size(); i++) {
            if (i + 1 != lines.size()) {
                lines.get(i).setNextLine(lines.get(i + 1));
            } else if (reconnect) {
                lines.get(i).setNextLine(lines.get(0));
            }
        }
        return lines;
    }

    public void calculatePointsAsPoints() {
        visualPoints.clear();
        visualPoints.addAll(points);
    }

    public ArrayList<Point> getVisualPoints() {
        return this.visualPoints;
    }

    public Point getLastPoint() {
        return this.points.get(this.points.size() - 1);
    }

    public void squarizeShape() {
        if (this.points.size() != 3) {
            return;
        }
        ArrayList<Line> lines = getLines(true);
        Line longest = null;
        for (Line l : lines) {
            if (longest == null) {
                longest = l;
            } else if (l.getLength() > longest.getLength()) {
                longest = l;
            }
        }
        if (longest == null) {
            return;
        }

        Point vert = Utils.getPointAlongLine(longest, longest.getLength() / 2f);

        Line before = null;
        Line after = null;
        Line afterVert = null;
        Line beforeVert = null;
        for (Line l : lines) {
            if (l == longest) {
                Point p = Utils.getPointAlongLine(l.getNextLine(), l.getNextLine().getLength() / 2f);
                l.getNextLine().setStart(p);
                after = l.getNextLine();
                afterVert = new Line(vert, p);
                afterVert.setNextLine(l.getNextLine());
            }
            if (l.hasNextLine()) {
                if (l.getNextLine() == longest) {
                    Point p = Utils.getPointAlongLine(l, l.getLength() / 2f);
                    l.setEnd(p);
                    before = l;

                    beforeVert = new Line(p, vert);
                }
            }
        }

        if (beforeVert == null || afterVert == null || after == null || before == null) {
            return;
        }

        beforeVert.setNextLine(afterVert);
        before.setNextLine(beforeVert);
        afterVert.setNextLine(after);

        Line selected = beforeVert;
        ArrayList<Point> points = new ArrayList();

        while (selected != before) {
            points.add(selected.getStart());
            selected = selected.getNextLine();
        }
        points.add(selected.getStart());

        this.points = points;
    }

    /*
    given p[k], p[k+1], p[k+2] each with coordinates x, y:
    dx1 = x[k+1]-x[k]
    dy1 = y[k+1]-y[k]
    dx2 = x[k+2]-x[k+1]
    dy2 = y[k+2]-y[k+1]
    zcrossproduct = dx1*dy2 - dy1*dx2
     */
    public boolean isConvex() {
        int sign = 0;
        for (int i = 0; i <= this.points.size(); i++) {
            int place1 = i;
            int place2 = i + 1;
            int place3 = i + 2;

            if (place1 == points.size()) {
                place1 = 0;
            }
            while (place2 >= this.points.size()) {
                place2 -= this.points.size();
            }
            while (place3 >= this.points.size()) {
                place3 -= this.points.size();
            }

            double dx1 = this.points.get(place2).x - this.points.get(place1).x;
            double dy1 = this.points.get(place2).y - this.points.get(place1).y;
            double dx2 = this.points.get(place3).x - this.points.get(place2).x;
            double dy2 = this.points.get(place3).y - this.points.get(place2).y;

            double zcrossproduct = dx1 * dy2 - dy1 * dx2;

            if (sign == 0) {
                sign = (int) Math.signum(zcrossproduct);
            } else if (sign != (int) Math.signum(zcrossproduct)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < points.size(); i++) {
            s += "(" + points.get(i).x + ", " + points.get(i).y + ")\n";
        }
        return s;
    }

    public void clear() {
        this.points.clear();
    }

}

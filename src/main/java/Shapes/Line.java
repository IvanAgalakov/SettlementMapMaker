/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

/**
 *
 * @author Ivan
 */
public class Line {

    private Point start;
    private Point end;
    private Line nextLine;

    public Line(Point start, Point end) {
        this.end = end;
        this.start = start;
        this.nextLine = null;
    }
    
    public void setNextLine(Line line) {
        this.nextLine = line;
    }
    
    public Line getNextLine() {
        return this.nextLine;
    }
    
    public boolean hasNextLine() {
        return nextLine != null;
    }

    public double getRise() {
        return end.y - start.y;
    }

    public double getRun() {
        return end.x - start.x;
    }

    public double getLength() {
        return start.getDistanceToPoint(end);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
    
    public void setStart(Point p) {
        start = p;
    }
    
    public void setEnd(Point p) {
        end = p;
    }
    
    public Double getSlope() {
        if(getRun() != 0) {
            return (getRise()/getRun());
        } else {
            return null;
        }
    }
    
    public Double getY1() {
        if(this.getSlope() == null) {
            return null;
        }
        
        return start.y - this.getSlope()*start.x;
    }

//    public Point getIntersection(Line line) {
//        
//        if (line.getSlope() == null && this.getSlope() != null) {
//            return new Point(line.getStart().x, this.getSlope()*line.getStart().x + this.getY1());
//        } else if (line.getSlope() != null && this.getSlope() != null) {
//            return new Point(this.getStart().x, line.getSlope()*this.getStart().x + line.getY1());
//        } else if (line.getSlope() == null && this.getSlope() == null){
//            return null;
//        }
//        
//        if (line.getSlope().equals(this.getSlope())) {
//            return null;
//        }
//        
//        double x = (line.getY1() - this.getY1()) / (this.getSlope() - line.getSlope());
//        double y = this.getSlope() * x + this.getY1();
//        
//        return new Point(x,y);
//    }
    
    public Point getIntersection(Line line) {
        //System.out.println("value of : " + line);
        double a1 = this.end.y - this.start.y;
        double b1 = this.start.x - this.end.x;
        double c1 = a1 * this.start.x + b1 * this.start.y;
 
        double a2 = line.end.y - line.start.y;
        double b2 = line.start.x - line.end.x;
        double c2 = a2 * line.start.x + b2 * line.start.y;
 
        double delta = a1 * b2 - a2 * b1;
        //System.out.println("a1 : " + a1 + " b1 : " + b1 + " c1 : " + c1 + " a2 : " + a2 + " b2 : " + b2 + " c2 : " + c2 + " delta : " + delta );
        
        Point inter = new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
        
        
        
        if (!this.isPointOnLine(inter)) {
            inter = null;
        }
        
        
        
        return inter;
    }
    
    public boolean isPointOnLine(Point p) {
        return Math.abs(p.getDistanceToPoint(start) + p.getDistanceToPoint(end) - this.getLength()) < 0.0001f;
    }
    
    public Boolean isPointAbove (Point point) {
        
        if (this.getSlope().equals(null)) {
            if(this.getStart().x > point.x) {
                return true;
            } else if (this.getStart().x < point.x) {
                return false;
            } else {
                return null;
            }
        }
        
        double y = this.getSlope() * point.x + this.getY1();
        
        if (y < point.y) {
            return true;
        } else if (y > point.y) {
            return false;
        } else {
            return null;
        }
    }
    
    public Line reverse() {
        return new Line(new Point(this.end), new Point(this.start));
    }
    
    
    @Override
    public String toString() {
        return start + "|" + end;
    }

}

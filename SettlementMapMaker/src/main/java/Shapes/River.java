/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import com.wiz.settlementmapmaker.Constants;
import com.wiz.settlementmapmaker.Utilities.Utils;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class River {

    private ArrayList<QuadBezierCurve> curves = new ArrayList();
    private final float divisions;
    private final float devMin, devMax;
    private final Line line;
    private final float sectionDev;
    private River previous = null;
    private final long seed;
    private final int resolution;
    private final float thickness;
    
    private int sign = 1;

    public River(Line line, Obstacle obs) {
        this.line = line;
        this.divisions = obs.getDivisions().get();
        this.devMin = obs.getDevMin().get();
        this.devMax = obs.getDevMax().get();
        this.sectionDev = obs.getSectionDev().get();
        this.seed = obs.getSeed().get();
        this.resolution = obs.getResolution().get();
        this.thickness = obs.getThickness().get();
        this.calculate();
    }

    public River(Line line, Obstacle obs, River previous) {
        this.line = line;
        this.divisions = obs.getDivisions().get();
        this.devMin = obs.getDevMin().get();
        this.devMax = obs.getDevMax().get();
        this.sectionDev = obs.getSectionDev().get();
        this.seed = obs.getSeed().get();
        this.resolution = obs.getResolution().get();
        this.thickness = obs.getThickness().get();
        this.previous = previous;
        
        if (previous != null) {
            this.line.setStart(previous.getCurve(previous.getCurves().size()-1).getLastPoint());
            this.sign = previous.getSign();
        }
        
        this.calculate();
    }

    public final void calculate() {
        curves.clear();
        Random rand = new Random();
        rand.setSeed(seed);

        float step = divisions;

        
        QuadBezierCurve prev = null;
        if (previous != null) {
            prev = previous.getCurve(previous.getLength() - 1);
        }
        
        float walk = 0;
        float walkEnd = line.getLength();
        while (walk < walkEnd) {
            Point first = Utils.getPointAlongLine(line, walk);
            float addToWalk = step + rand.nextFloat(-sectionDev, sectionDev);
            Point second = Utils.getPointAlongLine(line, walk + addToWalk);

            walk += addToWalk;

            Line l = new Line(first, second);
            Point mid = Utils.getPointAlongLine(l, l.getLength() / 2);
            Point control = Utils.normalPointToPoint(mid, l.getRise(), l.getRun(), sign * rand.nextFloat(devMin, devMax));

            QuadBezierCurve toAdd = new QuadBezierCurve(first, second, control, resolution, thickness, prev);
            toAdd.calculateTrianglesFromPoints();
            curves.add(toAdd);
            prev = toAdd;
            sign *= -1;
        }
    }
    
    public int getSign() {
        return this.sign;
    }

    public ArrayList<EditorShape> getCurves() {
        ArrayList<EditorShape> shapes = new ArrayList();
        for (QuadBezierCurve b : curves) {
            shapes.add(b);
        }
        return shapes;
    }

    public QuadBezierCurve getCurve(int i) {
        return curves.get(i);
    }

    public int getLength() {
        return curves.size();
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import com.wiz.settlementmapmaker.Utilities.Utils;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class River {

    private ArrayList<QuadBezierCurve> curves = new ArrayList();
    private int divisions;
    private float devMin, devMax;
    private Line line;
    private float sectionDev;

    public River(Line line, int divisions, float devMin, float devMax, float sectionDev) {
        this.line = line;
        this.divisions = divisions;
        this.devMin = devMin;
        this.devMax = devMax;
        this.sectionDev = sectionDev;
        this.calculate();
    }

    public final void calculate() {
        curves.clear();
        Random rand = new Random();
        rand.setSeed(0);
        
        float step = line.getLength()/divisions;
        
        int sign = 1;
        QuadBezierCurve prev = null;
        float walk = 0;
        for (int i = 1; i <= divisions; i++) {
            Point first = Utils.getPointAlongLine(line, walk);
            float addToWalk = step+rand.nextFloat(-sectionDev, sectionDev);
            Point second = Utils.getPointAlongLine(line, walk+addToWalk);
            
            walk += addToWalk;
            
            Line l = new Line(first, second);
            Point mid = Utils.getPointAlongLine(l, l.getLength()/2);
            Point control = Utils.normalPointToPoint(mid, l.getRise(), l.getRun(), sign*rand.nextFloat(devMin, devMax));
            
            QuadBezierCurve toAdd = new QuadBezierCurve(first, second, control, 20, 0.04f, prev);
            curves.add(toAdd);
            prev = toAdd;
            sign *= -1;
        }
    }
    
    public ArrayList<EditorShape> getCurves() {
        ArrayList<EditorShape> shapes = new ArrayList();
        for (QuadBezierCurve b : curves) {
            shapes.add(b);
        }
        return shapes;
    }

}

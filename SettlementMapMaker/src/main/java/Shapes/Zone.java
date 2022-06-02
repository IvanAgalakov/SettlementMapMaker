/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImLong;
import java.util.ArrayList;
import kn.uni.voronoitreemap.j2d.PolygonSimple;

/**
 *
 * @author 904187003
 */
public class Zone extends EditorShape {

    private ImInt zoneType;
    private ImInt divisions = new ImInt(1);
    private ImInt regions = new ImInt(4);
    private ImFloat minPerimeter = new ImFloat(0.01f);
    private ImFloat minSideSize = new ImFloat(0.001f);
    private final ArrayList<Building> containedShapes = new ArrayList<Building>();
    private ImInt selectedContainedBuilding = new ImInt();
    private ImFloat roadSize = new ImFloat(0.01f);
    private long hiddenSeed = 0;

    public Zone(String name, int zoneType) {
        super(name);
        this.zoneType = new ImInt(zoneType);
    }
    
    public Zone() {
        super("");
    }

    public void setZoneType(String type) {
        
    }
    
    public long getHiddenSeed() {
        return hiddenSeed;
    }
    
    public void setHiddenSeed(long seed) {
        hiddenSeed = seed;
    }

    public ImInt getZoneType() {
        return zoneType;
    }
    
    public int getDivisions() {
        return divisions.get();
    }
    
    public ImInt getRegions() {
        return regions;
    }
    
    public float getMinPerimeter() {
        return minPerimeter.get();
    }
    
    public float getMinSideLength() {
        return minSideSize.get();
    }
    
    public int[] getDivisionData() {
        return divisions.getData();
    }
    
    public float[] getMinPerimeterData() {
        return minPerimeter.getData();
    }
    
    public float[] getMinSideLengthData() {
        return minSideSize.getData();
    }
    
    public ArrayList<Building> getContainedShapes() {
        return containedShapes;
    }
    
    public void addBuildings(ArrayList<Building> shapes) {
        containedShapes.addAll(shapes);
    }
    
    public void clearContainedShapes() {
        containedShapes.clear();
    }
    
    public ImInt getSelectedContainedBuilding() {
        return this.selectedContainedBuilding;
    }
    
    public String[] getContainedBuildingNames() {
        String[] s = new String[this.containedShapes.size()];
        for (int i = 0; i < this.containedShapes.size(); i++) {
            s[i] = containedShapes.get(i).getName().get();
        }
        return s;
    }
    
    public ImFloat getRoadSize() {
        return roadSize;
    }
}

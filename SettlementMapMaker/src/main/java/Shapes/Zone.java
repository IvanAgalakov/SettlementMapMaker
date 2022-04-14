/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shapes;

import imgui.type.ImInt;
import imgui.type.ImLong;

/**
 *
 * @author 904187003
 */
public class Zone extends EditorShape {

    private ImInt zoneType;
    private ImInt divisions = new ImInt(1);

    public Zone(String name, int zoneType) {
        super(name);
        this.zoneType = new ImInt(zoneType);
    }

    public void setZoneType(String type) {
        
    }

    public ImInt getZoneType() {
        return zoneType;
    }
    
    public int getDivisions() {
        return divisions.get();
    }
    
    public int[] getDivisionData() {
        return divisions.getData();
    }
}

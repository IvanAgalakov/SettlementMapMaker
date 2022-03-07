/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

/**
 *
 * @author 904187003
 */
public class Zone extends EditorShape {

    private String type;

    public Zone(String name, ZoneType type) {
        super(name);
        this.type = type.ZONE_TYPE;
    }

    public void setZoneType(String type) {
        
    }

    public String getZoneType() {
        return type;
    }

    public static enum ZoneType {
        BUILDING_GENERATION("buildingGen"),
        STOP_GENERATION("genBlock");

        public final String ZONE_TYPE;

        private ZoneType(String zoneType) {
            ZONE_TYPE = zoneType;
        }
    }
}

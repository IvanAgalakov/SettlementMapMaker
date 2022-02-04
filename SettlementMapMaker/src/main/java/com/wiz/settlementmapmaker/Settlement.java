/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

/**
 *
 * @author Ivan
 */
public class Settlement {
    
    private String name = "";
    
    private Zone[] cityZones;
    private Building[] cityBuildings;
    private Building[] generatedBuildings;
    private Obstacle[] cityObstacles;
    
    public Settlement(String name) {
        
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Building[] getBuildings() {
        return null;
    }
    
    public void addBuildings(Building[] buildings) {
        
    }
    
    public Zone[] getZones() {
        return cityZones;
    }
    
    public void addZones(Zone[] zones) {
        
    }
    
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.ImColor;
import java.util.ArrayList;
import java.util.Hashtable;

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

    private DrawColor defaultStyle = new DrawColor(0,0,0,1);
    private Hashtable<String, DrawColor> style = new Hashtable<>();
    private ArrayList<String> cityStyles = new ArrayList<>();

    public Settlement(String name) {
        style.put("Zone Color", new DrawColor(0.549f, 0.784f, 0.949f, 1));
        cityStyles.add("Zone Color");
        style.put("Placed Building Color", new DrawColor(0.549f, 0.784f, 0.949f, 1));
        cityStyles.add("Placed Building Color");
        style.put("Generated Building Color", new DrawColor(0.549f, 0.784f, 0.949f, 1));
        cityStyles.add("Generated Building Color");
        style.put("Obstacle Color", new DrawColor(0.549f, 0.784f, 0.949f, 1));
        cityStyles.add("Obstacle Color");

        this.name = name;
    }
    
    public float[] getStyle(String key) {
        return style.get(key).getColor();
    }
    
    public DrawColor getDefaultStyle() {
        return this.defaultStyle;
    }
    
    public ArrayList<String> getCityStyles() {
        return this.cityStyles;
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
    
    public void addStyle(String s) {
        if(!cityStyles.contains(s)) {
            cityStyles.add(s);
            style.put(s, new DrawColor(1,1,1,1));
        }
    }
    
    public void removeStyle(String s) {
        if(cityStyles.contains(s)) {
            cityStyles.remove(s);
            style.remove(s);
        }
    }

}

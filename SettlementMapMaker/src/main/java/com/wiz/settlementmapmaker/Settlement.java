/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Zone;
import Shape.Obstacle;
import Shape.Building;
import imgui.ImColor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class Settlement {

    private String name = "";
    private Hashtable<String, ArrayList<EditorShape>> cityShapes = new Hashtable<>();

    private Style defaultStyle = new Style(new DrawColor(0,0,0,1), 0);
    private Hashtable<String, Style> style = new Hashtable<>();
    

    private ArrayList<String> cityStyles = new ArrayList<>();

    public Settlement(String name) {
        style.put("Zone Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Zone Color");
        style.put("Placed Building Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Placed Building Color");
        style.put("Generated Building Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Generated Building Color");
        style.put("Obstacle Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Obstacle Color");
        
        for(int i = 0; i < Constants.CITY_SHAPE_TYPES.length; i++) {
            cityShapes.put(Constants.CITY_SHAPE_TYPES[i], new ArrayList());
        }

        this.name = name;
    }
    
    public float[] getColor(String key) {
        return style.get(key).getColor().getFloatOfColor();
    }
    
    public Style getStyle(String key) {
        return style.get(key);
    }
    
    public Style getDefaultStyle() {
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
    
    public void fillHash(Hashtable<String, String[]> hash) {
        ArrayList<Zone> zones = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Obstacle> Obstacles = new ArrayList<>();
    }

    public List<EditorShape> getShapes(String shapeType) {
        return cityShapes.get(shapeType);
    }

    public void addShapes(EditorShape shape, String shapeType) {
        cityShapes.get(shapeType).add(shape);
    }
    
    public void addStyle(String s) {
        if(!cityStyles.contains(s)) {
            cityStyles.add(s);
            style.put(s, new Style(new DrawColor(1,1,1,1), 0));
        }
    }
    
    public void removeStyle(String s) {
        if(cityStyles.contains(s)) {
            cityStyles.remove(s);
            style.remove(s);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import GUI.Style;
import GUI.DrawColor;
import Shapes.EditorShape;
import Shapes.Zone;
import Shapes.Obstacle;
import Shapes.Building;
import Shapes.Shape;
import imgui.ImColor;
import imgui.type.ImString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class Settlement {

    private String name = "";
    private HashMap<String, ArrayList<EditorShape>> cityShapes = new HashMap<>();

    private Style defaultStyle = new Style(new DrawColor(0, 0, 0, 1), 0);
    private Style backdropStyle = new Style(new DrawColor(0.9216f, 0.8353f, 0.702f, 1f), 0);
    private Style editStyle = new Style(new DrawColor(1f, 0, 0, 1f), 0);
    private Style waterStyle = new Style(new DrawColor(0, 0, 1f, 1f), 3);
    
    private HashMap<String, Style> style = new HashMap<>();

    private ArrayList<String> cityStyles = new ArrayList<>();
    
    private Zone cameraShape = new Zone();
    
    private ImString exportFilePath = new ImString();
    private ImString exportFileName = new ImString();

    public Settlement(String name) {
        style.put("Zone Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Zone Color");
        style.put("Placed Building Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Placed Building Color");
        style.put("Generated Building Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Generated Building Color");
        style.put("Obstacle Color", new Style(new DrawColor(0.549f, 0.784f, 0.949f, 1), 0));
        cityStyles.add("Obstacle Color");

        for (int i = 0; i < Constants.CITY_SHAPE_TYPES.length; i++) {
            cityShapes.put(Constants.CITY_SHAPE_TYPES[i], new ArrayList());
        }

        this.name = name;
        this.exportFileName.set(name);
    }

    public float[] getColor(String key) {
        return style.get(key).getColor().getFloatOfColor();
    }

    public Style getStyle(String key) {
        if (key.equals("default")) {
            return this.defaultStyle;
        } else if (key.equals("water")) {
            return this.waterStyle;
        } else {
            return style.get(key);
        }
    }

    public Style getDefaultStyle() {
        return this.defaultStyle;
    }
    
    public Style getBackdropStyle() {
        return this.backdropStyle;
    }
    
    public Style getEditStyle() {
        return this.editStyle;
    }
    
    public Style getWaterStye() {
        return this.waterStyle;
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

//    public void fillHash(HashMap<String, String[]> hash) {
//        ArrayList<Zone> zones = new ArrayList<>();
//        ArrayList<Building> buildings = new ArrayList<>();
//        ArrayList<Obstacle> Obstacles = new ArrayList<>();
//    }
    public List<EditorShape> getShapes(String shapeType) {
        return cityShapes.get(shapeType);
    }

    public ArrayList<EditorShape> getRawEditorShapes() {
        ArrayList<EditorShape> shapes = new ArrayList();
        List<ArrayList<EditorShape>> ciShape = new ArrayList<>(cityShapes.values());
        for (int i = 0; i < ciShape.size(); i++) {
            shapes.addAll(ciShape.get(i));
        }
        //EditorShape[] shapeArray = shapes.stream().map(s -> s).toArray(sz -> new EditorShape[sz]);

        return shapes;
    }

    public void addShapes(EditorShape shape, String shapeType) {
        cityShapes.get(shapeType).add(shape);
    }

    public void addStyle(String s) {
        if (!cityStyles.contains(s)) {
            cityStyles.add(s);
            style.put(s, new Style(new DrawColor(1, 1, 1, 1), 0));
        }
    }

    public void removeStyle(String s) {
        if (cityStyles.contains(s)) {
            cityStyles.remove(s);
            style.remove(s);
        }
    }
    
    public void clearCameraShape() {
        this.cameraShape.clear();
    }
    
    public EditorShape getCamera() {
        return cameraShape;
    }
    
    public void setExportFilePath(String f) {
        this.exportFilePath.set(f);
    }
    
    public ImString getExportFilePath() {
        return this.exportFilePath;
    }
    
    public ImString getExportFileName() {
        return this.exportFileName;
    }

}

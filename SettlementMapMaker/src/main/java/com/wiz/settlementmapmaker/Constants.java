/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shape.EditorShape;
import Shape.Zone;
import Shape.Obstacle;
import Shape.Building;
import com.wiz.settlementmapmaker.Utilities.CityEditorState;
import imgui.ImColor;
import java.util.function.Function;

/**
 *
 * @author 904187003
 */
public class Constants {
    public static final String[] CITY_SHAPE_TYPES = new String[]{"Zone", "Building", "Obstacle"};
    
    // colors commonly used throughout code
    public static final int COLOR_WARM_PARCHMENT = ImColor.intToColor(150, 109, 80, 255);
    public static final int COLOR_PARCHMENT = ImColor.intToColor(230, 203, 156, 255);
    
    public static final int CALM_GREEN = ImColor.intToColor(55, 140, 78, 255);
    
    public static enum CityShapeTypes {
        ZONE((state) -> new Zone(state.defaultZoneName, state.defaultZoneType)),
        BUILDING((state) -> new Building(state.defaultBuildingName)),
        OBSTACLE((state) -> new Obstacle(state.defaultObstacleName))
        ;
        
        private final Function<CityEditorState, EditorShape> provider;
            
        CityShapeTypes(Function<CityEditorState, EditorShape> f) {
            provider = f;
        }
        
        public EditorShape supply(CityEditorState state) {
            return provider.apply(state);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import Shapes.EditorShape;
import Shapes.Zone;
import Shapes.Obstacle;
import Shapes.Building;
import com.wiz.settlementmapmaker.Utilities.CityEditorState;
import imgui.ImColor;
import imgui.type.ImFloat;
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
    
    public static final int COLOR_CALM_GREEN = ImColor.intToColor(55, 140, 78, 255);
    public static final int COLOR_RED = ImColor.intToColor(212, 46, 34, 255);
    
    public static enum CityShapeTypes {
        ZONE((state) -> new Zone(state.defaultZoneName, 0)),
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
    
    public static final String[] ZONE_TYPES = new String[]{"Generate Buildings", "Generate City", "Block Generation"};
    
    public static final String[] OBSTACLE_TYPES = new String[]{"River"};
    
    
    public static final float MOUSE_WHEEL_SENSITIVITY = 0.05f;
    
    public static final float MAX_ZOOM = 2f;
    public static final float MIN_ZOOM = 0.01f;
    
    // Textures
    public static final String TEXTURE_FOLDER = "folder.png";
    public static final String TEXTURE_PLUS = "plus.png";
    public static final String TEXTURE_ARROW_UP = "arrowUp.png";
    public static final String TEXTURE_ARROW_DOWN = "arrowDown.png";
    public static final String TEXTURE_MOVE_ARROW = "moveArrow.png";
    
    
    // Name Files
    public static final String SETTLEMENT_NAME_FIRST = "SettlementFirstHalf.txt";
    public static final String SETTLEMENT_NAME_SECOND = "SettlementSecondHalf.txt";
}

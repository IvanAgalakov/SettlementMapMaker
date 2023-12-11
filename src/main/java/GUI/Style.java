/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import imgui.type.ImFloat;
import imgui.type.ImInt;

/**
 *
 * @author Ivan
 */
public class Style {
    
    private DrawColor styleColor;
    private ImInt selectedDrawType = new ImInt();
    private ImFloat lineThickness = new ImFloat();
    public static final String[] styleTypes = new String[]{"line", "point", "dashed line", "solid"};
    
    public Style(DrawColor styleColor, int selectedDrawType) {
        this.styleColor = styleColor;
        this.selectedDrawType.set(selectedDrawType);
    }
    
    public DrawColor getColor() {
        return styleColor;
    }
    
    public ImInt getSelectedStyle() {
        return this.selectedDrawType;
    }
    
}

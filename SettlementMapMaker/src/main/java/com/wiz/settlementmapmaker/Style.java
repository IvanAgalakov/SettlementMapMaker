/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.type.ImInt;

/**
 *
 * @author Ivan
 */
public class Style {
    
    private DrawColor styleColor;
    private ImInt selectedDrawType = new ImInt();
    public static final String[] styleTypes = new String[]{"point", "line", "dashed line", "solid"};
    
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

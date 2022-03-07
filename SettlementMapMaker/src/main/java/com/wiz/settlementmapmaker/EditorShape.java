/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

import imgui.type.ImInt;
import imgui.type.ImString;

/**
 *
 * @author 904187003
 */
public class EditorShape extends Shape {

    private ImString name = new ImString();
    private ImInt style = new ImInt(0);

    public EditorShape(String name) {
        super();
        this.name.set(name);
    }
    
    public ImString getName() {
        return name;
    }
    
    public void setStyle(int style) {
        this.style.set(style);
    }
    
    public ImInt getStyle() {
        return this.style;
    }

}

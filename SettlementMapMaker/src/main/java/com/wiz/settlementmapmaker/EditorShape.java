/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker;

/**
 *
 * @author 904187003
 */
public class EditorShape extends Shape {

    private String name = "";
    private String style = "default";

    public EditorShape(String name) {
        super();
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

}

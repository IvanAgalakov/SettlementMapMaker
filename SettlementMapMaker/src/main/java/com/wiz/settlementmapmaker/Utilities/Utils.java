/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Utilities;

import imgui.ImVec4;

/**
 *
 * @author Ivan
 */
public class Utils {

    public static ImVec4 integerRGBAtoVec4(int r, int g, int b, int a) {
        return new ImVec4(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}

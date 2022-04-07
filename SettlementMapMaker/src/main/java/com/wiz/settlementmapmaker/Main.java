/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiz.settlementmapmaker;

import GUI.GUILayer;

/**
 *
 * @author Ivan
 */
public class Main {
    public static void main(String[] args) {
        Window window = new Window(new GUILayer());
        window.init();
        window.run();
        window.destroy();
        System.exit(0);

    }
}

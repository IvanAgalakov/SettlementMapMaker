/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiz.settlementmapmaker;

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

//        Shape shape = new Shape(new Point[]{new Point(-1,0), new Point(1,0)});
//        for(int i = 0; i < shape.getEnclosedLinesFromPoints().length; i++) {
//            System.out.println(shape.getEnclosedLinesFromPoints()[i].toString());
//        }
    
     //   System.out.println(SettlementGenerator.getPointAlongLine(new Point(0.5f,0.5f), -1f, 0, 1, 1f).toString());
       // System.out.println(SettlementGenerator.normalPointToPoint(new Point(0.5f,0.5f), 0, 1f, 2f).toString());
    }
}

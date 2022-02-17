/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import imgui.type.ImString;

/**
 *
 * @author 904187003
 */
public class ImStringChangeAction implements Action {

    ImString toChange;
    String changedFrom;
    
    public ImStringChangeAction(ImString change, String changeTo) {
        toChange = change;
        changedFrom = change.get();
        change.set(changeTo);
    }
    
    @Override
    public Action revert() {
        return new ImStringChangeAction(toChange, changedFrom);
    }
    
}
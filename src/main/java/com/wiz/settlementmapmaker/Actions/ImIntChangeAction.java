/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import imgui.type.ImInt;

/**
 *
 * @author Ivan
 */
public class ImIntChangeAction implements Action {
    
    ImInt toChange;
    int changedFrom;

    public ImIntChangeAction(ImInt change, int changeTo) {
        toChange = change;
        changedFrom = change.get();
        change.set(changeTo);
    }

    public ImIntChangeAction(ImInt change, int changeTo, int original) {
        toChange = change;
        changedFrom = original;
        change.set(changeTo);
    }

    @Override
    public Action revert() {
        return new ImIntChangeAction(toChange, changedFrom);
    }
    
}

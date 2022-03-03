/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import imgui.type.ImBoolean;

/**
 *
 * @author 904187003
 */
public class ImBooleanChangeAction implements Action {

    ImBoolean toChange;
    boolean changedFrom;
    
    public ImBooleanChangeAction(ImBoolean change, boolean changeTo) {
        toChange = change;
        changedFrom = change.get();
        change.set(changeTo);
    }
    
    @Override
    public Action revert() {
        return new ImBooleanChangeAction(toChange, changedFrom);
    }
    
}

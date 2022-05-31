/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

/**
 *
 * @author 904187003
 */
public class CombinedAction implements Action {

    Action[] actions;
    
    public CombinedAction(Action... actions) {
        this.actions = actions;
    }
    
    @Override
    public Action revert() {
        Action[] revertedActions = new Action[actions.length];
        for(int i = 0; i < revertedActions.length; i++) {
            //System.out.println(actions[i].getClass().getName());
            revertedActions[i]= actions[i].revert();
        }
        
        return new CombinedAction(revertedActions);
    }
    
}

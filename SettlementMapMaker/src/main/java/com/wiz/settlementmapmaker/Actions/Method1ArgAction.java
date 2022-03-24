/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import com.wiz.settlementmapmaker.Utilities.MethodPass;
import com.wiz.settlementmapmaker.Utilities.MethodPass1Arg;

/**
 *
 * @author 904187003
 */
public class Method1ArgAction<E> implements Action {
    
    E ePass;
    E revertEPass;
    
    MethodPass1Arg method;
    MethodPass1Arg revertMethod;

    public Method1ArgAction(MethodPass1Arg method, E ePass) {
        this.method = method;
        this.revertMethod = method;
        this.ePass = ePass;
        this.revertEPass = ePass;
        this.method.myMethod(this.ePass);
    }

    public Method1ArgAction(MethodPass1Arg method, MethodPass1Arg revertMethod, E ePass, E revertEPass) {
        this.method = method;
        this.ePass = ePass;
        this.revertEPass = revertEPass;
        this.method.myMethod(this.ePass);
        this.revertMethod = revertMethod;
        
    }

    @Override
    public Action revert() {
        System.out.println(Thread.currentThread() + ", " + "method");
        return new Method1ArgAction(revertMethod, method, revertEPass, ePass);
    }
    
}

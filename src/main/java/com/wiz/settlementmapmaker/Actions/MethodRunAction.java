/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import com.wiz.settlementmapmaker.Utilities.MethodPass;

/**
 *
 * @author 904187003
 */
public class MethodRunAction implements Action {

    MethodPass method;
    MethodPass revertMethod;

    public MethodRunAction(MethodPass method) {
        this.method = method;
        this.revertMethod = method;
        this.method.myMethod();
    }

    public MethodRunAction(MethodPass method, MethodPass revertMethod) {
        this.method = method;
        this.method.myMethod();
        this.revertMethod = revertMethod;
    }

    @Override
    public Action revert() {
        System.out.println(Thread.currentThread() + ", " + "method");
        return new MethodRunAction(revertMethod, method);
    }

}

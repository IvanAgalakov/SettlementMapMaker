/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import java.util.List;

/**
 *
 * @author Ivan
 */
public class SetListAction<E> implements Action {

    private E original;
    private E replaceWith;
    private int index;
    private List<E> list;

    public SetListAction(List<E> list, int original, E replaceWith) {

        this.list = list;
        this.original = list.get(original);
        this.replaceWith = replaceWith;
        
        index = original;
        
        System.out.println("replaced : " + original + "with : " + replaceWith);
        list.set(original, replaceWith);
        
    }

    @Override
    public Action revert() {
        System.out.println("reverted list set");
        return new SetListAction<>(list, index, original);
    }

}

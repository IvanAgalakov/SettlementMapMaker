/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Actions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 904187003
 */
public class AlterListAction<E> implements Action {

    private E change;
    private List<E> list;
    private boolean remove;
    private int index;

    private AlterListAction(List<E> list, E change, boolean remove, int index) {
        this.change = change;

        this.list = list;
        this.remove = remove;
        this.index = index;
        
        if (remove == false) {
            list.add(index, this.change);
        } else {
            index = list.indexOf(this.change);
            this.index = index;
            list.remove(this.change);
        }
    }
    
    public AlterListAction(List<E> list, E change, boolean remove) {
        this(list, change, remove, list.size());
    }

    @Override
    public Action revert() {
        System.out.println("reverted list");
        return new AlterListAction<>(list, change, !remove, index);
    }

}

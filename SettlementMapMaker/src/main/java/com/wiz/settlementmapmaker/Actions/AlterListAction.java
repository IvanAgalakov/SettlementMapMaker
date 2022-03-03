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
    List<E> list;
    boolean remove;

    public AlterListAction(List<E> list, E change, boolean remove) {
        this.change = change;

        this.list = list;
        this.remove = remove;
        if (remove == false) {
            list.add(this.change);
        } else {
            
            System.out.println(list.remove(this.change));
        }
    }

    @Override
    public Action revert() {
        System.out.println("reverted list");
        return new AlterListAction<E>(list, change, !remove);
    }

}

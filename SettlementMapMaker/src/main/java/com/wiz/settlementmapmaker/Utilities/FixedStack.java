/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wiz.settlementmapmaker.Utilities;

/**
 *
 * @author 904187003
 */
public class FixedStack<T> {

    public class Node {

        T data;
        Node next, prev;

        public Node(T data) {
            this.data = data;
        }
    }

    Node start, end;
    public int length;
    int maxLength;

    public FixedStack(int maxLength) {
        this.maxLength = maxLength;
    }

    public void push(T t) {
        if (start == null && end == null) {
            start = end = new Node(t);
        } else if (start == end) {
            end = new Node(t);
            start.next = end;
            end.prev = start;
        } else if (start != end) {
            Node toAdd = new Node(t);
            end.next = toAdd;
            toAdd.prev = end;
            end = toAdd;
        }
        length++;
        while (length > maxLength) {
            popBottom();
        }
    }

    public T popTop() {
        if (start == null && end == null) {
            throw new RuntimeException("cannot pop end");
        } else if (start == end) {
            return popBottom();
        } else {
            T data = end.data;
            end.prev.next = null;
            end = end.prev;
            length--;
            return data;
        }

    }

    public T popBottom() {
        if (start == null && end == null) {
            throw new RuntimeException("cannot pop start");
        } else if (start == end) {
            T data = start.data;
            start = end = null;
            length--;
            return data;
        } else {
            T data = start.data;
            start.next.prev = null;
            start = start.next;
            length--;
            return data;
        }

    }

    public boolean canPop() {
        return start != null && end != null;
    }

    public void clear() {
        while (length > 0) {
            this.popBottom();
        }
    }

}

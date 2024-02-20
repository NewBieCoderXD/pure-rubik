package org.example.prog_meth_project;

import org.example.prog_meth_project.pane.NotationStack;
import org.example.prog_meth_project.Notation;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Stack;

public class NotationQueue extends LinkedList<Notation> {
    @Override
    public boolean add(Notation notation) {
        NotationStack.getInstance().add(notation);
        return super.add(notation);
    }

    @Override
    public Notation poll() {
        NotationStack.getInstance().poll();
        return super.poll();
    }
}

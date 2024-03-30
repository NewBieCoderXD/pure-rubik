package org.example.prog_meth_project.application;

import javafx.application.Platform;
import org.example.prog_meth_project.pane.NotationStack;

import java.util.LinkedList;

public class NotationQueue extends LinkedList<Notation> {
    @Override
    public boolean add(Notation notation) {
        Platform.runLater(()->{
            NotationStack.getInstance().add(notation);
        });
        return super.add(notation);
    }

    @Override
    public Notation poll() {
        NotationStack.getInstance().poll();
        return super.poll();
    }
}

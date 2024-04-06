package org.example.prog_meth_project.application;

import javafx.application.Platform;
import org.example.prog_meth_project.component.NotationStack;

import java.util.LinkedList;

public class NotationQueue extends LinkedList<Notation> {
    private NotationStack notationStack;
    public NotationQueue(NotationStack notationStack){
        setNotationStack(notationStack);
    }
    public void setNotationStack(NotationStack notationStack){
        this.notationStack=notationStack;
    }
    @Override
    public boolean add(Notation notation) {
        Platform.runLater(()->{
            notationStack.add(notation);
        });
        return super.add(notation);
    }

    @Override
    public Notation poll() {
        notationStack.poll();
        return super.poll();
    }
}

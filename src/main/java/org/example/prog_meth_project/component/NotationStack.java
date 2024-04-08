package org.example.prog_meth_project.component;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.prog_meth_project.application.Notation;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class NotationStack extends VBox {
    private final ArrayDeque<String> deque = new ArrayDeque<>();
    private final ArrayDeque<String> futureDeque = new ArrayDeque<>();

    public NotationStack(){
        this.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        StackPane.setAlignment(this,Pos.CENTER_LEFT);
        for(int i=0;i<5;i++){
            Text text = new Text("");
            text.setFont(new Font(26));
            HBox wrappingBox = new HBox(text);
            double size = 42;
            if(i==2){
                size*=1.5;
            }
            text.setTextAlignment(TextAlignment.CENTER);
            text.setWrappingWidth(size);
            text.setLineSpacing(0);
            wrappingBox.setPrefWidth(size);
            wrappingBox.setPrefHeight(size);
            wrappingBox.setAlignment(Pos.CENTER);
            wrappingBox.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
            wrappingBox.setBackground(
                new Background(
                    new BackgroundFill(
                        Color.WHITE,
                        null,
                        null
                    )
                )
            );
            wrappingBox.setBorder(
                new Border(
                    new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID,
                        null,
                        new BorderWidths((i==0||i==2)?2:0,2,(i==1)?0:2,2)
                    )
                )
            );
            super.getChildren().add(wrappingBox);
        }
    }
    public Text getNodeText(int index){
        HBox wrappingBox = (HBox) super.getChildren().get(index);
        return (Text) wrappingBox.getChildren().get(0);
    }

    public void update(Notation notation, Queue<Notation> notationQueue){
        this.getNodeText(4).setText(this.getNodeText(3).getText());
        this.getNodeText(3).setText(this.getNodeText(2).getText());
        this.getNodeText(2).setText(notation.toPrettyString());
        Iterator<Notation> currentStackNode = notationQueue.iterator();
        for(int i=1;i>=0;i--){
            if(currentStackNode.hasNext()){
                Notation currentNotation = currentStackNode.next();
                this.getNodeText(i).setText(currentNotation.toPrettyString());
                continue;
            }
            this.getNodeText(i).setText("");
        }
    }
    public ArrayDeque<String> getDeque(){
        return deque;
    }
    public ArrayDeque<String> getFutureDeque(){
        return futureDeque;
    }
    public void update(){
//        System.out.println("future "+futureDeque.size());
//        System.out.println("deque "+deque.size());
        Iterator<String> dequeItr = deque.iterator();
        Iterator<String> futureDequeItr = futureDeque.descendingIterator();
        for(int i=2;i>=0;i--){
            String notation = "";
            if(futureDequeItr.hasNext()){
                notation=futureDequeItr.next();
            }
            Text child = this.getNodeText(i);
            child.setText(notation);
        }
        for(int i=2;i<5&&dequeItr.hasNext();i++){
            String notation = dequeItr.next();
            Text child = this.getNodeText(i);
            child.setText(notation);
        }
    }
    public void add(Notation notation){
        if(deque.size()==3){
            futureDeque.addFirst(notation.toPrettyString());
        }
        else {
            deque.addFirst(notation.toPrettyString());
        }
        Platform.runLater(this::update);
    }
    public void poll(){
        if(!futureDeque.isEmpty()) {
            deque.pollLast();
            deque.addFirst(futureDeque.pollLast());
        }
        Platform.runLater(this::update);
    }
}

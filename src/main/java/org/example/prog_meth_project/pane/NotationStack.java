package org.example.prog_meth_project.pane;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.prog_meth_project.Notation;

import java.util.Iterator;
import java.util.Queue;

public class NotationStack extends GridPane {
    public NotationStack(){
        for(int i=0;i<5;i++){
            Text text = new Text("");
            text.setFont(new Font(26));
            HBox wrappingBox = new HBox(text);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setWrappingWidth(42);
            text.setLineSpacing(0);
            wrappingBox.setPrefWidth(42);
            wrappingBox.setPrefHeight(42);
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
                        new BorderWidths(i==0?2:0,2,2,2)
                    )
                )
            );
            super.add(wrappingBox,0,i);
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
//    public Text setNodeText(int index){
//
//    }
}

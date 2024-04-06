package org.example.prog_meth_project.application;

import com.ggFROOK.InvalidRubikNotation;
import com.ggFROOK.RubikFROOK;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.example.prog_meth_project.model.BaseRubik;
import org.example.prog_meth_project.model.Cubelet;
import org.example.prog_meth_project.rendering.Rotation;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.prog_meth_project.config.Config.SECOND_PER_NOTATION;

public class RubikAnimationThread extends Thread{
    private static boolean isPTRunning = false;
    private final ParallelTransition pt = new ParallelTransition();

    private NotationQueue notationQueue;
    private AtomicBoolean isSolving;
    private AtomicBoolean startSolving;
    private RubikFROOK rubikFROOK;
    private BaseRubik rubik;
    public RubikAnimationThread(NotationQueue notationQueue, AtomicBoolean isSolving, AtomicBoolean startSolving, RubikFROOK rubikFROOK, BaseRubik rubik){
        this.notationQueue=notationQueue;
        this.isSolving=isSolving;
        this.startSolving=startSolving;
        this.rubikFROOK=rubikFROOK;
        this.rubik=rubik;
        super.setDaemon(true);
    }
    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            if(isPTRunning){
                continue;
            }
            if(notationQueue.isEmpty()){
                // if it is empty that mean either 1.done all scrambles or 2.done solving
                // either way, isSolving should be set to false
                isSolving.set(false);
                // if solving button is just pressed, set isSolving to true
                if(startSolving.get()){
                    isSolving.set(true);
                    startSolving.set(false);
                    ArrayList<com.ggFROOK.Notation> solution = rubikFROOK.mainSolving();
                    for(String notationString: solution.stream().map(Object::toString).toList()){
                        notationQueue.add(Notation.valueOf(notationString));
                    }
                }
                continue;
            }

            Notation currentNotation = notationQueue.poll();
            isPTRunning=true;
            assert currentNotation != null;
            ArrayList<Cubelet> cubelets = rubik.getSideOfNotation(currentNotation);
            for (Cubelet cubelet : cubelets) {
                Timeline timeline = buildCubeletTimeline(cubelet, currentNotation);
                pt.getChildren().add(timeline);
            }
            pt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    callNotation(currentNotation);
//                    notationStack.update(currentNotation,notationQueue);
                    pt.getChildren().clear();
                    isPTRunning=false;
                }
            });
            Platform.runLater(pt::play);
        }
    }

    private static Timeline buildCubeletTimeline(Cubelet cubelet, Notation currentNotation) {
        int sign = currentNotation.rotatingDirection();

        Rotation rotation = new Rotation(cubelet, currentNotation);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO, new KeyValue(rotation, 0d)
                ), new KeyFrame(
                Duration.seconds(SECOND_PER_NOTATION), new KeyValue(rotation, sign * 90d)
        )
        );
        timeline.setCycleCount(1);
        return timeline;
    }

    private void callNotation(Notation notation){
        try{
            System.out.println(notation.toString());
            rubik.call(notation);
            if(!isSolving.get()) {
                rubikFROOK.call(notation.toString(), true);
            }
        }
        catch(InvalidRubikNotation e){
            e.printStackTrace();
        }
    }

}

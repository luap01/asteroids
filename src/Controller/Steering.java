package Controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class Steering {

    private String inputType;
    private SteeringImplementation impl;
    private Canvas canvas;

    public Steering(String inputType, Canvas canvas) {
        this.inputType = inputType;
        this.canvas = canvas;
        addHandler(KeyEvent.KEY_PRESSED, this::pauseKeyPressed);
    }

    public String getUserInput() {
        return inputType;
    }

    public void changeInputType(String input) {
        inputType = input;
    }

    public SteeringImplementation getImpl() {
        return this.impl;
    }

    public void setImpl(SteeringImplementation impl) {
        this.impl = impl;
    }

    public abstract double[] getDir();

    public abstract boolean getShooting();

    public void pauseKeyPressed(Event e) {
        if (((KeyEvent) e).getCode().equals(KeyCode.ESCAPE)) {
            impl.getPausePressed();
        }
    }

    public void addHandler(EventType eventType, EventHandler eventHandler) {
        canvas.addEventHandler(eventType, eventHandler);
    }

}

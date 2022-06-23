package game;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class KeyMon {

    public ArrayList<KeyCode> keyStore = new ArrayList<>();

    class doPressed implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            if (!keyStore.contains(event.getCode())){
                keyStore.add(event.getCode());
            }
        }
    }
    class doReleased implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            keyStore.remove(event.getCode());
        }
    }

    public void setScene(Scene scene){
        scene.addEventHandler(KeyEvent.KEY_PRESSED,new doPressed());
        scene.addEventHandler(KeyEvent.KEY_RELEASED,new doReleased());
    }

    public KeyMon(Scene scene){
        setScene(scene);
    }

}

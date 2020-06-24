package fg331.free.bg.State;

import java.awt.*;

public abstract class State {
    static State currentState = null;

    public static State getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(State currentState) {
        State.currentState = currentState;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract void mouseSetter();

    public abstract void mouseRemover();

}

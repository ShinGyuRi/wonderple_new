package kr.co.easterbunny.wonderple.event;

/**
 * Created by scona on 2017-02-10.
 */

public class ClickEvent {

    private final boolean isClicked;

    public ClickEvent(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public static class ClickNextEvent {
        private boolean isClicked = false;

        public ClickNextEvent(boolean isClicked) {
            this.isClicked = isClicked;
        }

        public boolean isClicked() {
            return isClicked;
        }
    }

}

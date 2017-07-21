package kr.co.easterbunny.wonderple.event;

/**
 * Created by scona on 2017-05-22.
 */

public class ClickBtnSelectComplete {
    boolean isClicked;
    int selection;

    public ClickBtnSelectComplete(boolean isClicked, int selection) {
        this.isClicked = isClicked;
        this.selection = selection;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public int getSelection() {
        return selection;
    }
}

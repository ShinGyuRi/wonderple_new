package kr.co.easterbunny.wonderple.event;

/**
 * Created by scona on 2017-05-08.
 */

public class ConfirmEvent {

    private final boolean isConfirm;

    public ConfirmEvent(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public boolean isConfirm() {
        return isConfirm;
    }
}

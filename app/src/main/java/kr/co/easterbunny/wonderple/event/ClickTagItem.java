package kr.co.easterbunny.wonderple.event;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * Created by scona on 2017-05-19.
 */

public class ClickTagItem {
    private boolean isClicked;
    private List<LoadTagPouchResult.FacePart> clickItems = new ArrayList<>();
    private int position;

    public ClickTagItem(boolean isClicked, List<LoadTagPouchResult.FacePart> clickItems, int position) {
        this.isClicked = isClicked;
        this.clickItems = clickItems;
        this.position = position;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public List<LoadTagPouchResult.FacePart> getClickItems() {
        return clickItems;
    }

    public int getPosition() {
        return position;
    }
}

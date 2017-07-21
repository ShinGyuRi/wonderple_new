package kr.co.easterbunny.wonderple.listener;

import java.util.List;

import kr.co.easterbunny.wonderple.model.ItemType;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * Created by scona on 2017-05-23.
 */

public interface Updateable {
    public void update(ItemType itemType, List<LoadTagPouchResult.FacePart> faceParts);
}

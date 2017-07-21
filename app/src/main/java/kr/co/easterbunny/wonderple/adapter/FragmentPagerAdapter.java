package kr.co.easterbunny.wonderple.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.listener.Updateable;
import kr.co.easterbunny.wonderple.model.ItemType;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * Created by scona on 2017-05-23.
 */

public class FragmentPagerAdapter extends ViewPagerAdapter {
    private List<LoadTagPouchResult.FacePart> faceParts;
    private ItemType itemType;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm, fragments);
    }

    public void update(ItemType itemType, List<LoadTagPouchResult.FacePart> faceParts) {
        this.itemType = itemType;
        this.faceParts = faceParts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof Updateable) {
            ((Updateable) object).update(itemType, faceParts);
        }
        return super.getItemPosition(object);
    }
}

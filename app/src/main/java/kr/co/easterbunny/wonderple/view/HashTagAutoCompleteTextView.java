package kr.co.easterbunny.wonderple.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.HashTagSuggestAdapter;

/**
 * Created by scona on 2017-02-17.
 */

public class HashTagAutoCompleteTextView extends AutoCompleteTextView{

    public HashTagAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public HashTagAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.autoCompleteTextViewStyle);
    }

    public HashTagAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void replaceText(CharSequence text) {

        clearComposingText();

        HashTagSuggestAdapter adapter = (HashTagSuggestAdapter) getAdapter();
        HashTagSuggestAdapter.HashTagFilter filter = (HashTagSuggestAdapter.HashTagFilter) adapter.getFilter();

        Editable span = getText();
        span.replace(filter.start, filter.end, text);
    }
}

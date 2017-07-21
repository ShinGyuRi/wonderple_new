package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Window;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgCommentOptionBinding;

/**
 * Created by scona on 2017-03-30.
 */

public class CommentOptionDialog extends ParentDialog {

    private DlgCommentOptionBinding binding;

    private Context context;

    public CommentOptionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public DlgCommentOptionBinding getBinding() {
        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_comment_option, null, false);
        setContentView(binding.getRoot());
        binding.setCommentOption(this);
    }
}

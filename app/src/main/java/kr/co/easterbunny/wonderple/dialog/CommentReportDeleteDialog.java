package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Window;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgCommentReportDeleteBinding;

/**
 * Created by scona on 2017-04-04.
 */

public class CommentReportDeleteDialog extends ParentDialog {
    private DlgCommentReportDeleteBinding binding;

    private Context context;

    public CommentReportDeleteDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public DlgCommentReportDeleteBinding getBinding() {
        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_comment_report_delete, null, false);
        setContentView(binding.getRoot());
        binding.setCommentReportDelete(this);
    }
}

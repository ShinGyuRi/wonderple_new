package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgCommentReviseOptionBinding;
import kr.co.easterbunny.wonderple.library.util.Toaster;

/**
 * Created by scona on 2017-04-04.
 */

public class CommentReviseOptionDialog extends ParentDialog {
    private DlgCommentReviseOptionBinding binding;

    Context context;
    String cid;
    String uid;

    public CommentReviseOptionDialog(@NonNull Context context, String cid, String uid) {
        super(context);
        this.context = context;
        this.cid = cid;
        this. uid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_comment_revise_option, null, false);
        setContentView(binding.getRoot());
        binding.setCommentRevise(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_return_report:
                sendCommentReport(cid, uid, "", "2");
                Toaster.shortToast("신고가 철회되었습니다.");
                dismiss();
                break;

            case R.id.btn_revise:
                CommentReportDialog dialog = new CommentReportDialog(context, cid, uid, "1");
                dialog.show();
                break;
        }
    }
}

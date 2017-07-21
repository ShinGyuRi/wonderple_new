package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgReviseOptionBinding;
import kr.co.easterbunny.wonderple.library.util.Toaster;

/**
 * Created by scona on 2017-03-27.
 */

public class ReviseOptionDialog extends ParentDialog {

    private DlgReviseOptionBinding binding;

    Context context;
    String iid;
    String uid;

    public ReviseOptionDialog(@NonNull Context context, String iid, String uid) {
        super(context);
        this.context = context;
        this.iid = iid;
        this. uid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_revise_option, null, false);
        setContentView(binding.getRoot());
        binding.setRevise(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_return_report:
                sendReport(iid, uid, "", "2");
                Toaster.shortToast("신고가 철회되었습니다.");
                dismiss();
                break;

            case R.id.btn_revise:
                ReportDialog dialog = new ReportDialog(context, iid, uid, "1");
                dialog.show();
                break;
        }
    }
}

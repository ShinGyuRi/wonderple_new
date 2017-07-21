package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgReportBinding;

/**
 * Created by scona on 2017-03-24.
 */

public class ReportDialog extends ParentDialog {

    private DlgReportBinding binding;

    private Context context;

    String iid = null;
    String uid = null;
    String reasons = null;
    String theCase = null;

    String option1 = "";
    String option2 = "";
    String option3 = "";
    String option4 = "";
    String option5 = "";
    String option6 = "";
    String option7 = "";

    public ReportDialog(@NonNull Context context, String iid, String uid, String theCase) {
        super(context);
        this.context = context;
        this.iid = iid;
        this.uid = uid;
        this.theCase = theCase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_report, null, false);
        setContentView(binding.getRoot());
        binding.setReport(this);

        binding.btnOk.setEnabled(false);

        setOption7();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option1:
                if (!binding.imgOption1.isChecked()) {
                    binding.imgOption1.setChecked(true);
                    option1 =  " " + binding.option1.getText().toString();
                } else {
                    binding.imgOption1.setChecked(false);
                    option1 = "";
                }
                break;

            case R.id.option2:
                if (!binding.imgOption2.isChecked()) {
                    binding.imgOption2.setChecked(true);
                    option2 =  " " + binding.option2.getText().toString();
                } else {
                    binding.imgOption2.setChecked(false);
                    option2 = "";
                }
                break;

            case R.id.option3:
                if (!binding.imgOption3.isChecked()) {
                    binding.imgOption3.setChecked(true);
                    option3 =  " " + binding.option3.getText().toString();
                } else {
                    binding.imgOption3.setChecked(false);
                    option3 = "";
                }
                break;

            case R.id.option4:
                if (!binding.imgOption4.isChecked()) {
                    binding.imgOption4.setChecked(true);
                    option4 =  " " + binding.option4.getText().toString();
                } else {
                    binding.imgOption4.setChecked(false);
                    option4 = "";
                }
                break;

            case R.id.option5:
                if (!binding.imgOption5.isChecked()) {
                    binding.imgOption5.setChecked(true);
                    option5 =  " " + binding.option5.getText().toString();
                } else {
                    binding.imgOption5.setChecked(false);
                    option5 = "";
                }
                break;

            case R.id.option6:
                if (!binding.imgOption6.isChecked()) {
                    binding.imgOption6.setChecked(true);
                    option6 =  " " + binding.option6.getText().toString();
                } else {
                    binding.imgOption6.setChecked(false);
                    option6 = "";
                }
                break;

            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_ok:
                option7 = " " + binding.option7.getText().toString();
                reasons = option1 + option2 + option3 + option4 + option5 + option6 + option7;
                sendReport(iid, uid, reasons, theCase);
                dismiss();
                break;

        }

        if (binding.imgOption1.isChecked() || binding.imgOption2.isChecked() || binding.imgOption3.isChecked() || binding.imgOption4.isChecked()
                || binding.imgOption5.isChecked() || binding.imgOption6.isChecked() || binding.imgOption7.isChecked()) {
            binding.btnOk.setEnabled(true);
        } else {
            binding.btnOk.setEnabled(false);
        }
    }


    private void setOption7() {
        binding.option7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.imgOption7.setChecked(true);
                    binding.btnOk.setEnabled(true);
                } else {
                    binding.imgOption7.setChecked(false);
                    binding.btnOk.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

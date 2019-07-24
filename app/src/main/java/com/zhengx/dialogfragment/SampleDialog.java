package com.zhengx.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhengx.dialogfragutil.DialogFragUtils;

/**
 * name：com.zhengx.dialogfragment
 * class: describe
 * author: zhengx
 * create_time: 19-6-10
 */
public class SampleDialog extends DialogFragment {

    private static final String showTag = "show_tag";

    public static SampleDialog show(FragmentManager fm) {
//        DialogFragUtils.LoggerEnable = false;
        SampleDialog oneDialog = new SampleDialog();
        DialogFragUtils.show(fm, oneDialog, showTag);
        return oneDialog;
    }

    public static void dismiss(FragmentManager fm) {
        DialogFragment dialogFragment;
        if ((dialogFragment = (DialogFragment) fm.findFragmentByTag(showTag)) != null) {
            dialogFragment.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_one_test, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog dialog = new AlertDialog.DialogStyle(getContext())
//                .setTitle("title")
//                .setMessage("message")
//                .create();
//        Log.e("SampleDialog", "on created dialog");
//        return dialog;
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
//                p.gravity = Gravity.TOP;
//                getDialog().getWindow().setAttributes(p);
                DialogFragUtils.changeLocation(SampleDialog.this, Gravity.TOP, 0, 0);
            }
        });
    }
}

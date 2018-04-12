package com.gxb.gxswallet.base.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gxb.gxswallet.R;
import com.sxds.common.widget.AnimationBottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/4/12.
 */

public class PasswordDialog extends BottomSheetDialogFragment {

    @BindView(R.id.password_password_confirm)
    EditText mPasswordEt;


    private View mRootView;
    private OnPasswordConfirmListener mListener;

    /**
     * 密码确认监听器
     */
    public interface OnPasswordConfirmListener {
        /**
         * 确认密码
         *
         * @param password 密码
         */
        void onConfirm(String password);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_password_confirm, container, false);
            ButterKnife.bind(this, mRootView);
            initView();
        } else {
            if (mRootView.getParent() != null) {
                // 将当前的根布局从其父布局中移除
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }
        return mRootView;
    }

    private void initView() {

    }

    public PasswordDialog setPasswordConfirmListener(OnPasswordConfirmListener listener) {
        mListener = listener;
        return this;
    }

    @OnClick(R.id.confirm_password_confirm)
    void onConfirmBtnClick() {
        mListener.onConfirm(mPasswordEt.getText().toString());
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AnimationBottomSheetDialog(getContext());
    }
}

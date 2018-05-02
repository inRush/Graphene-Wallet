package com.gxb.gxswallet.page.send.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.send.model.Transaction;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.sxds.common.widget.AnimationBottomSheetDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author inrush
 * @date 2018/4/12.
 */

public class TransactionConfirmDialog extends BottomSheetDialogFragment {

    @BindView(R.id.to_dialog_transaction_confirm)
    TextView mToAccountTv;
    @BindView(R.id.avatar_dialog_transaction_confirm)
    ImageView mAvatarIv;
    @BindView(R.id.currency_dialog_transaction_confirm)
    TextView mCurrencyTv;
    @BindView(R.id.amount_dialog_transaction_confirm)
    TextView mAmountTv;
    @BindView(R.id.memo_dialog_transaction_confirm)
    TextView mMemoTv;
    @BindView(R.id.fee_dialog_transaction_confirm)
    TextView mFeeTv;


    private static final String DATA_KEY = "transaction";

    private Transaction mTransaction;
    private View mRootView;
    private OnTransactionConfirmListener mListener;
    private boolean isConfirm = false;

    public interface OnTransactionConfirmListener {
        /**
         * 交易确认
         */
        void onConfirm();

        /**
         * 交易取消
         */
        void onCancel();
    }

    /**
     * 设置交易确认监听器
     *
     * @param listener Listener
     * @return {@link TransactionConfirmDialog}
     */
    public TransactionConfirmDialog setOnTransactionConfirmListener(OnTransactionConfirmListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 获取实例
     *
     * @return UserInfoFragment
     */
    public static TransactionConfirmDialog newInstance(@NonNull Transaction transaction) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_KEY, transaction);
        TransactionConfirmDialog infoFragment = new TransactionConfirmDialog();
        infoFragment.setArguments(bundle);
        return infoFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_transaction_confirm, container, false);
            ButterKnife.bind(this, mRootView);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTransaction = bundle.getParcelable(DATA_KEY);
            }

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
        try {
            mAvatarIv.setImageDrawable(Jdenticon.from(mTransaction.getTo()).drawable());
            mToAccountTv.setText(mTransaction.getTo());
            mCurrencyTv.setText(mTransaction.getAsset());
            mAmountTv.setText(String.valueOf(mTransaction.getAmount()));
            mMemoTv.setText(mTransaction.getMemo());
            mFeeTv.setText(String.valueOf(mTransaction.getFee()));
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.confirm_dialog_transaction_confirm)
    void onConfirmBtnClick() {
        isConfirm = true;
        mListener.onConfirm();
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AnimationBottomSheetDialog(getContext());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isConfirm) {
            mListener.onCancel();
        }
    }
}

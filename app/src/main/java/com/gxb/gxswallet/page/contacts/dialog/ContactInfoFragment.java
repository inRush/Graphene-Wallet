package com.gxb.gxswallet.page.contacts.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactManager;
import com.gxb.gxswallet.page.contacts.ContactsFragment;
import com.gxb.gxswallet.page.contacts.OnContactChangeListener;
import com.gxb.gxswallet.page.editcontact.EditContactActivity;
import com.gxb.gxswallet.page.send.SendActivity;
import com.gxb.gxswallet.page.send.model.Sender;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.sxds.common.widget.AnimationBottomSheetDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户信息Fragment
 *
 * @author inrush
 * @date 2017/12/4.
 */

public class ContactInfoFragment extends BottomSheetDialogFragment
        implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.address_contact_dialog)
    TextView addressTv;
    @BindView(R.id.phone_contact_dialog)
    TextView phoneTv;
    @BindView(R.id.memo_contact_dialog)
    TextView memoTv;
    @BindView(R.id.avatar_contact_dialog)
    ImageView avatarIv;

    private static final String DATA_KEY = "contact";
    private OnContactChangeListener mListener;
    private View mRootView;
    private ContactData mContact;
    private ContactManager mContactManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnContactChangeListener) context;
    }

    /**
     * 获取实例
     *
     * @param contact User
     * @return UserInfoFragment
     */
    public static ContactInfoFragment newInstance(@NonNull ContactData contact) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_KEY, contact);
        ContactInfoFragment infoFragment = new ContactInfoFragment();
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mContactManager = new ContactManager(getContext());
            mRootView = inflater.inflate(R.layout.dialog_contact_message, container, false);
            ButterKnife.bind(this, mRootView);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mContact = bundle.getParcelable(DATA_KEY);
            }
            mToolbar.setTitle(mContact.getName());
            addressTv.setText(mContact.getAddress());
            phoneTv.setText(mContact.getPhone());
            memoTv.setText(mContact.getMemo());
            try {
                avatarIv.setImageDrawable(Jdenticon.from(mContact.getAddress()).drawable());
            } catch (IOException | SVGParseException e) {
                e.printStackTrace();
            }
        } else {
            if (mRootView.getParent() != null) {
                // 将当前的根布局从其父布局中移除
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }
        mToolbar.inflateMenu(R.menu.contact_info_menu);
        mToolbar.setOnMenuItemClickListener(this);
        return mRootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AnimationBottomSheetDialog(getContext());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                EditContactActivity.start(getActivity(), ContactsFragment.CONTACT_REQUEST_CODE, mContact);
                dismiss();
                break;
            case R.id.menu_trash:
                deleteAction();
                break;
            default:
                break;
        }
        return true;
    }


    public void deleteAction() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.are_you_sure_delete))
                .addAction(getString(R.string.cancel), (dialog, index) -> dialog.dismiss())
                .addAction(0, getString(R.string.delete), QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                    dialog.dismiss();
                    if (mContactManager.delete(mContact)) {
                        App.showToast(R.string.delete_success);
                        mListener.onChange();
                        dismiss();
                    } else {
                        App.showToast(R.string.delete_failure);

                    }
                })
                .show();
    }

    @OnClick(R.id.send_btn_contact_dialog)
    void onSendBtnClick() {
        SendActivity.start(getActivity(), new Sender(addressTv.getText().toString(),
                "0", AssetDataManager.getDefault(), memoTv.getText().toString()));
    }


}

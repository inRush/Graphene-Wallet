package com.gxb.gxswallet.page.editcontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.utils.CodeUtil;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.PresenterActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class EditContactActivity extends PresenterActivity<EditContactContract.Presenter>
        implements EditContactContract.View {
    @BindView(R.id.topbar_add_contact)
    QMUITopBar mTopBar;
    @BindView(R.id.avatar_add_contact)
    ImageView avatarIv;
    @BindView(R.id.name_add_contact)
    EditText nameEt;
    @BindView(R.id.address_add_contact)
    EditText addressEt;
    @BindView(R.id.phone_add_contact)
    EditText phoneEt;
    @BindView(R.id.memo_add_contact)
    EditText memoEt;

    private ContactData mContact;

    public static final int UPDATE_CONTACT_RESULT_CODE = CodeUtil.getCode();
    private static final String CONTACT_DATA_KEY = EditContactActivity.class.getSimpleName() + "contact";

    public static void start(Activity activity, int requestCode, ContactData contact) {
        Intent intent = new Intent(activity, EditContactActivity.class);
        intent.putExtra(CONTACT_DATA_KEY, contact);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mContact = bundle.getParcelable(CONTACT_DATA_KEY);
        }
        return super.initArgs(bundle);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        try {
            mTopBar.setTitle(getString(R.string.edit_contact));
            mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            avatarIv.setImageDrawable(Jdenticon.from("inrush").drawable());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
        nameEt.setText(mContact.getName());
        addressEt.setText(mContact.getAddress());
        phoneEt.setText(mContact.getPhone());
        memoEt.setText(mContact.getMemo());
        try {
            avatarIv.setImageDrawable(Jdenticon.from(mContact.getAddress()).drawable());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
        initAddressEtEvent();
    }

    private void initAddressEtEvent() {
        addressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    avatarIv.setImageDrawable(Jdenticon.from(addressEt.getText().toString()).drawable());
                } catch (IOException | SVGParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.add_btn_add_contact)
    void onSaveBtnClick() {
        String name = nameEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        String memo = memoEt.getText().toString().trim();
        ContactData contactData = new ContactData(mContact.getId(), name, address, phone, memo);
        if (mPresenter.updateContact(contactData)) {
            App.showToast(R.string.save_success);
            this.setResult(UPDATE_CONTACT_RESULT_CODE);
            finish();
        } else {
            showError(getString(R.string.save_failure));
        }
    }

    @Override
    protected EditContactContract.Presenter initPresenter() {
        return new EditContactPresenter(this);
    }

}

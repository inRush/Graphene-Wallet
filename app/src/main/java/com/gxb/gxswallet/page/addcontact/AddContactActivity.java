package com.gxb.gxswallet.page.addcontact;

import android.app.Activity;
import android.content.Intent;
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

public class AddContactActivity extends PresenterActivity<AddContactContract.Presenter>
        implements AddContactContract.View {
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

    public static final int ADD_CONTACT_RESULT_CODE = CodeUtil.getCode();

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AddContactActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        try {
            mTopBar.setTitle(getString(R.string.add_contact));
            mTopBar.addLeftBackImageButton().setOnClickListener(view -> finish());
            mTopBar.addRightImageButton(R.drawable.ic_scan, View.generateViewId());
            avatarIv.setImageDrawable(Jdenticon.from("").drawable());
            initAddressEtEvent();
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
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
    void onAddBtnClick() {
        String name = nameEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        if ("".equals(name) || "".equals(address) || "".equals(phone)) {
            showError(getString(R.string.fill_whole));
            return;
        }
        mPresenter.checkWalletExist(address);
    }

    @Override
    protected AddContactContract.Presenter initPresenter() {
        return new AddContactPresenter(this);
    }

    @Override
    public void onCheckWalletExistSuccess(boolean isExist, String walletName) {
        if (!isExist) {
            showError(getString(R.string.address_not_exists));
            return;
        }
        String name = nameEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        String memo = memoEt.getText().toString().trim();
        ContactData contactData = new ContactData(null, name, address, phone, memo);
        if (mPresenter.saveContact(contactData)) {
            App.showToast(R.string.save_success);
            this.setResult(ADD_CONTACT_RESULT_CODE);
            finish();
        } else {
            showError(getString(R.string.address_is_exist));
        }
    }

    @Override
    public void onCheckWalletExistError(Error error) {
        showError(error.getMessage());
    }
}

package com.gxb.gxswallet.page.contacts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.page.addcontact.AddContactActivity;
import com.gxb.gxswallet.page.contacts.adapter.ContactRecyclerAdapter;
import com.gxb.gxswallet.page.contacts.dialog.ContactInfoFragment;
import com.gxb.gxswallet.utils.CodeUtil;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.PresenterFragment;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ContactsFragment extends PresenterFragment<ContactsContract.Presenter>
        implements ContactsContract.View {
    @BindView(R.id.empty_view_contacts)
    QMUIEmptyView mEmptyView;
    @BindView(R.id.topbar_contacts)
    QMUITopBar mTopBar;
    @BindView(R.id.recycler_contacts)
    RecyclerView mContactRecycler;

    /**
     * 添加联系人的ActivityResultCode
     */
    public static final int CONTACT_REQUEST_CODE = CodeUtil.getCode();
    private List<ContactData> mContactDataList;
    private ContactRecyclerAdapter mContactRecyclerAdapter;

    @Override
    protected ContactsContract.Presenter initPresenter() {
        return new ContactsPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mTopBar.setTitle(getString(R.string.contacts));

        mTopBar.addRightImageButton(R.drawable.add_contact,View.generateViewId())
                .setOnClickListener(v -> AddContactActivity.start(getActivity(), CONTACT_REQUEST_CODE));

        mContactDataList = new ArrayList<>();
        mContactRecyclerAdapter = new ContactRecyclerAdapter(mContactDataList);
        mContactRecycler.setAdapter(mContactRecyclerAdapter);
        mContactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactRecyclerAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<ContactData>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, ContactData data) {
                super.onItemClick(holder, data);
                ContactInfoFragment.newInstance(data).show(getFragmentManager(), "info");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        loadContactList();
    }

    /**
     * 加载联系人列表
     */
    public void loadContactList() {
        mContactDataList = mPresenter.fetchContacts();
        if (mContactDataList.size() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
        mContactRecyclerAdapter.replace(mContactDataList);
    }


    private void showEmptyView() {
        mEmptyView.show(false, getString(R.string.you_have_no_contact), null,
                getString(R.string.add_now), view -> AddContactActivity.start(getActivity(), CONTACT_REQUEST_CODE));
    }

    private void hideEmptyView() {
        if (mEmptyView.isShowing()) {
            mEmptyView.hide();
        }
    }

}

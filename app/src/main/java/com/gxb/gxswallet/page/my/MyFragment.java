package com.gxb.gxswallet.page.my;

import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.webview.RefreshAgentWebActivity;
import com.gxb.gxswallet.page.setting.SettingActivity;
import com.gxb.gxswallet.page.walletmanager.WalletManagerActivity;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sxds.common.app.PresenterFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/24.
 */

public class MyFragment extends PresenterFragment<MyContract.Presenter>
        implements MyContract.View, View.OnClickListener {

    @BindView(R.id.groupListView_my)
    QMUIGroupListView mGroupListView;
    private String[] partOneSettings;
    private String[] partTwoSettings;
    private String[] partThreeSettings;

    @Override
    protected MyContract.Presenter initPresenter() {
        return new MyPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_my;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initMenus();
        List<QMUICommonListItemView> partOneItemViews = generateMenus(partOneSettings);
        List<QMUICommonListItemView> partTwoItemViews = generateMenus(partTwoSettings);
        List<QMUICommonListItemView> partThreeItemViews = generateMenus(partThreeSettings);

        QMUIGroupListView.Section partOneSection = QMUIGroupListView.newSection(getContext()).setTitle("");
        QMUIGroupListView.Section partTwoSection = QMUIGroupListView.newSection(getContext()).setTitle("");
        QMUIGroupListView.Section partThreeSection = QMUIGroupListView.newSection(getContext()).setTitle("");

        for (QMUICommonListItemView itemView : partOneItemViews) {
            partOneSection.addItemView(itemView, this);
        }
        for (QMUICommonListItemView itemView : partTwoItemViews) {
            partTwoSection.addItemView(itemView, this);
        }
        for (QMUICommonListItemView itemView : partThreeItemViews) {
            partThreeSection.addItemView(itemView, this);
        }
        partOneSection.addTo(mGroupListView);
        partTwoSection.addTo(mGroupListView);
        partThreeSection.addTo(mGroupListView);
    }

    private void initMenus() {
        partOneSettings = new String[]{
                getString(R.string.my_card),
                getString(R.string.system_setting),
        };
        partTwoSettings = new String[]{
                getString(R.string.gxs_community)
        };
        partThreeSettings = new String[]{
                getString(R.string.help_center),
                getString(R.string.about)
        };
    }

    private List<QMUICommonListItemView> generateMenus(String[] settings) {
        List<QMUICommonListItemView> itemViews = new ArrayList<>();
        for (String setting : settings) {
            QMUICommonListItemView myCardItemView = mGroupListView.createItemView(setting);
            myCardItemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
            itemViews.add(myCardItemView);
        }
        return itemViews;
    }


    @OnClick(R.id.my_wallet_my)
    void onMyWalletClick() {
        WalletManagerActivity.start(getActivity());
    }

    @OnClick(R.id.transaction_record_my)
    void onTransactionRecordClick() {

    }

    @Override
    public void onClick(View view) {
        if (view instanceof QMUICommonListItemView) {
            String text = ((QMUICommonListItemView) view).getText().toString();
            if (partTwoSettings[0].equals(text)) {
                RefreshAgentWebActivity.start(getActivity(), "https://forum.gxb.io/");
                return;
            }
            if (partOneSettings[1].equals(text)) {
                SettingActivity.start(getActivity());
                return;
            }
        }

    }
}

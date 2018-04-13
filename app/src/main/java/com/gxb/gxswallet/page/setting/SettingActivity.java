package com.gxb.gxswallet.page.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.main.MainActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sxds.common.app.BaseActivity;
import com.sxds.common.utils.LanguageUtil;

import java.util.Locale;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/4/12.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.topbar_setting)
    QMUITopBar mTopBar;
    @BindView(R.id.groupListView_setting)
    QMUIGroupListView mGroupListView;

    private QMUICommonListItemView languageItem;
    private String[] languages;
    private String language;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        languageItem = mGroupListView.createItemView(getString(R.string.language));
        initLanguage();
        language = LanguageUtil.getInstance().getAppLanguage(this);
        if (language.equals(Locale.CHINA.getLanguage())) {
            language = getString(R.string.chinese);
        } else if (language.equals(Locale.ENGLISH.getLanguage())) {
            language = getString(R.string.english);
        }
        languageItem.setDetailText(language);
        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(languageItem, this)
                .addTo(mGroupListView);
    }

    private void initLanguage() {
        languages = new String[]{
                getString(R.string.chinese),
                getString(R.string.english)
        };
    }

    private void initTopBar() {
        mTopBar.setTitle(R.string.system_setting);
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {
        if (view instanceof QMUICommonListItemView) {
            String text = ((QMUICommonListItemView) view).getText().toString();
            if (getString(R.string.language).equals(text)) {
                showLanguageDialog(languages, getCurrentLanguageIndex());
            }
        }
    }

    private void showLanguageDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    Locale locale = Locale.SIMPLIFIED_CHINESE;
                    if (which == 1) {
                        locale = Locale.ENGLISH;
                    }
                    LanguageUtil.getInstance().changeAppLanguageSetting(SettingActivity.this,MainActivity.class,locale);
                })
                .show();
    }

    private int getCurrentLanguageIndex() {
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                return i;
            }
        }
        return 0;
    }
}

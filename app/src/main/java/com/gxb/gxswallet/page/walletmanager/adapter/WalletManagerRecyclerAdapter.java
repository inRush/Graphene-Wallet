package com.gxb.gxswallet.page.walletmanager.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.config.AssetSymbol;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/27.
 */

public class WalletManagerRecyclerAdapter extends RecyclerAdapter<WalletData> {

    public WalletManagerRecyclerAdapter(List<WalletData> dataList) {
        super(dataList);
    }

    @Override
    protected int getItemViewType(int position, WalletData data) {
        return R.layout.item_wallet_manager;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<WalletData> {

        @BindView(R.id.avatar_wallet_manager_item)
        ImageView avatarIv;
        @BindView(R.id.name_wallet_manager_item)
        TextView nameTv;
        @BindView(R.id.gxs_count_wallet_manager_item)
        TextView gxsCountTv;
        @BindView(R.id.back_btn_wallet_manager_item)
        QMUIRoundButton isBackupIv;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(WalletData data) {
            try {
                avatarIv.setImageDrawable(Jdenticon.from(data.getName()).drawable());
                nameTv.setText(data.getName());
                if (data.getIsBackUp()) {
                    isBackupIv.setVisibility(View.GONE);
                } else {
                    isBackupIv.setVisibility(View.VISIBLE);
                }
                if (data.getBalances(AssetSymbol.GXS) != null) {
                    gxsCountTv.setText(String.valueOf(data.getBalances(AssetSymbol.GXS).getAmount() / 100000.0));
                }

            } catch (IOException | SVGParseException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.gxb.gxswallet.page.home.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/20.
 */

public class DrawerWalletRecyclerAdapter extends RecyclerAdapter<WalletData> {

    private int currentSelect = 0;

    public DrawerWalletRecyclerAdapter(List<WalletData> wallets) {
        super(wallets);
    }


    @Override
    protected int getItemViewType(int position, WalletData data) {
        return R.layout.item_drawer_wallet;
    }

    @Override
    protected RecyclerAdapter.ViewHolder<WalletData> onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    @Override
    protected void onItemClick(View v) {
        super.onItemClick(v);
        ViewHolder viewHolder = (ViewHolder) v.getTag(com.sxds.common.R.id.tag_recycler_holder);
        currentSelect = viewHolder.getAdapterPosition();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<WalletData> {

        @BindView(R.id.avatar_drawer_wallet_item)
        ImageView avatar;
        @BindView(R.id.name_drawer_wallet_item)
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(WalletData data) {
            try {
                if (getAdapterPosition() == currentSelect) {
                    itemView.setBackgroundColor(Color.parseColor("#eeeeee"));
                } else {
                    itemView.setBackgroundColor(Color.WHITE);
                }
                avatar.setImageDrawable(Jdenticon.from(data.getName()).drawable());
                name.setText(data.getName());
            } catch (IOException | SVGParseException e) {
                e.printStackTrace();
            }
        }

    }
}

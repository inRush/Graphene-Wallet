package com.gxb.gxswallet.page.choose_coin.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/4/11.
 */

public class ChooseCoinRecyclerAdapter extends RecyclerAdapter<CoinItem> {

    public ChooseCoinRecyclerAdapter(List<CoinItem> dataList) {
        super(dataList);
    }

    @Override
    protected int getItemViewType(int position, CoinItem data) {
        return R.layout.item_choose_coin;
    }

    @Override
    protected RecyclerAdapter.ViewHolder<CoinItem> onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<CoinItem> {

        @BindView(R.id.coin_image_choose_coin_item)
        ImageView mAvatarIv;
        @BindView(R.id.name_choose_coin_item)
        TextView mNameTv;
        @BindView(R.id.switch_btn_choose_coin_item)
        CheckBox mSwitchBtn;
        @BindView(R.id.item_layout_choose_coin_item)
        LinearLayout mItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void onBind(CoinItem data) {
            mSwitchBtn.setChecked(data.isEnable());
            mAvatarIv.setImageBitmap(data.getIcon());
            mNameTv.setText(data.getName());
            itemView.setOnClickListener(v -> {
                onItemClick(v);
                mSwitchBtn.toggle();
            });
        }
    }

}

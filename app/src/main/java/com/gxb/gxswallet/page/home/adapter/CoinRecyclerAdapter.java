package com.gxb.gxswallet.page.home.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class CoinRecyclerAdapter extends RecyclerAdapter<CoinItem> {

    /**
     * 设置保留的位数
     */
    private DecimalFormat df = new DecimalFormat("0.00");

    public CoinRecyclerAdapter(List<CoinItem> dataList) {
        super(dataList);
    }

    @Override
    protected int getItemViewType(int position, CoinItem data) {
        return R.layout.item_coin_recycler;
    }

    @Override
    protected RecyclerAdapter.ViewHolder<CoinItem> onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<CoinItem> {
        @BindView(R.id.coin_image_coin_item)
        ImageView image;
        @BindView(R.id.name_coin_item)
        TextView name;
        @BindView(R.id.num_coin_item)
        TextView num;
        @BindView(R.id.price_coin_item)
        TextView price;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(CoinItem data) {
            image.setImageBitmap(data.getIcon());
            name.setText(data.getName());
            num.setText(String.valueOf(data.getAmount()));
            price.setText(App.getInstance().getString(R.string.total_price, df.format(data.getPrice())));
        }
    }
}

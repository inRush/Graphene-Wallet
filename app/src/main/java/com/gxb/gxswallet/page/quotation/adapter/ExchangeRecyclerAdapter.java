package com.gxb.gxswallet.page.quotation.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.gxb.gxswallet.R;
import com.gxb.sdk.models.GXSExchange;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/15.
 */

public class ExchangeRecyclerAdapter extends RecyclerAdapter<GXSExchange> {

    public ExchangeRecyclerAdapter(List<GXSExchange> exchanges) {
        super(exchanges);
    }

    @Override
    protected int getItemViewType(int position, GXSExchange data) {
        return R.layout.item_exchange_recycler;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GXSExchange> {

        @BindView(R.id.symbol_exchange)
        TextView symbolTv;
        @BindView(R.id.name_exchange)
        TextView nameTv;
        @BindView(R.id.price_exchange)
        TextView priceTv;
        @BindView(R.id.price_rmb_exchange)
        TextView rmbPriceTv;
        @BindView(R.id.quote_exchange)
        QMUIRoundButton quoteBtn;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onBind(GXSExchange data) {
            symbolTv.setText(data.getSymbol());
            nameTv.setText(data.getName());
            priceTv.setText(data.getPrice());
            rmbPriceTv.setText(String.format("￥%s", data.getPrice_rmb()));
            String quote;
            if (data.getQuote() < 0) {
                quoteBtn.setEnabled(true);
                quote = String.valueOf(data.getQuote()) + "%";
            } else {
                quoteBtn.setEnabled(false);
                quote = "+" + String.valueOf(data.getQuote()) + "%";

            }
            quoteBtn.setText(quote);
        }

    }
}

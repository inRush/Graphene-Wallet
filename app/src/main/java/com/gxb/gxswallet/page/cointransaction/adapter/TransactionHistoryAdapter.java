package com.gxb.gxswallet.page.cointransaction.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class TransactionHistoryAdapter extends RecyclerAdapter<TransactionHistory> {

    private AssetData mCoin;

    public TransactionHistoryAdapter(List<TransactionHistory> dataList, AssetData coinItem) {
        super(dataList);
        this.mCoin = coinItem;
    }

    @Override
    protected int getItemViewType(int position, TransactionHistory data) {
        return R.layout.item_transaction_history;
    }

    @Override
    protected RecyclerAdapter.ViewHolder<TransactionHistory> onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<TransactionHistory> {

        @BindView(R.id.avatar_transaction_history)
        ImageView mAvatarIv;
        @BindView(R.id.name_transaction_history)
        TextView mNameTv;
        @BindView(R.id.type_transaction_history)
        TextView mTypeTv;
        @BindView(R.id.amount_transaction_history)
        TextView mAmountTv;
        @BindView(R.id.asset_transaction_history)
        TextView mAssetTv;
        @BindView(R.id.time_transaction_history)
        TextView mTimeTv;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TransactionHistory data) {
            try {
                if (data.getType() == TransactionHistory.TransactionType.send) {
                    mAvatarIv.setImageDrawable(Jdenticon.from(data.getTo()).drawable());
                    mTypeTv.setText(R.string.send);
                    mNameTv.setText(data.getTo());
                    mAmountTv.setText(String.format(Locale.CHINA, "- %f", data.getAmount()));
                    mAmountTv.setTextColor(App.getInstance().getResources().getColor(R.color.red_100));
                } else {
                    mAvatarIv.setImageDrawable(Jdenticon.from(data.getFrom()).drawable());
                    mTypeTv.setText(R.string.receive);
                    mNameTv.setText(data.getFrom());
                    mAmountTv.setText(String.format(Locale.CHINA, "+ %f", data.getAmount()));
                    mAmountTv.setTextColor(App.getInstance().getResources().getColor(R.color.green_100));
                }
                mTimeTv.setText(data.getDate());
                mAssetTv.setText(mCoin.getName());
                data.setAsset(mCoin.getName());
            } catch (IOException | SVGParseException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.gxb.gxswallet.page.contacts.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ContactRecyclerAdapter extends RecyclerAdapter<ContactData> {

    public ContactRecyclerAdapter(List<ContactData> dataList) {
        super(dataList);
    }

    @Override
    protected int getItemViewType(int position, ContactData data) {
        return R.layout.item_contact_recycler;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<ContactData> {
        @BindView(R.id.avatar_contact_item)
        ImageView avatarIv;
        @BindView(R.id.name_contact_item)
        TextView nameTv;
        @BindView(R.id.address_contact_item)
        TextView addressTv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(ContactData data) {
            try {
                avatarIv.setImageDrawable(Jdenticon.from(data.getAddress()).drawable());
                nameTv.setText(data.getName());
                addressTv.setText(data.getAddress());
            } catch (IOException | SVGParseException e) {
                e.printStackTrace();
            }
        }
    }
}

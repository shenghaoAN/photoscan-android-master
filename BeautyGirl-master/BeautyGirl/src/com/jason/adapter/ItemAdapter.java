package com.jason.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.ItemCategoryBean;
import com.jason.global.CommonData;
import com.jason.hao.ImageActivity;
import com.jason.hao.R;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author jason
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<ItemCategoryBean> list;

    public ItemAdapter(Context context, List<ItemCategoryBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    public void updateAdapter(List<ItemCategoryBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_row, null);
            viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.txt_description = (TextView) convertView.findViewById(R.id.txt_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_title.setText(list.get(position).title);
        if (list.get(position).ftags.isEmpty()) {
            viewHolder.txt_description.setText(Html.fromHtml(list.get(position).tag));
        } else {
            viewHolder.txt_description.setText(Html.fromHtml(list.get(position).ftags));
        }
        imageLoader.displayImage(list.get(position).icon, viewHolder.img_icon, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));

        viewHolder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.PHOTO, list.get(position).icon);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView img_icon;
        TextView txt_title;
        TextView txt_description;
    }
}

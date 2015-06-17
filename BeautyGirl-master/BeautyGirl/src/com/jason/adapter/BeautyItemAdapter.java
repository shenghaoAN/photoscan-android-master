package com.jason.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.bean.CartoonObject;
import com.jason.global.CommonData;
import com.jason.hao.R;
import com.jason.hao.ZoomProductActivity;
import com.jason.utils.UniversalImageLoadTool;
import com.jason.view.ScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 瀑布流Adapter
 *
 * @author jason
 */
public class BeautyItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<CartoonObject> list;
    private ImageLoader imageLoader;
    private ViewHolder viewHolder;

    public BeautyItemAdapter(Context mContext, List<CartoonObject> list) {
        this.mContext = mContext;
        this.list = list;
        imageLoader = ImageLoader.getInstance();
    }

    public void updateAdapter(List<CartoonObject> list) {
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_beauty_row, null);
            viewHolder.content_layout = (LinearLayout) convertView
                    .findViewById(R.id.content_layout);
            viewHolder.thumbImage = (ScaleImageView) convertView
                    .findViewById(R.id.thumbImage);
            viewHolder.title_tag = (TextView) convertView
                    .findViewById(R.id.title_tag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_tag.setText(list.get(position).getDesc());

        UniversalImageLoadTool.loadimgAnimate(list.get(position).getImage_url(), viewHolder.thumbImage);

        viewHolder.content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomProductActivity.class);
                Bundle bundle = new Bundle();
                ArrayList arrayList = new ArrayList();
                arrayList.add(list);
                bundle.putParcelableArrayList(CommonData.LIST, arrayList);
                bundle.putInt(CommonData.POSITION, position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        // layout
        LinearLayout content_layout;
        // 缩略图
        ScaleImageView thumbImage;
        // 标签
        TextView title_tag;
    }

}

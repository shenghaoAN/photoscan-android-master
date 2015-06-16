package com.jason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.CartoonObject;
import com.jason.hao.R;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_beauty_row, null);
            viewHolder.thumbImage = (ImageView) convertView
                    .findViewById(R.id.thumbImage);
            viewHolder.img_like = (ImageView) convertView
                    .findViewById(R.id.img_like);
            viewHolder.title_tag = (TextView) convertView
                    .findViewById(R.id.title_tag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_tag.setText(list.get(position).getDesc());
        imageLoader.displayImage(list.get(position).getImage_url(), viewHolder.thumbImage,
                UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));

        return convertView;
    }

    static class ViewHolder {
        // 缩略图
        ImageView thumbImage;
        // 收藏
        ImageView img_like;
        // 标签
        TextView title_tag;
    }

}

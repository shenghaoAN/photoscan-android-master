package com.jason.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.ItemObject;
import com.jason.hao.R;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by shenghao on 2015/6/16.
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<ItemObject> list;

    public ItemAdapter(Context context, List<ItemObject> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
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
        viewHolder.txt_title.setText(list.get(position).getTitle());
        viewHolder.txt_description.setText(Html.fromHtml(list.get(position).getTag()));
        imageLoader.displayImage(list.get(position).getIcon(), viewHolder.img_icon, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        return convertView;
    }

    class ViewHolder {
        ImageView img_icon;
        TextView txt_title;
        TextView txt_description;
    }
}

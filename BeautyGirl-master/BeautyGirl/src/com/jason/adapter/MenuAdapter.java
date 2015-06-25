package com.jason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jason.bean.ItemCategoryBean;
import com.jason.hao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author jason
 */
public class MenuAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<ItemCategoryBean> list;

    public MenuAdapter(Context context, List<ItemCategoryBean> list) {
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
            convertView = inflater.inflate(R.layout.item_menu_row, null);
//            viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_title.setText(list.get(position).title);
//        imageLoader.displayImage(list.get(position).getIcon(), viewHolder.img_icon, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        return convertView;
    }

    class ViewHolder {
        //        ImageView img_icon;
        TextView txt_title;
    }

}

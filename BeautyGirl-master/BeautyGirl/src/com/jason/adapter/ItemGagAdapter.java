package com.jason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.object.GagObject;
import com.jason.hao.R;
import com.jason.utils.DensityUtils;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by asus-1 on 2015/7/1.
 */
public class ItemGagAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GagObject> list;
    private ImageLoader imageLoader;
    private int width;

    public ItemGagAdapter(Context context, List<GagObject> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        width = DensityUtils.getWidth(context);
    }

    public void updateAdapter(List<GagObject> list) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_gag_row, null);
            viewHolder.img_normal = (ImageView) convertView.findViewById(R.id.img_normal);
            viewHolder.txt_caption = (TextView) convertView.findViewById(R.id.txt_caption);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width - DensityUtils.dip2px(context, 10), 2 * width / 3);
            params.leftMargin = DensityUtils.dip2px(context, 5);
            params.rightMargin = DensityUtils.dip2px(context, 5);
            viewHolder.img_normal.setLayoutParams(params);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_caption.setText(list.get(position).getCaption());
        imageLoader.displayImage(list.get(position).getNormal(), viewHolder.img_normal
                , UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        return convertView;
    }

    class ViewHolder {
        TextView txt_caption;
        ImageView img_normal;
    }
}

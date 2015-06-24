package com.jason.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.bean.FavroiteBean;
import com.jason.dbservice.FavroiteBeanService;
import com.jason.hao.R;
import com.jason.utils.UniversalImageLoadTool;
import com.jason.view.ConfirmDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class FavroiteAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<FavroiteBean> list;
    private FavroiteBeanService service;

    public FavroiteAdapter(Context context, List<FavroiteBean> list, FavroiteBeanService service) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        this.service = service;
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
            convertView = inflater.inflate(R.layout.item_favroite_row, null);
            viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.txt_description = (TextView) convertView.findViewById(R.id.txt_description);
            viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_title.setText(list.get(position).tag);
        viewHolder.txt_description.setText(list.get(position).description);
        imageLoader.displayImage(list.get(position).image_url, viewHolder.img_icon, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.Show(context, "", context.getString(R.string.delete_confirm), context.getString(R.string.ok),
                        context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                ConfirmDialog.Hide();
                                DeleteItem(list.get(position));
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                ConfirmDialog.Hide();
                            }
                        }
                );
            }
        });
        return convertView;
    }

    /**
     * 删除收藏item
     */
    private void DeleteItem(FavroiteBean favroiteBean) {
        service.delete(favroiteBean.id);
        list.remove(favroiteBean);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView img_icon;
        TextView txt_title;
        TextView txt_description;
        ImageView img_delete;
    }

}

package com.jason.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.bean.FavroiteBean;
import com.jason.bean.ItemCartoonDetailBean;
import com.jason.dbservice.FavroiteBeanService;
import com.jason.global.CommonData;
import com.jason.hao.R;
import com.jason.hao.ZoomProductActivity;
import com.jason.pinnedheaderlistview.SectionedBaseAdapter;
import com.jason.utils.UniversalImageLoadTool;
import com.jason.view.ConfirmDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class FavroiteAdapter extends SectionedBaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<String> groupList;
    private List<List<FavroiteBean>> childList;
    private FavroiteBeanService service;
    private List<ItemCartoonDetailBean> itemCartoonDetailBeans;

    public FavroiteAdapter(Context context, List<String> groupList, List<List<FavroiteBean>> childList, FavroiteBeanService service) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        this.service = service;
        itemCartoonDetailBeans = new ArrayList<ItemCartoonDetailBean>();
    }

    @Override
    public Object getItem(int section, int position) {
        return childList.get(section).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return groupList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return childList.get(section).size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_favroite_row, null);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.txt_description = (TextView) convertView.findViewById(R.id.txt_description);
            viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FavroiteBean favroiteBean = childList.get(section).get(position);
        viewHolder.txt_title.setText(favroiteBean.tag);
        viewHolder.txt_description.setText(favroiteBean.description);
        imageLoader.displayImage(favroiteBean.image_url, viewHolder.img_icon, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.Show(context, "", context.getString(R.string.delete_confirm), context.getString(R.string.ok),
                        context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                ConfirmDialog.Hide();
                                DeleteItem(favroiteBean, section);
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

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataForFavroiteBean(groupList.get(section));
                Intent intent = new Intent(context, ZoomProductActivity.class);
                Bundle bundle = new Bundle();
                ArrayList arrayList = new ArrayList();
                arrayList.add(itemCartoonDetailBeans);
                bundle.putParcelableArrayList(CommonData.LIST, arrayList);
                bundle.putInt(CommonData.POSITION, position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) inflater.inflate(R.layout.item_header, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.txt_header_item)).setText(String.format(
                context.getString(R.string.favroite_head_title),
                groupList.get(section), childList.get(section).size()));
        return layout;
    }

    /**
     * 删除收藏item
     */
    private void DeleteItem(FavroiteBean favroiteBean, int section) {
        service.delete(favroiteBean.id);
        childList.get(section).remove(favroiteBean);
        notifyDataSetChanged();
    }

    /**
     * 将数据库获取的数据保存到list中,用于传递到ZoomProductActivity中显示
     *
     * @param tag 根据tag获取list
     */
    private void getDataForFavroiteBean(String tag) {
        List<FavroiteBean> list = service.findListByTag(tag);
        if (itemCartoonDetailBeans != null && !itemCartoonDetailBeans.isEmpty())
            itemCartoonDetailBeans.clear();
        if (list != null && !list.isEmpty()) {
            for (FavroiteBean favroiteBean : list) {
                ItemCartoonDetailBean itemCartoonDetailBean = new ItemCartoonDetailBean();
                itemCartoonDetailBean.desc = favroiteBean.description;
                itemCartoonDetailBean.colum = favroiteBean.colum;
                itemCartoonDetailBean.tag = favroiteBean.tag;
                itemCartoonDetailBean.image_url = favroiteBean.image_url;
                itemCartoonDetailBean.share_url = favroiteBean.share_url;
                itemCartoonDetailBeans.add(itemCartoonDetailBean);
            }
        }
    }

    class ViewHolder {
        LinearLayout linearLayout;
        ImageView img_icon;
        TextView txt_title;
        TextView txt_description;
        ImageView img_delete;
    }
}




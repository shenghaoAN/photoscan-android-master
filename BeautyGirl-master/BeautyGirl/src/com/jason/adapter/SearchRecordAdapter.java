package com.jason.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.bean.SearchBean;
import com.jason.dbservice.SearchBeanService;
import com.jason.global.CommonData;
import com.jason.hao.DetailActivity;
import com.jason.hao.R;
import com.jason.helper.DateHelper;
import com.jason.pinnedheaderlistview.SectionedBaseAdapter;
import com.jason.view.ConfirmDialog;

import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordAdapter extends SectionedBaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> groupList;
    private List<List<SearchBean>> childList;
    private SearchBeanService searchBeanService;

    public SearchRecordAdapter(Context context, SearchBeanService searchBeanService, List<String> groupList, List<List<SearchBean>> childList) {
        this.context = context;
        this.searchBeanService = searchBeanService;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_search_record_row, parent, false);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            viewHolder.txt_text = (TextView) convertView.findViewById(R.id.txt_text);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SearchBean searchBean = childList.get(section).get(position);
        viewHolder.txt_text.setText(searchBean.text);
        viewHolder.txt_date.setText(DateHelper.formatDate(searchBean.date));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.TAG, searchBean.text);
                bundle.putString(CommonData.TITLE, searchBean.column);
                intent.putExtras(bundle);
                context.startActivity(intent);

/*                Intent intent = new Intent(context, PhotoSearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.WORD, searchBean.text);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
            }
        });

        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ConfirmDialog.Show(context, "", context.getString(R.string.delete_confirm), context.getString(R.string.ok),
                        context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                ConfirmDialog.Hide();
                                deleteItem(searchBean, section);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                ConfirmDialog.Hide();
                            }
                        }
                );
                return false;
            }
        });
        return convertView;
    }

    /**
     * 删除搜索记录
     */
    private void deleteItem(SearchBean searchBean, int section) {
        searchBeanService.delete(searchBean.id);
        childList.get(section).remove(searchBean);
        if (childList.get(section).isEmpty())
            groupList.remove(section);
        notifyDataSetChanged();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) inflater.inflate(R.layout.item_header, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.txt_header_item)).setText(groupList.get(section));
        return layout;
    }

    class ViewHolder {
        LinearLayout linearLayout;
        TextView txt_text;
        TextView txt_date;
    }
}

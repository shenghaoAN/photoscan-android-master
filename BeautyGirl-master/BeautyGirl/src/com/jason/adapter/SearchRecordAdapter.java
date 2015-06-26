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
    private List<String> grouplist;
    private List<List<SearchBean>> lists;
    private SearchBeanService searchBeanService;

    public SearchRecordAdapter(Context context, SearchBeanService searchBeanService, List<String> grouplist, List<List<SearchBean>> lists) {
        this.context = context;
        this.searchBeanService = searchBeanService;
        this.grouplist = grouplist;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int section, int position) {
        return lists.get(section).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return grouplist.size();
    }

    @Override
    public int getCountForSection(int section) {
        return lists.get(section).size();
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
        viewHolder.txt_text.setText(lists.get(section).get(position).text);
        viewHolder.txt_date.setText(DateHelper.formatDate(lists.get(section).get(position).date));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBean searchBean = lists.get(section).get(position);
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CommonData.TAG, searchBean.text);
                bundle.putString(CommonData.TITLE, searchBean.column);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
                                DeleteItem(lists.get(section).get(position), section);
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
    private void DeleteItem(SearchBean searchBean, int section) {
        searchBeanService.delete(searchBean.id);
        lists.get(section).remove(searchBean);
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
        ((TextView) layout.findViewById(R.id.txt_header_item)).setText(grouplist.get(section));
        return layout;
    }

    class ViewHolder {
        LinearLayout linearLayout;
        TextView txt_text;
        TextView txt_date;
    }
}

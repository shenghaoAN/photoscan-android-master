package com.jason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jason.bean.SearchBean;
import com.jason.hao.R;
import com.jason.helper.DateHelper;

import java.util.List;

/**
 * Created by shenghao on 2015/6/24.
 */
public class SearchRecordAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<SearchBean> list;

    public SearchRecordAdapter(Context context, List<SearchBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_search_record_row, parent, false);
            viewHolder.txt_text = (TextView) convertView.findViewById(R.id.txt_text);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_text.setText(list.get(position).text);
        viewHolder.txt_date.setText(DateHelper.formatDate(list.get(position).date));
        return convertView;
    }

    class ViewHolder {
        TextView txt_text;
        TextView txt_date;
    }

}

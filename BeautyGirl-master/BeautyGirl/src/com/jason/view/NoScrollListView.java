package com.jason.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 *
 * 解决scrollview嵌套listview滑动问题
 *
 * Created by shenghao on 2015/6/3.
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}

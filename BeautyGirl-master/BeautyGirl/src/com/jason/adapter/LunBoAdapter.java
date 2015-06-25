package com.jason.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jason.bean.ItemCartoonDetailBean;
import com.jason.global.CommonData;
import com.jason.hao.R;
import com.jason.hao.ZoomProductActivity;
import com.jason.utils.UniversalImageLoadTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播适配
 * <p/>
 *
 * @author jason
 */
public class LunBoAdapter extends PagerAdapter {

    private Context context;
    private List<ItemCartoonDetailBean> list;
    private ImageLoader imageLoader;

    public LunBoAdapter(Context context, List<ItemCartoonDetailBean> list) {
        this.list = list;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (list != null && !list.isEmpty()) {
            final int park = position % list.size();
            final ItemCartoonDetailBean itemCartoonDetailBean = list.get(park);
            ImageView img = new ImageView(context);
            img.setTag(position);
            img.setScaleType(ScaleType.CENTER_CROP);
            imageLoader.displayImage(itemCartoonDetailBean.image_url, img, UniversalImageLoadTool.getImageOption(R.drawable.btn_upload_image));
            img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ZoomProductActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(list);
                    bundle.putParcelableArrayList(CommonData.LIST, arrayList);
                    bundle.putInt(CommonData.POSITION, park);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            ((ViewPager) container).addView(img, 0);
            return img;
        }
        return null;
    }

}

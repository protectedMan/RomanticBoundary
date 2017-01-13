package com.lanxin.romanticboundary.adapter;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.data.bean.GirlsBean;


/**
 * viewholder
 */
public class GirlsViewHolder extends BaseViewHolder<GirlsBean.ResultsEntity> {

    public ImageView image;

    public GirlsViewHolder(ViewGroup parent) {
      super(parent, R.layout.item_girl);
     image = $(R.id.girl_img);
    }

    @Override
    public void setData(GirlsBean.ResultsEntity data) {

//      super.setData(data);
     Log.i("test",data.getUrl());
        Glide.with(getContext())//平滑加载图片到image
                .load(data.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(image);
    }
}

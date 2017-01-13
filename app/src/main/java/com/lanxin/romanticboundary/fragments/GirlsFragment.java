package com.lanxin.romanticboundary.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.activitys.GirlActivity;
import com.lanxin.romanticboundary.adapter.GirlsAdapter;
import com.lanxin.romanticboundary.adapter.GirlsContract;
import com.lanxin.romanticboundary.adapter.GirlsPresenter;
import com.lanxin.romanticboundary.base.BaseActivity;
import com.lanxin.romanticboundary.data.bean.GirlsBean;
import com.lanxin.romanticboundary.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by oracleen on 2016/6/21.
 */
public class GirlsFragment extends Fragment implements GirlsContract.View, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    public static final String TAG = "GirlsFragment";


    @Bind(R.id.girls_recycler_view)
    EasyRecyclerView mGirlsRecyclerView;
    protected View rootView;


    private ArrayList<GirlsBean.ResultsEntity> data;
    private GirlsAdapter mAdapter;

    private GirlsPresenter mPresenter;
    private int page = 1;
    private int size = 20;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_girls, container, false);
            ButterKnife.bind(this, rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        ButterKnife.bind(this, rootView);
        initView(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("girls", data);

        super.onSaveInstanceState(outState);
    }

    protected void initView(View view, Bundle savedInstanceState) {
        mPresenter = new GirlsPresenter(this);
        initRecyclerView();
        if (savedInstanceState != null) {
            savedInstanceState.getCharSequenceArrayList("datas");
        } else {
            mGirlsRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mGirlsRecyclerView.getSwipeToRefresh().setRefreshing(true);
                }
            });
            //初始化数据
            onRefresh();
        }
    }

    private void initRecyclerView() {
        data = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mGirlsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new GirlsAdapter(getContext());

        mGirlsRecyclerView.setAdapterWithProgress(mAdapter);

        mAdapter.setMore(R.layout.load_more_layout, this);
        mAdapter.setNoMore(R.layout.no_more_layout);
        mAdapter.setError(R.layout.error_layout);
        mGirlsRecyclerView.setErrorView(R.layout.error_layout);
        mGirlsRecyclerView.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), GirlActivity.class);
                intent.putParcelableArrayListExtra("girls", data);
                intent.putExtra("current", position);
                startActivity(intent);
            }
        });

        mGirlsRecyclerView.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getGirls(page, size, true);
    }

    @Override
    public void onLoadMore() {
        if (data.size() % 20 == 0) {
            page++;
            mPresenter.getGirls(page, size, false);
        }
    }


    @Override
    public void refresh(List<GirlsBean.ResultsEntity> datas) {
        data.clear();
        data.addAll(datas);
        mAdapter.clear();
        mAdapter.addAll(datas);
    }

    @Override
    public void load(List<GirlsBean.ResultsEntity> datas) {
        data.addAll(datas);
        mAdapter.addAll(datas);
    }

    @Override
    public void showError() {
        mGirlsRecyclerView.showError();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

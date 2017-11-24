package com.itgoyo.retrofit.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itgoyo.retrofit.R;
import com.itgoyo.retrofit.module.BasicResponse;
import com.itgoyo.retrofit.module.bean.MeiZi;
import com.itgoyo.retrofit.net.DefaultObserver;
import com.itgoyo.retrofit.net.IdeaApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private Button  btn;
    private Context context;

    @BindView(R.id.lv)
    ListView mLv;

    Gson mGson;
    private List<MeiZi> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        context = MainActivity.this;
        mGson = new Gson();
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


    }

    public class GirlAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View inflate = inflater.inflate(R.layout.item, null);
            ViewHolder viewHolder = null;
            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item, null);
                viewHolder = new ViewHolder(convertView);
                viewHolder.mIv = (ImageView) convertView.findViewById(R.id.iv);
                viewHolder.mTv = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
//                Glide.with(context).load(mList.get(position).getUrl()).into(viewHolder.mIv);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(mList.get(position).getUrl()).into(viewHolder.mIv);
            viewHolder.mTv.setText(mList.get(position).getDesc());
            return convertView;
        }


         class ViewHolder {
            @BindView(R.id.iv)
            ImageView mIv;
             @BindView(R.id.tv)
             TextView mTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    public void getData() {
        IdeaApi.getApiService()
                .getMezi()
                .compose(this.<BasicResponse<List<MeiZi>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<MeiZi>>>(this) {
                    @Override
                    public void onSuccess(BasicResponse<List<MeiZi>> response) {
                        List<MeiZi> results = response.getResults();

                        mList = new ArrayList<>();

                        for (int i = 0; i < results.size(); i++) {
                            mList.add(i, results.get(i));
                        }

                        showToast("请求成功，妹子个数为" + results.size());


                        Log.i("itgoyo", "results-->" + results.toString());


                        if (mGson == null) {
                            mGson = new Gson();
                        }

                        GirlAdapter girlAdapter = new GirlAdapter();
                        mLv.setAdapter(girlAdapter);


                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

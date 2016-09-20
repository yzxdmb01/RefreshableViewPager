package com.example.jr.refreshableviewpager;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<View> viewList; //储存viewpager中的view
    private RVPAdapter mAdapter;
    private boolean hasMoreData = true;
    private int pageSize = 4;
    private int page = 0;   //记录当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initView();
    }

    private void findView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initView() {
        viewList = new ArrayList<>();
        View loadView = LayoutInflater.from(this).inflate(R.layout.viewpager_load, null);
        viewList.add(loadView);

        mAdapter = new RVPAdapter(viewList);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        //两种动画模式
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        viewPager.setPageTransformer(true, new ScalePageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (hasMoreData && position == viewList.size() - 1) {
                    //滚动到最后了开始加载下一页数据
                    Toast.makeText(MainActivity.this, "正在加载..", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(++page);
                            /*重新进入当前页*/
                            viewPager.setCurrentItem(Math.min(position - 1, viewList.size() - 2), false);
                            viewPager.setCurrentItem(Math.min(position, viewList.size() - 1));
                        }
                    }, 1 * 1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        loadData(page);
    }


    /**
     * 模拟加载数据
     *
     * @param page
     */
    private void loadData(int page) {
        this.page = page;
        List<Integer> imgs = new ArrayList<>();
        if (page == 3) {
            //模拟加载到第三页则没有数据了
        } else {
            if (page % 2 == 1) {
                imgs.add(R.drawable.img01);
                imgs.add(R.drawable.img02);
                imgs.add(R.drawable.img03);
                imgs.add(R.drawable.img04);
            } else {
                imgs.add(R.drawable.img05);
                imgs.add(R.drawable.img06);
                imgs.add(R.drawable.img07);
                imgs.add(R.drawable.img08);
            }
        }

        //根据获取的数据生成view添加到viewList中
        for (int i = 0; i < imgs.size(); i++) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.viewpager_item, null);
            ImageView ivMain = (ImageView) view.findViewById(R.id.iv_main);
            TextView tvIntro = (TextView) view.findViewById(R.id.tv_intro);
            ivMain.setImageResource(imgs.get(i));
            tvIntro.setText("item:" + i);
            viewList.add(viewList.size() - 1, view);
        }
        if (imgs.size() < pageSize) {
            //没有更多数据了移除loadView;
            hasMoreData = false;
            viewList.remove(viewList.size() - 1);
        }
        mAdapter.notifyDataSetChanged();
    }


}

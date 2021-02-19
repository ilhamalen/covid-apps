package com.daws.projects.codamation.views.activities;

import android.content.Intent;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityNewsDetailBinding;
import com.daws.projects.codamation.databinding.ItemNewsDetailBinding;
import com.daws.projects.codamation.helpers.DataHelper;
import com.daws.projects.codamation.models.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends BaseActivity<ActivityNewsDetailBinding> {

    private List<NewsModel> newsModelList;
    private SimpleRecyclerAdapter<NewsModel> newsAdapter;

    private JSONArray newsJSONArray;

    @Override
    protected int attachLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        super.initData();

        newsModelList = new ArrayList<>();
        newsJSONArray = DataHelper.newInstance().loadJSONFromAsset(this, "news_resource.json");
        try {
            for (int i = 0; i < newsJSONArray.length(); i++){
                JSONObject newsObject = newsJSONArray.getJSONObject(i);
                NewsModel newsModel = new NewsModel(this, newsObject);
                newsModelList.add(newsModel);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        newsAdapter= new SimpleRecyclerAdapter<>(
                newsModelList,
                R.layout.item_news_detail,
                ((holder, item) -> {
                    ItemNewsDetailBinding binding = (ItemNewsDetailBinding) holder.getLayoutBinding();
                    binding.setNewsModel(item);

                    Intent newsIntent = new Intent(this, NewsActivity.class);
                    newsIntent.putExtra(GlobalVariable.NAME_URL, item.getUrl());
                    newsIntent.putExtra(GlobalVariable.NAME_NEWS_TITLE, item.getTitle());

                    binding.setNewsModel(item);
                    binding.mainLayout.setOnClickListener(v -> startActivity(newsIntent));
                })
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        binding.setNewsAdapter(newsAdapter);
        binding.back.setOnClickListener(v -> finish());
    }
}

package com.daws.projects.codamation.views.activities;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityHomeBinding;
import com.daws.projects.codamation.databinding.ItemInfoBinding;
import com.daws.projects.codamation.databinding.ItemMediaBinding;
import com.daws.projects.codamation.databinding.ItemNewsBinding;
import com.daws.projects.codamation.helpers.DataHelper;
import com.daws.projects.codamation.models.MediaModel;
import com.daws.projects.codamation.models.NewsModel;
import com.daws.projects.codamation.viewmodels.SummaryGlobalCaseViewModel;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private List<Integer> imageInfoList;
    private SimpleRecyclerAdapter<Integer> imageInfoAdapter;
    private List<NewsModel> newsModelList;
    private SimpleRecyclerAdapter<NewsModel> newsModelAdapter;
    private List<MediaModel> mediaModelList;
    private SimpleRecyclerAdapter<MediaModel> mediaModelAdapter;

    private SummaryGlobalCaseViewModel summaryGlobalCaseViewModel;

    private boolean pressedOnce = false;
    private JSONArray newsJSONArray;
    private JSONArray mediaJSONArray;

    @Override
    protected int attachLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        super.initData();

        summaryGlobalCaseViewModel = ViewModelProviders.of(this).get(SummaryGlobalCaseViewModel.class);

        imageInfoList = new ArrayList<>();
        imageInfoList.add(R.drawable.slide1);
        imageInfoList.add(R.drawable.slide2);
        imageInfoList.add(R.drawable.slide3);
        imageInfoList.add(R.drawable.slide4);

        imageInfoAdapter = new SimpleRecyclerAdapter<>(
                imageInfoList,
                R.layout.item_info,
                ((holder, item) -> {
                    ItemInfoBinding binding = (ItemInfoBinding) holder.getLayoutBinding();
                    binding.image.setImageResource(item);
                })
        );


        newsModelList = new ArrayList<>();
        newsJSONArray = DataHelper.newInstance().loadJSONFromAsset(this, "news_resource.json");
        try {
            for (int i = 0; i < 2; i++){
                JSONObject newsObject = newsJSONArray.getJSONObject(i);
                NewsModel newsModel = new NewsModel(this, newsObject);
                newsModelList.add(newsModel);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        newsModelAdapter = new SimpleRecyclerAdapter<>(
                newsModelList,
                R.layout.item_news,
                ((holder, item) -> {
                    ItemNewsBinding binding = (ItemNewsBinding) holder.getLayoutBinding();
                    Intent newsIntent = new Intent(this, NewsActivity.class);
                    newsIntent.putExtra(GlobalVariable.NAME_URL, item.getUrl());
                    newsIntent.putExtra(GlobalVariable.NAME_NEWS_TITLE, item.getTitle());

                    binding.setNewsModel(item);
                    binding.news.setOnClickListener(v -> startActivityForResult(newsIntent, GlobalVariable.REQUEST_REFRESH));
                })
        );

        mediaModelList = new ArrayList<>();
        mediaJSONArray = DataHelper.newInstance().loadJSONFromAsset(this, "media_resource.json");
        try {
            for (int i = 0; i < mediaJSONArray.length(); i++){
                JSONObject mediaObject = mediaJSONArray.getJSONObject(i);
                MediaModel mediaModel = new MediaModel(mediaObject);
                mediaModelList.add(mediaModel);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        mediaModelAdapter = new SimpleRecyclerAdapter<>(
                mediaModelList,
                R.layout.item_media,
                ((holder, item) -> {
                    ItemMediaBinding binding = (ItemMediaBinding) holder.getLayoutBinding();
                    binding.setMedia(item);

                    Glide.with(HomeActivity.this)
                            .load(item.getThumbnailUrl())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                            .into(binding.image);

                    binding.mainLayout.setOnClickListener(v -> {
                        Intent mediaIntent = new Intent(this, MediaActivity.class);
                        mediaIntent.putExtra(GlobalVariable.NAME_MEDIA_ID, item.getId());
                        startActivity(mediaIntent);
                    });
                })
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        binding.setInfoAdapter(imageInfoAdapter);
        binding.setNewsAdapter(newsModelAdapter);
        binding.setMediaAdapter(mediaModelAdapter);

        getNetworkData();

        binding.refresh.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP){
                binding.refresh.setRefreshing(true);
                getNetworkData();
            }
        });
        binding.newsMore.setOnClickListener(v -> startActivity(new Intent(this, NewsDetailActivity.class)));
        binding.localDetail.setOnClickListener(v ->
                startActivityForResult(new Intent(this, LocalCaseActivity.class), GlobalVariable.REQUEST_REFRESH));
        binding.globalDetail.setOnClickListener(v ->
                startActivityForResult(new Intent(this, GlobalCaseActivity.class), GlobalVariable.REQUEST_REFRESH));
        binding.info.setOnClickListener(v ->
                startActivityForResult(new Intent(this, InformationActivity.class), GlobalVariable.REQUEST_REFRESH));
    }

    private void getNetworkData(){
        summaryGlobalCaseViewModel.getGlobalSummaryCaseLiveData()
                .observe(this, globalSummaryCaseModel -> {
                    binding.setGlobalModel(globalSummaryCaseModel);
                    binding.setLastUpdate(globalSummaryCaseModel.getLastUpdate());
                });

        summaryGlobalCaseViewModel.getLocalSummaryCaseLiveData()
                .observe(this, localSummaryCaseModel -> {
                    binding.setIndonesiaModel(localSummaryCaseModel);
                    binding.refresh.setRefreshing(false);
                });
    }

    @Override
    public void onBackPressed() {

        if (pressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        this.pressedOnce = true;
        Toast.makeText(this, getString(R.string.warning_press_once_to_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> pressedOnce = false, 1500);
    }
}

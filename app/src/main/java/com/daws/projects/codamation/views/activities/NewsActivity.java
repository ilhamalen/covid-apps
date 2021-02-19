package com.daws.projects.codamation.views.activities;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.ActivityNewsBinding;
import com.daws.projects.codamation.helpers.UIHelper;
import com.daws.projects.codamation.helpers.ValidationHelper;

public class NewsActivity extends BaseActivity<ActivityNewsBinding>{

    private String title;
    private String url;

    @Override
    protected int attachLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void initData() {
        super.initData();

        if (getIntent().getExtras().containsKey(GlobalVariable.NAME_NEWS_TITLE))
            title = getIntent().getExtras().getString(GlobalVariable.NAME_NEWS_TITLE);
        else
            title = null;

        if (getIntent().getExtras().containsKey(GlobalVariable.NAME_URL))
            url = getIntent().getExtras().getString(GlobalVariable.NAME_URL);
        else
            url = null;
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        binding.setLoading(true);
        binding.setTitle(title);

        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.setLoading(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                UIHelper.newInstance(getApplication()).showErrorToast("Error Load Page");
                finish();
            }
        });

        if (ValidationHelper.checkNotNullOrEmpty(url))
            binding.webview.loadUrl(url);
        else{
            UIHelper.newInstance(this).showErrorToast("URL Not Found");
            finish();
        }

        binding.back.setOnClickListener(v -> finish());
    }
}

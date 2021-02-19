package com.daws.projects.codamation.views.fragments;

import android.content.Context;

import androidx.lifecycle.ViewModelProviders;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.FragmentDashboardBinding;
import com.daws.projects.codamation.models.NewsModel;
import com.daws.projects.codamation.viewmodels.SummaryGlobalCaseViewModel;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BaseFragment<FragmentDashboardBinding> {

    private SummaryGlobalCaseViewModel summaryGlobalCaseViewModel;
    private List<NewsModel> newsList;
    private SimpleRecyclerAdapter<NewsModel> newsAdapter;
    private DashboardFragmentListener listener;

    public DashboardFragment(){}

    public static DashboardFragment newInstance(){
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    public interface DashboardFragmentListener{
        void onLocalDetailClick();
        void onGlobalDetailClick();
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initData() {
        super.initData();
        summaryGlobalCaseViewModel = ViewModelProviders.of(this).get(SummaryGlobalCaseViewModel.class);

        newsList = new ArrayList<>();
        newsAdapter = new SimpleRecyclerAdapter<>(
                newsList,
                R.layout.item_news,
                ((holder, item) -> {

                })
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        summaryGlobalCaseViewModel.getGlobalSummaryCaseLiveData()
                .observe(this, globalSummaryCaseModel -> binding.setGlobalModel(globalSummaryCaseModel));

        summaryGlobalCaseViewModel.getLocalSummaryCaseLiveData()
                .observe(this, localSummaryCaseModel -> binding.setIndonesiaModel(localSummaryCaseModel));

        binding.setListener(listener);
        binding.setNewsAdapter(newsAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DashboardFragmentListener)
            listener = (DashboardFragmentListener) context;
        else
            throw new RuntimeException(context.toString() + " mus implement DashboardFragmentListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setListener(DashboardFragmentListener listener) {
        this.listener = listener;
    }
}

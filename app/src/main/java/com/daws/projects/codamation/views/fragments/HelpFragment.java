package com.daws.projects.codamation.views.fragments;

import android.os.Bundle;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.FragmentHelpBinding;


public class HelpFragment extends BaseFragment<FragmentHelpBinding> {

    public HelpFragment(){}

    public static HelpFragment newInstance(Bundle bundle){
        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initLayout() {
        super.initLayout();
    }
}

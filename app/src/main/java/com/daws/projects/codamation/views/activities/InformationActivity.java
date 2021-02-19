package com.daws.projects.codamation.views.activities;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.adapters.SimpleRecyclerAdapter;
import com.daws.projects.codamation.databinding.ActivityInformationBinding;
import com.daws.projects.codamation.databinding.ItemInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends BaseActivity<ActivityInformationBinding> {

    private List<Integer> imageInfoList;
    private SimpleRecyclerAdapter<Integer> imageInfoAdapter;

    @Override
    protected int attachLayout() {
        return R.layout.activity_information;
    }

    @Override
    protected void initData() {
        super.initData();

        imageInfoList = new ArrayList<>();
        imageInfoList.add(R.drawable.slide5);
        imageInfoList.add(R.drawable.slide6);
        imageInfoList.add(R.drawable.slide7);
        imageInfoList.add(R.drawable.slide8);

        imageInfoAdapter = new SimpleRecyclerAdapter<>(
                imageInfoList,
                R.layout.item_info,
                ((holder, item) -> {
                    ItemInfoBinding binding = (ItemInfoBinding) holder.getLayoutBinding();
                    binding.image.setImageResource(item);
                })
        );
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        binding.setInfoAdapter(imageInfoAdapter);
        binding.back.setOnClickListener(v -> finish());
    }
}

package com.daws.projects.codamation.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.ActivityDashboardBinding;
import com.daws.projects.codamation.utils.ActivityUtils;
import com.daws.projects.codamation.views.fragments.DashboardFragment;
import com.daws.projects.codamation.views.fragments.HelpFragment;
import com.daws.projects.codamation.views.fragments.InformationFragment;

public class DashboardActivity extends BaseActivity<ActivityDashboardBinding> implements DashboardFragment.DashboardFragmentListener {

    private DashboardFragment dashboardFragment;
    private InformationFragment informationFragment;
    private HelpFragment helpFragment;

    private boolean pressedOnce = false;

    @Override
    protected int attachLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        declareFragments();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), dashboardFragment, binding.frameLayout.getId()
        );

        binding.bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.dashboard : {

                    if (binding.bottomNavigation.getSelectedItemId() == R.id.dashboard){
                        return false;
                    }

                    ActivityUtils.replaceFragment(getSupportFragmentManager(), dashboardFragment, binding.frameLayout.getId());
                    return true;
                }

                case R.id.information : {

                    if (binding.bottomNavigation.getSelectedItemId() == R.id.information){
                        return false;
                    }

                    ActivityUtils.replaceFragment(getSupportFragmentManager(), informationFragment, binding.frameLayout.getId());
                    return true;
                }

                case R.id.help : {

                    if (binding.bottomNavigation.getSelectedItemId() == R.id.help){
                        return false;
                    }

                    ActivityUtils.replaceFragment(getSupportFragmentManager(), helpFragment, binding.frameLayout.getId());
                    return true;
                }
            }
            return false;
        });
    }

    private void declareFragments(){
        dashboardFragment = DashboardFragment.newInstance();
        informationFragment = new InformationFragment();
        helpFragment = new HelpFragment();

        dashboardFragment.setListener(this);
    }

    @Override
    public void onBackPressed() {

        if (pressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (binding.bottomNavigation.getSelectedItemId() == R.id.dashboard){
            this.pressedOnce = true;
            Toast.makeText(this, getString(R.string.warning_press_once_to_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> pressedOnce = false, 1500);
        }
        else
            binding.bottomNavigation.setSelectedItemId(R.id.dashboard);
    }

    @Override
    public void onLocalDetailClick() {
        startActivityForResult(new Intent(this, LocalCaseActivity.class), GlobalVariable.REQUEST_REFRESH);
    }

    @Override
    public void onGlobalDetailClick() {
        startActivityForResult(new Intent(this, GlobalCaseActivity.class), GlobalVariable.REQUEST_REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalVariable.REQUEST_REFRESH){
            if (resultCode == Activity.RESULT_OK)
                binding.bottomNavigation.setSelectedItemId(R.id.dashboard);
        }
    }
}

package com.daws.projects.codamation.views.activities;

import android.app.PictureInPictureParams;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.util.Rational;
import android.view.Display;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.daws.projects.codamation.GlobalVariable;
import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.ActivityMediaBinding;
import com.daws.projects.codamation.helpers.UIHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MediaActivity extends BaseVideoActivity<ActivityMediaBinding>{

    private String mediaId;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected int attachLayout() {
        return R.layout.activity_media;
    }

    @Override
    protected void initData() {
        super.initData();

        if (getIntent().getExtras().containsKey(GlobalVariable.NAME_MEDIA_ID))
            mediaId = getIntent().getExtras().getString(GlobalVariable.NAME_MEDIA_ID);
        else
            mediaId = null;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        binding.setMediaId(mediaId);
        initYouTubePlayerView();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        if(isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(false);
        } else {
            youTubePlayerView.exitFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(true);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    private void initYouTubePlayerView() {
        youTubePlayerView = binding.youtubePlayerView;

        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        mediaId,0f
                );
            }
        });
    }


    private void initPictureInPicture(YouTubePlayerView youTubePlayerView){
        ImageView closeIcon = new ImageView(this);
        closeIcon.setImageResource(R.drawable.ic_close_white);
        ImageView pictureInPictureIcon = new ImageView(this);
        pictureInPictureIcon.setImageResource(R.drawable.ic_picture_in_picture);

        closeIcon.setOnClickListener(view -> finish());

        pictureInPictureIcon.setOnClickListener( view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if(supportsPIP)
                    pictureInPictureVideo();
            } else {
                UIHelper.newInstance(this).showErrorToast("Versi android tidak mendukung");
            }
        });

        youTubePlayerView.getPlayerUiController().addView(closeIcon);
    }

    private void pictureInPictureVideo(){

        Rational ratio;
        Display d = getWindowManager()
                .getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int width = p.x;
        int height = p.y;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ratio = new Rational(height,width);

            PictureInPictureParams.Builder pip_Builder = new PictureInPictureParams.Builder();
            pip_Builder.setAspectRatio(ratio).build();
            enterPictureInPictureMode(pip_Builder.build());

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

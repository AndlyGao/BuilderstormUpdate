/**
 * Copyright (C) 2018 iDEA foundation pvt.,Ltd
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.builderstrom.user.views.activities;

import android.content.res.Configuration;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoPlayerActivity extends BaseActivity {
    @BindView(R.id.youtube_player_view) YouTubePlayerView playerView;
    private String videoUrl = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void init() {
        videoUrl = getIntent().getStringExtra("videoUrl");
        hidePlayerControls();
        getLifecycle().addObserver(playerView);
        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                if (!videoUrl.isEmpty()) {
                    hidePlayerControls();
                    youTubePlayer.loadVideo(CommonMethods.getVideoId(videoUrl), 0);
                }
            }
        });
    }

    private void hidePlayerControls() {
        if (playerView != null) {
            playerView.getPlayerUiController().showCustomAction1(false);
            playerView.getPlayerUiController().showCustomAction2(false);
            playerView.getPlayerUiController().showMenuButton(false);
        }
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != playerView) {
            playerView.release();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setPlayerViewDimen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    private void setPlayerViewDimen(boolean isLandscape) {

        if (playerView != null) {
            playerView.getLayoutParams().height = isLandscape ? ViewGroup.LayoutParams.MATCH_PARENT : getResources().getDimensionPixelSize(R.dimen.dimen_240);
        }
    }

}



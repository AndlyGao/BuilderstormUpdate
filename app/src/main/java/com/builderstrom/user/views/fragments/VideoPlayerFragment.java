//package com.builderstrom.user.views.fragments;
//
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//
//import com.builderstrom.user.R;
//import com.builderstrom.user.utils.CommonMethods;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//public class VideoPlayerFragment extends BaseFragment {
//
//    private String videoUrl = "";
//
//    public static VideoPlayerFragment newInstance(String videoUrl) {
//        VideoPlayerFragment fragment = new VideoPlayerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("videoUrl", videoUrl);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @BindView(R.id.youtube_player_view) YouTubePlayerView playerView;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_video_player;
//    }
//
//    @Override
//    protected void bindView(View view) {
//        if (getArguments() != null) {
//            videoUrl = getArguments().getString("videoUrl");
//        }
//        hidePlayerControls();
//        getViewLifecycleOwner().getLifecycle().addObserver(playerView);
//    }
//
//    private void hidePlayerControls() {
//        if (playerView != null) {
//            playerView.getPlayerUiController().showCustomAction1(false);
//            playerView.getPlayerUiController().showCustomAction2(false);
//            playerView.getPlayerUiController().showMenuButton(false);
//        }
//
//    }
//
//    @Override
//    protected void init() {
//        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                super.onReady(youTubePlayer);
//                if (!videoUrl.isEmpty()) {
//                    hidePlayerControls();
//                    youTubePlayer.loadVideo(CommonMethods.getVideoId(videoUrl), 0);
//                }
//            }
//        });
//    }
//
//    @OnClick(R.id.ivBack)
//    public void onClick() {
//        if (null != getFragmentManager()) {
//            getFragmentManager().popBackStackImmediate();
//        }
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (null != playerView) {
//            playerView.release();
//        }
//    }
//
//
//
//}

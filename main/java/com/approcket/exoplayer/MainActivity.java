package com.approcket.exoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MainActivity extends AppCompatActivity {

    public PlayerView playerView;
    public ProgressBar progressBar;
    public ImageView full_Screen;
    public SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView=findViewById(R.id.player_view);
        progressBar=findViewById(R.id.progress_bar);
        full_Screen=playerView.findViewById(R.id.bt_full);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
        ,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Uri videourl=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        LoadControl loadControl=new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();

        TrackSelector trackSelector=new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(
                MainActivity.this,trackSelector,loadControl
        );

        DefaultHttpDataSourceFactory factory=new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );

        ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();

        MediaSource mediaSource=new ExtractorMediaSource(videourl
        ,factory,extractorsFactory,null,null);

        playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView.setKeepScreenOn(true);

        simpleExoPlayer.prepare(mediaSource);

        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                if(playbackState==Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState==Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        full_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    full_Screen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_24));

                    //set portrait orientation

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    flag=false;
                }else{
                    full_Screen.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_baseline_fullscreen_exit_24));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    flag=true;
                }
            }
        });
    }
     option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Playback Speed");
                String[] items = {"0.5x","1x","1.5x","2x","2.5x","3x"};
                boolean[] checkedItems = {false, false, false, false, false,false};
                alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        switch (which) {
                            case 0:
                                if(isChecked)
                                    playbackSpeed(0.5f);

                                break;
                            case 1:
                                if(isChecked)
                                    playbackSpeed(1f);
                                    break;
                            case 2:
                                if(isChecked)
                                    playbackSpeed(1.5f);
                                    break;
                            case 3:
                                if(isChecked)
                                    playbackSpeed(2f);
                                    break;
                            case 4:
                                if(isChecked)
                                    playbackSpeed(2.5f);
                                    break;
                            case 5:
                                if(isChecked)
                                    playbackSpeed(3f);
                                    break;
                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();


            }
        });
    }

    public void playbackSpeed(float speed)
    {
        PlaybackParams params= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params = new PlaybackParams();
            params.setSpeed(speed);
        }
        simpleExoPlayer.setPlaybackParams(params);
    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }
}

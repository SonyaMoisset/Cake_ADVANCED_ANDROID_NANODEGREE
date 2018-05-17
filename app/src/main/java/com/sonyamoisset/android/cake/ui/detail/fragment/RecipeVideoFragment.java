package com.sonyamoisset.android.cake.ui.detail.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.databinding.FragmentRecipeVideoBinding;
import com.sonyamoisset.android.cake.db.entity.Step;
import com.sonyamoisset.android.cake.ui.detail.RecipeDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RecipeVideoFragment extends Fragment {

    private static final String CURRENT_STEP_VIDEO = "current_step_video";
    private static final String PLAY_STEP_VIDEO = "play_step_video";
    private static final String STEP_NUMBER = "step_number";

    private long previousStepPosition;
    private boolean playStepVideo;
    private int stepNumber;

    private FragmentRecipeVideoBinding fragmentRecipeVideoBinding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private SimpleExoPlayer simpleExoPlayer;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentRecipeVideoBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_video, container, false);
        fragmentRecipeVideoBinding.setHandler(this);

        return fragmentRecipeVideoBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_STEP_VIDEO, previousStepPosition);
        outState.putBoolean(PLAY_STEP_VIDEO, playStepVideo);
        outState.putInt(STEP_NUMBER, stepNumber);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stepNumber = -1;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_STEP_VIDEO)) {
                previousStepPosition = savedInstanceState.getLong(CURRENT_STEP_VIDEO);
            }

            if (savedInstanceState.containsKey(PLAY_STEP_VIDEO)) {
                playStepVideo = savedInstanceState.getBoolean(PLAY_STEP_VIDEO);
            }

            if (savedInstanceState.containsKey(STEP_NUMBER)) {
                stepNumber = savedInstanceState.getInt(STEP_NUMBER);
            }
        }

        populateFullScreenModeForMobile();

        recipeDetailViewModel =
                ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory)
                        .get(RecipeDetailViewModel.class);

        recipeDetailViewModel.getSteps().observe(this, this::populateUI);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickNext() {
        resetVideo();
        recipeDetailViewModel.nextStepId();
    }

    public void onClickPrevious() {
        resetVideo();
        recipeDetailViewModel.previousStepId();
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            fragmentRecipeVideoBinding.fragmentRecipeVideoExoplayerView.setPlayer(simpleExoPlayer);
        }

        populateSimpleExoPlayer(mediaUri);
    }

    private void populateSimpleExoPlayer(Uri mediaUri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                        Util.getUserAgent(getContext(), getString(R.string.app_name)));
        MediaSource mediaSource =
                new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.seekTo(previousStepPosition);
        simpleExoPlayer.setPlayWhenReady(playStepVideo);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            previousStepPosition = simpleExoPlayer.getCurrentPosition();
            playStepVideo = simpleExoPlayer.getPlayWhenReady();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void resetVideo() {
        playStepVideo = true;
        previousStepPosition = 0;

        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
    }

    private void showVideoOrImageOrThumbnail(Step step) {
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            fragmentRecipeVideoBinding
                    .fragmentRecipeVideoExoplayerPlaceholderImage.setVisibility(View.GONE);
            fragmentRecipeVideoBinding
                    .fragmentRecipeVideoExoplayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrl));
        } else {
            fragmentRecipeVideoBinding
                    .fragmentRecipeVideoExoplayerPlaceholderImage.setVisibility(View.VISIBLE);
            fragmentRecipeVideoBinding
                    .fragmentRecipeVideoExoplayerView.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(step.getThumbnailURL())) {
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .placeholder(R.drawable.cake)
                        .into(fragmentRecipeVideoBinding
                                .fragmentRecipeVideoExoplayerPlaceholderImage);
            } else {
                fragmentRecipeVideoBinding
                        .fragmentRecipeVideoExoplayerPlaceholderImage
                        .setImageResource(R.drawable.cake);
            }
        }
    }

    private void populateUI(Step step) {

        if (stepNumber == -1 || stepNumber != step.getId()) {
            resetVideo();
            stepNumber = step.getId();
        }

        fragmentRecipeVideoBinding.setStepSize(recipeDetailViewModel.getStepSize());
        fragmentRecipeVideoBinding.setStep(step);

        showVideoOrImageOrThumbnail(step);
    }

    private void populateFullScreenModeForMobile() {
        if (isFullScreenModeForMobile()) {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).hide();

            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private boolean isFullScreenModeForMobile() {
        return Objects.requireNonNull(getContext()).getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE
                && getContext().getResources().getConfiguration().smallestScreenWidthDp < 600;
    }
}

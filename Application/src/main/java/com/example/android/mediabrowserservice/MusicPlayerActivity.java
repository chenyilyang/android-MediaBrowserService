/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.mediabrowserservice;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Main activity for the music player.
 */
public class MusicPlayerActivity extends AppCompatActivity
        implements BrowseFragment.FragmentDataHelper {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, BrowseFragment.newInstance(null))
                    .commit();
        }
    }

    @Override
    public void onMediaItemSelected(MediaBrowserCompat.MediaItem item) {
        if (item.isPlayable()) {
            getMediaController().getTransportControls().playFromMediaId(item.getMediaId(), null);
            QueueFragment queueFragment = QueueFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, queueFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.isBrowsable()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BrowseFragment.newInstance(item.getMediaId()))
                    .addToBackStack(null)
                    .commit();
        }
    }
}

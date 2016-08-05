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

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mediabrowserservice.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fragment that lists all the various browsable queues available
 * from a {@link android.service.media.MediaBrowserCompatService}.
 * <p/>
 * It uses a {@link MediaBrowserCompat} to connect to the {@link MusicService}. Once connected,
 * the fragment subscribes to get all the children. All {@link MediaBrowserCompat.MediaItem}'s
 * that can be browsed are shown in a ListView.
 */
public class BrowseFragment extends Fragment {

    private static final String TAG = LogHelper.makeLogTag(BrowseFragment.class.getSimpleName());

    public static final String ARG_MEDIA_ID = "media_id";

    public static interface FragmentDataHelper {
        void onMediaItemSelected(MediaBrowserCompat.MediaItem item);
    }

    // The mediaId to be used for subscribing for children using the MediaBrowserCompat.
    private String mMediaId;

    private MediaBrowserCompat mMediaBrowserCompat;
    private BrowseAdapter mBrowserAdapter;

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {

        @Override
        public void onChildrenLoaded(String parentId, List<MediaBrowserCompat.MediaItem> children) {
            mBrowserAdapter.clear();
            mBrowserAdapter.notifyDataSetInvalidated();
            for (MediaBrowserCompat.MediaItem item : children) {
                mBrowserAdapter.add(item);
            }
            mBrowserAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(String id) {
            Toast.makeText(getActivity(), R.string.error_loading_media,
                    Toast.LENGTH_LONG).show();
        }
    };

    private MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            LogHelper.d(TAG, "onConnected: session token " + mMediaBrowserCompat.getSessionToken());

            if (mMediaId == null) {
                mMediaId = mMediaBrowserCompat.getRoot();
            }
            mMediaBrowserCompat.subscribe(mMediaId, mSubscriptionCallback);
            if (mMediaBrowserCompat.getSessionToken() == null) {
                throw new IllegalArgumentException("No Session token");
            }
            try {
                MediaControllerCompat mediaController = new MediaControllerCompat(getActivity(),
                        mMediaBrowserCompat.getSessionToken());
                getActivity().setSupportMediaController(mediaController);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectionFailed() {
            LogHelper.d(TAG, "onConnectionFailed");
        }

        @Override
        public void onConnectionSuspended() {
            LogHelper.d(TAG, "onConnectionSuspended");
            getActivity().setMediaController(null);
        }
    };

    public static BrowseFragment newInstance(String mediaId) {
        Bundle args = new Bundle();
        args.putString(ARG_MEDIA_ID, mediaId);
        BrowseFragment fragment = new BrowseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mBrowserAdapter = new BrowseAdapter(getActivity());

        View controls = rootView.findViewById(R.id.controls);
        controls.setVisibility(View.GONE);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(mBrowserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaBrowserCompat.MediaItem item = mBrowserAdapter.getItem(position);
                try {
                    FragmentDataHelper listener = (FragmentDataHelper) getActivity();
                    listener.onMediaItemSelected(item);
                } catch (ClassCastException ex) {
                    Log.e(TAG, "Exception trying to cast to FragmentDataHelper", ex);
                }
            }
        });

        Bundle args = getArguments();
        mMediaId = args.getString(ARG_MEDIA_ID, null);

        mMediaBrowserCompat = new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), MusicService.class),
                mConnectionCallback, null);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserCompat.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaBrowserCompat.disconnect();
    }

    // An adapter for showing the list of browsed MediaItem's
    private static class BrowseAdapter extends ArrayAdapter<MediaBrowserCompat.MediaItem> {

        public BrowseAdapter(Context context) {
            super(context, R.layout.media_list_item, new ArrayList<MediaBrowserCompat.MediaItem>());
        }

        static class ViewHolder {
            ImageView mImageView;
            TextView mTitleView;
            TextView mDescriptionView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.media_list_item, parent, false);
                holder = new ViewHolder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.play_eq);
                holder.mImageView.setVisibility(View.GONE);
                holder.mTitleView = (TextView) convertView.findViewById(R.id.title);
                holder.mDescriptionView = (TextView) convertView.findViewById(R.id.description);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MediaBrowserCompat.MediaItem item = getItem(position);
            holder.mTitleView.setText(item.getDescription().getTitle());
            holder.mDescriptionView.setText(item.getDescription().getDescription());
            if (item.isPlayable()) {
                holder.mImageView.setImageDrawable(
                        getContext().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                holder.mImageView.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }
}

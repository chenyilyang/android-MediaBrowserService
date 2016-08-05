package com.example.android.mediabrowserservice.local;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.media.MediaMetadataCompat;
import android.provider.MediaStore;

import com.example.android.mediabrowserservice.MyApplication;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.mediabrowserservice.model.MusicProvider.CUSTOM_METADATA_TRACK_SOURCE;

/**
 * Created by chenyilyang on 2016/08/04.
 */

public class Utils {

    public static List<LocalAudioBean> queryLocalMusics(Context context) {

        List<LocalAudioBean> musicList = new ArrayList<LocalAudioBean>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio
                .Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {

                /**
                 * String ALBUM = "album";
                 String ALBUM_ID = "album_id";
                 String ALBUM_KEY = "album_key";
                 String ARTIST = "artist";
                 String ARTIST_ID = "artist_id";
                 String ARTIST_KEY = "artist_key";
                 String BOOKMARK = "bookmark";
                 String COMPOSER = "composer";
                 String DURATION = "duration";
                 String IS_ALARM = "is_alarm";
                 String IS_MUSIC = "is_music";
                 String IS_NOTIFICATION = "is_notification";
                 String IS_PODCAST = "is_podcast";
                 String IS_RINGTONE = "is_ringtone";
                 String TITLE_KEY = "title_key";
                 String TRACK = "track";
                 String YEAR = "year";
                 */

                String album = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ALBUM));
                String album_id = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ALBUM_ID));
                String album_key = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ALBUM_KEY));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ARTIST));
                String artist_id = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ARTIST_ID));
                String artist_key = cursor.getString(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.ARTIST_KEY));
                Integer track = cursor.getInt(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.TRACK));
                Integer year = cursor.getInt(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.YEAR));

                Integer isMusic = cursor.getInt(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.IS_MUSIC));
                Integer isRingTone = cursor.getInt(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.IS_RINGTONE));
                String size = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                Integer duration = cursor.getInt(cursor.getColumnIndexOrThrow
                        (MediaStore.Audio.Media.DURATION));

                /*if (isMusic != 1 || isRingTone == 1){
                    continue;//非音乐
                }*/


                /**
                 * 像上面一样自己写
                 */

                String path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media
                                        .DATA));
                String dateAdded = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
                String dateModified = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
                String displayName = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                String mimeType = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));

                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                /**
                 * 自己填充对象   LocalAudioBean
                 */
                LocalAudioBean localAudioBean = new LocalAudioBean();
                localAudioBean.setAlbum(album);
                localAudioBean.setAlbum_id(album_id);
                localAudioBean.setAlbum_key(album_key);
                localAudioBean.setArtist(artist);
                localAudioBean.setArtist_id(artist_id);
                localAudioBean.setArtist_key(artist_key);
                localAudioBean.setTrack(track);
                localAudioBean.setYear(year);
                localAudioBean.setIsMusic(isMusic);
                localAudioBean.setDuration(duration);
                localAudioBean.setTitle(title);
                localAudioBean.setSize(size);
                localAudioBean.setMimeType(mimeType);
                localAudioBean.setDisplayName(displayName);
                localAudioBean.setDateModified(dateModified);
                localAudioBean.setDateAdded(dateAdded);
                localAudioBean.setPath(path);
                musicList.add(localAudioBean);
            }
        }
        return musicList;
    }

    public static List<MediaMetadataCompat> buildAllMediaMetadataCompats(){
        List<LocalAudioBean> list = queryLocalMusics(MyApplication.instance);
        List<MediaMetadataCompat> mediaMetadatas = new ArrayList<MediaMetadataCompat>(list.size());
        for (LocalAudioBean bean : list){
            mediaMetadatas.add(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(bean.getPath().hashCode()))
                    .putString(CUSTOM_METADATA_TRACK_SOURCE, bean.getPath())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, bean.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, bean.getArtist())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.valueOf(bean.getDuration()))
                    .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "all_music")
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, bean.getAlbum())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, bean.getTitle())
                    .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, bean.getTrack())
                    .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, bean.getTrack())
                    .build());
        }
        return mediaMetadatas;
    }
}

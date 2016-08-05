package com.example.android.mediabrowserservice.local;

/**
 * Created by hpplay on 2016/7/27.
 */
public class LocalAudioBean extends LocalMediaBean{
    private int _id;
    private String album;
    private String album_id;
    private String album_key;
    private String artist;
    private String artist_id;
    private String artist_key;
    private Integer track;
    private Integer year;
    private Integer isMusic;
    private Integer duration;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_key() {
        return album_key;
    }

    public void setAlbum_key(String album_key) {
        this.album_key = album_key;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(Integer isMusic) {
        this.isMusic = isMusic;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "LocalAudioBean{" +
                "album='" + album + '\'' +
                ", album_id='" + album_id + '\'' +
                ", album_key='" + album_key + '\'' +
                ", artist='" + artist + '\'' +
                ", artist_id='" + artist_id + '\'' +
                ", artist_key='" + artist_key + '\'' +
                ", track=" + track +
                ", year=" + year +
                ", isMusic=" + isMusic +
                ", duration=" + duration +
                '}';
    }
}
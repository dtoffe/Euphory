/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * @author Daniel Toffetti
 * 
 */
public class AlbumData {
    
    private String artist;
    private String title;
    private int episode;
    private ArrayList<TrackData> tracks;
    private Image coverImage;
    private String fileName;

    public AlbumData() {
        this.tracks = new ArrayList<>();
    }

    public AlbumData(String artist, String title) {
        this(); // Call the no-arg constructor to initialize tracks
        this.artist = artist;
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public Image getCoverImage() {
        return coverImage;
    }    

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<TrackData> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<TrackData> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(String artist, String title, int trackNumber, int startTime) {
        TrackData track = new TrackData(artist, title, trackNumber, startTime);
        this.tracks.add(track);
    }

}

/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory.model;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 * @author Daniel Toffetti
 *
 * Represents the data for an album in the Euphory application.
 */
public class AlbumDataViewModel {

    /**
     * The album artist property.
     */
    private StringProperty albumArtist = new SimpleStringProperty();

    /**
     * The album title property.
     */
    private StringProperty albumTitle = new SimpleStringProperty();

    /**
     * The album episode property.
     */
    private StringProperty albumEpisode = new SimpleStringProperty();

    /**
     * The album date property.
     */
    private StringProperty albumDate = new SimpleStringProperty();

    /**
     * The album tracks.
     */
    private ObservableList<TrackDataViewModel> albumTracks = FXCollections.observableArrayList();

    /**
     * The cover image property.
     */
    private ObjectProperty<Image> coverImage = new SimpleObjectProperty<>();
    
    private Map<String, String> additionalTags;

    /**
     * Constructs a new AlbumDataViewModel.
     */
    public AlbumDataViewModel() {
        this.additionalTags = new HashMap<>();
    }

    /**
     * Gets the album artist property.
     *
     * @return The album artist property.
     */
    public StringProperty albumArtistProperty() {
        return albumArtist;
    }

    /**
     * Gets the album title property.
     *
     * @return The album title property.
     */
    public StringProperty albumTitleProperty() {
        return albumTitle;
    }

    /**
     * Gets the album episode property.
     *
     * @return The album episode property.
     */
    public StringProperty albumEpisodeProperty() {
        return albumEpisode;
    }

    /**
     * Gets the album date property.
     *
     * @return The album date property.
     */
    public StringProperty albumDateProperty() {
        return albumDate;
    }

    /**
     * Gets the list of album tracks.
     *
     * @return The list of album tracks.
     */
    public ObservableList<TrackDataViewModel> getAlbumTracks() {
        return albumTracks;
    }

    /**
     * Gets the cover image property.
     *
     * @return The cover image property.
     */
    public ObjectProperty<Image> coverImageProperty() {
        return coverImage;
    }

    public Map<String, String> getAdditionalTags() {
        return additionalTags;
    }

    public void setAdditionalTags(Map<String, String> additionalTags) {
        this.additionalTags = additionalTags;
    }

    /**
     * Adds a track to the album.
     *
     * @param artist      The artist of the track.
     * @param title       The title of the track.
     * @param trackNumber The track number.
     * @param startTime   The start time of the track in seconds.
     */
    public void addTrack(String artist, String title, int trackNumber, int startTime) {
        TrackDataViewModel track = new TrackDataViewModel(artist, title, trackNumber, startTime);
        this.albumTracks.add(track);
    }

    /**
     * Adds a track to the album.
     *
     * @param track The track to add.
     */
    public void addTrack(TrackDataViewModel track) {
        this.albumTracks.add(track);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Artist: ").append(albumArtist).append("\n");
        sb.append("Title: ").append(albumTitle).append("\n");
        sb.append("Episode: ").append(albumEpisode).append("\n");
        sb.append("Date: ").append(albumDate).append("\n");
        sb.append("Additional Tags:\n");
        for (Map.Entry<String, String> entry : additionalTags.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

}

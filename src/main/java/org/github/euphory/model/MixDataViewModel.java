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
public class MixDataViewModel {

    /**
     * The mix name property.
     */
    private StringProperty mixName = new SimpleStringProperty();

    /**
     * The mix author property.
     */
    private StringProperty mixAuthor = new SimpleStringProperty();

    /**
     * The mix episode property.
     */
    private StringProperty mixEpisode = new SimpleStringProperty();

    /**
     * The mix date property.
     */
    private StringProperty mixDate = new SimpleStringProperty();

    /**
     * The mix total duration property.
     */
    private StringProperty mixDuration = new SimpleStringProperty();

    /**
     * The mix tracks.
     */
    private ObservableList<TrackDataViewModel> mixTracks = FXCollections.observableArrayList();

    /**
     * The cover image property.
     */
    private ObjectProperty<Image> coverImage = new SimpleObjectProperty<>();

    /**
     * The additional tags.
     */
    private Map<String, String> additionalTags;

    /**
     * Constructs a new MixDataViewModel.
     */
    public MixDataViewModel() {
        this.additionalTags = new HashMap<>();
    }

    /**
     * Gets the mix name property.
     *
     * @return The mix name property.
     */
    public StringProperty mixNameProperty() {
        return mixName;
    }

    /**
     * Gets the mix author property.
     *
     * @return The mix author property.
     */
    public StringProperty mixAuthorProperty() {
        return mixAuthor;
    }

    /**
     * Gets the mix episode property.
     *
     * @return The mix episode property.
     */
    public StringProperty mixEpisodeProperty() {
        return mixEpisode;
    }

    /**
     * Gets the mix date property.
     *
     * @return The mix date property.
     */
    public StringProperty mixDateProperty() {
        return mixDate;
    }

    /**
     * Gets the mix duration property.
     *
     * @return The mix duration property.
     */
    public StringProperty mixDurationProperty() {
        return mixDuration;
    }

    /**
     * Gets the list of mix tracks.
     *
     * @return The list of mix tracks.
     */
    public ObservableList<TrackDataViewModel> getMixTracks() {
        return mixTracks;
    }

    /**
     * Gets the cover image property.
     *
     * @return The cover image property.
     */
    public ObjectProperty<Image> coverImageProperty() {
        return coverImage;
    }

    /**
     * Gets the cover image property.
     *
     * @return The cover image property.
     */
    public Map<String, String> getAdditionalTags() {
        return additionalTags;
    }

    public void setAdditionalTags(Map<String, String> additionalTags) {
        this.additionalTags = additionalTags;
    }

    /**
     * Adds a track to the mix.
     *
     * @param title       The title of the track.
     * @param artist      The artist of the track.
     * @param trackNumber The track number.
     * @param startTime   The start time of the track in seconds.
     */
    public void addTrack(String trackNumber, int startTime, String artist, String title) {
        TrackDataViewModel track = new TrackDataViewModel(trackNumber, startTime, artist, title);
        this.mixTracks.add(track);
    }

    /**
     * Adds a track to the mix.
     *
     * @param track The track to add.
     */
    public void addTrack(TrackDataViewModel track) {
        this.mixTracks.add(track);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(mixName).append("\n");
        sb.append("Author: ").append(mixAuthor).append("\n");
        sb.append("Episode: ").append(mixEpisode).append("\n");
        sb.append("Date: ").append(mixDate).append("\n");
        sb.append("Duration: ").append(mixDuration).append("\n");
        sb.append("Additional Tags:\n");
        for (Map.Entry<String, String> entry : additionalTags.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

}

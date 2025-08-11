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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Daniel Toffetti
 *
 * Represents the data for a track in the Euphory application.
 */
public class TrackDataViewModel {

    /**
     * The track artist property.
     */
    private StringProperty trackArtist = new SimpleStringProperty();

    /**
     * The track title property.
     */
    private StringProperty trackTitle = new SimpleStringProperty();

    /**
     * The track number property.
     */
    private IntegerProperty trackNumber = new SimpleIntegerProperty();
    
    /**
     * The start time property.
     */
    private IntegerProperty startTime = new SimpleIntegerProperty();

    private Map<String, String> additionalTags;

    public TrackDataViewModel() {
        this.additionalTags = new HashMap<>();
    }

    /**
     * Constructs a new TrackDataViewModel.
     *
     * @param artist      The artist of the track.
     * @param title       The title of the track.
     * @param trackNumber The track number.
     * @param startTime   The start time of the track in seconds.
     */
    public TrackDataViewModel(String artist, String title, int trackNumber, int startTime) {
        this();
        this.trackArtist.set(artist);
        this.trackTitle.set(title);
        this.trackNumber.set(trackNumber);
        this.startTime.set(startTime);
    }

    /**
     * Gets the track artist property.
     *
     * @return The track artist property.
     */
    public StringProperty trackArtistProperty() {
        return trackArtist;
    }

    /**
     * Gets the track title property.
     *
     * @return The track title property.
     */
    public StringProperty trackTitleProperty() {
        return trackTitle;
    }

    /**
     * Gets the track number property.
     *
     * @return The track number property.
     */
    public IntegerProperty trackNumberProperty() {
        return trackNumber;
    }

    /**
     * Gets the start time property.
     *
     * @return The start time property.
     */
    public IntegerProperty startTimeProperty() {
        return startTime;
    }

    public Map<String, String> getAdditionalTags() {
        return additionalTags;
    }

    public void setAdditionalTags(Map<String, String> additionalTags) {
        this.additionalTags = additionalTags;
    }

    /**
     * Displays the start time in HH:MM:SS format.
     *
     * @return The formatted start time.
     */
    public String displayStartTime() {
        int hours = startTime.get() / 3600;
        int minutes = (startTime.get() % 3600) / 60;
        int seconds = (startTime.get() % 3600) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void addTag(String key, String value) {
        this.additionalTags.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(trackTitle).append("\n");
        sb.append("Artist: ").append(trackArtist).append("\n");
        sb.append("Track: ").append(trackNumber).append("\n");
        sb.append("Time: ").append(startTime).append("\n");
        sb.append("Additional Tags:\n");
        for (Map.Entry<String, String> entry : additionalTags.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

}

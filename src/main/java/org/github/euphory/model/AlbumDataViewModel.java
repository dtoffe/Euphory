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

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
     * The album subtitle property.
     */
    private StringProperty albumSubtitle = new SimpleStringProperty();

    /**
     * The album episode property.
     */
    private StringProperty albumEpisode = new SimpleStringProperty();

    /**
     * The album date property.
     */
    private ObjectProperty<LocalDate> albumDate = new SimpleObjectProperty<>();

    /**
     * The album tracks.
     */
    private ObservableList<TrackDataViewModel> albumTracks = FXCollections.observableArrayList();

    /**
     * The cover image property.
     */
    private ObjectProperty<Image> coverImage = new SimpleObjectProperty<>();
    
    /**
     * Constructs a new AlbumDataViewModel.
     */
    public AlbumDataViewModel() {
        albumEpisode.addListener((observable, oldValue, newValue) -> validateEpisode(null));
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
     * Gets the album subtitle property.
     *
     * @return The album subtitle property.
     */
    public StringProperty albumSubtitleProperty() {
        return albumSubtitle;
    }

    /**
     * Gets the album episode property.
     *
     * @return The album episode property.
     */
    public StringProperty albumEpisodeProperty() {
        albumEpisode.set(formatEpisode());
        return albumEpisode;
    }

    /**
     * Gets the album date property.
     *
     * @return The album date property.
     */
    public ObjectProperty<LocalDate> albumDateProperty() {
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

    /**
     * Validates the album episode number.
     *
     * @param alert The alert to show if the episode number is invalid.
     */
    public void validateEpisode(Alert alert) {
        try {
            if (albumEpisode.get() == null || albumEpisode.get() == "") {
                return;
            }
            int value = Integer.parseInt(albumEpisode.get());
            if (value < 0 || value > 2000) {
                if (alert != null) {
                    alert.setAlertType(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid episode number: Integer out of range.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            if (alert != null) {
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid number or numeric format");
                alert.showAndWait();
            }
        }
    }

    /**
     * Formats the album episode number.
     *
     * @return The formatted episode number.
     */
    public String formatEpisode() {
        if (albumEpisode.get() == null) {
            return "";
        }
        int value = Integer.parseInt(albumEpisode.get());
        return String.format("%03d", value);
    }
}

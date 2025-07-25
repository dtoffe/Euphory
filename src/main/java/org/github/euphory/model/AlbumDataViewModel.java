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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

import org.github.euphory.Main;

/**
 * @author Daniel Toffetti
 * 
 */
public class AlbumDataViewModel {

    private StringProperty albumArtist = new SimpleStringProperty();
    private StringProperty albumTitle = new SimpleStringProperty();
    private StringProperty albumSubtitle = new SimpleStringProperty();
    private StringProperty albumEpisode = new SimpleStringProperty();
    private ObjectProperty<LocalDate> albumDate = new SimpleObjectProperty<LocalDate>();
    private ObservableList<TrackDataViewModel> albumTracks = FXCollections.observableArrayList();
    private ObjectProperty<Image> coverImage = new SimpleObjectProperty<Image>();

    public AlbumDataViewModel() {
        albumEpisode.addListener((observable, oldValue, newValue) -> validateEpisode());
    }

    public StringProperty albumArtistProperty() {
        return albumArtist;
    }

    public StringProperty albumTitleProperty() {
        return albumTitle;
    }

    public StringProperty albumSubtitleProperty() {
        return albumSubtitle;
    }

    public StringProperty albumEpisodeProperty() {
        albumEpisode.set(formatEpisode());
        return albumEpisode;
    }

    public ObjectProperty<LocalDate> albumDateProperty() {
        return albumDate;
    }

    public ObservableList<TrackDataViewModel> getAlbumTracks() {
        return albumTracks;
    }

    public ObjectProperty<Image> coverImageProperty() {
        return coverImage;
    }

    public void addTrack(String artist, String title, int trackNumber, int startTime) {
        TrackDataViewModel track = new TrackDataViewModel(artist, title, trackNumber, startTime);
        this.albumTracks.add(track);
    }

    public void addTrack(TrackDataViewModel track) {
        this.albumTracks.add(track);
    }

    public void validateEpisode() {
        try {
            if (albumEpisode.get() == null || albumEpisode.get() == "") {
                return;
            }
            int value = Integer.parseInt(albumEpisode.get());
            if (value < 0 || value > 2000) {
                Main.showAlert(AlertType.ERROR, "Error", "Invalid episode number: Integer out of range.", "");
            }
        } catch (NumberFormatException e) {
            Main.showAlert(AlertType.ERROR, "Error", "Invalid number or numeric format", "");
        }
    }

    public String formatEpisode() {
        if (albumEpisode.get() == null) {
            return "";
        }
        int value = Integer.parseInt(albumEpisode.get());
        return String.format("%03d", value);
    }
}

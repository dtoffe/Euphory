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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Daniel Toffetti
 * 
 */
public class TrackDataViewModel {
    
    private StringProperty trackArtist = new SimpleStringProperty();
    private StringProperty trackTitle = new SimpleStringProperty();
    private IntegerProperty trackNumber = new SimpleIntegerProperty();
    private IntegerProperty startTime = new SimpleIntegerProperty();

    public TrackDataViewModel(String artist, String title, int trackNumber, int startTime) {
        this.trackArtist.set(artist);
        this.trackTitle.set(title);
        this.trackNumber.set(trackNumber);
        this.startTime.set(startTime);
    }
    
    public StringProperty trackArtistProperty() {
        return trackArtist;
    }

    public StringProperty trackTitleProperty() {
        return trackTitle;
    }

    public IntegerProperty trackNumberProperty() {
        return trackNumber;
    }

    public IntegerProperty startTimeProperty() { 
        return startTime;
    }
    
    public String displayStartTime() {
        int hours = startTime.get() / 3600;
        int minutes = (startTime.get() % 3600) / 60;
        int seconds = (startTime.get() % 3600) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

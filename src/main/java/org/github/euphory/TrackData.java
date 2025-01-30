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

/**
 * @author Daniel Toffetti
 * 
 */
public class TrackData {
    
    private String artist;
    private String title;
    private int trackNumber;
    private int startTime;  // in seconds
    
    public TrackData(String artist, String title, int trackNumber, int startTime) {
        this.artist = artist;
        this.title = title;
        this.trackNumber = trackNumber;
        this.startTime = startTime;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getStartTime() { 
        return startTime;
    }
    
    public String displayStartTime() {
        int hours = startTime / 3600;
        int minutes = (startTime % 3600) / 60;
        int seconds = (startTime % 3600) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

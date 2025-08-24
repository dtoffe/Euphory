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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlatTagEditionModel {

    // Stores all metadata in a flat key-value structure
    private final Map<String, String> metadata = new LinkedHashMap<>();

    // Define which fields repeat per track
    private static final List<String> TRACK_FIELDS = Arrays.asList(
        "tracknumber", "trackstarttime", "trackartist", "trackname"
    );

    public FlatTagEditionModel() {
        // Example of initializing global (non-repeating) fields
        metadata.put("title", "");
        metadata.put("artist", "");
        metadata.put("album", "");
        metadata.put("genre", "");
    }

    public void put(String key, String value) {
        metadata.put(key, value);
    }

    public String get(String key) {
        return metadata.get(key);
    }

    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(metadata);
    }

    /**
     * Adds a new empty track (auto-numbered).
     * Returns the track index that was added.
     */
    public int addTrack() {
        int trackCount = getTrackCount() + 1;
        for (String field : TRACK_FIELDS) {
            metadata.put(field + trackCount, "");
        }
        return trackCount;
    }

    /**
     * Gets the total number of tracks based on the "trackname" entries.
     */
    public int getTrackCount() {
        int count = 0;
        for (String key : metadata.keySet()) {
            if (key.startsWith("trackname")) {
                count++;
            }
        }
        return count;
    }

}

/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory.service;

import java.util.Map;
import java.io.RandomAccessFile;
import org.github.euphory.tags.MP4TagManager;
import org.github.euphory.model.MixDataViewModel;
import org.github.euphory.model.TrackDataViewModel;

public class TagService {

    public TagService() {
        
    }

    public MixDataViewModel populateAlbumDataViewModel(String filePath) {
        MixDataViewModel albumDataViewModel = new MixDataViewModel();
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            Map<String, String> tags = MP4TagManager.readMP4Tag(file);

            albumDataViewModel.mixNameProperty().set(tags.getOrDefault("title", ""));
            albumDataViewModel.mixAuthorProperty().set(tags.getOrDefault("artist", ""));
            albumDataViewModel.mixEpisodeProperty().set(tags.getOrDefault("episode", ""));
            albumDataViewModel.mixDateProperty().set(tags.getOrDefault("date", ""));

            // Add additional tags not represented by fields
            tags.remove("title");
            tags.remove("artist");
            tags.remove("album");
            tags.remove("year");
            tags.remove("date");

            albumDataViewModel.setAdditionalTags(tags);

            return albumDataViewModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TrackDataViewModel populateTrackDataViewModel(String filePath) {
        TrackDataViewModel trackDataViewModel = new TrackDataViewModel();
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            Map<String, String> tags = MP4TagManager.readMP4Tag(file);

            trackDataViewModel.trackTitleProperty().set(tags.getOrDefault("title", ""));
            trackDataViewModel.trackArtistProperty().set(tags.getOrDefault("artist", ""));
            trackDataViewModel.trackNumberProperty().set(tags.getOrDefault("track", "0"));
            trackDataViewModel.startTimeProperty().set(Integer.parseInt(tags.getOrDefault("starttime", "0")));

            // Add additional tags not represented by fields
            tags.remove("title");
            tags.remove("artist");
            tags.remove("track");
            tags.remove("starttime");

            trackDataViewModel.setAdditionalTags(tags);

            return trackDataViewModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

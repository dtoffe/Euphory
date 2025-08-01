package org.github.euphory.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import org.github.euphory.model.AlbumDataViewModel;
import org.github.euphory.model.TrackDataViewModel;

public class TagService {

    private static final Logger logger = Logger.getLogger(TagService.class.getName());

    public void loadTagsFromFile(String filePath, AlbumDataViewModel albumData) {
        File file = new File(filePath);
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            if (tag != null) {
                logger.log(Level.INFO, "Tag system: " + audioFile.getTag().getClass().getName());
                albumData.albumArtistProperty().setValue(tag.getFirst(FieldKey.ARTIST));
                albumData.albumTitleProperty().setValue(tag.getFirst(FieldKey.ALBUM));
                albumData.albumSubtitleProperty().setValue(tag.getFirst(FieldKey.SUBTITLE));
                albumData.albumEpisodeProperty().setValue(tag.getFirst(FieldKey.TRACK));
                albumData.albumDateProperty().setValue(LocalDate.parse(tag.getFirst(FieldKey.YEAR)));

                // Load track-specific tags
                if (tag.hasField(FieldKey.TRACK)) {
                    int trackNumber = Integer.parseInt(tag.getFirst(FieldKey.TRACK));
                    if (trackNumber > 0 && trackNumber <= albumData.getAlbumTracks().size()) {
                        TrackDataViewModel track = albumData.getAlbumTracks().get(trackNumber - 1);
                        track.trackTitleProperty().setValue(tag.getFirst(FieldKey.TITLE));
                        track.trackArtistProperty().setValue(tag.getFirst(FieldKey.ARTIST));
                        track.trackNumberProperty().setValue(trackNumber);
                        // Assuming startTime is not available in the tag, otherwise parse and set it
                    }
                }
                // Add more fields as needed
            } else {
                logger.log(Level.WARNING, "No tags found in file: " + filePath);
            }

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            logger.log(Level.SEVERE, "Error loading tags from file: " + filePath, e);
        }
    }

    public void writeTagsToFile(String filePath, AlbumDataViewModel albumData) {
        File file = new File(filePath);
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            if (tag == null) {
                // If no tag exists, create a new one (e.g., ID3v2)
                tag = audioFile.createDefaultTag();
            }

            if (tag != null) {
                tag.setField(FieldKey.ARTIST, albumData.albumArtistProperty().getValue());
                tag.setField(FieldKey.ALBUM, albumData.albumTitleProperty().getValue());
                tag.setField(FieldKey.SUBTITLE, albumData.albumSubtitleProperty().getValue());
                tag.setField(FieldKey.TRACK, albumData.albumEpisodeProperty().getValue());
                tag.setField(FieldKey.YEAR, albumData.albumDateProperty().getValue().toString());

                // Write track-specific tags
                for (int i = 0; i < albumData.getAlbumTracks().size(); i++) {
                    TrackDataViewModel track = albumData.getAlbumTracks().get(i);
                    tag.setField(FieldKey.TITLE, track.trackTitleProperty().getValue());
                    tag.setField(FieldKey.ARTIST, track.trackArtistProperty().getValue());
                    tag.setField(FieldKey.TRACK, String.valueOf(i + 1));
                    // Assuming startTime is not available in the tag, otherwise set it
                }
                // Add more fields as needed

                audioFile.setTag(tag);
                AudioFileIO.write(audioFile);
            } else {
                logger.log(Level.WARNING, "Could not create or access tag for file: " + filePath);
            }

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
            logger.log(Level.SEVERE, "Error writing tags to file: " + filePath, e);
        }
    }

}

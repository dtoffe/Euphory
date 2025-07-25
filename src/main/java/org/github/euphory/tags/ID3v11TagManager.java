/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory.tags;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author Daniel Toffetti
 * 
 */
public class ID3v11TagManager {

    private static final int ID3v11_TAG_SIZE = 128;

    private static File audioFile;
    private static ID3v11Tag tag;

    private ID3v11TagManager() {

    }

    public static void setAudioFile(File file) throws IOException {
        audioFile = file;
    }

    public static ID3v11Tag getTag() throws IOException {
        if (audioFile == null) {
            return null;
        }
        tag = readID3v11Tag(audioFile);
        return tag;
    }

    public static void updateTag(String title, String artist, String album, String year, byte trackNumber) throws IOException {
        tag.setTitle(title);
        tag.setArtist(artist);
        tag.setAlbum(album);
        tag.setYear(year);
        tag.setComment("");
        tag.setTrackNumber((byte) 0);
        tag.setGenre((byte) 31);   // Trance, what else !!
        writeID3v11Tag(audioFile, tag);
    }

    // Read the ID3v1.1 tag
    public static ID3v11Tag readID3v11Tag(File mp3File) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(mp3File, "r")) {
            if (file.length() < ID3v11_TAG_SIZE) {
                // If the file is too short, return null to indicate no ID3 tag
                return null;
            }

            file.seek(file.length() - ID3v11_TAG_SIZE); // Go to the last 128 bytes
            byte[] tagBytes = new byte[ID3v11_TAG_SIZE];
            file.read(tagBytes);

            // Check if the bytes match the ID3v1.1 tag header
            if (!new String(Arrays.copyOfRange(tagBytes, 0, 3)).equals("TAG")) {
                // If not, return null to indicate no ID3 tag
                return null;
            }
            
            return ID3v11Tag.fromBytes(tagBytes);
        }
    }

    // Write the ID3v1.1 tag
    public static void writeID3v11Tag(File mp3File, ID3v11Tag tag) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(mp3File, "rw")) {
            // Check if the file already has an ID3 tag
            if (file.length() >= ID3v11_TAG_SIZE) {
                file.seek(file.length() - ID3v11_TAG_SIZE); // Go to the last 128 bytes
                byte[] existingTagBytes = new byte[ID3v11_TAG_SIZE];
                file.read(existingTagBytes);

                // Check if the bytes match the ID3v1.1 tag header
                if (new String(Arrays.copyOfRange(existingTagBytes, 0, 3)).equals("TAG")) {
                    // If so, overwrite the existing tag
                    file.seek(file.length() - ID3v11_TAG_SIZE); // Go to the last 128 bytes
                    file.write(tag.toBytes());
                } else {
                    // If not, append the new tag to the end of the file
                    file.seek(file.length()); // Go to the end of the file
                    file.write(tag.toBytes());
                }
            } else {
                // If the file is too short, append the new tag to the end of the file
                file.seek(file.length()); // Go to the end of the file
                file.write(tag.toBytes());
            }
        }
    }
    
}

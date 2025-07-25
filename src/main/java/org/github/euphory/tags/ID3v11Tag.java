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

/**
 * @author Daniel Toffetti
 * 
 */
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ID3v11Tag {

    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private byte trackNumber;
    private byte genre;

    public ID3v11Tag(String title, String artist, String album, String year, String comment, byte trackNumber, byte genre) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.comment = comment;
        this.trackNumber = trackNumber;
        this.genre = genre;
    }

    // Convert tag to byte array for writing to the MP3 file
    public byte[] toBytes() {
        byte[] bytes = new byte[128];
        System.arraycopy("TAG".getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 0, 3);

        System.arraycopy(fixLength(title, 30).getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 3, 30);
        System.arraycopy(fixLength(artist, 30).getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 33, 30);
        System.arraycopy(fixLength(album, 30).getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 63, 30);
        System.arraycopy(fixLength(year, 4).getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 93, 4);
        System.arraycopy(fixLength(comment, 28).getBytes(StandardCharsets.ISO_8859_1), 0, bytes, 97, 28);

        bytes[125] = 0; // Null byte for comment padding
        bytes[126] = trackNumber; // Track number
        bytes[127] = genre; // Genre

        return bytes;
    }

    // Read the ID3v1.1 tag from a byte array
    public static ID3v11Tag fromBytes(byte[] bytes) {
        if (bytes.length != 128 || !new String(Arrays.copyOfRange(bytes, 0, 3)).equals("TAG")) {
            throw new IllegalArgumentException("Invalid ID3v1.1 tag format");
        }

        String title = trimString(new String(Arrays.copyOfRange(bytes, 3, 33), StandardCharsets.ISO_8859_1));
        String artist = trimString(new String(Arrays.copyOfRange(bytes, 33, 63), StandardCharsets.ISO_8859_1));
        String album = trimString(new String(Arrays.copyOfRange(bytes, 63, 93), StandardCharsets.ISO_8859_1));
        String year = trimString(new String(Arrays.copyOfRange(bytes, 93, 97), StandardCharsets.ISO_8859_1));
        String comment = trimString(new String(Arrays.copyOfRange(bytes, 97, 125), StandardCharsets.ISO_8859_1));
        byte trackNumber = bytes[126];
        byte genre = bytes[127];

        return new ID3v11Tag(title, artist, album, year, comment, trackNumber, genre);
    }

    // Utility methods
    private static String trimString(String str) {
        return str.replaceAll("\u0000", "").trim();
    }

    private static String fixLength(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length);
        }
        return String.format("%-" + length + "s", str);
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public byte getTrackNumber() { return trackNumber; }
    public void setTrackNumber(byte trackNumber) { this.trackNumber = trackNumber; }

    public byte getGenre() { return genre; }
    public void setGenre(byte genre) { this.genre = genre; }

}

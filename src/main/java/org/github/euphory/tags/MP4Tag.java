package org.github.euphory.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MP4Tag {

    // private static final int TAG_HEADER_SIZE = 8;
    // private static final int TAG_FOOTER_SIZE = 4;

    private byte[] tagData;
    private int tagSize;

    // MP4 tag fields
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private int trackNumber;
    private int totalTracks;

    public MP4Tag(byte[] tagData) {
        this.tagData = tagData;
        this.tagSize = tagData.length;
        parseTagData();
    }

    public byte[] getTagData() {
        return tagData;
    }
    
    private void parseTagData() {
        // Parse the MP4 tag data
        int offset = 0;
        while (offset < tagSize) {
            int atomSize = getInt(tagData, offset);
            int atomType = getInt(tagData, offset + 4);

            if (atomType == 0x7469646c) { // 'titl'
                title = getString(tagData, offset + 8, atomSize - 8);
            } else if (atomType == 0x61727473) { // 'arts'
                artist = getString(tagData, offset + 8, atomSize - 8);
            } else if (atomType == 0x616c6274) { // 'albt'
                album = getString(tagData, offset + 8, atomSize - 8);
            } else if (atomType == 0x67656e72) { // 'genr'
                genre = getString(tagData, offset + 8, atomSize - 8);
            } else if (atomType == 0x79656172) { // 'yeaR'
                year = getInt(tagData, offset + 8);
            } else if (atomType == 0x7472616b) { // 'trak'
                trackNumber = getInt(tagData, offset + 8);
                totalTracks = getInt(tagData, offset + 12);
            }

            offset += atomSize;
        }
    }

    public byte[] toBytes() {
        // Create the MP4 tag data
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        // Write the tag header
        try {
            dos.writeInt(tagSize);
            dos.writeInt(0x66747970); // 'ftyp'

            // Write the title atom
            dos.writeInt(title.length() + 8);
            dos.writeInt(0x7469646c); // 'titl'
            dos.writeBytes(title);

            // Write the artist atom
            dos.writeInt(artist.length() + 8);
            dos.writeInt(0x61727473); // 'arts'
            dos.writeBytes(artist);

            // Write the album atom
            dos.writeInt(album.length() + 8);
            dos.writeInt(0x616c6274); // 'albt'
            dos.writeBytes(album);

            // Write the genre atom
            dos.writeInt(genre.length() + 8);
            dos.writeInt(0x67656e72); // 'genr'
            dos.writeBytes(genre);

            // Write the year atom
            dos.writeInt(12);
            dos.writeInt(0x79656172); // 'yeaR'
            dos.writeInt(year);

            // Write the track atom
            dos.writeInt(16);
            dos.writeInt(0x7472616b); // 'trak'
            dos.writeInt(trackNumber);
            dos.writeInt(totalTracks);

            // Write the tag footer
            dos.writeInt(0x6d646174); // 'mdat'
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    private int getInt(byte[] data, int offset) {
        return (data[offset] & 0xff) << 24 | (data[offset + 1] & 0xff) << 16 | (data[offset + 2] & 0xff) << 8 | (data[offset + 3] & 0xff);
    }

    private String getString(byte[] data, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, offset, bytes, 0, length);
        return new String(bytes);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getTotalTracks() {
        return totalTracks;
    }
}

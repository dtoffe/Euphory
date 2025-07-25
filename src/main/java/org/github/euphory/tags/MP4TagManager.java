package org.github.euphory.tags;

import java.io.IOException;
import java.io.RandomAccessFile;

public class MP4TagManager {
    
    private static final int MP4_TAG_HEADER_SIZE = 8;
    private static final int MP4_TAG_FOOTER_SIZE = 4;

    public static MP4Tag readMP4Tag(RandomAccessFile file) throws IOException {
        // Seek to the beginning of the file
        file.seek(0);

        // Read the MP4 tag header
        byte[] header = new byte[MP4_TAG_HEADER_SIZE];
        file.read(header);

        // Check if the file has an MP4 tag
        if (header[0] != 'f' || header[1] != 't' || header[2] != 'y' || header[3] != 'p') {
            return null;
        }

        // Read the MP4 tag data
        int tagSize = getInt(header, 4);
        byte[] tagData = new byte[tagSize];
        file.read(tagData);

        // Create an MP4Tag object from the tag data
        MP4Tag tag = new MP4Tag(tagData);

        return tag;
    }

    public static void writeMP4Tag(RandomAccessFile file, MP4Tag tag) throws IOException {
        // Seek to the beginning of the file
        file.seek(0);

        // Write the MP4 tag header
        byte[] header = new byte[MP4_TAG_HEADER_SIZE];
        header[0] = 'f';
        header[1] = 't';
        header[2] = 'y';
        header[3] = 'p';
        header[4] = (byte) (tag.getTagData().length >> 24);
        header[5] = (byte) (tag.getTagData().length >> 16);
        header[6] = (byte) (tag.getTagData().length >> 8);
        header[7] = (byte) (tag.getTagData().length);
        file.write(header);

        // Write the MP4 tag data
        file.write(tag.getTagData());

        // Write the MP4 tag footer
        byte[] footer = new byte[MP4_TAG_FOOTER_SIZE];
        footer[0] = 'm';
        footer[1] = 'd';
        footer[2] = 'a';
        footer[3] = 't';
        file.write(footer);
    }

    private static int getInt(byte[] data, int offset) {
        return (data[offset] & 0xff) << 24 | (data[offset + 1] & 0xff) << 16 | (data[offset + 2] & 0xff) << 8 | (data[offset + 3] & 0xff);
    }
    
}

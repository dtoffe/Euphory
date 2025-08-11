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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

public final class MP4TagManager {
    
    private static final int MP4_TAG_HEADER_SIZE = 8;

    public static Map<String, String> readMP4Tag(RandomAccessFile file) throws IOException {
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

        // Create an MP4Tag object
       Map<String, String> tags = MP4Tag.parseTagData(tagData);

        // Parse the tag data and set tags
        int offset = 0;
        while (offset < tagSize) {
            int atomSize = getInt(tagData, offset);
            int atomType = getInt(tagData, offset + 4);
            String atomName = MP4Tag.getAtomName(atomType);

            if (atomName != null) {
                String value = MP4Tag.getString(tagData, offset + 8, atomSize - 8);
                MP4Tag.setTag(tags, atomName, value);
            }

            offset += atomSize;
        }

        return tags;
    }

    public static void writeMP4Tag(RandomAccessFile file, Map<String, String> tags) throws IOException {
        // Seek to the beginning of the file
        file.seek(0);

        // Create a ByteArrayOutputStream to write the tag data
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        // Write the MP4 tag header
        dos.writeInt(MP4Tag.toBytes(tags).length);
        dos.writeInt(0x66747970); // 'ftyp'

        // Write the tag data
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int atomType = MP4Tag.getAtomType(key);
            if (atomType != 0) {
                dos.writeInt(value.length() + 8);
                dos.writeInt(atomType);
                dos.writeBytes(value);
            }
        }

        // Write the MP4 tag footer
        dos.writeInt(0x6d646174); // 'mdat'

        // Write the tag data to the file
        file.write(bos.toByteArray());
    }

private static int getInt(byte[] data, int offset) {
        return (data[offset] & 0xff) << 24 | (data[offset + 1] & 0xff) << 16 | (data[offset + 2] & 0xff) << 8 | (data[offset + 3] & 0xff);
    }
    
}

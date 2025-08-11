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
import java.util.HashMap;
import java.util.Map;

public final class MP4Tag {

    public static Map<String, String> parseTagData(byte[] tagData) {
        Map<String, String> tags = new HashMap<>();
        int tagSize = tagData.length;
        int offset = 0;
        while (offset < tagSize) {
            int atomSize = getInt(tagData, offset);
            int atomType = getInt(tagData, offset + 4);
            String atomName = getAtomName(atomType);

            if (atomName != null) {
                String value = getString(tagData, offset + 8, atomSize - 8);
                tags.put(atomName, value);
            }

            offset += atomSize;
        }
        return tags;
    }

    public static String getAtomName(int atomType) {
        switch (atomType) {
            case 0x7469646c: return "title";
            case 0x61727473: return "artist";
            case 0x616c6274: return "album";
            case 0x67656e72: return "genre";
            case 0x79656172: return "year";
            case 0x7472616b: return "track";
            default: return null;
        }
    }

    public static byte[] toBytes(Map<String, String> tags) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeInt(tags.size() * 16); // Assuming each tag is 16 bytes for simplicity
            dos.writeInt(0x66747970); // 'ftyp'

            for (Map.Entry<String, String> entry : tags.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                int atomType = getAtomType(key);
                if (atomType != 0) {
                    dos.writeInt(value.length() + 8);
                    dos.writeInt(atomType);
                    dos.writeBytes(value);
                }
            }
            dos.writeInt(0x6d646174); // 'mdat'
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public static int getAtomType(String key) {
        switch (key) {
            case "title": return 0x7469646c;
            case "artist": return 0x61727473;
            case "album": return 0x616c6274;
            case "genre": return 0x67656e72;
            case "year": return 0x79656172;
            case "track": return 0x7472616b;
            default: return 0;
        }
    }

    private static int getInt(byte[] data, int offset) {
        return (data[offset] & 0xff) << 24 | (data[offset + 1] & 0xff) << 16 | (data[offset + 2] & 0xff) << 8 | (data[offset + 3] & 0xff);
    }

    public static String getString(byte[] data, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, offset, bytes, 0, length);
        return new String(bytes);
    }

    public static String getTag(Map<String, String> tags, String key) {
        return tags.get(key);
    }

    public static void setTag(Map<String, String> tags, String key, String value) {
        tags.put(key, value);
    }

    public static String toString(Map<String, String> tags) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
    
}

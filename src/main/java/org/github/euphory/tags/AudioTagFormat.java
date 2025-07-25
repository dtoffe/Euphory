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
 * An enumeration representing the different types of audio tag formats
 * that can be detected.
 */
public enum AudioTagFormat {

    /**
     * ID3v1 tag format, typically found at the end of MP3 files.
     */
    ID3V1,

    /**
     * ID3v2 tag format, typically found at the beginning of MP3 files.
     */
    ID3V2,

    /**
     * Vorbis Comment format, used by audio codecs like Ogg Vorbis, FLAC, and Opus.
     * This often signifies the presence of a metadata block that follows the Vorbis Comment specification.
     */
    VORBIS_COMMENT,

    /**
     * APE (Audio Tagging Format) tag, commonly found in Monkey's Audio (APE) files
     * and sometimes in MP3s.
     */
    APE_TAG,

    /**
     * MP4 tag format, used in MPEG-4 audio files (e.g., M4A, M4B).
     * This is a simplified detection based on the 'ftyp' atom.
     */
    MP4_TAG,

    /**
     * Basic metadata information within WAV (Waveform Audio File Format) files.
     * This includes the standard RIFF/WAVE header.
     */
    WAV_INFO,

    /**
     * Basic metadata information within AIFF (Audio Interchange File Format) files.
     * This includes the standard FORM/AIFF header.
     */
    AIFF_INFO,

    /**
     * Indicates that no recognized audio tag format was detected.
     */
    UNKNOWN;

    /**
     * Checks if the given format is supported.
     *
     * @param format the format to check
     * @return true if the format is supported, false otherwise
     */
    public static boolean isSupported(AudioTagFormat format) {
        if (format == ID3V1) {
            return true;
        }
        return false;
    }

}

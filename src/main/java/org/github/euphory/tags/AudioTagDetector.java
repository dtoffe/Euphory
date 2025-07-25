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
import java.nio.charset.StandardCharsets;

public class AudioTagDetector {

    /**
     * Detects the primary metadata tag format of a given audio file.
     * This method reads specific byte sequences from the beginning and end of the file
     * to identify common tag signatures. It prioritizes header-based tags (like ID3v2)
     * over footer-based tags (like ID3v1) if both are present, as header tags are
     * generally more comprehensive.
     *
     * @param audioFile The {@link File} object representing the audio file to analyze.
     * @return The detected {@link AudioTagFormat}, or {@link AudioTagFormat#UNKNOWN}
     * if no recognized format is found.
     * @throws IOException If an I/O error occurs while reading the file (e.g., file not found,
     * permission issues).
     */
    public static AudioTagFormat detectTagFormat(File audioFile) throws IOException {
        // Basic validation of the input file
        if (audioFile == null || !audioFile.exists() || !audioFile.isFile()) {
            System.err.println("Invalid file: " + (audioFile != null ? audioFile.getAbsolutePath() : "null"));
            return AudioTagFormat.UNKNOWN;
        }

        long fileSize = audioFile.length();
        // A minimum file size is required to check for any tags
        if (fileSize < 32) { // Smallest possible APE tag is 32 bytes, ID3v1 is 128
            return AudioTagFormat.UNKNOWN;
        }

        // Use RandomAccessFile to efficiently read bytes from specific positions
        try (RandomAccessFile raf = new RandomAccessFile(audioFile, "r")) {
            // --- 1. Check for header-based tags (usually more definitive) ---

            // Check for ID3v2 tag (at the very beginning of the file)
            // ID3v2 tags start with "ID3"
            byte[] id3v2Header = new byte[3];
            if (raf.read(id3v2Header) == 3 && new String(id3v2Header, StandardCharsets.US_ASCII).equals("ID3")) {
                return AudioTagFormat.ID3V2;
            }
            raf.seek(0); // Reset file pointer for the next check

            // Check for FLAC header (uses Vorbis Comment for metadata)
            // FLAC files start with "fLaC"
            byte[] flacHeader = new byte[4];
            if (raf.read(flacHeader) == 4 && new String(flacHeader, StandardCharsets.US_ASCII).equals("fLaC")) {
                return AudioTagFormat.VORBIS_COMMENT; // FLAC embeds metadata using Vorbis Comment
            }
            raf.seek(0); // Reset file pointer

            // Check for Ogg container header (used by Vorbis, Opus, Speex - all use Vorbis Comment)
            // Ogg files start with "OggS"
            byte[] oggHeader = new byte[4];
            if (raf.read(oggHeader) == 4 && new String(oggHeader, StandardCharsets.US_ASCII).equals("OggS")) {
                return AudioTagFormat.VORBIS_COMMENT;
            }
            raf.seek(0); // Reset file pointer

            // Check for WAV/BWF header (RIFF format)
            // WAV files start with "RIFF" at byte 0 and "WAVE" at byte 8
            byte[] riffWaveHeader = new byte[12];
            if (raf.read(riffWaveHeader) == 12 &&
                new String(riffWaveHeader, 0, 4, StandardCharsets.US_ASCII).equals("RIFF") &&
                new String(riffWaveHeader, 8, 4, StandardCharsets.US_ASCII).equals("WAVE")) {
                return AudioTagFormat.WAV_INFO;
            }
            raf.seek(0); // Reset file pointer

            // Check for AIFF header (FORM chunk)
            // AIFF files start with "FORM" at byte 0 and "AIFF" at byte 8
            byte[] formAiffHeader = new byte[12];
            if (raf.read(formAiffHeader) == 12 &&
                new String(formAiffHeader, 0, 4, StandardCharsets.US_ASCII).equals("FORM") &&
                new String(formAiffHeader, 8, 4, StandardCharsets.US_ASCII).equals("AIFF")) {
                return AudioTagFormat.AIFF_INFO;
            }
            raf.seek(0); // Reset file pointer

            // Check for MP4 (simplified check for 'ftyp' atom)
            // MP4 files are complex, but the 'ftyp' atom is typically at offset 4.
            // A full parser would navigate the atom structure. This is a basic indicator.
            if (fileSize >= 8) { // Need at least 8 bytes for size + 'ftyp'
                raf.seek(4); // Skip the initial 4-byte size field of the first atom
                byte[] ftypBytes = new byte[4];
                if (raf.read(ftypBytes) == 4 && new String(ftypBytes, StandardCharsets.US_ASCII).equals("ftyp")) {
                    return AudioTagFormat.MP4_TAG;
                }
            }
            raf.seek(0); // Reset file pointer


            // --- 2. Check for footer-based tags (less common for primary metadata, but still relevant) ---

            // Check for ID3v1 tag (at the very end of the file, 128 bytes from EOF)
            // ID3v1 tags start with "TAG"
            if (fileSize >= 128) {
                raf.seek(fileSize - 128); // Move to 128 bytes before the end of the file
                byte[] id3v1Tag = new byte[3];
                if (raf.read(id3v1Tag) == 3 && new String(id3v1Tag, StandardCharsets.US_ASCII).equals("TAG")) {
                    return AudioTagFormat.ID3V1;
                }
            }

            // Check for APE Tag (APEv2 footer is 32 bytes, usually at the very end)
            // APEv2 footer starts with "APETAGEX"
            if (fileSize >= 32) {
                raf.seek(fileSize - 32); // Move to 32 bytes before the end of the file
                byte[] apeFooter = new byte[8];
                if (raf.read(apeFooter) == 8 && new String(apeFooter, StandardCharsets.US_ASCII).equals("APETAGEX")) {
                    return AudioTagFormat.APE_TAG;
                }
            }

            // If no known tag format is detected after all checks
            return AudioTagFormat.UNKNOWN;

        } catch (IOException e) {
            // Log the error or re-throw it, depending on your application's error handling strategy
            System.err.println("Error reading audio file " + audioFile.getAbsolutePath() + ": " + e.getMessage());
            throw e; // Re-throw the exception to indicate a failure in detection
        }
    }

}

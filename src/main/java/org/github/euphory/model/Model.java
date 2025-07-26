/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory.model;

import java.io.File;

import javafx.scene.media.Media;

/**
 * @author Daniel Toffetti
 *
 * The Model class provides access to the application's data. It acts as a
 * central point for managing the current media file and album data.
 */
public class Model {

    /**
     * The currently selected media file.
     */
    private static File currentFile;

    /**
     * The current album data.
     */
    private static AlbumDataViewModel currentAlbum;

    /**
     * Private constructor to prevent instantiation.
     */
    private Model() {
    }

    /**
     * Sets the current media file.
     *
     * @param file The file to set as the current media file.
     */
    public static void setMediaFile(File file) {
        currentFile = file;
    }

    /**
     * Gets the current media file.
     *
     * @return The current media file.
     */
    public static File getMediaFile() {
        return currentFile;
    }

    /**
     * Gets the file name of the current media file.
     *
     * @return The file name of the current media file.
     */
    public static String getFileName() {
        if (currentFile != null) {
            return currentFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * Gets the Media object for the current media file.
     *
     * @return The Media object for the current media file, or null if no file is selected.
     */
    public static Media getMedia() {
        if (currentFile != null) {
            return new Media(currentFile.toURI().toString());
        }
        return null;
    }

    /**
     * Sets the current album data.
     *
     * @param album The album data to set.
     */
    public static void setCurrentAlbum(AlbumDataViewModel album) {
        currentAlbum = album;
    }

    /**
     * Gets the current album data.
     *
     * @return The current album data.  If no album data exists, a new one is created.
     */
    public static AlbumDataViewModel getCurrentAlbum() {
        if (currentAlbum == null) {
            currentAlbum = new AlbumDataViewModel();
        }
        return currentAlbum;
    }
    
}

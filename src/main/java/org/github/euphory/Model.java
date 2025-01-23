/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory;

import java.io.File;
import javafx.scene.media.Media;

/**
 * @author Daniel Toffetti
 * 
 */
public class Model {
    
    static File currentFile;
    
    public Model() {
        
    }
    
    public static void setMediaFile(File file) {
        currentFile = file;
    }
    
    public static File getMediaFile() {
        return currentFile;
    }
    
    public static String getFileName() {
        return currentFile.getAbsolutePath();
    }
    
    public static Media getMedia() {
        if (currentFile != null) {
            return new Media(currentFile.toURI().toString());
        }
        return null;
    }
    
}

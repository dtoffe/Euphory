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
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;

/**
 * @author Daniel Toffetti
 * 
 */
public class FileService {
    
    public static Media openMediaFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Mp3/M4a File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.mp3", "*.m4a")
        );
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file == null) {
            Main.showAlert(Alert.AlertType.INFORMATION, "Info", "No file was selected", "");
            return null;
        }
        Model.setMediaFile(file);
        return Model.getMedia();
    }

    public void saveMediaFile(File file, String content) throws IOException {
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showSaveDialog(null);
//        if (file != null) {
//            try {
//                fileService.saveFile(file, textArea.getText());
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Show error message to the user
//            }
//        }
    }

}

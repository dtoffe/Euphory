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
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Daniel Toffetti
 * 
 */
public class Controller {

    MediaPlayer mediaPlayer;

    @FXML
    private Button openButton;

    @FXML
    private Button rewindButton;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button saveButton;
    
    @FXML
    private FontIcon saveIcon;
    
    @FXML
    private Label runTimeLabel;

    @FXML
    private Label maxTimeLabel;

    @FXML
    private Slider songSlider;
    
    @FXML
    private Canvas waveFormsCanvas;
    
    @FXML
    private ImageView coverImageView;
    
    @FXML
    private HBox id3DataBox;
    
    @FXML
    private ListView<String> editableListView;
    
    @FXML
    private TextField newItemField;
    
    @FXML
    private Button addButton;
    
    private final ObservableList<String> items = FXCollections.observableArrayList();

    public Controller() {
        songSlider = new Slider();
    }
    
    @FXML
    private void initialize() throws Exception {
        songSlider.setMin(0);
        playButton.setOnAction(e -> mediaPlayer.play());
        pauseButton.setOnAction(e -> mediaPlayer.pause());
        stopButton.setOnAction(e -> mediaPlayer.stop());
        Image image = new Image("none.png");
        coverImageView.setImage(image);
        setControlsEnabled(false);
        
        editableListView.setItems(items);
        editableListView.setCellFactory(TextFieldListCell.forListView());
        editableListView.setEditable(true);
        editableListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            items.set(index, newValue);
        });
    }

    @FXML
    private void openButtonAction(ActionEvent actionEvent) {
        Media media = FileService.openMediaFile();
        if (media == null) {
            return;
        }
        Main.appendFileNameToTitle(Model.getFileName());
        
        mediaPlayer = new MediaPlayer(media);
        songSlider.setMin(0);
        mediaPlayer.setOnReady(() -> {
            songSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            maxTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getTotalDuration()));
        });

        mediaPlayer.currentTimeProperty().addListener((Observable observable) -> {
            if (!songSlider.isValueChanging()) { // Check if the user is not currently dragging the slider
                songSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());
                runTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getCurrentTime()));
            }
        });
        
        songSlider.valueChangingProperty().addListener((observable, wasChanging, isChanging) -> {
            if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(songSlider.getValue()));
                runTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getCurrentTime()));
            }
        });

        songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!songSlider.isValueChanging()) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                    runTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getCurrentTime()));
                }
            }
        });
        onContentEdited();
    }
    
    @FXML
    private void analyzeButtonAction(ActionEvent actionEvent) {
        File file = Model.getMediaFile();
        if (file != null) {
            GraphicsContext gc = waveFormsCanvas.getGraphicsContext2D();
            gc.setFill(Color.BLUE);
            gc.fillRect(50, 50, 100, 100); // Draw a blue rectangle on the canvas
        } else {
            Main.showAlert(Alert.AlertType.INFORMATION, "Info", "Open a media file first", "");
        }
    }
    
    @FXML
    private void rewindButtonAction(ActionEvent actionEvent) {
        mediaPlayer.seek(Duration.seconds(songSlider.getValue() - 5));
        runTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getCurrentTime()));
    }

    @FXML
    private void playButtonAction(ActionEvent actionEvent) {
        mediaPlayer.play();
    }

    @FXML
    private void pauseButtonAction(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    @FXML
    private void stopButtonAction(ActionEvent actionEvent) {
        mediaPlayer.stop();
    }

    @FXML
    private void forwardButtonAction(ActionEvent actionEvent) {
        mediaPlayer.seek(Duration.seconds(songSlider.getValue() + 5));
        runTimeLabel.setText(Util.toFormattedTime(mediaPlayer.getCurrentTime()));
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        onContentSaved();
    }
    
    public void onContentEdited() {
        saveIcon.getStyleClass().add("font-icon-red");
    }

    public void onContentSaved() {
        saveIcon.getStyleClass().remove("font-icon-red");
    }

    @FXML
    private void cueButtonAction(ActionEvent actionEvent) {
        //
    }
    
    @FXML
    private void handleAddItem(ActionEvent actionEvent) {
        String newItem = newItemField.getText().trim();
        if (!newItem.isEmpty()) {
            items.add(newItem);
            newItemField.clear();
        }
    }

    public void setControlsEnabled(boolean enabled) {
        for (var node : id3DataBox.getChildren()) {
            if (node instanceof Control) {
                ((Control) node).setDisable(!enabled);
            }
        }
    }
    
}

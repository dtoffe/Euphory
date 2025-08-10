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
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import org.github.euphory.model.Model;
import org.github.euphory.model.TrackDataViewModel;
import org.github.euphory.service.FileService;
import org.github.euphory.service.PlayerService;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Daniel Toffetti
 * 
 */
public class Controller {

    private final PlayerService playerService;

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
    private HBox metaDataBox;
    
    @FXML
    private TextField albumArtist;

    @FXML
    private TextField albumTitle;

    @FXML
    private TextField albumSubtitle;

    @FXML
    private TextField albumEpisode;

    @FXML
    private TextField albumDate;

    @FXML
    private TableView<TrackDataViewModel> dataTableView;
    
    @FXML
    private TextField trackArtist, trackTitle, trackNumber, startTime;

    @FXML
    private Button addButton;

    public Controller() {
        playerService = new PlayerService();
        songSlider = new Slider();
    }
    
    /**
     * Sets up the listeners for the song slider to handle user interaction and updates to the player's current time.
     */
    private void setupPlayerListeners() {
        songSlider.valueChangingProperty().addListener((observable, wasChanging, isChanging) -> {
            if (!isChanging) {
                playerService.seek(Duration.seconds(songSlider.getValue()));
                runTimeLabel.setText(Util.toFormattedTime(playerService.getCurrentTime()));
            }
        });
        songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!songSlider.isValueChanging()) {
                double currentTime = playerService.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    playerService.seek(Duration.seconds(newValue.doubleValue()));
                    runTimeLabel.setText(Util.toFormattedTime(playerService.getCurrentTime()));
                }
            }
        });
        playerService.getMediaPlayer().setOnReady(() -> {
            songSlider.setMin(0);
            songSlider.setMax(playerService.getTotalDuration().toSeconds());
            maxTimeLabel.setText(Util.toFormattedTime(playerService.getTotalDuration()));
        });
        playerService.getMediaPlayer().currentTimeProperty().addListener((Observable observable) -> {
            if (!songSlider.isValueChanging()) { // Check if the user is not currently dragging the slider
                songSlider.setValue(playerService.getCurrentTime().toSeconds());
                runTimeLabel.setText(Util.toFormattedTime(playerService.getCurrentTime()));
            }
        });
    }

    @FXML
    private void initialize() throws Exception {
        songSlider.setMin(0);
        Image image = new Image("none.png");
        coverImageView.setImage(image);
        setControlsEnabled(false);
        
        /*editableListView.setItems(items);
        editableListView.setCellFactory(TextFieldListCell.forListView());
        editableListView.setEditable(true);
        editableListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            items.set(index, newValue);
        });*/

    }

    @FXML
    private void openButtonAction(ActionEvent actionEvent) {
        Media media = FileService.openMediaFile();
        if (media == null) {
            return;
        } else {
            playerService.setMedia(media);
            appendFileNameToTitle(Model.getFileName());
            setupPlayerListeners();
            setupModelUIBindings();
            setupChangeListeners();
        }
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
        playerService.seek(Duration.seconds(songSlider.getValue() - 5));
        runTimeLabel.setText(Util.toFormattedTime(playerService.getCurrentTime()));
    }

    @FXML
    private void playButtonAction(ActionEvent actionEvent) {
        playerService.play();
    }

    @FXML
    private void pauseButtonAction(ActionEvent actionEvent) {
        playerService.pause();
    }

    @FXML
    private void stopButtonAction(ActionEvent actionEvent) {
        playerService.stop();
    }

    @FXML
    private void forwardButtonAction(ActionEvent actionEvent) {
        playerService.seek(Duration.seconds(songSlider.getValue() + 5));
        runTimeLabel.setText(Util.toFormattedTime(playerService.getCurrentTime()));
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        saveAll();
        onContentSaved();
    }
    
    private void saveAll() {
        
    }
    
    private void onContentEdited() {
        saveIcon.getStyleClass().add("font-icon-red");
    }

    private void onContentSaved() {
        saveIcon.getStyleClass().remove("font-icon-red");
    }

    @FXML
    private void cueButtonAction(ActionEvent actionEvent) {
        //
    }

    @FXML
    private void handleAddTrack(ActionEvent actionEvent) {
        String artist = trackArtist.getText();
        String title = trackTitle.getText();
        int track = Integer.parseInt(trackNumber.getText());
        int time = Integer.parseInt(startTime.getText());
    
        // Create a new Song object
        TrackDataViewModel newTrack = new TrackDataViewModel(artist, title, track, time);
        
        // Add to the TableView
        dataTableView.getItems().add(newTrack);
        
        // Clear the input fields
        trackArtist.clear();
        trackTitle.clear();
        trackNumber.clear();
        startTime.clear();
    }

    private void setControlsEnabled(boolean enabled) {
        for (var node : metaDataBox.getChildren()) {
            if (node instanceof Control) {
                ((Control) node).setDisable(!enabled);
            }
        }
    }
    
    private void setupModelUIBindings() {
        // Bind the ViewModel to the UI controls
        Bindings.bindBidirectional(albumArtist.textProperty(), Model.getCurrentAlbum().albumArtistProperty());
        Bindings.bindBidirectional(albumTitle.textProperty(), Model.getCurrentAlbum().albumTitleProperty());
        Bindings.bindBidirectional(albumSubtitle.textProperty(), Model.getCurrentAlbum().albumSubtitleProperty());
        Bindings.bindBidirectional(albumDate.textProperty(), Model.getCurrentAlbum().albumDateProperty());
        Bindings.bindBidirectional(albumEpisode.textProperty(), Model.getCurrentAlbum().albumEpisodeProperty());
        // Bindings.bindContentBidirectional(dataTableView.getItems(), viewModel.getAlbumTracks());
        // Bindings.bindBidirectional(coverImageView.imageProperty(), viewModel.coverImageProperty());
    }

    private void setupChangeListeners() {

        coverImageView.imageProperty().addListener((observable, oldValue, newValue) -> onContentEdited());

        albumArtist.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        albumTitle.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        albumSubtitle.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        albumDate.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        albumEpisode.textProperty().addListener((observable, oldValue, newValue) -> {
            onContentEdited();
            Model.getCurrentAlbum().validateEpisode(new Alert(AlertType.NONE));
        });

        trackArtist.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        trackTitle.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        trackNumber.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());
        startTime.textProperty().addListener((observable, oldValue, newValue) -> onContentEdited());

    }

    public static void appendFileNameToTitle(String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            String baseTitle = Main.getStage().getTitle().split("\\.")[0];
            Main.getStage().setTitle(baseTitle + ".   -   " + fileName);
        }
    }
    
}

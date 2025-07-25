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

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Daniel Toffetti
 * 
 * JavaFX Main
 */
public class Main extends Application {

    private static Stage stage;
    
    @Override
    public void start(Stage stage) throws IOException {
        setupStage(stage);
    }

    private void setupStage(Stage stage) throws IOException {
        setStage(stage);
        Scene scene = new Scene(loadFXML("Controller"), 640, 480);
        stage.setScene(scene);
        Image icon512 = new Image("wave_512.png");
        Image icon256 = new Image("wave_256.png");
        Image icon128 = new Image("wave_128.png");
        Image icon064 = new Image("wave_064.png");
        Image icon032 = new Image("wave_032.png");
        Image icon016 = new Image("wave_016.png");
        stage.getIcons().addAll(icon512, icon256, icon128, icon064, icon032, icon016);
        stage.setTitle("Euphory - A tagging and track detector tool for collectors of DJ radio shows.");
        stage.setMaximized(true);
        stage.show();
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error Loading FXML", "An error occurred while loading the FXML file.", e.getMessage());
            return null; // Or handle the error in a way that makes sense for your application
        }
    }
    
    private void setStage(Stage stage) {
        Main.stage = stage;
    }

    static public Stage getStage() {
        return Main.stage;
    }
  
    public static void main(String[] args) {
        launch();
    }

    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(stage.getScene().getWindow());
        alert.showAndWait();
    }
    
}

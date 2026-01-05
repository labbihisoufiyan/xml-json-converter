package com.mycompany.xml.json.converter;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

public class XmlJsonConverter extends Application {

    private TextArea xmlArea;
    private TextArea jsonArea;
    private CheckBox useApiCheckbox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Convertisseur XML ↔ JSON");

        // Titre centré en haut
        Label titleLabel = new Label("Convertisseur XML ↔ JSON");
        titleLabel.setFont(Font.font("Times New Roman", javafx.scene.text.FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // Zones de texte
        xmlArea = createStyledTextArea();
        jsonArea = createStyledTextArea();

        // Boutons rouges
        Button chooseXmlButton = createStyledButton("📁 Choisir XML");
        Button chooseJsonButton = createStyledButton("📁 Choisir JSON");
        Button toJsonButton = createStyledButton("➡️ Vers JSON");
        Button toXmlButton = createStyledButton("⬅️ Vers XML");
        Button cleanButton = createStyledButton("🗑 Nettoyer");

        // Checkbox
        useApiCheckbox = new CheckBox("Utiliser la bibliothèque json.jar");
        useApiCheckbox.setFont(Font.font("Times New Roman", 14));
        useApiCheckbox.setTextFill(Color.BLACK);

        // Actions
        chooseXmlButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un fichier XML");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    String content = new String(Files.readAllBytes(selectedFile.toPath()));
                    xmlArea.setText(content);
                    jsonArea.clear();
                } catch (IOException ex) {
                    showErrorDialog("Erreur", "❌ Erreur lors de la lecture du fichier XML.");
                }
            }
        });

        chooseJsonButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un fichier JSON");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    String content = new String(Files.readAllBytes(selectedFile.toPath()));
                    jsonArea.setText(content);
                    xmlArea.clear();
                } catch (IOException ex) {
                    showErrorDialog("Erreur", "❌ Erreur lors de la lecture du fichier JSON.");
                }
            }
        });

        toJsonButton.setOnAction(e -> {
            String xmlContent = xmlArea.getText().trim();
            if (!xmlContent.isEmpty()) {
                String json;
                if (useApiCheckbox.isSelected()) {
                    json = XmlToJsonAPI.convertXmlStringToJson(xmlContent);
                } else {
                    json = XmlToJson.convertXmlStringToJson(xmlContent);
                }
                if (json.startsWith("❌")) {
                    showErrorDialog("Erreur de conversion", json);
                } else {
                    jsonArea.setText(json);
                }
            } else {
                showErrorDialog("Attention", "⚠️ Zone XML vide.");
            }
        });

        toXmlButton.setOnAction(e -> {
            String jsonContent = jsonArea.getText().trim();
            if (!jsonContent.isEmpty()) {
                String xml;
                if (useApiCheckbox.isSelected()) {
                    xml = JsonToXmlAPI.convertJsonStringToXml(jsonContent);
                } else {
                    xml = JsonToXml.convertJsonStringToXml(jsonContent);
                }
                if (xml.startsWith("❌")) {
                    showErrorDialog("Erreur de conversion", xml);
                } else {
                    xmlArea.setText(xml);
                }
            } else {
                showErrorDialog("Attention", "⚠️ Zone JSON vide.");
            }
        });

        cleanButton.setOnAction(e -> {
            xmlArea.clear();
            jsonArea.clear();
        });

        // Layout XML
        Label xmlLabel = new Label("XML");
        xmlLabel.setFont(Font.font("Times New Roman", javafx.scene.text.FontWeight.BOLD, 16));
        xmlLabel.setTextFill(Color.BLACK);

        VBox xmlBox = new VBox(10, xmlLabel, chooseXmlButton, xmlArea);

        // Layout JSON
        Label jsonLabel = new Label("JSON");
        jsonLabel.setFont(Font.font("Times New Roman", javafx.scene.text.FontWeight.BOLD, 16));
        jsonLabel.setTextFill(Color.BLACK);

        VBox jsonBox = new VBox(10, jsonLabel, chooseJsonButton, jsonArea);

        // Conversion + checkbox
        VBox conversionBox = new VBox(15, toJsonButton, toXmlButton, useApiCheckbox);
        conversionBox.setPadding(new Insets(10));
        conversionBox.setAlignment(Pos.TOP_CENTER);

        // Regroupement horizontal
        HBox mainContent = new HBox(15, xmlBox, conversionBox, jsonBox);

        // Layout global
        VBox layout = new VBox(15, titleLabel, mainContent, cleanButton);
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(Color.web("#e0e0e0"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(layout, 980, 560);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextArea createStyledTextArea() {
        TextArea area = new TextArea();
        area.setWrapText(true);
        area.setFont(Font.font("Times New Roman", 14));
        area.setPrefHeight(400);
        area.setStyle("-fx-control-inner-background: #ffffff; -fx-text-fill: #000000;");
        area.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        area.setPadding(new Insets(10));
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Times New Roman", 15));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #c62828; -fx-background-radius: 8;");
        button.setPadding(new Insets(8, 16, 8, 16));
        button.setEffect(new DropShadow(3, Color.GRAY));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #b71c1c; -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #c62828; -fx-background-radius: 8;"));
        return button;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

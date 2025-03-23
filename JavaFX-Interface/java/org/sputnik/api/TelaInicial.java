package org.sputnik.api;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TelaInicial {
    public static VBox createTelaInicial() {
        Label title = new Label("Sputnik");
        title.getStyleClass().add("title");

        Label createProject = new Label("Crie novo projeto");
        Label openFile = new Label("Abra arquivo");
        Label openFolder = new Label("Abra pasta");
        Label cloneGit = new Label("Clone repositório Git");

        createProject.getStyleClass().add("meio");
        openFile.getStyleClass().add("meio");
        openFolder.getStyleClass().add("meio");
        cloneGit.getStyleClass().add("meio");

        VBox vbox = new VBox(10, title, createProject, openFile, openFolder, cloneGit);
        vbox.getStyleClass().add("inicio");
        return vbox;
    }
}


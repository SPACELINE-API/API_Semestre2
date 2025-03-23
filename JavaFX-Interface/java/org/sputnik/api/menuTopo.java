package org.sputnik.api;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;


public class menuTopo {
    public static MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Arquivo");
        MenuItem newFile = new MenuItem("Novo");
        MenuItem openFile = new MenuItem("Abrir");
        MenuItem saveFile = new MenuItem("Salvar");

        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        Menu editMenu = new Menu("Editar");
        MenuItem undo = new MenuItem("Desfazer");
        MenuItem redo = new MenuItem("Refazer");
        MenuItem delete = new MenuItem("Deletar");

        editMenu.getItems().addAll(undo, redo, delete);

        Menu helpMenu = new Menu("Ajuda");
        MenuItem about = new MenuItem("Sobre");

        helpMenu.getItems().addAll(about);
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        return menuBar;
    }
}

package org.sputnik;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CodeArea editor = new CodeArea();
            editor.setVisible(true); // Torna a janela visível na tela.
        });
    }
}
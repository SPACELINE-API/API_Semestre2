package org.sputnik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CodeArea extends JFrame {
    private final JTextArea textArea; // Área de texto para edição
    private final FileManager fileManager = new FileManager(); // Gerenciador de arquivos

    public CodeArea() {
        // Configuração da janela
        setTitle("Editor Sputnik");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Área de texto e scroll
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Criação do Menu Dropdown
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");

        // Arquivo - Item Abrir
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(this::handleOpen);

        // Arquivo - Item Salvar
        JMenuItem saveItem = new JMenuItem("Salvar");
        saveItem.addActionListener(this::handleSave);

        // Arquivo - Item Salvar Como
        JMenuItem saveAsItem = new JMenuItem("Salvar Como");
        saveAsItem.addActionListener(this::handleSaveAs);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // Manipulador de Abrir Arquivo
    private void handleOpen(ActionEvent e) {
        String content = fileManager.openFile();
        if (content != null) {
            textArea.setText(content);
            JOptionPane.showMessageDialog(this, "Arquivo carregado com sucesso!");
        }
    }

    // Manipulador de Salvar
    private void handleSave(ActionEvent e) {
        boolean success = fileManager.saveFile(textArea.getText());
        if (success) {
            JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
        }
    }

    // Manipulador de Salvar Como
    private void handleSaveAs(ActionEvent e) {
        boolean success = fileManager.saveFileAs(textArea.getText());
        if (success) {
            JOptionPane.showMessageDialog(this, "Arquivo salvo como: " + fileManager.getCurrentFile().getName());
        }
    }
}

package org.sputnik;

import javax.swing.*;
import java.awt.*;

public class CodeArea extends JFrame {
    private JTextArea textArea;

    public CodeArea() {
        setTitle("Editor Sputnik");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

}
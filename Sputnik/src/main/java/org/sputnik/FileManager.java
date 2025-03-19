package org.sputnik;

import java.io.*;
import javax.swing.*;

/**
 * Classe utilitária para operações de salvamento de arquivos em um editor de texto.
 * Pode ser integrada em um projeto de IDE.
 */
public class FileManager {
    
    private File currentFile; // Armazena o arquivo atualmente aberto ou salvo
    private final JFileChooser fileChooser; // Seletor de arquivos para "Salvar Como"
    
    /**
     * Construtor que inicializa o seletor de arquivos.
     */
    public FileManager() {
        fileChooser = new JFileChooser();
    }

    /**
     * Define o arquivo atual a ser manipulado.
     * @param file Arquivo a ser definido como atual.
     */
    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    /**
     * Retorna o arquivo atual.
     * @return O arquivo atualmente sendo editado.
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Salva o conteúdo fornecido no arquivo atual.
     * Se não houver um arquivo definido, chama "saveFileAs".
     * @param content Conteúdo a ser salvo.
     * @return Verdadeiro se o salvamento for bem-sucedido, falso caso contrário.
     */
    public boolean saveFile(String content) {
        if (currentFile != null) {
            return writeToFile(currentFile, content);
        } else {
            return saveFileAs(content);
        }
    }

    /**
     * Exibe um seletor de arquivos e salva o conteúdo em um novo arquivo.
     * @param content Conteúdo a ser salvo.
     * @return Verdadeiro se o arquivo foi salvo com sucesso, falso caso contrário.
     */
    public boolean saveFileAs(String content) {
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            return writeToFile(currentFile, content);
        }
        return false; // Retorna falso se o usuário cancelar a operação
    }

    /**
     * Escreve o conteúdo no arquivo especificado.
     * @param file Arquivo onde o conteúdo será salvo.
     * @param content Conteúdo a ser salvo.
     * @return Verdadeiro se o salvamento for bem-sucedido, falso caso contrário.
     */
    private boolean writeToFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    /**
     * Abre o Arquivo Requerido
     * @return     
     */
    public String openFile(){
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION){
            currentFile = fileChooser.getSelectedFile();
            return readFile(currentFile);
        }
        return null;
    }
    /**
     * Lê o arquivo
     * @return
     */
        private String readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
}


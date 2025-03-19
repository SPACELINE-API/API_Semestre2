public class InitSave {
    private FileManager fileManager; // Instância do gerenciador de arquivos

    /**
     * Construtor da classe InitSave.
     * Inicializa o gerenciador de arquivos e realiza a operação de salvamento.
     * @param textArea Componente contendo o texto a ser salvo.
     */
    public InitSave(javax.swing.JTextArea textArea) {
        fileManager = new FileManager(); // Inicializa o gerenciador de arquivos

        // Define o arquivo atual (caso já tenha um aberto)
        fileManager.setCurrentFile(new File("caminho/do/arquivo.txt"));

        // Obtém o conteúdo da área de texto e salva
        String conteudoEditor = textArea.getText();
        boolean sucesso = fileManager.saveFile(conteudoEditor);

        // Exibe a mensagem de sucesso ou erro
        if (sucesso) {
            System.out.println("Arquivo salvo com sucesso!");
        } else {
            System.out.println("Erro ao salvar o arquivo.");
        }
    }
}

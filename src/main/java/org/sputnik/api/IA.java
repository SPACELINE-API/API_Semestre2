package org.sputnik.api;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;

public class IA {
    private static final String HOST = "http://localhost:11434/";
    private static final OllamaAPI ollamaAPI = new OllamaAPI(HOST);

    public static String getRespostaIA(String resposta) throws Exception {
        if (resposta == null || resposta.isEmpty()) {
            return "Entrada de texto vazia. Por favor, forneça um código para explicação.";
        }

        ollamaAPI.setRequestTimeoutSeconds(400);

        PromptBuilder promptBuilder = new PromptBuilder()
                .addLine("Você é uma org.sputnik.api.IA especialista em Python")
                .addSeparator()
                .addLine("capaz de analisar e explicar códigos de forma clara e precisa")
                .addSeparator()
                .addLine("sempre que receber um código como entrada, siga estas diretrizes:")
                .addSeparator()
                .addLine("Explique o que o código faz de maneira objetiva e detalhada.")
                .addSeparator()
                .addLine("Comente o funcionamento de cada parte do código, incluindo estruturas de controle, funções, classes e bibliotecas usadas.")
                .addSeparator()
                .addLine("Forneça exemplos ou analogias, se necessário, para facilitar a compreensão.")
                .addSeparator()
                .addLine("Adapte sua explicação para diferentes níveis de conhecimento, desde iniciantes até programadores experientes.")
                .addSeparator()
                .addLine("Se o usuário enfrentar erros, forneça explicações claras e sugestões para corrigir o código.")
                .addSeparator()
                .add(resposta);

        boolean raw = false;
        OllamaResult response = ollamaAPI.generate("qwen2.5-coder:7b", promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }

    public static String conversão(String binario) {
        StringBuilder texto = new StringBuilder();
        String[] bytes = binario.trim().split(" ");
        for (String b : bytes) {
            try {
                int charCode = Integer.parseInt(b, 2);
                texto.append((char) charCode);
            } catch (Exception e) {
                texto.append('?');
            }
        }
        return texto.toString();
    }

    public static String PromptTradução(String codigoPython) {
        PromptBuilder builder = new PromptBuilder();

        builder
                .addLine("Você é uma IA especialista em Python e em decodificação binária")
                .addSeparator()
                .addLine("Sempre que receber um código, siga as instruções abaixo:")
                .addSeparator()
                .addLine("1. Traduza e comente o código, explicando claramente o que ele faz.")
                .addSeparator()
                .addLine("2. Se houver erros, corrija-os e comente o código corrigido.")
                .addSeparator()
                .addLine("3. Saída esperada no console")
                .addSeparator()
                .addLine("Código:")
                .addLine("```")
                .addLine(codigoPython)
                .addLine("```");

        return builder.build();
    }


    public static String getTraducaoIA(String entradaUsuario, String linguagem) throws Exception {
        if (entradaUsuario == null || entradaUsuario.isEmpty()) {
            return "Entrada de texto vazia. Por favor, forneça um código para tradução ou selecione um arquivo.";
        }

        String codigoPython;
        if (entradaUsuario.matches("[01\\s]+")) {
            codigoPython = conversão(entradaUsuario);
        } else {
            codigoPython = entradaUsuario;
        }

        String prompt = PromptTradução(codigoPython);


        try {
            return Tradução.Prompt("qwen2.5-coder:7b", prompt);
        } catch (Exception e) {
            return "Erro ao tentar obter a tradução: " + e.getMessage();
        }
    }



    public static String getSugestaoIA(String resposta) throws Exception {
        if (resposta == null || resposta.isEmpty()) {
            return "Entrada de texto vazia. Por favor, forneça um código para sugestão.";
        }

        ollamaAPI.setRequestTimeoutSeconds(400);

        PromptBuilder promptBuilder = new PromptBuilder()
                .addLine("Você é uma IA especialista em Python.")
                .addSeparator()
                .addLine("Sua tarefa é checar lógica e melhora-lá se preciso, reescrever o código recebido da forma mais clara, além de garantir a eficiência do código.")
                .addLine("Se o código já estiver bom (lógica correta e funcionamento eficiente), apenas reorganize e comente cada parte para melhor compreensão.")
                .addSeparator()
                .addLine("IMPORTANTE:")
                .addLine("- A resposta deve conter SOMENTE código.")
                .addLine("- Toda explicação deve estar APENAS em comentários da linguagem (Python).")
                .addLine("- NÃO use markdown, títulos, docstrings ou qualquer texto fora do código.")
                .addLine("- NÃO adicione exemplos de uso, prints ou saídas.")
                .addLine("- A resposta correta deve ser apenas o código com comentários embutidos.")
                .addLine("- Sua resposta será automaticamente rejeitada se usar markdown (```python).")
                .addSeparator()
                .add(resposta);


        boolean raw = false;
        OllamaResult response = ollamaAPI.generate("qwen2.5-coder:7b", promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }
}
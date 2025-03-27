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
}

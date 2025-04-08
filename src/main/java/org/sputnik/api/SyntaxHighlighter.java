package org.sputnik.api;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collection;
import java.util.Collections;

public class SyntaxHighlighter {
    private static final String[] KEYWORDS = {
            "def", "return", "if", "else", "elif", "for", "while", "import", "from",
            "class", "try", "except", "finally", "with", "as", "lambda", "yield",
            "pass", "break", "continue", "global", "nonlocal", "assert", "del", "raise"
    };

    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("\\b(" + String.join("|", KEYWORDS) + ")\\b");
    private static final Pattern PATTERN_STRINGS = Pattern.compile("\"([^\"]*)\"|'([^']*)'");

    public static void applyHighlighting(CodeArea codeArea) {
        codeArea.multiPlainChanges().subscribe(change -> {
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        });
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        Matcher matcher = PATTERN_KEYWORDS.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            spansBuilder.add(Collections.singleton("letras"), matcher.start() - lastEnd);
            spansBuilder.add(Collections.singleton("keyword"), matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }

        matcher = PATTERN_STRINGS.matcher(text);
        while (matcher.find()) {
            spansBuilder.add(Collections.singleton("letras"), matcher.start() - lastEnd);
            spansBuilder.add(Collections.singleton("string"), matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }

        spansBuilder.add(Collections.singleton("letras"), text.length() - lastEnd);


        return spansBuilder.create();
    }
}

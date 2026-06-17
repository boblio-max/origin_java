package im.manus.origin.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final List<TokenRule> RULES = new ArrayList<>();

    static {
        // Order matters: match more specific patterns first
        addRule(null, "[ \\t]+"); // Skip whitespace
        addRule(null, "#.*");     // Skip comments
        addRule(TokenType.NEWLINE, "\\n");
        addRule(TokenType.HEX, "0x[0-9a-fA-F]+");
        addRule(TokenType.FLOAT, "\\d+\\.\\d+");
        addRule(TokenType.INT, "\\d+");
        addRule(TokenType.STRING, "\".*?\"|'.*?'");
        addRule(TokenType.COMP, "===|!==|==|!=|<=|>=|<>|<|>");
        addRule(TokenType.LOGIC, "\\&\\&|\\|\\||\\b(and|or|not)\\b|!");
        addRule(TokenType.UNARY, "\\+\\+|\\-\\-");
        addRule(TokenType.ASSIGN_OP, "\\+=|\\-=|\\*=|/=|%=|\\*\\*=|//=|&=|\\|=");
        addRule(TokenType.SPECIAL, "\\?\\?|->|=>|<=>|::");
        addRule(TokenType.ASSIGN, "=");
        addRule(TokenType.ARITH, "\\+|\\-|\\*\\*|\\*|//|/|%|\\&|\\||\\^|<<|>>");
        addRule(TokenType.BRACKET, "\\[|\\]|\\{|\\}");
        addRule(TokenType.SYMBOL, "\\(|\\)|:|,|\\.|;|\\?");
        addRule(TokenType.KEYWORD, "\\b(none|if|elif|open|else|check|for|get|while|return|py|int|len|str|sqrt|float|let|rand_num|const|in|print|true|exec|false|break|input|continue|def|import|from|class|try|call|except|raise|set|pass|yield|with|as|del|assert|global|nonlocal|async|await|match|case|macro|inline|parallel|when|range|unless|loop|until|do|struct|enum|type|bool|interface|pub|priv)\\b");
        addRule(TokenType.IDENT, "[A-Za-z_][A-Za-z0-9_]*");
    }

    private static void addRule(TokenType type, String regex) {
        RULES.add(new TokenRule(type, Pattern.compile("^(" + regex + ")")));
    }

    private record TokenRule(TokenType type, Pattern pattern) {}

    public static List<Token> lex(String code) {
        List<Token> tokens = new ArrayList<>();
        String[] lines = code.split("(?<=\\n)");
        int lineNum = 1;

        for (String line : lines) {
            int col = 0;
            while (col < line.length()) {
                boolean matched = false;
                String remaining = line.substring(col);

                for (TokenRule rule : RULES) {
                    Matcher matcher = rule.pattern.matcher(remaining);
                    if (matcher.find()) {
                        String value = matcher.group(1);
                        if (rule.type != null) {
                            tokens.add(new Token(rule.type, value, lineNum, col));
                        }
                        col += value.length();
                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    throw new RuntimeException("Illegal Character '" + line.charAt(col) + "' at " + lineNum + ":" + col);
                }
            }
            // In the original Python lexer, it adds a NEWLINE token after each line.
            // But my rule already matches \n as NEWLINE. Let's ensure consistency.
            if (!line.endsWith("\n") && col > 0) {
                 tokens.add(new Token(TokenType.NEWLINE, "\\n", lineNum, col));
            }
            lineNum++;
        }
        tokens.add(new Token(TokenType.EOF, "", lineNum, 0));
        return tokens;
    }
}

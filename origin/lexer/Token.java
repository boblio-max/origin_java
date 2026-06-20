package origin.lexer;

public record Token(TokenType type, String value, int line, int col) {
    @Override
    public String toString() {
        return String.format("Token(%s, '%s', %d:%d)", type, value.replace("\n", "\\n"), line, col);
    }
}

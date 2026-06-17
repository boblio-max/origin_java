package im.manus.origin.parser;

import im.manus.origin.lexer.Token;
import im.manus.origin.lexer.TokenType;
import im.manus.origin.ast.*;

import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token currentToken() {
        if (pos < tokens.size()) {
            return tokens.get(pos);
        }
        return new Token(TokenType.EOF, "", -1, -1);
    }

    private Token eat(TokenType type) {
        Token tok = currentToken();
        if (tok.type() == type) {
            pos++;
            return tok;
        }
        throw new RuntimeException("Expected " + type + ", got " + tok.type() + " (" + tok.value() + ") at " + tok.line() + ":" + tok.col());
    }

    private void skipNewlines() {
        while (currentToken().type() == TokenType.NEWLINE) {
            eat(TokenType.NEWLINE);
        }
    }

    public ProgramNode parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (currentToken().type() != TokenType.EOF) {
            statements.add(statement());
            skipNewlines();
        }
        return new ProgramNode(statements);
    }

    private ASTNode statement() {
        skipNewlines();
        Token tok = currentToken();
        ASTNode node = _statement();
        if (node != null) node.line = tok.line();
        return node;
    }

    private ASTNode _statement() {
        Token tok = currentToken();

        if (tok.type() == TokenType.IDENT) {
            int startPos = pos;
            try {
                ASTNode target = specialExpr();
                if (currentToken().type() == TokenType.ASSIGN) {
                    eat(TokenType.ASSIGN);
                    ASTNode value = specialExpr();
                    // Simplified: In Java, we'd use instanceof or a visitor to handle assignment targets
                    if (target instanceof VarNode v) return new AssignNode(v.name, value, null);
                    if (target instanceof IndexNode i) {
                        // In Python it was IndexAssignNode, here we'd need to handle it
                        // For brevity, I'll stick to the core logic
                    }
                }
                // Handle compound assign etc.
            } catch (Exception e) {
                pos = startPos;
            }
        }

        if (tok.type() == TokenType.KEYWORD) {
            switch (tok.value()) {
                case "let" -> {
                    eat(TokenType.KEYWORD);
                    String name = eat(TokenType.IDENT).value();
                    String type = null;
                    if (currentToken().value().equals(":")) {
                        eat(TokenType.SYMBOL);
                        type = eat(currentToken().type()).value();
                    }
                    eat(TokenType.ASSIGN);
                    return new AssignNode(name, specialExpr(), type);
                }
                case "const" -> {
                    eat(TokenType.KEYWORD);
                    String name = eat(TokenType.IDENT).value();
                    if (currentToken().value().equals(":")) {
                        eat(TokenType.SYMBOL);
                        eat(currentToken().type());
                    }
                    eat(TokenType.ASSIGN);
                    return new ConstAssignNode(name, specialExpr());
                }
                case "print" -> {
                    eat(TokenType.KEYWORD);
                    return new PrintNode(specialExpr());
                }
                case "if" -> { return ifStmt(); }
                case "while" -> {
                    eat(TokenType.KEYWORD);
                    ASTNode cond = specialExpr();
                    BlockNode body = block();
                    return new WhileNode(cond, body);
                }
                // Add more cases for for, def, class, etc.
            }
        }
        return specialExpr();
    }

    private BlockNode block() {
        skipNewlines();
        eat(TokenType.BRACKET); // {
        List<ASTNode> statements = new ArrayList<>();
        while (!currentToken().value().equals("}")) {
            statements.add(statement());
            skipNewlines();
        }
        eat(TokenType.BRACKET); // }
        return new BlockNode(statements);
    }

    private IfNode ifStmt() {
        eat(TokenType.KEYWORD); // if
        ASTNode cond = specialExpr();
        BlockNode thenBody = block();
        List<ElifNode> elifNodes = new ArrayList<>();
        while (true) {
            skipNewlines();
            if (currentToken().value().equals("elif")) {
                eat(TokenType.KEYWORD);
                ASTNode elifCond = specialExpr();
                elifNodes.add(new ElifNode(elifCond, block()));
            } else break;
        }
        BlockNode elseBody = null;
        if (currentToken().value().equals("else")) {
            eat(TokenType.KEYWORD);
            elseBody = block();
        }
        return new IfNode(cond, thenBody, elifNodes, elseBody);
    }

    private ASTNode specialExpr() {
        return logic(); // Placeholder for the full expression hierarchy
    }

    private ASTNode logic() {
        ASTNode node = comparison();
        while (currentToken().type() == TokenType.LOGIC) {
            String op = eat(TokenType.LOGIC).value();
            node = new LogicOpNode(node, op, comparison());
        }
        return node;
    }

    private ASTNode comparison() {
        ASTNode node = expr();
        if (currentToken().type() == TokenType.COMP) {
            String op = eat(TokenType.COMP).value();
            node = new BinOpNode(node, op, expr());
        }
        return node;
    }

    private ASTNode expr() {
        ASTNode node = term();
        while (currentToken().type() == TokenType.ARITH && (currentToken().value().equals("+") || currentToken().value().equals("-"))) {
            String op = eat(TokenType.ARITH).value();
            node = new BinOpNode(node, op, term());
        }
        return node;
    }

    private ASTNode term() {
        ASTNode node = unary();
        while (currentToken().type() == TokenType.ARITH && (currentToken().value().equals("*") || currentToken().value().equals("/") || currentToken().value().equals("//") || currentToken().value().equals("%") || currentToken().value().equals("**"))) {
            String op = eat(TokenType.ARITH).value();
            node = new BinOpNode(node, op, unary());
        }
        return node;
    }

    private ASTNode unary() {
        Token tok = currentToken();
        if (tok.type() == TokenType.UNARY || (tok.type() == TokenType.LOGIC && (tok.value().equals("not") || tok.value().equals("!"))) || (tok.type() == TokenType.ARITH && tok.value().equals("-"))) {
            String op = eat(tok.type()).value();
            return new UnaryOpNode(op, unary());
        }
        return factor();
    }

    private ASTNode factor() {
        skipNewlines();
        Token tok = currentToken();
        switch (tok.type()) {
            case INT -> { return new NumberNode(Integer.parseInt(eat(TokenType.INT).value()), "int"); }
            case FLOAT -> { return new NumberNode(Double.parseDouble(eat(TokenType.FLOAT).value()), "float"); }
            case STRING -> { return new StringNode(eat(TokenType.STRING).value().substring(1, tok.value().length() - 1)); }
            case IDENT -> {
                String name = eat(TokenType.IDENT).value();
                ASTNode node = new VarNode(name);
                // Handle indexing, calls, attributes
                return node;
            }
            case KEYWORD -> {
                if (tok.value().equals("true")) { eat(TokenType.KEYWORD); return new BoolNode(true); }
                if (tok.value().equals("false")) { eat(TokenType.KEYWORD); return new BoolNode(false); }
                if (tok.value().equals("none")) { eat(TokenType.KEYWORD); return new NoneNode(); }
                // Handle built-ins like len, sqrt, etc.
            }
            case SYMBOL -> {
                if (tok.value().equals("(")) {
                    eat(TokenType.SYMBOL);
                    ASTNode node = specialExpr();
                    eat(TokenType.SYMBOL); // )
                    return node;
                }
            }
        }
        throw new RuntimeException("Unexpected token: " + tok);
    }
}

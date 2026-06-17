package im.manus.origin;

import im.manus.origin.lexer.Lexer;
import im.manus.origin.lexer.Token;
import im.manus.origin.parser.Parser;
import im.manus.origin.ast.ProgramNode;
import im.manus.origin.interpreter.JavaInterpreter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar origin.jar <file.or>");
            return;
        }

        try {
            String source = Files.readString(Paths.get(args[0]));
            List<Token> tokens = Lexer.lex(source);
            
            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parse();
            
            JavaInterpreter interpreter = new JavaInterpreter();
            String javaCode = interpreter.translate(program);
            
            System.out.println("// Generated Java Code:");
            System.out.println(javaCode);
            
            // In a full implementation, we might compile and run this code
            // or use a direct AST evaluator.
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

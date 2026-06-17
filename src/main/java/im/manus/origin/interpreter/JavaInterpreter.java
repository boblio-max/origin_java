package im.manus.origin.interpreter;

import im.manus.origin.ast.*;
import java.util.*;
import java.util.stream.Collectors;

public class JavaInterpreter implements ASTNode.ASTVisitor<String> {

    private final Map<String, String> variableTypes = new HashMap<>();
    private final Set<String> constVars = new HashSet<>();

    public String translate(ASTNode node) {
        return node.accept(this);
    }

    @Override
    public String visit(ProgramNode node) {
        return node.statements.stream()
                .map(this::translate)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(BlockNode node) {
        return node.statements.stream()
                .map(this::translate)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(NumberNode node) {
        return node.value.toString();
    }

    @Override
    public String visit(StringNode node) {
        return "\"" + node.value + "\"";
    }

    @Override
    public String visit(BoolNode node) {
        return node.value ? "true" : "false";
    }

    @Override
    public String visit(NoneNode node) {
        return "null";
    }

    @Override
    public String visit(VarNode node) {
        return node.name;
    }

    @Override
    public String visit(AssignNode node) {
        if (constVars.contains(node.name)) {
            throw new RuntimeException("Cannot reassign constant '" + node.name + "'");
        }
        String val = translate(node.value);
        if (node.type != null) {
            variableTypes.put(node.name, node.type);
            return mapType(node.type) + " " + node.name + " = " + val + ";";
        }
        return node.name + " = " + val + ";";
    }

    @Override
    public String visit(ConstAssignNode node) {
        constVars.add(node.name);
        return "final var " + node.name + " = " + translate(node.value) + ";";
    }

    @Override
    public String visit(BinOpNode node) {
        String left = translate(node.left);
        String right = translate(node.right);
        if (node.op.equals("+")) {
            // Java handles string concatenation with + if one is a string
            return "(" + left + " + " + right + ")";
        }
        if (node.op.equals("**")) {
            return "Math.pow(" + left + ", " + right + ")";
        }
        if (node.op.equals("//")) {
            return "(int)(" + left + " / " + right + ")";
        }
        return "(" + left + " " + node.op + " " + right + ")";
    }

    @Override
    public String visit(UnaryOpNode node) {
        String op = node.op;
        if (op.equals("not")) op = "!";
        return "(" + op + translate(node.node) + ")";
    }

    @Override
    public String visit(LogicOpNode node) {
        String op = switch (node.op) {
            case "and", "&&" -> "&&";
            case "or", "||" -> "||";
            default -> node.op;
        };
        return "(" + translate(node.left) + " " + op + " " + translate(node.right) + ")";
    }

    @Override
    public String visit(IfNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(translate(node.condition)).append(") {\n");
        sb.append(indent(translate(node.thenBody))).append("\n}");
        for (var elif : node.elifNodes) {
            sb.append(" else if (").append(translate(elif.condition)).append(") {\n");
            sb.append(indent(translate(elif.body))).append("\n}");
        }
        if (node.elseBody != null) {
            sb.append(" else {\n");
            sb.append(indent(translate(node.elseBody))).append("\n}");
        }
        return sb.toString();
    }

    @Override
    public String visit(WhileNode node) {
        return "while (" + translate(node.condition) + ") {\n" +
                indent(translate(node.body)) + "\n}";
    }

    @Override
    public String visit(ForNode node) {
        // Simplified for-each
        return "for (var " + node.varName + " : " + translate(node.iterable) + ") {\n" +
                indent(translate(node.body)) + "\n}";
    }

    @Override
    public String visit(FuncNode node) {
        String params = node.params.stream()
                .map(p -> "Object " + p) // Generic Object for now
                .collect(Collectors.joining(", "));
        return "public static Object " + node.name + "(" + params + ") {\n" +
                indent(translate(node.body)) + "\n}";
    }

    @Override
    public String visit(ClassNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(node.name).append(" {\n");
        for (String field : node.fields) {
            sb.append("    public Object ").append(field).append(";\n");
        }
        sb.append("    public ").append(node.name).append("(");
        sb.append(node.fields.stream().map(f -> "Object " + f).collect(Collectors.joining(", ")));
        sb.append(") {\n");
        for (String field : node.fields) {
            sb.append("        this.").append(field).append(" = ").append(field).append(";\n");
        }
        sb.append("    }\n");
        sb.append(indent(translate(node.body)));
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String visit(CallNode node) {
        String args = node.args.stream()
                .map(this::translate)
                .collect(Collectors.joining(", "));
        return translate(node.callee) + "(" + args + ")";
    }

    @Override
    public String visit(PrintNode node) {
        return "System.out.println(" + translate(node.expr) + ");";
    }

    @Override
    public String visit(ListNode node) {
        String elements = node.elements.stream()
                .map(this::translate)
                .collect(Collectors.joining(", "));
        return "List.of(" + elements + ")";
    }

    @Override
    public String visit(DictNode node) {
        // Simplified Map.of (only works for up to 10 entries in Java 9+)
        return "Map.of(...)"; 
    }

    @Override
    public String visit(IndexNode node) {
        return translate(node.collection) + ".get(" + translate(node.index) + ")";
    }

    @Override
    public String visit(ReturnNode node) {
        return "return " + translate(node.value) + ";";
    }

    @Override
    public String visit(BreakNode node) { return "break;"; }

    @Override
    public String visit(ContinueNode node) { return "continue;"; }

    @Override
    public String visit(PassNode node) { return ""; }

    @Override
    public String visit(RangeNode node) {
        return "IntStream.range(" + translate(node.start) + ", " + translate(node.end) + ")";
    }

    @Override
    public String visit(LenNode node) {
        return translate(node.value) + ".size()"; // Assuming collection
    }

    @Override
    public String visit(SqrtNode node) {
        return "Math.sqrt(" + translate(node.value) + ")";
    }

    @Override
    public String visit(RandNumNode node) {
        return "new Random().nextInt(" + translate(node.start) + ", " + translate(node.end) + ")";
    }

    @Override
    public String visit(CastNode node) {
        String targetType = mapType(node.castType);
        return "((" + targetType + ")" + translate(node.value) + ")";
    }

    @Override
    public String visit(InputNode node) {
        return "new Scanner(System.in).nextLine()";
    }

    @Override
    public String visit(HardwarePrimitiveNode node) {
        // Hardware calls would need a Java library or JNI
        return "// Hardware call: " + node.namespace + "." + node.method;
    }

    @Override
    public String visit(SetNode node) {
        return "// Hardware set: " + node.name + "." + node.type;
    }

    @Override
    public String visit(ImportNode node) {
        return "// import " + node.name;
    }

    @Override
    public String visit(ImportAsNode node) {
        return "// import " + node.name + " as " + node.alias;
    }

    @Override
    public String visit(ImportFromNode node) {
        return "// from " + node.lib + " import " + node.name;
    }

    @Override
    public String visit(PyNode node) {
        return "/* Raw Python: " + node.code + " */";
    }

    @Override
    public String visit(ExecNode node) {
        return "/* Exec: " + node.code + " */";
    }

    private String mapType(String originType) {
        return switch (originType) {
            case "int" -> "int";
            case "float" -> "double";
            case "str" -> "String";
            case "bool" -> "boolean";
            default -> "Object";
        };
    }

    private String indent(String code) {
        return Arrays.stream(code.split("\n"))
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }
}

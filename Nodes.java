package im.manus.origin.ast;

import java.util.List;
import java.util.Map;

class ProgramNode extends ASTNode {
    public List<ASTNode> statements;
    public ProgramNode(List<ASTNode> statements) { this.statements = statements; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class BlockNode extends ASTNode {
    public List<ASTNode> statements;
    public BlockNode(List<ASTNode> statements) { this.statements = statements; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class NumberNode extends ASTNode {
    public Object value;
    public String type;
    public NumberNode(Object value, String type) { this.value = value; this.type = type; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class StringNode extends ASTNode {
    public String value;
    public StringNode(String value) { this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class BoolNode extends ASTNode {
    public boolean value;
    public BoolNode(boolean value) { this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class NoneNode extends ASTNode {
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class VarNode extends ASTNode {
    public String name;
    public VarNode(String name) { this.name = name; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class AssignNode extends ASTNode {
    public String name;
    public ASTNode value;
    public String type;
    public AssignNode(String name, ASTNode value, String type) { this.name = name; this.value = value; this.type = type; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ConstAssignNode extends ASTNode {
    public String name;
    public ASTNode value;
    public ConstAssignNode(String name, ASTNode value) { this.name = name; this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class BinOpNode extends ASTNode {
    public ASTNode left;
    public String op;
    public ASTNode right;
    public BinOpNode(ASTNode left, String op, ASTNode right) { this.left = left; this.op = op; this.right = right; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class UnaryOpNode extends ASTNode {
    public String op;
    public ASTNode node;
    public UnaryOpNode(String op, ASTNode node) { this.op = op; this.node = node; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class LogicOpNode extends ASTNode {
    public ASTNode left;
    public String op;
    public ASTNode right;
    public LogicOpNode(ASTNode left, String op, ASTNode right) { this.left = left; this.op = op; this.right = right; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class IfNode extends ASTNode {
    public ASTNode condition;
    public BlockNode thenBody;
    public List<ElifNode> elifNodes;
    public BlockNode elseBody;
    public IfNode(ASTNode condition, BlockNode thenBody, List<ElifNode> elifNodes, BlockNode elseBody) {
        this.condition = condition; this.thenBody = thenBody; this.elifNodes = elifNodes; this.elseBody = elseBody;
    }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ElifNode {
    public ASTNode condition;
    public BlockNode body;
    public ElifNode(ASTNode condition, BlockNode body) { this.condition = condition; this.body = body; }
}

class WhileNode extends ASTNode {
    public ASTNode condition;
    public BlockNode body;
    public WhileNode(ASTNode condition, BlockNode body) { this.condition = condition; this.body = body; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ForNode extends ASTNode {
    public String varName;
    public ASTNode iterable;
    public BlockNode body;
    public ForNode(String varName, ASTNode iterable, BlockNode body) { this.varName = varName; this.iterable = iterable; this.body = body; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class FuncNode extends ASTNode {
    public String name;
    public List<String> params;
    public BlockNode body;
    public FuncNode(String name, List<String> params, BlockNode body) { this.name = name; this.params = params; this.body = body; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ClassNode extends ASTNode {
    public String name;
    public List<String> fields;
    public BlockNode body;
    public ClassNode(String name, List<String> fields, BlockNode body) { this.name = name; this.fields = fields; this.body = body; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class CallNode extends ASTNode {
    public ASTNode callee;
    public List<ASTNode> args;
    public CallNode(ASTNode callee, List<ASTNode> args) { this.callee = callee; this.args = args; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class PrintNode extends ASTNode {
    public ASTNode expr;
    public PrintNode(ASTNode expr) { this.expr = expr; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ListNode extends ASTNode {
    public List<ASTNode> elements;
    public ListNode(List<ASTNode> elements) { this.elements = elements; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class DictNode extends ASTNode {
    public Map<ASTNode, ASTNode> elements;
    public DictNode(Map<ASTNode, ASTNode> elements) { this.elements = elements; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class IndexNode extends ASTNode {
    public ASTNode collection;
    public ASTNode index;
    public IndexNode(ASTNode collection, ASTNode index) { this.collection = collection; this.index = index; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ReturnNode extends ASTNode {
    public ASTNode value;
    public ReturnNode(ASTNode value) { this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class BreakNode extends ASTNode {
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ContinueNode extends ASTNode {
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class PassNode extends ASTNode {
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class RangeNode extends ASTNode {
    public ASTNode start;
    public ASTNode end;
    public RangeNode(ASTNode start, ASTNode end) { this.start = start; this.end = end; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class LenNode extends ASTNode {
    public ASTNode value;
    public LenNode(ASTNode value) { this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class SqrtNode extends ASTNode {
    public ASTNode value;
    public SqrtNode(ASTNode value) { this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class RandNumNode extends ASTNode {
    public ASTNode start;
    public ASTNode end;
    public RandNumNode(ASTNode start, ASTNode end) { this.start = start; this.end = end; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class CastNode extends ASTNode {
    public String castType;
    public ASTNode value;
    public CastNode(String castType, ASTNode value) { this.castType = castType; this.value = value; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class InputNode extends ASTNode {
    public ASTNode prompt;
    public InputNode(ASTNode prompt) { this.prompt = prompt; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class HardwarePrimitiveNode extends ASTNode {
    public String namespace;
    public String method;
    public List<ASTNode> args;
    public HardwarePrimitiveNode(String namespace, String method, List<ASTNode> args) {
        this.namespace = namespace; this.method = method; this.args = args;
    }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class SetNode extends ASTNode {
    public String name;
    public ASTNode num;
    public String type;
    public ASTNode params;
    public SetNode(String name, ASTNode num, String type, ASTNode params) {
        this.name = name; this.num = num; this.type = type; this.params = params;
    }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ImportNode extends ASTNode {
    public String name;
    public ImportNode(String name) { this.name = name; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ImportAsNode extends ASTNode {
    public String name;
    public String alias;
    public ImportAsNode(String name, String alias) { this.name = name; this.alias = alias; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ImportFromNode extends ASTNode {
    public String name;
    public String lib;
    public ImportFromNode(String name, String lib) { this.name = name; this.lib = lib; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class PyNode extends ASTNode {
    public String code;
    public PyNode(String code) { this.code = code; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

class ExecNode extends ASTNode {
    public String code;
    public ExecNode(String code) { this.code = code; }
    @Override public <R> R accept(ASTVisitor<R> visitor) { return visitor.visit(this); }
}

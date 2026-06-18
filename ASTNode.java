package im.manus.origin.ast;

import java.util.List;
import java.util.Map;

public abstract class ASTNode {
    public int line;

    public ASTNode() {}
    public ASTNode(int line) { this.line = line; }

    public abstract <R> R accept(ASTVisitor<R> visitor);

    public interface ASTVisitor<R> {
        R visit(ProgramNode node);
        R visit(BlockNode node);
        R visit(NumberNode node);
        R visit(StringNode node);
        R visit(BoolNode node);
        R visit(NoneNode node);
        R visit(VarNode node);
        R visit(AssignNode node);
        R visit(ConstAssignNode node);
        R visit(BinOpNode node);
        R visit(UnaryOpNode node);
        R visit(LogicOpNode node);
        R visit(IfNode node);
        R visit(WhileNode node);
        R visit(ForNode node);
        R visit(FuncNode node);
        R visit(ClassNode node);
        R visit(CallNode node);
        R visit(PrintNode node);
        R visit(ListNode node);
        R visit(DictNode node);
        R visit(IndexNode node);
        R visit(ReturnNode node);
        R visit(BreakNode node);
        R visit(ContinueNode node);
        R visit(PassNode node);
        R visit(RangeNode node);
        R visit(LenNode node);
        R visit(SqrtNode node);
        R visit(RandNumNode node);
        R visit(CastNode node);
        R visit(InputNode node);
        R visit(HardwarePrimitiveNode node);
        R visit(SetNode node);
        R visit(ImportNode node);
        R visit(ImportAsNode node);
        R visit(ImportFromNode node);
        R visit(PyNode node);
        R visit(ExecNode node);
    }
}

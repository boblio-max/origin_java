package origin.nodes;

import java.util.List;

public class ProgramNode extends ASTNode {
    public List<ASTNode> statements;
    public ProgramNode(List<ASTNode> statements) { this.statements = statements; }
    @Override public <R> R accept(ASTNode.ASTVisitor<R> visitor) { return visitor.visit(this); }
}

package compiler;

import compiler.AST.*;
import compiler.lib.BaseASTVisitor;
import compiler.lib.Node;

public class PrintASTVisitor extends BaseASTVisitor<Void>{

	PrintASTVisitor() { super(true); }	// Abilito la stampa

	public Void visitNode(ProgNode n) {
		printNode(n);
		visit(n.exp);
		return null;
	}

	public Void visitNode(PlusNode n) {
		printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	public Void visitNode(TimesNode n) {
		printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	public Void visitNode(IntNode n) {
		printNode(n, n.val.toString());
		return null;
	}

	public Void visitNode(EqualNode n) {
		printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	public Void visitNode(BoolNode n) {
		printNode(n, n.bool.toString());
		return null;
	}

	public Void visitNode(IfNode n) {
		printNode(n);
		visit(n.cond);
		visit(n.then_stat);
		visit(n.else_stat);
		return null;
	}

	public Void visitNode(PrintNode n) {
		printNode(n);
		visit(n.exp);
		return null;
	}
}



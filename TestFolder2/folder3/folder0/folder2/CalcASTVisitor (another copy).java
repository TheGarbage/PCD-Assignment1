package compiler;

import compiler.AST.*;
import compiler.lib.*;

import java.util.Objects;

public class CalcASTVisitor extends BaseASTVisitor<Integer> {


    CalcASTVisitor() {}
    CalcASTVisitor(boolean debug) { super(debug); } // enables print for debugging

	@Override
	public Integer visitNode(ProgNode n) {
		// print è preso dalla classe di base, è protected e quindi visibile
	   if (print) printNode(n);
	   return visit(n.exp);
	}

	@Override
	public Integer visitNode(PlusNode n) {
		if (print) printNode(n);
	    return visit(n.left) + visit(n.right);
	}

	@Override
	public Integer visitNode(TimesNode n) {
		if (print) printNode(n);
		return visit(n.left) * visit(n.right);
	}

	@Override
	public Integer visitNode(IntNode n) {
		if (print) printNode(n,": "+n.val);
        return n.val;
	}

	public Integer visitNode(BoolNode n) {
		if (print) printNode(n, ": " + n.bool);
		return n.bool ? 1 : 0;
	}

	public Integer visitNode(EqualNode n) {
		if (print) printNode(n);
		return Objects.equals(visit(n.left), visit(n.right)) ? 1 : 0;
	}

	public Integer visitNode(IfNode n) {
		if (print) printNode(n);
		return visit(n.cond) == 1 ? visit(n.then_stat) : visit(n.else_stat);
	}

	public Integer visitNode(PrintNode n) {
		if (print) printNode(n);
		return visit(n.exp);
	}

}


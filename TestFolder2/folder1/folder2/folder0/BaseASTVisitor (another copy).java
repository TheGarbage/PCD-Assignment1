package compiler.lib;

import compiler.AST.*;

public class BaseASTVisitor<S>{

	protected boolean print=false;
	String indent;

	protected BaseASTVisitor() {}
	protected BaseASTVisitor(boolean p) { print=p; }

	protected void printNode(Node n) {
		System.out.println(indent+extractNodeName(n.getClass().getName()));
	}

	protected void printNode(Node n, String s) {
		System.out.println(indent+extractNodeName(n.getClass().getName())+": "+s);
	}

	String extractNodeName(String s) { // s is in the form compiler.AST$NameNode
		return s.substring(s.lastIndexOf('$')+1,s.length()-4);
	}

	public S visit(Node n) {
		if(print) {
			String temp = indent;
			indent = (indent == null) ? "" : indent + "  ";
			S result = visitByAcc(n);
			indent = temp;
			return result;
		} else {
			return visitByAcc(n);	// Non faccio il codice dell'indentazione
		}
	}

	public S visitByAcc(Node n) {
		return n.accept(this); //performs the "n"-specific visit
	}
	
	public S visitNode(ProgNode n) { throw new UnimplException(); }
	public S visitNode(PlusNode n) { throw new UnimplException(); }
	public S visitNode(TimesNode n) { throw new UnimplException(); }
	public S visitNode(IntNode n) { throw new UnimplException(); }
	public S visitNode(EqualNode n) { throw new UnimplException(); }
	public S visitNode(BoolNode n) { throw new UnimplException(); }
	public S visitNode(IfNode n) { throw new UnimplException(); }
	public S visitNode(PrintNode n) { throw new UnimplException(); }
}

//throw new UnimplException();
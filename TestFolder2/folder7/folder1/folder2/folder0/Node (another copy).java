package compiler.lib;

import compiler.PrintASTVisitor;

public interface Node {
    <S> S accept(BaseASTVisitor<S> visitor);
}












//    void accept(PrintASTVisitor visitor);
	

	  
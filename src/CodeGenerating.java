import java.io.*;
/*
 *  This Visitor class generates JVM assembler code (using Jasmin's format)
 *  for CSX lite in the Printstream afile. You'll need to extend it to
 *  handle all of CSX. Note that for some AST nodes (like asgNode) code generation
 *  for CSX is more complex than that needed for CSX lite.
 *  All methods marked TODO will have to be completed by you (for full CSX)
 */

import com.sun.org.apache.bcel.internal.generic.StoreInstruction;

public class CodeGenerating extends Visitor {

	PrintStream afile; // File to generate JVM code into

	int cgErrors = 0; // Total number of code generation errors

	int numberOfLocals = 0; // Total number of local CSX-lite vars

	int labelCnt = 0; // counter used to generate unique labels

	methodDeclNode currentMethod;

	String CLASS;

	CodeGenerating(PrintStream f) {
		afile = f;
	}

	public enum AdrModes {
		global, local, stack, literal, none
	}

	static void assertCondition(boolean assertion) {
		if (!assertion)
			throw new RuntimeException();
	}

	String error(ASTNode n) {
		return "Error (line " + n.linenum + "): ";
	}

	// generate a comment
	void genComment(String text) {
		gen("; " + text);
	}

	// generate an instruction w/ 0 operands
	void gen(String opcode) {
		afile.println("\t" + opcode);
	}

	// generate an instruction w/ 1 operand
	void gen(String opcode, String operand) {
		afile.println("\t" + opcode + "\t" + operand);
	}

	// generate an instruction w/ 1 integer operand
	void gen(String opcode, int operand) {
		afile.println("\t" + opcode + "\t" + operand);
	}

	// generate an instruction w/ 2 operands
	void gen(String opcode, String operand1, String operand2) {
		afile.println("\t" + opcode + "\t" + operand1 + "  " + operand2);
	}

	// generate an instruction w/ 2 operands (String and int)
	void gen(String opcode, String operand1, int operand2) {
		afile.println("\t" + opcode + "\t" + operand1 + "  " + operand2);
	}

	// Generate a new label of form labeln (e.g., label7 or label123)
	String genLab() {
		return "label" + labelCnt++;
	}

	// Place a label in generated code
	void defineLab(String label) {
		afile.println(label + ":");
	}

	void branch(String label) {
		gen("goto", label);
	}

	void loadI(int val) {
		gen("ldc", val);
	}

	void loadGlobalInt(String name) {
		gen("genstatic", CLASS + "/" + name + " I");
	}

	void loadLocalInt(int index) {
		gen("iload", index);
	}

	void storeGlobalInt(String name) {
		gen("putstatic", CLASS + "/" + name);
	}

	void storeLocalInt(int index) {
		gen("istore", index);
	}

	void binOp(String op) {
		gen(op);
	}

	void computeAdr(nameNode name) {
		// Compute address associated w/ name node
		// donÕt load the value addressed onto the stack
		if (name.subscriptVal.isNull()) {
			// Simple (unsubscripted) identifier
			if (name.varName.idinfo.kind == ASTNode.Kinds.Var) {
				// id is a scalar variable
				if (name.varName.idinfo.adr == AdrModes.global) {
					name.adr = AdrModes.global;
					name.label = name.varName.idinfo.label;
				} else { // varName.idinfo.adr == local
					name.adr = AdrModes.local;
					name.intval = name.varName.idinfo.varIndex;
				}
			} else {// Handle arrays later
			}
		} else {
		} // Handle subscripted variables later
	}

	void storeId(identNode id) {
		if (id.idinfo.kind == ASTNode.Kinds.Var
				|| id.idinfo.kind == ASTNode.Kinds.Value) {
			if (id.idinfo.adr == AdrModes.global) {
				storeGlobalInt(id.idinfo.label);
			} else {
				storeLocalInt(id.idinfo.varIndex);
			}
		}
	}

	void storeName(nameNode name) {
		if (name.subscriptVal.isNull()) {
			// Simple (unsubscripted) identifier
			if (name.varName.idinfo.kind == ASTNode.Kinds.Var) {
				if (name.adr == AdrModes.global)
					storeGlobalInt(name.label);
				else
					// (name.adr == local)
					storeLocalInt(name.intval);
			} else {
			} // Handle arrays later
		} else {
		} // Handle subscripted variables later
	}

	boolean isNumericLit(exprNodeOption e) {
		return (e instanceof intLitNode) || (e instanceof charLitNode)
				|| (e instanceof trueNode) || (e instanceof falseNode);
	}

	int getLitValue(exprNode e) {
		if (e instanceof intLitNode)
			return e.intval;
		else if (e instanceof charLitNode)
			return ((charLitNode) e).charval;
		else if (e instanceof trueNode)
			return 1;
		else
			return 0;
	}

	void declGlobalInt(String name, exprNodeOption initValue) {
		if (isNumericLit(initValue)) {
			int numValue = getLitValue((exprNode) initValue);
			// Generate a field declaration with initial value:
			gen(".field", "public static", name + " I = " + numValue);
		} else {
			// Gen a field declaration without an initial value:
			gen(".field", "public static", name + " I");
		}
	}

	String arrayTypeCode(typeNode type) {
		// Return array type code
		if (type instanceof intTypeNode)
			return "[I";
		else if (type instanceof charTypeNode)
			return "[C";
		else
			// (type instanceof boolTypeNode)
			return "[Z";
	}

	void declGlobalArray(String name, typeNode type) {
		// Generate a field declaration for an array:
		// .field public static name arrayTypeCode(type)
		gen(".field", "public static", name + arrayTypeCode(type));
	}

	void allocateArray(typeNode type) {
		if (type instanceof intTypeNode) {
			// Generate a newarray instruction for an integer array:
			// newarray int
			gen("newarray", "int");
		} else if (type instanceof charTypeNode) {

			// Gen a newarray instruction for a character array:
			// newarray char
			gen("newarray", "char");
		} else {// (type instanceof boolTypeNode) {
			// Gen a newarray instruction for a boolean array:
			// newarray boolean
			gen("newarray", "boolean");
		}
	}

	void storeGlobalReference(String name, String typeCode) {
		// Generate a store of a reference from the stack into
		// a static field:
		// putstatic CLASS/name typeCode
		gen("putstatic", CLASS + "/" + name, typeCode);
	}

	void storeLocalReference(int index) {
		// Generate a store of a reference from the stack into
		// a local variable:
		// astore index
		gen("astore", index);
	}

	void declField(varDeclNode n) {
		String varLabel = n.varName.idname + "$";
		declGlobalInt(varLabel, n.initValue);
		n.varName.idinfo.label = varLabel;
		n.varName.idinfo.adr = AdrModes.global;
	}

	void declField(constDeclNode n) {
		String constLabel = n.constName.idname + "$";
		declGlobalInt(constLabel, n.constValue);
		n.constName.idinfo.label = constLabel;
		n.constName.idinfo.adr = AdrModes.global;
	}

	void declField(arrayDeclNode n) {
		String arrayLabel = n.arrayName.idname + "$";
		declGlobalArray(arrayLabel, n.elementType);
		n.arrayName.idinfo.label = arrayLabel;
		n.arrayName.idinfo.adr = AdrModes.global;
	}
	
	void branchRelationalCompare(int tokenCode, String label){ 
		// Generate a conditional branch to label based on tokenCode: 
		// Generate: 
		// "if_icmp"+relationCode(tokenCode) label 
		//maybe?
		gen("if_icmp" + relationCode(tokenCode) + " " + label);
	 } 
	
	void genRelationalOp(int operatorCode){ 
		 // Generate code to evaluate a relational operator 
		 String trueLab = genLab(); 
		 String skip = genLab(); 
		 branchRelationalCompare(operatorCode, trueLab); 
		 loadI(0); // Push false 
		 branch(skip); 
		 defineLab(trueLab); 
		 loadI(1); // Push true 
		 defineLab(skip); 
	 }

	static Boolean isRelationalOp(int op) {
		switch (op) {
		case sym.EQ:
		case sym.NOTEQ:
		case sym.LT:
		case sym.LEQ:
		case sym.GT:
		case sym.GEQ:
			return true;
		default:
			return false;
		}
	}

	static String relationCode(int op) {
		switch (op) {
		case sym.EQ:
			return "eq";
		case sym.NOTEQ:
			return "ne";
		case sym.LT:
			return "lt";
		case sym.LEQ:
			return "le";
		case sym.GT:
			return "gt";
		case sym.GEQ:
			return "ge";
		default:
			return "";
		}
	}

	static String selectOpCode(int op) {
		switch (op) {
		case sym.PLUS:
			return ("iadd");
		case sym.MINUS:
			return ("isub");
		case sym.TIMES:
			return ("imul");
		case sym.SLASH:
			return ("idiv");
		case sym.CAND:
			return ("iand");
		case sym.COR:
			return ("ior");
		default:
			assertCondition(false);
			return "";
		}
	}

	// startCodeGen translates the AST rooted by node n
	// into JVM code which is written in afile.
	// If no errors occur during code generation,
	// TRUE is returned, and afile should contain a
	// complete and correct JVM program.
	// Otherwise, FALSE is returned and afile need not
	// contain a valid program.

	boolean startCodeGen(csxLiteNode n) {// For CSX Lite
		this.visit(n);
		return (cgErrors == 0);
	}

	boolean startCodeGen(classNode n) {// For CSX
		this.visit(n);
		return (cgErrors == 0);
	}

	void visit(csxLiteNode n) {
		genComment("CSX Lite program translated into Java bytecodes (Jasmin format)");
		gen(".class", "public", "test");
		gen(".super", "java/lang/Object");
		gen(".method", " public static", "main([Ljava/lang/String;)V");
		this.visit(n.progDecls);
		if (numberOfLocals > 0)
			gen(".limit", "locals", numberOfLocals);
		this.visit(n.progStmts);
		gen("return");
		gen(".limit", "stack", 10);
		gen(".end", "method");
	}

	void visit(fieldDeclsNode n) {
		this.visit(n.thisField);
		this.visit(n.moreFields);
	}

	void visit(nullFieldDeclsNode n) {
	}

	void visit(stmtsNode n) {
		// System.out.println ("In stmtsNode\n");
		this.visit(n.thisStmt);
		this.visit(n.moreStmts);

	}

	void visit(nullStmtsNode n) {
	}

	void visit(varDeclNode n) {
		// Give this variable an index equal to numberOfLocals (initially 0)
		// and remember index in symbol table entry

		// n.varName.idinfo.varIndex = numberOfLocals;

		// Increment numberOfLocals used in this prog

		// numberOfLocals++;

		if (currentMethod == null) { // A global field decl
			if (n.varName.idinfo.adr == AdrModes.none) {
				// First pass; generate field declarations
				declField(n);
			} else { // 2nd pass; do field initialization (if needed)
				if (!n.initValue.isNull()) {
					if (!isNumericLit(n.initValue)) {
						// Compute init val onto stack; store in field
						this.visit(n.initValue);
						storeId(n.varName);
					}
				} else {
					// Handle local variable declarations later
				}
			}
		}
	}

	void visit(nullTypeNode n) {
	}

	void visit(intTypeNode n) {
		// No code generation needed
	}

	void visit(boolTypeNode n) {
		// No code generation needed
	}

	void visit(charTypeNode n) {
		// No code generation needed
	}

	void visit(voidTypeNode n) {
		// No code generation needed
	}

	void visit(asgNode n) {
		// Translate RHS (an expression)
		// this.visit(n.source);

		// Value to be stored is now on the stack
		// Save it into target variable, using the variable's index
		// gen("istore", n.target.varName.idinfo.varIndex);

		computeAdr(n.target);
		this.visit(n.source);
		storeName(n.target);
	}

	void visit(ifThenNode n) { // No else statement in CSX lite
		String endLab; // label that will mark end of if stmt 
		String elseLab; // label that will mark start of else part
		// translate boolean condition, pushing it onto the stack
		this.visit(n.condition);
		elseLab = genLab();
		// generate conditional branch around then stmt
		branchZ(elseLab);
		// translate then part
		this.visit(n.thenPart);
		// branch around else part
		endLab = genLab();
		branch(endLab);
		// translate else part
		defineLab(elseLab);
		this.visit(n.elsePart);
		// generate label marking end of if stmt
		defineLab(endLab);

	}

	void visit(printNode n) {
		// compute value to be printed onto the stack
		this.visit(n.outputValue);

		// Call CSX library routine "printInt(int i)"
		if (n.outputValue.type == ASTNode.Types.Integer) {
			gen("invokestatic", " CSXLib/printInt(I)V");
		} else if (n.outputValue.type == ASTNode.Types.Character) {
			gen("invokestatic", " CSXLib/printChar(C)V");
		} else if (n.outputValue.type == ASTNode.Types.Boolean) {
			gen("invokestatic", " CSXLib/printBool(B)V");
		} else if (n.outputValue.kind == ASTNode.Kinds.String) {
			gen("invokestatic", " CSXLib/printString(LJava/lang/String;)V");
		} else if (n.outputValue.kind == ASTNode.Kinds.Array
				|| n.outputValue.kind == ASTNode.Kinds.ArrayParm) {
			gen("invokestatic", " CSXLib/printCharArray([C)V");
		}
		this.visit(n.morePrints);
	}

	void visit(nullPrintNode n) {
	}

	void visit(blockNode n) {
		this.visit(n.decls);
		this.visit(n.stmts);
	}

	void visit(binaryOpNode n) {
		// First translate the left and right operands 
		 this.visit(n.leftOperand); 
		 this.visit(n.rightOperand); 
		 // Now the values of the operands are on the stack 
		 // Is this a relational operator? 
		 if (relationCode(n.operatorCode) == ""){ 
		 gen(selectOpCode(n.operatorCode)); 
		 } else { // relational operator 
		 genRelationalOp(n.operatorCode); 
		 } 
		 n.adr = AdrModes.stack; 
	}

	void visit(identNode n) {
		// In CSX-lite, we don't code generate identNode directly.
		// Instead, we do translation in parent nodes where the
		// context of identNode is known
		// Hence no code generation actions are defined here
		// (though you may want/need to define some in full CSX)

	}

	void visit(intLitNode n) {
		loadI(n.intval);
		n.adr = AdrModes.literal;
	}

	void visit(nameNode n) {
		// In CSX lite no arrays exist and all variable names are local
		// variables

		// Load value of this variable onto stack using its index
		gen("iload", n.varName.idinfo.varIndex);

		if (n.subscriptVal.isNull()) {
			// Simple (unsubscripted) identifier
			if (n.varName.idinfo.kind == ASTNode.Kinds.Var
					|| n.varName.idinfo.kind == ASTNode.Kinds.Value) {
				// id is a scalar variable or const
				if (n.varName.idinfo.adr == AdrModes.global) {
					// id is a global
					String label = n.varName.idinfo.label;
					loadGlobalInt(label);
				} else { // (n.varName.idinfo.adr == Local)
					n.intval = n.varName.idinfo.varIndex;
					loadLocalInt(n.intval);
				}
			} else
				// Handle arrays later
				n.adr = AdrModes.stack;
		} else {
		} // Handle subscripted variables later
	}

	void visit(classNode n) {
		currentMethod = null;
		CLASS = n.className.idname;

		gen(".class", "public", CLASS);
		gen(".super", "java/lang/object");

		this.visit(n.members.fields);

		gen(".method", "public static", "main([Ljava/lang/String;)V");

		this.visit(n.members.fields);

		gen("invokestatic", CLASS + "/main()V");
		gen("return");
		gen(".limit", "stack", 2);
		gen(".end", "method");

		this.visit(n.members.methods);

	}

	void visit(memberDeclsNode n) {
		// TODO Auto-generated method stub

	}

	void visit(valArgDeclNode n) {
		// TODO Auto-generated method stub

	}

	void visit(arrayArgDeclNode n) {
		// TODO Auto-generated method stub

	}

	void visit(argDeclsNode n) {
		// TODO Auto-generated method stub

	}

	void visit(nullArgDeclsNode n) {
	}

	void visit(methodDeclsNode n) {
		// TODO Auto-generated method stub

	}

	void visit(nullMethodDeclsNode n) {
	}

	void visit(methodDeclNode n) {
		// TODO Auto-generated method stub

	}

	void visit(trueNode n) {
		loadI(1);
		n.adr = AdrModes.literal;
		n.intval = 1;
	}

	void visit(falseNode n) {
		loadI(0);
		n.adr = AdrModes.literal;
		n.intval = 0;

	}

	void visit(constDeclNode n) {
		if (currentMethod == null) { // A global const decl
			if (n.constName.idinfo.adr == AdrModes.none) {
				// First pass; generate field declarations
				declField(n);
			} else { // 2nd pass; do field initialization (if needed)
				if (!isNumericLit(n.constValue)) {
					// Compute const val onto stack and store in field
					this.visit(n.constValue);
					storeId(n.constName);
				}
			}
		} else {// Handle local const declarations later}

		}
	}

	void visit(arrayDeclNode n) {
		if (currentMethod == null) {
			// A global array decl
			if (n.arrayName.idinfo.adr == AdrModes.none) {
				// First pass; generate field declarations
				declField(n);
				return;
			}
		} else {
			// Handle local array declaration later
		}

		// Now create the array & store a reference to it
		loadI(n.arraySize.intval); // Push number of array elements
		allocateArray(n.elementType);
		if (n.arrayName.idinfo.adr == AdrModes.global)
			storeGlobalReference(n.arrayName.idinfo.label,
					arrayTypeCode(n.elementType));
		else
			storeLocalReference(n.arrayName.idinfo.varIndex);

	}

	void visit(readNode n) {
		if (n.targetVar.type == ASTNode.Types.Integer) {
			gen("invokestatic", " CSXLib/readInt(I)V");
		} else if (n.targetVar.type == ASTNode.Types.Character) {
			gen("invokestatic", " CSXLib/readChar(C)V");
		}

		gen("invokestatic", " CSXLib/storeName(" + n.targetVar + ")V");

		this.visit(n.moreReads);
	}

	void visit(nullReadNode n) {
	}

	void visit(charLitNode n) {
		loadI(n.charval);
		n.adr = AdrModes.literal;
		n.intval = n.charval;
	}

	void visit(strLitNode n) {
		// TODO Auto-generated method stub

	}

	void visit(argsNode n) {
		this.visit(n.argVal);
		this.visit(n.moreArgs);
	}

	void visit(nullArgsNode n) {
	}

	void visit(unaryOpNode n) {
		// TODO Auto-generated method stub

	}

	void visit(nullStmtNode n) {
	}

	void visit(nullExprNode n) {
	}

	void visit(whileNode n) {
		// TODO Auto-generated method stub

	}

	void visit(callNode n) {
		// TODO Auto-generated method stub

	}

	void visit(fctCallNode n) {
		// TODO Auto-generated method stub

	}

	void visit(returnNode n) {
		// TODO Auto-generated method stub

	}

	void visit(breakNode n) {
		branch(n.label.idinfo.bottomLabel); 
	}

	void visit(continueNode n) {
		branch(n.label.idinfo.topLabel); 
	}

	void visit(castNode n) {
		this.visit(n.operand);
		// Is the operand an int or char and the resultType a bool?
		if (((n.operand.type == ASTNode.Types.Integer) || (n.operand.type == ASTNode.Types.Character))
				&& (n.resultType instanceof boolTypeNode)) {
			loadI(0);
			genRelationalOp(sym.NOTEQ);
		} else if ((n.operand.type == ASTNode.Types.Integer)
				&& (n.resultType instanceof charTypeNode)) {
			loadI(127); // Equal to 1111111B
			gen("iand");
		}
	}

	void visit(incrementNode n) {
		// TODO Auto-generated method stub
	}

	void visit(decrementNode n) {
		// TODO Auto-generated method stub
	}

}

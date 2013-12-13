import java.util.ArrayList;

import javax.swing.LookAndFeel;

// The following methods type check  AST nodes used in CSX Lite
//  You will need to complete the methods after line 238 to type check the
//   rest of CSX
//  Note that the type checking done for CSX lite may need to be extended to
//   handle full CSX (for example binaryOpNode).

public class TypeChecking extends Visitor {

	int typeErrors = 0; // Total number of type errors found
	SymbolTable st;
	boolean mainDeclared = false;
	methodDeclNode currentMethod;

	TypeChecking() {
		typeErrors = 0;
		st = new SymbolTable();
	}

	boolean isTypeCorrect(csxLiteNode n) {
		this.visit(n);
		return (typeErrors == 0);
	}

	boolean isTypeCorrect(classNode n) {
		this.visit(n);
		return (typeErrors == 0);
	}

	static void assertCondition(boolean assertion) {
		if (!assertion)
			throw new RuntimeException();
	}

	void typeMustBe(ASTNode.Types testType, ASTNode.Types requiredType,
			String errorMsg) {
		if ((testType != ASTNode.Types.Error) && (testType != requiredType)) {
			System.out.println(errorMsg);
			typeErrors++;
		}
	}
	
	void typeMustBe(ASTNode.Types testType, ASTNode.Types requiredType1, ASTNode.Types requiredType2,
			String errorMsg) {
		if ((testType != ASTNode.Types.Error) && (testType != requiredType1) && (testType != requiredType2)) {
			System.out.println(errorMsg);
			typeErrors++;
		}
	}
	
	void typeBinaryOpCheck(ASTNode.Types testType1, ASTNode.Types testType2, String opCode){
		if(testType1 == ASTNode.Types.Error || testType2 == ASTNode.Types.Error){
			if(((testType1 != testType2) || (testType1 != ASTNode.Types.Character) ||  
					(testType1 != ASTNode.Types.Integer) || (testType1 != ASTNode.Types.Boolean))){
				typeErrors++;
					System.out.println("Operands of " + opCode + " must be both either arithmetic or boolean");
			}
			
		}
	}

	void typesMustBeEqual(ASTNode.Types type1, ASTNode.Types type2,
			String errorMsg) {
		if ((type1 != ASTNode.Types.Error) && (type2 != ASTNode.Types.Error)
				&& (type1 != type2)) {
			System.out.println(errorMsg);
			typeErrors++;
		}
	}

	String error(ASTNode n) {
		return "Error (line " + n.linenum + "): ";
	}

	static String opToString(int op) {
		switch (op) {
		case sym.PLUS:
			return (" + ");
		case sym.MINUS:
			return (" - ");
		case sym.EQ:
			return (" == ");
		case sym.NOTEQ:
			return (" != ");
		case sym.SLASH:
			return (" / ");
		case sym.LT:
			return (" < ");
		case sym.GEQ:
			return (" >= ");
		case sym.COR:
			return (" || ");
		case sym.TIMES:
			return (" * ");
		case sym.CAND:
			return (" && ");
		case sym.LEQ:
			return (" <= ");
		case sym.GT:
			return (" > ");
		default:
			assertCondition(false);
			return "";
		}
	}

	static void printOp(int op) {
		switch (op) {
		case sym.PLUS:
			System.out.print(" + ");
			break;
		case sym.MINUS:
			System.out.print(" - ");
			break;
		case sym.EQ:
			System.out.print(" == ");
			break;
		case sym.NOTEQ:
			System.out.print(" != ");
			break;
		case sym.SLASH:
			System.out.print(" / ");
			return;
		case sym.LT:
			System.out.print(" < ");
			return;
		case sym.GEQ:
			System.out.print(" >= ");
			return;
		case sym.COR:
			System.out.print(" || ");
			return;
		case sym.TIMES:
			System.out.print(" * ");
			return;
		case sym.CAND:
			System.out.print(" && ");
			return;
		case sym.LEQ:
			System.out.print(" <= ");
			return;
		case sym.GT:
			System.out.print(" > ");
			return;
		default:
			throw new Error();
		}
	}
	
	//checks to see if the variable is scalar
	boolean isScalar(ASTNode.Kinds var){
		boolean isScalar = false;
		if(var == ASTNode.Kinds.Var || var == ASTNode.Kinds.Value || var == ASTNode.Kinds.ScalarParm){
			isScalar = true;
		}
		
		return isScalar;
	}

	void visit(csxLiteNode n) {
		this.visit(n.progDecls);
		this.visit(n.progStmts);
	}

	void visit(fieldDeclsNode n) {
		this.visit(n.thisField);
		this.visit(n.moreFields);
	}

	void visit(nullFieldDeclsNode n) {
	}

	void visit(stmtsNode n) {
		this.visit(n.thisStmt);
		this.visit(n.moreStmts);

	}

	void visit(nullStmtsNode n) {
	}

	void visit(varDeclNode n) {

		SymbolInfo id = (SymbolInfo) st.localLookup(n.varName.idname);
		if (id != null) {
			typeErrors++;
			System.out.println(error(n) + id.name() + " is already declared.");
			n.varName.type = ASTNode.Types.Error;

		} else {
			//variable is getting initialized
			if(!n.initValue.isNull()){
				this.visit(n.initValue);
				typesMustBeEqual(n.varType.type, ((exprNode)n.initValue).type, error(n) + "Initilizer must be a " + n.varType.type);
				
				//check to make sure value is scalar
				try{
					assertCondition(isScalar(((exprNode)n.initValue).kind));
				}catch(RuntimeException ex){
					System.out.println(error(n) + "initial var value must be scalar");
				}
				
			}
			
			//add var name to symbol table
			id = new SymbolInfo(n.varName.idname, ASTNode.Kinds.Var, n.varType.type);
			n.varName.type = n.varType.type;
			try {
				st.insert(id);
			} catch (DuplicateException ex) {
				/* don't have to worry about this case */
			} catch (EmptySTException ex) {
				/* don't have to worry about this case */
			}
			n.varName.idinfo = id;
			
			
			
		}

	};

	void visit(nullTypeNode n) {
		//No type checking needed!!!!
	}

	void visit(intTypeNode n) {
		// no type checking needed!!!!
	}

	void visit(boolTypeNode n) {
		// no type checking needed!!
	}

	void visit(identNode n) {
		SymbolInfo id;
		id = (SymbolInfo) st.globalLookup(n.idname); //Checks if the node is in the symbol table
		if (id == null) { // If correct, it should always be in teh table
			typeErrors++;
			System.out.println(error(n) + n.idname + " is not declared.");
			n.type = ASTNode.Types.Error;
		} else {
			n.type = id.type; //Since it's in the table, get the node details
			n.kind = id.kind;
			n.idinfo = id; 
		}
	}

	void visit(nameNode n) {
		this.visit(n.varName); 
		
		if(n.subscriptVal.isNull()){
			n.type = n.varName.type;
			n.kind = n.varName.kind;
			return;
		}
		
		this.visit(n.subscriptVal);
		
		try{
			assertCondition(n.varName.kind == ASTNode.Kinds.Array);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "subscript value can only be Arrays");
			return;
		}
		
		try{
			exprNode subscript = (exprNode)n.subscriptVal;
			assertCondition(isScalar(subscript.kind));
			assertCondition(subscript.type == ASTNode.Types.Integer || subscript.type == ASTNode.Types.Character);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "subsript value can only be int or char");
		}
		
		n.type = n.varName.type;
	}

	void visit(asgNode n) {
		this.visit(n.target);
		this.visit(n.source);
		//Must be a type of variable that can be assigned.
		try{
			assertCondition(n.target.kind == ASTNode.Kinds.Var || n.target.kind == ASTNode.Kinds.Array
					|| n.target.kind == ASTNode.Kinds.ScalarParm || n.target.kind == ASTNode.Kinds.ArrayParm);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Target has to be assignable");
		}
		
		if(isScalar(n.target.kind)){
			try{
				assertCondition(n.target.kind != ASTNode.Kinds.Value); //Can't assign a value to a value but you can assign it to a var
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + "Cannot assign to target becasue it is constant");
			}
			
			typesMustBeEqual(n.source.type, n.target.type, error(n)
					+ "Both the left and right"
					+ " hand sides of an assignment must " + "have the same type.");

			return;
		}
		if(n.target.kind == ASTNode.Kinds.Array && n.target.type == ASTNode.Types.Character 
				&& n.source.kind == ASTNode.Kinds.String){
			SymbolInfo id = (SymbolInfo)st.globalLookup(n.target.varName.idname);
			String str = ((strLitNode)n.source).strval;
			try{
				String editedString = str.replace("\\", "1").replace("\\n", "1").replace("\\t", "1").replace("\\'", "1");
				assertCondition(id.arraySize == editedString.length());
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + "Array and String must have the same length");
			}
			
			
			return;
		}
		System.out.println(error(n) + "wrong assignment.");

	}
	
	void visit(ifThenNode n) {
		this.visit(n.condition); 
		typeMustBe(n.condition.type, ASTNode.Types.Boolean, error(n) // Must have a true or false return type
				+ "The control expression of an" + " if must be a bool.");

		this.visit(n.thenPart);
		this.visit(n.elsePart);
	}

	void visit(printNode n) {
		this.visit(n.outputValue);
		try{ //Must have some type that allows printing.
			assertCondition(n.outputValue.type == ASTNode.Types.Integer ||
					n.outputValue.type == ASTNode.Types.Boolean ||
					n.outputValue.type == ASTNode.Types.Character ||
					(n.outputValue.type == ASTNode.Types.Character && n.outputValue.kind == ASTNode.Kinds.Array)||
					n.outputValue.kind == ASTNode.Kinds.String);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Can only print int, bool, char, char array, and string");
		}
		this.visit(n.morePrints);
	}

	void visit(blockNode n) {
		// open a new local scope for the block body
		st.openScope();
		this.visit(n.decls);
		this.visit(n.stmts);
		// close this block's local scope
		try {
			st.closeScope();
		} catch (EmptySTException e) { /* can't happen */
		}
	}

	void visit(binaryOpNode n) {

		assertCondition(n.operatorCode == sym.PLUS
				|| n.operatorCode == sym.MINUS || n.operatorCode == sym.EQ
				|| n.operatorCode == sym.NOTEQ || n.operatorCode == sym.SLASH ||
				n.operatorCode == sym.LT || n.operatorCode == sym.GEQ ||
				n.operatorCode == sym.COR || n.operatorCode == sym.TIMES ||
				n.operatorCode == sym.TIMES || n.operatorCode == sym.CAND ||
				n.operatorCode == sym.LEQ || n.operatorCode == sym.GT);
		
		n.kind = ASTNode.Kinds.Value;
		
		this.visit(n.leftOperand);
		this.visit(n.rightOperand);
		
		try{
			assertCondition(isScalar(n.leftOperand.kind));
			assertCondition(isScalar(n.rightOperand.kind));
			
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "When using a "+ opToString(n.operatorCode) +" the operands must be scalar");
		}
		
		if (n.operatorCode == sym.PLUS || n.operatorCode == sym.MINUS || n.operatorCode == sym.SLASH || n.operatorCode == sym.TIMES) {
			n.type = ASTNode.Types.Integer;
			
			typeMustBe(n.leftOperand.type, ASTNode.Types.Integer, ASTNode.Types.Character, error(n)
					+ "Left operand of" + opToString(n.operatorCode)
					+ "must be arthimetic.");
			typeMustBe(n.rightOperand.type, ASTNode.Types.Integer, ASTNode.Types.Character, error(n)
					+ "Right operand of" + opToString(n.operatorCode)
					+ "must be arthimetic.");
			
		} else if(n.operatorCode == sym.COR || n.operatorCode == sym.CAND){ 
			n.type = ASTNode.Types.Boolean;
			String errorMsg = error(n) + "Both operands of"
					+ opToString(n.operatorCode) + "must have the same type.";
			typesMustBeEqual(n.leftOperand.type, n.rightOperand.type, errorMsg);
		}else{
			//relational operator
			
			n.type = ASTNode.Types.Boolean;
			typeBinaryOpCheck(n.leftOperand.type, n.rightOperand.type, opToString(n.operatorCode));
		}
	}

	void visit(intLitNode n) {
		n.type = ASTNode.Types.Integer;
		n.kind = ASTNode.Kinds.Var;
	}

	void visit(classNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.className.idname);
		if (id != null) {
			typeErrors++;
			System.out.println(error(n) + id.name()
					+ " is already declared.");
			n.className.type = ASTNode.Types.Error;
		} else {
			id = new SymbolInfo(n.className.idname, ASTNode.Kinds.Var,
					n.className.type);

			try {
				this.st.insert(id);
				this.st.openScope();
				this.visit(n.members);
				this.st.closeScope();
			} catch (DuplicateException ex) {
				/* can't happen */
			} catch (EmptySTException ex) {
				/* can't happen */
			}
		}
	}

	void visit(memberDeclsNode n) {
		this.visit(n.fields);
		this.visit(n.methods);

	}

	void visit(methodDeclsNode n) {
		this.visit(n.thisDecl);
		this.visit(n.moreDecls);
	}

	//Null statements don't need to be handled
	void visit(nullStmtNode n) {
	}

	void visit(nullReadNode n) {// Null doesn't matter
	}

	void visit(nullPrintNode n) { //Null doesn't matter
	}

	void visit(nullExprNode n) {
	}

	void visit(nullMethodDeclsNode n) {
	}

	void visit(methodDeclNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.name.idname);
		
		if(this.mainDeclared){ //Main method is declared last
			typeErrors++;
			System.out.println(error(n) + "a method cannot be declared after main");
			n.name.type = ASTNode.Types.Error;
		}
		
		if(n.name.idname.equals("main")){ //This is the main, make sure that there's no other method calls
			this.mainDeclared = true;
			if(n.returnType.type != ASTNode.Types.Void){
				typeErrors++;
				System.out.println(error(n) + "main method must be of void type");
				n.name.type = ASTNode.Types.Error;
			}
			if(!n.args.isNull()){ //Main can't have any arguements
				typeErrors++;
				System.out.println(error(n) + "main method cannot have any arguments");
				n.name.type = ASTNode.Types.Error;
			}
		}
		
		if(id == null){ //If it's null, then you create a new node.
			id = new SymbolInfo(n.name.idname, ASTNode.Kinds.Method, n.returnType.type);
			
			try{
				this.st.insert(id);
			}catch(DuplicateException ex){
				/* can't happen*/
			}catch(EmptySTException ex){
				/* can't happen*/
			}
			
			this.st.openScope();
			
			this.visit(n.args);
			id.methodParams.add(this.st.top.methodParams);
			currentMethod = n;
			this.visit(n.decls);//Visit all statements and decls nodes!
			this.visit(n.stmts);
			
			
			try{
				this.st.closeScope();
			}catch(EmptySTException ex){
				//can't happen
			}
			
			n.name.idinfo = id;
			
		}else{
			if(id.kind != ASTNode.Kinds.Method){ //This method already exists with these parameters
				typeErrors++;
				System.out.println(error(n) + n.name.idname + " is already declared");
				n.name.type = ASTNode.Types.Error;
			}
			
			this.st.openScope();
			this.visit(n.args);
			
			if(id.type != n.returnType.type){ //Return type doesn't match the assignment
				typeErrors++;
				System.out.println(error(n) + n.name.idname + " cannot return this type");
				n.name.type = ASTNode.Types.Error;
	
			}
			if(id.kind == ASTNode.Kinds.Method && id.contains(this.st.top.methodParams)){ //Top is the scope
				typeErrors++;
				System.out.println(error(n) + n.name.idname + " cannot be overloaded");
				n.name.type = ASTNode.Types.Error;
			}else if(id.kind == ASTNode.Kinds.Method){
				id.methodParams.add(this.st.top.methodParams);
			}
			
			
			currentMethod = n; //Next method and visit the method / decls / statements
			this.visit(n.decls);
			this.visit(n.stmts);
			try{
				this.st.closeScope();
			}catch(EmptySTException ex){
				//can't happen
			}
		}
		
	}

	void visit(incrementNode n) {
		this.visit(n.target);
		
		try{
			assertCondition((n.target.type == ASTNode.Types.Character || n.target.type == ASTNode.Types.Integer) &&
					(n.target.kind == ASTNode.Kinds.Var || n.target.kind == ASTNode.Kinds.ArrayParm 
					|| n.target.kind == ASTNode.Kinds.ScalarParm)); //Must be an incrementable type
			
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Cannot increment this target");
		}
	}

	void visit(decrementNode n) {
		this.visit(n.target);
		
		try{
			assertCondition((n.target.type == ASTNode.Types.Character || n.target.type == ASTNode.Types.Integer) &&
					(n.target.kind == ASTNode.Kinds.Var || n.target.kind == ASTNode.Kinds.ArrayParm 
					|| n.target.kind == ASTNode.Kinds.ScalarParm));//Must be an decrement type
			
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Cannot decrement this target");
		}
	}

	void visit(argDeclsNode n) {
		this.visit(n.thisDecl);
		this.visit(n.moreDecls); //Always look for more nodes!
	}

	void visit(nullArgDeclsNode n) {
	}

	void visit(valArgDeclNode n) {
		
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.argName.idname);
		if(! (id == null)) { //If it's not null, it exists
			typeErrors++;
			n.argName.type = ASTNode.Types.Error;
			System.out.println(error(n) + id.name() + " has already been declared");
		}else{ //Or add it for reference later
			id = new SymbolInfo(n.argName.idname, ASTNode.Kinds.ScalarParm, n.argType.type);
		
		n.argName.type = n.argType.type;
		try {
			this.st.insert(id);
		} 
		catch (DuplicateException ex) {} 
		catch (EmptySTException ex){} //can't happen
		n.argName.idinfo= id;
		this.st.top.methodParams.add(new param(n.argType.type, ASTNode.Kinds.ScalarParm));
	}
	}

	void visit(arrayArgDeclNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.argName.idname);
		if(id != null){
			typeErrors++;
			System.out.println(error(n) + " Argname Already declared"); //Array already exists
			n.argName.type = ASTNode.Types.Error;		
		}else{
			id = new SymbolInfo(n.argName.idname, ASTNode.Kinds.ArrayParm, n.elementType.type);
			n.argName.type = n.elementType.type;
			
			try {
				this.st.insert(id);
			} 
			catch (DuplicateException ex) {} 
			catch (EmptySTException ex){}
			n.argName.idinfo = id;
			this.st.top.methodParams.add(new param(n.elementType.type, ASTNode.Kinds.ArrayParm));
		}
	}

	void visit(constDeclNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.constName.idname);
		
		if(id != null){
			typeErrors++;
			System.out.println(error(n) + id.name() + " is already declared");
			n.constName.type = ASTNode.Types.Error;
		}else{
			this.visit(n.constValue);
			try{
				assertCondition(isScalar(n.constValue.kind));
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + "Constants can only be scalars");
			}
			
			id = new SymbolInfo(n.constName.idname, ASTNode.Kinds.Value, n.constValue.type);
			n.constName.type = n.constValue.type;
			try {
				this.st.insert(id);
			} catch (DuplicateException ex) {
				/* can't happen */
			} catch (EmptySTException ex) {
				/* can't happen */
			}
			
			n.constName.idinfo = id;
		}
	}

	void visit(arrayDeclNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.arrayName.idname);
		
		if(id != null){
			typeErrors++;
			System.out.println(error(n) + id.name() + " is already declared");
			n.arrayName.type = ASTNode.Types.Error;
		}else{
			id = new SymbolInfo(n.arrayName.idname, ASTNode.Kinds.Array, n.elementType.type);
			
			if(n.arraySize.intval < 1){
				typeErrors++;
				System.out.println(error(n) + n.arrayName.idname + " must have atleast 1 element");
				n.arrayName.type = ASTNode.Types.Error;
				id.arraySize = 1;
			}else{
				id.arraySize = n.arraySize.intval;
				n.arrayName.type = n.elementType.type;
			}
			
			try {
				this.st.insert(id);
			} catch (DuplicateException ex) {
				/* can't happen */
			} catch (EmptySTException ex) {
				/* can't happen */
			}
			n.arrayName.idinfo = id;
		}
	}

	void visit(charTypeNode n) {
		// don't need to worry about this
	}

	void visit(voidTypeNode n) {
		// don't need to worry about this
	}

	void visit(whileNode n) {
		this.visit(n.condition);
		try{
			assertCondition(n.condition.type == ASTNode.Types.Boolean && isScalar(n.condition.kind));
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Condition should be of type boolean and kind should be a scalar");
		}
		
		if(n.label.isNull()){
			this.visit(n.loopBody);
			return;
		}else{
			SymbolInfo id = (SymbolInfo) this.st.localLookup(((identNode)n.label).idname);
			if(id == null){
				id = new SymbolInfo(((identNode)n.label).idname, ASTNode.Kinds.VisibleLabel, ASTNode.Types.Void);
				try{
					this.st.insert(id);
				}catch(DuplicateException ex){}
				catch(EmptySTException ex){}
				
				this.visit(n.loopBody);
				id.kind = ASTNode.Kinds.HiddenLabel;
			}else{
				typeErrors++;
				System.out.println(error(n) + "label is already declared");
				((identNode)n.label).type = ASTNode.Types.Error;
			}
		}
		
	}

	void visit(breakNode n) {
		SymbolInfo id = (SymbolInfo) this.st.globalLookup(n.label.idname);
		
		if(id == null){
			typeErrors++;
			System.out.println(error(n) + n.label.idname + " isn't a correct label");
			return;
		}
		try{
			assertCondition(id.kind == ASTNode.Kinds.VisibleLabel);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Label " + n.label.idname + " is out of scope");
		}
	}

	void visit(continueNode n) {
		SymbolInfo id = (SymbolInfo) this.st.globalLookup(n.label.idname);
		
		if(id == null){
			typeErrors++;
			System.out.println(error(n) + n.label.idname + " isn't a correct label");
			return;
		}
		try{
			assertCondition(id.kind == ASTNode.Kinds.VisibleLabel);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Label " + n.label.idname + " is out of scope");
		}
	}

	void visit(callNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.methodName.idname);
		
		if(id == null){
			typeErrors++;
			System.out.println(error(n) + n.methodName.idname + "isn't declared");
			return;
		}
		
		try{
			assertCondition(id.type == ASTNode.Types.Void && id.kind == ASTNode.Kinds.Method);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + n.methodName.idname + "No return value needed");
		}
		
		this.visit(n.args);
		
		ArrayList<param> args = new ArrayList<param>();
		argsNodeOption argsNodeOp = n.args;

		while(!argsNodeOp.isNull()){
			param p = new param(((argsNode)argsNodeOp).argVal.type, ((argsNode)argsNodeOp).argVal.kind);
			args.add(p);
			argsNodeOp = ((argsNode)argsNodeOp).moreArgs;
		}
		
		try{
			assertCondition(id.contains(args));
		}catch(RuntimeException ex){
			typeErrors++;
			n.methodName.type = ASTNode.Types.Error;
			if(id.methodParams.size() == 0){
				System.out.println(error(n) + n.methodName.idname + " does not need any params");
			}
			else if(id.methodParams.size() == 1){
				if(id.methodParams.get(0).size() != args.size()){
					System.out.println(error(n) + "" +  n.methodName.idname 
							+ " must have the following number of parameters: " + id.methodParams.get(0).size());
					
				}
				else{
					for(int i = 0; i < id.methodParams.get(0).size(); i++){
						if(i == args.size())break;
						if(!args.get(i).CompareTo(id.methodParams.get(0).get(i))){
							System.out.println(error(n) + " when calling " + n.methodName.idname + 
									" the follow parameter has an incorrect type: " + n.methodName.idname);
						}
					}
				}
				
			}else{
				System.out.println(error(n) + " when calling " + n.methodName.idname + 
						" the follow definitions do not match the parameters. " + n.methodName.idname);
			}
			}
			
		}
		
	

	void visit(readNode n) {
		this.visit(n.targetVar);
		try{
			assertCondition(n.targetVar.type == ASTNode.Types.Integer || n.targetVar.type == ASTNode.Types.Character);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Only int and char values may be read");
		}
		this.visit(n.moreReads);
	}

	void visit(returnNode n) {
		if(n.returnVal.isNull()){
			try{
				assertCondition(currentMethod.returnType.type == ASTNode.Types.Void);
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + "Return value can't be null and when method is not void");
			}
		}else{
			try{
				assertCondition(isScalar(((exprNode)n.returnVal).kind) && 
						((exprNode)n.returnVal).type == currentMethod.returnType.type);
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + "Incorrect return type.");
			}
		}
	}

	void visit(argsNode n) {
		this.visit(n.argVal);
		this.visit(n.moreArgs);
	}

	void visit(nullArgsNode n) {
	}

	void visit(castNode n) {
		this.visit(n.operand);
		
		try{
			assertCondition(n.operand.type == ASTNode.Types.Boolean || n.operand.type == ASTNode.Types.Character || 
					n.operand.type == ASTNode.Types.Integer);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Can only cast int, bool, chars to other int, bool, and chars");
		}
		if(n.resultType.type == ASTNode.Types.Boolean){
			n.type = ASTNode.Types.Boolean;
		}else if(n.resultType.type == ASTNode.Types.Character){
			n.type = ASTNode.Types.Character;
		}else if(n.resultType.type == ASTNode.Types.Integer){
			n.type = ASTNode.Types.Integer;
		}
		n.kind = n.operand.kind;
	}

	void visit(fctCallNode n) {
		SymbolInfo id = (SymbolInfo) this.st.localLookup(n.methodName.idname);
		
		if(id == null){
			typeErrors++;
			System.out.println(error(n) + n.methodName.idname + "isn't declared");
			return;
		}else if(id.kind != ASTNode.Kinds.Method){
			typeErrors++;
			System.out.println(error(n) + n.methodName.idname + " isn't a method");
			n.methodName.type = ASTNode.Types.Error;
		}else{
			n.type = id.type;
			n.kind = ASTNode.Kinds.ScalarParm;
			try{
				assertCondition(id.type != ASTNode.Types.Void);
			}catch(RuntimeException ex){
				typeErrors++;
				System.out.println(error(n) + n.methodName.idname + "must return void");
			}

			this.visit(n.methodArgs);

			ArrayList<param> args = new ArrayList<param>();
			argsNodeOption argsNodeOp = n.methodArgs;

			while(!argsNodeOp.isNull()){
				param p = new param(((argsNode)argsNodeOp).argVal.type, ((argsNode)argsNodeOp).argVal.kind);
				args.add(p);
				argsNodeOp = ((argsNode)argsNodeOp).moreArgs;
			}

			try{
				assertCondition(id.contains(args));
			}catch(RuntimeException ex){
				typeErrors++;
				n.methodName.type = ASTNode.Types.Error;
				if(id.methodParams.size() == 0){
					System.out.println(error(n) + n.methodName.idname + " does not need any params");
				}
				else if(id.methodParams.size() == 1){
					if(id.methodParams.get(0).size() != args.size()){
						System.out.println(error(n) + "" +  n.methodName.idname 
								+ " must have the following number of parameters: " + id.methodParams.get(0).size());

					}
					else{
						for(int i = 0; i < id.methodParams.get(0).size(); i++){
							if(i == args.size())break;
							if(!args.get(i).CompareTo(id.methodParams.get(0).get(i))){
								System.out.println(error(n) + " when calling " + n.methodName.idname + 
										" the follow parameter has an incorrect type: " + n.methodName.idname);
							}
						}
					}

				}else{
					System.out.println(error(n) + " when calling " + n.methodName.idname + 
							" the follow definitions do not match the parameters. " + n.methodName.idname);
				}
			}

		}
		
			
	}

	void visit(unaryOpNode n) {
		this.visit(n.operand);
		try{
			assertCondition(n.operand.type == ASTNode.Types.Boolean);
		}catch(RuntimeException ex){
			typeErrors++;
			System.out.println(error(n) + "Operand must be boolean");
		}
		n.type = ASTNode.Types.Boolean;
	}

	void visit(charLitNode n) {
		n.type = ASTNode.Types.Character;
		n.kind = ASTNode.Kinds.Var;
	}

	void visit(strLitNode n) {
		n.kind = ASTNode.Kinds.String;
	}

	void visit(trueNode n) {
		n.kind = ASTNode.Kinds.Value;
		n.type = ASTNode.Types.Boolean;
	}

	void visit(falseNode n) {
		n.kind = ASTNode.Kinds.Value;
		n.type = ASTNode.Types.Boolean;
	}

}

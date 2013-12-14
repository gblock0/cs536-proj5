// The following methods type check   AST nodes used in CSX Lite
//  You will need to complete the methods after line 242 to type check the
//   rest of CSX
//  Note that the type checking done for CSX lite may need to be extended to
//   handle full CSX (for example binaryOpNode).

import java.util.*;
public class TypeChecking extends Visitor { 

//	static int typeErrors =  0;     // Total number of type errors found 
//  	public static SymbolTable st = new SymbolTable(); 	
	int typeErrors;     // Total number of type errors found 
	SymbolTable st;	   
        // A pointer to the root of the method currently being compiled
	methodDeclNode currentMethod;
	
	TypeChecking(){
		typeErrors = 0;
		st = new SymbolTable(); 
		currentMethod = null;
	}

	boolean isTypeCorrect(csxLiteNode n) {
        	this.visit(n);
        	return (typeErrors == 0);
	}
	
	boolean isTypeCorrect(classNode n) {  
    		this.visit(n);
    		if (typeErrors > 0)
    			System.out.println("Error count = "+typeErrors);
    		return (typeErrors == 0); 
}
	
	static void assertCondition(boolean assertion){  
		if (! assertion)
			 throw new RuntimeException();
	}
	 void typeMustBe(ASTNode.Types testType,ASTNode.Types requiredType,String errorMsg) {
		 if ((testType != ASTNode.Types.Error) && (testType != requiredType)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }
	  void typeMustBe(ASTNode.Types testType,ASTNode.Types testType1,
			 		        ASTNode.Types testType2,String errorMsg) {
		 		if ((testType != ASTNode.Types.Error) && (testType != testType1)
                     && (testType != testType2)) {
		 			System.out.println(errorMsg);
		 			typeErrors++;  
		 		}
	 }
	  void typeMustBe(ASTNode.Types testType,ASTNode.Types testType1,
		        ASTNode.Types testType2,ASTNode.Types testType3,String errorMsg) {
	if ((testType != ASTNode.Types.Error) && (testType != testType1)
       && (testType != testType2) && (testType != testType3)) {
		System.out.println(errorMsg);
		typeErrors++;  
	}
}
	 void typesMustBeEqual(ASTNode.Types type1,ASTNode.Types type2,String errorMsg) {
		 if ((type1 != ASTNode.Types.Error) && (type2 != ASTNode.Types.Error) &&
                     (type1 != type2)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }
	  void mustBeScalar(exprNode target, String errorMsg) {
			if (target.type != ASTNode.Types.Error && !isScalar(target.kind)){
	                	System.out.println(errorMsg);
	                	typeErrors++;
			}
	   }
	   void mustBeAssignable(nameNode target,
              exprNode source, String errorMsg) {
		  if (target.type==ASTNode.Types.Error ||
				  source.type==ASTNode.Types.Error)
			  return; // Can't check values w/ error type
		  if (isScalar(target.kind) ){
			  if (isScalar(source.kind) &&
					  source.type==target.type)
				  return; // Assignment is OK
		  }else if (isArray(target.kind) &&
				    isArray(source.kind) &&
				    source.type==target.type) {
			  checkLengths(target,source,
					  error(source) + "Source and target of the "
							  	+ "assignment must have the same length.");
			  	return; // Assignment is OK
		  }else if (isArray(target.kind) &&
				  target.type==ASTNode.Types.Character &&
				  source.kind == ASTNode.Kinds.String ) {
			  	checkLengths(target,source,
			  			error(source) + "Source and target of the "
			  					+ "assignment must have the same length.");
			  	return; // Special case of string to char array asg
		  } 
		  // Illegal assignment if we reach here
		  System.out.println(errorMsg);
		  typeErrors++;
	  }
	   public int getLen(String strval) { //Compute length of str lit
			int cnt =0;
			// Ignore enclosing quotes and escapes
			for (int i=1;i<strval.length()-1;i++) {
				cnt++;
				if (strval.charAt(i) == '\\')
					i++;
			}
			return cnt;
		}
	   
	    void checkLengths(nameNode target,
               exprNode source, String errorMsg) {
		   if (target.kind == ASTNode.Kinds.Array) {
			   int targetLen =
					   ((ArrayInfo) target.varName.idinfo).size;
			   if (source.kind == ASTNode.Kinds.Array) {
				   nameNode s = (nameNode) source;
				   int sourceLen = 
						   ((ArrayInfo) s.varName.idinfo).size;
				   if (targetLen != sourceLen) {
					   System.out.println(errorMsg);
					   typeErrors++; }}
			   else if (source.kind == ASTNode.Kinds.String){
				   int sourceLen = 
						   getLen(((strLitNode) source).strval);
				   if (targetLen != sourceLen) {
					   System.out.println(errorMsg);
					   typeErrors++; }}
		   		}
	   }
	   boolean isScalar(ASTNode.Kinds k) { // Is this kind scalar?
	        switch(k){
	          case Var: return true;
	          case Value: return true;
	          case Array: return false;
	          case Method: return false;
	          case ArrayParm: return false;
	          case ScalarParm: return true;
	          case String: return false;
	          case VisibleLabel: return false;
	          case HiddenLabel: return false;
	          case Other: return false;
	          default: throw new RuntimeException();
	        }
	        /*
	        Var,	
		    Value,
		    Array,
		    Method,
		    ArrayParm,
		    ScalarParm,
		    String,
		    VisibleLabel,
		    HiddenLabel,
		    Other
	         */
	 }
	    boolean isAssignable(ASTNode.Kinds k) { // Can this kind of object be assigned to?
	        switch(k){
	          case Var: return true;
	          case Value: return false;
	          case Array: return true;
	          case Method: return false;
	          case ArrayParm: return true;
	          case ScalarParm: return true;
	          case String: return false;
	          case VisibleLabel: return false;
	          case HiddenLabel: return false;
	          case Other: return false;
	          default: throw new RuntimeException();
	        }
	 }  
	boolean isArray(ASTNode.Kinds k) { // Is this kind scalar?
		return k==ASTNode.Kinds.Array || k==ASTNode.Kinds.ArrayParm;
	}
	
	String error(ASTNode n) {
		return "Error (line " + n.linenum + "): ";
        }

	public void compareParms(ParmInfo actuals, identNode m, int pos, ParmInfo formals) {
		if (actuals.kind != formals.kind || actuals.type != formals.type) {
			System.out.println(error(m)+ "In the call to "+
			  m.idname+ ", parameter "+pos+ " has incorrect type.");
			typeErrors++; }
		if (actuals.next != null)
			compareParms(actuals.next,m,pos+1,formals.next);
	 }
	
	public boolean canMatchParms(ParmInfo actuals,  ParmInfo formals) {
		if (actuals.kind != formals.kind || actuals.type != formals.type) 
			return false;
		if (actuals.next == null)
			return true;
		else return canMatchParms(actuals.next,formals.next);
	 }
	
	public boolean parmsMatch(ParmInfo actuals, ParmInfo formals) {
		if (ParmInfo.length(actuals) != ParmInfo.length(formals))
			return false;
		else if (actuals == null)
			return true;
			else return canMatchParms(actuals,formals);
	}
	
	public boolean parmsMatch(ParmInfo actuals, ArrayList<ParmInfo> signatures) {
		Iterator<ParmInfo> itr = signatures.iterator();
		while (itr.hasNext()){
			if (parmsMatch(actuals, itr.next()))
				return true;
		}
		return false;
	}
	
	
	 void typesMustBeComparable(ASTNode.Types type1,ASTNode.Types type2,
            String errorMsg) {
		if ((type1 == ASTNode.Types.Error) || (type2 == ASTNode.Types.Error))
			return;
		if ((type1 == ASTNode.Types.Boolean) && (type2 == ASTNode.Types.Boolean))
			return;
		if (((type1 == ASTNode.Types.Integer) || (type1 == ASTNode.Types.Character)) &&
				((type2 == ASTNode.Types.Integer) || (type2 == ASTNode.Types.Character)))
			return;
		System.out.println(errorMsg);
		typeErrors++;
	 }

		 void isScalarAssignable(nameNode target, String errorMsg) {
			if (target.kind==ASTNode.Kinds.Var ||
			    target.kind==ASTNode.Kinds.ScalarParm) {
				return;  // Scalar may be assigned to
			} else {
	                	System.out.println(errorMsg);
	                	typeErrors++;
			}
	    }

	static String opToString(int op) {
		switch (op) {
			case sym.PLUS:
				return("+");
			case sym.MINUS:
				return("-");
			case sym.COR:
				return("||");
			case sym.CAND:
				return("&&");
			case sym.LT:
				return("<");
			case sym.GT:
				return(">");
			case sym.LEQ:
				return("<=");
			case sym.GEQ:
				return(">=");
			case sym.EQ:
				return("==");
			case sym.NOTEQ:
				return("!=");
			case sym.TIMES:
				return("*");
			case sym.SLASH:
				return("/");
			case sym.NOT:
				return("!");
			default:
				assertCondition(false);
				return "";
		}
	}



	 void visit(csxLiteNode n){
		this.visit(n.progDecls);
		this.visit(n.progStmts);
	}
	
	void visit(fieldDeclsNode n){
			this.visit(n.thisField);
			this.visit(n.moreFields);
	}
	void visit(nullFieldDeclsNode n){}

	void visit(stmtsNode n){
		  //System.out.println ("In stmtsNode\n");
		  this.visit(n.thisStmt);
		  this.visit(n.moreStmts);

	}
	void visit(nullStmtsNode n){}

	void visit(varDeclNode n){
		SymbolInfo     id;
		if (! n.initValue.isNull()) {
        	this.visit(n.initValue);
        	exprNode init = (exprNode) n.initValue;// Know initValue is non-null now
			typesMustBeEqual(n.varType.type,
							init.type,  
                       	    error(n) + "The initializer must be of type "+
                            n.varType.type);
			mustBeScalar(init,
                       	    error(n) + "The initializer must be a scalar. "); 

		}

        	id = (SymbolInfo) st.localLookup(n.varName.idname);
        	if (id != null) {
               		 System.out.println(error(n) + id.name()+
                                     " is already declared.");
                	typeErrors++;
                	n.varName.type = ASTNode.Types.Error;

        	} else {
                	id = new SymbolInfo(n.varName.idname,
                                         ASTNode.Kinds.Var, n.varType.type);
                	n.varName.type = n.varType.type;
			try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
                	n.varName.idinfo=id;
        	}

	};
	
	void visit(nullTypeNode n){}
	
	void visit(intTypeNode n){
		// No type checking needed
	}
	void visit(boolTypeNode n){
		// No type checking needed
	}
	void visit(identNode n){
		SymbolInfo    id;
        	id =  (SymbolInfo) st.globalLookup(n.idname);
        	if (id == null) {
               	 	System.out.println(error(n) +  n.idname +
                             " is not declared.");
                typeErrors++;
                n.type = ASTNode.Types.Error;
        } else {
                n.type = id.type; 
                n.kind = id.kind; 
                n.idinfo = id; // Save ptr to correct symbol table entry
        	}
	}

// Extend nameNode's method to handle subscripts
	void visit(nameNode n){
		this.visit(n.varName);
        	n.type=n.varName.type;
		if (n.subscriptVal.isNull()) {
        		n.type=n.varName.type;
        		n.kind=n.varName.kind;
		} else {
			this.visit(n.subscriptVal);
			if (! isArray(n.varName.kind) ){
               	 		System.out.println(error(n) +
                                 "Only arrays can be subscripted.");
                		n.type = ASTNode.Types.Error;
               			typeErrors++;
			} else {
				exprNode subscript = (exprNode) n.subscriptVal;// Know subscriptVal is non-null now
        			typeMustBe(subscript.type, 
		           	    ASTNode.Types.Character,
				        ASTNode.Types.Integer,
                	    	     error(n) 
                	    	     + "Array subscripts must be "
                         	     +  "integer or character expressions.");
        			mustBeScalar(subscript, 
                                  error(n) + "The subscript must be a scalar. ");
        			n.type=n.varName.type;
        			n.kind= ASTNode.Kinds.Var;
			}
		}


	}

	void visit(asgNode n){
	 
		this.visit(n.target);
		this.visit(n.source);

		if (! isAssignable(n.target.kind)) 
               		 System.out.println(error(n) + 
                                 "Target of assignment can't be changed.");

		mustBeAssignable(n.target,n.source,
                        error(n) + "Right hand side of an assignment is not "
                            	+ "assignable to left hand side.");
	}
	
	 void visit(incrementNode n){
		 this.visit(n.target);
		 typeMustBe(n.target.type,
                 ASTNode.Types.Integer, ASTNode.Types.Character,
                 error(n) + "Operand of ++"  +  " must be arithmetic.");
		 mustBeScalar(n.target,
                 error(n) + "Operand of ++"  +  " must be a scalar.");
		 if (! isAssignable(n.target.kind)) 
       		 System.out.println(error(n) + 
                         "Target of ++ can't be changed.");		 }
	 
	 void visit(decrementNode n){
		 this.visit(n.target);
		 typeMustBe(n.target.type,
                 ASTNode.Types.Integer, ASTNode.Types.Character,
                 error(n) + "Operand of --"  +  " must be arithmetic.");
		 mustBeScalar(n.target,
                 error(n) + "Operand of --"  +  " must be a scalar.");
		 if (! isAssignable(n.target.kind)) 
       		 System.out.println(error(n) + 
                         "Target of -- can't be changed.");
		 }

	void visit(ifThenNode n){
		  this.visit(n.condition);
        	  typeMustBe(n.condition.type, ASTNode.Types.Boolean,
                	error(n) + "The control expression of an" +
                          	" if must be a bool.");
        	  mustBeScalar(n.condition,
        			  error(n) + "The control expression of an" +
        			  				" if must be a scalar.");

		  this.visit(n.thenPart);
		  this.visit(n.elsePart);
	}
	  
	 void visit(printNode n){
		String errorMsg = 
                	error(n) + 
                           "Only integers, booleans, strings, " +
                           "characters and character arrays may be written.";
		this.visit(n.outputValue);
		if (isScalar(n.outputValue.kind))
        	typeMustBe(n.outputValue.type,
			   ASTNode.Types.Integer,
			   ASTNode.Types.Character, 
			   ASTNode.Types.Boolean,
			   errorMsg);
		else if (isArray(n.outputValue.kind))
        		typeMustBe(n.outputValue.type,
				ASTNode.Types.Character, errorMsg);
		else if (n.outputValue.kind != ASTNode.Kinds.String)
			System.out.println(errorMsg);
		this.visit(n.morePrints);
	  }
	 
	  
	  void visit(blockNode n){
		// open a new local scope for the block body
			st.openScope();
			this.visit(n.decls);
			this.visit(n.stmts);
			// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
	                      { /* can't happen */ }
	  }

	
	  void visit(binaryOpNode n){
		  
		this.visit(n.leftOperand);
		this.visit(n.rightOperand);

		mustBeScalar(n.leftOperand,
                    error(n) + "Left operand of "
                            + opToString(n.operatorCode) 
                      	    +  " must be a scalar.");
        	mustBeScalar(n.rightOperand,
                    error(n) + "Right operand of "
                            + opToString(n.operatorCode) 
                      	    +  " must be a scalar.");
        	if(n.operatorCode == sym.PLUS ||
		   n.operatorCode == sym.MINUS ||
		   n.operatorCode == sym.TIMES ||
		   n.operatorCode == sym.SLASH) {
        		n.type = ASTNode.Types.Integer;
        		n.kind = ASTNode.Kinds.Value;
        		typeMustBe(n.leftOperand.type,
                                   ASTNode.Types.Integer,
				   ASTNode.Types.Character,
                	    error(n) + "Left operand of "
                                    + opToString(n.operatorCode) 
                         	    +  " must be arithmetic.");
        		typeMustBe(n.rightOperand.type,
                                   ASTNode.Types.Integer,
				   ASTNode.Types.Character,
                	    error(n) + "Right operand of "
                                    + opToString(n.operatorCode) 
                         	    +  " must be arithmetic.");
		} else if (n.operatorCode == sym.COR ||
		           n.operatorCode == sym.CAND) {
        		n.type = ASTNode.Types.Boolean;
        		n.kind = ASTNode.Kinds.Value;
        		typeMustBe(n.leftOperand.type,
			    ASTNode.Types.Boolean,
                	    error(n) + "Left operand of "
                                    + opToString(n.operatorCode) 
                         	    +  " must be a bool.");
        		typeMustBe(n.rightOperand.type,
			    ASTNode.Types.Boolean,
                	    error(n) + "Right operand of "
                                    + opToString(n.operatorCode) 
                         	    +  " must be a bool.");
		} else // Must be a relational operator
		       typesMustBeComparable(n.leftOperand.type,
                                             n.rightOperand.type,
                	    error(n) + "Operands of "
                                    + opToString(n.operatorCode) 
                         	    +  " must both be arithmetic"
                         	    +  " or both must be boolean.");
	  }

	
	
	void visit(intLitNode n){
	//      All intLits are automatically type-correct
	}


 

	 
	 void visit(classNode n){
		this.visit(n.members);
		}

	 void  visit(memberDeclsNode n){
		this.visit(n.fields);
		this.visit(n.methods);
		if (n.methods.isNull()){
			System.out.println(error(n) + 
                    "The last method in a class must be "+
                    "declared as void main() {...");
          	typeErrors++;}
		else checkLastMethod((methodDeclsNode)n.methods);
	 }
	 
	 void  visit(methodDeclsNode n){
		this.visit(n.thisDecl);
		this.visit(n.moreDecls);
		 }
	 
	  void checkLastMethod(methodDeclsNode n) {
		if (n.moreDecls.isNull())
			checkLastMethod(n.thisDecl);
		else	checkLastMethod((methodDeclsNode)n.moreDecls);
	}

	 // check that last method is void main() {...}
        void checkLastMethod(methodDeclNode n) {
        	if (!n.name.idname.equalsIgnoreCase("main") ||
                   (! n.args.isNull()) ||
                   (((typeNode) n.returnType).type != ASTNode.Types.Void)) {
               		 System.out.println(error(n) + 
                          "The last method in a class must be "+
                          "declared as void main() {...");
                	typeErrors++;
        	}
        }
   
	 void visit(nullStmtNode n){}
	 
	 void visit(nullReadNode n){}

	 void visit(nullPrintNode n){}

	 void visit(nullExprNode n){}

	 void visit(nullMethodDeclsNode n){}

	 boolean possibleOverload(SymbolInfo currentDef, SymbolInfo newDef) {
		 return (currentDef.kind == ASTNode.Kinds.Method) &&
				(currentDef.type == newDef.type);
	 }
	 
	 void visit(methodDeclNode n){
		SymbolInfo    lookupid;
        	MethodInfo    id;

                n.name.type = ((typeNode) n.returnType).type;
                n.name.kind = ASTNode.Kinds.Method;
                id = new MethodInfo(n.name.idname, n.name.type);

                //System.out.println("Error count in = "+typeErrors);
        	lookupid = (SymbolInfo) st.localLookup(n.name.idname);
        	if (lookupid != null && !possibleOverload(lookupid,id)) {
               	System.out.println(error(n) + lookupid.name()+
                                     " is already declared.");
                typeErrors++;
                n.name.type = ASTNode.Types.Error;

        	} else { try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
        	}
		// open a new local scope for the method body
		st.openScope();
		// remember which method we are compiling
		currentMethod = n; 
		//System.out.println("Error countbefore args = "+typeErrors);
		this.visit(n.args);   
		id.parms=(ParmInfo) n.args.parms;  // parms defined as Symb to simplify compilation
        n.name.idinfo=id;
        // Is this an overloading of method name?
        if (lookupid != null && possibleOverload(lookupid,id)){
        	MethodInfo methodDef = (MethodInfo) lookupid;
        	if (!methodDef.isOverloaded){
        		// First overloading
        		// See if previous and current defs diff in their parm lists
        		if (ParmInfo.differentParmsList(id.parms, methodDef.parms)) {
        			methodDef.isOverloaded = true;
        			methodDef.signatures.add(methodDef.parms);
        			methodDef.signatures.add(id.parms);
        			methodDef.parms = null; // parms only used for non-overloaded methods
        			//System.out.println(error(n) + "Updated method def:"+methodDef);
        		}else { System.out.println(error(n) + 
        				 "Illegal overloaded method definition (this parameter combination already used).");
        		 		typeErrors++;}
        	}else {//  See if  current and all previous overloaded defs differ in their parm lists
        		if (ParmInfo.differentParmsList(id.parms, methodDef.signatures)) {
        			methodDef.signatures.add(id.parms);
        			//System.out.println(error(n) + "Updated method def:"+methodDef);
        		}else { System.out.println(error(n) + 
        				"Illegal overloaded method definition (this parameter combination already used).");
        		 		typeErrors++;}
        		//System.out.println(error(n) + "Multiple overloadings not implemented yet.");
        	}
        }
        //System.out.println("Error count  decls= "+typeErrors);
		this.visit(n.decls);
		//System.out.println("Error count before stmts= "+typeErrors);
		this.visit(n.stmts);
		//System.out.println("Error count after stmts = "+typeErrors);
		// close this method's local scope
		try {
			st.closeScope();
		}  catch (EmptySTException e) 
                             { /* can't happen */ }
		// we are done w/ this method
		currentMethod = null;
	 }

	 
	void visit(argDeclsNode n){
		this.visit(n.thisDecl);
		this.visit(n.moreDecls);
		n.parms=n.thisDecl.parm;
		((ParmInfo) n.parms).next=((ParmInfo) n.moreDecls.parms);
	}

	void visit(nullArgDeclsNode n){}

	
	void visit(valArgDeclNode n){
		SymbolInfo     lookupid;
        	ParmInfo       id;

                n.argName.type = n.argType.type;
                n.argName.kind = ASTNode.Kinds.ScalarParm;
                id = new ParmInfo(n.argName.idname,
                                  n.argName.kind, n.argType.type);
        	lookupid = (SymbolInfo) st.localLookup(n.argName.idname);
        	if (lookupid != null) {
               		 System.out.println(error(n) + lookupid.name()+
                                     " is already declared.");
                	typeErrors++;
                	n.argName.type = ASTNode.Types.Error;

        	} else { try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
        	}
                n.argName.idinfo=id;
                n.parm=id;

	}
	
	void visit(arrayArgDeclNode n){
		SymbolInfo     lookupid;
        	ParmInfo       id;

                n.argName.type = n.elementType.type;
                n.argName.kind = ASTNode.Kinds.ArrayParm;
                id = new ParmInfo(n.argName.idname,
                                  n.argName.kind, n.elementType.type);
        	lookupid = (SymbolInfo) st.localLookup(n.argName.idname);
        	if (lookupid != null) {
               		 System.out.println(error(n) + lookupid.name()+
                                     " is already declared.");
                	typeErrors++;
                	n.argName.type = ASTNode.Types.Error;

        	} else { try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
        	}
            n.argName.idinfo=id;
            n.parm=id;
	}
	
	void visit(constDeclNode n){
		SymbolInfo     id;

		this.visit(n.constValue);

        	id = (SymbolInfo) st.localLookup(n.constName.idname);
        	if (id != null) {
               		 System.out.println(error(n) + id.name()+
                                     " is already declared.");
                	typeErrors++;
                	n.constName.type = ASTNode.Types.Error;

        	} else {
                	n.constName.type = n.constValue.type;
			mustBeScalar(n.constValue,
                       	    error(n) + "The constant must be a scalar. ");
			if (! isScalar(n.constValue.kind))
				n.constValue.type  = ASTNode.Types.Error;
                	id = new SymbolInfo(n.constName.idname,
                                      ASTNode.Kinds.Value,n.constName.type);
			try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
                	n.constName.idinfo=id;
                      }
	 }
	 
	 void visit(arrayDeclNode n){
		ArrayInfo     id;
        	SymbolInfo    lookupid;

        	if (n.arraySize.intval <= 0) {
               		 System.out.println(error(n) + n.arrayName.idname+
                                     " must have more than 0 elements.");
                	typeErrors++;
		}
        	lookupid = (SymbolInfo) st.localLookup(n.arrayName.idname);
        	if (lookupid != null) {
               		 System.out.println(error(n) + lookupid.name()+
                                     " is already declared.");
                	typeErrors++;
                	n.arrayName.type = ASTNode.Types.Error;

        	} else {
                	n.arrayName.type = n.elementType.type;
                	n.arrayName.kind = ASTNode.Kinds.Array;
                	id = new ArrayInfo(n.arrayName.idname,
					   n.elementType.type,
                                           n.arraySize.intval);
			try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
                	n.arrayName.idinfo=id;
        	}
	 }
	
	void visit(charTypeNode n){
	//      No type checking needed
	}
	void visit(voidTypeNode n){
	//      No type checking needed
	}

	void visit(whileNode n){
		SymbolInfo     id, lookupid;

		this.visit(n.condition);
        typeMustBe(n.condition.type, ASTNode.Types.Boolean,
                	error(n) + "The control expression of a while loop" +
                          	" must be a bool.");
        mustBeScalar(n.condition,
  			  error(n) + "The control expression of a" +
  			  				" while loop must be a scalar.");
		if (! n.label.isNull()) {
                	String labelName = ((identNode) n.label).idname;
                	id = new SymbolInfo(labelName,
                                  ASTNode.Kinds.VisibleLabel,
                                  ASTNode.Types.Void);
        		lookupid = (SymbolInfo) st.localLookup(labelName);
        		if (lookupid != null) {
               			 System.out.println(error(n) + lookupid.name()+
                                     " is already declared.");
                		typeErrors++;
        		} else { try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
        		}
			this.visit(n.loopBody);
			// Now the label becomes hidden
			id.kind=ASTNode.Kinds.HiddenLabel;
		}else	this.visit(n.loopBody);

	  }

	void visit(breakNode n){
		SymbolInfo    id;
        	id = (SymbolInfo) st.globalLookup(n.label.idname);
        	if (id == null) {
               	 	System.out.println(error(n) +  n.label.idname +
                             " is not declared.");
               		typeErrors++;
                	n.label.type = ASTNode.Types.Error;
        	} else {
                	n.label.type = id.type; 
                	n.label.kind = id.kind; 
                	n.label.idinfo = id; // Save ptr to sym tab entry
			if (id.kind == ASTNode.Kinds.HiddenLabel) {
               	 		System.out.println(error(n) +
                                 n.label.idname +
                                  " doesn't label an enclosing while loop.");
               			typeErrors++;
			} else  if (id.kind != ASTNode.Kinds.VisibleLabel) {
               	 		System.out.println(error(n) +
                                 n.label.idname + " isn't a label.");
               			typeErrors++;
			}
        	}
	}
	void visit(continueNode n){
		SymbolInfo    id;
        	id = (SymbolInfo) st.globalLookup(n.label.idname);
        	if (id == null) {
               	 	System.out.println(error(n) +  n.label.idname +
                             " is not declared.");
               		typeErrors++;
                	n.label.type = ASTNode.Types.Error;
        	} else {
                	n.label.type = id.type; 
                	n.label.kind = id.kind; 
                	n.label.idinfo = id; // Save ptr to sym tab entry
			if (id.kind == ASTNode.Kinds.HiddenLabel) {
               	 		System.out.println(error(n) +
                                 n.label.idname +
                                  " doesn't label an enclosing while loop.");
               			typeErrors++;
			} else  if (id.kind != ASTNode.Kinds.VisibleLabel) {
               	 		System.out.println(error(n) +
                                 n.label.idname + " isn't a label.");
               			typeErrors++;
			}
        	}
	}
	  
	void visit(callNode n){
		SymbolInfo    id;

		this.visit(n.args);
        id = (SymbolInfo) st.globalLookup(n.methodName.idname);
        if (id == null) {
        	System.out.println(error(n) +  n.methodName.idname +
                             " is not declared.");
            typeErrors++;
            n.methodName.type = ASTNode.Types.Error;
        } else {
                n.methodName.type = id.type; 
                n.methodName.kind = id.kind; 
                n.methodName.idinfo = id; // Save ptr to sym tab entry
                if (id.kind != ASTNode.Kinds.Method) {
                	System.out.println(error(n) +
                      n.methodName.idname + " isn't a method.");
               		typeErrors++;
                } else {
                	if (id.type != ASTNode.Types.Void) {
               	 		System.out.println(error(n) +
                          n.methodName.idname +
                          " is called as a procedure"+
               	 		  " and must therefore return void.");
               			typeErrors++;}
                	// check args here
                	MethodInfo m = (MethodInfo) id;
                	if (!m.isOverloaded) {
                		if (ParmInfo.length(m.parms) !=
                			ParmInfo.length((ParmInfo)  n.args.parms)) {
               	 			System.out.println(error(n) +
               	 				n.methodName.idname + " requires " +
               	 				ParmInfo.length(m.parms) + " parameters.");
               	 			typeErrors++;}
                		else if (ParmInfo.length(m.parms) > 0)
                			compareParms((ParmInfo) n.args.parms,
                						  n.methodName,1, m.parms);}
                	else { //check all possible overloadings
                			if (! parmsMatch((ParmInfo) n.args.parms, m.signatures)){
                				System.out.println(error(n) +
                						"None of the " + m.signatures.size() +
                						" definitions of method " +
                       	 				n.methodName.idname +
                       	 				" match the parameters in this call.");
                				typeErrors++;
                			}
                	}
			   }
        	}
	}

	  
	  void visit(readNode n){
		this.visit(n.targetVar);
        	typeMustBe(n.targetVar.type,ASTNode.Types.Integer,
                           ASTNode.Types.Character, error(n) + 
                           "Only integers and characters may be read.");
        	isScalarAssignable(n.targetVar,
                	error(n) + n.targetVar.varName.idname +
                           " may not be assigned to.");
		this.visit(n.moreReads);
	  }
	  

	  void visit(returnNode n){
		if (n.returnVal.isNull()) {
			if (currentMethod.returnType.type != ASTNode.Types.Void) {
               	 		System.out.println(error(n) +
				"Return type of " + currentMethod.name.idname +
				" is not void.");
               			typeErrors++;}
		} else {
			 this.visit(n.returnVal);
			 if (currentMethod.returnType.type !=
                                         ((exprNode) n.returnVal).type){
               	 		System.out.println(error(n) +
				"Return type of " + currentMethod.name.idname +
				" is "+ currentMethod.returnType.type);
               			typeErrors++;
                        }
			 mustBeScalar((exprNode)n.returnVal,
       			  error(n) + "The return value" +
       			  				" must be a scalar.");
		}

	  }

	  
	  void visit(argsNode n){
		ASTNode.Kinds argKind;

		this.visit(n.argVal);
		this.visit(n.moreArgs);
		if (isScalar(n.argVal.kind))
			argKind=ASTNode.Kinds.ScalarParm;
		else	if (isArray(n.argVal.kind))
				argKind=ASTNode.Kinds.ArrayParm;
			else argKind= ASTNode.Kinds.Other;
		n.parms = new ParmInfo("", argKind, n.argVal.type,
				      ((ParmInfo) n.moreArgs.parms));
	  }
	  	
	  void visit(nullArgsNode n){}
		
	  void visit(castNode n){
		this.visit(n.operand);
        	n.type = n.resultType.type;
        	n.kind = ASTNode.Kinds.Value;
		if (! isScalar(n.operand.kind))
			System.out.println(
                	    error(n) + "Operand of cast must be an "
                         	    +  " integer, character or boolean.");
        		typeMustBe(n.resultType.type,
				ASTNode.Types.Boolean,
		           	ASTNode.Types.Character,
				ASTNode.Types.Integer,
                	    	error(n) + "Target type of cast must be "
                         	    +  " integer, character or boolean.");

	  }

	  void visit(fctCallNode n){
		SymbolInfo    id;

		this.visit(n.methodArgs);
        	id = (SymbolInfo) st.globalLookup(n.methodName.idname);
        	if (id == null) {
               	 	System.out.println(error(n) +  n.methodName.idname +
                             " is not declared.");
               		typeErrors++;
                	n.methodName.type = ASTNode.Types.Error;
        	} else {
                	n.methodName.type = id.type; 
                	n.methodName.kind = id.kind; 
                	n.methodName.idinfo = id; // Save ptr to sym tab entry
                	n.type = n.methodName.type;
                	n.kind = ASTNode.Kinds.Value;
                	if (id.kind != ASTNode.Kinds.Method) {
               	 		System.out.println(error(n) +
                                 n.methodName.idname + " isn't a method.");
               			typeErrors++;
                	} else {
                		if (id.type == ASTNode.Types.Void) {
               	 			System.out.println(error(n) +
                                n.methodName.idname +
                                " is called as a function"+
               	 				" and therefore can't return void.");
               	 			typeErrors++;}
                		// check args here
                		MethodInfo m = (MethodInfo) id;
                		if (!m.isOverloaded) {
                			if (ParmInfo.length(m.parms) !=
                					ParmInfo.length((ParmInfo) n.methodArgs.parms)) {
                				System.out.println(error(n) +
                						n.methodName.idname +
                						" requires " +
                						ParmInfo.length(m.parms) +
                						" parameters.");
                				typeErrors++;}
                			else if (ParmInfo.length(m.parms) > 0)
                				compareParms((ParmInfo) n.methodArgs.parms,
                                     n.methodName,1, m.parms);}
                		else { //check all possible overloadings
                			if (! parmsMatch((ParmInfo) n.methodArgs.parms, m.signatures)){
                				System.out.println(error(n) +
                						"None of the " + m.signatures.size() +
                						" definitions of method " +
                						n.methodName.idname + 
                						" match the parameters in this call.");
                				typeErrors++;
                			}
                		}
                	}
        	}
	  }

	  void visit(unaryOpNode n){
		this.visit(n.operand);
        	assertCondition(n.operatorCode == sym.NOT);
        	n.type = ASTNode.Types.Boolean;
        	n.kind = ASTNode.Kinds.Value;
        	typeMustBe(n.operand.type, ASTNode.Types.Boolean,
                	    error(n) + "Operand of "
                                    + opToString(n.operatorCode) 
                         	    +  " must be boolean.");
        	mustBeScalar(n.operand,
                    error(n) + "Operand of "
                            + opToString(n.operatorCode) 
                      	    +  " must be a scalar.");

	  }

	
	void visit(charLitNode n){
	//      All character literals are automatically type-correct
	}
	  
	void visit(strLitNode n){
	//      All string literals are automatically type-correct
	}

	
	void visit(trueNode n){
	//      All trueNodes are automatically type-correct
	}

	void visit(falseNode n){
	//      All falseNodes are automatically type-correct
	}


}

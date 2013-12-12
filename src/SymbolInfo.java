import java.util.ArrayList;

import com.sun.tools.javac.util.List;

/**************************************************
*  class used to hold information associated w/
*  Symbs (which are stored in SymbolTables)
*  Update to handle arrays and methods
*
****************************************************/

class SymbolInfo extends Symb {
 public ASTNode.Kinds kind; 
 public ASTNode.Types type; 
 public ArrayList<ArrayList<param>> methodParams = new ArrayList<ArrayList<param>>();
 
 public ArrayList<exprNode> arrayElements;
 int arraySize; 
 
 public CodeGenerating.AdrModes adr;
 public String label;
 public int varIndex;
 public int numberOfLocals;

 public SymbolInfo(String id, ASTNode.Kinds k, ASTNode.Types t){    
	super(id);
	kind = k;
	type = t;
	arraySize = 0;
	methodParams = new ArrayList<ArrayList <param>>();
	adr = CodeGenerating.AdrModes.none;
	label = null;
	varIndex = 0;
	numberOfLocals = 0;
	};
	
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";}
 
 
 public boolean contains(ArrayList<param> params){
	 boolean foundParam = false;
	 
	 for(ArrayList<param> p : methodParams){
		 if(params.size() == p.size()){
			 
			 if(params.size() == 0){
				 return true;
			 }
			 foundParam = true;
			 
			 int index = 0;
			 for(param pa : p){
				 foundParam = pa.CompareTo(p.get(index));
				if(!foundParam){
					break;
				}
				 index++;
			 }
			 if(foundParam){
				 return true;
			 }
		 }
	 }
	 
	 return false;
 }
 
 
}
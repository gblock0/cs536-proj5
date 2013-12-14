import java.util.ArrayList;
import java.util.Iterator;

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
 public String bottomLabel;
 public String topLabel;
 public String methodReturnCode;

 
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
	bottomLabel = null;
	topLabel = null;
	methodReturnCode = null;
	
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
class ArrayInfo extends SymbolInfo {
 public int size; // Size of the array 

 public ArrayInfo(String id, ASTNode.Types t,int size){
	super(id, ASTNode.Kinds.Array ,t);
	this.size=size;
 }

 public String toString(){
	if (name() != "")
             return "("+name()+": kind=Array" + ", type="+  type+
                       "size="+size+  ")";
        else return "(kind=Array" + ", type="+  type+
                       "size="+size+  ")";
 }
}

class ParmInfo extends SymbolInfo {
 public ParmInfo next; // next method parm in the arg list

 public ParmInfo(String id, ASTNode.Kinds k, ASTNode.Types t,ParmInfo next){   
	super(id,k,t);
	this.next=next;
 }
 
 public ParmInfo(String id, ASTNode.Kinds k, ASTNode.Types t){
	super(id,k,t);
	this.next=null;
 }
 
 public String toString(){
	String val;
	if (name() != "")
	     val = "("+name()+": kind=" + kind + ", type="+  type+ ")";
	else val = "(kind=" + kind + ", type="+  type+ ")";
        
	if (next == null)
	     return  val;
        else return val+"; "+next.toString();
 }
 public int length() {
	if (next == null)
		return 1;
	else	return 1+ next.length();
 }
 public static int length(ParmInfo p) {
	//System.out.println("Parm list ="+ p);
	if (p == null)
		return 0;
	else	return p.length();
 }
 public static boolean differentParmsList(ParmInfo l1, ParmInfo l2){
	 //Do 2 ParmInfo lists differ in length, type or kind?
	 if (length(l1) != length(l2))
		 return true;
	 else // Know the lists have same length
		 if (length(l1) == 0)
			 return false;
		 else // Know both list are non-null
			 if (l1.type != l2.type || l1.kind != l2.kind )
				 return true;
			 else return differentParmsList(l1.next, l2.next);

 }
 
 public static boolean differentParmsList(ParmInfo l1, ArrayList<ParmInfo> l2){
	 //Does the l1 ParmInfo list differ in length, type or kind?from each ParmInfo list in l2?
	 Iterator<ParmInfo> itr = l2.iterator();
	 while (itr.hasNext()){
		if (! differentParmsList(l1, itr.next()))
			return false;
	 }
	 return true; 
 }
 
}
/* Moved to TypeChecking
  public void compareParms(ParmInfo actuals, identNode m, int pos, ParmInfo formals) {

	if (kind != formals.kind || type != formals.type) {
		System.out.println(error(m)+ "In the call to "+
		  m.idname+ ", parameter "+pos+ " has incorrect type.");
		m.typeErrors++; }
	if (next != null)
		next.compareParms(m,pos+1,formals.next);
 }
}
*/
class MethodInfo extends SymbolInfo { 
	public ParmInfo parms;
	public boolean isOverloaded;
	public ArrayList<ParmInfo> signatures;

	public MethodInfo(String id, ASTNode.Types t, ParmInfo parms){  
		super(id, ASTNode.Kinds.Method, t);
		this.parms = parms;
		isOverloaded = false;
		signatures = new ArrayList<ParmInfo>();};
 
	public MethodInfo(String id, ASTNode.Types t){
		super(id, ASTNode.Kinds.Method, t);
		this.parms = null;
		isOverloaded = false;
		signatures = new ArrayList<ParmInfo>();
	};
 
	public String toString(){
		if (isOverloaded) {
			if (name() != "")
				return "("+name()+": kind=Method" + ", type="+  type+
						" signatures="+signatures+ ")";
			else return "(kind=Method" + ", type="+  type+
                     " signatures="+signatures+ ")";}
		else {
			if (name() != "")
				return "("+name()+": kind=Method" + ", type="+  type+
						" parms="+parms+ ")";
			else return "(kind=Method" + ", type="+  type+
                     " parms="+parms+ ")";
		  }
    }
  }


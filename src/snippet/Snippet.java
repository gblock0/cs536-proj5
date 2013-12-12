package snippet;

public class Snippet {
	 
	declField(arrayDeclNode n){ 
	 String arrayLabel = n.arrayName.idname +"$"; 
	 declGlobalArray(arrayLabel,n.elementType); 
	 n.arrayName.idinfo.label = arrayLabel; 
	 n.arrayName.idinfo.adr = global; 
	} 
	
}


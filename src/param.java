class param{
	ASTNode.Kinds kind;
	ASTNode.Types type;
	public param(ASTNode.Types type, ASTNode.Kinds kind){
		this.kind = kind;
		this.type = type;
		
	}
	
	boolean isScalar(ASTNode.Kinds var){
		boolean isScalar = false;
		if(var == ASTNode.Kinds.Var || var == ASTNode.Kinds.Value || var == ASTNode.Kinds.ScalarParm){
			isScalar = true;
		}
		
		return isScalar;
	}
	
	public boolean CompareTo(param p){
		if(this.type != p.type)return false;
		
		if(isScalar(this.kind) && isScalar(p.kind)){
			return true;
		}else if(!isScalar(this.kind) && !isScalar(p.kind)){
			return true;
		}
		return false;
	}
}
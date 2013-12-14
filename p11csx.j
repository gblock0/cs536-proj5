	.class	public  p11csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p11csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing program p11csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	"Enter an integer:\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	invokestatic	CSXLib/readInt()I
	istore	0
	ldc	"The integer you entered was "
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	0
	invokestatic	CSXLib/printInt(I)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  1
	.end	method

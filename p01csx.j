	.class	public  p01csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p01csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing Program p01csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	0
	invokestatic	CSXLib/printInt(I)V
	ldc	-123456
	invokestatic	CSXLib/printInt(I)V
	ldc	123456
	invokestatic	CSXLib/printInt(I)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	"Test compeleted\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  0
	.end	method

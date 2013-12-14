	.class	public  p07csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p07csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing program p07csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	10
	istore	0
	ldc	-20
	istore	1
	iload	0
	iload	1
	imul
	istore	2
	iload	2
	iload	0
	idiv
	ldc	1
	iadd
	istore	3
	iload	0
	ldc	1
	isub
	iload	0
	idiv
	ldc	1
	iadd
	istore	4
	ldc	1
	ldc	4
	iadd
	ldc	3
	isub
	ldc	2
	isub
	ldc	1
	isub
	ldc	-10
	isub
	ldc	-9
	iadd
	istore	5
	iload	3
	iload	1
	isub
	istore	6
	iload	2
	iload	1
	idiv
	istore	7
	iload	0
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	1
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	2
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic	CSXLib/printInt(I)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	4
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	5
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	6
	invokestatic	CSXLib/printInt(I)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	7
	invokestatic	CSXLib/printInt(I)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  8
	.end	method

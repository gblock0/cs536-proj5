	.class	public  p16csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p16csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	1
	istore	0
	ldc	1
	istore	1
	ldc	0
	istore	2
	ldc	"Testing program p16csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label1:
	iload	0
	ldc	100
	if_icmple	label3
	ldc	0
	goto	label4
label3:
	ldc	1
label4:
	ifeq	label2
	ldc	1
	istore	1
label5:
	iload	1
	ldc	100
	if_icmple	label7
	ldc	0
	goto	label8
label7:
	ldc	1
label8:
	ifeq	label6
	iload	1
	ldc	1
	iadd
	istore	1
	iload	2
	ldc	1
	iadd
	istore	2
	goto	label5
label6:
	iload	0
	ldc	1
	iadd
	istore	0
	goto	label1
label2:
	ldc	"The value of sum is "
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	2
	invokestatic	CSXLib/printInt(I)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  3
	.end	method

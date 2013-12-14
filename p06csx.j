	.class	public  p06csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p06csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing program p06csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	1
	ifeq	label1
	ldc	"if then stmt works\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label2
label1:
label2:
	ldc	0
	ifeq	label3
	ldc	"if then stmt DOESN'T work\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label4
label3:
label4:
	ldc	1
	ifeq	label5
	ldc	"if then else stmt works\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label6
label5:
	ldc	"if then else stmt DOESN'T work\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label6:
	ldc	0
	ifeq	label7
	ldc	"if then else stmt DOESN'T work\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label8
label7:
	ldc	"if then else stmt works\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label8:
	return
	.limit	stack  25
	.limit	locals  0
	.end	method

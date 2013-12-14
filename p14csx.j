	.class	public  p14csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p14csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing program p14csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	-2147483640
	istore	0
	ldc	2147483647
	istore	1
	iload	0
	iload	1
	if_icmpgt	label1
	ldc	0
	goto	label2
label1:
	ldc	1
label2:
	iload	0
	iload	1
	if_icmpeq	label3
	ldc	0
	goto	label4
label3:
	ldc	1
label4:
	ior
	iload	1
	iload	0
	if_icmplt	label5
	ldc	0
	goto	label6
label5:
	ldc	1
label6:
	ior
	iload	1
	iload	1
	if_icmpne	label7
	ldc	0
	goto	label8
label7:
	ldc	1
label8:
	ior
	ifeq	label9
	ldc	"ERROR: Error in relational operators (integer)\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label10
label9:
label10:
	iload	0
	ldc	0
	imul
	istore	0
	iload	1
	ldc	1
	imul
	istore	1
	ldc	0
	istore	2
	ldc	1
	istore	3
	iload	2
	ldc	1
	if_icmpeq	label11
	ldc	0
	goto	label12
label11:
	ldc	1
label12:
	iload	3
	iload	2
	if_icmpeq	label13
	ldc	0
	goto	label14
label13:
	ldc	1
label14:
	iand
	istore	3
	iload	3
	ifeq	label15
	ldc	"ERROR: In boolean expression\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label16
label15:
label16:
	iload	0
	iload	1
	if_icmplt	label17
	ldc	0
	goto	label18
label17:
	ldc	1
label18:
	iload	0
	ldc	0
	if_icmpeq	label19
	ldc	0
	goto	label20
label19:
	ldc	1
label20:
	iand
	ldc	0
	iand
	istore	2
	iload	2
	ifeq	label21
	ldc	"ERROR: In boolean/integer expression\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label22
label21:
label22:
	ldc	1
	ldc	2
	iadd
	ldc	3
	imul
	ldc	4
	ldc	5
	iadd
	idiv
	istore	0
	ldc	1
	ldc	2
	iadd
	ldc	3
	ldc	4
	ldc	5
	iadd
	idiv
	imul
	istore	1
	iload	0
	ldc	1
	if_icmpne	label23
	ldc	0
	goto	label24
label23:
	ldc	1
label24:
	iload	1
	ldc	0
	if_icmpne	label25
	ldc	0
	goto	label26
label25:
	ldc	1
label26:
	ior
	ifeq	label27
	ldc	"ERROR: Improper Integer division and associativity\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label28
label27:
label28:
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  4
	.end	method

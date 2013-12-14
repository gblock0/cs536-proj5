	.class	public  p09csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p09csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing program p09csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	10
	istore	0
	ldc	-10
	istore	1
	iload	0
	iload	0
	if_icmpeq	label1
	ldc	0
	goto	label2
label1:
	ldc	1
label2:
	istore	2
	iload	0
	iload	0
	if_icmpne	label3
	ldc	0
	goto	label4
label3:
	ldc	1
label4:
	istore	3
	iload	0
	iload	0
	if_icmple	label5
	ldc	0
	goto	label6
label5:
	ldc	1
label6:
	istore	4
	iload	0
	iload	0
	if_icmplt	label7
	ldc	0
	goto	label8
label7:
	ldc	1
label8:
	istore	5
	iload	0
	iload	0
	if_icmpge	label9
	ldc	0
	goto	label10
label9:
	ldc	1
label10:
	istore	6
	iload	0
	iload	0
	if_icmpgt	label11
	ldc	0
	goto	label12
label11:
	ldc	1
label12:
	istore	7
	iload	2
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	4
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	5
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	6
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	7
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	0
	iload	1
	if_icmpeq	label13
	ldc	0
	goto	label14
label13:
	ldc	1
label14:
	istore	2
	iload	0
	iload	1
	if_icmpne	label15
	ldc	0
	goto	label16
label15:
	ldc	1
label16:
	istore	3
	iload	0
	iload	1
	if_icmple	label17
	ldc	0
	goto	label18
label17:
	ldc	1
label18:
	istore	4
	iload	0
	iload	1
	if_icmplt	label19
	ldc	0
	goto	label20
label19:
	ldc	1
label20:
	istore	5
	iload	0
	iload	1
	if_icmpge	label21
	ldc	0
	goto	label22
label21:
	ldc	1
label22:
	istore	6
	iload	0
	iload	1
	if_icmpgt	label23
	ldc	0
	goto	label24
label23:
	ldc	1
label24:
	istore	7
	iload	2
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	4
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	5
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	6
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	iload	7
	invokestatic	CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  8
	.end	method

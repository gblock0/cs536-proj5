	.class	public  p20csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p20csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	"Testing Program p20csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	1
	istore	0
	ldc	2
	istore	1
	ldc	1
	istore	2
	iload	0
	iload	1
	if_icmplt	label1
	ldc	0
	goto	label2
label1:
	ldc	1
label2:
	ifeq	label3
	iload	2
	ldc	1
	iand
	istore	2
	goto	label4
label3:
	ldc	"ERROR: Less Than operator not working\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	0
	istore	2
label4:
	iload	1
	iload	0
	if_icmpgt	label5
	ldc	0
	goto	label6
label5:
	ldc	1
label6:
	ifeq	label7
	iload	2
	ldc	1
	iand
	istore	2
	goto	label8
label7:
	ldc	"ERROR: Greater Than operator not working\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	0
	istore	2
label8:
	iload	0
	iload	1
	if_icmpeq	label9
	ldc	0
	goto	label10
label9:
	ldc	1
label10:
	ifeq	label11
	ldc	"ERROR: Equal operator not working\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	0
	istore	2
	goto	label12
label11:
	iload	2
	ldc	1
	iand
	istore	2
label12:
	iload	0
	iload	1
	if_icmpne	label13
	ldc	0
	goto	label14
label13:
	ldc	1
label14:
	ifeq	label15
	iload	2
	ldc	1
	iand
	istore	2
	goto	label16
label15:
	ldc	"ERROR: Less Than operator not working\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	0
	istore	2
label16:
	iload	2
	ldc	0
	if_icmpeq	label17
	ldc	0
	goto	label18
label17:
	ldc	1
label18:
	ifeq	label19
	ldc	"ERRORS found in program\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label20
label19:
label20:
	ldc	"Test compeleted\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  3
	.end	method

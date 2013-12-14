	.class	public  p19csx
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	p19csx/main()V
	return
	.limit	stack  2
	.end	method
	.method	public static  main()V
	ldc	1
	istore	0
	ldc	9
	istore	1
	ldc	1
	istore	2
	ldc	0
	istore	3
	ldc	"Testing Program p19csx\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	1
	istore	4
	ldc	9
	istore	5
	ldc	1
	istore	6
	ldc	0
	istore	7
	ldc	0
	istore	8
	iload	3
	ifeq	label1
	ldc	"\n====> Wrong Control Flow 1\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label2
label1:
	iload	7
	iload	6
	ior
	ifeq	label3
	iload	8
	ldc	1
	iadd
	istore	8
	iload	7
	iload	4
	ldc	1
	if_icmpne	label4
	ldc	0
	goto	label5
label4:
	ldc	1
label5:
	if_icmpne	label6
	ldc	0
	goto	label7
label6:
	ldc	1
label7:
	ifeq	label8
	ldc	"===> Wrong Control Flow 2\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	ldc	20
	ldc	10
	if_icmpgt	label9
	ldc	0
	goto	label10
label9:
	ldc	1
label10:
	ifeq	label11
	iload	3
	iload	4
	ldc	0
	if_icmpne	label12
	ldc	0
	goto	label13
label12:
	ldc	1
label13:
	iand
	ifeq	label14
	ldc	"\n ==> Wrong Control Flow 3\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label15
label14:
	iload	5
	iload	0
	iload	4
	isub
	if_icmplt	label16
	ldc	0
	goto	label17
label16:
	ldc	1
label17:
	ifeq	label18
	ldc	"\n===> Wrong Control Flow 4\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label19
label18:
label19:
label15:
	goto	label20
label11:
	ldc	"\n===> Wrong Control Flow 5\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label20:
	goto	label21
label8:
	ldc	1
	ifeq	label22
	iload	5
	ldc	9
	if_icmpeq	label23
	ldc	0
	goto	label24
label23:
	ldc	1
label24:
	iload	3
	ldc	1
	if_icmpne	label25
	ldc	0
	goto	label26
label25:
	ldc	1
label26:
	iand
	ifeq	label27
	iload	8
	ldc	1
	iadd
	istore	8
	goto	label28
label27:
	ldc	"\n===> Wrong Flow Control 6\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label28:
	goto	label29
label22:
	ldc	"\n==> Wrong Flow Control 7\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
label29:
label21:
	goto	label30
label3:
label30:
label2:
	iload	8
	ldc	2
	if_icmpne	label31
	ldc	0
	goto	label32
label31:
	ldc	1
label32:
	ifeq	label33
	ldc	"\n ERROR : Incorrect paths followed \n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	goto	label34
label33:
label34:
	ldc	"Test compeleted\n"
	invokestatic	CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  9
	.end	method

## Test 43
  Created: Dec 2002

 Tests simple break, continue uses

##

class p43csx {


 void main() {
  int a = 0;

  label1: while (a < 10) {
    if(a == 5) {
      print("break at a = ",a,"\n");
      break label1;
    }
    else {
    	a = a + 1;
	continue label1;
    }
  }
  a = 0;
  label2: while (a < 10) {
    if(a == 5) {
      a = a + 1;
      continue label2;
    }
    print("a = ",a,"\n");
    a = a + 1;
  }
  print("a = 5 should not have been written","\n");

  print("Test completed", "\n");

 }
}

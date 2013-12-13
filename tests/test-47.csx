## Test 47
  Created: Dec 2002

 Tests array lengths in array copies 

##

class p47csx {


 void main() {
   int myIntArray1[10];
   int myIntArray2[10];
   int i = 0;
   while(i < 10) {
     myIntArray1[i] = i;
     i = i + 1;
   }
   myIntArray2 = myIntArray1;
   i = 0;
   while(i < 10) {
     print("myIntArray2[",i,"] = ",myIntArray2[i],"\n");
     i = i + 1;
   }
 }
}

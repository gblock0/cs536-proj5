## Test 44
  Created: Dec 2002

 Tests char arithmetic 

##

class p44csx {


 void main() {
   char ch1 = 'a';
   char ch2 = 'b';

   if(ch1 < ch2) {
     print("This should print","\n");
   }

   ch1 = (char)(ch1 + 1);
   if(ch1 == ch2) {
     print("This should also print","\n");
   }
 }
}

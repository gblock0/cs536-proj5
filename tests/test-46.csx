## Test 46
  Created: Dec 2002

 Tests array assignment 

##

class p46csx {


 void main() {
   int myIntArray[10];
   char myCharArray[5];
   bool myBoolArray[3];
   int i = 0;
   
   while(i < 10) {
     myIntArray[i] = i;
     print("myIntArray[",i,"] = ",myIntArray[i],"\n");
     i = i + 1;
   }
   myCharArray[0] = 'a';
   myCharArray[1] = 'b';
   myCharArray[2] = 'c';
   myCharArray[3] = 'd';
   myCharArray[4] = 'e';
   i = 0;
   while(i < 5) {
     print("myCharArray[",i,"] = ",myCharArray[i],"\n");
     i = i + 1;
   }
   myBoolArray[0] = false;
   myBoolArray[1] = true;
   myBoolArray[2] = false;
   i = 0;
   while(i < 3) {
     if(myBoolArray[i] == true)
       print("This should only print once","\n");
     i = i + 1;
   }
 }
}

// //The user has to guess a number from 0 - x.
// //output whether or not the person is correct

// #include <stdio.h>
// #include <stdbool.h>
// #include <stdlib.h>
// #include <time.h>

// int main()
// {
//   //pseudo random number generator
//   //this gets seeded
//   //outputs a result
//   //seed - from the clock 

//   srand(time(NULL));
//   int randomNumber = rand() % 5;

//   //printf("%d\n", randomNumber);

//   //modulus operator %
//   //remainder of some division

//   printf("please pick a number 0 -5. If you guess right, you win\n");
//   printf("Guess wrong and face your fate ...");
  
//   int guess;
//   scanf("%d", &guess);

//   printf("you guessed %d\n", guess);
//   printf("The correct answer was %d\n", randomNumber);

//   if(guess == randomNumber)
//   {
//       printf("you win!\n");
//   }else
//   {
//       printf("you lost!\n");
//   }

//   printf("Thank you for playing!\n");
//   return 0;
// }


/* Correction of that code above */
//The user has to guess a number from 0 - x.
//output whether or not the person is correct
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

int main()
{
  // pseudo random number generator
  // this gets seeded
  // outputs a result
  // seed - from the clock

  srand(time(NULL));
  int randomNumber = rand() % 5;

  printf("Please pick a number from 0 to 5. If you guess right, you win.\n");
  printf("Guess wrong and face your fate ...\n");

  int guess;
  if (scanf("%d", &guess) != 1) {
      printf("Invalid input. Please enter a valid number.\n");
      return 1;
  }

  printf("You guessed %d\n", guess);
  printf("The correct answer was %d\n", randomNumber);

  if (guess == randomNumber) {
      printf("You win!\n");
  } else {
      printf("You lost!\n");
  }

  printf("Thank you for playing!\n");
  return 0;
}
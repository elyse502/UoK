#include <stdio.h>
#include <stdlib.h>

int main()
{
    printf("In this Game you're going to guess any Number between 1-10.You have only 3 chances, Let's go...\n");
    int secretNumber = 5;
    int guess;
    int guessCount = 0;
    int guessLimit = 3;
    int outOfGuesses = 0;

    while (guess != secretNumber && outOfGuesses == 0)
    {
        if(guessCount < guessLimit)
        {
        printf("Enter a number: ");
        scanf("%d", &guess);
        guessCount++;
        } else
        {
            outOfGuesses = 1;
        }
    }
    if (outOfGuesses == 1)
    {
        printf("Sorry! You're Out of Guesses, Try again next Time.\n");
    } else
    {
        printf("Congrats! You win the Game.\n");
    }
    
    return 0;
}
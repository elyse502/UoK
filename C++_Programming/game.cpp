/*
 * File: game.cpp
 * Auth: Elysée NIYIBIZI
 */

#include <iostream>
/**
 * main - Prints a wholesome game!
 *
 * Return: Always 0.
 */

using namespace std;

int main() {
    int menu;

    cout << "Welcome to the Treasure Quest!\n";
    cout << "You are on a quest to find the hidden treasure." << endl;

    cout << "\nYou are at a crossroad. Where do you want to go?\n";
    cout << "1. Go left\n2. Go right\n3. Go straight" << endl;
    cin >> menu;

    switch (menu) {
        case 1:
            cout << "\nYou chose to go left. You encounter a dangerous river!\n";
            cout << "1. Try to swim across\n2. Look for a bridge\n";
            int left;
            cin >> left;
            switch (left) {
                case 1:
                    cout << "\nYou attempt to swim across, but the current is too strong.\n";
                    cout << "You are swept away by the river. Game over!" << endl;
                    break;
                case 2:
                    cout << "\nYou find a sturdy bridge and safely cross the river.\n";
                    cout << "Congratulations! You have successfully crossed the river." << endl;
                    cout << "You come across a mysterious cave. Do you want to enter?\n";
                    cout << "1. Enter the cave\n2. Continue on your path" << endl;
                    int choiceMade;
                    cin >> choiceMade;
                    switch (choiceMade) {
                        case 1:
                            cout << "\nYou enter the dark cave. After navigating through some twists and turns,\n";
                            cout << "You discover a hidden treasure chest!\nCongratulations! You found the hidden treasure. You win!" << endl;
                            break;
                        case 2:
                            cout << "\nYou decide not to enter the cave and continue on your path.\n";
                            cout << "Unfortunately, you miss the hidden treasure. Game over!" << endl;
                            break;
                        default:
                            cout << "\nWrong choice! Try again..." << endl;
                    }
                    break;
                default:
                    cout << "\nWrong choice! Try again..." << endl;
            }
            break;
        case 2:
            cout << "\nYou chose to go right. You stumble upon a group of hostile bandits!\n";
            cout << "1. Attempt to fight them\n2. Run away" << endl;
            int right;
            cin >> right;
            switch (right) {
                case 1:
                    cout << "\nYou bravely fight the bandits, but they overpower you.\n";
                    cout << "You are captured. Game over!" << endl;
                    break;
                case 2:
                    cout << "\nYou run away from the bandits and escape to safety.\n";
                    cout << "You find yourself in front of an ancient temple.\n";
                    cout << "Do you want to enter the temple?\n";
                    cout << "1. Enter the temple\n2. Continue on your path" << endl;
                    int choiceMade1;
                    cin >> choiceMade1;
                    switch (choiceMade1) {
                        case 1:
                            cout << "\nYou enter the ancient temple and discover a hidden treasure chest!\n";
                            cout << "Congratulations! You found the hidden treasure. You win!" << endl;
                            break;
                        case 2:
                            cout << "\nYou decide not to enter the temple and continue on your path.\n";
                            cout << "Unfortunately, you miss the hidden treasure. Game over!" << endl;
                            break;
                        default:
                            cout << "\nWrong Choice! Try again..." << endl;
                    }
                    break;
            }
            break;
        case 3:
            cout << "\nYou chose to go straight. You encounter a deep forest!\n";
            cout << "1. Try to find a path through the forest\n2. Turn back and choose another path" << endl;
            int straight;
            cin >> straight;
            switch (straight) {
                case 1:
                    cout << "\nYou navigate through the dense forest and get lost.\n";
                    cout << "You cannot find your way out. Game over!" << endl;
                    break;
                case 2:
                    cout << "\nYou turn back and choose another path.\n";
                    cout << "You find yourself at the crossroad again.\n";
                    cout << "Choose another path wisely!" << endl;
                    break;
                default:
                    cout << "\nWrong Choice! Try again..." << endl;
            }
            break;
        default:
            cout << "\nWrong Choice! Try again..." << endl;
    }

    return 0;
}

#include <iostream>

using namespace std;

int main() {
  string phrase = "Giraffe Academy";
  cout << phrase << endl;
  string phrasesub;
  phrasesub = phrase.substr(8, 3);
  cout << phrasesub << endl;
  cout << "The LENGTH of phrace = " << phrase.length() << endl;
  cout << "The Word \'Academy\' is found: " << phrase.find("Academy", 0) << endl;
  phrase[0] = 'B';
  cout << phrase << endl;
  return 0;
}
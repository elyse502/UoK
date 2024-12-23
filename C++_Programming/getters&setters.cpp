#include <iostream>
using namespace std;

class Movie {
  private:
    string rating;
  public:
    string tittle;
    string director;
    Movie(string aTittle, string aDirector, string aRating) {
      tittle = aTittle;
      director = aDirector;
      setRating(aRating);
    }

    void setRating(string aRating) {
      if (aRating == "G" || aRating == "PG" || aRating == "PG-13" || aRating == "R" || aRating == "NR") {
          rating = aRating;
        } else {
          rating = "NR";
      }
    }
      

    string getRating() {
      return rating;
    }
};

int main() {

  Movie avengers("The Avengers", "Joss Whedon", "PG-13");
  cout << avengers.getRating() << endl;

  avengers.setRating("Dog"); // Trying to modify the rating to something that is not G, PG, PG-13, R, or NR.
  cout << avengers.getRating() << endl;
  
  return 0;
}
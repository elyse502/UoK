#include <iostream>
using namespace std;

// Curve parameters: y^2 = x^3 + ax + b (mod p)
const int a = 0;
const int p = 19;

// Structure for elliptic curve point
struct Point {
    int x, y;
    bool infinity;
};

// Brute-force modular inverse
int InverseModulo(int u, int m) {
    for (int n = 1; n < m; n++) {
        if ((u * n) % m == 1)
            return n;
    }
    return 0;
}

// Point addition function
Point addPoints(Point P, Point Q) {
    Point R;
    int lambda;

    if (P.infinity) return Q;
    if (Q.infinity) return P;

    // P + (-P) = infinity
    if (P.x == Q.x && (P.y + Q.y) % p == 0) {
        R.infinity = true;
        return R;
    }

    // Point doubling
    if (P.x == Q.x && P.y == Q.y) {
        if (P.y == 0) {
            R.infinity = true;
            return R;
        }
        lambda = ((3 * P.x * P.x + a) *
                  InverseModulo(2 * P.y, p)) % p;
    }
    else {
        int dy = (Q.y - P.y + p) % p;
        int dx = (Q.x - P.x + p) % p;

        if (dx == 0) {
            R.infinity = true;
            return R;
        }

        lambda = (dy * InverseModulo(dx, p)) % p;
    }

    R.x = (lambda * lambda - P.x - Q.x) % p;
    R.x = (R.x + p) % p;

    R.y = (lambda * (P.x - R.x) - P.y) % p;
    R.y = (R.y + p) % p;

    R.infinity = false;
    return R;
}

int main() {
    Point P, Q, R;

    cout << "Enter coordinates for point P:\n";
    cout << "x: ";
    cin >> P.x;
    cout << "y: ";
    cin >> P.y;
    P.infinity = false;

    cout << "\nEnter coordinates for point Q:\n";
    cout << "x: ";
    cin >> Q.x;
    cout << "y: ";
    cin >> Q.y;
    Q.infinity = false;

    R = addPoints(P, Q);

    cout << "\nResult of P + Q:\n";
    if (R.infinity)
        cout << "Point at infinity\n";
    else
        cout << "R = (" << R.x << ", " << R.y << ")\n";

    return 0;
}


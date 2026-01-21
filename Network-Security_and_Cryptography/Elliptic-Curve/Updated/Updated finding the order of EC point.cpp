#include <iostream>
using namespace std;

// Curve parameters
const int a = 0;
const int b = 1;
const int p = 19;

// Point structure
struct Point {
    int x, y;
    bool infinity;
};

// Brute-force modular inverse
int InverseModulo(int u, int m) {
    u = u % m;
    for (int n = 1; n < m; n++) {
        if ((u * n) % m == 1)
            return n;
    }
    return 0;
}

// Add two points
Point addPoints(Point P, Point Q) {
    Point R;

    if (P.infinity) return Q;
    if (Q.infinity) return P;

    int lambda;

    // P + (-P) = infinity
    if (P.x == Q.x && (P.y + Q.y) % p == 0) {
        R.infinity = true;
        return R;
    }

    if (P.x == Q.x && P.y == Q.y) {
        // Doubling
        if (P.y == 0) {
            R.infinity = true;
            return R;
        }
        lambda = ((3 * P.x * P.x + a) * InverseModulo(2 * P.y, p)) % p;
        lambda = (lambda + p) % p;
    }
    else {
        int dy = (Q.y - P.y + p) % p;
        int dx = (Q.x - P.x + p) % p;

        if (dx == 0) { // vertical line
            R.infinity = true;
            return R;
        }

        lambda = (dy * InverseModulo(dx, p)) % p;
        lambda = (lambda + p) % p;
    }

    R.x = (lambda * lambda - P.x - Q.x) % p;
    R.x = (R.x + p) % p;

    R.y = (lambda * (P.x - R.x) - P.y) % p;
    R.y = (R.y + p) % p;

    R.infinity = false;
    return R;
}

int main() {
    int Gx, Gy;
    cout << "Enter x-coordinate of the point: ";
    cin >> Gx;
    cout << "Enter y-coordinate of the point: ";
    cin >> Gy;

    Point P = {Gx, Gy, false};
    Point W = P;
    int order = 1;

    while (true) {
        W = addPoints(W, P);
        order++;

        if (W.infinity) {
            cout << "The order of point (" << Gx << "," << Gy << ") is " << order << endl;
            break;
        }

        cout << order << " * (" << Gx << "," << Gy << ") = (" << W.x << "," << W.y << ")\n";

        if (order > p + 1) { // safety bound
            cout << "Order not found.\n";
            break;
        }
    }

    return 0;
}


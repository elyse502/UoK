#include <iostream>
using namespace std;

// Elliptic curve parameters: y^2 = x^3 + ax + b mod p
int a, b, p; // will ask user input for generality

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
    return -1; // no inverse
}

// Add two points on the elliptic curve
Point addPoints(Point P, Point Q) {
    Point R;
    int lambda, dx, dy;

    if (P.infinity) return Q;
    if (Q.infinity) return P;

    if (P.x == Q.x && (P.y + Q.y) % p == 0) {
        R.infinity = true;
        return R;
    }

    if (P.x == Q.x && P.y == Q.y) {
        if (P.y == 0) {
            R.infinity = true;
            return R;
        }
        lambda = ((3 * P.x * P.x + a) * InverseModulo(2 * P.y, p)) % p;
        lambda = (lambda + p) % p;
    } else {
        dy = (Q.y - P.y + p) % p;
        dx = (Q.x - P.x + p) % p;

        if (dx == 0) {
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

// Convert integer message to elliptic curve point
Point messageToPoint(int Mx) {
    int My = -1;
    bool found = false;

    for (int t = 0; t < p; t++) {
        if ((t * t) % p == (Mx * Mx * Mx + a * Mx + b) % p) {
            My = t;
            found = true;
            break;
        }
    }

    if (!found) {
        cout << "ATTENTION! " << Mx << " does not correspond to a point on the curve." << endl;
        return {0, 0, true}; // point at infinity
    }

    return {Mx, My, false};
}

int main() {
    int message;

    // Ask user for curve parameters
    cout << "Enter elliptic curve parameter a: "; cin >> a;
    cout << "Enter elliptic curve parameter b: "; cin >> b;
    cout << "Enter prime modulus p: "; cin >> p;

    cout << "Enter integer message to be encrypted: ";
    cin >> message;

    Point M = messageToPoint(message);
    if (M.infinity) return 0;

    cout << "Message as elliptic curve point: (" << M.x << ", " << M.y << ")\n";

    // Public point of receiver
    int Gx, Gy;
    cout << "Enter x-coordinate of receiver's public point: "; cin >> Gx;
    cout << "Enter y-coordinate of receiver's public point: "; cin >> Gy;
    Point P = {Gx, Gy, false};

    // Encrypt: C = M + P
    Point C = addPoints(M, P);
    cout << "Encrypted message point: (" << C.x << ", " << C.y << ")\n";

    // Decrypt: M = C - P
    // In ECC, subtraction: M = C + (-P)
    Point negP = {P.x, (p - P.y) % p, false};
    Point D = addPoints(C, negP);

    cout << "Decrypted message point: (" << D.x << ", " << D.y << ")\n";
    cout << "Recovered integer message: " << D.x << endl;

    return 0;
}


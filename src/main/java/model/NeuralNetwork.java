package model;

import java.util.Random;

/**
 * Created by odin on 07.06.15.
 */
public class NeuralNetwork {


    double lr=5.9; // learning rate
    double[] d={20.90/20.90, 20.88/20.90, 20.73/20.90, 20.40/20.90}; // wzorzec na wyjściu sieci

    //notowania z dnia	06.04	07.04	08.04	09.04	10.04
    //-----------------------------------------------------------
    //4FUNMEDIA (4FM)	14.25	14.30	14.30	14.50	13.60
    //TVN (TVN)		    16.90	16.95	16.65	16.70	16.83
    //CYFRPLSAT (CPS)	20.62	20.90	20.88	20.73	20.40
    //TOYA (TOA)		4.95	5.00	5.10	5.00	5.05

    int n=100; // liczba kroków obliczeniowych w procesie uczenia się sieci
    // deklaracje wag
    double[] w111= new double[101]; // pierwszej warstwy, pierwszej jednostki, pierwszego wejscia
    double[] w112= new double[101];
    double[] w121= new double[101];
    double[] w122= new double[101];

    double[] w21= new double[101]; // drugiej warstwy, pierwszego wejscia
    double[] w22= new double[101];

    double[] beta= new double[100];
    double[] beta21= new double[100];
    double[] beta22= new double[100];
    double[] bb= new double[100]; // wektor pomocniczy

    // wektory dla wag skośnych
    double[]  w1s2= new double[101]; // wejście pierwsze, jednostki drugiej
    double[] w2s2= new double[101];
    double[] w3s1= new double[101];
    double[] w4s1= new double[101];

    double[] dw111= new double[100]; // wartość o jaką zmieni się waga w11 w danym kroku obliczeniowym
    double[] dw112= new double[100];
    double[] dw121= new double[100];
    double[] dw122= new double[100];

    double[] dw1s2= new double[100]; // wektor dla wagi, które łączy wejscie pierwsze, z jednostką drugą
    double[] dw2s2= new double[100];
    double[] dw3s1= new double[100];
    double[] dw4s1= new double[100];

    double[] dw21= new double[100]; // wektor dla zmiany wagi w21
    double[] dw22= new double[100];

    double[] blad= new double[100]; // przyjmujemy błąd średniokwadratowy
    // czyli błąd = suma((d-u)*(d-u))
    double[] u1= new double[4];
    double[] u2= new double[4];
    double[] u3= new double[4];   // sygnał u3 będzie miał cztery notowania cyfrowego polsatu
    double[] u4= new double[4];

    double[] yy21 = new double[101];
    double z11 = 0;
    double z12 = 0;
    double z21 = 0;
    double z22 = 0;
    double s11 = 0;
    double s12 = 0;
    double s21 = 0;
    double s22 = 0;
    double zt11 = 0;
    double zt12 = 0;
    double zt21 = 0;
    double zt22 = 0;
    double st11 = 0;
    double st12 = 0;
    double st21 = 0;
    double st22 = 0;

    double wt111 =0; // wartość wagi w kroku 101
    double wt112 =0;
    double wt121 =0;
    double wt122 =0;
    double wt1s2 =0;
    double wt2s2 =0;
    double wt3s1 =0;
    double wt4s1 =0;
    double wt21 = 0;
    double wt22 = 0;

    //notowania z dnia	07.04	08.04	09.04	10.04
    //-------------------------------------------------
    //4FUNMEDIA (4FM)	14.25	14.30	14.30	14.50
    //TVN (TVN)		    16.90	16.95	16.65	16.70
    //CYFRPLSAT (CPS)	20.62	20.90	20.88	20.73
    //TOYA (TOA)		4.95	5.00	5.10	5.00

    public void start() {
        u1[0] = 14.25 / 14.50;   // określamy [z]ormalizowaną) wartość sygnału pierwszego
        u2[0] = 16.90 / 16.95;
        u3[0] = 20.62 / 20.90;
        u4[0] = 4.95 / 5.10;
        u1[1] = 14.30 / 14.50;
        u2[1] = 16.95 / 16.95;
        u3[1] = 20.90 / 20.90;
        u4[1] = 5.0 / 5.10;

        u1[2] = 14.30 / 14.50;
        u2[2] = 16.65 / 16.95;
        u3[2] = 20.88 / 20.90;
        u4[2] = 5.10 / 5.10;

        u1[3] = 14.50 / 14.50;
        u2[3] = 16.70 / 16.95;
        u3[3] = 20.73 / 20.90;
        u4[3] = 5.0 / 5.10;

        w111[0] = 0.74; // arbitralnie określamy wstępne wartości wag
        w112[0] = 0.52;
        w121[0] = 1.21;
        w122[0] = 0.33;

        w1s2[0] = 0.7; // arbitralnie określamy wstępnie wartość wag skośnych
        w2s2[0] = 0.5;
        w3s1[0] = 3.1;
        w4s1[0] = 0.4;

        w21[0] = 0.9; // arbitralnie określamy wstępnie wartość wag w drugiej warstwie
        w22[0] = 0.83;

        //for i = 1:n // pętla for wykonująca się sto razy

        for (int i = 0; i < 100; i++) {
            dw111[i] = 0; // przed każdym krokiem zerujemy przyrost wag
            dw112[i] = 0;
            dw121[i] = 0;
            dw122[i] = 0;

            dw1s2[i] = 0;
            dw2s2[i] = 0;
            dw3s1[i] = 0;
            dw4s1[i] = 0;

            dw21[i] = 0;
            dw22[i] = 0;

            bb[i] = 0; // przed każdym krokiem zerujemy wektory pomocnicze
            yy21[i] = 0;

            //for j = 1:4 // pętla for wykonująca się cztery razy
            for (int j = 0; j < 3; j++) {
                z11 = (u1[j] * w111[i] + u2[j] * w112[i] + u3[j] * w3s1[i] + u4[j] * w4s1[i]); // sygnał po sumowaniu w węźle sumacyjnym przed funkcją przejścia
                s11 = (1 / (1 + Math.exp(-z11))); // sygnał po funkcji przejścia
                z12 = (u3[j] * w121[i] + u4[j] * w112[i] + u1[j] * w1s2[i] + u2[j] * w2s2[i]);
                s12 = (1 / (1 + Math.exp(-z12)));

                z21 = (s11 * w21[i] + s12 * w22[i]);
                s21 = (1 / (1 + Math.exp(-z21)));

                bb[i] = bb[i] + 1 / 2 * (Math.pow(s21 - d[j], 2)); // obliczany jest błąd średniokwadratowy

                yy21[i] = yy21[i] + s21 * (1 - s21); // sumujemy wartości pochodnych

                beta[i] = s21 - d[j]; //  pochodna błędu
                dw21[i] = dw21[i] + s11 * (s21 * (1 - s21)) * beta[i];
                dw22[i] = dw22[i] + s12 * (s21 * (1 - s21)) * beta[i];
                beta21[i] = w21[i] * (s21 * (1 - s21)) * beta[i]; // błąd w środku sieci rzutowany do wyjścia w torze sygnału wagi w21
                beta22[i] = w22[i] * (s21 * (1 - s21)) * beta[i];

                dw111[i] = dw111[i] + u1[j] * (s11 * (1 - s11)) * beta21[i]; //obliczamy zmiany wag w pierwszej warstwie
                dw112[i] = dw112[i] + u2[j] * (s11 * (1 - s11)) * beta21[i];
                dw121[i] = dw121[i] + u3[j] * (s12 * (1 - s12)) * beta22[i];
                dw122[i] = dw122[i] + u4[j] * (s12 * (1 - s12)) * beta22[i];

                dw1s2[i] = dw1s2[i] + u1[j] * (s12 * (1 - s12)) * beta22[i];
                dw2s2[i] = dw2s2[i] + u2[j] * (s12 * (1 - s12)) * beta22[i];
                dw3s1[i] = dw3s1[i] + u3[j] * (s11 * (1 - s11)) * beta21[i];
                dw4s1[i] = dw4s1[i] + u4[j] * (s11 * (1 - s11)) * beta21[i];

            }// koniec pętli wykonującej się cztery razy

            blad[i] = bb[i];

            w21[i + 1] = w21[i] - lr * dw21[i];
            w22[i + 1] = w22[i] - lr * dw22[i];

            w111[i + 1] = w111[i] - lr * dw111[i]; // forsowanie procesu uczenia
            w112[i + 1] = w112[i] - lr * dw112[i];
            w121[i + 1] = w121[i] - lr * dw121[i];
            w122[i + 1] = w122[i] - lr * dw122[i];

            w1s2[i + 1] = w1s2[i] - lr * dw1s2[i];
            w2s2[i + 1] = w2s2[i] - lr * dw2s2[i];
            w3s1[i + 1] = w3s1[i] - lr * dw3s1[i];
            w4s1[i + 1] = w4s1[i] - lr * dw4s1[i];

        } // koniec epoki

        wt111 = w111[100]; // wartość wagi w kroku 101
        wt112 = w112[100];
        wt121 = w121[100];
        wt122 = w122[100];
        wt1s2 = w1s2[100];
        wt2s2 = w2s2[100];
        wt3s1 = w3s1[100];
        wt4s1 = w4s1[100];
        wt21 = w21[100];
        wt22 = w22[100];

        double[] ut1 = new double[4]; // sygnał ut1 będzie miał 4 elementy
        double[] ut2 = new double[4];
        double[] ut3 = new double[4];
        double[] ut4 = new double[4];


                //Notowania z dnia 		07.04	08.04	09.04	10.04
                //4FUNMEDIA (4FM)		14.30	14.30	14.50	13.60
                //TVN (TVN)			16.95	16.65	16.70	16.83
                //CYFRPLSAT (CPS)		20.90	20.88	20.73	20.40
                //TOYA (TOA)			5.00	5.10	5.00	5.05

        ut1[0] = 14.30 / 14.50; // tu opkreślamy wartość sygnału
        ut2[0] = 16.95 / 16.95;
        ut3[0] = 20.90 / 20.90;
        ut4[0] = 5.0 / 5.10;    // czwarty element pierwszego sygnału

        ut1[1] = 14.30 / 14.50;
        ut2[1] = 16.65 / 16.95;
        ut3[1] = 20.88 / 20.90;
        ut4[1] = 5.10 / 5.10;

        ut1[2] = 14.50 / 14.50;
        ut2[2] = 16.70 / 16.95;
        ut3[2] = 20.73 / 20.90;
        ut4[2] = 5.0 / 5.10;

        ut1[3] = 13.60 / 14.50;
        ut2[3] = 16.83 / 16.95;
        ut3[3] = 20.40 / 20.90;
        ut4[3] = 5.05 / 5.10;

                // liczymy pierwszą sumę
        zt11 = ut1[0] * wt111 + ut2[0] * wt112 + ut3[0] * wt3s1 + ut4[0] * wt4s1;
        st11 = (1 / (1 + Math.exp(-zt11)));
        zt12 = ut3[0] * wt121 + ut4[0] * wt122 + ut1[0] * wt1s2 + ut2[0] * wt2s2;
        st12 = (1 / (1 + Math.exp(-zt12)));

        zt21 = st11 * wt21 + st12 * wt22;
        st21 = (1 / (1 + Math.exp(-zt21)));

        // liczymu sume dla wektora drugiego
        z11 = ut1[1] * wt111 + ut2[1] * wt112 + ut3[1] * wt3s1 + ut4[1] * wt4s1;
        st11 = (1 / (1 + Math.exp(-zt11)));
        zt12 = ut3[1] * wt121 + ut4[1] * wt122 + ut1[1] * wt1s2 + ut2[1] * wt2s2;
        st12 = (1 / (1 + Math.exp(-zt12)));

        zt21 = st11 * wt21 + st12 * wt22;
        st21 = (1 / (1 + Math.exp(-zt21)));

        // liczymy sume dla wektora trzeciego
        zt11 = ut1[2] * wt111 + ut2[2] * wt112 + ut3[2] * wt3s1 + ut4[2] * wt4s1;
        st11 = (1 / (1 + Math.exp(-zt11)));
        zt12 = ut3[2] * wt121 + ut4[2] * wt122 + ut1[2] * wt1s2 + ut2[2] * wt2s2;
        st12 = (1 / (1 + Math.exp(-zt12)));

        zt21 = st11 * wt21 + st12 * wt22;
        st21 = (1 / (1 + Math.exp(-zt21)));

        // liczymy sume dla wektora danych testowych

        zt11 = ut1[3] * wt111 + ut2[3] * wt112 + ut3[3] * wt3s1 + ut4[3] * wt4s1;
        st11 = (1 / (1 + Math.exp(-zt11)));
        zt12 = ut3[3] * wt121 + ut4[3] * wt122 + ut1[3] * wt1s2 + ut2[3] * wt2s2;
        st12 = (1 / (1 + Math.exp(-zt12)));

        zt21 = st11 * wt21 + st12 * wt22;
        st21 = (1 / (1 + Math.exp(-zt21)));

        st21 = st21 * 20.90; // wynik na wyjściu z sieci denormalizujemy

        System.out.println("Wynik pisanej sieci "+st11*14.50);
        System.out.println("Wynik pisanej sieci "+st12*16.95);
        System.out.println("Wynik pisanej sieci "+st21*20.90);
        System.out.println("Wynik pisanej sieci "+st22*5.10);
// oczekiwany wynik testu 20.63 (notowania z dnia 11.04.2014)
// wynik testu 20.6303
// SUKCES!
            }
        }




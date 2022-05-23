package com.example.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import Mates.Calculadora;

/**
 * Referencia del codigo original
 *          https://download.code-projects.org/details/4216e408-57a4-42f5-ae90-6a995f528785
 * Ha sido modificado y se le ha añadido el paquete Mates que amplia la aplicación
 */

public class MainActivity extends AppCompatActivity {
    boolean isNewOperator = true;
    EditText edt1;
    String expresion="";

    //TODO CONFIGURAR LA PASARELA DE PAGO SIGUIENDO ESTA REFERENCIA https://www.miguelangeljulvez.com/servicios/desarrollo-software/easyredsys

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = findViewById(R.id.editText);
    }


    public void ExpresionEvent(View view) {

        if(edt1.getText().toString()=="0") edt1.setText("");
        String expresion = edt1.getText().toString();



        if(view.getId() == R.id.btnOne){
            expresion += "1";
        }
        else if(view.getId() == R.id.btnTwo){
            expresion += "2";
        }
        else if(view.getId() == R.id.btnThree){
            expresion += "3";
        }
        else if(view.getId() == R.id.btnFour){
            expresion += "4";
        }
        else if(view.getId() == R.id.btnFive){
            expresion += "5";
        }
        else if(view.getId() == R.id.btnSix){
            expresion += "6";
        }
        else if(view.getId() == R.id.btnSeven){
            expresion += "7";
        }
        else if(view.getId() == R.id.btnEight){
            expresion += "8";
        }
        else if(view.getId() == R.id.btnNine){
            expresion += "9";
        }
        else if(view.getId() == R.id.btnZero){
            expresion += "0";
        }
        else if(view.getId() == R.id.btnDot){
            expresion += ".";
        }
        else if(view.getId() == R.id.btnMultiply){
            expresion += "*";
        }
        else if(view.getId() == R.id.btnMinus){
            expresion += "-";
        }
        else if(view.getId() == R.id.btnPlus){
            expresion += "+";
        }
        else if(view.getId() == R.id.btnDivide){
            expresion += "/";
        }
        else if(view.getId() == R.id.btnPower){
            expresion += "^";
        }
        else if(view.getId() == R.id.btnPar1){
            expresion += "(";}

        else if(view.getId() == R.id.btnPar2){
            expresion += ")";
        }

        edt1.setText(expresion);
    
    }

    @SuppressLint("NewApi")
    public void equalEvent(View view) {
        double output = 0.0;
        output= Calculadora.evaluar(edt1.getText().toString());
        edt1.setText(output+"");
    }


    public void clearEvent(View view) {
        edt1.setText("0");
    }

}
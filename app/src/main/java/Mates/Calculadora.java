package Mates;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta es una clase que se utiliza para hacer calculos
 * @author Bazz
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class Calculadora {
    /**
     * Lista de distintos operadores
     */
    private  static List<Character> operadorez = new ArrayList<>();
    static{
        operadorez.add('(');
        operadorez.add(')');
        operadorez.add('/');
        operadorez.add('*');
        operadorez.add('-');
        operadorez.add('+');
        operadorez.add('^');
    }

    /**
     * Calcula una expresion matematica
     * @param expr Un String con la operacion a realizar
     * @return El resultado final
     */
    @SuppressLint("NewApi")
    public static double evaluar(String expr){

        //TODO comprobar errores
        //comprobarErrores(expr);



        char[] expresion=expr.toCharArray();
        List<Double> valor=new ArrayList<>();
        List<Character> operador=new ArrayList<>();
        String numero="";
        int cont=0;

        for (char x:expresion) { //Se recorre la expresion a evaluar para separar en dos listas los valores y los operadores
            if(operadorez.contains(x)) {
                if(cont==0&&numero==""&&x=='-'){
                    valor.add(-1.0);
                    operador.add('*');
                }else if(operador.size()>0&&operador.get(cont)=='('&&x=='-'){
                    numero+=x;
                }else if(operador.size()>0&&operador.get(cont)=='-'&&x=='('){
                    valor.add(-1.0);
                    operador.add('*');
                    operador.add('(');
                    numero = "";
                }else {
                    if (!numero.isEmpty()) valor.add(Double.parseDouble(numero)); //Si la variable numero tiene algun valor almacenado se añade a la lista de valores (El valor anterior al operador)
                    operador.add(x); //Se añáde el operador a la lista de valores
                    numero = ""; //Se vacia la variable numero para almacenar el siguiente
                }
                cont++;
            }else{
                numero+=x; //Se almacena en un String los caracteres correspondientes a los numeros
            }
        }
        if(!numero.isEmpty()) valor.add(Double.parseDouble(numero)); //Si la expresion no acaba con un parentesis se añade el ultimo valor a la lista

        List<Integer> aux=new ArrayList<>();
        Map<Integer, Integer> parentesis=new HashMap<>();
        Map<Integer, Integer> contadorPar=new HashMap<>();
        int paren=0;

        //Los ( deben tener su cierre bien puesto TODO en el metodo comprobar errores

        if(operador.contains('(')){
            /*
             * Las posiciones de los parentesis se almacenan en un map donde se enlaza la posicion de la apertura del parentesis en la lista de operadores, con la posicion de su cierre.
             * Además se almacena en otro map la posicion de apertura del parentesis con un contador de los parentesis previos, para despues poder actualizar la lista.
             */
            for(int i=0; i<operador.size(); i++){
                if(operador.get(i)=='('){
                    aux.add(i); //Ayudandonos de una lista auxiliar se añade la posicion del parentesis de apertura en la lista operador
                    contadorPar.put(i, paren); //Se añade a un mapa que cuenta cuantos parentesis previos hay
                    paren++; //Ahora hay un parentesis previo más
                }else if(operador.get(i)==')'){
                    parentesis.put(aux.get(aux.size()-1), i); //Se añade al mapa la posicion del ultimo parentesis abierto con la del cierre del parentesis
                    aux.remove(aux.size()-1); //Se elimina de la lista auxiliar la posicion del parentesis que se acaba de cerrar
                    paren++; //Ahora hay un parentesis previo más
                }
            }//Al salir del bucle nos quedamos con un mapa con las aperturas y su correspondiente cierre y con otro mapa con la apertura y los parentesis previos a él

            Integer[] apertura=parentesis.keySet().toArray(new Integer[0]);
            Arrays.sort(apertura); //Hacemos una lista ordenada de las posiciones de los parentesis de apertura
            int abrir, cerrar;

            for (int i = apertura.length-1; i >=0; i--) {  //Recorremos los parentesis de manera inversa
                for (int j=i-1; j>=0; j--) { //Recorremos de manera inversa todos los parentesis de apertura previos
                    if(parentesis.get(apertura[i])<parentesis.get(apertura[j])){ /* Si el parentesis de cierre de la apertura mas lejana es mas pequeño que el parentesis de cierre de la apertura menor
                                                                                  * implica que estamos ante un parentesis anidado, siendo el que corresponde con la i el mas simple */
                        parentesis.replace(apertura[j], parentesis.get(apertura[j])-operador.subList(apertura[i],parentesis.get(apertura[i])+1).size()); /*  Esta operacion modifica el map de parentesis para que
                                                                                                                                                             *  ua vez evaluado el parentesis mas pequeño posible dentro
                                                                                                                                                             *  del nido de parentesis, el map quede con las nuevas posiciones
                                                                                                                                                             *  actualizadas y el cierre quede con la resta de las posiciones de
                                                                                                                                                             *  la expresion ya evaluada (fuera de este for anidado)
                                                                                                                                                             *  (Se puede poner el +1 en el sublist sin miedo a error
                                                                                                                                                             *  porque nunca va a ser el ultimo valor porque sino no
                                                                                                                                                             *  nunca hubiera entrado en el bucle)*/
                    }
                }

                //Procede a evaluar y eliminar lo de dentro del parentesis
                abrir=apertura[i]-contadorPar.get(apertura[i]); //Posicion del primer valor dentro del parentesis
                cerrar=parentesis.get(apertura[i])-contadorPar.get(apertura[i]); //Posicion del ultimo valor dentro del parentesis



                /*valor.set(abrir, evaluarSinParentesis(valor.subList(abrir, cerrar), operador.subList(apertura[i]+1, parentesis.get(apertura[i]))));*//* Se modifica en la lista de valores el valor correspondiente
                                                                                                                                                        * al primer nuemero del parentesis por la evaluacion de lo que
                                                                                                                                                        * esta dentro del conjunto entero, es decir se resuelve el parentesis)
                                                                                                                                                        NO SE POR QUE ELIMINA LOS VALORES QUE EVALUA EN LA LISTA
                                                                                                                                                        * pero lo hace asi que con esto ya se sustituye lo que incluye el parentesis
                                                                                                                                                        * añadiendo el resultado final*/

                evaluarSinParentesis(valor.subList(abrir, cerrar), operador.subList(apertura[i]+1, parentesis.get(apertura[i])));//Al parecer el metodo estatico privado evaluar parentesis modifica las listas de su interior TODO ver que pollas pasa
                //valor.subList(abrir+1, cerrar).clear(); //Eliminamos los valores de dentro del parentesiss
                //operador.subList(apertura[i], parentesis.get(apertura[i])+1).clear(); //Se eliminan los operadores de los parentesis
                parentesis.remove(apertura[i].intValue()); //Se elimina la pareja de parentesis
                operador.remove(apertura[i].intValue());
                operador.remove(apertura[i].intValue());  //Se elimina el parentesis de apertura y de cierre de la lista de operadores
            }//Al terminar el bucle nos quedamos con dos listas de valores y operaciones sin parentesis
        }

        return evaluarSinParentesis(valor, operador);
    }

    /**
     * Suma y resta
     * @param a valor 1
     * @param b valor 2
     * @param c operacion
     * @throws Exception si c no es + o -
     * @return a+-b
     */
    private static double sumarRestar(double a, double b, char c){
        if(c=='+') return a+b;
        else if(c=='-') return a-b;
        else throw new IllegalArgumentException("Caracter no valido");
    }

    /**
     * Multiplica y divide
     * @param a valor 1
     * @param b valor 2
     * @param c operacion
     * @throws Exception si c no es * o /
     * @throws Exception si c es / y b es 0
     * @return a/*b
     */
    private static double multiplicarDividir(double a, double b, char c){
        if(c=='*') return a*b;
        else if(c=='/'){
            if(b!=0) return a/b;
            else throw new IllegalArgumentException("No puedes dividir entre 0");
        }else throw new IllegalArgumentException("Caracter no valido");
    }

    /**
     * Eleva a a b
     * @param a valor 1
     * @param b valor 2
     * @throws Exception si es un valor negativo y b es una raiz cuadrada
     * @return a^b
     */
    private static double elevar(double a, double b){
        if(a>=0&&b%1==0) return Math.pow(a, b);
        else throw new IllegalArgumentException("No queremos numeros imaginarios");
    }

    /**
     * Dado una lista de valores y una lista de operadores evalua como si fuera una operación donde el primer valor de la lista de valores se concatena con el primer valor de la lista de operadores, segido del segundo de la lista de valores y así consecutivamente.
     * @param valores valores
     * @param operadores operadores
     * @return expresion evaluada
     */
    private static double evaluarSinParentesis(@NonNull List<Double> valores, @NonNull List<Character> operadores){
        int i=0;
        if(operadores.contains('^')){
            do{
                if(operadores.get(i)=='^'){
                    valores.set(i, elevar(valores.get(i), valores.get(i+1)));
                    valores.remove(i+1);
                    operadores.remove(i);
                }else i++;
            }while (i<operadores.size());
            i=0;
        }
        if(operadores.contains('*')||operadores.contains('/')){
            do{
                if(operadores.get(i)=='*'||operadores.get(i)=='/'){
                    valores.set(i, multiplicarDividir(valores.get(i), valores.get(i+1), operadores.get(i)));
                    valores.remove(i+1);
                    operadores.remove(i);
                }else i++;
            }while (i<operadores.size());
        }

        while (!operadores.isEmpty()){
            valores.set(0, sumarRestar(valores.get(0), valores.get(1),operadores.get(0)));
            valores.remove(1);
            operadores.remove(0);
        }

        return valores.get(0);
    }

    private static void comprobarErrores(String expr){

    }
}
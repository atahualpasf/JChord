/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Util;

/**
 * Clase para recibir las entradas por teclado del usuario.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class KeyIn {

    /**
     * Método para mostrar al usuario el prompt
     *
     * @param prompt Prompt
     */
    public static void printPrompt(String prompt) {
        System.out.print(prompt + " ");
        System.out.flush();
    }

    /**
     * Método para asegurarse de que no haya datos disponibles en la secuencia
     * de entrada
     *
     */
    public static void inputFlush() {
        int dummy;
        int bAvail;

        try {
            while ((System.in.available()) != 0) {
                dummy = System.in.read();
            }
        } catch (java.io.IOException e) {
            Util.showMessage(3, 3, KeyIn.class.getSimpleName(), "Input error.");
        }
    }

    /**
     * Método de entrada de datos de tipo String.
     *
     * @param prompt
     * @return Datos ingresador por el usuario.
     */
    public static String inString(String prompt) {
        inputFlush();
        printPrompt(prompt);
        return inString();
    }

    /**
     * Método de entrada de datos de tipo String.
     *
     * @return Datos ingresador por el usuario.
     */
    public static String inString() {
        int aChar;
        String s = "";
        boolean finished = false;

        while (!finished) {
            try {
                aChar = System.in.read();
                if (aChar < 0 || (char) aChar == '\n') {
                    finished = true;
                } else if ((char) aChar != '\r') {
                    s = s + (char) aChar; // Enter into string
                }
            } catch (java.io.IOException e) {
                Util.showMessage(3, 3, KeyIn.class.getSimpleName(), "Input error, not a String.");
                finished = true;
            }
        }
        return s;
    }

    /**
     * Método de entrada de datos de tipo Integer.
     *
     * @param prompt
     * @return Datos ingresador por el usuario.
     */
    public static int inInt(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Integer.parseInt(inString().trim());
            } catch (NumberFormatException e) {
                Util.showMessage(3, 3, KeyIn.class.getSimpleName(), "Invalid input, not an integer.");
            }
        }
    }

    /**
     * Método de entrada de datos de tipo Char.
     *
     * @param prompt
     * @return Datos ingresador por el usuario.
     */
    public static char inChar(String prompt) {
        int aChar = 0;

        inputFlush();
        printPrompt(prompt);

        try {
            aChar = System.in.read();
        } catch (java.io.IOException e) {
            Util.showMessage(3, 3, KeyIn.class.getSimpleName(), "Input error, no a char.");
        }
        inputFlush();
        return (char) aChar;
    }

    /**
     * Método de entrada de datos de tipo Double.
     *
     * @param prompt
     * @return Datos ingresador por el usuario.
     */
    public static double inDouble(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Double.valueOf(inString().trim());
            } catch (NumberFormatException e) {
                Util.showMessage(3, 3, KeyIn.class.getSimpleName(), "Invalid input. not a floating point number");
            }
        }
    }
}

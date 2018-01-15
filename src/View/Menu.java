/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Util;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Clase que se encarga del armado del menú de opciones que visualizará el
 * cliente al momento de ingresar al sistema.
 *
 * @author Atahualpa Silva F.
 * @link https://github.com/atahualpasf
 * <br> 
 * @author Andrea L. Contreras D.
 * @link https://github.com/andrecontdi
 */
public class Menu {

    private String name;

    private LinkedHashMap<String, Runnable> actionsMap = new LinkedHashMap<>();

    /**
     * Constructor de la clase.
     *
     * @param name Nombre del menú a construir.
     */
    public Menu(String name) {
        this.name = name;
    }

    /**
     * Método que se encarga de indicar cual es la acción que se debe ejecutar
     * al seleccionar una opción.
     *
     * @param name Nombre de la opción.
     * @param action Acción a ejecutar.
     */
    public void putAction(String name, Runnable action) {
        actionsMap.put(name, action);
    }

    /**
     * Método que se encarga de generar el menú que se imprimirá o mostrará al
     * usuario.
     *
     * @return String a mostrar.
     */
    public String generateText() {
        StringBuilder stringToShow = new StringBuilder();
        stringToShow.append("=================================================================\n");
        String title = name + " MENU";
        stringToShow.append("|\t\t\t").append(addSpaces(title, Util.ANSI_CYAN + name.charAt(0) + Util.ANSI_RESET + name.substring(1) + " " + Util.ANSI_CYAN + "M" + Util.ANSI_RESET + "ENU ")).append(Util.ANSI_RESET + "\t\t\t|\n");
        stringToShow.append("=================================================================\n");
        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            stringToShow.append(String.format("|" + "\t\t\t" + Util.ANSI_CYAN + "%02d." + Util.ANSI_RESET + " %-25s\t\t" + "|\n", i + 1, actionNames.get(i)));
        }
        stringToShow.append("=================================================================");
        return stringToShow.toString();
    }

    /**
     * Método que se encarga de ejecutar la acción indicada.
     *
     * @param actionNumber Número que indica la acción a ejecutar.
     */
    public void executeAction(int actionNumber) {
        int effectiveActionNumber = actionNumber - 1;
        if (effectiveActionNumber < 0 || effectiveActionNumber >= actionsMap.size()) {
            System.out.println("Ignoring menu choice: " + actionNumber);
        } else {
            List<Runnable> actions = new ArrayList<>(actionsMap.values());
            actions.get(effectiveActionNumber).run();
        }
    }

    /**
     * Método que se encarga de agregar espacios al principio y al final del
     * nombre del menú, para que se muestre lo más centrado posible.
     *
     * @param original Titulo original.
     * @param value Valor al que se le agregaran los espacios en blanco
     * @return Nuevo valor con los espacios en blanco.
     */
    private String addSpaces(String original, String value) {
        int originalLength = original.length();
        for (int i = originalLength; i < Util.MENU_TITLE_LENGTH; i++) {
            value = " " + value + " ";
        }
        return value;
    }
}

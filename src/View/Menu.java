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
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class Menu {
    private String name;
    
    private LinkedHashMap<String, Runnable> actionsMap = new LinkedHashMap<>();

    public Menu(String name) {
        this.name = name;
    }

    public void putAction(String name, Runnable action) {
        actionsMap.put(name, action);
    }

    public String generateText() {
        StringBuilder stringToShow = new StringBuilder();
        stringToShow.append(Util.ANSI_BLUE + "=================================================================\n");
        String title = name + " MENU";
        stringToShow.append(Util.ANSI_BLUE + "|\t\t\t").append(addSpaces(title, Util.ANSI_RED + name.charAt(0) + Util.ANSI_RESET + name.substring(1) + " " + Util.ANSI_RED  + "M" + Util.ANSI_RESET + "ENU ")).append(Util.ANSI_RESET  + "\t\t\t|\n");
        stringToShow.append(Util.ANSI_BLUE + "=================================================================\n");
        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            stringToShow.append(String.format(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t\t" + Util.ANSI_RED + "%02d." + Util.ANSI_RESET  + " %-25s\t\t" + Util.ANSI_BLUE + "|\n", i + 1, actionNames.get(i)));
        }
        stringToShow.append(Util.ANSI_BLUE + "=================================================================");
        return stringToShow.toString();
    }

    public void executeAction(int actionNumber) {
        int effectiveActionNumber = actionNumber - 1;
        if (effectiveActionNumber < 0 || effectiveActionNumber >= actionsMap.size()) {
            System.out.println("Ignoring menu choice: " + actionNumber);
        } else {
            List<Runnable> actions = new ArrayList<>(actionsMap.values());
            actions.get(effectiveActionNumber).run();
        }
    }
    
    private String addSpaces(String original, String value) {
        int originalLength = original.length();
        for (int i = originalLength; i < Util.MENU_TITLE_LENGTH ; i++) {
            value = " " + value + " ";
        }
        return value;
    }
}

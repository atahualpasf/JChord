/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Data;
import Controller.JChordController;
import Controller.Util;
import Model.Node;

/**
 *
 * @author Atahualpa Silva F. <https://github.com/atahualpasf>
 * @author Andrea L. Contreras D. <https://github.com/andrecontdi>
 */
public class JChord {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        // TODO code application logic here
        Util.calculateMaxNodes();
        while (true) {
            int option;
            System.out.println(Util.ANSI_BLUE + "=================================================================");
            System.out.printf(Util.ANSI_BLUE + "|\t\t\t" + Util.ANSI_RED + "M" + Util.ANSI_RESET + "ENU " + Util.ANSI_RED  + "S" + Util.ANSI_RESET  + "ELECTION " + Util.ANSI_RED  + "D" + Util.ANSI_RESET  + "EMO\t\t\t|\n");
            System.out.println(Util.ANSI_BLUE + "=================================================================");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "1." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "JOIN RING");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "1." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "LEAVE RING");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "2." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "NOT DEFINED");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "3." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "NOT DEFINED");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "4." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "NOT DEFINED");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "5." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "NOT DEFINED");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "6." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "NOT DEFINED");
            System.out.printf(Util.ANSI_BLUE + "|" + Util.ANSI_RESET + "\t\t" + Util.ANSI_RED + "7." + Util.ANSI_RESET  + " %-25s\t\t\t" + Util.ANSI_BLUE + "|\n", "EXIT");
            System.out.printf(Util.ANSI_BLUE + "=================================================================\n");
            option = KeyIn.inInt("|\t\t   YOUR OPTION ->");

            switch(option) 
            {
                case 1: Util.cls();
                        JChordController.joinRing(Util.getMyIp(), Util.MY_PORT);
                        break;
                case 3: Util.cls();
                        System.out.println(Data.getMyNode());
                        System.out.println("Chao");
                        break;
            }
        }
    }
    
}

package be.ugent.caagt.equi;

import be.ugent.caagt.equi.main.Equi;

/**
 * Main class used to circumvent the module system
 * Cf. https://stackoverflow.com/questions/52569724/javafx-11-create-a-jar-file-with-gradle
 *
 */
public class Main {

    public static void main(String[] args) {
        Equi.main(args);
    }
}

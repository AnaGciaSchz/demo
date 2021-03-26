package com.main;

import DbLogic.ImdbLogic;
import io.micronaut.runtime.Micronaut;

import java.io.IOException;

/**
 * Class that runs the application with its main method
 *
 * @Author Ana Garcia
 */
public class Application {

    /**
     * Main method that starts running the application
     *
     * @param args parameters for the application
     */
    public static void main(String[] args) throws IOException {
        //write here the files path
        String titleBasics = ""; //title.basics.tsv
        String titleRatings = ""; //title.ratings.tsv

        Micronaut.run(Application.class, args);
        ImdbLogic imdbLogic = new ImdbLogic();
        //imdbLogic.resetDB(titleBasics,titleRatings);
        imdbLogic.createDB(titleBasics,titleRatings);
    }
}

package com.main;

import DbLogic.ImdbLogic;
import io.micronaut.runtime.Micronaut;
import utils.elasticSearch.manageIndex.CreateElasticSearchIndex;
import utils.elasticSearch.manageIndex.DeleteElasticSearchInformation;
import utils.elasticSearch.manageIndex.LoadElasticSearchInformation;

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

        Micronaut.run(Application.class, args);
        //ImdbLogic imdbLogic = new ImdbLogic();
        //imdbLogic.resetDB("/Users/anamariagarciasanchez/Documents/title.basics.tsv","/Users/anamariagarciasanchez/Documents/title.ratings.tsv");
        //imdbLogic.createDB("/Users/anamariagarciasanchez/Documents/title.basics.tsv","/Users/anamariagarciasanchez/Documents/title.ratings.tsv");
    }
}

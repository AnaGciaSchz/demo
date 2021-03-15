package com.main;

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
     * @param args parameters for the application
     */
    public static void main(String[] args) throws IOException {

        Micronaut.run(Application.class, args);
/**
        DeleteElasticSearchInformation d = new DeleteElasticSearchInformation();
        d.deleteInformation("imdb");

        System.out.println("deleted");

        CreateElasticSearchIndex c = new CreateElasticSearchIndex();
        c.createImdbIndex("./IMDBCreateIndex.json");

        System.out.println("created");

        LoadElasticSearchInformation l = new LoadElasticSearchInformation();
        l.loadImdbInformation("/Users/anamariagarciasanchez/Documents/title.basics.tsv");

        System.out.println("inserted");
 */

    }
}

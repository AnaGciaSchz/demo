package com.main;

import io.micronaut.runtime.Micronaut;

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
    public static void main(String[] args){

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

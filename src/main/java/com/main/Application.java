package com.main;

import io.micronaut.runtime.Micronaut;
import utils.LoadElasticSearchInformation;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Micronaut.run(Application.class, args);
    }
}

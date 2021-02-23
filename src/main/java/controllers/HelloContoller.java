package controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

@Controller("/hello")
public class HelloContoller {

    @Get(produces = MediaType.TEXT_PLAIN)
    public String index(){

        //RestHighLevelClient client = new RestHighLevelClient(
        //       RestClient.builder(
        //                new HttpHost("localhost", 9200, "http"),
        //                new HttpHost("localhost", 9201, "http")));
        //client.close();

        return "Hello World";
    }

}

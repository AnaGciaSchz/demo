package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

@Controller("/search")
public class SearchContoller {

    @Get(produces = MediaType.TEXT_JSON)
    public HttpResponse index(@QueryValue String query){

        //RestHighLevelClient client = new RestHighLevelClient(
        //       RestClient.builder(
        //                new HttpHost("localhost", 9200, "http"),
        //                new HttpHost("localhost", 9201, "http")));
        //client.close();

        return HttpResponse.ok().body("{\n\"query\":\"" + query+"\",\n" +
                "\"cluster_name\":\"7.11.1\"\n}");
    }

}

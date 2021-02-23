package controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import java.io.IOException;

@Controller("/search")
public class SearchContoller {

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse index(@QueryValue String query) {

        RestHighLevelClient client = new RestHighLevelClient(
               RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        MainResponse response = null;

        try {
            response = client.info(RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response !=null){
            String clusterVersion = response.getClusterName()+" "+response.getVersion().getNumber();

            return HttpResponse.ok().body("{\n\"query\":\"" + query+"\",\n" +
                    "\"cluster_name\":\""+clusterVersion+"\"\n}");
        }

        return HttpResponse.notFound();
    }

}

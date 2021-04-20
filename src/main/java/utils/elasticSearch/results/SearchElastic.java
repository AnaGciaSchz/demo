package utils.elasticSearch.results;

import io.micronaut.http.server.exceptions.InternalServerException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;
import utils.elasticSearch.aggregation.AggregationUtilsInterface;
import utils.elasticSearch.query.QueryUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Class that has all the logic related with the search in elasticsearch
 */
public class SearchElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    @Inject
    AggregationUtilsInterface aggregationUtils;

    @Inject
    QueryUtilsInterface queryUtils;

    /**
     * Method to search a query in some fields of an index
     *
     * @param parameters Query and parameters that we want to use to search in that index
     * @return A list of the results (size <=10)
     * @throws IOException If there is an error
     */
    public Map<String, Object> searchImdb(Map<String, String> parameters) throws InternalServerException, IOException, ParseException {
        String[] fields = {"index", "primaryTitle", "genres", "titleType", "start_yearText", "end_yearText"};

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        searchRequest.source(searchInEveryfieldWithImdbParameters(fields, parameters));


        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        SearchResponse response;

        response = client.search(searchRequest, RequestOptions.DEFAULT);

        return getHitsAndAggregations(response);
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields     Fields where to search
     * @param parameters Query and parameters to search
     * @return a builder with the query
     */
    private SearchSourceBuilder searchInEveryfieldWithImdbParameters(String[] fields, Map<String, String> parameters) throws ParseException, IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        char quotes ='"';
        PhraseSuggestionBuilder psb = new PhraseSuggestionBuilder("primaryTitle");

        BoolQueryBuilder resultQuery = createQuery(fields, parameters);
        BoolQueryBuilder facetsGenre = null;
        BoolQueryBuilder facetsType = null;
        BoolQueryBuilder facetsDate = null;
        BoolQueryBuilder filter = new BoolQueryBuilder();


        sourceBuilder.aggregation(aggregationUtils.createAggregation("genreAggregation", "genres"));
        sourceBuilder.aggregation(aggregationUtils.createAggregation("titleTypeAggregation", "titleType"));
        sourceBuilder.aggregation(aggregationUtils.datesAggregation("dateRange", "start_year"));

        if (parameters.get("genre") != null) {
            filter.filter(queryUtils.createQuery(parameters.get("genre"), "genres"));
            if(facetsDate == null){
                facetsDate = new BoolQueryBuilder();
            }
            facetsDate.must(queryUtils.createQuery(parameters.get("genre"), "genres"));
            if(facetsType == null){
                facetsType = new BoolQueryBuilder();
            }
            facetsType.must(queryUtils.createQuery(parameters.get("genre"), "genres"));
        }

        if (parameters.get("type") != null) {
            filter.filter(queryUtils.createQuery(parameters.get("type"), "titleType"));
            if(facetsDate == null){
                facetsDate = new BoolQueryBuilder();
            }
            facetsDate.must(queryUtils.createQuery(parameters.get("type"), "titleType"));
            if(facetsGenre == null){
                facetsGenre = new BoolQueryBuilder();
            }
            facetsGenre.must(queryUtils.createQuery(parameters.get("type"), "titleType"));
        }

        if (parameters.get("date") != null) {
            filter.filter(queryUtils.datesQuery(parameters.get("date"), "start_year"));
            if(facetsType == null){
                facetsType = new BoolQueryBuilder();
            }
            facetsType.must(queryUtils.datesQuery(parameters.get("date"), "start_year"));
            if(facetsGenre == null){
                facetsGenre = new BoolQueryBuilder();
            }
            facetsGenre.must(queryUtils.datesQuery(parameters.get("date"), "start_year"));
        }

            if (facetsGenre != null) {
                FilterAggregationBuilder genreFilter = AggregationBuilders.filter("genreFilter",
                        facetsGenre);
                genreFilter.subAggregation(aggregationUtils.createAggregation("genreAggregation", "genres"));
                sourceBuilder.aggregation(genreFilter);
            }
            if (facetsType != null) {
                FilterAggregationBuilder typeFilter = AggregationBuilders.filter("typeFilter",
                        facetsType);
                typeFilter.subAggregation(aggregationUtils.createAggregation("titleTypeAggregation", "titleType"));
                sourceBuilder.aggregation(typeFilter);
            }
            if (facetsDate != null) {
                FilterAggregationBuilder dateFilter = AggregationBuilders.filter("dateFilter",
                        facetsDate);
                dateFilter.subAggregation(aggregationUtils.datesAggregation("dateRange", "start_year"));
                sourceBuilder.aggregation(dateFilter);
            }
            sourceBuilder.postFilter(filter);
        sourceBuilder.query(resultQuery);
        sourceBuilder.suggest(new SuggestBuilder().setGlobalText(parameters.get("query")).addSuggestion("did_you_mean", psb));
        return sourceBuilder;
    }

    /**
     * Method that creates the structure of the query
     *
     * @param fields     fields to search
     * @param parameters parameters of the query
     * @return boolean query
     */
    private BoolQueryBuilder createQuery(String[] fields, Map<String, String> parameters) {
        BoolQueryBuilder query = searchInEveryfield(fields, parameters.get("query"));
        BoolQueryBuilder query2 = new BoolQueryBuilder();
        query2.must(QueryBuilders.matchPhraseQuery("primaryTitle", parameters.get("query")).boost(3)).boost(3);

        BoolQueryBuilder resultQuery = new BoolQueryBuilder();
        DisMaxQueryBuilder disMaxQuery = new DisMaxQueryBuilder();

        disMaxQuery.add(query2);
        FunctionScoreQueryBuilder f = QueryBuilders.functionScoreQuery(query,
                ScoreFunctionBuilders.linearDecayFunction("averageRatingLogic", 10, 5, "0d", 0.5));
        disMaxQuery.add(f);

        resultQuery.must(disMaxQuery);
        resultQuery.should(QueryBuilders.matchQuery("titleType", "movie").boost(5));

        return resultQuery;
    }

    /**
     * Method to search in every field where the param says
     *
     * @param fields Fields where to search
     * @param query  Query to search
     * @return a builder with the query
     */
    private BoolQueryBuilder searchInEveryfield(String[] fields, String query) {
        BoolQueryBuilder builder = new BoolQueryBuilder();
        builder.should(QueryBuilders.multiMatchQuery(query, fields).type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        return builder;

    }

    /**
     * Method that creates the list of results
     *
     * @param response Response of the search
     * @return The list of results (size<=10)
     */
    private Map<String, Object> getHitsAndAggregations(SearchResponse response) {
        Map<String, Object> results = new HashMap<>();
        results.put("hits", elasticSearchUtils.getHits(response));

        ArrayList<String> suggestions = new ArrayList();
        var suggestEntries = response.getSuggest().getSuggestion("did_you_mean").getEntries();
        for(int i = 0; i<suggestEntries.get(0).getOptions().size();i++){
           suggestions.add(suggestEntries.get(0).getOptions().get(i).getText().toString());
       }
        results.put("suggestion",suggestions);

        ParsedFilter facetGenre = response.getAggregations().get("genreFilter");
        ParsedFilter facetType = response.getAggregations().get("typeFilter");
        ParsedFilter facetDate = response.getAggregations().get("dateFilter");
        Terms filteredGenre = null;
        Terms filteredTypeTerms = null;
        Range filteredDateRange = null;
        if(facetGenre != null){
            filteredGenre = facetGenre.getAggregations().get("genreAggregation");
        }
        if(facetType != null){
            filteredTypeTerms = facetType.getAggregations().get("titleTypeAggregation");
        }
        if(facetDate != null){
            filteredDateRange = facetDate.getAggregations().get("dateRange");
        }

        if (response.getAggregations() != null) {
            Terms genreTerms = response.getAggregations().get("genreAggregation");
            if (filteredGenre != null) {
                    results.put("genres", aggregationUtils.getGenresAggregation(filteredGenre));
            } else {
                if (genreTerms != null) {
                    results.put("genres", aggregationUtils.getGenresAggregation(genreTerms));
                }
            }

            Terms typeTerms = response.getAggregations().get("titleTypeAggregation");

            if (filteredTypeTerms != null) {
                    results.put("types", aggregationUtils.getTypesAggregation(filteredTypeTerms));
            } else {
                if (typeTerms != null) {
                    results.put("types", aggregationUtils.getTypesAggregation(typeTerms));
                }
            }

            Range dateRange = response.getAggregations().get("dateRange");
            if (filteredDateRange != null) {
                    results.put("dates", aggregationUtils.getDatesAggregation(filteredDateRange));
            } else {
                if (dateRange != null) {
                    results.put("dates", aggregationUtils.getDatesAggregation(dateRange));
                }
            }
        }
        return results;
    }
}

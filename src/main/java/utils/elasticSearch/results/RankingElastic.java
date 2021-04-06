package utils.elasticSearch.results;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import utils.elasticSearch.ElasticSearchUtilsInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to return all necessary rankings
 *
 * @Author Ana Garcia
 */
public class RankingElastic {

    @Inject
    ElasticSearchUtilsInterface elasticSearchUtils;

    /**
     * Methot to return the top 10 movies or Imdb (most voted with high averageRating, sorted by
     * averageraing and numVotes)
     *
     * @return a Map with the results
     * @throws IOException if there's a problem
     */
    public Map<String, Object> imdbRankingMovies() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("imdb");

        searchRequest.source(getMovieRanking());


        RestHighLevelClient client = elasticSearchUtils.getClientInstance();
        SearchResponse response;

        response = client.search(searchRequest, RequestOptions.DEFAULT);

        Map<String, Object> results = new HashMap<>();
        results.put("hits", elasticSearchUtils.getHits(response));

        return results;
    }

    /**
     * Method that creates the query to fidn the top 10 movies of imdb
     *
     * @return The query to find the top 10 movies
     */
    private SearchSourceBuilder getMovieRanking() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.must(new RangeQueryBuilder("averageRating").gte(9));
        boolQuery.filter(QueryBuilders.matchQuery("titleType", "movie"));
        boolQuery.filter(new RangeQueryBuilder("numVotes").gte(5000));

        FieldValueFactorFunctionBuilder numVotesFactor = ScoreFunctionBuilders.fieldValueFactorFunction("numVotes").factor((float) 0.00001).modifier(FieldValueFactorFunction.Modifier.NONE);
        FieldValueFactorFunctionBuilder averageRatingFactor = ScoreFunctionBuilders.fieldValueFactorFunction("averageRating").factor((float) 1000).modifier(FieldValueFactorFunction.Modifier.NONE);
        LinearDecayFunctionBuilder linearFunction = ScoreFunctionBuilders.linearDecayFunction("averageRating", 10, 8, "0d", 0.8);

        FunctionScoreQueryBuilder.FilterFunctionBuilder[] score = new FunctionScoreQueryBuilder.FilterFunctionBuilder[3];
        score[0] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(numVotesFactor);
        score[1] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(averageRatingFactor);
        score[2] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(linearFunction);

        FunctionScoreQueryBuilder resultQuery = QueryBuilders.functionScoreQuery(boolQuery, score);

        resultQuery.scoreMode(FunctionScoreQuery.ScoreMode.SUM);

        sourceBuilder.query(resultQuery);
        return sourceBuilder;
    }
}

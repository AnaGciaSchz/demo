package utils.elasticSearch.mainResponse;

import io.micronaut.context.annotation.Primary;
import org.elasticsearch.client.core.MainResponse;

/**
 * Primary class of the interface that has methods to deal with MainResponse objects.
 *
 * @Author Ana Garcia
 */
@Primary
public class MainResponseUtils implements MainResponseUtilsInterface{
    /**
     * Method to return the name of the cluster of a MainResponse and the elasticSearchVersion
     * @param response MainResponse
     * @return String with the name and the version separated by an space
     */
    @Override
    public String getClusterNameAndVersion(MainResponse response){
        return response.getClusterName()+" "+response.getVersion().getNumber();
    }
}

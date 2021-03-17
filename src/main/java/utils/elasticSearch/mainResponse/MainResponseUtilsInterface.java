package utils.elasticSearch.mainResponse;

import org.elasticsearch.client.core.MainResponse;

/**
 * Interface to represent a class that has methods to deal with MainResponse objects
 *
 * @Author Ana Garcia
 */
public interface MainResponseUtilsInterface {

    /**
     * Method to return the name of the cluster of a MainResponse and the elasticSearchVersion
     *
     * @param response MainResponse
     * @return String with the name and the version separated by an space
     */
    String getClusterNameAndVersion(MainResponse response);
}

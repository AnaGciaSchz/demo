## Demo search on a IMDb titles index documentation

<!-- ABOUT -->
## About The Demo

This demo is the result of the search path's sessions about elasticsearch. To develop this project it was used technologies like Java, Micronaut, Docker and, of course, elasticsearch.

In this program, you can do different searches in an IMDb index that contains some film titles and information about its genre, type of film or release date. The results will be in JSON format so they can be easily connected with a UI.

<!-- GETTING STARTED -->
## Getting Started
Here you can find the steps to run this project.
### Prerequisites
First, you need to [download Docker](https://hub.docker.com/editions/community/docker-ce-desktop-mac/) in order to execute the IMDb index when needed. You might also want to download java, micronaut and elasticsearh with docker:
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.11.1
```
And create and run an image with the following lines:
```
docker run --rm -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.11.1
```
I recomend saving that image after setting the index (this way you won't have to fill the index each time you execute it):

```
docker commit “id container” elasticsearch_imdb:version1
```

To obtain the "id containter" just execute:

```
docker ps
```

and copy the id of the container that you are running.

To execute that saved image,m just run:
```
docker run --rm -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch_imdb:version1
```
### Setting the index

Then you need to set up the IMDb index. To do so, download the "title.basics.tsv.gz" archive from [this url](https://datasets.imdbws.com/) (it is big, thats why it's not already inserted in the project).

When you are done, open the project and pass it the titles.basics.tsv.gv path and execute the "createDB" method that's in Application.class in its main method (just write it down in there and write the path).

Example of path:
```
/Users/ana/Documents/title.basics.tsv
```
When you are done, you can go to http://localhost:9200/imdb and if everything is correct, you should see a json with information about the fields of the index like:
``
"{"imdb":{"aliases":{},"mappings":{"properties":{"end_year":{"type":"integer"}"
``

If you see and error, it means that the imdb index is not created.

### Executing the project


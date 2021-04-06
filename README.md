![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)
![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)
![Awesome Badges](https://img.shields.io/badge/badges-awesome-green.svg)

<!-- PROJECT LOGO -->
<p align="center">
  <img src="https://user-images.githubusercontent.com/49797815/112634739-3fd3ec80-8e3b-11eb-8c3a-977ae26803b7.png" alt="Logo" width="80" height="80">
</p>
<h3 align="center">Demo search on a IMDb titles index documentation</h3>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li> <a href="#about-the-demo">About The Demo</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#setting-the-index">Setting the index</a></li>
      </ul>
    </li>
    <li><a href="#functionalities-of-the-project">Functionalities of the project</a>
    <ul>
      <li><a href="#search-with-query-and-parameters">Search with query and parameters</a></li>
      <li><a href="#getting-the-top-10-movies">Getting the top 10 movies</a></li>
    </ul>
      </li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#credits">Credits</a></li>
  </ol>
</details>

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
I recommend saving that image after setting the index (this way you won't have to fill the index each time you execute it):

```
docker commit “id container” elasticsearch_imdb:version1
```

To obtain the "id container" just execute:

```
docker ps
```

and copy the id of the container that you are running. If you use `` docker ps -q `` It will only show you the id of the containers you are running, if you are just running the elasticsearch image, you can use that to get the id easily.

To execute that saved image, just run:
```
docker run --rm -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch_imdb:version1
```
### Setting the index

Then you need to set up the IMDb index. To do so, download the "title.basics.tsv.gz" and the "title.ratings.tsv.gz" files from [this url](https://datasets.imdbws.com/) (They are big, that's why they are not already inserted in the project).

When you are done, open the project and pass it the title.basics.tsv and title.rattings.tsv path and execute the "createDB" method that's in [the Application class](https://github.com/AnaGciaSchz/demoSearch/blob/master/src/main/java/com/main/Application.java) in its main method (just write the paths in the variables and descoment the method).

Example of path:
```
/Users/ana/Documents/title.basics.tsv
```

If you want to reinsert the index, just use the resetDB method, it uses the same params as createDB.

When you are done (the console should show you an "inserted" message, it takes some time), you can go to http://localhost:9200/imdb and if everything is correct, you should see a JSON with information about the fields of the index like:
``
"{"imdb":{"aliases":{},"mappings":{"properties":{"end_year":{"type":"integer"}"
``
If you see an error, it means that the IMDb index is not created.

## Functionalities of the project
In this project you can search in the imdb index using several filters or even get the top 10 movies of that index.
### Search with query and parameters

When the project is running (you can create an index or reset it whenever you want, if you just want to execute the project, delete the calls to that method) you can go to http://localhost:8080/ and start searching.

You have 4 different parameters. 
* query lets you write the query like in a search box
* genre lets you filter the genres of the movies. If you want to filter by more than one, separate them using a comma.
* type lets you filter the type of the movies. If you want to filter by more than one, separate them using a comma.
* date lets you filter the start_date of the movies, just write a range of dates in the following format: firstdate/lastdate. If you want to filter by more than one, separate them using a comma.

Example with one parameter:
```
http://localhost:8080/search?query=Kung%20Fu&genre=Action&type=movie&date=2000/2010
```

Example with more than one parameters:
```
http://localhost:8080/search?query=Kung%20Fu&genre=Action,Drama&type=movie,tvepisode&date=2000/2010,1990/1999
```
### Getting the top 10 movies
To get the Top 10 movies of Imdb, sorted by the number of votes and average rating, you just need to execute the project and go to the http://localhost:8080/ url, to the /ranking/movie page:
```
http://localhost:8080/ranking/movie
```
There you'll find a json containig the information of the sorted movies, without any aggregation.
<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<!-- CONTACT -->
## Contact

Ana Mª García Sánchez - @anamg in Slack - anamg@empathy.co

<!-- CREDITS -->
## Credits
<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>



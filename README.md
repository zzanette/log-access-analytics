[![](https://ilegra.com/en/wp-content/themes/ilegra-wp-theme/images/Logo.svg)](https://ilegra.com/en/)

# Log Access Analytics
Solution that receives logs via REST endpoints and calculate metrics. This solutions was built over [**AWS infrastructure**](https://console.aws.amazon.com/) using the Java Framework [**RestExpress**](https://github.com/RestExpress/RestExpress). The goal of this README is explain the strategies adobted and libraries used.

## REST API Libs:
* [RestExpress Framework](https://github.com/RestExpress/RestExpress): It's a Java framework, running on Netty. Garantee a small and fast REST api. We don't use Spring Boot;
* [Junit](https://junit.org/): To assurence quality, we are using JUnit for unit test. We are not using test integration;
* [JPA with Hibernate](https://hibernate.org/): We are using JPA with hibernate to make queries to collect metrics about the logs;
* [Flyway](https://flywaydb.org/): Makes the database migrations and create a version for our db;
* [Jedis](https://github.com/xetorthio/jedis): Client that help us to connect to Redis Database and persist metric's cache;
* [RestHighLevelClient](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high.html): Client to connect with ElasticSearch and persist ingested logs.

### API Endpoints
|   Method   | Endpoint | Description          |
| ---------- | -------- |--------------------- |
|     GET    | /        | "Say Hello" endpoint                                                                         |
|     GET    | /health  | Check if the api is started returning a 200 http status code.                                |
|     POST   | /log-analytics/ingest                              |Persist the log in MariaDB and ElasticSearch.       |
|     GET    | /log-analytics/metrics/top-3-urls                   |Return the top 3 URL accessed all around the world.|
|     GET    | /log-analytics/metrics/top-3-urls/region/{regionId} |Return the top 3 URL accessed PER region.          |
|     GET    | /log-analytics/metrics/less-access-url              |Return the URL with less access in all world.      |

#### Ingest LOG payload
To call the ingest endpoint, assume we have the log */pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1*, just send a POST request with header **Content-Type=application/json** and body as follow:
```json
{
  "regionCode": 1,
  "timestampUser": "1037825323957",
  "url": "/pets/exotic/cats/10",
  "userUuid": "5b019db5-b3d0-46d2-9963-437860af707f"
}
```

#### Metrics Parameters YEAR, WEEK, DAY and MINUTE
You can call the API endpoints **/log-analytics/metrics/top-3-urls** and **/log-analytics/metrics/top-3-urls/region/{regionId}** passing year, week, day and minute as parameter like this:
```
/log-analytics/metrics/top-3-urls?year=2002&minute=48
```
You can combine all parameter as you want.

### Devops Tools

We are running the log analytics application on AWS infrastructure, using the follow AWS services:

*  **[EC2](https://console.aws.amazon.com/ec2/)**: We have pre-installed enviroment 1 instance running Jenkins and 1 instance running Kibana and ElasticSearch. Jenkins have 3 Jobs using [Packer](https://packer.io/) and [Terraform](https://www.terraform.io/) to provision an EC2 thats contain the log analytics application.
*  **[RDS](https://console.aws.amazon.com/rds/)**: Our application is persisting data in MariaDB on RDS;
*  **[ElastiCache](https://console.aws.amazon.com/elasticache/)**: Our Redis are running was setup in this service to persist the metrics cache.

#### Jenkins' JOBs
To provision a machine with the log analytics application, we have 3 jobs thats do the work for us:
| JOB | Description |
| ------ | ------ |
| build-ami-log-access-analytics | Generate the .jar app from production branch and build the AMI with [Packer](https://packer.io/). |
| deploy-log-access-analytics    | Provision a machine in AWS EC2 using terraform. The project used to do this work we can find ont this [terraform project](https://github.com/zzanette/terraform-log-access-analytics). |
| log-analytics-pipeline | To do the proccess more directly, we have a pipeline to do all the work. This pipeline uses the Jenkinsfile on project, that's build the project, build AMI and launch the instance on AWS EC2.|


### Running Services
|                             Server                                                  |  Access URL                                    |                               
| ------------------------------------------------------------------------------------|------------------------------------------------|
| [Log Access Analytics API](http://ec2-54-164-225-234.compute-1.amazonaws.com:8080) | ec2-54-164-225-234.compute-1.amazonaws.com:8080 |
| [Jenkins Service](http://ec2-54-173-12-240.compute-1.amazonaws.com:8080)           | ec2-54-173-12-240.compute-1.amazonaws.com:8080  |
| [Kibana Service](http://ec2-54-173-72-3.compute-1.amazonaws.com:5601)              | ec2-54-173-72-3.compute-1.amazonaws.com:5601    |

### Todos
 - Write MORE Tests and Integration Test;
 - Run application over Kubernets;
 - Add Swagger;
 - Add Basic Authentication or OpenID Authentication;
 - Add authentication on kibana.
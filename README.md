# crashy-mccrashface

A simple app that will use up resources and eventually crash in order to make interesting monitoring graphs.

The purpose is to demo the ability of the platform
* to restart when the application crashes
* horizontal scaling
* gather and graph data such as
    * memory usage
    * cpu usage
    * log collection
    * http requests

## Seeing webstats
The property `management.metrics.web.server.auto-time-requests` is enabled, so if you hit some pages, then if
you go to a url like http://localhost:8080/actuator/metrics/http.server.requests you can
see the available tags.
You can filter to a given url like
http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/load

You can also filter to a given status code like
http://localhost:8080/actuator/metrics/http.server.requests?tag=status:404

Note: building container images with spring boot or the native image needs a newer OS than redhat 7
See https://github.com/buildpacks/pack/issues/439 for similar issues.

## Building and Running

### Building it yourself.
You can build it yourself using the regular maven commands. See the [maven.yml](.github/workflows/maven.yml) for
examples used for the github actions.

### Container image
You can run the image that is build by the github actions and pushed to docker hub.
`docker run --rm -p8080:8080 sellersj/crashy-mccrashface`

### Liveness and Readiness Probes
It depends on the name of your deployment, but assuming it is called crashy-mccrashface and the actuator 
are running on the standard port the following commands can be run.
```
oc set probe deployment/crashy-mccrashface --startup --get-url=http://localhost:8080/actuator/health/readiness --initial-delay-seconds=2 --failure-threshold=60 --period-seconds=2 --timeout-seconds=2
oc set probe deployment/crashy-mccrashface --readiness --get-url=http://localhost:8080/actuator/health/readiness --initial-delay-seconds=30 --period-seconds=5 --timeout-seconds=5
oc set probe deployment/crashy-mccrashface --liveness --get-url=http://localhost:8080/actuator/health/liveness --initial-delay-seconds=45 --period-seconds=5 --timeout-seconds=5
```

test

Can clear the probes
```
oc set probe deployment/crashy-mccrashface --remove --startup --readiness --liveness
```
References
https://www.mankier.com/1/oc-set-probe
https://spring.io/blog/2020/03/25/liveness-and-readiness-probes-with-spring-boot

Set resource limits
```
oc set resources deployment crashy-mccrashface --limits=cpu=500m,memory=600Mi --requests=cpu=100m,memory=400Mi
```

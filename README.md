# crashy-mccrashface

A simple app that will use up resources and eventually crash.

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

# crashy-mccrashface

A simple app that will use up resources and eventually crash.

## Seeing webstats
The property `management.metrics.web.server.auto-time-requests` is enabled, so if you hit some pages, then if
you go to a url like http://localhost:8080/actuator/metrics/http.server.requests you can
see the available tags.
You can filter to a given url like
http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/load
You can also filter to a given status code like
http://localhost:8080/actuator/metrics/http.server.requests?tag=status:404
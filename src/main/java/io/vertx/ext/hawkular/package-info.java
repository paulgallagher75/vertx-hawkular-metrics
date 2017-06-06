/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

/**
 * = Hawkular Metrics
 *
 * This project is an implementation of the Vert.x Metrics Service Provider Interface (SPI): metrics built from Vert.x
 * events will be sent to Hawkular, an http://www.hawkular.org[open source monitoring and management solution].
 *
 * == Features
 *
 * * Vert.x core tools monitoring: TCP/HTTP client and servers, {@link io.vertx.core.datagram.DatagramSocket},
 * {@link io.vertx.core.eventbus.EventBus} and handlers
 * * User defined metrics via an {@link io.vertx.core.eventbus.EventBus} bridge.
 *
 * == Prerequisites
 *
 * Follow the http://www.hawkular.org/hawkular-services/docs/quickstart-guide/[instructions to get Hawkular up and running].
 *
 * NOTE: You can use a standalone https://github.com/hawkular/hawkular-metrics[Hawkular Metrics] server as well.
 *
 * == Getting started
 *
 * The _${maven.artifactId}_ module must be present in the classpath.
 *
 * Maven users should add this to their project POM file:
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * And Gradle users, to their build file:
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 *
 * Vert.x does not enable SPI implementations by default. You must enable metric collection in the Vert.x options:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setup()}
 * ----
 *
 * == Configuration
 *
 * === Remote Hawkular server
 *
 * By default, _${maven.artifactId}_ sends metrics to a Hawkular server listening on `localhost` port `8080`.
 * But in production, the Hawkular server will likely run on a separate machine:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupRemote()}
 * ----
 *
 * === Tenant selection
 *
 * Hawkular Metrics is a multi-tenant solution, and _${maven.artifactId}_ can send metrics for a tenant other than `default`:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupTenant()}
 * ----
 *
 * === Connecting to a Hawkular server
 *
 * Requests sent to a Hawkular server must be authenticated and tenant must be set to `hawkular`:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupHawkularServer()}
 * ----
 *
 * === Openshift Metrics token authentication
 *
 * When working with Openshift's internal Metrics server, you can configure token authentication with a custom HTTP header:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupOpenshiftTokenAuthentication()}
 * ----
 *
 * === HTTPS and other HTTP related options
 *
 * _${maven.artifactId}_ communicates with the Hawkular server over HTTP. In order to communicate over HTTPS, set the
 * `ssl` flag to true:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupSecured()}
 * ----
 *
 * Note that all the usual {@link io.vertx.core.http.HttpClientOptions} properties can be used for SSL setup or client
 * tuning.
 *
 * === Metric tags
 *
 * http://www.hawkular.org/hawkular-metrics/docs/user-guide/#_tagging[Tags] can be applied to metrics:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupMetricTags()}
 * ----
 *
 * _${maven.artifactId}_ maintains a LRU cache of tagged metrics to avoid repeating tagging requests.
 * The cache size can be configured and defaults to `4096` metric names.
 *
 * It is also possible to apply tags to a specific set of metrics defined via exact match or regex match:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupMetricTagMatches()}
 * ----
 *
 * WARNING: If you use regex match, a wrong regex can potentially match a lot of metrics.
 *
 * Please refer to {@link io.vertx.ext.hawkular.VertxHawkularOptions} for an exhaustive list of options.
 *
 * == Vert.x core tools metrics
 *
 * This section lists all the metrics generated by monitoring the Vert.x core tools.
 *
 * === Net Client
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Gauge
 * |{@code vertx.net.client.<host>:<port>.connections}
 * |Number of connections to the remote host currently opened.
 *
 * |Counter
 * |{@code vertx.net.client.<host>:<port>.bytesReceived}
 * |Total number of bytes received from the remote host.
 *
 * |Counter
 * |{@code vertx.net.client.<host>:<port>.bytesSent}
 * |Total number of bytes sent to the remote host.
 *
 * |Counter
 * |{@code vertx.net.client.<host>:<port>.errorCount}
 * |Total number of errors.
 *
 * |===
 *
 * === HTTP Client
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Gauge
 * |{@code vertx.http.client.<host>:<port>.connections}
 * |Number of connections to the remote host currently opened.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.bytesReceived}
 * |Total number of bytes received from the remote host.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.bytesSent}
 * |Total number of bytes sent to the remote host.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.errorCount}
 * |Total number of errors.
 *
 * |Gauge
 * |{@code vertx.http.client.<host>:<port>.requests}
 * |Number of requests waiting for a response.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.requestCount}
 * |Total number of requests sent.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.responseTime}
 * |Cumulated response time.
 *
 * |Gauge
 * |{@code vertx.http.client.<host>:<port>.wsConnections}
 * |Number of websockets currently opened.
 *
 * |===
 *
 * === Datagram socket
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Counter
 * |{@code vertx.datagram.<host>:<port>.bytesReceived}
 * |Total number of bytes received on the {@code <host>:<port>} listening address.
 *
 * |Counter
 * |{@code vertx.datagram.<host>:<port>.bytesSent}
 * |Total number of bytes sent to the remote host.
 *
 * |Counter
 * |{@code vertx.datagram.errorCount}
 * |Total number of errors.
 *
 * |===
 *
 * === Net Server
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Gauge
 * |{@code vertx.net.server.<host>:<port>.connections}
 * |Number of opened connections to the Net Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.net.server.<host>:<port>.bytesReceived}
 * |Total number of bytes received by the Net Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.net.server.<host>:<port>.bytesSent}
 * |Total number of bytes sent to the Net Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.net.server.<host>:<port>.errorCount}
 * |Total number of errors.
 *
 * |===
 *
 * === HTTP Server
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Gauge
 * |{@code vertx.http.server.<host>:<port>.connections}
 * |Number of opened connections to the HTTP Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.http.server.<host>:<port>.bytesReceived}
 * |Total number of bytes received by the HTTP Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.http.server.<host>:<port>.bytesSent}
 * |Total number of bytes sent to the HTTP Server listening on the {@code <host>:<port>} address.
 *
 * |Counter
 * |{@code vertx.http.server.<host>:<port>.errorCount}
 * |Total number of errors.
 *
 * |Gauge
 * |{@code vertx.http.client.<host>:<port>.requests}
 * |Number of requests being processed.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.requestCount}
 * |Total number of requests processed.
 *
 * |Counter
 * |{@code vertx.http.client.<host>:<port>.processingTime}
 * |Cumulated request processing time.
 *
 * |Gauge
 * |{@code vertx.http.client.<host>:<port>.wsConnections}
 * |Number of websockets currently opened.
 *
 * |===
 *
 * === Event Bus
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Gauge
 * |{@code vertx.eventbus.handlers}
 * |Number of event bus handlers.
 *
 * |Counter
 * |{@code vertx.eventbus.errorCount}
 * |Total number of errors.
 *
 * |Counter
 * |{@code vertx.eventbus.bytesWritten}
 * |Total number of bytes sent while sending messages to event bus cluster peers.
 *
 * |Counter
 * |{@code vertx.eventbus.bytesRead}
 * |Total number of bytes received while reading messages from event bus cluster peers.
 *
 * |Gauge
 * |{@code vertx.eventbus.pending}
 * |Number of messages not processed yet. One message published will count for {@code N} pending if {@code N} handlers
 * are registered to the corresponding address.
 *
 * |Gauge
 * |{@code vertx.eventbus.pendingLocal}
 * |Like {@code vertx.eventbus.pending}, for local messages only.
 *
 * |Gauge
 * |{@code vertx.eventbus.pendingRemote}
 * |Like {@code vertx.eventbus.pending}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.publishedMessages}
 * |Total number of messages published (publish / subscribe).
 *
 * |Counter
 * |{@code vertx.eventbus.publishedLocalMessages}
 * |Like {@code vertx.eventbus.publishedMessages}, for local messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.publishedRemoteMessages}
 * |Like {@code vertx.eventbus.publishedMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.sentMessages}
 * |Total number of messages sent (point-to-point).
 *
 * |Counter
 * |{@code vertx.eventbus.sentLocalMessages}
 * |Like {@code vertx.eventbus.sentMessages}, for local messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.sentRemoteMessages}
 * |Like {@code vertx.eventbus.sentMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.receivedMessages}
 * |Total number of messages received.
 *
 * |Counter
 * |{@code vertx.eventbus.receivedLocalMessages}
 * |Like {@code vertx.eventbus.receivedMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.receivedRemoteMessages}
 * |Like {@code vertx.eventbus.receivedMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.deliveredMessages}
 * |Total number of messages delivered to handlers.
 *
 * |Counter
 * |{@code vertx.eventbus.deliveredLocalMessages}
 * |Like {@code vertx.eventbus.deliveredMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.deliveredRemoteMessages}
 * |Like {@code vertx.eventbus.deliveredMessages}, for remote messages only.
 *
 * |Counter
 * |{@code vertx.eventbus.replyFailures}
 * |Total number of message reply failures.
 *
 * |Counter
 * |{@code vertx.eventbus.<address>.processingTime}
 * |Cumulated processing time for handlers listening to the {@code address}.
 *
 * |===
 *
 * == Vert.x pool metrics
 *
 * This section lists all the metrics generated by monitoring Vert.x pools.
 *
 * There are two types currently supported:
 *
 * * _worker_ (see {@link io.vertx.core.WorkerExecutor})
 * * _datasource_ (created with Vert.x JDBC client)
 *
 * Note that Vert.x creates two worker pools upfront, _vert.x-worker-thread_ and _vert.x-internal-blocking_.
 *
 * All metrics are prefixed with {@code <type>.<name>.}. For example, {@code worker.vert.x-internal-blocking.}.
 *
 * [cols="15,50,35", options="header"]
 * |===
 * |Metric type
 * |Metric name
 * |Description
 *
 * |Counter
 * |{@code vertx.pool.<type>.<name>.delay}
 * |Cumulated time waiting for a resource (queue time).
 *
 * |Gauge
 * |{@code vertx.pool.<type>.<name>.queued}
 * |Current number of elements waiting for a resource.
 *
 * |Counter
 * |{@code vertx.pool.<type>.<name>.queueCount}
 * |Total number of elements queued.
 *
 * |Counter
 * |{@code vertx.pool.<type>.<name>.usage}
 * |Cumulated time using a resource (i.e. processing time for worker pools).
 *
 * |Gauge
 * |{@code vertx.pool.<type>.<name>.inUse}
 * |Current number of resources used.
 *
 * |Counter
 * |{@code vertx.pool.<type>.<name>.completed}
 * |Total number of elements done with the resource (i.e. total number of tasks executed for worker pools).
 *
 * |Gauge
 * |{@code vertx.pool.<type>.<name>.maxPoolSize}
 * |Maximum pool size, only present if it could be determined.
 *
 * |Gauge
 * |{@code vertx.pool.<type>.<name>.inUse}
 * |Pool usage ratio, only present if maximum pool size could be determined.
 *
 * |===
 *
 * == User defined metrics
 *
 * Users can send their own metrics to the Hawkular server. In order to do so, the event bus metrics bridge must be
 * enabled:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#enableMetricsBridge()}
 * ----
 *
 * By default, the metrics bus handler is listening to the {@code hawkular.metrics} address. But the bridge address
 * can be configured:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#customMetricsBridgeAddress()}
 * ----
 *
 * The metrics bridge handler expects messages in the JSON format. The JSON object must at least provide a metric
 * {@code id} and a numerical {@code value}:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#userDefinedMetric()}
 * ----
 *
 * The handler will assume the metric is a gauge and will assign a timestamp corresponding to the time when the message was processed.
 * If the metric is a counter or availability, or if you prefer explicit configuration, set the {@code type} and/or {@code timestamp} attributes:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#userDefinedMetricExplicit()}
 * ----
 *
 * Note that Hawkular understands all timestamps as milliseconds since January 1, 1970, 00:00:00 UTC.
 *
 */
@ModuleGen(name = "vertx-hawkular", groupPackage = "io.vertx")
@Document(fileName = "index.adoc") package io.vertx.ext.hawkular;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;

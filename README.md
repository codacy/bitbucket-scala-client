[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc618d371b1a45f8a7129d868512a4fc)](https://www.codacy.com/gh/codacy/bitbucket-scala-client?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=codacy/bitbucket-scala-client&amp;utm_campaign=Badge_Grade)
[![Circle CI](https://circleci.com/gh/codacy/bitbucket-scala-client.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/codacy/bitbucket-scala-client)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codacy/bitbucket-scala-client_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codacy/bitbucket-scala-client_2.11)

# Bitbucket Scala client

This is a simple library that aims to have basic functions of the Bitbucket API.
This library is meant to support Codacy when interacting with Bitbucket.

### Usage

Import on SBT:

    "com.codacy" %% "bitbucket-scala-client" % "<VERSION>"

To build the `BitbucketClient` you need to pass a builder function from Unit => WSClient

#### Play 2.4

```Scala
import com.ning.http.client.AsyncHttpClientConfig
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}

() => {
  val config = new NingAsyncHttpClientConfigBuilder().build()
  val clientConfig = new AsyncHttpClientConfig.Builder(config).build()
  val client = new NingWSClient(clientConfig)
}
```

#### Play 2.7

```Scala
import akka.stream.Materializer
import play.api.libs.ws.ahc.AhcWSClient

() => {
  //You need to have an implicit Materializer in scope
  AhcWSClient()
}
```

### Creators

1. Rodrigo Fernandes

## What is Codacy?

[Codacy](https://www.codacy.com/) is an Automated Code Review Tool that monitors your technical debt, helps you improve your code quality, teaches best practices to your developers, and helps you save time in Code Reviews.

### Among Codacy’s features:

- Identify new Static Analysis issues
- Commit and Pull Request Analysis with GitHub, BitBucket/Stash, GitLab (and also direct git repositories)
- Auto-comments on Commits and Pull Requests
- Integrations with Slack, HipChat, Jira, YouTrack
- Track issues in Code Style, Security, Error Proneness, Performance, Unused Code and other categories

Codacy also helps keep track of Code Coverage, Code Duplication, and Code Complexity.

Codacy supports PHP, Python, Ruby, Java, JavaScript, and Scala, among others.

### Free for Open Source

Codacy is free for Open Source projects.

### License

bitbucket-scala-client is available under the The Apache Software License, Version 2.0.

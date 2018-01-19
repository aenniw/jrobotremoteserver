# Jrobot-remote-server

[![N|Solid](https://travis-ci.org/aenniw/jrobotremoteserver.svg?branch=master)](https://travis-ci.org/aenniw/jrobotremoteserver)
[![N|Solid](https://coveralls.io/repos/github/aenniw/jrobotremoteserver/badge.svg?branch=master)](https://coveralls.io/github/aenniw/jrobotremoteserver?branch=master)

Jrobot-remote-server is a [remote server](https://code.google.com/p/robotframework/wiki/RemoteLibrary) for [Robot Framework](http://robotframework.googlecode.com/) implemented in Java.
Documentation can be found on the project [wiki](https://github.com/ombre42/jrobotremoteserver/wiki).
The original jrobotremoteserver can be found on [Google Code](http://code.google.com/p/jrobotremoteserver/).

#### Building

Maven is used as build tool for jrobot-remote-server.
You can simply do a ```mvn clean install``` in the project directory.

#### Robot Tests

There are some number of robot acceptance tests under `jrobot-karaf/src`.

#### Dependencies

```
<dependency>
    <groupId>com.github.aenniw</groupId>
    <artifactId>jrobot-remote-server</artifactId>
    <version>3.5.1</version>
    <type>pom</type>
</dependency>
```

```
<dependency>
    <groupId>com.github.aenniw</groupId>
    <artifactId>jrobot-remote-server</artifactId>
    <version>3.6-SNAPSHOT</version>
    <type>pom</type>
</dependency>
```

#### License

Jrobot-remote-server is open source software provided under under [Apache License 2.0](http://apache.org/licenses/LICENSE-2.0)

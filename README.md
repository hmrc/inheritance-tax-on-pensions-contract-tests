# Inheritance Tax On Pensions Contract Tests

## Pre-requisites

1. Make sure you run all the dependant services through the service manager:

> `sm2 --start IHTP_ALL`

To note in the scenario you want to run a specific version of a microservice. Stop the specific frontend, backend or stubs microservice from the service manager, and run it locally.

> `Example: sm2 --stop INHERITANCE_TAX_ON_PENSIONS_STUBS`

> `sbt run -Dplay.http.router=testOnlyDoNotUseInAppConf.Routes`

## Tests

The default target is `http://localhost:10712`. 

### To run all tests:

```bash
./run-tests.sh local
```

Omitting the argument also selects `local`. For an IDE run, use the VM option
`-Denvironment=local`, not `-Denv=local`.

To override the local host or run the suite directly:

```bash
sbt -Denvironment=local -Dlocal.hip.host=http://localhost:10712 test
```

## Scalafmt

Check all project files are formatted as expected as follows:

```bash
sbt scalafmtCheckAll scalafmtSbtCheck
```

Format `*.sbt` and `project/*.scala` files as follows:

```bash
sbt scalafmtSbt
```

Format all project files as follows:

```bash
sbt scalafmtAll
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

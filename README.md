# inheritance-tax-on-pensions-contract-tests

Contract tests for the IHTP payment-notice API. The current smoke test posts a
payment notice and checks the 201 response contains a form bundle number and
IHT payment reference, using the current stub contract.

## Pre-requisites

Use JDK 21 and sbt. Start the local stub:

```bash
sm2 --start INHERITANCE_TAX_ON_PENSIONS_STUBS
```

## Tests

The default target is `http://localhost:10712`. Run all tests:

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

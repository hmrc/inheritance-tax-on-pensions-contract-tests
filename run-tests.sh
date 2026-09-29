#!/usr/bin/env bash

set -euo pipefail

ENVIRONMENT=${1:-local}

sbt -Denvironment="$ENVIRONMENT" clean test

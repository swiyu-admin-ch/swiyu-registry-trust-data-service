<!--
SPDX-FileCopyrightText: 2025 Swiss Confederation

SPDX-License-Identifier: MIT
-->

![github-banner](https://github.com/swiyu-admin-ch/swiyu-admin-ch.github.io/blob/main/assets/images/github-banner.jpg)

# Registry trust data

The datastore services are the backbone of the Swiss SSI inspired ecosystem.
This repository does contain the current readonly, public facing implementation for the trust registry.

With the trust registry entities can prove and maintain their status as verified issuers or verifiers, ensuring trust within the ecosystem. 
Users will be able to see the verification status of issuers and verifiers in their wallets.

## Table of Contents

- [Overview](#Overview)
- [Installation](#installation)
- [Usage](#usage)
- [Contributions and feedback](#contributions-and-feedback)
- [License](#license)

## Overview

For a general overview of the public beta environment and its components, please check
the [Public Beta context](https://swiyu-admin-ch.github.io/open-source-components/#public-beta).

This service provides all the protocol conform read operations.

The write operations are done in the [swiyu-trust-management-scs](https://github.com/swiyu-admin-ch/swiyu-trust-management-scs).

## Installation

> [!NOTE]
> Starting the service with the local profile does not connect the data and authoring services. It only serves to start this service locally.

In order to start the service locally, run:

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The openapi definition can then be found [here](http://localhost:8390/swagger-ui.html)

Note: if you want to run the application alongside with the other services of SWIYU, you can run

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=local,shared
```

This will assume that the swiyu-trust-management-scs was already started the same way (which provides the DB at port 5380 via
its docker-compose config).

## Usage

### Auth

The data service is always unprotected as all data on the registers should be readable by everyone.  
The swiyu-trust-management-scs do need protection, which is as of now handled by the infrastructure.  
We do recommend to add mTLS authentication to your infrastructure endpoints.

## Contributions and feedback

The code for this repository is developed privately and will be released after each sprint. The published code can therefore only be a snapshot of the current development and not a thoroughly tested version. However, we welcome any feedback on the code regarding both the implementation and security aspects. Please follow the guidelines for contributing found in [CONTRIBUTING.md](/CONTRIBUTING.md).

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](/LICENSE) file for details.

# Cyberia

A simple Pleroma client.

## Installation

Download from [Releases](https://github.com/Tsuribori/cyberia/releases)

## Build from source

### Requirements

* [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases) in `$PATH` with `native-image` installed
* Leiningen

### Build

`lein native-image`

## Usage

`cyberia [global-options] command [command options] [arguments...]`

## Examples

Obtain OAuth2 token:

```shell
cyberia login "https://example.com"
```

Post a status:

```shell
cyberia post "Everyone's Connected"
cyberia p "Everyone's Connected"
```

Post a status with sensitive file:

```shell
cyberia post --sensitive --file /path/to/protocol7.jpeg "Schumann resonance"
cyberia p -s -f /path/to/protocol7.jpeg "Schumann resonance"
```
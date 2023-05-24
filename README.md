# User Interface for the Obyte Decentralized Crypto Fund

This project provides a user interface for the [Obyte Decentralized Crypto Fund](https://github.com/pmiklos/obyte-crypto-fund).
It is plain HTML and Javascript, so it can be hosted on any web server or run locally.

For a fully decentralized experience, simply clone this repository and follow the instructions below to run the app on your local machine.

You can try out the Crypto Fund with no financial risk by visiting the [live demo page](https://pmiklos.github.io/obyte-crypto-fund-ui)
which is a hosted instance of this project that connects to the Obyte testnet.


## Run the app

The simplest way to get started quickly is to launch a webpack dev server that will serve the app on `http://localhost:8080`: 
```bash
./gradlew jsBrowserRun
```

## Build distribution

Building a distribution is useful if you want to host the app on a web server or run it locally without a dev server.
```bash
./gradle assemble
```

This will compile the Kotlin source into a Javacript file and place it under `build/distributions`:

```bash
$ ls build/distributions/
index.html  obyte-crypto-fund-ui.js  obyte-crypto-fund-ui.js.map
```
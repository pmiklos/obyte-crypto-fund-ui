# User Interface for the Obyte Decentralized Crypto Fund

NOTE: The current version is in early development and only shows mock data,
see [live demo page](https://pmiklos.github.io/obyte-crypto-fund-ui).

## Run the app

```bash
./gradlew jsBrowserRun
```

## Build distribution

```bash
./gradle assemble
```

This will compile the Kotlin source into a Javacript file and place it under `build/distributions`:

```bash
$ ls build/distributions/
index.html  obyte-crypto-fund-ui.js  obyte-crypto-fund-ui.js.map
```
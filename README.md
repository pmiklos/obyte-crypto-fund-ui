# User Interface for the Obyte Decentralized Crypto Fund

NOTE: The current version is in early development and only shows mock data.

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
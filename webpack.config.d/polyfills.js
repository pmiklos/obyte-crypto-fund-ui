const webpack = require('webpack');

config.resolve.fallback = {
    "stream": require.resolve("stream-browserify"),
    "buffer": require.resolve("buffer/")
}
config.plugins = [
    new webpack.ProvidePlugin({
        Buffer: ['buffer', 'Buffer'],
    }),
]
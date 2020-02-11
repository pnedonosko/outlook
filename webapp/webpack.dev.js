const path = require('path');
const merge = require('webpack-merge');
const webpackCommonConfig = require('./webpack.common.js');

// the display name of the war
const app = 'outlook';

// add the server path to your server location path
const exoServerPath = "/eXo/outlook/platform-6.0.0-M16";

let config = merge(webpackCommonConfig, {
  output: {
    path: path.resolve(`${exoServerPath}/webapps/${app}/`)
  },
  devtool: 'inline-source-map'
});

config.mode = "development";

module.exports = config;

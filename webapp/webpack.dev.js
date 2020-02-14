const path = require("path");
const merge = require("webpack-merge");
const webpackCommonConfig = require("./webpack.common.js");

// the display name of the war
const app = "outlook";

// add the server path to your server location path
const exoServerPath = process.env.EXO_OUTLOOK_PLATFORM;
//const exoServerPath = "/eXo/outlook/platform-6.0.0-M16";
// const exoServerPath = 'D:/sasha_work/2020-02/platform3/platform-6.0.0-M16';

let config = merge(webpackCommonConfig, {
  output: {
    path: path.resolve(`${exoServerPath}/webapps/${app}/`)
  },
  devtool: "inline-source-map"
});

config.mode = "development";

module.exports = config;

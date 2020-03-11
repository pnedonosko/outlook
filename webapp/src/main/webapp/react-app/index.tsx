import * as React from "react";
import * as ReactDOM from "react-dom";
import { initializeIcons } from "@uifabric/icons";
import OutlookApp from "./OutlookApp";
import "./index.less";

initializeIcons("https://static2.sharepointonline.com/files/fabric/assets/icons/");
console.log(window["eXo"]);
// let isOfficeInitialized = false;
const eXo = window["eXo"];

const render = (componentName: string) => {
  ReactDOM.render(
    <OutlookApp
      componentName={componentName}
      userUrl={eXo.env.app.rest.user}
      userName={eXo.env.portal.userName}
    />,
    document.getElementById("outlook-app")
  );
};

/* Render application after Office initializes */
Office.initialize = () => {
  // isOfficeInitialized = true;
  render(eXo.env.app.command);
};

/* Initial render showing a progress bar */
render(eXo.env.app.command);

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js

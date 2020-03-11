import * as React from "react";
import * as ReactDOM from "react-dom";
import { initializeIcons } from "@uifabric/icons";
import OutlookApp from "./OutlookApp";
import "./index.less";

initializeIcons("https://static2.sharepointonline.com/files/fabric/assets/icons/");

// let isOfficeInitialized = false;

// variable that define what component should be loaded
const settings = {
  baseUrl: "/outlook/app/v2/exo",
  command: "ConvertToActivity",
  userName: "$UID"
};

const render = (componentName: string) => {
  ReactDOM.render(
    <OutlookApp componentName={componentName} baseUrl={settings.baseUrl} userName={settings.userName} />,
    document.getElementById("outlook-app")
  );
};

/* Render application after Office initializes */
Office.initialize = () => {
  // isOfficeInitialized = true;
  render(settings.command);
};

/* Initial render showing a progress bar */
render(settings.command);

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js
// service URL should link to a starting point in our HATEOAS/HAL
// const props = {eXo: eXo, service: "/outlook/app"}

// TODO temporal for dev tests
//import './index.css' // TODO cleanup

//const props = {eXo: 'loginForm', method: 'POST', action: '/perform_login', inputs: inputs}
//const params = new URLSearchParams(window.location.search)

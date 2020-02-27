import * as React from "react";
import * as ReactDOM from "react-dom";
import { initializeIcons } from "@uifabric/icons";
import OutlookApp from "./OutlookApp";

initializeIcons();

// let isOfficeInitialized = false;

// variable that define what component should be loaded
const command = "ConvertToForum";

const render = (componentName: string) => {
  ReactDOM.render(<OutlookApp componentName={componentName} />, document.getElementById("outlook-app"));
};

/* Render application after Office initializes */
Office.initialize = () => {
  // isOfficeInitialized = true;
  render(command);
};

/* Initial render showing a progress bar */
render(command);

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js
// service URL should link to a starting point in our HATEOAS/HAL
// const props = {eXo: eXo, service: "/outlook/app"}

// TODO temporal for dev tests
//import './index.css' // TODO cleanup

//const props = {eXo: 'loginForm', method: 'POST', action: '/perform_login', inputs: inputs}
//const params = new URLSearchParams(window.location.search)

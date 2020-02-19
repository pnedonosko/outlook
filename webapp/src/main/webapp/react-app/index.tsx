import * as React from "react";
import * as ReactDOM from "react-dom";
import { initializeIcons } from "@uifabric/icons";

initializeIcons();

let isOfficeInitialized = false;
const exoVariables = {
  context: window["eXo"].env.portal.context,
  rest: window["eXo"].env.portal.rest,
  language: window["eXo"].env.portal.language,
  outlook: window["eXo"].env.app.outlook,
  spring: window["eXo"].env.app.spring
};

// variable that define what component should be loaded
const command = "SaveAttachments";

const render = (componentName: string) => {
  import(`./containers/${componentName}`).then(module => {
    ReactDOM.render(
      <module.default isOfficeInitialized={isOfficeInitialized} {...exoVariables} />,
      document.getElementById("outlook-app")
    );
  });
};

/* Render application after Office initializes */
Office.initialize = () => {
  isOfficeInitialized = true;
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

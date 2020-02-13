import * as React from 'react';
import * as ReactDOM from 'react-dom';
import SaveAttachments from './containers/SaveAttachments/save-attachments';

let isOfficeInitialized = false;
const exoVariables = {
    context: window['eXo'].env.portal.context,
    rest: window['eXo'].env.portal.rest,
    language: window['eXo'].env.portal.language,
    outlook: window['eXo'].env.app.outlook,
    spring: window['eXo'].env.app.spring
};

const render = Component => {
    ReactDOM.render(<Component isOfficeInitialized={isOfficeInitialized} {...exoVariables} />, document.getElementById('outlook-app'));
};

/* Render application after Office initializes */
Office.initialize = () => {
    isOfficeInitialized = true;
    render(SaveAttachments);
  };
  
  /* Initial render showing a progress bar */
  render(SaveAttachments);
  

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js
// service URL should link to a starting point in our HATEOAS/HAL
// const props = {eXo: eXo, service: "/outlook/app"}
// ReactDOM.render(
    // <App {...props} />,
    // document.getElementById('outlook-app')
// );

// TODO temporal for dev tests
//import './index.css' // TODO cleanup

//const props = {eXo: 'loginForm', method: 'POST', action: '/perform_login', inputs: inputs}
//const params = new URLSearchParams(window.location.search)


import * as React from 'react';
import * as ReactDOM from 'react-dom';
import App from './app';

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js
// service URL should link to a starting point in our HATEOAS/HAL
// const props = {eXo: eXo, service: "/outlook/app"}
ReactDOM.render(
	// <App {...props} />,
	<App />,
	document.getElementById('outlook-app')
);

// TODO temporal for dev tests
//import './index.css' // TODO cleanup

//const props = {eXo: 'loginForm', method: 'POST', action: '/perform_login', inputs: inputs}
//const params = new URLSearchParams(window.location.search)


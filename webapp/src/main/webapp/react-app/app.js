import React, { Component } from "react";
import "./app.less";
import TheApp from "./TheApp/TheApp";

class App extends Component {
  // TODO real app name & markup
  render() {
    return (
      <div className="the-app">
        <TheApp></TheApp>
      </div>
    );
  }
}

export default App;

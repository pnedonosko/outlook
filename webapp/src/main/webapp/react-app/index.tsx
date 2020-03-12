import * as React from "react";
import * as ReactDOM from "react-dom";
import { initializeIcons } from "@uifabric/icons";
import OutlookApp from "./OutlookApp";
import "./index.less";
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import XHR from "i18next-xhr-backend";
import { Spinner, SpinnerSize } from "office-ui-fabric-react";

initializeIcons("https://static2.sharepointonline.com/files/fabric/assets/icons/");
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

i18n
  .use(XHR)
  .use(initReactI18next)
  .init({
    lng: eXo.env.portal.language,
    fallbackLng: "en",
    backend: {
      loadPath: eXo.env.app.rest.resourcesBundle
    },
    load: "all",
    saveMissing: true,
    keySeparator: false,
    interpolation: {
      escapeValue: false
    }
  })
  .then(() => {
    /* Render application after Office initializes */
    Office.initialize = () => {
      render(eXo.env.app.command);
    };

    /* Initial render showing a progress bar, should be replaced by application shell */
    <Spinner size={SpinnerSize.large} />
  });

// TODO make eXo non global, e.g. via AMD wrapping the app bundle.js

eXo = {};
eXo.env = {};
eXo.env.portal = {};
eXo.env.app = {};
eXo.env.app.rest = {};

eXo.env.portal.context = [[${baseName}]];
console.log("context: " + eXo.env.portal.context);
eXo.env.portal.rest = [[${currentRestContextName}]];
console.log("rest: " + eXo.env.portal.rest);
eXo.env.portal.language = [[${language}]];
console.log("language: " + eXo.env.portal.language);

// getting language of user
const lang = eXo && eXo.env && eXo.env.portal && eXo.env.portal.language || 'en';
console.log("lang: " + lang);

const resourceBundleName = 'locale.outlook.Outlook';
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;
console.log("url: " + url);

eXo.env.app.rest.resourcesBundle = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle`;
console.log("resourcesBundle: " + eXo.env.app.rest.resourcesBundle);
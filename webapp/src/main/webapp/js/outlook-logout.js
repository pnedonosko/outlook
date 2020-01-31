/**
 * log out of user session
 */
var logout = function () {
  // Dispatch logout event
  var logoutEvent = document.createEvent('Event');
  logoutEvent.initEvent('exo-logout', true, true);
  window.dispatchEvent(logoutEvent);
  // Perform logout
  //window.location = createPortalURL("UIPortal", "Logout", false);
  $.get(createPortalURL("UIPortal", "Logout", false), function (data) {
    console.log("successful logout");
    window.location.reload(true);
  });
};

var createPortalURL = function (targetComponentId, actionName, useAjax, params) {
  var href = "/portal/intranet?portal:componentId=" + targetComponentId + "&portal:action=" + actionName;

  if (params != null) {
    var len = params.length;
    for (var i = 0; i < len; i++) {
      href += "&" + params[i].name + "=" + params[i].value;
    }
  }
  if (useAjax) href += "&ajaxRequest=true";
  return href;
};
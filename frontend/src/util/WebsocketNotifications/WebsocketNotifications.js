import React from 'react';

import Cookies from "universal-cookie";
import {notification} from "antd";

const WS_GATEWAY_ADDRESS=process.env.REACT_APP_WS_GATEWAY_ADDRESS;

function showNotification(title, message, style={}) {
  notification.open({
    message: title,
    description: message,
    style: style
  });
}

function onError(ev) {
  const message = "Cannot establish connection with notification server. Try refresh your page.";
  showNotification("Error!", message);
  console.log(ev);
}

function onMessage(ev) {
  const data = JSON.parse(ev.data);

  switch (data.type) {
    case "SUCCESS_MESSAGE":
      showNotification("Success!", data.payload, {border: "1px solid #d9f7be"});
      break;
    case "ERROR_MESSAGE":
      showNotification("Error!", data.payload, {border: "1px solid #ffccc7"});
      break;
    case "ALERT_MESSAGE":
      showNotification("", data.payload, {border: "1px solid #bae7ff"});
      break;
    case "SESSION_ID":
      const cookies = new Cookies();
      cookies.set("user-ws-session-id", data.payload)
      break;
  }
}

function initNotifications() {
  const socket = new WebSocket(`${WS_GATEWAY_ADDRESS}/notification-channel/ws/notifications`);

  socket.onerror = ev => onError(ev);
  socket.onmessage = ev => onMessage(ev);
}

export default initNotifications;

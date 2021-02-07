import React from 'react';
import './LandingPage.css';

import Home from "../../containers/Home/Home";
import Register from "../../containers/Register/Register";
import Cookies from "universal-cookie";
import {notification} from "antd";

const WS_GATEWAY_ADDRESS=process.env.REACT_APP_WS_GATEWAY_ADDRESS;

class SmileOutlined extends React.Component<{ style: { color: string } }> {
    render() {
        return null;
    }
}

function showNotification(title, message) {
    notification.open({
        message: title,
        description: message,
        icon: <SmileOutlined style={{ color: '#108ee9' }} />,
    });
}

const LandingPage = () => {
    const socket = new WebSocket(`${WS_GATEWAY_ADDRESS}/notification-channel/ws/notifications`);
    socket.onerror = ev => console.log("close", ev);
    socket.onopen = ev => console.log("open", ev);
    socket.onclose = ev => console.log("close", ev);
    socket.onmessage = ev => {
        const data = JSON.parse(ev.data);
        console.log(data);

        switch (data.type) {
            case "SESSION_ID":
                console.log("session")
                const cookies = new Cookies();
                cookies.set("user-ws-session-id", data.payload)
                break;
            case "SUCCESS_MESSAGE":
                showNotification("Success!", data.payload)
                console.log("message")
                console.log(data.payload);
                break;
            default:
                console.log(data)
                break;
        }
    };

    return (
        <>
            <Home />
            <Register />
        </>
    )
};

export default LandingPage;

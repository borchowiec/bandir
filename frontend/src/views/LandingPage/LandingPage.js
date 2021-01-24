import React from 'react';
import './LandingPage.css';

import Home from "../../containers/Home/Home";
import Register from "../../containers/Register/Register";
import Cookies from "universal-cookie";
import {notification} from "antd";

const GATEWAY_ADDRESS=process.env.REACT_APP_GATEWAY_ADDRESS;

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
    const socket = new WebSocket(`wss://localhost:8080/ws/users`);
    socket.onerror = ev => console.log("close");
    socket.onopen = ev => console.log("open");
    socket.onclose = ev => console.log("close");
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
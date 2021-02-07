import React from 'react';

import initNotifications from "../../util/WebsocketNotifications/WebsocketNotifications";

import './LandingPage.css';
import Home from "../../containers/Home/Home";
import Register from "../../containers/Register/Register";

const LandingPage = () => {
    initNotifications();

    return (
        <>
            <Home />
            <Register />
        </>
    )
};

export default LandingPage;

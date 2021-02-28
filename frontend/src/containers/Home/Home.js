import React from 'react';
import './Home.css';
import {Button, Col, Row} from "antd";
import axios from 'axios';

import logo from '../../assets/svg/logo.svg';
import wave from '../../assets/svg/white-wave.svg';
import FormInput from "../../components/FormInput/FormInput";
import FormCard from "../../components/FormCard/FormCard";
import {useInput} from "../../hooks/useInput";
import Cookies from "universal-cookie";
import {showNotification} from "../../util/WebsocketNotifications/WebsocketNotifications";

const GATEWAY_ADDRESS = process.env.REACT_APP_GATEWAY_ADDRESS;

const Home = () => {
    const {value: usernameOrEmail, bind: bindUsernameOrEmail} = useInput('');
    const {value: password, bind: bindPassword} = useInput('');

    function signIn() {
        const body = {
            usernameOrEmail: usernameOrEmail,
            password: password
        };

        const headers = {
            "user-ws-session-id": new Cookies().get("user-ws-session-id")
        };

        axios.post(`${GATEWAY_ADDRESS}/auth/authenticate`, body, {
            headers: headers
        })
            .then(function (response) {
                const {type, token} = response.data;
                console.log(`${type} ${token}`);
                new Cookies().set("Authentication", `${type} ${token}`);
                showNotification("Success", "You are logged in!", {border: "1px solid #d9f7be"});
            })
            .catch(function (error) {
                console.log(error);
                console.log(error.data);
            });
    }

    return (
        <div id="homeSection">
            <Row>
                <Col sm={24} lg={12} className="leftColumn" style={{width: "100%"}}>
                    <div className="logoContainer">
                        <img src={logo} alt="logo"/>
                        <h1>bandir</h1>
                        <p>Improve the quality of your gigs</p>
                    </div>
                </Col>
                <Col sm={24} lg={12} className="rightColumn">
                    <FormCard title="LOGIN">
                        <FormInput placeholder="Username or Email" {...bindUsernameOrEmail} />
                        <FormInput placeholder="Password" type="password" {...bindPassword} />
                        <Button type="primary" className="loginBtn" onClick={() => signIn()}>Log in</Button>
                        <p>
                            Not registered? <span style={{
                            color: "#722ED1",
                            cursor: "pointer"
                        }}>Create an account.</span> {/* todo redirect to registration */}
                        </p>
                    </FormCard>
                </Col>
            </Row>
            <img src={wave} alt="wave" style={{width: "100%", position: "relative", bottom: "-2px", zIndex: 0}}/>
        </div>
    );
}

export default Home;

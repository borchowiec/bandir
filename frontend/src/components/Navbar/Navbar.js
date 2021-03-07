import React, {useState, useEffect} from 'react';
import {Button, Col, Layout, Row} from 'antd';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSignOutAlt, faUser } from '@fortawesome/free-solid-svg-icons'

import axios from 'axios';
const {Header} = Layout;

const GATEWAY_ADDRESS=process.env.REACT_APP_GATEWAY_ADDRESS;

const Navbar = props => {
    const [principal, setPrincipal] = useState();

    const buttonStyle = {
        border: 0,
        color: "#595959",
        fontSize: "12px",
        fontStyle: "Segoe UI"
    }

    useEffect(async () => {
        axios.get(`${GATEWAY_ADDRESS}/user`)
            .then(function (response) {
                setPrincipal(response.data);
            })
            .catch(function (error) {
                console.log(error.data);
            });
    }, []);

    return (
        <Header style={{backgroundColor: "#fff", height: "64px"}}>
            <Row>
                <Col>
                    {props.children}
                </Col>
                <Col flex="auto"/>
                <Col>
                    <Button style={buttonStyle}>
                        <FontAwesomeIcon style={{marginRight: "5px"}} icon={faSignOutAlt}/> Logout {/*todo logout*/}
                    </Button>
                    <Button style={buttonStyle}>
                        <FontAwesomeIcon style={{marginRight: "5px"}} icon={faUser}/> {principal ? principal.username : ""}
                    </Button>
                </Col>
            </Row>
        </Header>
    )
};

export default Navbar;

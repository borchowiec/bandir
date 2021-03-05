import React from 'react';
import {Button, Col, Layout, Row} from 'antd';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSignOutAlt, faUser } from '@fortawesome/free-solid-svg-icons'

const {Header} = Layout;

const Navbar = props => {
    const buttonStyle = {
        border: 0,
        color: "#595959",
        fontSize: "12px",
        fontStyle: "Segoe UI"
    }

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
                        <FontAwesomeIcon style={{marginRight: "5px"}} icon={faUser}/> John {/*todo get username*/}
                    </Button>
                </Col>
            </Row>
        </Header>
    )
};

export default Navbar;

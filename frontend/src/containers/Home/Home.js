import React from 'react';
import './Home.css';
import {Button, Col, Input, Row} from "antd";

import logo from '../../assets/svg/logo.svg';
import wave from '../../assets/svg/white-wave.svg';

const Home = () => (
  <div id="homeSection">
    <Row>
      <Col span={12} className="leftColumn">
        <div>
          <img src={logo} alt="logo" />
          <h1>bandir</h1>
          <p>Improve the quality of your gigs</p>
        </div>
      </Col>
      <Col span={12} className="rightColumn">
        <div className="loginForm_container">
          <h2>Login</h2>
          <Input placeholder="Username" />
          <Input placeholder="Password" />
          <Button type="primary" className="loginBtn">Log in</Button> {/* todo login btn */}
          <p>
            Not registered? <span style={{color: "#722ED1", cursor: "pointer"}}>Create an account.</span> {/* todo redirect to registration */}
          </p>
        </div>
      </Col>
    </Row>
    <img src={wave} alt="wave" style={{width: "100%", position: "relative"}}/>
  </div>
);

export default Home;

import React from 'react';
import './Register.css';
import {Button, Col, Row} from "antd";

import wave from '../../assets/svg/violet-wave.svg';
import band from '../../assets/svg/band.svg';
import FormCard from "../../components/FormCard/FormCard";
import FormInput from "../../components/FormInput/FormInput";

const formInputStyle = {
  fontSize: "18px",
  marginBottom: "27px",
  height: "52px"
}

const Register = () => (
  <div id="registerSection">
    <Row style={{position: "absolute", width: "100%", height: "100%", zIndex: 10}}>
      <Col span={12} style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        height: "100%"
      }}>
        <img src={band} alt="wave" style={{width: "100%", position: "relative", zIndex: 1}}/>
      </Col>
      <Col span={12} style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        height: "100%"
      }}>
        <FormCard title="Create Account">
          <FormInput placeholder="Username" style={formInputStyle} />
          <FormInput placeholder="Password" style={formInputStyle} />
          <FormInput placeholder="Confirm password" style={formInputStyle} />
          <FormInput placeholder="Email" style={formInputStyle} />
          <div className="socialButtonsContainer" style={{textAlign: "left"}}>
            <Button style={{backgroundColor: "#3B5998"}}>Facebook</Button>
          </div>
          <div className="socialButtonsContainer" style={{textAlign: "right"}}>
            <Button style={{backgroundColor: "#EA4335"}}>Gmail</Button>
          </div>
          <Button className="signUpButton">SIGN UP</Button>
        </FormCard>
      </Col>
    </Row>
    <img src={wave} alt="wave" style={{width: "100%", position: "relative", zIndex: 1}}/>
  </div>
);

export default Register;

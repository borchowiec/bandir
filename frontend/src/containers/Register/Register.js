import React from 'react';
import './Register.css';
import {Button, Col, Row} from "antd";

import wave from '../../assets/svg/violet-wave.svg';
import band from '../../assets/svg/band.svg';
import bandWithoutPlate from '../../assets/svg/bandWithoutPlate.svg';
import FormCard from "../../components/FormCard/FormCard";
import FormInput from "../../components/FormInput/FormInput";

const formInputStyle = {
  fontSize: "18px",
  marginBottom: "27px",
}

const Register = () => (
  <div id="registerSection">
    <Row style={{width: "100%", height: "100%", zIndex: 10}}>
      <Col sm={24} lg={12} style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center"
      }}>
        <Col xs={0} lg={24}>
          <img src={band} alt="wave" style={{width: "100%", position: "relative", zIndex: 1}}/>
        </Col>
        <Col sm={24} lg={0}>
          <img src={bandWithoutPlate} alt="wave" style={{width: "100%", position: "relative", zIndex: 1}}/>
        </Col>
      </Col>
      <Col sm={24} lg={12} style={{
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
      <img src={wave} alt="wave" className="purpleWave" style={{width: "100%", position: "absolute", zIndex: -1}}/>
    </Row>
  </div>
);

export default Register;

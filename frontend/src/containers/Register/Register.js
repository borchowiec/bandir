import React from 'react';
import './Register.css';
import {Col, Row} from "antd";

import wave from '../../assets/svg/violet-wave.svg';

const Register = () => (
  <div id="registerSection">
    <Row>
      <Col span={12}>
      </Col>
      <Col span={12}>
      </Col>
    </Row>
    <img src={wave} alt="wave" style={{width: "100%", position: "absolute", zIndex: 1}}/>
  </div>
);

export default Register;

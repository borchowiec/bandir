import React from 'react';
import {Input} from "antd";

import './FormInput.css';

const FormInput = props => (
    <Input className="formInput" {...props} />
);

export default FormInput;

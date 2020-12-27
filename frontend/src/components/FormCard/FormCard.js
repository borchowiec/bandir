import React from 'react';
import './FormCard.css';

const FormCard = props => (
  <div className="loginForm_container">
    {props.title ? <h2>{props.title}</h2> : ""}
    {props.children}
  </div>
);

export default FormCard;

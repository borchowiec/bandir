import React from 'react';

const EmptySider = props => (
    <div style={{
        backgroundColor: "white",
        padding: "22px 59px 22px 59px"
    }}>
        <h1 style={{fontSize: "42px", color: "#ABA9A9", fontFamily: "Segoe UI"}}>{props.title}</h1>
        <div style={{marginTop: "21px"}}>
            {props.children}
        </div>
    </div>
);

export default EmptySider;

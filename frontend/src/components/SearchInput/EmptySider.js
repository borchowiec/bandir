import React from 'react';
import {Layout} from 'antd';

const {Sider} = Layout;

const EmptySider = props => (
    <Sider
        width={"256px"}
        style={{
            background: "transparent linear-gradient(180deg, #22075E 0%, #120338 100%) 0% 0% no-repeat padding-box",
        }}>
        <h2 style={{
            textAlign: "center",
            fontWeight: "normal",
            color: "#fff",
            fontFamily: "Fjalla One",
            fontSize: "42px",
            marginTop: "5px"
        }}>bandir</h2>
        <div>
            {props.children}
        </div>
    </Sider>
);

export default EmptySider;

import React from 'react';

import {Button, Col, Input, Layout, Row} from 'antd';
import initNotifications from "../../util/WebsocketNotifications/WebsocketNotifications";
import EmptySider from "../../components/EmptySider/EmptySider";
import Navbar from "../../components/Navbar/Navbar";
import NavChildren from "../../components/EmptySider/NavChildren/NavChildren";
import SearchInput from "../../components/SearchInput/NavChildren/SearchInput";
import ContentCard from "../../components/ContentCard/ContentCard";

const { Content } = Layout;

const AddSong = () => {
    // initNotifications();

    const inputStyle = {
        background: "#FFFFFF",
        border: "1px solid #00000026",
        height: "52px",
        fontStyle: "italic",
        fontFamily: "Segoe UI",
        fontSize: "18px",
        color: "#00000040",
        borderRadius: 0,
        width: "100%"
    }

    return (
        <Layout>
            <EmptySider>
                <NavChildren/>
            </EmptySider>
            <Layout style={{backgroundColor: "#F0F2F5"}}>
                <Navbar>
                    <SearchInput />
                </Navbar>
                <Content style={{padding: "20px"}}>
                    <Row>
                        <Col sm={24}>
                            <ContentCard title="Basic information">
                                <Row>
                                    <Col style={{padding: "0 10px"}} md={12}>
                                        <Input style={inputStyle} placeholder="Title"/>
                                    </Col>
                                    <Col style={{padding: "0 10px"}} md={12}>
                                        <Input style={inputStyle} placeholder="Album"/>
                                    </Col>
                                </Row>
                                <Row style={{marginTop: "23px"}}>
                                    <Col style={{padding: "0 10px"}} md={12}>
                                        <Input style={inputStyle} placeholder="Artist"/>
                                    </Col>
                                    <Col style={{padding: "0 10px"}} md={12}>
                                        <Input style={inputStyle} placeholder="Genre"/>
                                    </Col>
                                </Row>
                            </ContentCard>
                        </Col>
                        <Col sm={24} style={{backgroundColor: "#FFF", marginTop: "10px"}}>
                            <ContentCard title="Content">
                                <textarea>
                                    {/*todo rich editor*/}
                                </textarea>
                            </ContentCard>
                        </Col>

                        <Col sm={24} style={{backgroundColor: "#FFF", marginTop: "10px"}}>
                            <Button type="primary" block>Add</Button> {/*todo add song*/}
                        </Col>
                    </Row>
                </Content>
            </Layout>
        </Layout>
    )
};

export default AddSong;

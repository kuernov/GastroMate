import React, { useState } from "react";
import { Layout, Menu } from "antd";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import "antd/dist/reset.css";

const { Header, Content, Footer } = Layout;

const App = () => {
    const [currentPage, setCurrentPage] = useState("login");

    const renderPage = () => {
        switch (currentPage) {
            case "login":
                return <LoginPage setCurrentPage={setCurrentPage} />;
            case "register":
                return <RegisterPage setCurrentPage={setCurrentPage} />;
            default:
                return <LoginPage setCurrentPage={setCurrentPage} />;
        }
    };

    return (
        <Layout className="layout">
            <Header>
                <Menu theme="dark" mode="horizontal" selectedKeys={[currentPage]}>
                    <Menu.Item key="login" onClick={() => setCurrentPage("login")}>
                        Login
                    </Menu.Item>
                    <Menu.Item key="register" onClick={() => setCurrentPage("register")}>
                        Register
                    </Menu.Item>
                </Menu>
            </Header>

            <Content style={{ padding: "50px 50px" }}>
                {renderPage()}
            </Content>

            <Footer style={{ textAlign: "center" }}>
                Ant Design Example ©2024 Created by You
            </Footer>
        </Layout>
    );
};

export default App;

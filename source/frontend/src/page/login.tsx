import { Alert, Button, Col, Form, Input, Row } from "antd";
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import React, { Fragment, useState } from "react";
import ApiConfig from "../configuration/apiConfig";
import { LoginRequest, UserRestServiceApi } from "../rest";
import ErrorCode from "../error/errorCode";

const Login = () => {
    const [username, changeUsername] = useState("");
    const [password, changePassword] = useState("");
    const [btnLoading, toggleLoading] = useState(false);
    const [hasError, toggleError] = useState(false);
    const [errorMessage, changeErrorMessage] = useState("");
    const userApi = new UserRestServiceApi(ApiConfig());

    const login = () => {
        if (username.length < 3 || password.length < 3) {
            changeErrorMessage("Veuillez entrer votre nom d'utilisateur et votre mot de passe.")
            toggleError(true);
            return;
        }
        toggleLoading(true);
        const loginRequest: LoginRequest = {username, password};
        userApi.login(loginRequest)
        .then(response => {
            window.location.reload()
        })
        .catch(error => {
            if (error?.response?.data?.code === ErrorCode.INCORRECT_LOGIN)
                changeErrorMessage("Nom d'utilisateur et/ou mot de passe erronés.")
            else
                changeErrorMessage("Une erreur inconnue s'est produite. Veuillez réessayer.")
            toggleError(true);
        })
        .finally(() => toggleLoading(false));
    }

    return (
        <div className="login-container">
            <div className="middle">
                <div className="login-content">
                    <Row className="login-row">
                        <Col md={12} sm={0}>
                            <div className="login-bg" />
                        </Col>
                        <Col md={12} sm={24}>
                            <div className="login-form-container">
                                <img src="/img/logo.png" alt="logo" className="login-logo" />
                                <span className="separator"></span>
                                <Form onFinish={login} className="login-form">
                                    <Form.Item>
                                        <Input
                                            prefix={<UserOutlined style={{ color: 'rgba(0,0,0,.25)' }} />}
                                            placeholder="Nom d'utilisateur..."
                                            onChange={val => changeUsername(val.target.value)} 
                                            />
                                        </Form.Item>
                                    <Form.Item>
                                        <Input
                                            prefix={<LockOutlined style={{ color: 'rgba(0,0,0,.25)' }} />}
                                            placeholder="Mot de passe..."
                                            type="password"
                                            onChange={val => changePassword(val.target.value)}
                                        />
                                    </Form.Item>
                                        {hasError ? (
                                            <Fragment>
                                                <Alert
                                                message={errorMessage}
                                                type="error"
                                                closable
                                                afterClose={() => toggleError(false)}
                                            />
                                            <br />
                                            </Fragment>
                                        ) : null}
                                        <Button type="primary" style={{ paddingLeft: 50, paddingRight: 50 }} htmlType="submit" loading={btnLoading}>
                                            Connectez-vous
                                            </Button>
                                </Form>
                            </div>
                        </Col>
                    </Row>
                </div>
            </div>
        </div>
    )
}

export default Login;
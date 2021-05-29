import { HeartTwoTone, LogoutOutlined, UsergroupAddOutlined, UserOutlined } from "@ant-design/icons";
import { Layout, Menu, message, Modal } from "antd";
import { Content, Footer } from "antd/lib/layout/layout";
import { BrowserRouter, Link } from 'react-router-dom';
import React, { useCallback, useContext, useEffect, useState } from "react";
import ApiConfig from "../configuration/apiConfig";
import Login from "../page/login";
import { UserDto, UserRestServiceApi } from "../rest";
import Context from "./context";
import Loading from "./loading";
import Routes from "./routes";

const Main = () => {
    const [user, setLoggedinUser] = useState<UserDto>();
    const [globalLoading, toggleGlobalLoading] = useState(true);

    const getUser = useCallback(() => {
        if (!user) {
            let userApi = new UserRestServiceApi(ApiConfig())
            return userApi.me()
            .then(response => setLoggedinUser(response.data))
            .catch(error => {})
            .finally(() => toggleGlobalLoading(false))
        }
    }, [setLoggedinUser, user])

    useEffect(() => { getUser() }, [getUser])
    
    if (globalLoading)
        return <Loading />
    if (user)
        return (
            <Context.Provider value={{user, toggleGlobalLoading, setLoggedinUser}}>
                <MainContainer user={user} />
            </Context.Provider>
        )
    return <Login />
}

const MainContainer = ({user}: MainContainerProps) => {
    
    const {toggleGlobalLoading} = useContext(Context)

    const logout = () => {
        toggleGlobalLoading(true)
        let userApi = new UserRestServiceApi(ApiConfig())
        userApi.logout()
        .then(() => window.location.reload())
        .catch(() =>{
            toggleGlobalLoading(false)
            message.error("Une erreur inconnue s'est produite. Veuillez réessayer.")
        })
    }

    const showLogoutConfirm = () => {
        Modal.confirm({
          title: 'Êtes-vous sûr de vouloir vous déconnecter ?',
          okText: 'Oui',
          cancelText: 'Non',
          onOk() {
            logout()
          }
        })
      }

    let selectedElement = "1";
    const location = window.location.pathname;
    if(location.indexOf("profil") > -1)
      selectedElement = "profil";
    else if(location.indexOf("users") > -1)
      selectedElement = "users";
    else if(location.indexOf("404") > -1)
      selectedElement = "-1";

    return (
        <BrowserRouter>
            <Layout>
                <Menu
                theme="light"
                mode="horizontal"
                style={{ lineHeight: '64px' }}
                defaultSelectedKeys={[selectedElement]}
                >
                    <Menu.Item key="logo" className="logo-container">
                        <img src="/img/logo.png" alt="logo" className="header-logo" />
                    </Menu.Item>
                    {
                        user.isAdmin && (
                            <Menu.Item key="users">
                                <Link to="/users">
                                    <UsergroupAddOutlined className="menu-icon" />
                                    Utilisateurs
                                </Link>
                            </Menu.Item>
                        )
                    }
                    <Menu.Item key="profil">
                        <Link to="/profil">
                            <UserOutlined className="menu-icon" />
                            Mon Profil
                        </Link>
                    </Menu.Item>
                    <Menu.Item key="logout" onClick={showLogoutConfirm}>
                        <LogoutOutlined className="menu-icon" />
                        Déconnecter
                    </Menu.Item>
                </Menu>
            <Content
                style={{
                margin: '24px 16px',
                padding: 24,
                minHeight: '73vh',
                }}
            >
                {<Routes />}
            </Content>
            <Footer style={{ textAlign: 'center' }}>Communauté © 2021 - Créé avec <HeartTwoTone style={{fontSize: 14}} twoToneColor="#e74c3c" /></Footer>
            </Layout>
        </BrowserRouter>
    )
}

type MainContainerProps = {user: UserDto}

export default Main;
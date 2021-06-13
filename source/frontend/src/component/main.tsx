import { AlertOutlined, ApartmentOutlined, BankOutlined, CommentOutlined, ContactsOutlined, ControlOutlined, HeartTwoTone, InfoCircleOutlined, LogoutOutlined, UsergroupAddOutlined, UserOutlined } from "@ant-design/icons";
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
import { canSeeComplaints, canSeeDistricts, canSeeMunicipalities, userHomePage } from "../helper/roles";

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
    else if(location.indexOf("utilisateurs") > -1)
      selectedElement = "utilisateurs";
    else if(location.indexOf("arrondissements") > -1)
      selectedElement = "arrondissements";
    else if(location.indexOf("municipalites") > -1)
      selectedElement = "municipalites";
    else if(location.indexOf("plaintes") > -1)
      selectedElement = "plaintes";
    else if(location.indexOf("404") > -1)
      selectedElement = "-1";
    else if(location.indexOf("info") > -1)
      selectedElement = "information";
    else if(location.indexOf("nouveautes") > -1)
      selectedElement = "nouveautes";

    else
        selectedElement = userHomePage(user);

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
                        canSeeComplaints(user) && (
                            <Menu.Item key="plaintes" icon={<AlertOutlined />}>
                                <Link to="/plaintes">
                                    Plaintes
                                </Link>
                            </Menu.Item>
                        )
                    }
                    {
                        user.isAdmin && (
                            <Menu.Item key="nouveautes" icon={<CommentOutlined />}>
                                <Link to="/nouveautes">
                                    Nouveautés
                                </Link>
                            </Menu.Item>
                        )
                    }
                    {
                        user.isAdmin && (
                            <Menu.Item key="information" icon={<InfoCircleOutlined />}>
                                <Link to="/info">
                                    Informations
                                </Link>
                            </Menu.Item>
                        )
                    }
                    {  
                        (canSeeDistricts(user) || canSeeMunicipalities(user)) && (
                            <Menu.SubMenu key="masterdata" icon={<ControlOutlined />} title="Données de Base">
                                {
                                    canSeeMunicipalities(user) && (
                                        <Menu.Item key="municipalites" icon={<BankOutlined />}>
                                            <Link to="/municipalites">
                                                Municipalités
                                            </Link>
                                        </Menu.Item>
                                    )
                                }
                                {
                                    canSeeDistricts(user) && (
                                        <Menu.Item key="arrondissements" icon={<ApartmentOutlined />}>
                                            <Link to="/arrondissements">
                                                Arrondissements
                                            </Link>
                                        </Menu.Item>
                                    )
                                }
                                {
                                    user.isAdmin && (
                                        <Menu.Item key="utilisateurs" icon={<UsergroupAddOutlined />}>
                                            <Link to="/utilisateurs">
                                                Utilisateurs
                                            </Link>
                                        </Menu.Item>
                                    )
                                }
                            </Menu.SubMenu>
                        )
                    }
                    <Menu.SubMenu key="me" icon={<ContactsOutlined />} title="Mon Compte">
                        <Menu.Item key="profil" icon={<UserOutlined />}>
                            <Link to="/profil">
                                Mon Profil
                            </Link>
                        </Menu.Item>
                        <Menu.Item key="logout" onClick={showLogoutConfirm} icon={<LogoutOutlined />}>
                            Déconnecter
                        </Menu.Item>
                    </Menu.SubMenu>
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
import React, { useContext } from "react";
import { Switch } from "react-router";
import { Redirect, Route } from "react-router-dom";
import { userHomePage } from "../helper/roles";
import Complaints from "../page/complaints";
import Districts from "../page/districts";
import Information from "../page/information";
import Municipalities from "../page/municipalities";
import NotFound from "../page/notFound";
import Profile from "../page/profile";
import Users from "../page/users";
import { UserDto } from "../rest";
import Context from "./context";

const Routes = () => (
    <Switch>
        <Route path="/" exact component={HomeRedirect} />
        <Route path="/plaintes" exact component={Complaints} />
        <Route path="/arrondissements" exact component={Districts} />
        <Route path="/municipalites" exact component={Municipalities} />
        <Route path="/utilisateurs" exact component={Users} />
        <Route path="/profil" exact component={Profile} />
        <Route path="/info" exact component={Information} />
        <Route path="/404" exact component={NotFound} />
        <Redirect to="/404" />
    </Switch>
)

const HomeRedirect = () => {
    const { user } = useContext(Context)
    return <Redirect to={getRedirectToPage(user)} />
}

const getRedirectToPage = (user?: UserDto): string => "/" + userHomePage(user)

export default Routes;
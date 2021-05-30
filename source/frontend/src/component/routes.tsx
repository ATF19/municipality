import React from "react";
import { Switch } from "react-router";
import { Redirect, Route } from "react-router-dom";
import Districts from "../page/districts";
import Municipalities from "../page/municipalities";
import NotFound from "../page/notFound";
import Profile from "../page/profile";
import Users from "../page/users";

const Routes = () => (
    <Switch>
        <Route path="/arrondissements" exact component={Districts} />
        <Route path="/municipalites" exact component={Municipalities} />
        <Route path="/utilisateurs" exact component={Users} />
        <Route path="/profil" exact component={Profile} />
        <Route path="/404" exact component={NotFound} />
        <Redirect to="/404" />
    </Switch>
)

export default Routes;
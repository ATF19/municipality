import React from "react";
import { Switch } from "react-router";
import { Redirect, Route } from "react-router-dom";
import NotFound from "../page/notFound";
import Profile from "../page/profile";

const Routes = () => (
    <Switch>
        <Route path="/profil" exact component={Profile} />
        <Route path="/404" exact component={NotFound} />
        <Redirect to="/404" />
    </Switch>
)

export default Routes;
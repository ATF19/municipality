import React, { useContext } from "react";
import { Redirect } from "react-router-dom";
import Context from "../component/context";
import { canSeeComplaints } from "../helper/roles";

const Complaints = () => {
    const { user } = useContext(Context)
    if (!user || !canSeeComplaints(user))
        return <Redirect to="/404" />

    return <h1>Plaintes</h1>
}

export default Complaints;
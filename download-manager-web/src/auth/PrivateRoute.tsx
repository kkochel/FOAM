import React, {useContext} from "react";
import {AuthContext} from "./AuthProvider.tsx";
import {Navigate} from "react-router-dom";

export const PrivateRoute = ({children}: { children: React.JSX.Element }) => {
    const {authenticated} = useContext(AuthContext)
    return authenticated ? children : <Navigate to={"/sign-in"} replace/>
}
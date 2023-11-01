import React, {useContext} from "react";
import {AuthContext} from "./AuthProvider.tsx";
import {Navigate} from "react-router-dom";
import {isTokenExpired} from "../main.tsx";

export const PrivateRoute = ({children}: { children: React.JSX.Element }) => {
    const {token} = useContext(AuthContext)
    return isTokenExpired(token) ? children : <Navigate to={"/sign-in"} replace/>
}
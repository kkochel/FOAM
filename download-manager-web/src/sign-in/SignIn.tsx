import {SignInForm} from "./SignInForm.tsx";
import {Header} from "../header/Header.tsx";
import {Footer} from "../common/Footer.tsx";
import {useContext, useEffect} from "react";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {useNavigate} from "react-router-dom";

export const SignIn = () => {
    const {authenticated} = useContext(AuthContext)
    const navigate = useNavigate();

    useEffect(() => {
        if (authenticated) {
            navigate("/dashboard")
        }
    }, [authenticated]);

    return (
        <>
            <Header/>
            <SignInForm/>
            <Footer/>
        </>
    )
}
import {SignInForm} from "./SignInForm.tsx";
import {Header} from "../header/Header.tsx";
import {Footer} from "../common/Footer.tsx";

export const SignIn = () => {
    return (
        <>
            <Header/>
            <SignInForm/>
            <Footer/>
        </>
    )
}
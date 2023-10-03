import {Header} from "../header/Header.tsx";
import {SignInForm} from "./SignInForm.tsx";

export const SignIn = () => {
    return (
        <>
            <Header isAuthenticated={false}/>
            <SignInForm/>
        </>
    )
}
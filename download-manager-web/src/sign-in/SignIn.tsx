import {Header} from "../header/Header.tsx";
import {SignInForm} from "./SignInForm.tsx";
import {Container} from "react-bootstrap";

export const SignIn = () => {
    return (
        <Container fluid className={"h-100"}>
            <Header isAuthenticated={false}/>
            <SignInForm/>
        </Container>
    )
}
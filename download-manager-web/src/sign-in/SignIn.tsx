import {SignInForm} from "./SignInForm.tsx";
import {Container} from "react-bootstrap";
import {Header} from "../header/Header.tsx";

export const SignIn = () => {
    return (
        <Container fluid className={"h-100"}>
            <Header/>
            <SignInForm/>
        </Container>
    )
}
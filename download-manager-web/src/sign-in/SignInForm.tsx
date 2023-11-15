import {useContext,  useState} from "react";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
// import {removeRefreshTokenIfExpired} from "../main.tsx";

function invalidSignIn(responseStatus: number | undefined) {
    return <>
        {responseStatus && responseStatus !== 200 ?
            <Form.Text className={"text-danger fw-bolder"}>
                Invalid credentials or user does not exist
            </Form.Text> : null}
    </>;
}

export const SignInForm = () => {
    const [username, setUsername] = useState<string>()
    const [password, setPassword] = useState<string>()
    const navigate = useNavigate();
    const {setAuthenticated, handleSignIn} = useContext(AuthContext)
    const disableSignInButton = (username === undefined || username.length === 0) || (password === undefined || password.length === 0)
    const [responseStatus, setResponseStatus] = useState<number>()

    // useEffect(() => {
    //     removeRefreshTokenIfExpired()
    // }, []);

    const handleSubmit = () => {
        if (username && password) {
            handleSignIn(setAuthenticated, navigate, {username: username, password: password})
                .then(value => setResponseStatus(value))
                .catch(reason => setResponseStatus(reason))
        }
    }

    return (
        <Container>
            <Row>
                <Col xs={4}/>
                <Col className={"border-with-shadow"}>
                    <Form onSubmit={e => e.preventDefault()}>
                        <Form.Group>
                            <Form.Label className={"fw-bold align-content-start"}>Username</Form.Label>
                            <Form.Control id="username-input"
                                          type="text"
                                          value={username || ''}
                                          onChange={event => setUsername(event.target.value)}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label className={"fw-bold"}>Password</Form.Label>
                            <Form.Control id="password-input"
                                          type="password"
                                          value={password || ''}
                                          onChange={event => setPassword(event.target.value)}/>
                        </Form.Group>
                        {invalidSignIn(responseStatus)}
                    </Form>
                    <Button variant={"outline-primary"} disabled={disableSignInButton} className={"m-2"}
                            onClick={handleSubmit}>Sign in</Button>
                </Col>
                <Col xs={4}/>
            </Row>
        </Container>
    )
}
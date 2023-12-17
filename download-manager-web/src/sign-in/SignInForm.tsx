import {useContext,  useState} from "react";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {WcagContext} from "../common/WcagContextFoo.tsx";
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
    const {fontSize} = useContext(WcagContext)

    const handleSubmit = () => {
        if (username && password) {
            handleSignIn(setAuthenticated, navigate, {username: username, password: password})
                .then(value => setResponseStatus(value))
                .catch(reason => setResponseStatus(reason))
        }
    }

    return (
        <Container>
            <Row >
                <Col xs={12} sm={2} md={2} lg={4} xl={4} xxl={4}/>
                <Col xs={12} sm={8} md={8} lg={4} xl={4} xxl={4} className={"border-with-shadow"}>
                    <Form>
                        <Form.Group>
                            <Form.Label className={`fw-bold ${fontSize}`}>Username</Form.Label>
                            <Form.Control id="username-input"
                                          type="text"
                                          className={`form-control-${fontSize}`}
                                          placeholder={"login"}
                                          value={username || ''}
                                          onChange={event => setUsername(event.target.value)}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label className={`fw-bold ${fontSize}`}>Password</Form.Label>
                            <Form.Control id="password-input"
                                          type="password"
                                          className={`form-control-${fontSize}`}
                                          placeholder={"password"}
                                          value={password || ''}
                                          onChange={event => setPassword(event.target.value)}/>
                        </Form.Group>
                        {invalidSignIn(responseStatus)}
                    </Form>
                    <Button variant={"outline-primary"}
                            disabled={disableSignInButton}
                            className={`m-2 btn-${fontSize}`}
                            onClick={handleSubmit}>Sign in</Button>
                </Col>
                <Col xs={4}/>
            </Row>
        </Container>
    )
}
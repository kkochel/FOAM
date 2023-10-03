import {useState} from "react";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

export const SignInForm = () => {
    const [username, setUsername] = useState<string>()
    const [password, setPassword] = useState<string>()
    const navigate = useNavigate();

    function handleSignIn() {
        navigate("/dashboard")
    }

    return (
        <Container>
            <Row>
                <Col xs={4}/>
                <Col className={"border border-2 border-light rounded shadow"}>
                    <Form onSubmit={e => e.preventDefault()}>
                        <Form.Group>
                            <Form.Label className={"fw-bold align-content-start"}>Username</Form.Label>
                            <Form.Control type="text"
                                          value={username}
                                          onChange={event => setUsername(event.target.value)}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label className={"fw-bold"}>Password</Form.Label>
                            <Form.Control type="password"
                                          value={password}
                                          onChange={event => setPassword(event.target.value)}/>
                        </Form.Group>
                    </Form>
                    <Button className={"m-2"} onClick={handleSignIn}>Sign in</Button>
                </Col>
                <Col xs={4}/>
            </Row>
        </Container>
    )
}
import {Header} from "../header/Header.tsx";
import {Col, Container, Form, Nav, Row} from "react-bootstrap";
import {Link, Outlet} from "react-router-dom";

export const Dashboard = () => {
    return (
        <Container fluid className={"h-100"}>
            <Header isAuthenticated={true}/>
            <Row className={"h-100"}>
                <Col xs={3}>
                    <aside className={"h-100"}>
                        <h4>List of datasets</h4>
                        <Form style={{"paddingLeft": "2rem", "paddingRight": "2rem"}}>
                            <Form.Group style={{"textAlign": "left"}}>
                                <Form.Label htmlFor={"dataset-search"}>Search dataset</Form.Label>
                                <Form.Control id={"dataset-search"} type={"text"} placeholder={"Filter by dataset id"}/>
                            </Form.Group>
                        </Form>
                        <Nav className={"flex-column"}>
                            <Nav.Item>
                                <Nav.Link as={Link} to={`datasets/EGAD010000051`}>EGAD010000051</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link as={Link} to={`datasets/EGAD020000051`}>EGAD020000051</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </aside>
                </Col>
                <Col><Outlet/></Col>
            </Row>

        </Container>
    )
}
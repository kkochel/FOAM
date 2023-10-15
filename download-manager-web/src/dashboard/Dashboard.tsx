import {Header} from "../header/Header.tsx";
import {Col, Container, Form, Nav, Row} from "react-bootstrap";
import {Link, Outlet} from "react-router-dom";
import {useEffect, useState} from "react";
import {fetchData} from "../common/consts.ts";

export interface PermittedDatasetsResponse {
    stableIds: string[]
}

export interface ExportRequest {
    stableId: string
}

export const Dashboard = () => {
    const [dataset, setDataset] = useState<PermittedDatasetsResponse>()

    useEffect(() => {
        fetchData<PermittedDatasetsResponse>("http://localhost:8080/api/datasets")
            .then(response => setDataset(response))
    }, [])

    return (
        <Container fluid className={"h-100"}>
            <Header/>
            <Row className={"h-100"}>
                <Col xs={3}>
                    <aside className={"h-100 border-with-shadow"}>
                        <h4 className={"mt-2"}>List of datasets</h4>
                        <Form style={{"paddingLeft": "2rem", "paddingRight": "2rem"}}>
                            <Form.Group style={{"textAlign": "left"}}>
                                <Form.Label htmlFor={"dataset-search"}>Search dataset</Form.Label>
                                <Form.Control className={"border-black border-2"} id={"dataset-search"} type={"text"}
                                              placeholder={"Filter by dataset id"}/>
                            </Form.Group>
                        </Form>
                        <Nav className={"flex-column"}>
                            {dataset ?
                                dataset.stableIds.map((value, index) => {
                                    return (
                                        <Nav.Item key={index}>
                                            <Nav.Link as={Link} to={`datasets/${value}`}>{value}</Nav.Link>
                                        </Nav.Item>
                                    )
                                })
                                : null
                            }
                        </Nav>
                    </aside>
                </Col>
                <Col><Outlet/></Col>
            </Row>

        </Container>
    )
}
import {Col, ListGroup, Row} from "react-bootstrap";

export const HomeContent = () => {
    return (
        <Row>
            <Col xs={3}/>
            <Col xs={6}>
                <h3>FAQ</h3>
                <ListGroup>
                    <ListGroup.Item as="li" className="d-flex justify-content-between align-items-start">
                        <div className="ms-2 me-auto">
                            <div className="fw-bold">I can't log into the application</div>
                            The application has information only about users who have been given permissions to datasets managed by FEGA Poland
                        </div>
                    </ListGroup.Item>
                    <ListGroup.Item as="li" className="d-flex justify-content-between align-items-start">
                    <div className="ms-2 me-auto">
                        <div className="fw-bold">Public C4GH key</div>
                        -----BEGIN CRYPT4GH PUBLIC KEY-----
                        wdf5u+B8KlLtVo5X/3gVn3Y5+O2X9hAzogAk3TgFTCs=
                        -----END CRYPT4GH PUBLIC KEY-----
                    </div>
                </ListGroup.Item>

                </ListGroup>
            </Col>
            <Col xs={3}/>
        </Row>
    )
}
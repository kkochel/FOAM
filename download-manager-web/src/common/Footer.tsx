import footer from "../assets/horizontal_footer_pl.png"
import {Col, Container, Nav, Row} from "react-bootstrap";
import {FC} from "react";
import {Link} from "react-router-dom";


export const Footer: FC = () => {
    const alt = "„Logo projektowe Funduszy Europejskich, logo Rzeczpospolitej Polski, logo Uniwersytet Łódzki, logo Unii Europejskiej"
    return (
        <Container fluid={"sm"} className={"text-center text-md-start"}>
            <Row>
                <Col xs={12} sm={2} md={2} lg={2} xl={3} xxl={3}></Col>
                <Col xs={12} sm={10} md={10} lg={10} xl={9} xxl={9}>
                    <footer>
                        <img style={{"maxWidth": "100%", "height": "auto"}} src={footer} alt={alt}/>
                    </footer>
                </Col>
            </Row>
            <Row>
                <Nav className={"flex-column"}>
                    <Nav.Item key={"declaration"}>
                        <Nav.Link as={Link} to={`/declaration`}>Declaration of availability</Nav.Link>
                    </Nav.Item>
                </Nav>
            </Row>

        </Container>
    )
}
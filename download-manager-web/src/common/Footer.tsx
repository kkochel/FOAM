import footer from "../assets/horizontal_footer_pl.png"
import {Col, Container, Row} from "react-bootstrap";
import {FC} from "react";


export const Footer: FC = () => {
    const alt = "„Logo projektowe Funduszy Europejskich, logo Rzeczpospolitej Polski, logo Uniwersytet Łódzki, logo Unii Europejskiej"
    return (
        <Container fluid={"sm"} className={"text-center text-md-start"}>
            <Row>
                <Col xs={12} lg={2} xl={2} xxl={3}/>
                <Col xs={12} sm={5} md={11} lg={2} xl={2} xxl={5}>
                    <footer>
                        <img src={footer} alt={alt}/>
                    </footer>
                </Col>
            </Row>

        </Container>
    )
}
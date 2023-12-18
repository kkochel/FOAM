import {Button, Col, Navbar, Row} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import genomicMapOfPoland from '../assets/logo_genomic_map_poland.png'
import {FC, useContext, useEffect, useState} from "react";
import {Link, useLocation} from "react-router-dom";
import {WcagContext} from "../common/WcagContextProvider.tsx";

export const Header: FC = () => {
    const {setFontSize} = useContext(WcagContext)
    const {fontSize} = useContext(WcagContext)
    const location: string = useLocation().pathname
    const [hideSignIn, setHideSignIn] = useState<boolean>(false)

    useEffect(() => {
        if ("/sign-in" !== location) {
            setHideSignIn(true)
        }

    }, [location]);

    return (
        <>
            <div id={"header-home"}>
                <Navbar>
                    <Row className={"w-100"}>
                        <Col xs={12} sm={4} md={4} lg={3} xl={2} xxl={2}>
                            <Navbar.Brand href="https://ega-archive.org/about/projects-and-funders/federated-ega/">
                                <img src={fegaLogo}
                                     width={"190px"}
                                     height={"75px"}
                                     className="d-inline-block align-top" alt="FEGA logo"/>
                            </Navbar.Brand>
                        </Col>
                        <Col xs={12} sm={4} md={4} lg={3} xl={2} xxl={2}>
                            <img src={genomicMapOfPoland}
                                 width={"190px"}
                                 height={"75px"}
                                 className="d-inline-block align-top" alt="Genomic Map of Poland logo"/>
                        </Col>
                        <Col xs={12} sm={2} md={2} lg={3} xl={2} xxl={4}>
                            <h1 className={`h1-${fontSize}`}>Federated EGA Polish Node</h1>
                            <Button variant={"outline-primary"}
                                    onClick={() => setFontSize("normal-style")}
                                    className={`btn-${fontSize} me-2`}
                                    active={fontSize === "normal-style"}
                                    aria-label={"Normal text"}>Default text size</Button>
                            <Button variant={"outline-primary"}
                                    onClick={() => setFontSize("big-style")}
                                    className={`btn-${fontSize}`}
                                    active={fontSize === "big-style"}
                                    aria-label={"Enlarge text"}>Enlarge text size</Button>
                        </Col>
                        {hideSignIn &&
                        <Col xs={12} sm={2} md={2} lg={3} xl={6} xxl={4}>
                            <Navbar.Collapse className="justify-content-end">
                                <Link to={"/sign-in"}>
                                    <Button variant={"outline-primary"}
                                            className={`btn-${fontSize}`}>Sign in</Button></Link>
                            </Navbar.Collapse>
                        </Col>}
                    </Row>
                </Navbar>
            </div>
        </>
    )
}
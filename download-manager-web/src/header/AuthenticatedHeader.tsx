import {Alert, Button, Col, Navbar, Row} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {Dispatch, FC, SetStateAction, useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {fetchData} from "../common/consts.ts";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import genomicMapOfPoland from "../assets/logo_genomic_map_poland.png";
import {SkipLink} from "./SkipLink.tsx";
import {WcagContext} from "../common/WcagContextProvider.tsx";

interface HeaderResponse {
    fullName: string
    c4ghKeyPresent: boolean
}

interface Props {
    setC4ghKeyPresent: Dispatch<SetStateAction<boolean>>
}

export const AuthenticatedHeader: FC<Props> = (props) => {
    const {handleSignOut, setAuthenticated} = useContext(AuthContext)
    const {setC4ghKeyPresent} = props
    const [userState, setUserState] = useState<HeaderResponse>()
    const href = "/api/cega-users/info";
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const {setFontSize} = useContext(WcagContext)
    const {fontSize} = useContext(WcagContext)

    const {data} = useQuery({
        queryKey: ["header-query"],
        queryFn: () => fetchData<HeaderResponse>(href)
    })

    useEffect(() => {
        if (data) {
            setUserState(data)
            setC4ghKeyPresent(data.c4ghKeyPresent)
        }
    }, [data, setC4ghKeyPresent]);

    const handleLogout = () => {
        handleSignOut(setAuthenticated, navigate)
        queryClient.removeQueries()
    }

    return (
        <>
            <SkipLink/>
            {userState ?
                <div id={"header"}>
                    <Navbar>
                        <Row className={"w-100"}>
                            <Col xs={12} sm={4} md={4} lg={3} xl={2} xxl={2}>
                                <Navbar.Brand href="https://ega-archive.org/federated">
                                    <img src={fegaLogo}
                                         width={"190px"}
                                         height={"75px"}
                                         className="d-inline-block align-top" alt="FEGA logo"/>
                                </Navbar.Brand>
                            </Col>
                            <Col xs={12} sm={4} md={4} lg={2} xl={2} xxl={2}>
                                <img src={genomicMapOfPoland}
                                     width={"190px"}
                                     height={"75px"}
                                     className="d-inline-block align-top" alt="Genomic Map of Poland logo"/>
                            </Col>
                            <Col xs={12} sm={4} md={4} lg={4} xl={4} xxl={4} className={"wrap"}>
                                <h1 className={`h1-${fontSize}`}>Federated EGA Polish Node</h1>
                                <Button variant={"outline-primary"}
                                        onClick={() => setFontSize("normal-style")}
                                        active={fontSize === "normal-style"}
                                        className={`btn-${fontSize}  me-2`}
                                        aria-label={"Normal text"}>Default text size</Button>
                                <Button variant={"outline-primary"}
                                        onClick={() => setFontSize("big-style")}
                                        className={`btn-${fontSize}`}
                                        active={fontSize === "big-style"}
                                        aria-label={"Enlarge text"}>Enlarge text size</Button>
                            </Col>
                            <Col xs={12} sm={12} md={12} lg={3} xl={4} xxl={4}>
                                <Navbar.Collapse className="justify-content-end">
                                    <Navbar.Text className={`fw-bold ${fontSize}`}>
                                        Signed in as: {userState.fullName}
                                    </Navbar.Text>
                                    <Button id={"sign-out"} variant={"outline-primary"}
                                            className={`ms-4 btn-${fontSize}`}
                                            onClick={handleLogout}>Sign out</Button>
                                </Navbar.Collapse>
                            </Col>
                        </Row>
                    </Navbar>

                    {!userState.c4ghKeyPresent ?
                        <Alert variant={"danger"}> Please upload the C4GH public key in ega-archive.org Without the key,
                            exporting files is impossible.</Alert>
                        : null}
                </div> : null}
        </>
    )

}
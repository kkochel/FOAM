import {Button, Nav} from "react-bootstrap";
import {useLocation} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {WcagContext} from "../common/WcagContextProvider.tsx";

export const SkipLink = () => {
    const [searchFiles, setSearchFiles] = useState<boolean>(false)
    const location: string = useLocation().pathname
    const {fontSize} = useContext(WcagContext)

    useEffect(() => {
        if ("/dashboard" !== location) {
            setSearchFiles(true)
        }
        console.log("location: ", location)
    }, [location]);

    return (
    <Nav className={"fixed-top"}>
            <Nav.Item >
                <Nav.Link as={Button} className={`visually-hidden-focusable bg-white btn-${fontSize}`} href="#dataset-search">Skip to search dataset</Nav.Link>
                <Nav.Link as={Button} className={`visually-hidden-focusable bg-white btn-${fontSize}`} href="#sign-out">Skip to sign out</Nav.Link>
                {searchFiles && <Nav.Link as={Button} className={`visually-hidden-focusable bg-white btn-${fontSize}`} href="#file-search">Skip to search files</Nav.Link>}
            </Nav.Item>
        </Nav>
    )
}
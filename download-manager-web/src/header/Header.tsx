import {Button, Navbar} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {FC} from "react";
import {Link} from "react-router-dom";

export const Header: FC = () => {

    return (
        <div id={"header"}>
            <Navbar>
                <Navbar.Brand href="https://ega-archive.org/federated">
                    <img src={fegaLogo}
                         width={"300px"}
                         height={"100px"}
                         className="d-inline-block align-top" alt="FEGA logo"/>
                </Navbar.Brand>

                <Navbar.Collapse className="justify-content-end">
                    <Link to={"/sing-in"}>
                        <Button variant={"outline-primary"}>Sign in</Button></Link>
                </Navbar.Collapse>
            </Navbar>
        </div>
    )

}
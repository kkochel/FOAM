import {Button, Navbar} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {FC} from "react";
import {Link} from "react-router-dom";

interface Props {
    isAuthenticated: boolean
}

export const Header: FC<Props> = (props) => {
    const {isAuthenticated} = props

    return (
        <>
            <Navbar >
                <Navbar.Brand href="https://ega-archive.org/federated">
                    <img src={fegaLogo}
                         width={"300px"}
                         height={"100px"}
                         className="d-inline-block align-top" alt="FEGA logo"/>
                </Navbar.Brand>


                {isAuthenticated ?
                    <Navbar.Collapse className="justify-content-end">
                        <Navbar.Text>
                            Signed in as: Mark Otto
                        </Navbar.Text>
                    </Navbar.Collapse>
                    :
                    <Navbar.Collapse className="justify-content-end">
                        <Link to={"/sing-in"}>
                            <Button>Sign in </Button></Link>
                    </Navbar.Collapse>
                }
            </Navbar>
        </>
    )

}
import {Button, Container, Navbar} from "react-bootstrap";
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
            <Navbar style={{"background": "aliceblue"}} className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand href="https://ega-archive.org/federated" style={{"background": "beige"}}>
                        <img src={fegaLogo}
                             width={"300px"}
                             height={"100px"}
                             className="d-inline-block align-top" alt="FEGA logo"/>
                    </Navbar.Brand>


                    {isAuthenticated ?
                        <Navbar.Collapse className="justify-content-end" style={{"background": "cadetblue"}}>
                            <Navbar.Text>
                                Signed in as: Mark Otto
                            </Navbar.Text>
                        </Navbar.Collapse>
                        :
                        <Link to={"/sing-in"}>
                        <Button>Sign in </Button></Link>
                    }
                </Container>
            </Navbar>
        </>
    )

}
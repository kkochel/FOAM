import {Button, Navbar} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {FC, useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {fetchData} from "../common/consts.ts";
import {AppSettings} from "../api/AppSettings.ts";

interface HeaderResponse {
    fullName: string
}


export const Header: FC = () => {
    const {token, handleSignOut, setToken} = useContext(AuthContext)
    const [fullName, setFullName] = useState<string>()
    const isAuthenticated = token !== null
    const navigate = useNavigate()

    useEffect(() => {
        if (isAuthenticated) {
            fetchData<HeaderResponse>(AppSettings.DOMAIN + "/api/cega-users/full-name")
                .then(response => setFullName(response.fullName))

        }
    }, [isAuthenticated]);

    return (
        <div id={"header"}>
            <Navbar>
                <Navbar.Brand href="https://ega-archive.org/federated">
                    <img src={fegaLogo}
                         width={"300px"}
                         height={"100px"}
                         className="d-inline-block align-top" alt="FEGA logo"/>
                </Navbar.Brand>


                {isAuthenticated ?
                    <Navbar.Collapse className="justify-content-end">
                        <Navbar.Text>
                            Signed in as: {fullName}
                        </Navbar.Text>
                        <Button className={"ms-4"} onClick={() => handleSignOut(setToken, navigate)}>Sign out</Button>
                    </Navbar.Collapse>

                    :
                    <Navbar.Collapse className="justify-content-end">
                        <Link to={"/sing-in"}>
                            <Button>Sign in </Button></Link>
                    </Navbar.Collapse>
                }
            </Navbar>
        </div>
    )

}
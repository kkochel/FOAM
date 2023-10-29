import {Button, Navbar} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {FC, useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {fetchData} from "../common/consts.ts";
import {useQuery} from "@tanstack/react-query";

interface HeaderResponse {
    fullName: string
}

export const AuthenticatedHeader: FC = () => {
    const {handleSignOut, setToken} = useContext(AuthContext)
    const [fullName, setFullName] = useState<string>()

    const href = "/api/cega-users/full-name";
    const navigate = useNavigate()


    const {data} = useQuery({
        queryKey: ["header-query"],
        queryFn: () => fetchData<HeaderResponse>(href)
    })

    useEffect(() => {
        if (data) {
            setFullName(data.fullName)
        }
    }, [data]);

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
                    <Navbar.Text className={"fw-bold"}>
                        Signed in as: {fullName}
                    </Navbar.Text>
                    <Button variant={"outline-primary"} className={"ms-4"}
                            onClick={() => handleSignOut(setToken, navigate)}>Sign out</Button>
                </Navbar.Collapse>
            </Navbar>
        </div>
    )

}
import {Alert, Button, Navbar} from "react-bootstrap";
import fegaLogo from '../assets/FEGA-logo-generic.svg'
import {Dispatch, FC, SetStateAction, useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../auth/AuthProvider.tsx";
import {fetchData} from "../common/consts.ts";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import genomicMapOfPoland from "../assets/logo_genomic_map_poland.png";

interface HeaderResponse {
    fullName: string
    c4ghKeyPresent: boolean
}

interface Props {
    setC4ghKeyPresent:Dispatch<SetStateAction<boolean>>
}

export const AuthenticatedHeader: FC<Props> = (props) => {
    const {handleSignOut, setAuthenticated} = useContext(AuthContext)
    const {setC4ghKeyPresent} = props
    const [userState, setUserState] = useState<HeaderResponse>()
    const href = "/api/cega-users/info";
    const navigate = useNavigate()
    const queryClient = useQueryClient()

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
            {userState ?
                <div id={"header"}>
                    <Navbar>
                        <Navbar.Brand href="https://ega-archive.org/federated">
                            <img src={fegaLogo}
                                 width={"190px"}
                                 height={"75px"}
                                 className="d-inline-block align-top" alt="FEGA logo"/>
                        </Navbar.Brand>
                        <img src={genomicMapOfPoland}
                             width={"190px"}
                             height={"75px"}
                             className="d-inline-block align-top" alt="Genomic Map of Poland logo"/>
                        <Navbar.Collapse className="justify-content-end">
                            <Navbar.Text className={"fw-bold"}>
                                Signed in as: {userState.fullName}
                            </Navbar.Text>
                            <Button variant={"outline-primary"} className={"ms-4"}
                                    onClick={handleLogout}>Sign out</Button>
                        </Navbar.Collapse>
                    </Navbar>

                    {!userState.c4ghKeyPresent ?
                        <Alert variant={"danger"}> Please upload the C4GH public key in ega-archive.org Without the key,
                            exporting files is impossible.</Alert>
                        : null}
                </div> : null}
        </>
    )

}
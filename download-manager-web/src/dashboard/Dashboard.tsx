import {AuthenticatedHeader} from "../header/AuthenticatedHeader.tsx";
import {Col, Container, Form, Nav, Row} from "react-bootstrap";
import {Link, Outlet, useLocation} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {fetchData} from "../common/consts.ts";
import {useQuery} from "@tanstack/react-query";
import {Footer} from "../common/Footer.tsx";
import {WcagContext} from "../common/WcagContextProvider.tsx";

export interface PermittedDatasetsResponse {
    stableIds: string[]
}

export interface ExportRequest {
    stableId: string
}

export const Dashboard = () => {
    const [dataset, setDataset] = useState<PermittedDatasetsResponse>()
    const [filteredDatasets, setFilteredDatasets] = useState<PermittedDatasetsResponse>()
    const [filterValue, setFilterValue] = useState<string>()
    const [c4ghKeyPresent, setC4ghKeyPresent] = useState<boolean>(false)
    const location: string = useLocation().pathname
    const [displayEmpty, setDisplayEmty] = useState<boolean>(true)
    const {fontSize} = useContext(WcagContext)

    const href: string = "/api/datasets"

    const {data} = useQuery({
        queryKey: ["datasets"],
        queryFn: () => fetchData<PermittedDatasetsResponse>(href),
        enabled: c4ghKeyPresent
    })

    useEffect(() => {
        if (data) {
            setDataset(data)
        }
    }, [data]);


    useEffect(() => {
        if (dataset && filterValue) {
            const filtered = dataset.stableIds.filter(stableId => stableId.toLowerCase().includes(filterValue.toLowerCase()))
            setFilteredDatasets({stableIds: filtered})
        } else {
            setFilteredDatasets(dataset)
        }
    }, [filterValue, dataset]);

    useEffect(() => {
        if ("/dashboard" !== location) {
            setDisplayEmty(false)
        }
        console.log("location: ", location)
    }, [location]);

    return (
        <div>
            <div>
                <AuthenticatedHeader setC4ghKeyPresent={setC4ghKeyPresent}/>
                <Row id={"dashboard-row"} className={`h-100 ${fontSize}`}>
                    <Col id={"dashboard-aside-column"} xs={12} sm={12} md={12} lg={2} xl={2} xxl={2}>
                        <aside className={"h-100 border-with-shadow overflow-auto"}>
                            <h3 className={`mt-2 h3-${fontSize} wrap`}>List of datasets</h3>
                            <Form style={{"paddingLeft": "2rem", "paddingRight": "2rem"}}
                                  onSubmit={e => e.preventDefault()}>
                                <Form.Group style={{"textAlign": "left"}}>
                                    <Form.Label htmlFor={"dataset-search"}>Search dataset</Form.Label>
                                    <Form.Control id={"dataset-search"}
                                                  className={`border-black border-2 form-control-${fontSize}`}
                                                  type={"text"}
                                                  placeholder={"Filter by dataset id"}
                                                  onChange={event => setFilterValue(event.target.value)}
                                    />
                                </Form.Group>
                            </Form>
                            {c4ghKeyPresent &&
                                <Nav className={"flex-column wrap"}>
                                    {filteredDatasets && filteredDatasets.stableIds.length > 0 ?
                                        filteredDatasets.stableIds.map((value, index) => {
                                            return (
                                                <Nav.Item key={index}>
                                                    <Nav.Link as={Link} to={`datasets/${value}`}>{value}</Nav.Link>
                                                </Nav.Item>
                                            )
                                        })
                                        :
                                        <div>
                                            <p>No content. Please change the search phrase</p>
                                        </div>
                                    }
                                </Nav>
                            }
                        </aside>
                    </Col>
                    {displayEmpty ?
                        <Container>
                            <Row xs={12} sm={12} md={12} lg={10} xl={10} xxl={10} className="overflow-auto"
                                 style={{minHeight: '70vh'}}/>
                        </Container> : null
                    }
                    <Col><Outlet/></Col>
                </Row>
            </div>
            <Footer/>
        </div>
    )
}
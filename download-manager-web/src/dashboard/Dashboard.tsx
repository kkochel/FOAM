import {AuthenticatedHeader} from "../header/AuthenticatedHeader.tsx";
import {Col, Form, Nav, Row} from "react-bootstrap";
import {Link, Outlet} from "react-router-dom";
import {useEffect, useState} from "react";
import {fetchData} from "../common/consts.ts";
import {useQuery} from "@tanstack/react-query";

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

    return (
        <>
            <AuthenticatedHeader setC4ghKeyPresent={setC4ghKeyPresent}/>
            <Row id={"dashboard-row"} className={"h-100"}>
                <Col id={"dashboard-aside-column"} xs={2}>
                    <aside className={"h-100 border-with-shadow overflow-auto"}>
                        <h4 className={"mt-2"}>List of datasets</h4>
                        <Form style={{"paddingLeft": "2rem", "paddingRight": "2rem"}}>
                            <Form.Group style={{"textAlign": "left"}}>
                                <Form.Label htmlFor={"dataset-search"}>Search dataset</Form.Label>
                                <Form.Control id={"dataset-search"}
                                              className={"border-black border-2"}
                                              type={"text"}
                                              placeholder={"Filter by dataset id"}
                                              onChange={event => setFilterValue(event.target.value)}
                                />
                            </Form.Group>
                        </Form>
                        {c4ghKeyPresent &&
                            <Nav className={"flex-column"}>
                                {filteredDatasets ?
                                    filteredDatasets.stableIds.map((value, index) => {
                                        return (
                                            <Nav.Item key={index}>
                                                <Nav.Link as={Link} to={`datasets/${value}`}>{value}</Nav.Link>
                                            </Nav.Item>
                                        )
                                    })
                                    :
                                    <div style={{flex: "1 1 auto"}}>
                                        <p>content (fills remaining space)</p>
                                    </div>
                                }
                            </Nav>
                        }
                    </aside>
                </Col>
                <Col><Outlet/></Col>
            </Row>

        </>
    )
}
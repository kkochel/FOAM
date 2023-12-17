import {FC, Fragment, useContext, useEffect, useState} from "react";
import {Dataset, DatasetFile} from "./DashboardView.tsx";
import {Col, Container, Form, Row} from "react-bootstrap";
import {fetchData} from "../common/consts.ts";
import {DatasetFileItemCard} from "./DatasetFileItemCard.tsx";
import {useQuery} from "@tanstack/react-query";
import {DatasetFilesHeader} from "./DatasetFilesHeader.tsx";
import {WcagContext} from "../common/WcagContextProvider.tsx";

interface Props {
    dataset: Dataset
}

export const DatasetFiles: FC<Props> = (props) => {
    const {dataset} = props
    const [files, setFiles] = useState<DatasetFile[]>()
    const [filteredFiles, setFilteredFiles] = useState<DatasetFile[]>()
    const [filterValue, setFilterValue] = useState<string>()
    const {fontSize} = useContext(WcagContext)

    const href: string = `/api/export/datasets/${dataset.stableId}/files`

    const {data} = useQuery({
        queryKey: ["dataset-files", dataset.stableId],
        queryFn: () => fetchData<DatasetFile[]>(href),
        refetchInterval: 20000
    })

    useEffect(() => {
        if (data) {
            setFiles(data)
        }
    }, [data]);

    useEffect(() => {
        if (files && filterValue) {
            const filtered = files.filter(file =>
                file.stableId.toLowerCase().includes(filterValue.toLowerCase()) ||
                file.lastStage.toLowerCase().includes(filterValue.toLowerCase()))
            setFilteredFiles(filtered)
        } else {
            setFilteredFiles(files)
        }
    }, [filterValue, files]);

    return (
        <Container className={"border-with-shadow bg-white"}>
            <DatasetFilesHeader datasetId={dataset.stableId} status={dataset.status}/>
            <Form className={"m-2"} onSubmit={e => e.preventDefault()}>
                <Form.Group style={{"textAlign": "left"}}>
                    <Form.Label htmlFor={"file-search"}>Search file</Form.Label>
                    <Form.Control id={"file-search"}
                                  className={`border-black border-2 form-control-${fontSize}`}
                                  type={"text"}
                                  placeholder={"Filter by file id or current status"}
                                  onChange={event => setFilterValue(event.target.value)}
                    />
                </Form.Group>
            </Form>
            <Row xs={1} md={2} className="overflow-auto" style={{maxHeight: '70vh'}}>
                {filteredFiles && filteredFiles.length > 0 ? filteredFiles.map((value, index) => {
                    return <Fragment key={index}>
                        <Col>
                            <DatasetFileItemCard
                                datasetFile={value}
                                datasetStatus={dataset.status}
                                datasetId={dataset.stableId}/>
                        </Col>
                    </Fragment>
                })
                    :
                    <Col>
                      <div className={"wrap"}>No content. Please change the search phrase</div>
                    </Col>
                }
            </Row>
        </Container>
    )
}
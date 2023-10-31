import {FC, Fragment, useEffect, useState} from "react";
import {Dataset, DatasetFile} from "./DashboardView.tsx";
import {Col, Container, Row} from "react-bootstrap";
import {fetchData} from "../common/consts.ts";
import {DatasetFileItemCard} from "./DatasetFileItemCard.tsx";
import {useQuery} from "@tanstack/react-query";
import {DatasetFilesHeader} from "./DatasetFilesHeader.tsx";

interface Props {
    dataset: Dataset
}

export const DatasetFiles: FC<Props> = (props) => {
    const {dataset} = props
    const [files, setFiles] = useState<DatasetFile[]>()

    const href: string = `/api/export/datasets/${dataset.stableId}/files`

    const {data} = useQuery({
        queryKey: ["dataset-files", dataset.stableId],
        queryFn: () => fetchData<DatasetFile[]>(href)
    })

    useEffect(() => {
        if (data) {
            setFiles(data)
        }
    }, [data]);

    return (
        <Container className={"border-with-shadow"}>
            <DatasetFilesHeader datasetId={dataset.stableId} status={dataset.status}/>
            <Row xs={1} md={2} className="overflow-auto" style={{maxHeight: '75vh'}}>
                {files && files.map((value, index) => {
                    return <Fragment key={index}>
                        <Col>
                            <DatasetFileItemCard
                                datasetFile={value}
                                datasetStatus={dataset.status}
                                datasetId={dataset.stableId}/>
                        </Col>
                    </Fragment>
                })}
            </Row>
        </Container>
    )
}
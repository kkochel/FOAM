import {FC, Fragment, useEffect, useState} from "react";
import {Dataset, DatasetFile} from "./DashboardView.tsx";
import {Button, Col, Container, Row} from "react-bootstrap";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {disableExportButton, fetchData} from "../common/consts.ts";
import {DatasetFileItemCard} from "./DatasetFileItemCard.tsx";
import {useQuery} from "@tanstack/react-query";

interface Props {
    dataset: Dataset
}

const confirmationMessage: string = "Please confirm that you intend to start the dataset export process."

export const DatasetFiles: FC<Props> = (props) => {
    const {dataset} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);
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

    const handleExportAllFiles = () => {
        setSuccessNotification(true)
    }

    return (
        <Container className={"border-with-shadow"}>
            <h4 className={"mt-2"}>{dataset.stableId}</h4>
            <Button variant={"outline-primary"}
                    onClick={() => setConfirmationDialog(true)}
                    className={"m-3"}
                    disabled={disableExportButton(dataset.status)}>Export all files to outbox</Button>
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
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportAllFiles}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </Container>
    )

}
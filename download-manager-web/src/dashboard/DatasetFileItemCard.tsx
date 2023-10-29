import {FC, useState} from "react";
import {DatasetFile} from "./DashboardView.tsx";
import {Button, Card, ListGroup} from "react-bootstrap";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {DatasetStatus, disableExportButton, fetchData, postData} from "../common/consts.ts";
import {ExportRequest} from "./Dashboard.tsx";

interface Props {
    datasetFile: DatasetFile
    datasetStatus: DatasetStatus
    datasetId: string
}

interface FileHistoryItem {
    stage: string
    created: string
}

const confirmationMessage: string = "Please confirm that you intend to start the file export process."

function getLastStatus(lastStage: string | undefined) {
    if (lastStage) {
        return `Status: ${lastStage}`
    }
    return undefined;
}

export const DatasetFileItemCard: FC<Props> = (props) => {
    const {datasetFile, datasetStatus, datasetId} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);
    const [fileLog, setFileLog] = useState<FileHistoryItem[]>()


    const handleExportFile = () => {
        postData<ExportRequest, string>(`/api/export/datasets/${datasetId}/files`, {stableId: datasetFile.stableId})
            .then(() => setSuccessNotification(true))
    }

    const getFileHistory = () => {
        if (!fileLog) {
            fetchData<FileHistoryItem[]>(`/api/export/datasets/${datasetId}/files/${datasetFile.stableId}/history`)
                .then(response => setFileLog(response))
        }
    }
    return (
        <>
            <Card className={"m-1 border-with-shadow"}>
                <Card.Body>
                    <Card.Title>{datasetFile.stableId} </Card.Title>
                    <Card.Subtitle className={"text-bg-primary"}>{getLastStatus(datasetFile.lastStage)}</Card.Subtitle>
                        {fileLog ?
                            <>
                                <p className={"fw-bolder"}>Export history:</p>
                                <ListGroup className={"overflow-auto"}
                                           style={{"maxHeight": "200px"}}>
                                    {fileLog.map((value, index) => {
                                        return <ListGroup.Item key={index}>{value.stage} ({value.created})</ListGroup.Item>
                                    })}
                                </ListGroup>
                            </>
                            : null
                        }
                    <Button variant={"outline-primary"}
                            disabled={disableExportButton(datasetStatus)}
                            onClick={() => setConfirmationDialog(true)}
                            className={"m-1"}>Export to outbox</Button>

                    {fileLog ?
                    <Button
                            onClick={()=> setFileLog(undefined)}
                            className={"m-1"}>Close history</Button>
                    :
                    <Button variant={"outline-primary"}
                            onClick={getFileHistory}
                            className={"m-1"}>Display file history</Button>
                    }

                </Card.Body>
            </Card>


            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportFile}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </>
    )
}

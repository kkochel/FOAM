import {FC, useState} from "react";
import {DatasetFile} from "./DashboardView.tsx";
import {Button, Card} from "react-bootstrap";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {DatasetStatus, disableExportButton, postData} from "../common/consts.ts";
import {ExportRequest} from "./Dashboard.tsx";
import {FileHistoryList} from "./FileHistoryList.tsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";

interface Props {
    datasetFile: DatasetFile
    datasetStatus: DatasetStatus
    datasetId: string
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
    const [displayFileLog, setDisplayFileLog] = useState<boolean>()

    const href: string = `/api/export/datasets/${datasetId}/files`
    const queryClient = useQueryClient()

    const {mutate} = useMutation({
        mutationFn: (dto: ExportRequest) => postData<ExportRequest, string>(href, dto),
        onSuccess: () => {
            queryClient.refetchQueries(["dataset-files", datasetId])
        },
        onError: (error) => {
            console.error(error);
        }
    })

    const handleExportRequest = () => {
        mutate({stableId: datasetFile.stableId})
        setSuccessNotification(true)
    }

    return (
        <>
            <Card className={"m-1 border-with-shadow"}>
                <Card.Body>
                    <Card.Title>{datasetFile.stableId} </Card.Title>
                    <Card.Subtitle className={"text-bg-secondary"}>{getLastStatus(datasetFile.lastStage)}</Card.Subtitle>
                    <Button variant={"outline-primary"}
                            disabled={disableExportButton(datasetStatus)}
                            onClick={() => setConfirmationDialog(true)}
                            className={"m-1"}>Export to outbox</Button>

                    {displayFileLog ? <FileHistoryList datasetId={datasetId} fileId={datasetFile.stableId}/> : null}
                    {displayFileLog ?
                        <Button variant={"outline-primary"}
                                onClick={() => setDisplayFileLog(false)}
                                className={"m-1"}>Close history</Button>
                        :
                        <Button variant={"outline-primary"}
                                onClick={() => setDisplayFileLog(true)}
                                className={"m-1"}>Display file history</Button>
                    }

                </Card.Body>
            </Card>
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportRequest}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </>
    )
}

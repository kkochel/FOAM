import {FC, useState} from "react";
import {Button} from "react-bootstrap";
import {DatasetStatus, disableExportButton, postData} from "../common/consts.ts";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {ExportRequest} from "./Dashboard.tsx";

interface Props {
    datasetId:string
    status: DatasetStatus
}

const confirmationMessage: string = "Please confirm that you intend to start the dataset export process."

export const DatasetFilesHeader: FC<Props> = (props) => {
    const {datasetId, status} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);

    const href: string = `/api/export/datasets/${datasetId}`
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

    const handleExportAllFiles = () => {
        mutate({stableId: datasetId})
        setSuccessNotification(true)
    }
    return (
        <>
            <h4 className={"mt-2"}>{datasetId}</h4>
            <Button variant={"outline-primary"}
                    onClick={() => setConfirmationDialog(true)}
                    className={"mt-2"}
                    disabled={disableExportButton(status)}>Export all files to outbox</Button>
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportAllFiles}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </>
    )
}
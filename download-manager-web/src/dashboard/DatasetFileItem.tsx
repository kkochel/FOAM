import {FC, useState} from "react";
import {DatasetFile} from "./DashboardView.tsx";
import {Accordion, Button} from "react-bootstrap";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {DatasetStatus, disableExportButton, postData} from "../common/consts.ts";
import {ExportRequest} from "./Dashboard.tsx";

interface Props {
    eventKeyId: string
    datasetFile: DatasetFile
    datasetStatus: DatasetStatus
    datasetId: string
}

const confirmationMessage: string = "Please confirm that you intend to start the file export process."

export const DatasetFileItem: FC<Props> = (props) => {
    const {eventKeyId, datasetFile, datasetStatus, datasetId} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);


    const handleExportFile = () => {
        postData<ExportRequest, string>(`http://localhost:8080/api/export/datasets/${datasetId}/files`, {stableId: datasetFile.stableId})
            .then(value => console.log("+++++", value))
            .then(() => setSuccessNotification(true))
    }

    return (
        <>
            <Accordion.Item eventKey={eventKeyId}>
                <Accordion.Header>{datasetFile.stableId}
                </Accordion.Header>
                <Accordion.Body>
                    <div className={"text-lg-start"}>
                        <span>File size [bytes]: {datasetFile.fileSize}</span>
                    </div>

                    {/*{datasetFile.history.length > 0 ?*/}
                    {/*    <ListGroup>*/}
                    {/*        {datasetFile.history.map((value, index) => {*/}
                    {/*            return <ListGroup.Item key={index}>{value}</ListGroup.Item>*/}
                    {/*        })}*/}
                    {/*    </ListGroup>*/}
                    {/*    :*/}
                    {/*    <div>The history is empty</div>*/}
                    {/*}*/}

                    <Button disabled={disableExportButton(datasetStatus)}
                            onClick={() => setConfirmationDialog(true)}
                            className={"m-3"}>Export file to outbox</Button>
                </Accordion.Body>
            </Accordion.Item>
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportFile}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </>
    )
}
import {FC, useState} from "react";
import {DatasetFile} from "./DashboardView.tsx";
import {Accordion, Button, ListGroup} from "react-bootstrap";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";

interface Props {
    eventKeyId: string
    datasetFile: DatasetFile
}

const confirmationMessage: string = "Please confirm that you intend to start the file export process."

export const DatasetFileItem: FC<Props> = (props) => {
    const {eventKeyId, datasetFile} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);


    const handleExportFile = () => {

        setSuccessNotification(true)
    }

    return (
        <>
            <Accordion.Item eventKey={eventKeyId}>
                <Accordion.Header>{datasetFile.egafId}</Accordion.Header>
                <Accordion.Body>
                    {datasetFile.history.length > 0 ?
                        <ListGroup>
                            {datasetFile.history.map((value, index) => {
                                return <ListGroup.Item key={index}>{value}</ListGroup.Item>
                            })}
                        </ListGroup>
                        :
                        <div>The history is empty</div>
                    }

                    <Button disabled={!datasetFile.canExport}
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
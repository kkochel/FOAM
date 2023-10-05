import {FC, useState} from "react";
import {Dataset} from "./DashboardView.tsx";
import {Accordion, Button, Container} from "react-bootstrap";
import {DatasetFileItem} from "./DatasetFileItem.tsx";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";

interface Props {
    dataset: Dataset
}

const confirmationMessage: string = "Please confirm that you intend to start the dataset export process."

export const DatasetItem: FC<Props> = (props) => {
    const {dataset} = props
    const [confirmationDialog, setConfirmationDialog] = useState<boolean>(false)
    const [successNotification, setSuccessNotification] = useState(false);

    const handleExportAllFiles = () => {

        setSuccessNotification(true)
    }

    return (
        <Container className={"border-with-shadow"}>
            <h4 className={"mt-2"}>{dataset.egadId}</h4>
            <Accordion>
                {dataset.files.map((value, index) => {
                    return <DatasetFileItem key={index} eventKeyId={index.toString()} datasetFile={value}/>
                })}

            </Accordion>

            <Button disabled={!dataset.canExport}
                    onClick={() => setConfirmationDialog(true)}
                    className={"m-3"}>Export all files to outbox</Button>
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportAllFiles}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </Container>
    )

}
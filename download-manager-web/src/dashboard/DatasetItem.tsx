import {FC, useState} from "react";
import {Dataset} from "./DashboardView.tsx";
import {Accordion, Button, Container} from "react-bootstrap";
import {DatasetFileItem} from "./DatasetFileItem.tsx";
import {ConfirmationDialog} from "../common/ConfirmationDialog.tsx";
import {SuccessNotification} from "../common/SuccessNotification.tsx";
import {disableExportButton} from "../common/consts.ts";

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
            <h4 className={"mt-2"}>{dataset.stableId}</h4>
            <Accordion>
                {dataset.files.map((value, index) => {
                    return <DatasetFileItem key={index}
                                            eventKeyId={index.toString()}
                                            datasetFile={value}
                                            datasetStatus={dataset.status}
                                            datasetId={dataset.stableId}/>})}

            </Accordion>

            <Button onClick={() => setConfirmationDialog(true)}
                    className={"m-3"}
                    disabled={disableExportButton(dataset.status)}>Export all files to outbox</Button>
            <ConfirmationDialog showConfirmation={confirmationDialog}
                                onHideConfirmation={setConfirmationDialog}
                                action={handleExportAllFiles}
                                message={confirmationMessage}/>
            <SuccessNotification successNotification={successNotification}
                                 setSuccessNotification={setSuccessNotification}/>
        </Container>
    )

}
import {FC} from "react";
import {Button, Modal} from "react-bootstrap";

interface Props {
    showConfirmation: boolean
    onHideConfirmation: (hide: boolean) => void
    action: () => void
    message: string
}

export const ConfirmationDialog: FC<Props> = (props) => {
    const {showConfirmation, onHideConfirmation, action, message} = props

    function handleDenied() {
        onHideConfirmation(false)
    }

    function handleApprove() {
        action()
        onHideConfirmation(false)
    }

    return (
        <>
            <Modal
                show={showConfirmation}
                size={"lg"}
                onHide={() => onHideConfirmation(false)}
                backdrop="static"
                keyboard={true}
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title>Please confirm the action</Modal.Title>
                </Modal.Header>
                <Modal.Body>{message}</Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleDenied}>No</Button>
                    <Button onClick={handleApprove} variant="secondary">Yes</Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
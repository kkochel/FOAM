import {FC, useContext} from "react";
import {Button, Modal} from "react-bootstrap";
import {WcagContext} from "./WcagContextProvider.tsx";

interface Props {
    showConfirmation: boolean
    onHideConfirmation: (hide: boolean) => void
    action: () => void
    message: string
}

export const ConfirmationDialog: FC<Props> = (props) => {
    const {showConfirmation, onHideConfirmation, action, message} = props
    const {fontSize} = useContext(WcagContext)

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
                    <Modal.Title className={`h4-${fontSize}`}>Please confirm the action</Modal.Title>
                </Modal.Header>
                <Modal.Body className={`${fontSize}`}>{message}</Modal.Body>
                <Modal.Footer>
                    <Button variant="primary"
                            className={`btn-${fontSize}`}
                            onClick={handleDenied}>No</Button>
                    <Button variant="secondary"
                            className={`btn-${fontSize}`}
                            onClick={handleApprove}>Yes</Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
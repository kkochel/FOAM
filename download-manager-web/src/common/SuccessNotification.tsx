import {Dispatch, FC, SetStateAction} from "react";
import {Toast} from "react-bootstrap";
import {EXPORT_STARTED} from "./consts.ts";

interface Props {
    successNotification: boolean,
    setSuccessNotification: Dispatch<SetStateAction<boolean>>
}

export const SuccessNotification: FC<Props> = (props) => {
    const {successNotification, setSuccessNotification} = props

    return (
        <Toast show={successNotification}
               onClose={() => setSuccessNotification(false)}
               className={"text-black border-2 border-primary position-absolute top-0 end-0"}>
            <Toast.Header>
                <strong>{EXPORT_STARTED}</strong>
            </Toast.Header>
        </Toast>
    )
}
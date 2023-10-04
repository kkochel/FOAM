import {FC} from "react";
import {DatasetFile} from "./DashboardView.tsx";
import {Accordion, Button, ListGroup} from "react-bootstrap";

interface Props {
    eventKeyId: string
    datasetFile: DatasetFile
}

export const DatasetFileItem: FC<Props> = (props) => {
    const {eventKeyId, datasetFile} = props
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

                    <Button>Export file to outbox</Button>
                </Accordion.Body>
            </Accordion.Item>
        </>
    )
}
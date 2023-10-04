import {FC} from "react";
import {Dataset} from "./DashboardView.tsx";
import {Accordion, Button, Container} from "react-bootstrap";
import {DatasetFileItem} from "./DatasetFileItem.tsx";

interface Props {
    dataset: Dataset
}

export const DatasetItem: FC<Props> = (props) => {
    const {dataset} = props
    return (
        <Container>
                    <h4>{dataset.egadId}</h4>
                        <Accordion>
                            {dataset.files.map((value, index) => {
                                return <DatasetFileItem key={index} eventKeyId={index.toString()} datasetFile={value}/>
                            })}

                        </Accordion>

                        <Button className={"m-1"}>Export all files to outbox</Button>
        </Container>
    )

}
import {FC, useEffect, useState} from "react";
import {useQuery} from "@tanstack/react-query";
import {fetchData} from "../common/consts.ts";
import {ListGroup} from "react-bootstrap";

interface Props {
    datasetId: string
    fileId: string
}

interface FileHistoryItem {
    stage: string
    created: string
}

export const FileHistoryList: FC<Props> = (props) => {
    const {datasetId, fileId} = props
    const [fileLog, setFileLog] = useState<FileHistoryItem[]>()

    const href: string = `/api/export/datasets/${datasetId}/files/${fileId}/history`
    const {data} = useQuery({
        queryKey: ["file-log", datasetId, fileId],
        queryFn: () => fetchData<FileHistoryItem[]>(href)
    })

    useEffect(() => {
        if (data) {
            setFileLog(data)
        }
    }, [data]);

    return (
        <>
            {fileLog ?
                <>
                    <p className={"fw-bolder"}>Export history:</p>
                    <ListGroup className={"overflow-auto"}
                               style={{"maxHeight": "200px"}}>
                        {fileLog.map((value, index) => {
                            return <ListGroup.Item key={index}>{value.stage} ({value.created})</ListGroup.Item>
                        })}
                    </ListGroup>
                </>
                : null
            }
        </>
    )
}
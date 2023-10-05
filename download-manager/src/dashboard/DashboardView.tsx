import {DatasetItem} from "./DatasetItem.tsx";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";


export interface DatasetFile {
    egafId: string,
    history: string[]
    canExport: boolean
}

export interface Dataset {
    egadId: string,
    canExport: boolean,
    files: DatasetFile[]
}

const fooDatasets: Dataset[] = [
    {
        egadId: "EGAD010000051", canExport: true, files: [{
            egafId: "EGAF000001",
            history: ["Start reencryption", "Reenceryption finished"],
            canExport: false
        },
            {
                egafId: "EGAF000002",
                history: ["Start reencryption", "Reenceryption finished", "File deleted in outbox"],
                canExport: true

            }
        ]
    },
    {
        egadId: "EGAD020000051", canExport: false, files: [{
            egafId: "EGAF000011",
            history: ["Start reencryption", "Reenceryption finished"],
            canExport: false
        },
            {
                egafId: "EGAF000033",
                history: ["Start reencryption", "Reenceryption finished", "File deleted in outbox"],
                canExport: false
            }
        ]
    }
]

type UrlParams = {
    datasetId: string
}

export const DashboardView = () => {
    const {datasetId} = useParams<UrlParams>();
    const [dataset, setDataset] = useState<Dataset>()

    useEffect(() => {
        const selectedValue: Dataset | undefined = fooDatasets.filter(d => d.egadId === datasetId).pop()
        if (selectedValue) {
            setDataset(selectedValue)
        }

    }, [datasetId])

    // const {data} = useQuery<string[]>({
    //     queryKey: ["users", AppSettings.API_ENDPOINT],
    //     queryFn: () => fetchData<string[]>(AppSettings.API_ENDPOINT),
    // });

    return (
        <>
            {dataset ? <DatasetItem dataset={dataset}/> : <div>There is no data to display</div>}
        </>
    )
}
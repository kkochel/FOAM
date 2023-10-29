import {DatasetItem} from "./DatasetItem.tsx";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {DatasetStatus, fetchData} from "../common/consts.ts";
import {useQuery} from "@tanstack/react-query";


export interface DatasetFile {
  stableId: string,
  lastStage: string,
}

export interface Dataset {
  stableId: string,
  description: string
  title: string
  status: DatasetStatus
}

type UrlParams = {
  datasetId: string
}

export const DashboardView = () => {
  const {datasetId} = useParams<UrlParams>();
  const [dataset, setDataset] = useState<Dataset>()

  const href: string = `/api/datasets/${datasetId}`

  const {data} = useQuery({
    queryKey: ["dataset", datasetId],
    queryFn: () => fetchData<Dataset>(href)
  })

  useEffect(() => {
    if (data) {
      setDataset(data)
    }
  }, [data]);


  return (
  <>
    {dataset ? <DatasetItem dataset={dataset}/> : <div>There is no data to display</div>}
  </>
  )
}
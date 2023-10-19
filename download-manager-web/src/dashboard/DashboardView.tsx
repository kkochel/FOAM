import {DatasetItem} from "./DatasetItem.tsx";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {DatasetStatus, fetchData} from "../common/consts.ts";
import {AppSettings} from "../api/AppSettings.ts";


export interface DatasetFile {
  stableId: string,
  fileSize: number
  history: string[]
}

export interface Dataset {
  stableId: string,
  description: string
  title: string
  status: DatasetStatus
  files: DatasetFile[]
}

type UrlParams = {
  datasetId: string
}

export const DashboardView = () => {
  const {datasetId} = useParams<UrlParams>();
  const [dataset, setDataset] = useState<Dataset>()


  useEffect(() => {
    fetchData<Dataset>(AppSettings.DOMAIN + `/api/datasets/${datasetId}`)
    .then(response => setDataset(response))
  }, [datasetId])


  return (
  <>
    {dataset ? <DatasetItem dataset={dataset}/> : <div>There is no data to display</div>}
  </>
  )
}
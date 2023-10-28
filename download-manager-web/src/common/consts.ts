import {axiosClient} from "../main";

export const fetchData = <T>(href: string): Promise<T> =>
    axiosClient.get(href,).then((response) => response.data);

export const postData = <D, R>(href: string, data: D): Promise<R> =>
    axiosClient.post(href, data,).then((response) => response.data);

export const EXPORT_STARTED: string = "Export to the outbox has begun"

export enum DatasetStatus {
    available = "available",
    revoked = "revoked"
}

export const disableExportButton = (status: DatasetStatus): boolean => {
    if (DatasetStatus.available === status) {
        return false
    }

    if (DatasetStatus.revoked === status) {
        return true
    }

    throw new Error("Status has not been handled correctly: " + status)
}
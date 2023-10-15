import {axiosClient} from "../main";


const getAuthenticate = () => {
    const token: string | null = localStorage.getItem("token")
    if (token) {
        return {Authorization: 'Bearer ' + token};
    } else {
        return {}
    }
}

export const fetchData = <T>(href: string): Promise<T> =>
    axiosClient.get(href, {headers: getAuthenticate()}).then((response) => response.data);

export const postData = <D, R>(href: string, data: D): Promise<R> =>
    axiosClient.post(href, data, {headers: getAuthenticate()}).then((response) => response.data);

export const EXPORT_STARTED: string = "Export to the outbox has begun"

export enum DatasetStatus {
    available = "available",
    revoked = "revoked"
}

export const disableExportButton = (status: DatasetStatus): boolean => {
    if (DatasetStatus.available === status) {
        return false;
    }

    if (DatasetStatus.revoked === status) {
        return true
    }

    throw new Error("Status has not been handled correctly: " + status)
}
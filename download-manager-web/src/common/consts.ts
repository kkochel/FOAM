import {axiosClient} from "../main";


const getAuthenticate = () => {
    const token: string | null = localStorage.getItem("token")
    if (token) {
        return { Authorization: 'Bearer ' + token };
    } else {
        return {}
    }
}

export const fetchData = <T>(href: string): Promise<T> =>
    axiosClient.get(href, {headers: getAuthenticate()}).then((response) => response.data);

export const EXPORT_STARTED: string = "Export to the outbox has begun"
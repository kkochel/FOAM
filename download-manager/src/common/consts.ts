import {axiosClient} from "../main";

export const fetchData = <T>(href: string): Promise<T> =>
    axiosClient.get(href).then((response) => response.data);

export const EXPORT_STARTED: string = "Export to the outbox has begun"
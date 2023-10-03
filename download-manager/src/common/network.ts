import {axiosClient} from "../main";

export const fetchData = <T extends unknown>(href: string): Promise<T> =>
    axiosClient.get(href).then((response) => response.data);
import {useRouteError} from "react-router-dom";

export const ErrorPage = () => {
    const error = useRouteError();
    console.error(error);

    return (
        <div id="error-page">
            <h1>Something gone wrong !</h1>
            <p>Sorry, an unexpected error has occurred.</p>
            <p>
                {/* eslint-disable-next-line @typescript-eslint/ban-ts-comment */}
                <i>{// @ts-ignore
                    error.statusText || error.message}</i>
            </p>
        </div>
    );
}
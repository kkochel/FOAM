import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom"
import {ErrorPage} from "./ErrorPage.tsx";
import {SignIn} from "./sign-in/SignIn.tsx";
import {Dashboard} from "./dashboard/Dashboard.tsx";
import axios from "axios";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";
import {DashboardView} from "./dashboard/DashboardView.tsx";
import './assets/custom.scss'
import {AuthProvider} from "./auth/AuthProvider.tsx";
import {AppSettings} from "./api/AppSettings.ts";
import {Container} from "react-bootstrap";
import {PrivateRoute} from "./auth/PrivateRoute.tsx";
import {WcagProvider} from "./common/WcagContextProvider.tsx";
import {DeclarationOfAvailability} from "./common/DeclarationOfAvailability.tsx";

export const axiosClient = axios.create({
    withCredentials: true,
    baseURL: AppSettings.DOMAIN,
    timeout: 4000,
    headers: {
        "Accept": "application/json",
        "Content-Type": "application/json; charset=utf-8",
    },
})


const refreshAccessToken = () => {
    axiosClient.post("/api/auth/refresh-token", {})
}

axiosClient.interceptors.response.use(async (response) => {
    return response
}, function (error) {
    const originalConfig = error.config;

    if (error.response.status === 412) {
        refreshAccessToken();
        return axiosClient(originalConfig);
    }

    if (error.response.status === 403) {
        window.location.href = '/sign-in';
    }

    return Promise.reject(error);
})

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 60000,
        },
    },
})

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/sign-in",
        element: <SignIn/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/declaration",
        element: <DeclarationOfAvailability/>,
        errorElement: <ErrorPage/>

    },
    {
        path: "/dashboard",
        element:
            <PrivateRoute>
                <Dashboard/>
            </PrivateRoute>
        ,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/dashboard/datasets/:datasetId",
                element: <DashboardView/>,
                errorElement: <ErrorPage/>
            }
        ]
    },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AuthProvider>
            <WcagProvider>
                <QueryClientProvider client={queryClient}>
                    <Container fluid className={"h-100"}>
                        <RouterProvider router={router}/>
                        <ReactQueryDevtools initialIsOpen={false}/>
                    </Container>
                </QueryClientProvider>
            </WcagProvider>
        </AuthProvider>
    </React.StrictMode>,
)

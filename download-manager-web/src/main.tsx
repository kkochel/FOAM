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
import {jwtDecode} from "jwt-decode";

export const axiosClient = axios.create({
    baseURL: AppSettings.DOMAIN,
    timeout: 4000,
    headers: {
        "Accept": "application/json",
        "Content-Type": "application/json; charset=utf-8",
    },
})

axiosClient.interceptors.request.use((config) => {
    const token: string | null = localStorage.getItem("token")
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

const refreshAccessToken = () => {
    axiosClient.post("/api/auth/refresh-token", {"refreshToken": localStorage.getItem("refreshToken")})
        .then(response => {
            if (response && response.data.token) {
                localStorage.setItem("token", response.data.token)
                localStorage.setItem("refreshToken", response.data.refreshToken)
            }
        })
}

const addMinutes = (date: Date, minutes: number): Date => {
    date.setMinutes(date.getMinutes() + minutes);
    return date;
}

const isTokenExpired = (): boolean | undefined => {
    const token: string | null = localStorage.getItem("token")
    if (token) {
        const decoded = jwtDecode(token)
        if (decoded.iat) {
            const nextTwoMinutes: Date = addMinutes(new Date(), 2)
            return nextTwoMinutes.getMilliseconds() > decoded.iat
        }
    }
}

axiosClient.interceptors.response.use(async (response) => {
    return response
}, function (error) {
    const originalConfig = error.config;

    if (isTokenExpired()) {
        refreshAccessToken();
    }

    if (error.response.status === 403 && localStorage.getItem("refreshToken")) {
        refreshAccessToken();
        return axiosClient(originalConfig);
    }

    if (error.response.status === 401 && localStorage.getItem("refreshToken")) {
        localStorage.removeItem("token")
        localStorage.removeItem("refreshToken")
        window.location.href = '/sing-in';
    }

    return Promise.reject(error);
})


const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 300000,
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
        path: "/sing-in",
        element: <SignIn/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/dashboard",
        element: <Dashboard/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/dashboard/datasets/:datasetId",
                element: <DashboardView/>
            }
        ]
    },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AuthProvider>
            <QueryClientProvider client={queryClient}>
                <RouterProvider router={router}/>
                <ReactQueryDevtools initialIsOpen={false}/>
            </QueryClientProvider>
        </AuthProvider>
    </React.StrictMode>,
)

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

export const axiosClient = axios.create()

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: Infinity,
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

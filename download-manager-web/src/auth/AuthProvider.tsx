import {createContext, Dispatch, FC, ReactNode, SetStateAction, useState} from "react";
import {NavigateFunction} from "react-router-dom";
import {axiosClient} from "../main.tsx";

const handleSignOut = (setAuthenticated: Dispatch<SetStateAction<boolean>>, navigate: NavigateFunction,) => {
    setAuthenticated(false)
    navigate("/")
}

const handleSignIn = (setAuthenticated: Dispatch<SetStateAction<boolean>>, navigate: NavigateFunction, request: SingInRequest): Promise<number> => {
    return axiosClient.post("/api/auth/sign-in", request)
        .then(response => {
            if (response.status === 200) {
                setAuthenticated(true)
                navigate("/dashboard")
            }
            return response.status
        })
        .catch(reason => {
            return reason.response.status
        })
}


interface AuthContextProps {
    authenticated: boolean
    setAuthenticated: Dispatch<SetStateAction<boolean>>
    handleSignIn: (setAuthenticated: Dispatch<SetStateAction<boolean>>, navigate: NavigateFunction, request: SingInRequest) => Promise<number>
    handleSignOut: (setAuthenticated: Dispatch<SetStateAction<boolean>>, navigate: NavigateFunction) => void

}

export const AuthContext = createContext<AuthContextProps>({
    authenticated: false,
    setAuthenticated: () => {
    },
    handleSignIn,
    handleSignOut
})

export interface SingInRequest {
    username: string
    password: string
}

interface AuthProviderProps {
    children?: ReactNode
}

const tokenCookie = (): boolean => {
    const token = document.cookie.split("; ").find((row) => row.startsWith("token="))?.split("=")[1]
    return !!token;
}

export const AuthProvider: FC<AuthProviderProps> = (props) => {
    const {children} = props
    const [token, setToken] = useState<boolean>(tokenCookie())

    return (
        <AuthContext.Provider
            value={{authenticated: token, setAuthenticated: setToken, handleSignIn, handleSignOut}}>{children}
        </AuthContext.Provider>
    )
}

import {createContext, Dispatch, FC, ReactNode, SetStateAction, useState} from "react";
import {NavigateFunction} from "react-router-dom";
import axios from "axios";
import {AppSettings} from "../api/AppSettings.ts";

const handleSignOut = (setToken: Dispatch<SetStateAction<string | null>>, navigate: NavigateFunction,) => {
    localStorage.removeItem("token")
    localStorage.removeItem("refreshToken")
    setToken(null)
    navigate("/")
}

const handleSignIn = (setToken: Dispatch<SetStateAction<string | null>>, navigate: NavigateFunction, request: SingInRequest): Promise<number> => {
    return  axios.post(AppSettings.DOMAIN + "/api/auth/sign-in", request, {timeout: 3000})
        .then(response => {
            if (response && response.data.token) {
                setToken(response.data.token)
                localStorage.setItem("token", response.data.token)
                localStorage.setItem("refreshToken", response.data.refreshToken)
            }
            return response.status
        })
        .then((value) => {
            navigate("/dashboard")
            return value
        })
        .catch(reason => {return reason.response.status} )

}


interface AuthContextProps {
    token: string | null
    setToken: Dispatch<SetStateAction<string | null>>
    handleSignIn: (setToken: Dispatch<SetStateAction<string | null>>, navigate: NavigateFunction, request: SingInRequest) => Promise<number>
    handleSignOut: (setToken: Dispatch<SetStateAction<string | null>>, navigate: NavigateFunction) => void

}

export const AuthContext = createContext<AuthContextProps>({
    token: null, setToken: () => {
    }, handleSignIn, handleSignOut
})

export interface SingInRequest {
    username: string
    password: string
}

export interface SignInResponse {
    token: string
    refreshToken: string
}

interface AuthProviderProps {
    children?: ReactNode
}

export const AuthProvider: FC<AuthProviderProps> = (props) => {
    const {children} = props
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"))

    return (
        <AuthContext.Provider value={{token: token, setToken: setToken, handleSignIn, handleSignOut}}>{children}
        </AuthContext.Provider>
    )
}

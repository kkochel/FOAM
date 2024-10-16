import {createContext, Dispatch, FC, ReactNode, SetStateAction, useEffect, useState} from "react";
import {NavigateFunction} from "react-router-dom";
import {axiosClient} from "../main.tsx";

const handleSignOut = (setAuthenticated: Dispatch<SetStateAction<boolean>>, navigate: NavigateFunction) => {
    axiosClient.post("api/auth/sign-out")
        .then(response => {
            if (response.status > 200) {
                setAuthenticated(false)
                navigate("/")
            } else {
                setAuthenticated(false)
                navigate("/")
                console.error("Logout endpoint return not 2** status")
            }
        })
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
    verifyCookie: () => Promise<number>

}

const verifyCookie = async (): Promise<number> => {
    return await checkCookie();
}

export const AuthContext = createContext<AuthContextProps>({
    authenticated: false,
    setAuthenticated: () => {},
    handleSignIn,
    handleSignOut,
    verifyCookie: verifyCookie
})

export interface SingInRequest {
    username: string
    password: string
}

interface AuthProviderProps {
    children?: ReactNode
}

const checkCookie = async (): Promise<number> => {
    const response = await axiosClient.get("/api/auth/is-authenticated",{validateStatus: (status) => status < 500 });
    return response.status;
}

export const AuthProvider: FC<AuthProviderProps> = (props) => {
    const {children} = props
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false)

    console.log("AuthProvider: isAuthenticated: ", isAuthenticated)

    useEffect(() => {
        verifyCookie()
            .then((status) => {
                if (status === 200) {
                    setIsAuthenticated(true);
                }

                if (status === 401) {
                    console.log("Not authenticated");
                    setIsAuthenticated(false);
                }

                if (status === 403) {
                    setIsAuthenticated(false);
                }
            })
            .catch(() => {
                console.error("Not authenticated");
                setIsAuthenticated(false);
            });
    }, []);

    return (
        <AuthContext.Provider
            value={{
                authenticated: isAuthenticated,
                setAuthenticated: setIsAuthenticated,
                handleSignIn,
                handleSignOut,
                verifyCookie: verifyCookie
            }}>{children}</AuthContext.Provider>
    )
}

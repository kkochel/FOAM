import {createContext, Dispatch, FC, ReactNode, SetStateAction, useState} from "react";

export type FontClass = "normal-style" | "big-style"

export interface WcagContextProps {
    fontSize: FontClass
    setFontSize: Dispatch<SetStateAction<FontClass>>
}

export const WcagContext = createContext<WcagContextProps>({
    fontSize: "normal-style",
    setFontSize: () => {
    },
})

interface WcagProps {
    children?: ReactNode
}

export const WcagProvider: FC<WcagProps> = (props) => {
    const {children} = props
    const [fontSize, setFontSize] = useState<FontClass>("normal-style")

    return (
        <WcagContext.Provider value={{fontSize: fontSize, setFontSize: setFontSize}}>{
            children}
        </WcagContext.Provider>
    )
}
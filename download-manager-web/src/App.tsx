import './App.css'
import {useEffect} from "react";
import {removeRefreshTokenIfExpired} from "./main.tsx";
import {Header} from "./header/Header.tsx";
import {HomeContent} from "./home/HomeContent.tsx";

function App() {

    useEffect(() => {
        removeRefreshTokenIfExpired()
    }, []);

    return (
        <>
            <Header/>
            <HomeContent/>
        </>
    )

}

export default App

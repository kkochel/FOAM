import './App.css'
import {Header} from "./header/Header.tsx";
import {HomeContent} from "./home/HomeContent.tsx";
import {Footer} from "./common/Footer.tsx";
import {useContext, useEffect} from "react";
import {AuthContext} from "./auth/AuthProvider.tsx";
import {useNavigate} from "react-router-dom";

function App() {
    const {authenticated} = useContext(AuthContext)
    const navigate = useNavigate();

    useEffect(() => {
        if (authenticated) {
            navigate("/dashboard")
        }
    }, [authenticated]);

    return (
        <>
            <Header/>
            <HomeContent/>
            <Footer/>
        </>
    )

}

export default App

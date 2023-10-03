import {Header} from "../header/Header.tsx";
import {HomeContent} from "./HomeContent.tsx";

export const Home = () => {
    return (
        <>
            <Header isAuthenticated={false}/>
            <HomeContent/>
        </>
    )
}
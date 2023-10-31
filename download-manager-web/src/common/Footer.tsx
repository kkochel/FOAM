import {FC} from "react";
import footer from "../assets/horizontal_footer_pl.png"


export const Footer:FC = () => {
    return(
        <footer style={{zIndex:"-100", width:"auto", height:"90px"}}>
            <img src={footer} alt="Fotter"/>
        </footer>
    )
}
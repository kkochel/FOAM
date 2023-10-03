import './App.css'
import {Container} from "react-bootstrap";
import {Home} from "./home/Home.tsx";

function App() {

    return (
        <Container style={{"background": "aqua"}}>
            <Home/>
        </Container>
    )
}

export default App

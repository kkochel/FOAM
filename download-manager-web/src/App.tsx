import './App.css'
import {Container} from "react-bootstrap";
import {Home} from "./home/Home.tsx";

function App() {

    return (
        <Container fluid className={"h-100"}>
            <Home/>
        </Container>
    )
}

export default App

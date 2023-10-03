import {Header} from "../header/Header.tsx";
import {DashboardView} from "./DashboardView.tsx";

export const Dashboard = () => {
  return(
      <>
          <Header isAuthenticated={true}/>
          <DashboardView/>
      </>
  )
}
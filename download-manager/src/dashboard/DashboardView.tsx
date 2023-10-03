import {useQuery} from "@tanstack/react-query";
import {fetchData} from "../common/network.ts";
import {AppSettings} from "../api/AppSettings.ts";
import {ListGroup} from "react-bootstrap";

export const DashboardView = () => {



    const { data} = useQuery<string[]>({
        queryKey: ["users", AppSettings.API_ENDPOINT],
        queryFn: () => fetchData<string[]>(AppSettings.API_ENDPOINT),
    });

  return(
      <>
          {data ?
         <ListGroup>
             {data.map((value, index)=>
                <ListGroup.Item key={index}>{value}</ListGroup.Item>
             )}
         </ListGroup>

              :
              <div>There is no data to display</div>
          }
      </>
  )
}
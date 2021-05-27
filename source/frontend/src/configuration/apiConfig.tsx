import { Configuration } from "../rest";
import Config from "./config";

const ApiConfig = () => (
    new Configuration({
        basePath: Config.apiHost,
        baseOptions: {
            withCredentials: true
        }
    })
)

export default ApiConfig;
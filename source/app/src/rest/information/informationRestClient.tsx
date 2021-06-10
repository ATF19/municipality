import axios from "axios";
import Config from "../../config/config";

const getInformation = () => {
    const url = `${Config.server}information`
    return axios.get(url)
}

export { getInformation }
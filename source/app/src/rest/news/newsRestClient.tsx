import axios from "axios";
import Config from "../../config/config";

const getNews = (pageNumber: Number) => {
    const url = `${Config.server}articles?page=${pageNumber}`
    return axios.get(url)
}

const getArticleById = (id: string) => {
    const url = `${Config.server}articles/${id}`
    return axios.get(url)
}

export { getNews, getArticleById }
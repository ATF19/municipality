import axios from "axios";
import Config from "../../config/config";

const createComplaint = (request: CreateComplaintRequest) => {
    const url = `${Config.server}complaint`
    const content = {
        photo: request.photo,
        address: request.address,
        comment: request.comment,
        personalInfo: {
            firstName: request.firstName,
            lastName: request.lastName,
            email: request.email,
            phone: request.phone,
        },
        position: {
            longitude: request.longitude,
            latitude: request.latitude,
        }
    };
    return axios.post(url, content)
}

interface CreateComplaintRequest {
    photo: string,
    address: string,
    comment: string,
    firstName?: string,
    lastName?: string,
    email?: string,
    phone?: string,
    latitude?: number,
    longitude?: number
}

export {createComplaint, CreateComplaintRequest}
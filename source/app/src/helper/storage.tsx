import AsyncStorage from '@react-native-async-storage/async-storage';

const COMPLAINTS_KEY = "__complaints__"

const addComplaintId = async (complaint: any): Promise<any> => {
    const complaints = await getComplaintIds()
    complaints.push(complaint.id)
    await AsyncStorage.setItem(COMPLAINTS_KEY, JSON.stringify(complaints))
    return complaint
}

const getComplaintIds = async (): Promise<string[]> => {
    const complaintsSerialized = await AsyncStorage.getItem(COMPLAINTS_KEY)
    return complaintsSerialized ? JSON.parse(complaintsSerialized) : []
}

export { addComplaintId, getComplaintIds }
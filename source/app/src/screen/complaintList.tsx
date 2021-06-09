import React, { useCallback, useEffect, useState } from 'react';
import { Button, List, Spinner } from '@ui-kitten/components';
import { View } from 'react-native';
import Styles from '../style';
import Loading from '../component/loading';
import { complaintsByIds } from '../rest/complaint/complaintRestClient';
import { getComplaintIds } from '../helper/storage';
import NoData from '../component/noData';
import ComplaintCard from '../component/complaintCard';

const ComplaintList = ({navigation}: any) => {
    const [loading, toggleLoading] = useState(true)
    const [ids, setIds] = useState<string[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [haveNextPage, toggleHaveNextPage] = useState(false)
    const [nextPageLoading, toggleNextPageLoading] = useState(false)
    const [complaints, setComplaints] = useState<any[]>([])

    const getComplaints = useCallback((idList: string[], pageNumber: number) => {
        toggleNextPageLoading(true)
        setIds(idList)
        complaintsByIds(idList, pageNumber)
        .then(response => {
            setComplaints(existingComplaints => [...existingComplaints, ...response.data.elements])
            setCurrentPage(pageNumber)
            toggleHaveNextPage(pageNumber < response.data.totalPages)
        })
        .catch(error => {})
        .finally(() => {
            toggleNextPageLoading(false)
            toggleLoading(false)
        })
    }, [complaintsByIds, setComplaints, setCurrentPage, toggleHaveNextPage, toggleNextPageLoading, toggleLoading, setIds])

    useEffect(() => { 
        let mounted = true
        getComplaintIds()
            .then((response) => {
                if (mounted) {
                    if (response && response.length > 0)
                        getComplaints(response, 1)    
                    else
                        toggleLoading(false)
                }
            })
            .catch(err => {})
        return function cleanup() {
            mounted = false
        }
     }, [getComplaints, setIds])

    const renderElement = (element: any) => (
        <ComplaintCard complaint={element.item} onPress={() => navigation.navigate('Complaint', {complaint: element.item})} />
    )

    const renderLoadMoreButton = () => {
        if (!haveNextPage)
            return null;

        if (nextPageLoading)
            return (
                <View style={[Styles.containerCenter]}>
                    <Spinner size="small" style={Styles.spinner} />
                </View>
            )

        return (
            <Button onPress={() => getComplaints(ids, currentPage + 1)}>المزيد</Button>
        )
    }

    if (loading)
        return <Loading />

    if (complaints.length < 1)
        return <NoData dataTitle="شكوى" />

    return(
        <List
            style={[Styles.darkerBackground]}
            contentContainerStyle={[Styles.screenWithPadding]}
            data={complaints}
            renderItem={renderElement}
            ListFooterComponent={renderLoadMoreButton}
            scrollEnabled={true}
        />
    )
} 


export default ComplaintList
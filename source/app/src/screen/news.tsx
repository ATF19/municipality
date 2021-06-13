import { Button, Card, List, Spinner, Text } from '@ui-kitten/components';
import React, { useState, useCallback, useEffect } from 'react'
import { View } from 'react-native';
import Loading from '../component/loading';
import NoData from '../component/noData';
import { getNews } from '../rest/news/newsRestClient';
import Styles from '../style';
import moment from 'moment';

const News = ({navigation}: any) => {
    const [loading, toggleLoading] = useState(true)
    const [currentPage, setCurrentPage] = useState(1)
    const [haveNextPage, toggleHaveNextPage] = useState(false)
    const [nextPageLoading, toggleNextPageLoading] = useState(false)
    const [articles, setArticles] = useState<any[]>([])

    const getArticles = useCallback((pageNumber: number) => {
        toggleNextPageLoading(true)
        getNews(pageNumber)
        .then(response => {
            setArticles(existingArticles => [...existingArticles, ...response.data.elements])
            setCurrentPage(pageNumber)
            toggleHaveNextPage(pageNumber < response.data.totalPages)
        })
        .catch(error => {})
        .finally(() => {
            toggleNextPageLoading(false)
            toggleLoading(false)
        })
    }, [setArticles, setCurrentPage, toggleHaveNextPage, toggleNextPageLoading, toggleLoading])

    useEffect(() => { getArticles(1) }, [getArticles])

    const renderElement = (element: any) => (
        <Card style={Styles.marginBottom} onPress={() => navigation.navigate('Article', {articleId: element.item.id})}>
            <Text category="s1" style={Styles.rightText}>{element.item.title}</Text>
            <Text category="p2" style={Styles.rightText}>{moment(element.item.createdAt).format("DD/MM/yyyy HH:MM")}</Text>
        </Card>
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
            <Button onPress={() => getArticles(currentPage + 1)}>المزيد</Button>
        )
    }

    if (loading)
        return <Loading />

    if (articles.length < 1)
        return <NoData dataTitle="جديد إلى حد هذه اللحظة" />

    return(
        <List
            style={[Styles.darkerBackground]}
            contentContainerStyle={[Styles.screenWithPadding]}
            data={articles}
            renderItem={renderElement}
            ListFooterComponent={renderLoadMoreButton}
            scrollEnabled={true}
        />
    )
}

export default News;
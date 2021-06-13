import React, { useState, useCallback, useEffect } from 'react';
import { Divider, Layout, Text, Card } from '@ui-kitten/components';
import { getArticleById } from '../rest/news/newsRestClient';
import Loading from '../component/loading';
import { ScrollView } from 'react-native-gesture-handler';
import Styles from '../style';
import { View } from 'react-native';
import moment from 'moment';

const Article = ({route}: any) => {
    const id = route.params.articleId
    const [loading, toggleLoading] = useState(true)
    const [article, setArticle] = useState<any>()

    const loadArticle = useCallback(() => {
        toggleLoading(true)
        getArticleById(id)
        .then(response => {
            setArticle(response.data)
        })
        .catch(error => {})
        .finally(() => {
            toggleLoading(false)
        })
    }, [setArticle, toggleLoading])

    useEffect(() => { 
        loadArticle()
     }, [loadArticle])

    if (loading)
     return <Loading />

    return(
        <ScrollView>
            <Layout style={[Styles.darkerBackground]}>
                    <View style={[Styles.container, Styles.screenWithPadding]}>
                        <Card>
                            <Text style={Styles.rightText} category="s1">{article.title}</Text>
                            <Divider style={Styles.infoDivider} />

                            <View style={Styles.articleSpaceSeparation} />

                            <Text style={Styles.rightText} category="p1">{article.content}</Text>
                            <View style={Styles.articleSpaceSeparation} />

                            <Divider style={Styles.infoDivider} />

                            <Text style={Styles.rightText} category="p2">{moment(article.createdAt).format("DD/MM/yyyy HH:MM")}</Text>

                        </Card>
                    </View>
            </Layout>
        </ScrollView>
    )
}

export default Article;
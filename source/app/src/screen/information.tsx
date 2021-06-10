import { Card, Layout, Text, Divider, Icon } from '@ui-kitten/components';
import React, { useState, useCallback, useEffect } from 'react';
import { View } from 'react-native';
import { ScrollView } from 'react-native-gesture-handler';
import Loading from '../component/loading';
import { getInformation } from '../rest/information/informationRestClient';
import Styles from '../style';

const Information = () => {
    const [loading, toggleLoading] = useState(true)
    const [intro, setIntro] = useState("")
    const [email, setEmail] = useState("")
    const [phone, setPhone] = useState("")

    const loadInformation = useCallback(() => {
        toggleLoading(true)
        getInformation()
        .then(response => {
            setIntro(response.data.intro)
            setEmail(response.data.email)
            setPhone(response.data.phone)
        })
        .catch(error => {})
        .finally(() => {
            toggleLoading(false)
        })
    }, [setIntro, setEmail, setPhone, toggleLoading])

    useEffect(() => { 
        loadInformation()
     }, [loadInformation])

     if (loading)
        return <Loading />

     return(
        <ScrollView>
            <Layout style={[Styles.darkerBackground]}>
                <View style={Styles.containerCenter}>
                    <Icon
                      style={Styles.infoIcon}
                      fill='#74b9ff'
                      name='info'
                    />
                  </View>

                    <View style={[Styles.container, Styles.screenWithPadding]}>
                        <Card>
                            <Text style={Styles.rightText} category="label">التعريف</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{intro}</Text>

                            <View style={Styles.infoSpaceSeparation} />
                            
                            <Text style={Styles.rightText} category="label">رقم الهاتف</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{phone}</Text>

                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">البريد الإلكتروني</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{email}</Text>

                        </Card>
                    </View>
            </Layout>
        </ScrollView>
    )
}

export default Information;
import React, { useState } from 'react';
import { Button, Card, Divider, Icon, Input, Layout, Text } from '@ui-kitten/components';
import { View } from 'react-native';
import Styles from '../style';
import { ScrollView } from 'react-native-gesture-handler';

const Complaint = ({route}: any) => {
    const complaint = route.params.complaint

    return(
        <ScrollView>
            <Layout style={[Styles.darkerBackground]}>
                    <View style={[Styles.container, Styles.screenWithPadding]}>
                        <Text>jisjidos</Text>
                    </View>
            </Layout>
        </ScrollView>
    )
} 


export default Complaint
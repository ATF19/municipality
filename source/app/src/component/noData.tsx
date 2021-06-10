import { Icon, Text } from '@ui-kitten/components';
import React from 'react';
import { View } from 'react-native';
import Styles from '../style';

const NoData = ({dataTitle}: NoDataProps) => (
    <View style={Styles.errorPageContainer}>
        <Icon
            style={Styles.noDataIcon}
            fill='#2d3436'
            name='archive'
        />
        <Text style={Styles.errorPageElementWithMarginTop} category="h5">
            لا يوجد {dataTitle} ...
        </Text>
    </View>
)

interface NoDataProps {
    dataTitle: string
}

export default NoData;
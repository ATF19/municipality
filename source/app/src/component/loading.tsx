import { Spinner, Text } from '@ui-kitten/components';
import React from 'react';
import { View } from 'react-native';
import Styles from '../style';

const Loading = ({message}: LoadingProps) => (
    <View style={Styles.errorPageContainer}>
        <Spinner size="giant" style={Styles.spinner} />
        {message && <Text style={Styles.errorPageElementWithMarginTop} category="h5">{message}</Text>}
        <Text style={Styles.errorPageElementWithMarginTop} category="h5">الرجاء الإنتظار...</Text>
    </View>
)

interface LoadingProps {
    message?: string
}

export default Loading;
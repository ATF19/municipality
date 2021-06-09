import { Card, Text } from '@ui-kitten/components';
import React from 'react'
import { ImageBackground, View } from 'react-native';
import { statusBackground } from '../helper/style';
import { translateStatus } from '../helper/translation';
import Styles from '../style';

const ComplaintCard = ({complaint, onPress}: ComplaintCardProps) => {
    const cardHeader = () => (
        <ImageBackground
            style={Styles.complaintCardImage}
            source={{uri: complaint.pictureUrl}}
        >
            <View style={Styles.flexRow}>
                <Text style={[Styles.complaintStatus, statusBackground(complaint.status)]}>{translateStatus(complaint.status)}</Text>
            </View>
        </ImageBackground>  
    )

    return (
        <Card onPress={onPress} header={cardHeader} style={Styles.complaintCardContainer}>
            <Text style={Styles.rightText}>{complaint.comment}</Text>
        </Card>
    )
}

interface ComplaintCardProps {
    complaint: any;
    onPress?: () => any
}

export default ComplaintCard;
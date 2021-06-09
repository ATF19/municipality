import React, { useState } from 'react';
import { Button, Card, Divider, Icon, Input, Layout, Text } from '@ui-kitten/components';
import { ImageBackground, View } from 'react-native';
import Styles from '../style';
import { ScrollView } from 'react-native-gesture-handler';
import { translateStatus } from '../helper/translation';

const Complaint = ({route}: any) => {
    const complaint = route.params.complaint

    return(
        <ScrollView>
            <Layout style={[Styles.darkerBackground]}>
                    <ImageBackground
                        style={Styles.complaintPageImage}
                        source={{uri: complaint.pictureUrl}}
                    />
                    <View style={[Styles.container, Styles.screenWithPadding]}>
                        <Card>
                            <Text style={Styles.rightText} category="label">الحالة</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{translateStatus(complaint.status)}</Text>

                            <View style={Styles.infoSpaceSeparation} />
                            
                            <Text style={Styles.rightText} category="label">العنوان</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.address}</Text>

                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">التعليق</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.comment}</Text>

                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">الإسم</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.personalInfo?.firstName?.firstName}</Text>


                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">اللقب</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.personalInfo?.lastName?.lastName}</Text>


                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">رقم الهاتف</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.personalInfo?.phone?.phoneNumber}</Text>

                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">البريد الإلكتروني</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.personalInfo?.email?.email}</Text>

                            <View style={Styles.infoSpaceSeparation} />

                            <Text style={Styles.rightText} category="label">تعليق الدائرة</Text>
                            <Divider style={Styles.infoDivider} />
                            <Text style={Styles.rightText} category="s1">{complaint.resultComment}</Text>

                        </Card>
                    </View>
            </Layout>
        </ScrollView>
    )
} 


export default Complaint
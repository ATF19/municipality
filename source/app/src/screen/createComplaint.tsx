import { Button, Card, Divider, Icon, Input, Layout, Text } from '@ui-kitten/components';
import React, { useState } from 'react';
import { View, Image } from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import * as ImagePicker from 'expo-image-picker';
import * as Location from 'expo-location';
import Styles from '../style';
import { MediaTypeOptions } from 'expo-image-picker';
import Toast from 'react-native-root-toast';
import { validateEmail, validatePhone } from '../helper/validation';
import Loading from '../component/loading';
import { createComplaint, CreateComplaintRequest } from '../rest/complaint/complaintRestClient';
import { ImageInfo } from 'expo-image-picker/build/ImagePicker.types';
import { addComplaintId, getComplaintIds } from '../helper/storage';

const CreateComplaint = ({navigation}: any) => {
    const [loading, toggleLoading] = useState(false)
    const [photo, setPhoto] = useState<ImageInfo>()
    const [address, setAddress] = useState("")
    const [comment, setComment] = useState("")
    const [email, setEmail] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")
    const [phone, setPhone] = useState("")

    let openImagePickerAsync = async () => {
        let permissionResult = await ImagePicker.requestCameraPermissionsAsync();
    
        if (permissionResult.granted === false) {
          alert("الرجاء السماح بأخذ الصور");
          return;
        }
    
        let pickerResult = await ImagePicker.launchCameraAsync({
            mediaTypes: MediaTypeOptions.Images,
            allowsEditing: true,
            quality: 0.4,
            base64: true
        });
        if (pickerResult.cancelled === true) {
            return;
        }  
          
        setPhoto(pickerResult);
    }

    const renderPhotoCard = () => {
        if (photo)
            return (
                <View>
                    <Image source={{uri: photo.uri}} style={Styles.photoPreview} />
                    <Button onPress={() => setPhoto(undefined)} style={Styles.removePhotoBtn}>إحذف</Button>
                </View>
            )
        
        return (
                <>
                    <Icon
                        style={Styles.addPhotoIcon}
                        fill='#ced6e0'
                        name='camera'
                    />
                    <Text style={Styles.addPhotoText} category="h6">إلتقط صورة</Text>
                </>
        )
    }

    const getPosition = async (): Promise<{longitude?: number, latitude?: number}> => {
        const { status } = await Location.requestForegroundPermissionsAsync()
        if (status === 'granted') {
            const location = await Location.getLastKnownPositionAsync({});
            if (location)
                return {longitude: location.coords.longitude, latitude: location.coords.latitude};
        }
        return {longitude: undefined, latitude: undefined}
    }

    const sendComplaint = () => {
        if (!photo || !photo.base64) {
            showError("الرجاء إضافة صورة !")
            return;
        }

        if (!address || address.length <= 0) {
            showError("الرجاء إضافة العنوان !")
            return;
        }

        if (!comment || comment.length <= 0) {
            showError("الرجاء إضافة تعليقك !")
            return;
        }

        if (email && !validateEmail(email)) {
            showError("الرجاء إدخال بريد إلكتروني صحيح !")
            return;
        }

        if (phone && !validatePhone(phone)) {
            showError("الرجاء إدخال رقمك المتكون من 8 أرقام !")
            return;
        }

        toggleLoading(true)
        getPosition().then(({longitude, latitude}) => {
            let request: CreateComplaintRequest = {photo: photo.base64, address, comment, 
                firstName, lastName, email, phone, latitude, longitude}
            createComplaint(request)
            .then((response) => addComplaintId(response.data))
            .then((complaint) => {
                showSuccess("لقد تم إرسال شكواك بنجاح.")
                setPhoto(undefined)
                setAddress("")
                setEmail("")
                setComment("")
                setFirstName("")
                setLastName("")
                setPhone("")
                navigation.navigate('Complaint', {complaint})
            })
            .catch((error) => {
                showError("لقد حدث عطب. الرجاء إعادة التجربة.")
                console.log(error)
            })
            .finally(() => toggleLoading(false))    
        })
    }

    const showSuccess = (message: string) => {
        Toast.show(message, {
            duration: Toast.durations.LONG,
            position: Toast.positions.BOTTOM,
            backgroundColor: '#00cec9',
            opacity: 1,
            hideOnPress: true,
            containerStyle: Styles.toastContainerStyle,
            textStyle: Styles.toastTextStyle
          });
    }

    const showError = (message: string) => {
        Toast.show(message, {
            duration: -1,
            position: Toast.positions.BOTTOM,
            backgroundColor: '#ff7675',
            opacity: 1,
            hideOnPress: true,
            containerStyle: Styles.toastContainerStyle,
            textStyle: Styles.toastTextStyle
          });
    }

    if (loading)
        return <Loading message="يتم إرسال شكواك." />

    return (
        <KeyboardAwareScrollView extraScrollHeight={60}>
            <Layout style={[Styles.darkerBackground]}>
                    <View style={[Styles.container, Styles.screenWithPadding]}>
                        <Card style={Styles.addPhotoBox} onPress={openImagePickerAsync}>
                            <View style={Styles.containerCenter}>
                            {renderPhotoCard()}
                            </View>
                        </Card>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>العنوان *</Text>}
                                placeholder='أضف العنوان...'
                                value={address}
                                onChangeText={(nextValue: string) => setAddress(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>التعليق *</Text>}
                                placeholder='أضف تعليقك...'
                                value={comment}
                                onChangeText={(nextValue: string) => setComment(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <View style={[Styles.formElement, Styles.formDividerContainer]}>
                            <Divider style={Styles.formDivider} />
                            <Text style={[Styles.formLabel, Styles.formDividerText]}>معلومات اختيارية</Text>
                        </View>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>الإسم (يمكنك عدم إضافته)</Text>}
                                placeholder='أضف الإسم...'
                                value={firstName}
                                onChangeText={(nextValue: string) => setFirstName(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>اللقب (يمكنك عدم إضافته)</Text>}
                                placeholder='أضف اللقب...'
                                value={lastName}
                                onChangeText={(nextValue: string) => setLastName(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>رقم الهاتف (يمكنك عدم إضافته)</Text>}
                                placeholder='أضف رقم الهاتف...'
                                value={phone}
                                onChangeText={(nextValue: string) => setPhone(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <View style={Styles.formElement}>
                            <Input
                                size="large"
                                label={evaProps => <Text style={Styles.formLabel}>البريد الإلكتروني (يمكنك عدم إضافته)</Text>}
                                placeholder='أضف البريد الإلكتروني...'
                                value={email}
                                onChangeText={(nextValue: string) => setEmail(nextValue)}
                                textAlign="right"
                            />
                        </View>

                        <Button size="giant" style={[Styles.mainButton, Styles.formElement]} onPress={sendComplaint}>أرسل</Button>
                    </View>
            </Layout>
        </KeyboardAwareScrollView>
    )
}

interface Position {
    latitude: number,
    longitude: number
}

export default CreateComplaint;
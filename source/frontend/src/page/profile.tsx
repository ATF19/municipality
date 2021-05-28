import { Alert, Button, Divider, Form, Input, Layout, message } from 'antd';
import React, { useContext, useState } from 'react';
import Context from '../component/context';
import ApiConfig from '../configuration/apiConfig';
import ErrorCode from '../error/errorCode';
import { validateEmail } from '../helper/validation';
import { UpdateProfileRequest, UserRestServiceApi } from '../rest';

const Profile = () => {
    const { user, setLoggedinUser} = useContext(Context)
    const [email, changeEmail] = useState(user?.email)
    const [firstName, changeFirstName] = useState(user?.firstName)
    const [lastName, changeLastName] = useState(user?.lastName)
    const [password, changePassword] = useState("")
    const [confirmPassword, changeConfirmPassword] = useState("")
    const [btnLoading, toggleLoading] = useState(false);
    const [hasError, toggleError] = useState(false);
    const [errorMsg, changeErrorMsg] = useState("");
    const userApi = new UserRestServiceApi(ApiConfig());

    const updateProfile = () => {
        toggleError(false)

        if(!email || email.length <= 0 || !validateEmail(email)) {
            toggleError(true)
            changeErrorMsg("Veuillez saisir une adresse mail valide.")
            return
        }

        if(!firstName || firstName.length <= 0) {
            toggleError(true)
            changeErrorMsg("Veuillez saisir votre prénom.")
            return
        }

        if(!lastName || lastName.length <= 0) {
            toggleError(true)
            changeErrorMsg("Veuillez saisir votre nom.")
            return
        }

        if (password && password.length > 0) {
            if (password !== confirmPassword) {
                toggleError(true)
                changeErrorMsg("Veuillez vérifier que le mot de passe et la confirmation correspondent.")
                return    
            }
        }

        const updateProfileRequest: UpdateProfileRequest = {email, firstName, lastName}
        if (password && password.length > 0)
            updateProfileRequest.password = password

        userApi.updateProfile(updateProfileRequest)
        .then(response => {
            if (user)
                setLoggedinUser({...user, firstName, lastName, email})
                message.success("Vos informations ont été mises à jour.")
        })
        .catch(error => {
            if (error?.response?.data) {
                if(error.response.data.code === ErrorCode.EMAIL_EXISTS)
                    changeErrorMsg("Cet e-mail existe déjà. Veuillez en choisir un autre.")
                else if(error.response.data.code === ErrorCode.INVALID_EMAIL)
                    changeErrorMsg("Veuillez saisir une adresse mail valide.")
                else if(error.response.data.code === ErrorCode.WEAK_PASSWORD)
                    changeErrorMsg("Veuillez choisir un mot de passe comportant au moins 8 lettres (majuscule et minuscule) et chiffres.")
                else if(error.response.data.code === ErrorCode.MISSING_INFO)
                    changeErrorMsg("Veuillez remplir tous les champs obligatoires.")
                else
                    changeErrorMsg("Une erreur inconnue s'est produite. Veuillez réessayer.")
            }
            else
                changeErrorMsg("Une erreur inconnue s'est produite. Veuillez réessayer.")
            toggleError(true)
        })
        .finally(() => toggleLoading(false))
    }

    return (
        <Layout.Content className="content">
            <h2>Utilisateur @ {user?.username}</h2>
            <Divider plain />
            <Form
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 18 }}
                layout="horizontal"
                size="middle"
                onFinish={updateProfile}
                labelAlign="left">

                <Form.Item label="Email" rules={[{ required: true, message: 'Veuillez choisir une adresse e-mail !', whitespace: true }]}>
                    <Input placeholder="Email..." value={email}
                    onChange={val => changeEmail(val.target.value)} />
                </Form.Item>

                <Form.Item label="Prénom" rules={[{ required: true, message: 'Veuillez entrer votre prénom !', whitespace: true }]}>
                    <Input placeholder="Prénom..." value={firstName}
                    onChange={val => changeFirstName(val.target.value)} />
                </Form.Item>

                <Form.Item label="Nom" rules={[{ required: true, message: 'Veuillez entrer votre nom !', whitespace: true }]}>
                    <Input placeholder="Nom..." value={lastName}
                    onChange={val => changeLastName(val.target.value)} />
                </Form.Item>

                <Form.Item label="Mot de passe" rules={[{ required: false }]}>
                    <Input.Password placeholder="Mot de passe..." value={password}
                    onChange={val => changePassword(val.target.value)} />
                </Form.Item>

                <Form.Item label="Confirmation du mot de passe" rules={[{ required: false }]}>
                    <Input.Password placeholder="Confirmation du mot de passe..." value={confirmPassword}
                    onChange={val => changeConfirmPassword(val.target.value)} />
                </Form.Item>

                {hasError ? (
                    <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                        <Alert
                            message={errorMsg}
                            type="error"
                            closable
                            afterClose={() => toggleError(false)}
                        />
                    </Form.Item>
                ) : null}

                <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                    <Button block type="primary" htmlType="submit" loading={btnLoading}>
                        Mettre à jour
                    </Button>
                </Form.Item>

            </Form>
        </Layout.Content>
    )
}

export default Profile;
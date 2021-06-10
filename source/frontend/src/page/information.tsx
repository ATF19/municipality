import { Button, Divider, Form, Input, Layout, message } from "antd";
import React, { useCallback, useContext, useEffect, useState } from "react"
import { Redirect } from "react-router-dom";
import Context from "../component/context";
import Loading from "../component/loading";
import ApiConfig from "../configuration/apiConfig";
import { InformationDto, InformationRestServiceApi } from "../rest";

const Information = () => {
    const { user } = useContext(Context)
    const [btnLoading, toggleBtnLoading] = useState(false);
    const [loading, toggleLoading] = useState(false);
    const [intro, setIntro] = useState("")
    const [phone, setPhone] = useState("")
    const [email, setEmail] = useState("")

    const loadInfo = useCallback(() => {
        toggleLoading(true)
        const infoApi = new InformationRestServiceApi(ApiConfig())
        infoApi.getInformation()
        .then(response => {
            setIntro(response.data.intro)
            setPhone(response.data.phone)
            setEmail(response.data.email)
        })
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleLoading(false))
        
    }, [toggleLoading, setIntro, setPhone, setEmail])

    useEffect(() => { loadInfo() }, [loadInfo])

    const updateInfo = () => {
        toggleBtnLoading(true)
        const infoApi = new InformationRestServiceApi(ApiConfig())
        const dto: InformationDto = {intro, email, phone} 
        infoApi.saveInformation(dto)
        .then(response => message.success("Les informations ont été mises à jour avec succès."))
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleBtnLoading(false))
    }

    if (!user || !user.isAdmin)
        return <Redirect to="/404" />

    if (loading)
        return <Loading />

    return (
        <Layout.Content className="content">
            <h2>Informations</h2>
            <Divider plain />
            <Form
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 18 }}
                layout="horizontal"
                size="middle"
                onFinish={updateInfo}
                labelAlign="left">

                <Form.Item label="Introduction">
                    <Input.TextArea placeholder="Introduction..." value={intro}
                    onChange={val => setIntro(val.target.value)} />
                </Form.Item>

                <Form.Item label="Numéro de téléphone">
                    <Input placeholder="Numéro de téléphone..." value={phone}
                    onChange={val => setPhone(val.target.value)} />
                </Form.Item>

                <Form.Item label="Email">
                    <Input placeholder="Email..." value={email}
                    onChange={val => setEmail(val.target.value)} />
                </Form.Item>

                <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                    <Button block type="primary" htmlType="submit" loading={btnLoading}>
                        Mettre à jour
                    </Button>
                </Form.Item>

            </Form>
        </Layout.Content>
    )
}

export default Information;
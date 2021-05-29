import { UserAddOutlined } from "@ant-design/icons";
import { Alert, Button, Divider, Form, Input, Layout, message, Modal, Switch, Table, TablePaginationConfig, Tag } from "antd";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { Redirect } from "react-router";
import Context from "../component/context";
import ApiConfig from "../configuration/apiConfig";
import ErrorCode from "../error/errorCode";
import { validateEmail } from "../helper/validation";
import { RegisterRequest, UserDto, UserRestServiceApi } from "../rest";

const Users = () => {
    const { user } = useContext(Context)
    const [users, setUsers] = useState<UserDto[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, toggleLoading] = useState(false)
    const [showAddModal, toggleAddModal] = useState(false)
    const [addUsername, changeAddUsername] = useState("")
    const [addEmail, changeAddEmail] = useState("")
    const [addFirstName, changeAddFirstName] = useState("")
    const [addLastName, changeAddLastName] = useState("")
    const [addPassword, changeAddPassword] = useState("")
    const [confirmPassword, changeConfirmPassword] = useState("")
    const [addAdmin, toggleAddAdmin] = useState(false)
    const [addBtnLoading, toggleAddBtnLoading] = useState(false)
    const [hasAddError, toggleAddError] = useState(false);
    const [addErrorMsg, changeAddErrorMsg] = useState("");

    const loadUsers = useCallback((pageNumber: number) => {
        toggleLoading(true)
        const userApi = new UserRestServiceApi(ApiConfig());
        userApi.all(pageNumber)
            .then(response => {
                setUsers(response.data.elements)
                setTotalElements(response.data.totalPages * response.data.size)
                setCurrentPage(response.data.number)
            })
            .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
            .finally(() => toggleLoading(false))
    }, [toggleLoading, setUsers, setTotalElements, setCurrentPage])

    const handleChange = (pagination: TablePaginationConfig) => {
        loadUsers(pagination.current || 1)
    }

    useEffect(() => { loadUsers(1) }, [loadUsers])

    const addUser = () => {
        toggleAddError(false)

        if(!addUsername || addUsername.length <= 0) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez saisir un nom d'utilisateur.")
            return
        }

        if(!addEmail || addEmail.length <= 0 || !validateEmail(addEmail)) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez saisir une adresse mail valide.")
            return
        }

        if(!addFirstName || addFirstName.length <= 0) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez saisir votre prénom.")
            return
        }

        if(!addLastName || addLastName.length <= 0) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez saisir votre nom.")
            return
        }

        if(!addPassword || addPassword.length <= 0) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez saisir un mot de passe.")
            return
        }

        if (addPassword !== confirmPassword) {
            toggleAddError(true)
            changeAddErrorMsg("Veuillez vérifier que le mot de passe et la confirmation correspondent.")
            return    
        }

        const createUserRequest: RegisterRequest = {
            username: addUsername, email: addEmail, password: addPassword, firstName: addFirstName, 
            lastName: addLastName, isAdmin: addAdmin
        }
        toggleAddBtnLoading(true)
        const userApi = new UserRestServiceApi(ApiConfig());
        userApi.registerInternal(createUserRequest)
        .then(() => {
            closeAddModal()
            loadUsers(currentPage || 1)
            message.success("L'utilisateur a été crée avec succès.")
        })
        .catch((error) => {
            if (error?.response?.data) {
                if(error.response.data.code === ErrorCode.USERNAME_EXISTS)
                    changeAddErrorMsg("Cet nom d'utilisateur existe déjà. Veuillez en choisir un autre.")
                else if(error.response.data.code === ErrorCode.EMAIL_EXISTS)
                    changeAddErrorMsg("Cet e-mail existe déjà. Veuillez en choisir un autre.")
                else if(error.response.data.code === ErrorCode.INVALID_EMAIL)
                    changeAddErrorMsg("Veuillez saisir une adresse mail valide.")
                else if(error.response.data.code === ErrorCode.WEAK_PASSWORD)
                    changeAddErrorMsg("Veuillez choisir un mot de passe comportant au moins 8 lettres (majuscule et minuscule) et chiffres.")
                else if(error.response.data.code === ErrorCode.MISSING_INFO)
                    changeAddErrorMsg("Veuillez remplir tous les champs obligatoires.")
                else
                    changeAddErrorMsg("Il y a eu une erreur lors de la création de l'utilisateur. Veuillez réessayer.")
            }
            else
                changeAddErrorMsg("Il y a eu une erreur lors de la création de l'utilisateur. Veuillez réessayer.")
            toggleAddError(true)
        })
        .finally(() => toggleAddBtnLoading(false))

    }

    const deleteUser = (userId: string) => {
        const userApi = new UserRestServiceApi(ApiConfig());
        toggleLoading(true)
        userApi._delete(userId)
            .then(() => message.success("L'utilisateur a été supprimé avec succès."))
            .catch(() => message.error("Il y a eu une erreur en supprimant l'utilisateur. Veuillez réessayer."))
            .finally(() => loadUsers(currentPage || 1))
    }

    const showConfirmDelete = (userId: string) => {
        Modal.confirm({
            title: 'Êtes-vous sûr de vouloir supprimer cet utilisateur ?',
            okText: 'Oui',
            cancelText: 'Non',
            onOk() {
                deleteUser(userId)
            }
        })
    }

    const closeAddModal = () => {
        changeAddUsername("")
        changeAddEmail("")
        changeAddFirstName("")
        changeAddLastName("")
        changeAddPassword("")
        changeConfirmPassword("")
        toggleAddAdmin(false)
        toggleAddError(false)
        toggleAddBtnLoading(false)
        toggleAddModal(false)
    } 

    const userColumns = [
        {
            title: "Nom d'utilisateur",
            dataIndex: 'username'
        },
        {
            title: "Prénom",
            dataIndex: 'firstName'
        },
        {
            title: "Nom",
            dataIndex: 'lastName'
        },
        {
            title: "Email",
            dataIndex: 'email'
        },
        {
            title: "Rôles",
            width: "20%",
            render: (text: string, record: UserDto) => renderRoles(record)
        },
        {
            title: "Paramètres",
            render: (text: string, record: UserDto) => (
                <Button danger onClick={() => showConfirmDelete(record.id)}>Supprimer</Button>
            )
        }
    ]

    if (!user?.isAdmin)
        return <Redirect to="/404" />

    return (
        <Layout.Content className="content">
            <div className="header-with-button-on-right">
                <h2>Utilisateurs</h2>
                <Button size='large' icon={<UserAddOutlined />} onClick={() => toggleAddModal(true)}>Ajouter</Button>
            </div>
            <Divider plain />
            <Table
                columns={userColumns}
                rowKey={(row: UserDto) => row.id}
                dataSource={users}
                pagination={{ current: currentPage, total: totalElements, pageSize: 10 }}
                loading={loading}
                locale={{ emptyText: "Pas de données." }}
                onChange={handleChange}
            />
            <Modal title="Créer un utilisateur" visible={showAddModal} width={1200}
            onCancel={closeAddModal} footer={null}>
                <Form
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 18 }}
                layout="horizontal"
                size="middle"
                onFinish={addUser}
                labelAlign="left">
                <Form.Item label="Nom d'utilisateur" rules={[{ required: true, message: "Veuillez choisir un nom d'utilisateur !", whitespace: true }]}>
                    <Input placeholder="Nom d'utilisateur..." value={addUsername}
                    onChange={val => changeAddUsername(val.target.value)} />
                </Form.Item>

                <Form.Item label="Email" rules={[{ required: true, message: 'Veuillez choisir une adresse e-mail !', whitespace: true }]}>
                    <Input placeholder="Email..." value={addEmail}
                    onChange={val => changeAddEmail(val.target.value)} />
                </Form.Item>

                <Form.Item label="Prénom" rules={[{ required: true, message: 'Veuillez entrer votre prénom !', whitespace: true }]}>
                    <Input placeholder="Prénom..." value={addFirstName}
                    onChange={val => changeAddFirstName(val.target.value)} />
                </Form.Item>

                <Form.Item label="Nom" rules={[{ required: true, message: 'Veuillez entrer votre nom !', whitespace: true }]}>
                    <Input placeholder="Nom..." value={addLastName}
                    onChange={val => changeAddLastName(val.target.value)} />
                </Form.Item>

                <Form.Item label="Mot de passe" rules={[{ required: true, message: 'Veuillez entrer un mot de passe !', whitespace: true }]}>
                    <Input.Password placeholder="Mot de passe..."  value={addPassword}
                    onChange={val => changeAddPassword(val.target.value)} />
                </Form.Item>

                <Form.Item label="Confirmation du mot de passe" rules={[{ required: true, message: 'Veuillez entrer la confirmation du mot de passe !', whitespace: true }]}>
                    <Input.Password placeholder="Confirmation du mot de passe..." value={confirmPassword}
                    onChange={val => changeConfirmPassword(val.target.value)} />
                </Form.Item>

                <Form.Item label="Admin">
                    <Switch checkedChildren="Oui" unCheckedChildren="Non" checked={addAdmin} onChange={checked => toggleAddAdmin(checked)} />
                </Form.Item>


                {hasAddError ? (
                    <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                        <Alert
                            message={addErrorMsg}
                            type="error"
                            closable
                            afterClose={() => toggleAddError(false)}
                        />
                    </Form.Item>
                ) : null}

                <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                    <Button block type="primary" htmlType="submit" loading={addBtnLoading}>
                        Créer
                    </Button>
                </Form.Item>
                </Form>
            </Modal>
        </Layout.Content>
    )
}


const renderRoles = (user: UserDto): any[] => {
    let tags: any[] = []
    user.isAdmin && tags.push((
        <Tag color="#2c3e50" key={`${user.id}-admin`}>
            Admin
        </Tag>
    ))
    user.municipalitiesResponsible.size > 0 && tags.push((
        <Tag color="#8e44ad" key={`${user.id}-mresponsable`}>
            Responsable de la municipalité
        </Tag>
    ))
    user.municipalitiesAuditor.size > 0 && tags.push((
        <Tag color="#16a085" key={`${user.id}-mauditor`}>
            Auditeur de la municipalité
        </Tag>
    ))
    user.districtsResponsible.size > 0 && tags.push((
        <Tag color="#2980b9" key={`${user.id}-dresponsable`}>
            Responsable du district
        </Tag>
    ))
    user.districtsAuditor.size > 0 && tags.push((
        <Tag color="#d35400" key={`${user.id}-dauditor`}>
            Auditeur du district
        </Tag>
    ))
    return tags;
}

export default Users;

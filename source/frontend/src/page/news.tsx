import { FileAddOutlined } from "@ant-design/icons";
import { Button, Divider, Form, Input, Layout, message, Modal, Table, TablePaginationConfig } from "antd";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { Redirect } from "react-router";
import Context from "../component/context";
import ApiConfig from "../configuration/apiConfig";
import { ArticleRestServiceApi, ArticleWithoutContentDto, CreateOrUpdateArticleRequest } from "../rest";
import moment from 'moment'

const News = () => {
    const { user } = useContext(Context)
    const [articles, setArticles] = useState<ArticleWithoutContentDto[]>()
    const [currentPage, setCurrentPage] = useState(1)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, toggleLoading] = useState(false)
    const [showModal, toggleModal] = useState(false)
    const [modalId, changeModalId] = useState("")
    const [modalTitle, changeModalTitle] = useState("")
    const [modalContent, changeModalContent] = useState("")
    const [modalBtnLoading, toggleModalBtnLoading] = useState(false)

    const loadArticles = useCallback((pageNumber: number) => {
        toggleLoading(true)
        const articleApi = new ArticleRestServiceApi(ApiConfig());
        articleApi.allArticles(pageNumber)
            .then(response => {
                setArticles(response.data.elements)
                setTotalElements(response.data.totalPages * response.data.size)
                setCurrentPage(response.data.number)
            })
            .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
            .finally(() => toggleLoading(false))
    }, [toggleLoading, setArticles, setTotalElements, setCurrentPage])

    const handleChange = (pagination: TablePaginationConfig) => {
        loadArticles(pagination.current || 1)
    }

    useEffect(() => { loadArticles(1) }, [loadArticles])

    const addOrUpdateArticle= () => {
        const request: CreateOrUpdateArticleRequest = {
            title: modalTitle,
            content: modalContent
        }
        const articleApi = new ArticleRestServiceApi(ApiConfig());
        toggleModalBtnLoading(true)
        if (!modalId || modalId === "") {
            articleApi.createArticle(request)
            .then(() => {
                closeModal()
                loadArticles(currentPage || 1)
                message.success("L'article a été crée avec succès.")
            })
            .catch(() => {
                message.error("Il y a eu une erreur lors de la création de l'article. Veuillez réessayer.")
            })
            .finally(() => toggleModalBtnLoading(false))
        }
        else {
            articleApi.updateArticle(modalId, request)
            .then(() => {
                closeModal()
                loadArticles(currentPage || 1)
                message.success("L'article a été mis à jour avec succès.")
            })
            .catch(() => {
                message.error("Il y a eu une erreur lors de la mise à jour de l'article. Veuillez réessayer.")
            })
            .finally(() => toggleModalBtnLoading(false))
        }
    }

    const deleteArticle = (id: string) => {
        const articleApi = new ArticleRestServiceApi(ApiConfig());
        toggleLoading(true)
        articleApi.deleteArticle(id)
            .then(() => message.success("L'article a été supprimé avec succès."))
            .catch(() => message.error("Il y a eu une erreur en supprimant l'article. Veuillez réessayer."))
            .finally(() => loadArticles(currentPage || 1))
    }

    const showConfirmDelete = (id: string) => {
        Modal.confirm({
            title: 'Êtes-vous sûr de vouloir supprimer cet article ?',
            okText: 'Oui',
            cancelText: 'Non',
            onOk() {
                deleteArticle(id)
            }
        })
    }

    const showUpdateModal = (id: string) => {
        const articleApi = new ArticleRestServiceApi(ApiConfig());
        toggleLoading(true)
        articleApi.getArticle(id)
            .then((response) => {
                changeModalId(id)
                changeModalTitle(response.data.title)
                changeModalContent(response.data.content)
                toggleModal(true)
            })
            .catch(() => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
            .finally(() => toggleLoading(false))
    }

    const closeModal = () => {
        changeModalId("")
        changeModalTitle("")
        changeModalContent("")
        toggleModalBtnLoading(false)
        toggleModal(false)
    } 

    const articleColumns = [
        {
            title: "Titre",
            dataIndex: 'title',
            width: "50%"
        },
        {
            title: "Crée par",
            dataIndex: 'createdBy'
        },
        {
            title: "Date de creation",
            render: (text: string, record: ArticleWithoutContentDto) => (
                <p>{moment(record.createdAt).format("DD/MM/yyyy hh:mm")}</p>
            )
        },
        {
            title: "Actions",
            render: (text: string, record: ArticleWithoutContentDto) => (
                <>
                    <Button className="margin-right" onClick={() => showUpdateModal(record.id)}>Modifier</Button>
                    <Button danger onClick={() => showConfirmDelete(record.id)}>Supprimer</Button>
                </>
            )
        }
    ]


    if (!user?.isAdmin)
        return <Redirect to="/404" />

        return (
            <Layout.Content className="content">
                <div className="header-with-button-on-right">
                    <h2>Articles</h2>
                    <Button size='large' icon={<FileAddOutlined />} onClick={() => toggleModal(true)}>Ajouter</Button>
                </div>
                <Divider plain />
                <Table
                    columns={articleColumns}
                    rowKey={(row: ArticleWithoutContentDto) => row.id}
                    dataSource={articles}
                    pagination={{ current: currentPage, total: totalElements, pageSize: 10 }}
                    loading={loading}
                    locale={{ emptyText: "Pas de données." }}
                    onChange={handleChange}
                />
                <Modal title="Créer un article" visible={showModal} width={1200}
                onCancel={closeModal} footer={null}>
                    <Form
                    labelCol={{ span: 5 }}
                    wrapperCol={{ span: 18 }}
                    layout="horizontal"
                    size="middle"
                    onFinish={addOrUpdateArticle}
                    labelAlign="left">
                    <Form.Item label="Titre" rules={[{ required: true, message: "Veuillez choisir un titre !", whitespace: true }]}>
                        <Input placeholder="Titre..." value={modalTitle}
                        onChange={val => changeModalTitle(val.target.value)} />
                    </Form.Item>
    
                    <Form.Item label="Contenu" rules={[{ required: true, message: "Veuillez choisir le contenu de l'article !", whitespace: true }]}>
                        <Input.TextArea rows={3} placeholder="Contenu..." value={modalContent}
                        onChange={val => changeModalContent(val.target.value)} />
                    </Form.Item>
        
                    <Form.Item wrapperCol={{ offset: 5, span: 18 }}>
                        <Button block type="primary" htmlType="submit" loading={modalBtnLoading}>
                            Sauvegarder
                        </Button>
                    </Form.Item>
                    </Form>
                </Modal>
            </Layout.Content>
        )
}


export default News;
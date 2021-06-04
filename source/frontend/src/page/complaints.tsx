import { Button, Descriptions, Divider, Layout, message, Modal, Table, TablePaginationConfig, Image, Tag, Radio } from "antd";
import { ColumnsType } from "antd/lib/table";
import React, { Fragment, useCallback, useContext, useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import Context from "../component/context";
import Loading from "../component/loading";
import ApiConfig from "../configuration/apiConfig";
import { canSeeComplaints } from "../helper/roles";
import { ComplaintDto, ComplaintRestServiceApi, StatusEnum, UpdateComplaintRequest } from "../rest";

const Complaints = () => {
    const { user } = useContext(Context)
    const [showRejected, toggleShowRejected] = useState(false)
    const [complaints, setComplaints] = useState<ComplaintDto[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, toggleLoading] = useState(false)
    const [isComplaintModalVisible, toggleComplaintModal] = useState(false)
    const [activeComplaint, setActiveComplaint] = useState<ComplaintDto>()
    const [complaintLoading, toggleComplaintLoading] = useState(false)

    const getComplaints = (showRejected: boolean, pageNumber: number) => {
        const complaintApi = new ComplaintRestServiceApi(ApiConfig())
        if (showRejected)
            return complaintApi.rejectedComplaints(pageNumber)

        return complaintApi.allComplaints(pageNumber)
    }

    const loadComplaints = useCallback((pageNumber: number) => {
        toggleLoading(true)
        getComplaints(showRejected, pageNumber).then(response => {
            setComplaints(response.data.elements)
            setTotalElements(response.data.totalPages * response.data.size)
            setCurrentPage(response.data.number)
        })
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleLoading(false))
        
    }, [toggleLoading, setComplaints, setCurrentPage, setTotalElements, showRejected])

    const handleChange = (pagination: TablePaginationConfig) => {
        loadComplaints(pagination.current || 1)
    }

    useEffect(() => { loadComplaints(1) }, [loadComplaints])

    const openComplaintModal = (record: ComplaintDto) => {
        setActiveComplaint(record)
        toggleComplaintModal(true)
    }

    const closeComplaintModal = () => {
        setActiveComplaint(undefined)
        toggleComplaintModal(false)
    }

    const complaintColumns: ColumnsType<ComplaintDto> = [
        {
            title: "Code",
            width: 30,
            dataIndex: 'code'
        },
        {
            title: "Status",
            width: 30,
            render: (text: string, record: ComplaintDto) => renderStatus(record.status)
        },
        {
            title: "Adresse",
            width: 100,
            dataIndex: 'address'
        },
        {
            title: "Commentaire",
            width: 100,
            dataIndex: 'comment'
        },
        {
            title: "Position",
            width: 40,
            render: (text: string, record: ComplaintDto) => (
                <Button href={getPositionQuery(record)} target="_blank">Voir dans la carte</Button>
            )
        },
        {
            title: "Détails",
            width: 40,
            fixed: 'right',
            render: (text: string, record: ComplaintDto) => (
                <Button type="primary" onClick={() => openComplaintModal(record)} target="_blank">Voir détails</Button>
            )
        }
    ]

    const changeShowActive = (value: boolean) => {
        toggleShowRejected(value)
        loadComplaints(1)
    }

    const updateComplaintStatus = (activeComplaint: ComplaintDto, status: StatusEnum) => {
        toggleComplaintLoading(true)
        const complaintApi = new ComplaintRestServiceApi(ApiConfig())
        const updateRequest: UpdateComplaintRequest = {status}
        complaintApi.updateComplaint(activeComplaint.id, updateRequest)
        .then(response => {
            let updatedComplaint = activeComplaint
            updatedComplaint.status = status
            setActiveComplaint(updatedComplaint)
            message.success("Mise à jour effectuée avec succès.")
        })
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleComplaintLoading(false))
    }

    if (!user || !canSeeComplaints(user))
        return <Redirect to="/404" />

    if (complaintLoading)
        return <Loading />

    return(
        <Layout.Content className="content">
            <div className="header-with-button-on-right">
                <h2>Plaintes</h2>
                <Radio.Group value={showRejected} onChange={(e) => changeShowActive(e.target.value)}>
                    <Radio.Button value={false} checked={!showRejected}>Active</Radio.Button>
                    <Radio.Button value={true} checked={showRejected}>Rejetée</Radio.Button>
                </Radio.Group>
            </div>
            <Divider plain />
            <Table
                columns={complaintColumns}
                rowKey={(row: ComplaintDto) => row.id}
                dataSource={complaints}
                pagination={{ current: currentPage, total: totalElements, pageSize: 10 }}
                loading={loading}
                locale={{ emptyText: "Pas de données." }}
                onChange={handleChange}
                scroll={{ x: 1500 }}
            />
            <Modal title={`Plainte "${activeComplaint?.code}"`} 
                visible={isComplaintModalVisible}
                onCancel={closeComplaintModal}
                footer={null}
                width={1000}
            >
                {activeComplaint && (
                    <Descriptions bordered>
                        <Descriptions.Item label="Code" span={3}>{activeComplaint.code} {renderStatus(activeComplaint?.status)}</Descriptions.Item>
                        <Descriptions.Item label="Adresse" span={3}>{activeComplaint.address}</Descriptions.Item>
                        <Descriptions.Item label="Commentaire" span={3}>{activeComplaint.comment}</Descriptions.Item>
                        <Descriptions.Item label="Photo" span={3}>
                            <Image width={300} src={activeComplaint.pictureUrl} alt="" />
                        </Descriptions.Item>
                        {activeComplaint.personalInfo && (
                            <Fragment>
                                <Descriptions.Item label="Prénom" span={2}>{activeComplaint.personalInfo.firstName?.firstName}</Descriptions.Item>
                                <Descriptions.Item label="Nom" span={2}>{activeComplaint.personalInfo.lastName?.lastName}</Descriptions.Item>
                                <Descriptions.Item label="Numéro" span={2}>{activeComplaint.personalInfo.phone?.phoneNumber}</Descriptions.Item>
                                <Descriptions.Item label="Email" span={2}>{activeComplaint.personalInfo.email?.email}</Descriptions.Item>
                            </Fragment>
                        )}
                        <Descriptions.Item label="Carte" span={3}>
                            <Button href={getPositionQuery(activeComplaint)} target="_blank">Voir dans la carte</Button>
                        </Descriptions.Item>
                        <Descriptions.Item label="Modifier le statut" span={3}>
                            <Button onClick={() => updateComplaintStatus(activeComplaint, StatusEnum.InProgress)} style={{borderColor: 'blue', color: 'blue'}}>En cours</Button>
                            <Divider type="vertical" />
                            <Button onClick={() => updateComplaintStatus(activeComplaint, StatusEnum.SentToExternalService)} style={{borderColor: 'orange', color: 'orange'}}>Envoyée à un service externe</Button>
                            <Divider type="vertical" />
                            <Button onClick={() => updateComplaintStatus(activeComplaint, StatusEnum.Finished)} style={{borderColor: 'green', color: 'green'}}>Terminée</Button>
                            <Divider type="vertical" />
                            <Button onClick={() => updateComplaintStatus(activeComplaint, StatusEnum.Rejected)} danger>Rejetée</Button>
                        </Descriptions.Item>  
                    </Descriptions>
                )}
            </Modal>
        </Layout.Content>
    )
}

const MAP_QUERY_URL = "https://www.google.com/maps/search/?api=1&query="

function getPositionQuery(record: ComplaintDto): string {
    if (record.position && record.position.longitude && record.position.latitude)
        return `${MAP_QUERY_URL}${encodeURIComponent(record.position.latitude + "," + record.position.longitude)}`
    else
        return `${MAP_QUERY_URL}${encodeURIComponent(record.address + " Tunis Tunisia")}`
}


export default Complaints;

function renderStatus(status: string | undefined): React.ReactNode {
    if (!status)
        return "";
    
    if (status === StatusEnum.Created)
        return <Tag color="orange">CRÉÉE</Tag>

    if (status === StatusEnum.InProgress)
        return <Tag color="gold">EN COURS</Tag>

    if (status === StatusEnum.SentToExternalService)
        return <Tag color="magenta">SERVICE EXTERNE</Tag>

    if (status === StatusEnum.Finished)
        return <Tag color="green">TERMINÉE</Tag>

    if (status === StatusEnum.Rejected)
        return <Tag color="red">REJETÉE</Tag>

    return "";
}


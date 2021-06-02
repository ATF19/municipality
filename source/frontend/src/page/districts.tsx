import { Divider, Layout, message, Table, TablePaginationConfig } from "antd"
import React, { useCallback, useContext, useEffect, useState } from "react"
import { Redirect } from "react-router-dom"
import Context from "../component/context"
import ApiConfig from "../configuration/apiConfig"
import { canSeeDistricts } from "../helper/roles"
import { DistrictDto, DistrictRestServiceApi } from "../rest"

const Districts = () => {
    const { user } = useContext(Context)
    const [districs, setDistricts] = useState<DistrictDto[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, toggleLoading] = useState(false)

    const loadDistricts = useCallback((pageNumber: number) => {
        toggleLoading(true)
        const districtApi = new DistrictRestServiceApi(ApiConfig())
        districtApi.allDistricts(pageNumber)
        .then(response => {
            setDistricts(response.data.elements)
            setTotalElements(response.data.totalPages * response.data.size)
            setCurrentPage(response.data.number)
        })
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleLoading(false))
        
    }, [toggleLoading, setDistricts, setCurrentPage, setTotalElements])

    const handleChange = (pagination: TablePaginationConfig) => {
        loadDistricts(pagination.current || 1)
    }

    useEffect(() => { loadDistricts(1) }, [loadDistricts])

    if (!user || !canSeeDistricts(user))
        return <Redirect to="/404" />

    return(
        <Layout.Content className="content">
            <h2>Arrondissements</h2>
            <Divider plain />
            <Table
                columns={districtColumns}
                rowKey={(row: DistrictDto) => row.id}
                dataSource={districs}
                pagination={{ current: currentPage, total: totalElements, pageSize: 10 }}
                loading={loading}
                locale={{ emptyText: "Pas de données." }}
                onChange={handleChange}
            />
        </Layout.Content>
    )
}

const districtColumns = [
    {
        title: "Nom",
        dataIndex: 'name',
        width: "50%"
    },
    {
        title: "Nom en arabe",
        dataIndex: 'nameInArabic',
        width: "50%"
    }
]
export default Districts;
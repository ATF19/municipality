import { Divider, Layout, message, Table, TablePaginationConfig } from "antd";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import Context from "../component/context";
import ApiConfig from "../configuration/apiConfig";
import { canSeeMunicipalities } from "../helper/roles";
import { MunicipalityDto, MunicipalityRestServiceApi } from "../rest";

const Municipalities = () => {
    const { user } = useContext(Context)
    const [municipalities, setMunicipalities] = useState<MunicipalityDto[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, toggleLoading] = useState(false)

    const loadMunicipalities = useCallback((pageNumber: number) => {
        toggleLoading(true)
        const municipalityApi = new MunicipalityRestServiceApi(ApiConfig())
        municipalityApi.all1(pageNumber)
        .then(response => {
            setMunicipalities(response.data.elements)
            setTotalElements(response.data.totalPages * response.data.size)
            setCurrentPage(response.data.number)
        })
        .catch(err => message.error("Une erreur inconnue s'est produite. Veuillez réessayer."))
        .finally(() => toggleLoading(false))
        
    }, [toggleLoading, setMunicipalities, setCurrentPage, setTotalElements])

    const handleChange = (pagination: TablePaginationConfig) => {
        loadMunicipalities(pagination.current || 1)
    }

    useEffect(() => { loadMunicipalities(1) }, [loadMunicipalities])

    if (!user || !canSeeMunicipalities(user))
        return <Redirect to="/404" />

    return(
        <Layout.Content className="content">
            <h2>Municipalités</h2>
            <Divider plain />
            <Table
                columns={municiaplityColumns}
                rowKey={(row: MunicipalityDto) => row.id}
                dataSource={municipalities}
                pagination={{ current: currentPage, total: totalElements, pageSize: 10 }}
                loading={loading}
                locale={{ emptyText: "Pas de données." }}
                onChange={handleChange}
            />
        </Layout.Content>
    )
}

const municiaplityColumns = [
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

export default Municipalities;
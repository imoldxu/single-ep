import { useState } from "react"
import { queryPrescription } from '@/services/ant-design-pro/prescription'

export default function usePrescriptionModel(){

    const [prescriptionList, setPrescriptionList] = useState()

    async function queryPrescriptions(payload){
        const resp = await queryPrescription(payload)
        setPrescriptionList({...resp, current: payload.current, pageSize: payload.pageSize})
        return resp
    }

    return {
        prescriptionList,
        setPrescriptionList,
        queryPrescriptions,
    }
}
import { useState } from "react"
import { queryPrescription } from '@/services/ant-design-pro/prescription'

export default function usePrescriptionModel(){

    const [prescriptionList, setPrescriptionList] = useState([])

    async function queryPrescriptions(payload){
        const resp = await queryPrescription(payload)
        setPrescriptionList(resp.data)
        return resp
    }

    return {
        prescriptionList,
        setPrescriptionList,
        queryPrescriptions,
    }
}
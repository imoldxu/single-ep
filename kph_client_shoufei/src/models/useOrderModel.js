import { useState } from "react"
import { queryOrder } from '@/services/ant-design-pro/order'

export default function useOrderModel(){

    const [orderList, setOrderList] = useState([])

    async function queryOrders(payload){
        const resp = await queryOrder(payload)
        setOrderList(resp.data)
        return resp
    }

    return {
        orderList,
        setOrderList,
        queryOrders,
    }
}
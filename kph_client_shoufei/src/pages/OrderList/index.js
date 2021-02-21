import { message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryOrder, deliver, refundDrug } from '@/services/ant-design-pro/order';
import { querySaleRecord } from '@/services/ant-design-pro/saleRecord';
import RefundModal from './refundModal'

export default ()=>{

    const [refundModalVisible, setRefundModalVisible] = useState(false)
    const [modalValue, setRefundModalValue] = useState()

  const actionRef = useRef();

  async function commitRefund(values){
      try{
        await refundDrug(values)
        setRefundModalValue(null)
        setRefundModalVisible(false)
      }catch(e){
        message.error(e.message, 3)
      }finally{

      }
  }

    const handleRefund = async (order) =>{
        const hide = message.loading('')
        try{
            const pageResult = await querySaleRecord({prescriptionno: order.prescriptionno, current:1, pageSize: 100})
            setRefundModalValue({order:order, records:pageResult.data})
            setRefundModalVisible(true)
        }catch(e){
            message.error(e.message, 3)
        }finally{
            hide()
        }
    }

  const gotoPrint = async (pid) => {
    if (!history) return;
    setTimeout(() => {
      history.push('/print?id='+pid);
    }, 10);
  };

  const handleDeliver = async (orderno, pid) => {
    const hide = message.loading('确认领药中')
    try{
        await deliver({orderno:orderno})
        gotoPrint(pid)
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
    actionRef.current.reload();
  };

  const columns = [
    {
      title: '处方号',
      dataIndex: 'prescriptionno',
    },
    {
      title: '登记号',
      dataIndex: 'regNo',
    },
    {
      title: '患者姓名',
      dataIndex: 'patientname',
    },
    {
      title: '医生姓名',
      dataIndex: 'doctorname',
    },
    {
      title: '部门',
      dataIndex: 'department',
      search: false,
    },
    {
      title: '订单金额',
      dataIndex: 'amount',
      search: false,
      render: (_,record)=> (<span>{record.amount/100}元</span>)
    },
    {
        title: '支付方式',
        dataIndex: 'payway',
        valueEnum: {
            1: {
                text: '微信',
            },
            2: {
                text: '支付宝',
            }, 
            3: {
                text: '市医保',
            },
            4: {
                text: '省医保',
            },
            5: {
                text: '现金',
            }
        }
    },
    {
        title: '状态',
        dataIndex: 'state',
        valueEnum: {
            1: {
                text: '待支付',
                status: 'Processing',
            },
            2: {
                text: '已支付',
                status: 'Warning',
            }, 
            3: {
                text: '已领药',
                status: 'Success',
            },
            4: {
                text: '待退款',
                status: 'Processing',
            },
            5: {
                text: '已退款',
                status: 'Error',
            }
        }
    },
    {
      title: '开具时间',
      dataIndex: 'createTime',
      search: false,
    },
    {
      title:'时间范围',
      dataIndex:'dateRange',
      hideInTable:true,
      valueType:"dateRange"
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const {state} = record
        if (state === 2){
            return (
                <Space>
                  <a
                    onClick={() => {
                        handleDeliver(record.orderno, record.prescriptionid);
                    }}
                  >
                    确认领药
                  </a>
                </Space>
             ) 
            }else if(state === 3){
                return (
                <Space>
                  <a
                    onClick={() => {
                        handleRefund(record);
                    }}
                  >
                    退药
                  </a>
                </Space>
                )
            }
        }
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="交易列表"
        actionRef={actionRef}
        rowKey="key"
        request={(params) => {
            if(params.dateRange){
              params.startTime = params.dateRange[0] + "00:00:00";
              params.endTime = params.dateRange[1] + "23:59:59";
            }
            return queryOrder(params)
          }
        }
        columns={columns}
        manualRequest={true}
      />
      {
          modalValue && (
            <RefundModal 
            handleCommit={commitRefund}
            handleCancel={()=>setRefundModalVisible(false)}
            visible={refundModalVisible}
            values={modalValue}>
            </RefundModal>
          )
      }
      
    </PageContainer>
  );

}
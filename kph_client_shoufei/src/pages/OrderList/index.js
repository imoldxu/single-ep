import { message, Popconfirm, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryOrder, deliver, refundDrug } from '@/services/ant-design-pro/order';
import { querySaleRecord } from '@/services/ant-design-pro/saleRecord';
import RefundModal from './refundModal'
import { regFenToYuan } from "@/utils/money";

export default ()=>{

  const [refundModalVisible, setRefundModalVisible] = useState(false)
  const [refundModalValue, setRefundModalValue] = useState()
  
  const actionRef = useRef();

  let defaultData
  const sessionData = sessionStorage.getItem("orderList");
  if(sessionData){
    defaultData = JSON.parse(sessionData)
  }else{
    defaultData = []
  }

  async function commitRefund(values){
      try{
        await refundDrug(values)
        setRefundModalValue(null)
        setRefundModalVisible(false)
        message.success('提交成功')
      }catch(e){
        message.error(e.message, 3)
      }finally{
        actionRef.current.reload()
      }
  }

  //打开退货modal
  const handleRefund = async (order) =>{
      const hide = message.loading('加载中')
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
    }, 1500);
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
        actionRef.current.reload();
    }
  };

  const columns = [
    {
      title: '处方号',
      dataIndex: 'prescriptionno',
      fieldProps: {
        onKeyUp: (e)=>{
          var keycode = window.event?e.keyCode:e.which;
          if(keycode==13){//回车
              actionRef.current.reload();
          }
        }
    }
    },
    {
      title: '患者登记号',
      dataIndex: 'regNo',
      order:2,
      fieldProps: {
        onKeyUp: (e)=>{
          var keycode = window.event?e.keyCode:e.which;
          if(keycode==13){//回车
              actionRef.current.reload();
          }
        }
    }
    },
    {
      title: '就诊卡号',
      dataIndex: 'cardno',
      hideInTable: true,
    },
    {
      title: '订单号',
      dataIndex: 'orderno',
      hideInTable: true,
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
      render: (_,record)=> (<span>{regFenToYuan(record.amount)}元</span>)
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
                text: '异地医保',
            },
            5: {
                text: '现金',
            }
        }
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        search: false,
    },
    {
        title: '失效时间',
        dataIndex: 'invalidTime',
        search: false,
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
      title:'创建时间',
      dataIndex:'dateRange',
      hideInTable:true,
      valueType:"dateRange"
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const {state, orderno, amount, payway} = record
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
        defaultData={defaultData}
        rowKey="key"
        request={async (params) => {
            try{
                if(params.dateRange && params.dateRange.length>1){
                params.startTime = params.dateRange + " 00:00:00";
                params.endTime = params.dateRange + " 23:59:59";
                }
                const response = await queryOrder(params)
                sessionStorage.setItem("orderList", JSON.stringify(response.data))
                return response
            }catch(e){
                message.error(e.message, 3)
            }
          }
        }
        columns={columns}
        manualRequest={true}
      />
      {
          refundModalValue && (
            <RefundModal 
            handleCommit={commitRefund}
            handleCancel={()=>setRefundModalVisible(false)}
            visible={refundModalVisible}
            values={refundModalValue}>
            </RefundModal>
          )
      }
    </PageContainer>
  );

}
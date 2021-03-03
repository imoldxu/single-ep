import { message, Popconfirm, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryOrder, deliver, refundDrug, yidiYibaoOver, yibaoOver } from '@/services/ant-design-pro/order';
import { querySaleRecord } from '@/services/ant-design-pro/saleRecord';
import RefundModal from './refundModal'
import PayModal from './payModal'
import { regFenToYuan } from "@/utils/money";

export default ()=>{

  const [refundModalVisible, setRefundModalVisible] = useState(false)
  const [refundModalValue, setRefundModalValue] = useState()
  const [payModalVisible, setPayModalVisible] = useState(false)
  const [payModalValue, setPayModalValue] = useState({amount:0})

  const actionRef = useRef();

  async function commitRefund(values){
      try{
        await refundDrug(values)
        setRefundModalValue(null)
        setRefundModalVisible(false)
      }catch(e){
        message.error(e.message, 3)
      }finally{
        actionRef.current.reload()
      }
  }

  const handleYidiYibaoOver = async (orderno) => {
    const hide = message.loading('医保支付确认提交中')
    try{
        await yidiYibaoOver({orderno:orderno})
        message.success("提交成功", 3)
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
    setPayModalVisible(false)
    setPayModalValue({amount:0})
    actionRef.current.reload();
  };

  const handleYibaoOver = async (orderno) => {
    const hide = message.loading('医保支付确认提交中')
    try{
        await yibaoOver({orderno:orderno})
        message.success("提交成功", 3)
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
    setPayModalVisible(false)
    setPayModalValue({amount:0})
    actionRef.current.reload();
  };

  const handleOfflineRefund = async (orderno) => {
    const hide = message.loading('退款提交中')
    try{
        await offlineRefund({orderno:orderno})
        message.success("退款成功", 3)
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
    actionRef.current.reload();
  };

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
        if(state === 1){
          return (
          <Space>
              <a onClick={()=>{
                  setPayModalValue({orderno: orderno,amount:amount})
                  setPayModalVisible(true)
              }}>
                  确认缴费方式
              </a>
          </Space>)
        } else if (state === 2){
          return (
            <Space>
              <a
                onClick={() => {
                    handleDeliver(record.orderno, record.prescriptionid);
                }}
              >
                确认领药
              </a>
              {
                (payway === 3 || payway ===4) && (
                  <Popconfirm
                        title="确认是否要退款?"
                        okText="确认"
                        cancelText="取消"
                        onConfirm={() => {
                            handleOfflineRefund(record.orderno);
                        }}>
                        <a>
                            确认退款
                        </a>
                  </Popconfirm>
                )
              }
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
        } else if(state === 4 && (payway === 3 || payway ===4)){
            return (
              <Popconfirm
                  title="确认是否要退款?"
                  okText="确认"
                  cancelText="取消"
                  onConfirm={() => {
                      handleOfflineRefund(record.orderno);
                  }}>
                  <a>
                      确认退款
                  </a>
              </Popconfirm>
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
        request={async (params) => {
            try{
                if(params.dateRange && params.dateRange.length>1){
                params.startTime = params.dateRange + " 00:00:00";
                params.endTime = params.dateRange + " 23:59:59";
                }
                return await queryOrder(params)
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
      <PayModal key="modal"
        handleYibaoPay={handleYibaoOver}
        handleYidiYibaoPay={handleYidiYibaoOver}
        handleCancel={()=>{
            setPayModalVisible(false)
            setPayModalValue({amount:0})
        }}
        visible={payModalVisible}
        values={payModalValue}
      ></PayModal>
    </PageContainer>
  );

}
import { message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryPatientOrder, cashOver, cashRefund } from '@/services/ant-design-pro/order';
import { Loading3QuartersOutlined } from "@ant-design/icons";

export default ()=>{

  const actionRef = useRef();

  const handleCashOver = async (orderno) => {
    const hide = message.loading('支付确认提交中')
    try{
        await cashOver({orderno:orderno})
    }catch(e){
        message.error(e.msg, 3)
    }finally{
        hide()
    }
    actionRef.current.reload();
  };

  const handleCashRefund = async (orderno) => {
    const hide = message.loading('退款提交中')
    try{
        await cashRefund({orderno:orderno})
    }catch(e){
        message.error(e.msg, 3)
    }finally{
        hide()
    }
    actionRef.current.reload();
  };

  const columns = [
        
        {
          title: '登记号',
          dataIndex: 'regNo',
        },
        {
            title: '处方号',
            dataIndex: 'prescriptionno',
        },
        {
          title: '患者姓名',
          dataIndex: 'patientname',
          search: false,
        },
        {
          title: '医生姓名',
          dataIndex: 'doctorname',
          search: false,
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
            search: false,
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
            search: false,
            valueEnum: {
                1: {
                    text: '待支付',
                    status: 'Error',
                },
                2: {
                    text: '已支付',
                    status: 'Success',
                }, 
                3: {
                    text: '已领药',
                    status: 'Success',
                },
                4: {
                    text: '待退款',
                    status: 'Error',
                },
                5: {
                    text: '已退款',
                    status: 'Success',
                }
            }
        },
        {
          title: '开具时间',
          dataIndex: 'createTime',
          search: false,
        },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const {state, payway} = record
        if (state === 1){
            return (
                <Space>
                  <a
                    onClick={() => {
                        handleCashOver(record.orderno);
                    }}
                  >
                    确认已缴费
                  </a>
                </Space>
            )
        }else if((state === 2 || state === 4) && payway === 5){//cash
            return (
                <Space>
                  <a
                    onClick={() => {
                        handleCashRefund(record.orderno);
                    }}
                  >
                    确认退款
                  </a>
                </Space>
            )
        }
        },
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="缴/退费订单"
        actionRef={actionRef}
        rowKey="key"
        request={(params) => {  
            return queryPatientOrder(params)
          }
        }
        columns={columns}
        manualRequest={true}
      />
    </PageContainer>
  );

}
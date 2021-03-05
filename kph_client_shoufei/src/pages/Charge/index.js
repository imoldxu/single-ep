import { message, Popconfirm, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryPatientOrder, cashOver, offlineRefund, yibaoOver, yidiYibaoOver } from '@/services/ant-design-pro/order';
import { Loading3QuartersOutlined } from "@ant-design/icons";
import { regFenToYuan } from "@/utils/money";
import PayModal from "./payModal";

export default ()=>{

  const actionRef = useRef();

  const [modalVisible, setModalVisible] = useState(false)
  const [modalValue, setModalValue] = useState({amount:0})

  const handleCashOver = async (orderno) => {
    const hide = message.loading('现金支付确认提交中')
    try{
        await cashOver({orderno:orderno})
        message.success("提交成功", 3)    
        actionRef.current.reload();
        setModalVisible(false)
        setModalValue({amount:0})
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
  };

  const handleYidiYibaoOver = async (orderno) => {
    const hide = message.loading('异地医保支付确认提交中')
    try{
        await yidiYibaoOver({orderno:orderno})
        message.success("提交成功", 3)
        setModalVisible(false)
        setModalValue({amount:0})
        actionRef.current.reload();
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
  };

  const handleYibaoOver = async (orderno) => {
    const hide = message.loading('医保支付确认提交中')
    try{
        await yibaoOver({orderno:orderno})
        message.success("提交成功", 3)
        setModalVisible(false)
        setModalValue({amount:0})
        actionRef.current.reload();
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
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
            order: 2,
            hideInTable: true,
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
            order: 1,
            hideInTable: true,
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
          render: (_,record)=> (<span>{regFenToYuan(record.amount)}元</span>)
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
                    text: '异地医保',
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
          title: '创建时间',
          dataIndex: 'createTime',
          search: false,
        },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const {orderno, state, payway, amount} = record
        if (state === 1){
            return (
                <Space>
                    <a onClick={()=>{
                        setModalValue({orderno: orderno,amount:amount})
                        setModalVisible(true)
                    }}>
                        确认缴费方式
                    </a>
                </Space>
                // <Space size="large">
                //      <Popconfirm
                //         title={`确认是否已收现金：${regFenToYuan(record.amount)}元?`}
                //         okText="确认"
                //         cancelText="取消"
                //         onConfirm={() => {
                //             handleCashOver(record.orderno);
                //         }}>
                //         <a>
                //             现金缴费
                //         </a>
                //     </Popconfirm>
                // </Space>    
                //     <Popconfirm
                //         title="确认是否市医保已收费?"
                //         okText="确认"
                //         cancelText="取消"
                //         onConfirm={() => {
                //             handleYibaoOver(record.orderno);
                //         }}>
                //         <a>
                //             市医保缴费
                //         </a>
                //     </Popconfirm>
                //     <Popconfirm
                //         title="确认是否异地医保已收费?"
                //         okText="确认"
                //         cancelText="取消"
                //         onConfirm={() => {
                //             handleYidiYibaoOver(record.orderno);
                //         }}>
                //         <a>
                //             市医保缴费
                //         </a>
                //     </Popconfirm>
            )
        }else if((state == 2 || state == 4) && ( payway== 3 || payway == 4 || payway == 5)){//cash shiyibao yidiyibao
            return (
                <Space>
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
        rowKey="rowKey"
        request={async (params) => {  
            try{
                return await queryPatientOrder(params)
            }catch(e){
                message.error(e.message, 3)
            }
          }
        }
        columns={columns}
        manualRequest={true}
      />
      <PayModal key="modal"
        handleCashPay={handleCashOver}
        handleYibaoPay={handleYibaoOver}
        handleYidiYibaoPay={handleYidiYibaoOver}
        handleCancel={()=>{
            setModalVisible(false)
            setModalValue({amount:0})
        }}
        visible={modalVisible}
        values={modalValue}
      ></PayModal>
    </PageContainer>
  );

}
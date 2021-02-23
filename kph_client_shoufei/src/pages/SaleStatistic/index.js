import { message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { statistic } from '@/services/ant-design-pro/saleRecord';
import { regFenToYuan } from "@/utils/money";

export default ()=>{

  const actionRef = useRef();

  const handleRefund = async (orderno) => {
    const hide = message.loading('退货中')
    try{
        
    }catch(e){
        message.error(e.message, 3)
    }finally{
        hide()
    }
    actionRef.current.reload();
  };

  const columns = [
    {
      title:'时间范围',
      dataIndex:'dateRange',
      hideInTable:true,
      formItemProps: {
        rules: [
          {
            required: true,
            message: '此项为必填项',
          },
        ],
      },
      valueType:"dateRange"
    },
    {
      title: '药品名称',
      dataIndex: 'drugname',
    },
    {
      title: '厂商',
      dataIndex: 'drugcompany',
    },
    {
      title: '规格',
      dataIndex: 'standard',
      search: false,
    },
    {
      title: '医生姓名',
      dataIndex: 'doctorname',
    },
    {
      title: '科室',
      dataIndex: 'department',
    },   
    {
      title: '销售金额',
      dataIndex: 'totalPrice',
      search: false,
      render: (_,record)=> (<span>{regFenToYuan(record.totalPrice)}元</span>)
    },
    {
        title: '销售数量',
        dataIndex: 'totalSale',
        search: false,
    },
    
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="售药统计"
        actionRef={actionRef}
        rowKey="key"
        form={{
          ignoreRules: false,
        }}
        request={async (params) => {
            try{
                if(params.dateRange && params.dateRange.length>1){
                  params.startTime = params.dateRange[0] + " 00:00:00";
                  params.endTime = params.dateRange[1] + " 23:59:59";
                }else{
                  return Promise.reject()
                }
                return await statistic(params)
            }catch(e){
                message.error(e.message, 3)
            }
          }
        }
        columns={columns}
        manualRequest={true}
      />
    </PageContainer>
  );

}
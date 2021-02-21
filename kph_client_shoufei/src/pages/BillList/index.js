import { Space, Statistic } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryBill } from '@/services/ant-design-pro/bill';

export default ()=>{

  const actionRef = useRef();

  const columns = [
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
        title: '支付流水号',
        dataIndex: 'payid',
    },
    {
        title: '账单金额',
        dataIndex: 'amount',
        search: false,
        render: (_,record)=> {
          if(record.type===1){  
            return (
              <span style={{color:"green"}}>{record.amount/100}元</span>
              )
          }else{
              return (
                  <span style={{color:"red"}}>{0-record.amount/100}元</span>
              )  
          }
          }
    },
    {
        title: '关联订单号',
        dataIndex: 'orderno',
    },
    {
      title: '账单时间',
      dataIndex: 'createtime',
      search: false,
    },
    {
      title:'时间范围',
      dataIndex:'dateRange',
      hideInTable:true,
      valueType:"dateRange"
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="账单明细"
        actionRef={actionRef}
        rowKey="key"
        request={(params) => {
            if(params.dateRange){
              params.startTime = params.dateRange[0].format("yyyy-MM-DD") + "00:00:00";
              params.endTime = params.dateRange[1].format("yyyy-MM-DD") + "23:59:59";
            }
            return queryBill(params)
          }
        }
        columns={columns}
        manualRequest={true}
      />
    </PageContainer>
  );

}
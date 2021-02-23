import { message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { querySaleRecord } from '@/services/ant-design-pro/saleRecord';
import { regFenToYuan } from "@/utils/money";

export default ()=>{

  const actionRef = useRef();

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
        title: '医生姓名',
        dataIndex: 'doctorname',
    },
    {
        title: '科室',
        dataIndex: 'department',
    },
    {
      title: '药品名称',
      dataIndex: 'drugname',
    },
    {
      title: '规格',
      dataIndex: 'standard',
    },
    {
      title: '厂商',
      dataIndex: 'drugcompany',
    },
    {
      title: '单价',
      dataIndex: 'price',
      search: false,
      render: (_,record)=> (<span>{regFenToYuan(record.price)}元</span>)
    },
    {
        title: '数量',
        dataIndex: 'num',
        search: false,
    },
    {
        title: '退货数量',
        dataIndex: 'refundnum',
        search: false,
      },
    {
      title: '领药时间',
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
        headerTitle="售药记录"
        actionRef={actionRef}
        rowKey="key"
        request={async (params) => {
            try{
                if(params.dateRange && params.dateRange.length>1){
                params.startTime = params.dateRange[0] + " 00:00:00";
                params.endTime = params.dateRange[1] + " 23:59:59";
                }
                return await querySaleRecord(params)
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
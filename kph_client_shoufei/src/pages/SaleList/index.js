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
      ellipsis: true,
    },
    {
      title: '药品编号',
      dataIndex: 'drugno',
      ellipsis: true,
    },
    {
      title: '药品名称',
      dataIndex: 'drugname',
      ellipsis: true,
      search: false,
    },
    {
      title: '规格',
      dataIndex: 'standard',
      ellipsis: true,
      search: false,
    },
    {
      title: '厂商',
      dataIndex: 'drugcompany',
      ellipsis: true,
      search: false,
    },
    // {
    //   title: '医生姓名',
    //   dataIndex: 'doctorname',
    //   ellipsis: true,
    //   search: false,
    // },
    // {
    //   title: '科室',
    //   dataIndex: 'department',
    //   ellipsis: true,
    //   search: false,
    // },
    {
      title: '单价',
      dataIndex: 'price',
      search: false,
      render: (_,record)=> (<span>{regFenToYuan(record.price)}元</span>)
    },
    {
        title: '销售数量',
        dataIndex: 'num',
        search: false,
    },
    {
        title: '退货数量',
        dataIndex: 'refundnum',
        search: false,
      },
      {
        title: '实际销量',
        dataIndex: 'realnum',
        search: false,
        render: (_, record)=> (<span>{record.num - record.refundnum}</span>)
      },
    {
      title: '领药时间',
      dataIndex: 'createtime',
      search: false,
      ellipsis: true,
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
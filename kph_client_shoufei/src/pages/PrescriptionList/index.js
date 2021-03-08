import { message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { queryPrescription } from '@/services/ant-design-pro/prescription';

export default ()=>{

  const actionRef = useRef();

  let defaultData
  const sessionData = sessionStorage.getItem("prescriptionList");
  if(sessionData){
    defaultData = JSON.parse(sessionData)
  }else{
    defaultData = []
  }

  /**
   *  审核节点
   * @param selectedRows
   */
  const gotoPrint = async (pid) => {
    if (!history) return;
    setTimeout(() => {
      history.push('/print?id='+pid);
    }, 10);
  };

  const columns = [
    {
      title: '处方号',
      dataIndex: 'prescriptionno',
    },
    {
      title: '登记号',
      dataIndex: 'regNo',
      hideInTable: true,
    },
    {
      title: '卡号',
      dataIndex: 'cardNo',
      hideInTable: true,
    },
    {
      title: '患者姓名',
      dataIndex: 'patientname',
    },
    {
      title: '患者性别',
      dataIndex: 'patientsex',
      search: false,
    },
    {
      title: '年龄',
      dataIndex: 'patientage',
      search: false,
    },
    {
      title: '医生姓名',
      dataIndex: 'doctorname',
    },
    {
      title: '部门',
      dataIndex: 'department',
    },
    {
      title: '开具时间',
      dataIndex: 'createdate',
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
      render: (_, record) => (
        <Space>
          <a
            onClick={() => {
              gotoPrint(record.id);
            }}
          >
            详情
          </a>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="处方列表"
        actionRef={actionRef}
        defaultData={defaultData}
        rowKey="key"
        manualRequest={true}
        request={async(params) => {
          try{
            if(params.dateRange && params.dateRange.length>1){
              params.startdate = params.dateRange[0];
              params.enddate = params.dateRange[1];
            }  
            const response = await queryPrescription(params);
            sessionStorage.setItem("prescriptionList", JSON.stringify(response.data))
            return response;
            }catch(e){
              message.error(e.message, 3)
            }
          }
        }
        columns={columns}
      />
    </PageContainer>
  );

}
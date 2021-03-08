import { Button, message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { downloadStatistics, statistic } from '@/services/ant-design-pro/saleRecord';
import { regFenToYuan } from "@/utils/money";
import { DownloadOutlined } from "@ant-design/icons";

export default ()=>{

  const actionRef = useRef();
  const formRef = useRef();

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
      title: '厂商',
      dataIndex: 'drugcompany',
      ellipsis: true,
      search: false,
    },
    {
      title: '规格',
      dataIndex: 'standard',
      search: false,
      ellipsis: true,
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
        formRef={formRef}
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
        toolBarRender={() => [
          <Button
              type="primary"
              key="new"
              onClick={() => {
                  formRef.current
                  .validateFields()
                  .then(async params => {

                    if(params.dateRange && params.dateRange.length>1){

                      const payload = {
                        drugno: params.drugno,
                        startTime:  params.dateRange[0].format('YYYY-MM-DD') + " 00:00:00",
                        endTime: params.dateRange[1].format('YYYY-MM-DD') + " 23:59:59",
                        doctorname: params.doctorname,
                        department: params.department
                      }
                      try{
                        const rs = await downloadStatistics(payload)
                        //接收到后端的数据流以blob的方式创建一个a标签自动触发下载
                        const blob = new Blob([rs]) //, { type: 'text/plain' })
                        const fileName = "销售统计报表.xlsx";
                        if ('download' in document.createElement('a')) { // 非IE下载
                            const elink = document.createElement('a');
                            elink.download = fileName;
                            elink.style.display = 'none';
                            elink.href = URL.createObjectURL(blob);
                            document.body.appendChild(elink);
                            elink.click();
                            URL.revokeObjectURL(elink.href);// 释放 URL对象
                            document.body.removeChild(elink);
                        } else { // IE10+下载
                            navigator.msSaveBlob(blob, fileName)
                        }
                      }catch(e){
                        message.error(e.message)
                      }
                    }else{
                      message.error('必须选择一个时间范围')
                    }
                  })
                  .catch(e => {
                      console.log(e)
                      message.error('必须选择一个时间范围')
                  });
              }}
          >
              <DownloadOutlined />下载报表
          </Button>,
        ]}
      />
    </PageContainer>
  );

}
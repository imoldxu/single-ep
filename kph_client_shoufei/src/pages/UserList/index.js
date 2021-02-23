import { Button, message, Space } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { register, validUser, invalidUser, queryUser, resetPassword } from '@/services/ant-design-pro/users';
import { regFenToYuan } from "@/utils/money";
import UserModal from "./userModal";
import { PlusOutlined } from "@ant-design/icons";

export default ()=>{

  const [modalVisible, setModalVisible] = useState(false);
  
  const actionRef = useRef();

  const columns = [
    {
      title: '用户名',
      dataIndex: 'name',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
    },
    {
      title: '角色',
      dataIndex: 'role',
      render: (_, record) => {
        const {name} = record.roles[0]
        if(name === 'admin'){
          return (<span>超级管理员</span>)
        } else if(name === 'manager'){
          return (<span>管理员</span>)
        } else if(name === 'tollman'){
          return (<span>收费员</span>)
        }
        return ('')
      },
      valueEnum: {
        "manager": {
          text: '管理员',
        },
        "tollman": {
          text: '收费员',
        }
      }
    },
    {
      title: '创建时间',
      dataIndex: 'createtime',
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'state',
      valueEnum: {
        1: {
            text: '正常',
            status: 'Success',
        },
        0: {
            text: '停用',
            status: 'Warning',
        }, 
    }
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="large">
          <a
            onClick={async () => {
              const hide = message.loading('重置密码中')
              try{
                await resetPassword({phone: record.phone});
                message.success('重置密码成功',3)
              }catch(e){
                message.error(e.message,3)
              }finally{
                hide()
              }
            }}
          >
            重置密码
          </a>
          {
            record.state == 1 ? (
              <a
                onClick={async () => {
                  const hide = message.loading('处理中')
                  try{
                    invalidUser({phone: record.phone});
                    actionRef.current.reload()
                  }catch(e){
                    message.error(e.message,3)
                  }finally{
                    hide()
                  }
                }}
              >
                停用
              </a>
            ):(
              <a
                onClick={async() => {
                  const hide = message.loading('提交中')
                  try{
                    await validUser({phone: record.phone});
                    actionRef.current.reload()
                  }catch(e){
                    message.error(e.message,3)
                  }finally{
                    hide()
                  }
                }}
              >
                启用
              </a>
            )
          }
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="用户列表"
        actionRef={actionRef}
        rowKey="key"
        request={async (params) => {
            try{
                return await queryUser(params)
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
                  setModalVisible(true);
              }}
          >
              <PlusOutlined />新建
          </Button>,
      ]}
      />
      <UserModal
        handleCommit={async (values)=>{
          await register(values)
          setModalVisible(false)
        }}
        handleCancel={()=>{
          setModalVisible(false)
        }}
        visible={modalVisible} 
      />
    </PageContainer>
  );

}
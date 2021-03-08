import { Button, message, Space, Upload } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { addDrug, downDrug, modifyDrug, modifyStock, queryDrug, upDrug } from '@/services/ant-design-pro/drug';
import { PlusOutlined, UploadOutlined } from "@ant-design/icons";
import StockModal from './stockModal'
import { regFenToYuan, regYuanToFen } from "@/utils/money";

export default () => {

    const [infoModalVisible, handleInfoModalVisible] = useState(false);
    const [infoFormValues, setInfoFormValues] = useState({});
    
    const actionRef = useRef();

    async function handleDown(id) {
        const hide = message.loading("下架中")
        try {
            await downDrug({ drugid: id })
        } catch (e) {
            message.error(e.message)
        } finally {
            hide()
        }
        actionRef.current.reload()
    }

    async function handleUp(id) {
        const hide = message.loading("上架中")
        try {
            await upDrug({ drugid: id })
        } catch (e) {
            message.error(e.message)
        } finally {
            hide()
        }
        actionRef.current.reload()
    }

    async function handleCommit(values) {
        try {
            await modifyStock(values)
            message.success('提交成功')
            handleInfoModalVisible(false)
            actionRef.current.reload()
        } catch (e) {
            message.error(e.message)
        }
    }

    const columns = [
        {
            title: '药品名称',
            dataIndex: 'keys',
            hideInTable: true,
            fieldProps:{
                placeholder: "请输入药品名称或简称的拼音首字母"
            }
        },
        {
            title: '药品编号',
            dataIndex: 'drugno',
            ellipsis: true,
        },
        {
            title: '药品名称',
            dataIndex: 'drugname',
            search: false,
            ellipsis: true,
        },
        {
            title: '药品规格',
            dataIndex: 'standard',
            search: false,
            colSize: 2,
            ellipsis: true,
        },
        {
            title: '剂型',
            dataIndex: 'form',
            search: false,
        },
        {
            title: '厂商',
            dataIndex: 'company',
            search: false,
            ellipsis: true,
        },
        {
            title: '分类',
            dataIndex: 'category',
            search: false,
            ellipsis: true,
        },
        {
            title: '子类',
            dataIndex: 'subcategory',
            search: false,
            ellipsis: true,
        },
        {
            title: '单价',
            dataIndex: 'price',
            search: false,
            render: (_, record) => { return (<span>{`${regFenToYuan(record.price)}元/${record.unit}`}</span>) }
        },
        {
            title: '库存',
            dataIndex: 'stock',
            search: false,
        },
        {
            title: '状态',
            dataIndex: 'state',
            valueEnum: {
                0: {
                    text: '停售',
                    status: 'Error',
                },
                1: {
                    text: '在售',
                    status: 'Success',
                }
            }
        },
        {
            title: '操作',
            dataIndex: 'option',
            valueType: 'option',
            colSize: 2,
            render: (_, record) => (
                <Space>
                    <a
                        onClick={() => {
                            setInfoFormValues(record)
                            handleInfoModalVisible(true)
                        }}
                    >
                        修改库存
          </a>
                    {/* {
                        record.state === 1 ?
                            (<a
                                onClick={() => {
                                    handleDown(record.id);
                                }}
                            >
                                下架
                            </a>) :
                            (<a
                                onClick={() => {
                                    handleUp(record.id);
                                }}
                            >
                                上架
                            </a>)
                    } */}
                </Space>

            ),
        },
    ];

    return (
        <PageContainer>
            <ProTable
                headerTitle="药品清单"
                actionRef={actionRef}
                rowKey="key"
                request={async (params) => {
                    try{
                        return await queryDrug(params)
                    }catch(e){
                        message.error(e.message, 3)
                    }
                }
                }
                columns={columns}
            />
            <StockModal
                handleCommit={handleCommit}
                handleInfoModalVisible={() => handleInfoModalVisible(false)}
                visible={infoModalVisible}
                values={infoFormValues}
            ></StockModal>
        </PageContainer>
    );

}
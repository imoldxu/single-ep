import { Button, message, Space, Upload } from "antd";
import { useRef, useState } from "react";
import { PageContainer, FooterToolbar } from '@ant-design/pro-layout';
//import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import { addDrug, downDrug, modifyDrug, queryDrug, upDrug } from '@/services/ant-design-pro/drug';
import { PlusOutlined, UploadOutlined } from "@ant-design/icons";
import DrugModal from './drugModal'
import { regFenToYuan, regYuanToFen } from "@/utils/money";

export default () => {

    const [infoModalVisible, handleInfoModalVisible] = useState(false);
    const [infoFormValues, setInfoFormValues] = useState({});
    const [uploadFile, setUploadFile] = useState([]);

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
            if (values.id) {
                await modifyDrug(values)
            } else {
                await addDrug(values)
            }
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
            title: '药品名称',
            dataIndex: 'drugname',
            search: false,
            ellipsis: true,
            colSize: 2,
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
        },
        {
            title: '分类',
            dataIndex: 'category',
            search: false,
        },
        {
            title: '子类',
            dataIndex: 'subcategory',
            search: false,
        },
        {
            title: '单价',
            dataIndex: 'price',
            search: false,
            render: (_, record) => { return (<span>{`${regFenToYuan(record.price)}元/${record.unit}`}</span>) }
        },
        {
            title: '状态',
            dataIndex: 'state',
            valueEnum: {
                0: {
                    text: '无货',
                    status: 'Error',
                },
                1: {
                    text: '有货',
                    status: 'Success',
                }
            }
        },
        {
            title: '操作',
            dataIndex: 'option',
            valueType: 'option',
            render: (_, record) => (
                <Space>
                    <a
                        onClick={() => {
                            setInfoFormValues(record)
                            handleInfoModalVisible(true)
                        }}
                    >
                        修改
          </a>
                    {
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
                    }
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
                toolBarRender={() => [
                    <Upload
                        name="file"
                        fileList={uploadFile}
                        action='/api/drug/uploadByExcel'
                        onChange={(info) => {
                            if (info.file.status !== 'uploading') {
                                console.log(info.file, info.fileList);
                            }
                            if (info.file.status === 'done') {
                                setUploadFile([])
                                message.success(`${info.file.name} 上传成功`);
                                actionRef.current.reload()
                            } else if (info.file.status === 'error') {
                                message.error(`${info.file.name} 上传失败`);
                            }
                        }}
                    >
                        <Button type="primary" key="upload">
                            <UploadOutlined /> <FormattedMessage id="pages.searchTable.upload" defaultMessage="上传" />
                        </Button>
                    </Upload>,
                    <Button
                        type="primary"
                        key="new"
                        onClick={() => {
                            setInfoFormValues({})
                            handleInfoModalVisible(true);
                        }}
                    >
                        <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="新建" />
                    </Button>,
                ]}
            />
            <DrugModal
                handleCommit={handleCommit}
                handleInfoModalVisible={() => handleInfoModalVisible(false)}
                visible={infoModalVisible}
                values={infoFormValues}
            ></DrugModal>
        </PageContainer>
    );

}
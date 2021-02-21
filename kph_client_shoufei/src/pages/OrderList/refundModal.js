import { checkTwoPointNum, regFenToYuan } from '@/utils/money';
import { Button, Col, Descriptions, Divider, Input, Modal, Row, Select, Space } from 'antd';
import Form from 'antd/lib/form/Form';
import FormItem from 'antd/lib/form/FormItem';
import React, { useRef, useState } from "react";

const { Option } = Select;

const layout = {
    labelCol: {
        span: 6,
    },
    wrapperCol: {
        span: 18,
    },
}

const RefundModal = (props) => {
    const {
        handleCommit,
        handleCancel,
        visible,
        values,
    } = props;

    const formRef = useRef()
    const [isLoading, setLoading] = useState(false)

    const { order, records } = values;

    const handleOk = () => {

        formRef.current
            .validateFields()
            .then(values => {

                let array = []

                for(let key in values){
                    if(values[key]){//只取有值的
                        array.push({ recordid: key, toRefund: values[key]})
                    }
                }

                let payload = {
                    orderid: order.id,
                    refundRecords: array,
                }

                if (handleCommit) {
                    setLoading(true)
                    try{
                        handleCommit(payload)
                    }finally{
                        setLoading(false)
                    }
                }
            })
            .catch(info => {
                console.log('校验失败:', info);
            });
    }


    return (
        <Modal
            width={800}
            destroyOnClose={true}
            title="退回药品"
            visible={visible}
            confirmLoading={isLoading}
            onOk={handleOk}
            onCancel={handleCancel}
        >
            <Space direction="vertical">
                <Descriptions column={3}>
                    <Descriptions.Item label="处方号">{order.prescriptionno}</Descriptions.Item>
                    <Descriptions.Item label="登记号">{order.regNo}</Descriptions.Item>
                    <Descriptions.Item label="患者姓名">{order.patientname}</Descriptions.Item>
                </Descriptions>
                <Divider>药品清单</Divider>
                <Form {...layout}
                    ref={formRef}
                    name="refundDrug"
                    preserve={false}>
                    {
                        records.map((record, index)=>{
                            return (
                            <Row gutter={16} key={`record${index}`}>
                                <Col span={8}><span style={{lineHeight:"32px"}}>{record.drugname+" "+record.standard}</span></Col>
                                <Col span={3}><span style={{lineHeight:"32px"}}>{"单价:"+record.price/100+"元"}</span></Col>
                                <Col span={4}><span style={{lineHeight:"32px"}}>可退数量:</span><span style={{color:"red",lineHeight:"32px"}}>{(record.num-record.refundnum)+record.drugunit}</span></Col>
                                <Col span={9}>
                                    <FormItem name={record.id} label="退药数量"
                                        rules={[{validator: (_, value) => {
                                                if(value){
                                                    return value % 1 === 0 ?
                                                        Promise.resolve() :  Promise.reject('请输入正确的数量')
                                                }else{
                                                    return Promise.resolve()
                                                }
                                            }
                                        }]}
                                    ><Input type="number"></Input></FormItem>
                                </Col>
                            </Row>)
                        })
                    }
                </Form>
            </Space>
        </Modal>
    );
};

export default RefundModal;

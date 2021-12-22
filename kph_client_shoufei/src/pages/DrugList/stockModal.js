import { checkTwoPointNum, regFenToYuan, regYuanToFen } from '@/utils/money';
import { Button, Col, Descriptions, Divider, Input, message, Modal, Row, Select } from 'antd';
import Form from 'antd/lib/form/Form';
import FormItem from 'antd/lib/form/FormItem';
import React, { useRef, useState } from "react";
import { useAccess } from 'umi';

const { Option } = Select;

const layout = {
    labelCol: {
        span: 4,
    },
    wrapperCol: {
        span: 20,
    },
}

const StockModal = (props) => {
    const {
        handleCommit,
        handleInfoModalVisible,
        visible,
        values,
    } = props;

    const formRef = useRef()
    const [isLoading, setLoading] = useState(false)

    const { id, stock } = values;
    
    const onOk = () => {

        formRef.current
            .validateFields()
            .then(values => {

                if (handleCommit) {
                    setLoading(true)
                    try{
                        handleCommit(values)
                    } catch(e){
                        message.error(e.message, 3)
                    } finally{
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
            destroyOnClose={true}
            title="调整库存"
            visible={visible}
            onOk={onOk}
            confirmLoading={isLoading}
            onCancel={handleInfoModalVisible}
        >
            <Form {...layout}
                ref={formRef}
                name="modifyStock"
                initialValues={{ id:id }}
                preserve={false}>
                {
                    id ? (<FormItem name="id" noStyle>
                        <Input type="hidden"></Input>
                    </FormItem>) : ('')
                }
                <FormItem name="stock" label="库存"
                    rules={[{ required: true, validator: (_, value) => {
                            if(value){
                                return value % 1 === 0 ?
                                Promise.resolve() :  Promise.reject('请输入正确的数量')
                            }else{
                                return Promise.resolve()
                            }
                        }
                    }]}                      
                    hasFeedback>
                    <Input placeholder="请输入当前库存的数量"></Input>
                </FormItem>
            </Form>
        </Modal>
    );
};

export default StockModal;
